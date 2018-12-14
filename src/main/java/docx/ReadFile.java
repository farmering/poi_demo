package docx;


import java.io.File;

public class ReadFile {

	public static void main(String[] args) {
		ReadFile read = new ReadFile();
		read.readFile("F:\\file\\电梯(最新)\\电梯");
	}
	
	public void readFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length == 0) {
				System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						// System.out.println("文件夹:" + file2.getAbsolutePath());
						readFile(file2.getAbsolutePath());
					} else {
						//if (file2.getName().contains("检验报告")) {
							this.copyFile(file2.getAbsolutePath(), "D:\\电梯");
							System.out.println("文件:" + file2.getAbsolutePath());
						//}
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
		}
	}


	public void copyFile(String oldPath, String newPath) {
		 try {  
	         File file=new File(newPath);  
	         if(file.exists()) {
	        	 file.delete();
	         }else {
	        	 file.mkdir();
	         }
			 File afile = new File(oldPath);  
	            if (afile.renameTo(new File(newPath + afile.getName()))) {  
	                System.out.println("File is moved successful!");  
	            } else {  
	                System.out.println("File is failed to move!");  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } 

	}
}
