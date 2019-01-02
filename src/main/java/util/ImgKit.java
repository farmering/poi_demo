package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * 图片处理工具类
 */
public class ImgKit {
	
	 /**
     * 替换html中的base64图片数据为实际图片
     * @param html
     * @param fileRoot 本地路径
     * @param serRoot 服务器路径
     * @return
     */
    public static String replaceBase64Image(String image,String fileRoot,String serRoot) throws IOException{
        File file = new File(fileRoot);
        if(!file.exists()){
            new File(fileRoot).mkdirs();
        }
        UUID fileName = UUID.randomUUID(); 
        String base64ImgData = image;
        convertBase64DataToImage(base64ImgData, fileRoot+fileName+".jpg");//转成文件
        ImgCompress imgCom = new ImgCompress(fileRoot+fileName+".jpg");
		imgCom.resizeByWidth(400);
        String serPath = serRoot+fileName+".jpg";//服务器地址
        return serPath;
    }
    /**
     * 把base64图片数据转为本地图片
     * @param base64ImgData
     * @param filePath
     * @throws IOException
     */
    public static void convertBase64DataToImage(String base64ImgData,String filePath) throws IOException {
        byte[] bs  = Base64.getDecoder().decode(base64ImgData);
        try(FileOutputStream os = new FileOutputStream(filePath)){
        	os.write(bs);
        }
        
    }
}