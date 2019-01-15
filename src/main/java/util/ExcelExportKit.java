package util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * 利用开源组件POI动态导出EXCEL文档
 * 应用泛型，代表任意一个符合javabean风格的类
 */
public class ExcelExportKit<T> {
	
	private static final String PATTERN="yyyy-MM-dd";
	/**
	 * 利用了JAVA的反射机制,没有做关联的单表导出
	 * @param title 表格sheet名
	 * @param headers 表格属性列名汉字数组
	 * @param columns 表格属性数据库字段数组
	 * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。
	 * 此方法支持的 javabean属性的数据类型有基本数据类型及String,Date
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 */
	public SXSSFWorkbook exportSingleExcel(String title, String[] headers, String[] columns, Collection<T> dataset, String pattern) {
		if(StrKit.isBlank(pattern)) {
			pattern=PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		SXSSFWorkbook workbook = new SXSSFWorkbook(500);
	    workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式(加粗标题)
		CellStyle titlestyle = workbook.createCellStyle();
		Font titlefont = workbook.createFont();
		titlefont.setFontHeightInPoints((short) 12);
		titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titlestyle.setFont(titlefont);
		// 生成并设置另一个样式(普通样式)
		CellStyle cellstyle = workbook.createCellStyle();
		Font cellfont = workbook.createFont();
		cellfont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellstyle.setFont(cellfont);
		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(titlestyle);
			cell.setCellValue(headers[i]);
		}
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			int columnIndex=0;
			for (short i = 0; i < columns.length; i++) {
				String fieldName = columns[i];
				if(fieldName.indexOf("@@")!=-1){
					String[] array=fieldName.split("@@");
					String getMethodName = "get" + array[0].substring(0, 1).toUpperCase() + array[0].substring(1);
					try {
						Class<? extends Object> tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(t, new Object[] {});
						
						String getMethodNameChild = "get" + array[1].substring(0, 1).toUpperCase() + array[1].substring(1);
						Class<? extends Object> tCls1 = value.getClass();
						Method getMethodChild = tCls1.getMethod(getMethodNameChild, new Class[] {});
						Object value1 = getMethodChild.invoke(value, new Object[] {});
						Cell cell = row.createCell(columnIndex);
						cell=this.getCell(cell, cellstyle, value1, sdf);
						columnIndex++;
						//T t1 = (T) it.next();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						// 清理资源
					}
				}else{
					try {
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						Class<? extends Object> tCls = t.getClass();
						Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
						Object value = getMethod.invoke(t, new Object[] {});
						Cell cell = row.createCell(columnIndex);
						cell=this.getCell(cell, cellstyle, value, sdf);
						columnIndex++;
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						// 清理资源
					}
				}
				
			}

		}
		return workbook;
	}
	/**
	 * 有hibernate关联关系的多表数据导出
	 * @param title 表格sheet名
	 * @param headers 表格属性列名汉字数组
	 * @param columns 表格属性数据库字段数组
	 * @param dataset 需要显示的数据集合,
	 * 此方法支持的 javabean属性的数据类型有基本数据类型及String,Date
	 * @param pattern 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd"
	 */
	public SXSSFWorkbook exportMultipleExcel(String title, String[] headers, String[] columns, List<Map<String,Object>> dataset, String pattern) {
		if(StrKit.isBlank(pattern)) {
			pattern=PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		SXSSFWorkbook workbook = new SXSSFWorkbook(500);
	    workbook.setCompressTempFiles(true);
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		
		sheet.setDefaultColumnWidth((short) 30);
		// 生成一个样式(加粗标题)
		CellStyle titlestyle = workbook.createCellStyle();
		Font titlefont = workbook.createFont();
		titlefont.setFontHeightInPoints((short) 12);
		titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titlestyle.setFont(titlefont);
		titlestyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
		//titlestyle.se
		// 生成并设置另一个样式(普通样式)
		CellStyle cellstyle = workbook.createCellStyle();
		Font cellfont = workbook.createFont();
		cellfont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		cellstyle.setFont(cellfont);
		// 产生表格标题行
		Row row = sheet.createRow(0);
		row.setHeight((short) (25 * 20)); 
		for (int i = 0,length=headers.length; i <length ; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(titlestyle);
			cell.setCellValue(headers[i]);
		}
		// 遍历集合数据，产生数据行
		for(int index=0,length=dataset.size();index<length;index++) {
			row = sheet.createRow(index+1);
			row.setHeight((short) (25 * 20)); 
			Map<String,Object> map=dataset.get(index);
			for (int i = 0,titlelength=columns.length; i <titlelength ; i++) {
				Object value=map.get(columns[i]);
				Cell cell = row.createCell(i);
				cell=this.getCell(cell, cellstyle, value, sdf);
			}
		}
		return workbook;
	}
	/**
	 * 导出多个sheet
	 */
	public HSSFWorkbook exportExcel(String title, String[] headers,
			String[] columns, Map<String,List<Map<String,Object>>> datamap, String pattern) {
		if(StrKit.isBlank(pattern)) {
			pattern=PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		HSSFWorkbook workbook = new HSSFWorkbook();
		for (Iterator<String> k = datamap.keySet().iterator(); k.hasNext();) {  
			   Object obj = k.next();  
			   List<Map<String,Object>> dataset=datamap.get(obj);
			   Sheet sheet = workbook.createSheet(String.valueOf(obj));
			   sheet.setDefaultColumnWidth((short) 30);
				// 生成一个样式(加粗标题)
				CellStyle titlestyle = workbook.createCellStyle();
				Font titlefont = workbook.createFont();
				titlefont.setFontHeightInPoints((short) 12);
				titlefont.setBoldweight(Font.BOLDWEIGHT_BOLD);
				titlestyle.setFont(titlefont);
				titlestyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
				//titlestyle.se
				// 生成并设置另一个样式(普通样式)
				CellStyle cellstyle = workbook.createCellStyle();
				Font cellfont = workbook.createFont();
				cellfont.setBoldweight(Font.BOLDWEIGHT_NORMAL);
				cellstyle.setFont(cellfont);
				cellstyle.setWrapText(true);//自动换行
				// 产生表格标题行
				Row row = sheet.createRow(0);
				//row.setHeight((short) (25 * 20)); 
				for (int i = 0,length=headers.length; i <length ; i++) {
					Cell cell = row.createCell(i);
					cell.setCellStyle(titlestyle);
					cell.setCellValue(headers[i]);
				}
				// 遍历集合数据，产生数据行
				for(int index=0,length=dataset.size();index<length;index++) {
					row = sheet.createRow(index+1);
					//row.setHeight((short) (25 * 20)); 
					Map<String,Object> map=dataset.get(index);
					for (int i = 0,titlelength=columns.length; i <titlelength ; i++) {
						Object value=map.get(columns[i]);
						Cell cell = row.createCell(i);
						cell=this.getCell(cell, cellstyle, value, sdf);
					}
				}
	    }  
		return workbook;
	}
	/**
	 * 遍历数据时候设置cell单元格内容
	 * @param cell
	 * @param cellstyle
	 * @param value
	 * @param sdf
	 * @return
	 */
	private Cell getCell(Cell cell,CellStyle cellstyle,Object value,SimpleDateFormat sdf) {
		String textValue = null;
		if (value instanceof Date) {
			Date date = (Date) value;
			textValue = sdf.format(date);
		}else {
			// 其它数据类型都当作字符串简单处理
			if(value != null) {
				textValue = value.toString();
			}else {
				textValue = "";
			}
		}
		cell.setCellStyle(cellstyle);
		Pattern p = Pattern.compile("^//d+(//.//d+)?$");
		Matcher matcher = p.matcher(textValue);
		if (matcher.matches()) {
			cell.setCellValue(Double.parseDouble(textValue));
		} else {
			cell.setCellValue(textValue);
		}
		return cell;
	}
	public static void main(String[] args) {
		OutputStream os=null;
		String[] columns = new String[] {"COURSENAME","PRICE","PERIOD","UPLOADDATE","NUM"};// 汉字表头
		String[] headers = new String[] {"课程名称","价格","学时","上传时间","销量"};// 数据库字段
		ExcelExportKit <ExcelExportKit> excelExportKit=new  ExcelExportKit<ExcelExportKit>();
		List<Map<String, Object>> dataset=new ArrayList<Map<String, Object>>();
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			SXSSFWorkbook workbook=excelExportKit.exportMultipleExcel("课程统计数据", headers, columns, dataset,null);
			String fileName=URLEncoder.encode("课程统计数据"+sdf.format(System.currentTimeMillis()), "UTF-8");
			HttpServletResponse response=null;
			response.setHeader("Content-disposition", "attachment; filename="+fileName+".xlsx");
			response.setContentType("application/msexcel;charset=utf-8");
			os = response.getOutputStream();
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}