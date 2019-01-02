package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFSlideShowImpl;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
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
	/**
	 * ppt2007转图片
	 * @param pptPath
	 * @param path
	 */
	public static void ppt2Pic2007(String pptPath, String path)throws Exception {
        FileInputStream is = new FileInputStream(pptPath);  
        XMLSlideShow ppt = new XMLSlideShow(is);  
        is.close();  
        Dimension pgsize = ppt.getPageSize();  
        System.out.println(pgsize.width+"--"+pgsize.height);  
        for (int i = 0; i < ppt.getSlides().size(); i++) {  
            try {  
                //防止中文乱码  
                for(XSLFShape shape : ppt.getSlides().get(i).getShapes()){  
                    if(shape instanceof XSLFTextShape) {  
                        XSLFTextShape tsh = (XSLFTextShape)shape;  
                        for(XSLFTextParagraph p : tsh){  
                            for(XSLFTextRun r : p){  
                                r.setFontFamily("宋体");  
                            }  
                        }  
                    }  
                }  
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);  
                Graphics2D graphics = img.createGraphics();  
                graphics.setPaint(Color.white);  
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));  
                ppt.getSlides().get(i).draw(graphics);  
                String imgName = (i+1) + ".png";
//				File file = new File(path+"/" + imgName);
                String filename = path+"/" + imgName;  
                System.out.println(filename);  
                FileOutputStream out = new FileOutputStream(filename);  
                javax.imageio.ImageIO.write(img, "png", out);  
                out.close();  
            } catch (Exception e) {  
            	e.printStackTrace();
                System.out.println("第"+i+"张ppt转换出错");  
            }  
        }  
	}
	/**
	 * ppt2003转图片
	 * @param pptPath
	 * @param path
	 */
	public static void ppt2Pic2003(String pptPath, String path)throws Exception {
		try {  
            HSLFSlideShow ppt = new HSLFSlideShow(new HSLFSlideShowImpl(pptPath));  
            Dimension pgsize = ppt.getPageSize();  
            for (int i = 0; i < ppt.getSlides().size(); i++) {  
                //防止中文乱码  
                for(HSLFShape shape : ppt.getSlides().get(i).getShapes()){  
                    if(shape instanceof HSLFTextShape) {  
                        HSLFTextShape tsh = (HSLFTextShape)shape;  
                        for(HSLFTextParagraph p : tsh){  
                            for(HSLFTextRun r : p){  
                                r.setFontFamily("宋体");  
                            }  
                        }  
                    }  
                }  
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);  
                Graphics2D graphics = img.createGraphics();  
                graphics.setPaint(Color.white);  
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));  
                  
                ppt.getSlides().get(i).draw(graphics);  
                String imgName = (i+1) + ".png";
//				File file = new File(path+"/" + imgName);
                String filename = path+"/" + imgName;  
                System.out.println(filename);  
                FileOutputStream out = new FileOutputStream(filename);  
                javax.imageio.ImageIO.write(img, "png", out);  
                out.close();  
//              resizeImage(filename, filename, width, height);  
            }  
            System.out.println("3success");  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
	}
	public static void main(String[] args) throws Exception {
//		FileToImg.pdf2Pic("D:\\test.pdf", "D:\\testimg");
//		FileToImg.wordToImg("D:\\test.docx", "D:\\testimg");
		FileToImg.ppt2Pic2007("D:\\aa.pptx", "D:\\testimg");
	}
}
