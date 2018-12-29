package util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.aspose.words.ImageSaveOptions;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
/**
 * 文件转为图片
 * @author Administrator
 *
 */
public class FileToImg {

	/**
	 * pdf转为img
	 * @param pdfPath
	 * @param path
	 */
	public static void pdf2Pic(String pdfPath, String path) {
		Document document = new Document();
		document.setFile(pdfPath);
		float scale = 1f;// 缩放比例
		float rotation = 0f;// 旋转角度

		for (int i = 0; i < document.getNumberOfPages(); i++) {
			BufferedImage image = (BufferedImage) document.getPageImage(i,
					GraphicsRenderingHints.SCREEN,
					org.icepdf.core.pobjects.Page.BOUNDARY_CROPBOX, rotation,
					scale);
			RenderedImage rendImage = image;
			try {
				String imgName = (i+1) + ".png";
				File file = new File(path+"/" + imgName);
				ImageIO.write(rendImage, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.flush();
		}
		document.dispose();
	}

	/**
	 * 获取注册文件
	 */
	public static void getLicense() {
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("util/license.xml"); 
			License license = new License();
			license.setLicense(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void wordToImg(String wordPath, String path)  throws Exception {
		long start = System.currentTimeMillis();
		getLicense();
		ImageSaveOptions iso = new ImageSaveOptions(SaveFormat.JPEG);
		iso.setResolution(128F); //= 128;
		iso.setPrettyFormat( true);
		iso.setUseAntiAliasing(true);
		com.aspose.words.Document doc1 = new com.aspose.words.Document(wordPath);
		for (int i = 0; i < doc1.getPageCount(); i++)
		{
			iso.setPageIndex(i); 
			doc1.save(path+"/"+ + (i+1) + ".png", iso);
		}
		System.out.println(System.currentTimeMillis() - start);
	}
	
	public static void main(String[] args) throws Exception {
//		FileToImg.pdf2Pic("D:\\test.pdf", "D:\\testimg");
		FileToImg.wordToImg("D:\\test.docx", "D:\\testimg");
	}
}
