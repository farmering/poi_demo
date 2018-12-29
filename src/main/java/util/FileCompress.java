package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;  
  
/** 
 * 压缩算法类 
 * 实现文件压缩，文件夹压缩，以及文件和文件夹的混合压缩 
 * 
 */  
public class FileCompress {  
    /** 
     * 压缩文件 
     * @param srcfile 
     */  
    public boolean zipFiles(File srcfile,File targetFile) {  
        ZipOutputStream out = null;  
        try {  
            out = new ZipOutputStream(new FileOutputStream(targetFile));  
            if(srcfile.isFile()){  
                this.zipFile(srcfile, out, "");  
            } else{  
                File[] list = srcfile.listFiles();  
                for (int i = 0; i < list.length; i++) {  
                    this.compress(list[i], out, "");  
                }  
            }
            return true;
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;
        } finally {  
            try {  
                if (out != null) {
                	out.close();  
                } 
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 压缩文件夹里的文件 
     * 起初不知道是文件还是文件夹--- 统一调用该方法 
     * @param file 
     * @param out 
     * @param basedir 
     */  
    private void compress(File file, ZipOutputStream out, String basedir) {  
        /* 判断是目录还是文件 */  
        if (file.isDirectory()) {  
            this.zipDirectory(file, out, basedir);  
        } else {  
            this.zipFile(file, out, basedir);  
        }  
    }  
  
    /** 
     * 压缩单个文件 
     * @param srcfile 
     */  
    private void zipFile(File srcfile, ZipOutputStream out, String basedir) {  
        if (!srcfile.exists()||srcfile.getName().toLowerCase().endsWith(".zip")) {
        	return;  
        } 
        byte[] buf = new byte[1024];  
        FileInputStream in = null;  
        try {  
            int len;  
            in = new FileInputStream(srcfile);  
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));  
            while ((len = in.read(buf)) > 0) {  
                out.write(buf, 0, len);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (out != null) {
                	out.closeEntry();  
                }  
                if (in != null) {
                	in.close();  
                } 
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * 压缩文件夹 
     * @param dir 
     * @param out 
     * @param basedir 
     */  
    private void zipDirectory(File dir, ZipOutputStream out, String basedir) {  
        if (!dir.exists()) {
        	return; 	
        } 
        File[] files = dir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            /* 递归 */ 
            compress(files[i], out, basedir + dir.getName() + "/");  
        }  
    }  
  
}  

