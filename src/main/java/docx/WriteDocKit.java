package docx;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;  
 /**
  * 报告附页中docx4j 报告添加检验结论，类别，备注
  * @author sun
  *
  */
public class WriteDocKit {  
    public static void main(String[] args) {
    	WriteDocKit test = new WriteDocKit();  
        String filePath="D:\\aaa.docx";  
        String outPath="D:\\new.docx";
        test.testWord(filePath,outPath);  
        test.testDTWord(filePath,outPath);  
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
    public void testWord(String filePath,String outPath){  
    	try{  
	         FileInputStream in = new FileInputStream(filePath);//载入文档  
	         XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息  
	         Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格  
	         while(it.hasNext()){  
	             XWPFTable table = it.next();    
	             List<XWPFTableRow> rows=table.getRows();  
	             String num = null;
	             //读取每一行数据  
	             for (int i = 0; i < rows.size(); i++) {  
	                 XWPFTableRow  row = rows.get(i);  
	                 //读取每一列数据  
	                 List<XWPFTableCell> cells = row.getTableCells();   
	                 for (int j = 0; j < cells.size(); j++) {  
	                     XWPFTableCell cell=cells.get(j);  
	                     if(j==0) {
	                    	 num=cell.getText();
	                     }
	                     if(i>0) {
	                    	 if((cells.size()-j)==1) {
	                    		 String text=cell.getText();
	                    		 if(!"".equals(text)) {
	                    			 System.out.println(num+"行备注内容不为空+");
	                    		 }
	                    		 cell.setText("${BZ"+num.trim()+"}");
	                    	 } else if((cells.size()-j)==2) {
	                    		 String text=cell.getText();
	                    		 if(!"".equals(text)) {
	                    			 System.out.println(num+"行检验结论内容不为空+");
	                    		 }
	                    		 cell.setText("${JL"+num.trim()+"}");
	                    	 } else if((cells.size()-j)==3) {
	                    		 String text=cell.getText();
	                    		 if(!"".equals(text)) {
	                    			 System.out.println(num+"行检验结果内容不为空+");
	                    		 }
	                    		 cell.setText("${JYJG"+num.trim()+"}");
	                    	 }
	                     }
	                }  
	             }  
	               
	         } 
	         File file=new File(outPath);
    		 if (!file.exists()||file.isDirectory()) {   
    			 file.delete();  
	         }   
	         FileOutputStream out=new FileOutputStream(outPath);
             xwpf.write(out);
             out.close();
             System.out.println("++++++操作完成++++++++");
	      }catch(Exception e){  
	    	  e.printStackTrace();  
          } 
    } 
    /**
     * 电梯生成报告附页文件到excel,可生成BIG,SMALL,BIG2,BIG3 四级
     * 生成以下样例：BIG_ID取BIG前面标题数字；SMALL_ID,BIG_ID2,BIG_ID3先取前面数字，没有再继续往下加
     * 序号	检验类别				检验项目及其内容                                                                    检验结果      检验结论
			  						
										     		(1)使用登记资料
		1	 B			1技术资料		1.4使用资料	    (2)安全技术档案
													(3)管理规章制度
													(4)日常维护保养合同
													(5)特种设备作业人员证

     */
    public void testDTWord(String filePath,String outPath){  
    	try{  
	         FileInputStream in = new FileInputStream(filePath);//载入文档  
	         XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息  
	         Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格  
	         int  temp_num=1;
	         while(it.hasNext()){  
	             XWPFTable table = it.next();    
	             List<XWPFTableRow> rows=table.getRows();  
	             String num = null;
	             String fourthColumnName="";
	             //读取每一行数据  
	             for (int i = 1; i < rows.size(); i++) {  
	                 XWPFTableRow  row = rows.get(i);  
	                 //读取每一列数据  
	                 List<XWPFTableCell> cells = row.getTableCells(); 
	                 String  fourthColumnNameTemp="";
	                 for (int j = 1; j < cells.size(); j++) {  
	                     XWPFTableCell cell=cells.get(j);  
	                     num=String.valueOf(temp_num);
	                     if(j==3) {
	                    	 fourthColumnNameTemp=cell.getText().trim();
	                    	 if(!"".equals(fourthColumnNameTemp)) {
	                    		 fourthColumnName=fourthColumnNameTemp;
	                    	 }
	                     }
                    	 if((cells.size()-j)==1) {
                    		 String text=cell.getText();
                    		 if(!"".equals(text)) {
                    			 System.out.println(num+"行检验结论内容不为空+");
                    		 }
                    		// System.out.println(fourthColumnName+"++++"+fourthColumnNameTemp);
                    		 if(fourthColumnName.equals(fourthColumnNameTemp)) {
                    			 cell.setText("${JYJL"+num.trim()+"}");
                    		 }
                    	 } else if((cells.size()-j)==2) {
                    		 String text=cell.getText();
                    		 if(!"".equals(text)) {
                    			 System.out.println(num+"行检验结果内容不为空+");
                    		 }
                    		 cell.setText("${JYJG"+num.trim()+"}");
                    	 }
	                } 
	            	temp_num++;
	             }  
	               
	         } 
	         File file=new File(outPath);
    		 if (!file.exists()||file.isDirectory()) {   
    			 file.delete();  
	         }   
	         FileOutputStream out=new FileOutputStream(outPath);
             xwpf.write(out);
             out.close();
             System.out.println("++++++操作完成++++++++");
	      }catch(Exception e){  
	    	  e.printStackTrace();  
          } 
    }  
}  
