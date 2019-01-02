package util;


import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderKit {
	
    private Workbook wb;
    private Sheet sheet;
    private Row row;
    private boolean isXssf = false;

 
	public ExcelReaderKit(InputStream is) {
    	try {
			wb = WorkbookFactory.create(is);
			if (wb instanceof XSSFWorkbook) {
				isXssf = true; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}
	
	 /**
	  
	    * @param cell
	    * @return
	    */
	  public String getCellValue(Cell cell) {
		  String cellvalue = "";
	       if (cell != null) {
	           switch (cell.getCellType()) {
	           	case Cell.CELL_TYPE_NUMERIC:{
	           		cell.setCellType(1);
	           		cellvalue = cell.getRichStringCellValue().getString();
	           		break;
	           	}
	        
	           	case Cell.CELL_TYPE_STRING:{
	           		cellvalue = cell.getRichStringCellValue().getString();
	           		break;
	           	}
	         
	          }
	       } 
	       return cellvalue;
	   }
}