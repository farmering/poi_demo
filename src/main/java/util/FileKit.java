package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * 常用 文件处理工具
 */
public class FileKit {
	
	/**
	 * 读取文件流，生成文件
	 * @param in
	 * @param file
	 * @throws Exception
	 */
	public static void copy(InputStream in, File file) throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) in.close();
			if (out != null) out.close();
		}
	}
	/**
	 * 文件 生成MD5加密码
	 * @param file
	 * @return
	 * @throws Exception 
	 */
	public static String getFileMD5(File file){
		if (!file.isFile()) {
			return null;
		}
		FileInputStream in = null;
		try {

			MessageDigest digest = null;
			byte buffer[] = new byte[8192];
			int len;

			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			BigInteger bigInt = new BigInteger(1, digest.digest());
			String md5 = bigInt.toString(16);
			return md5;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 删除目录或者文件
	 * @param file
	 */
	public static void deleteAll(File file){
    	
    	/* 判断是目录还是文件 */   
        if (file.isDirectory()) {   
            deleteDirectory(file);   
        } else {   
            deleteFile(file);   
        }  
    	
    }
    
    /** 删除一个目录 */   
	public static void deleteDirectory(File file) {   
        if (!file.exists())   
            return;  
  
        File[] files = file.listFiles();   
        for (int i = 0; i < files.length; i++) {   
            /* 递归 */   
        	deleteAll(files[i]);   
        }
        file.delete();
    }  
    
    /** 删除一个文件 */   
	public static void deleteFile(File file) {   
        if (!file.exists()||file.isDirectory()) {   
            return;   
        }   
        file.delete(); 
    } 
	/**
	 * 复制文件
	 */
	public  static void copyFileUsingFileChannels(File source, File dest) throws IOException {    
        FileChannel inputChannel = null;    
        FileChannel outputChannel = null;    
	    try {
	        inputChannel = new FileInputStream(source).getChannel();
	        outputChannel = new FileOutputStream(dest).getChannel();
	        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
	    } finally {
	        inputChannel.close();
	        outputChannel.close();
	    }
	}
}
