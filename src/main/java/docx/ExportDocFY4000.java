package docx;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;  
 /**
  * 报告附页中docx4j 报告添加检验结论，类别，备注
  * 
  * @author sun
  * 
  */
public class ExportDocFY4000 {  
    public static void main(String[] args) {  
    	ExportDocFY4000 exportDocFY = new ExportDocFY4000();  
        String filePath="D:\\aaa.docx"; //word附页原文件 
        String outPath="D:\\new.xlsx";//生成excel文件
        String SE_TYPE="大型游乐设施";
        String SE_TYPE_ID="0006";
        String INSPECT_TYPE="蹦极验收检验报告";
        String INSPECT_TYPE_ID="000602015";
        
        exportDocFY.wordToExcel4000(filePath,
        		outPath,
        		SE_TYPE.trim(),
        		SE_TYPE_ID.trim(),
        		INSPECT_TYPE.trim(),
        		INSPECT_TYPE_ID.trim());  
    }
    /**
     * 起重机械生成报告附页文件到excel,可生成BIG,SMALL,BIG2,BIG3 四级
     * 生成以下样例：BIG_ID取BIG前面标题数字；SMALL_ID,BIG_ID2,BIG_ID3先取前面数字，没有再继续往下加
     * 序号				检验项目及其内容					检验结果		结论			备注
  		1	2作业环境和外观检查	2.1 起重量或者起重力矩标志		${JYJL1}	${JYJL2} 	${BZ1}
		2		 			2.2 安全距离				${JYJG2}	${JYJL2}	${BZ2}
		3					2.3 起重机运行轨道			${JYJG3}	${JYJL3}	${BZ3}
		4	3金属结构检查		主要受力结构件				${JYJG4}	${JYJL4}	${BZ4}
     */
    public void wordToExcel4000(String filePath,String outPath,String SE_TYPE,
    		String SE_TYPE_ID,String INSPECT_TYPE,String INSPECT_TYPE_ID){  
    	try{  
    		List<Map<String,Object>> dataset=new ArrayList<Map<String,Object>>(); 
    		String[] columns = new String[]{
    				 "序号",
    				 "SE_TYPE",
    				 "SE_TYPE_ID",
    				 "INSPECT_TYPE",
    				 "INSPECT_TYPE_ID",
    				 "BIG",
    				 "BIG_ID",
    				 "SMALL",
    				 "SMALL_ID",
    				 "INSPECT_CONCLUSION",
    				 "MEMO",
    				 "ANNALID",
    				 "CREATE_BUP_DATE",
    				 "UPDATE_DATE",
    				 "TYPE",
    				 "BIG2",
    				 "BIG_ID2",
    				 "BIG3",
    				 "BIG_ID3",
    				 "BIG4",
    				 "BIG_ID4",
    				 "BIG5",
    				 "BIG_ID5",
    				 "BIG6",
    				 "BIG_ID6",
    				 "TEST_CONCLUSION",
    				 "TEST_RESULT"};//数据库字段属性
			 String[] headers = columns;// 汉字表头
    		 FileInputStream in = new FileInputStream(filePath);//载入文档  
	         XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息  
	         Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格  
	         int num = 1;
             //读取每一行数据  
             String preSecondcellname = "";
             String preThirdcellname = "";
             String preFouthcellname = "";
             String preFifthcellname = "";
             String pre_BIG_ID="";
             String pre_SMALL_ID="";
             String pre_BIG_ID2="";
             String pre_SMALL="";
             String pre_BIG2="";
             String pre_BIG3="";
             int SMALL_ID=1;
             int BIG_ID2=1;
             int BIG_ID3=1;
             int pre_SMALL_ID_NUM=1;
             int pre_BIG_ID2_NUM=1;
             int pre_BIG_ID3_NUM=1;
	         while(it.hasNext()){ 
	             XWPFTable table = it.next();    
	             List<XWPFTableRow> rows=table.getRows();  
	             for (int i = 1; i < rows.size(); i++) {  
	                 XWPFTableRow  row = rows.get(i);
	                 Map<String,Object>map=new HashMap<String, Object>();
                	 map.put("SE_TYPE", SE_TYPE);
                	 map.put("SE_TYPE_ID", SE_TYPE_ID);
                	 map.put("INSPECT_TYPE", INSPECT_TYPE);
                	 map.put("INSPECT_TYPE_ID", INSPECT_TYPE_ID);
                	 //读取每一列数据  
	                 List<XWPFTableCell> cells = row.getTableCells();
	                 String secondcellname = "";
	                 String thirdcellname = "";
	                 String fouthcellname = "";
	                 String fifthcellname = "";
	                 for (int j = 0; j < cells.size(); j++) {  
	                     XWPFTableCell cell=cells.get(j);  
	                     if(j==1) {//第二列数据BIG
	                    	 secondcellname=cell.getText().trim();
	                    	 if(!"".equals(secondcellname)) {
	                    		 preSecondcellname=secondcellname; 
	                    	 }
	                    	 if(secondcellname.contains("$")) {
	                    		 secondcellname=""; 
	                    	 }
	                    	 if(preSecondcellname.contains("$")) {
	                    		 preSecondcellname=""; 
	                    	 }
	                     }else if(j==2) {//第三列数据SMALL
	                    	 thirdcellname=cell.getText().trim();
	                    	 if(!"".equals(thirdcellname)) {
	                    		 preThirdcellname=thirdcellname; 
	                    	 }
	                    	 if(thirdcellname.contains("$")) {
	                    		 thirdcellname=""; 
	                    	 }
	                    	 if(preThirdcellname.contains("$")) {
	                    		 preThirdcellname=""; 
	                    	 }
	                     }else if(j==3) {//第四列数据BIG2
	                    	 fouthcellname=cell.getText().trim();
	                    	 if(cells.size()>7&&!"".equals(fouthcellname)) {
	                    		 preFouthcellname=fouthcellname; 
	                    	 }
	                    	 if(fouthcellname.contains("$")) {
	                    		 fouthcellname=""; 
	                    	 }
	                    	 if(preFouthcellname.contains("$")) {
	                    		 preFouthcellname=""; 
	                    	 }
	                     }else if(j==4) {//第五列数据BIG3
	                    	 fifthcellname=cell.getText().trim();
	                    	 if(cells.size()>7&&!"".equals(fifthcellname)) {
	                    		 preFifthcellname=fifthcellname; 
	                    	 }
	                    	 if(fifthcellname.contains("$")) {
	                    		 fifthcellname=""; 
	                    	 }
	                    	 if(preFifthcellname.contains("$")) {
	                    		 preFifthcellname=""; 
	                    	 }
	                     }
	                 } 
	                 //BIG赋值
	                 if(!"".equals(secondcellname)) {
	                	 map.put("BIG", secondcellname);
	                 }else {
	                	 map.put("BIG", preSecondcellname);
	                 }
	                 //BIG_ID赋值
	                 String BIG_ID=map.get("BIG").toString().substring(0,2);
	                 if(this.checkNumber(BIG_ID, "+")) {
	                	 map.put("BIG_ID", "00"+BIG_ID);
	                 }else {
	                	 BIG_ID=map.get("BIG").toString().substring(0,1);
	                	 map.put("BIG_ID", "000"+BIG_ID);
	                 }
	                 //SMALL赋值
	                 if(!"".equals(thirdcellname)) {
	                	 map.put("SMALL", thirdcellname);
	                 }else {
	                	 map.put("SMALL", preThirdcellname);
	                 }
	                 //SMALL_ID赋值
	                 String small_temp=map.get("SMALL").toString();
	                 //获取文字上是否有数字小标题
	                 String SMALL_ID_TEMP=this.getNum(small_temp);
	                 if(SMALL_ID_TEMP!=null
	                		 &&!"".equals(SMALL_ID_TEMP)) {
	                	 SMALL_ID=Integer.parseInt(SMALL_ID_TEMP);
	                 }else {
	                	 if(pre_BIG_ID.equals(map.get("BIG_ID"))) {
	                		 if(pre_SMALL.equals(map.get("SMALL"))) {
	                			 SMALL_ID=pre_SMALL_ID_NUM;
		                	 }else {
		                		 pre_SMALL_ID_NUM++;
	                			 SMALL_ID=pre_SMALL_ID_NUM;
		                	 }
	                	 }else {
	                		 SMALL_ID=1;
	                	 }
	                 }
	                 if(SMALL_ID<10) {
	                	 map.put("SMALL_ID", "000"+SMALL_ID);
	                 }else {
	                	 map.put("SMALL_ID", "00"+SMALL_ID);
	                 }
	                 pre_BIG_ID=map.get("BIG_ID").toString();
	                 pre_SMALL=map.get("SMALL").toString();
	                 pre_SMALL_ID_NUM=Integer.parseInt(map.get("SMALL_ID").toString());
	                 
	                 //BIG2赋值
	                 if(!"".equals(fouthcellname)) {
	                	 map.put("BIG2", fouthcellname);
	                 }else {
	                	 if(cells.size()>7&&!"".equals(preFouthcellname)) {
	                		 map.put("BIG2", preFouthcellname);
	                	 }
	                 }
	                 //BIG_ID2赋值
	                 if(map.get("BIG2")!=null&&!"".equals(map.get("BIG2"))) {
	                	 String big2_temp=map.get("BIG2").toString();
	                	 //获取文字上是否有数字小标题
		                 String BIG_ID2_TEMP=this.getNum(big2_temp);
		                 if(BIG_ID2_TEMP!=null
		                		 &&!"".equals(BIG_ID2_TEMP)) {
		                	 BIG_ID2=Integer.parseInt(BIG_ID2_TEMP);
		                 }else {
		                	 if(pre_SMALL_ID.equals(map.get("SMALL_ID"))) {
		                		 if(pre_BIG2.equals(map.get("BIG2"))) {
		                			 BIG_ID2=pre_BIG_ID2_NUM;
			                	 }else {
			                		 pre_BIG_ID2_NUM++;
			                		 BIG_ID2=pre_BIG_ID2_NUM;
			                	 }
		                	 }else {
		                		 BIG_ID2=1;
		                	 }
		                 }
		                 if(BIG_ID2<10) {
		                	 map.put("BIG_ID2", "000"+BIG_ID2);
		                 }else {
		                	 map.put("BIG_ID2", "00"+BIG_ID2);
		                 } 
		                 
		                 pre_SMALL_ID=map.get("SMALL_ID").toString();
		                 pre_BIG2=map.get("BIG2").toString();
		                 pre_BIG_ID2_NUM=Integer.parseInt(map.get("BIG_ID2").toString()); 
	                 }
	                 //BIG3赋值
	                 if(!"".equals(fifthcellname)) {
	                	 map.put("BIG3", fifthcellname);
	                 }else {
	                	 if(cells.size()>7&&!"".equals(fifthcellname)) {
	                		 map.put("BIG3", fifthcellname);
	                	 }
	                 }
	                 //BIG_ID3赋值
	                 if(map.get("BIG3")!=null&&!"".equals(map.get("BIG3"))) {
	                	 String big3_temp=map.get("BIG3").toString();
	                	 //获取文字上是否有数字小标题
		                 String BIG_ID3_TEMP=this.getNum(big3_temp);
		                 if(BIG_ID3_TEMP!=null
		                		 &&!"".equals(BIG_ID3_TEMP)) {
		                	 BIG_ID3=Integer.parseInt(BIG_ID3_TEMP);
		                 }else {
		                	 if(pre_BIG_ID2.equals(map.get("BIG2"))) {
		                		 if(pre_BIG3.equals(map.get("BIG3"))) {
		                			 BIG_ID3=pre_BIG_ID3_NUM;
			                	 }else {
			                		 pre_BIG_ID3_NUM++;
			                		 BIG_ID3=pre_BIG_ID3_NUM;
			                	 }
		                	 }else {
		                		 BIG_ID3=1;
		                	 }
		                 }
		                 if(BIG_ID3<10) {
		                	 map.put("BIG_ID3", "000"+BIG_ID3);
		                 }else {
		                	 map.put("BIG_ID3", "00"+BIG_ID3);
		                 }
		                 
		                 pre_BIG_ID2=map.get("BIG2").toString();
		                 pre_BIG3=map.get("BIG3").toString();
		                 pre_BIG_ID3_NUM=Integer.parseInt(map.get("BIG_ID3").toString()); 
	                 }
	                 map.put("序号", num);
	                 dataset.add(map);
	                 num++;
	             } 
	               
	         } 
	         File file=new File(outPath);
    		 if (!file.exists()||file.isDirectory()) {   
    			 file.delete();  
	         }
    		 SXSSFWorkbook wb=this.exportMultipleExcel("附页字典数据", headers, columns, dataset);
	         FileOutputStream out=new FileOutputStream(outPath);
	         wb.write(out);
             out.close();
             System.out.println("++++++操作完成++++++++");
	      }catch(Exception e){  
	    	  e.printStackTrace();  
          } 
    } 
    
