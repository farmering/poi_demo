package docx;
import java.io.File;

import com.heavenlake.wordapi.Document;
/**
 * 往word里面指定位置插入图片
 * @author sun
 */
public class InsertWord {
	public static void main(String[] args) {
		 Document doc = null; 
	        try {  
	            doc = new Document();  
	            doc.open("D:\test1.doc");//打开E盘中的word文档  
	            doc.find("二维码");//把图片插到word文档“柱状图”文字位置中  
	            File imageFile = new File("C:\\Users\\Administrator\\Desktop\\logoword.png");  
	            doc.insert(imageFile);  
	            doc.close(true);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  	
	}
}
