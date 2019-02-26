package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;



/** 
 * 生成PDF，通过freemarker模板
 */
public class HtmlPDFTool {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("html开始转换pdf");
		try {
			String url = "http://127.0.0.1:8080/SDTMC/print/print_apply!createpdf.action?id=297e3d6e6903af80016903c06ffe0012";
			new HtmlPDFTool().generationPdfDzOrder(url, "d:/test.pdf");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - start);
		System.out.println("html转换pdf结束...");
	}
	public InputStream generationPdfDzOrder(String htmlFileName,String pdfFileName) throws Exception{
		String command = getCommand(htmlFileName , pdfFileName);
		System.out.println(command);
		File pdfFile = new  File(pdfFileName); //pdf文件
		if(!pdfFile.exists()){pdfFile.createNewFile();}
		Process process=Runtime.getRuntime().exec(command);
		InputStreamReader ir = new InputStreamReader(process.getErrorStream());
		LineNumberReader input = new LineNumberReader(ir);
		while (input.readLine() != null) {
			System.out.println(input.readLine());
		}
		process.destroy();
		System.out.println("运行完毕。");
		process.destroy();
		return new FileInputStream(pdfFile);
	}
	public String getCommand(String htmlName , String pdfName){
		return "D:/wkhtmltopdf/bin/wkhtmltopdf.exe "+ "--margin-top 0mm --margin-bottom 0mm --margin-right 0mm --margin-left 0mm " + htmlName + " " + pdfName ;
	}
}
