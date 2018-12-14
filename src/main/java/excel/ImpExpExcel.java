package excel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import excel.JsonUtil.Message;

/**
 * 利用模板导出excel
 */
public class ImpExpExcel {
	
	/**
	 * 导入方法调用
	 */
	public void importExcel(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();  
			ServletFileUpload upload = new ServletFileUpload(factory);  
			JiDianLeiExcel jiDianLeiExcel=new JiDianLeiExcel();
			List<FileItem> items = upload.parseRequest(request);
			String inspectTypeId=request.getParameter("inspectTypeId")!=null?request.getParameter("inspectTypeId").toString().trim():"";
			String examineType=request.getParameter("examineType")!=null?request.getParameter("examineType").toString().trim():"";
			String seTypeId=request.getParameter("seTypeId")!=null?request.getParameter("seTypeId").toString().trim():"";
			String reportName=request.getParameter("reportName")!=null?request.getParameter("reportName").toString().trim():"";
			if(items.size()>0&&!"".equals(inspectTypeId)) {
				FileItem fi = items.get(0);  
				InputStream is=fi.getInputStream();
				if (is != null) {  
                    BufferedInputStream in = new BufferedInputStream(is);
					jiDianLeiExcel.importToDb(in,0,inspectTypeId,examineType,seTypeId,reportName);
					jiDianLeiExcel.saveIORows(inspectTypeId);
				}
			}
			JsonUtil.ajaxJsonMessage(response, "导入成功",Message.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			JsonUtil.ajaxJsonMessage(response, "导入失败",Message.ERROR);
		} 
		
	}
	/***
	 * 导出数据
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void exportExcel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String inspectTypeId=request.getParameter("inspectTypeId");
		JianYanBO jianYanBO=new JianYanBO();
		String querycondition = "SELECT * FROM "
				+ "INSPECTION_OPINION_TEMPLATE WHERE"
				+ " INSPECT_TYPE_ID='"+inspectTypeId+"' ORDER BY FIELD_ORDERROWID, FIELD_ORDERCLSID";
		Vector vec=jianYanBO.getList(querycondition);
		
		HSSFWorkbook wb = new HSSFWorkbook(); 
		
		HSSFCellStyle cellStyle = wb.createCellStyle();  
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellStyle.setWrapText(true);//指定当单元格内容显示不下时自动换行 
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//指定单元格居中对齐    
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //指定单元格垂直居中对齐  
		HSSFSheet sheet = this.createSheet(wb, inspectTypeId, vec);
		
		HSSFRow row = null;
		int lastFieldOrderrowid = 0;
		for(int i=0;i<vec.size();i++){
			Hashtable hashtable=(Hashtable)vec.get(i);
			//单元格是否跨行
			int fieldIsrowspan=Integer.parseInt(hashtable.get("FIELD_ISROWSPAN").toString());
			//单元格是否跨列
			int fieldIscolspan=Integer.parseInt(hashtable.get("FIELD_ISCOLSPAN").toString());
			//单元格占用行数
			int fieldRowspan=Integer.parseInt(hashtable.get("FIELD_ROWSPAN").toString());
			//单元格占用列数
			int fieldColspan=Integer.parseInt(hashtable.get("FIELD_COLSPAN").toString());
			//单元格行序号
			int fieldOrderrowid=Integer.parseInt(hashtable.get("FIELD_ORDERROWID").toString());
			//单元格列序号
			int fieldOrderclsid=Integer.parseInt(hashtable.get("FIELD_ORDERCLSID").toString());
			//单元格宽度倍数
			int widthnum=Integer.parseInt(hashtable.get("WIDTHNUM").toString());
			String fieldCelltype=hashtable.get("FIELD_CELLTYPE").toString();
			if(lastFieldOrderrowid==0
					||lastFieldOrderrowid!=fieldOrderrowid) {
				row = sheet.getRow(fieldOrderrowid-1);
			}
			lastFieldOrderrowid=fieldOrderrowid;
			if(!"button".equals(fieldCelltype)
					&&(fieldIsrowspan==1||fieldIscolspan==1)) {
				CellRangeAddress callRangeAddress = new CellRangeAddress(fieldOrderrowid-1,fieldOrderrowid-1+fieldRowspan-1,fieldOrderclsid-1,fieldOrderclsid-1+fieldColspan-1);
				sheet.addMergedRegion(callRangeAddress);
				this.setBorderForMergeCell(1, callRangeAddress, sheet,wb);
			}
			HSSFCell cell = row.getCell(fieldOrderclsid-1);
			sheet.setColumnWidth(fieldOrderclsid-1, widthnum);
			cell.setCellStyle(this.setCellStyle(wb));
			cell.setCellValue(hashtable.get("FIELD_NAME").toString()); 
		}
		
		String filename=URLEncoder.encode(String.valueOf(System.currentTimeMillis()), "UTF-8")+".xls";//IE浏览器
		response.reset();  
		response.setContentType("application/vnd.ms-excel");  
		response.setHeader("Content-Disposition","attachment;filename=\"" + filename + "\"");//指定下载的文件名  
		 
		response.setHeader("Pragma", "no-cache");   
		response.setHeader("Cache-Control", "no-cache"); 
		response.setDateHeader("Expires", 0);  
		OutputStream output = response.getOutputStream();  
		try {
			wb.write(output); 
			output.flush();
		}  finally{
			if(output!=null){
				output.close();
			}
		}
	}
	
	/**
	 * 创建 HSSFWorkbook
	 * @param inspectTypeId
	 * @param vec
	 */
	private HSSFSheet createSheet(HSSFWorkbook wb,String inspectTypeId,Vector vec) {
		HSSFSheet sheet = wb.createSheet(inspectTypeId);
		HSSFRow row = null;
		int lastFieldOrderrowid = 0;
		for(int i=0;i<vec.size();i++){
			Hashtable hashtable=(Hashtable)vec.get(i);
			//单元格行序号
			int fieldOrderrowid=Integer.parseInt(hashtable.get("FIELD_ORDERROWID").toString());
			//单元格列序号
			int fieldOrderclsid=Integer.parseInt(hashtable.get("FIELD_ORDERCLSID").toString());
			if(lastFieldOrderrowid==0
					||lastFieldOrderrowid!=fieldOrderrowid) {
				row = sheet.createRow(fieldOrderrowid-1);
			}
			row.createCell(fieldOrderclsid-1);
			lastFieldOrderrowid=fieldOrderrowid;
		}
		return sheet;
	}
	private  void setBorderForMergeCell(int i,
			CellRangeAddress callRangeAddress,
			HSSFSheet sheet, 
			HSSFWorkbook workBook){
		RegionUtil.setBorderBottom(1, callRangeAddress, sheet,workBook);
		RegionUtil.setBorderLeft(1, callRangeAddress, sheet,workBook);
		RegionUtil.setBorderRight(1, callRangeAddress, sheet,workBook);
		RegionUtil.setBorderTop(1, callRangeAddress, sheet,workBook);
   }
	/**
	 * 合并单元格添加边框
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle setCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();  
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		cellStyle.setWrapText(true);//指定当单元格内容显示不下时自动换行 
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//指定单元格居中对齐    
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //指定单元格垂直居中对齐  
		return cellStyle;
	}

}
