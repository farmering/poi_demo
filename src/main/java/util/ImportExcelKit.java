package util;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel导入读取
 *
 */
public class ImportExcelKit{
	
	private final static String excel2003L =".xls";    //2003- 版本的excel
	private final static String excel2007U =".xlsx";   //2007+ 版本的excel
	
	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 * @param in,fileName
	 * @return
	 * @throws IOException 
	 * che----选择要导入的sheet页，如果che=-1，则表示读取全部sheet页内容
	 */
	public  List<List<Object>> getBankListByExcel(InputStream in,String fileName,int che) throws Exception{
		List<List<Object>> list = null;
		
		//创建Excel工作薄
		Workbook work = this.getWorkbook(in,fileName);
		if(null == work){
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;
		
		list = new ArrayList<List<Object>>();
		//遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if(sheet==null){continue;}
			
			if(che!=-1 && i!=che){continue;}
			//遍历当前sheet中的所有行sheet.getLastRowNum()：获取下标从0开始的行数；sheet.getPhysicalNumberOfRows()：获取从1开始的真实行数；数据行数读取小标从0开始
			System.out.println(sheet.getLastRowNum());
			row:
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				//此处row.getFirstCellNum()==j过滤掉第一行
				if(row==null||row.getFirstCellNum()==j){continue;}
				//遍历所有的列
				List<Object> li = new ArrayList<Object>();
				///getPhysicalNumberOfCells 是获取不为空的列个数。 
				//getLastCellNum 是获取最后一个不为空的列是第几个。 
//				System.out.println(row.getPhysicalNumberOfCells()+"-----"+row.getLastCellNum());
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					if(StrKit.isBlank(String.valueOf(this.getCellValue(cell)) )&&y==row.getFirstCellNum()){
						continue row;  	
					}
					li.add(this.getCellValue(cell));
				}
				list.add(li);
				
			}
		}
		return list;
	}
	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 * che----选择要导入的sheet页，如果che=-1，则表示读取全部sheet页内容
	 */
	public  List<List<Object>> getListByExcel(InputStream in,String fileName,int che) throws Exception{
		List<List<Object>> list = null;
		Workbook work = this.getWorkbook(in,fileName);
		if(null == work){
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		list = new ArrayList<List<Object>>();
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if(sheet==null){
				continue;
			}
			if(che!=-1 && i!=che){
				continue;
			}
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if(row==null){continue;}
				List<Object> li = new ArrayList<Object>();
				for (int y = 0; y < row.getLastCellNum(); y++) {
					li.add(this.getCellValue(row.getCell(y)));
				}
				list.add(li);
			}
		}
		return list;
	}
	/**
	 * 描述：根据文件后缀，自适应上传文件的版本 
	 * @param inStr,fileName
	 * @return
	 * @throws Exception
	 */
	public  Workbook getWorkbook(InputStream inStr,String fileName) throws Exception{
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if(excel2003L.equals(fileType)){
			wb = new HSSFWorkbook(inStr);  //2003-
		}else if(excel2007U.equals(fileType)){
			wb = new XSSFWorkbook(inStr);  //2007+
		}else{
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 * @param cell
	 * @return
	 */
	public  Object getCellValue(Cell cell){
		Object value = null;
		DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
		if(cell!=null){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				value = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if("General".equals(cell.getCellStyle().getDataFormatString())){
					value = df.format(cell.getNumericCellValue());
				}else if("yyyy/mm".equals(cell.getCellStyle().getDataFormatString())
						|| "m/d/yy".equals(cell.getCellStyle().getDataFormatString())
				        || "yy/m/d".equals(cell.getCellStyle().getDataFormatString()) 
				        || "mm/dd/yy".equals(cell.getCellStyle().getDataFormatString())
				        || "dd-mmm-yy".equals(cell.getCellStyle().getDataFormatString())
				        || "yyyy/m/d".equals(cell.getCellStyle().getDataFormatString())){
				    value = sdf.format(cell.getDateCellValue());
				}else{
					value = df.format(cell.getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;
			default:
				break;
			}
		}
		return value;
	}
}