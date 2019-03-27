package org.alex.core.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.alex.core.kit.proputil.ReadFileServiceKit;
import org.apache.commons.io.FileUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class FlashKit {
	
	private static final String DOC = ".doc";  
    private static final String DOCX = ".docx";  
    private static final String XLS = ".xls";  
    private static final String XLSX = ".xlsx";  
    private static final String PDF = ".pdf";  
    private static final String SWF = ".swf";  
    private static final String TOOL = "pdf2swf.exe";  
    private static final String pdf2swfPath=ReadFileServiceKit.getInstance().getPdf2swfPath();
    private static final OfficeManager officeManager;
    private static final OfficeDocumentConverter converter;
    static {
    	  DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
          configuration.setOfficeHome(new File(ReadFileServiceKit.getInstance().getLibreOffice()));
	      configuration.setPortNumber(8100);
          //configuration.setPortNumbers(new int[] { 8100, 8101});  
	      officeManager = configuration.buildOfficeManager();
	      //officeManager.start();
	      converter = new OfficeDocumentConverter(officeManager);
    }
    /** 
     * 入口方法-通过此方法转换文件至swf格式 
     * @param swfPath swf存储的绝对路径
     * @param swfName  swf的名称  如test.swf
     * @param filePath 上传文件所在文件夹的绝对路径 
     * @param fileName  文件名称   如 test.pdf
     * @param op  是否删除已有的swf
     * @return          是否转换成功
     */  
    public  static synchronized Boolean beginConvert(String swfPath,String swfName,String filePath,
    		String fileName,Boolean op,HttpSession session) { 
    	try {
    		String fileExt = ""; 
    		long begin_time = new Date().getTime();
	        if (null != fileName && fileName.indexOf(".") > 0) {  
	            int index = fileName.lastIndexOf(".");  
	            fileExt = fileName.substring(index).toLowerCase();  
	        }  
	        
	        String inputFile = filePath + File.separator + fileName;
	        String outFile = swfPath + File.separator + swfName;
	        
	        //先判断是否已经存在flash
	        File swfFile = new File(swfPath);
	        //如果存在
	        if(swfFile.exists() && !FuncKit.isEmpty(swfFile.listFiles())){
	        	if(op){
	        		FileKit.deleteAll(swfFile);
	        		File newswffile = new File(swfPath);
	        		
	        		if(!newswffile.exists()){
	        			newswffile.mkdirs();
	        		}
	        	}else{
	        		return true;
	        	}
	        }else{
	        	if(!swfFile.exists()){
	        		swfFile.mkdirs();
	        	}
	        }
	      //如果是源文件是不是flash文件，直接复制
	        if(fileExt.equals(SWF)){  
	        	FileUtils.copyFile(new File(inputFile), new File(outFile));
	        }else{
	        	//如果是office文档，先转为pdf文件  
	        	if (fileExt.equals(DOC) || fileExt.equals(DOCX) || fileExt.equals(XLS) || fileExt.equals(XLSX)) {
	        		
	        		String outputFile = filePath + File.separator + fileName.substring(0,fileName.lastIndexOf(".")) + PDF;  
                    
                    //office2PDF(inputFile, outputFile); 
                    //libOffice2PDF(inputFile, outputFile,session); 
                    libOffice2PDFCmd(inputFile, filePath,session); 
                    inputFile = outputFile;  
                    fileExt = PDF;
                    String toolFile = pdf2swfPath  + TOOL; 
                    String languagedir = pdf2swfPath  +"xpdf-chinese-simplified";  
                    File f = new File(inputFile);
                    System.out.println(inputFile);
                    while(!f.exists()){
                    	Thread.sleep(10000);
                    };
                    convertPdf2Swf(inputFile, outFile, toolFile,languagedir); 
	        	}else if (fileExt.toLowerCase().equals(PDF)) {  
                    String toolFile = pdf2swfPath  + TOOL; 
                    String languagedir = pdf2swfPath  +"xpdf-chinese-simplified";  
                    convertPdf2Swf(inputFile, outFile, toolFile,languagedir);  
                } 
	        }
	        long end_time = new Date().getTime();
	        System.out.println("文件转换总耗时：[" + (end_time - begin_time) + "]ms");
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }  
    /** 
     * 将pdf文件转换成swf文件 
     * @param sourceFile pdf文件绝对路径 
     * @param outFile    swf文件绝对路径 
     * @param toolFile   转换工具绝对路径 
     * @throws Exception 
     */  
    private static void convertPdf2Swf(String sourceFile, String outFile,  
                                String toolFile,String languagedir) throws Exception {  
    	long begin_time = new Date().getTime();
    	String command = "cmd /c "+toolFile + " \"" + sourceFile + "\" -o  \"" + outFile  
        + "\" -s flashversion=9 -s languagedir="+languagedir;
    	Process process = Runtime.getRuntime().exec(command); // 调用外部程序
    	final InputStream is1 = process.getInputStream();
    	new Thread(new Runnable() {
    	    public void run() {
    	        BufferedReader br = new BufferedReader(new InputStreamReader(is1)); 
    	        try {
					while(br.readLine() != null) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
    	    }
    	}).start();// 启动单独的线程来清空process.getInputStream()的缓冲区
    	InputStream is2 = process.getErrorStream();
    	BufferedReader br2 = new BufferedReader(new InputStreamReader(is2)); 
    	StringBuilder buf = new StringBuilder(); // 保存输出结果流
    	String line = null;
    	while((line = br2.readLine()) != null) {
    		buf.append(line); // 循环等待进程结束
    	} 
    	process.waitFor();
    	long end_time = new Date().getTime();
	    System.out.println("pdf转swf文件转换总耗时：[" + (end_time - begin_time) + "]ms");
    }  
  
    /** 
     * office文档转pdf文件 
     * @param sourceFile    office文档绝对路径 
     * @param destFile      pdf文件绝对路径 
     * @return 
     * @throws IOException 
     */  
    @Deprecated
    private static void office2PDF(String sourceFile, String destFile) throws IOException {  
    	String OpenOffice_HOME = ReadFileServiceKit.getInstance().getOpenOffice_HOME();  
        String host_Str = ReadFileServiceKit.getInstance().getHost_Str(); 
        String port_Str = ReadFileServiceKit.getInstance().getPort_Str();  
        File inputFile = new File(sourceFile);  
        if (inputFile.exists()) {  
        	// 如果目标路径不存在, 则新建该路径  
            File outputFile = new File(destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
            // 启动OpenOffice的服务  
            String command = "\""+OpenOffice_HOME  
                    + "\\program\\soffice.exe\" -headless -accept=\"socket,host="  
                    + host_Str + ",port=" + port_Str + ";urp;\"";
            Process pro = Runtime.getRuntime().exec(command);  
            // 连接openoffice服务  
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(host_Str, Integer.parseInt(port_Str));  
            connection.connect();  
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);  
            converter.convert(inputFile, outputFile);  
            // 关闭连接和服务  
            connection.disconnect();  
            pro.destroy(); 
        }  
    }  
    /** 
     * LibreOffice文档转pdf文件 
     * @param sourceFile    office文档绝对路径 
     * @param destFile      pdf文件绝对路径 
     * @return 
     * @throws IOException 
     */  
    private static void libOffice2PDF(String sourceFile, String destFile,HttpSession session) throws IOException {  
        File inputFile = new File(sourceFile);  
        if (inputFile.exists()) {  
            long begin_time = new Date().getTime();
            File outputFile = new File(destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
//          DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
//          configuration.setOfficeHome(new File(ReadFileServiceKit.getInstance().getLibreOffice()));
//	        configuration.setPortNumber(8100);
//	        OfficeManager officeManager = configuration.buildOfficeManager();
//	        officeManager.start();
//	        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
	        converter.convert(inputFile, outputFile);
	       // officeManager.stop();
	        long end_time = new Date().getTime();
	        System.out.println("doc转pdf文件转换耗时：[" + (end_time - begin_time) + "]ms");
        }
        if(session!=null) {
			session.setAttribute("convertDocState", "pdf"); 
		}
    } 
    /** 
     * 利用 LibreOffice 命令进行文档转pdf文件 
     * @param sourceFile    office文档绝对路径 
     * @param destFile      pdf文件绝对路径 
     * @return 
     * @throws IOException 
     */  
    private static void libOffice2PDFCmd(String sourceFile, String destFile,HttpSession session) throws  Exception {  
    	File inputFile = new File(sourceFile);  
        if (inputFile.exists()) {  
            long begin_time = new Date().getTime();
            File outputFile = new File(destFile);  
            if (!outputFile.getParentFile().exists()) {  
                outputFile.getParentFile().mkdirs();  
            }  
            String destpath=checkFilePath(destFile);
	        System.out.println("+sourceFile+"+sourceFile);
	        System.out.println("+destpath+"+destpath);
            String command ="cmd /c start soffice --convert-to pdf:writer_pdf_Export --outdir \""+destpath+"\" \""+sourceFile+"\"";
        	Process process = Runtime.getRuntime().exec(command); // 调用外部程序
        	final InputStream is1 = process.getInputStream();
        	new Thread(new Runnable() {
        	    public void run() {
        	        BufferedReader br = new BufferedReader(new InputStreamReader(is1)); 
        	        try {
    					while(br.readLine() != null) ;
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
        	    }
        	}).start();
        	InputStream is2 = process.getErrorStream();
        	BufferedReader br2 = new BufferedReader(new InputStreamReader(is2)); 
        	StringBuilder buf = new StringBuilder();
        	String line = null;
        	while((line = br2.readLine()) != null) {
        		buf.append(line); 
        	} 
        	process.waitFor();
        	long end_time = new Date().getTime();
	        System.out.println("doc转pdf文件转换耗时：[" + (end_time - begin_time) + "]ms");
        }
        if(session!=null) {
			session.setAttribute("convertDocState", "pdf"); 
		}
    }
    /**
     * 判断文件路径是否以/或者\结束，并递归进行截取
     */
    private static String checkFilePath(String filePath) {
    	if(filePath.endsWith("\\")) {
    		filePath=filePath.substring(0,filePath.length()-1);
    		checkFilePath(filePath);
    	}
    	if(filePath.endsWith("/")) {
    		filePath=filePath.substring(0,filePath.length()-1);
    		checkFilePath(filePath);
    	}
    	return filePath;
    }
}
