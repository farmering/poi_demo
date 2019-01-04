package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 读取项目中的某个文件
 * @author Administrator
 *
 */
public class ReadFilePath {
	public static void main(String[] args) {
		 // 第一种：
		 File f = new File(ReadFilePath.class.getResource("/").getPath());
		 System.out.println("+1+"+f);
		 // 结果: D:/sdjz2016/poi_demo/target/classes
		 
		 f = new File(ReadFilePath.class.getResource("").getPath());
		 System.out.println("+2+"+f);
		 // 多返回了包名
		 //	结果: D:/sdjz2016/poi_demo/target/classes/util
		 
		 // 第二种：获取项目名
		 File directory = new File("");
		 String courseFile = null;
		 try {
		    courseFile = directory.getCanonicalPath();
		 } catch (IOException e) {
		    e.printStackTrace();
		 }
		 System.out.println("+获取项目名+"+courseFile);
		 // 结果: D:\sdjz2016\poi_demo

		 // 第三种：获取项目名
		 System.out.println("+第三种：获取项目名+"+System.getProperty("user.dir"));
		 // 结果: 获取项目名+D:\sdjz2016\poi_demo

		 // 第四种：获取项目中src目录下的一级文件路径
		 URL path =EhCacheConfigUtil.class.getClassLoader().getResource("ehcache.xml");
		 System.out.println(path.getPath());
		 //结果/D:/sdjz2016/poi_demo/target/classes/ehcache.xml
		 System.out.println("path+"+path.getPath());
	}
}