    private SXSSFWorkbook exportMultipleExcel(String title, String[] headers, String[] columns, List<Map<String,Object>> dataset) {
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
		for (int i = 0,length=headers.length; i <length ; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(titlestyle);
			cell.setCellValue(headers[i]);
		}
		// 遍历集合数据，产生数据行
		for(int index=0,length=dataset.size();index<length;index++) {
			row = sheet.createRow(index+1);
			Map<String,Object> map=dataset.get(index);
			for (int i = 0,titlelength=columns.length; i <titlelength ; i++) {
				Object value=map.get(columns[i]);
				Cell cell = row.createCell(i);
				cell=this.getCell(cell, cellstyle, value);
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
	private Cell getCell(Cell cell,CellStyle cellstyle,Object value) {
		String textValue = null;
		if(value != null) {
			textValue = value.toString();
		}else {
			textValue = "";
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
    /**  
     * 检查字符串是否是数字  
     * @param num  
     * @param type  "0+":非负整数； "+":正整数； "-0":非正整数 ；
     * @param type "-":负整数； "-0+":整数  ；"":数字  
     * @return  
     */ 
	private  boolean checkNumber(String num,String type){   
        String  eL="";  
        if(type.equals("0+")) {
        	eL = "^\\d+$";//非负整数   （正整数，0）
        }else if(type.equals("+")) {
        	eL = "^\\d*[1-9]\\d*$";//正整数 (不含0)  
        }else if(type.equals("-0")) {
        	eL = "^((-\\d+)|(0+))$";//非正整数 	（负整数，0）
        }else if(type.equals("-")) {
        	eL = "^-\\d*[1-9]\\d*$";//负整数 
        }else if(type.equals("-0+")){
        	eL = "^-?\\d+$";//整数  （负整数，0，正整数）
        }else{
        	eL = "^\\d+$|^\\d+\\.\\d+$|-\\d+$|-\\d+\\.\\d+$";//浮点数和整数
        } 
        Pattern p = Pattern.compile(eL);   
        Matcher m = p.matcher(num);   
        return m.matches();   
    } 
    /**
     * 根据存在数字字符返回1位或者2位数字
     * 规则实例：(2.3),（2.3）,(2.3）,（2.3),2.3
     * @param str
     * @return
     */
	private String getNum(String str) {
    	if(str==null) {
    		return null;
    	}
    	str=str.trim();
    	String temp;
    	if(str.contains(".")) {
    		//先截取2位，是数字进行返回；如果不是，再截取1位
    		//System.out.println("++"+str+"++"+(str.indexOf(".")+1)+"++"+(str.indexOf(".")+3));
    		if(str.substring((str.indexOf(".")+1)).length()>=2) {
    			temp=str.substring((str.indexOf(".")+1),(str.indexOf(".")+3));
        		if(this.checkNumber(temp.trim(), "+")) {
        			return temp.trim();
        		}else {
        			temp=str.substring((str.indexOf(".")+1),(str.indexOf(".")+2));
        			if(this.checkNumber(temp.trim(), "+")) {
            			return temp.trim();
        			}
        		}
    		}else {
    			temp=str.substring((str.indexOf(".")+1),(str.indexOf(".")+2));
    			if(this.checkNumber(temp.trim(), "+")) {
        			return temp.trim();
    			}
    		}
    	}else if(str.contains("(")&&str.contains(")")) {//全英文
    		temp=str.substring((str.indexOf("(")+1),str.indexOf(")"));
    		if(this.checkNumber(temp.trim(), "+")) {
    			return temp.trim();
    		}
    	}else if(str.contains("（")&&str.contains("）")) {//全中文
    		temp=str.substring((str.indexOf("（")+1),str.indexOf("）"));
    		if(this.checkNumber(temp.trim(), "+")) {
    			return temp.trim();
    		}
    	}else if(str.contains("(")&&str.contains("）")) {//前英文后中文
    		temp=str.substring((str.indexOf("(")+1),str.indexOf("）"));
    		if(this.checkNumber(temp.trim(), "+")) {
    			return temp.trim();
    		}
    	}else if(str.contains("（")&&str.contains(")")) {//前中文后英文
    		temp=str.substring((str.indexOf("(")+1),str.indexOf("）"));
    		if(this.checkNumber(temp.trim(), "+")) {
    			return temp.trim();
    		}
    	}
    	return null;
    }
}  
