package excel;

/**
 * 本类用于把调整好的excel模板转化为布局模板信息存入数据库
 * 
 */

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;



public class ExcelToDatabase {
	private JianYanBO jianYanBO=new JianYanBO();
	/**
	 * 解析excel表格并插入到数据库中
	 * @param file  excel文件
	 * @param ignoreRows  第几行开始解析，excel序号从0开始，该值为0时从第一行开始解析
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void importToDb(BufferedInputStream in,int ignoreRows,
			String inspectTypeId,String examineType,String seTypeId,String reportName) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now=sdf.format(new Date());
		POIFSFileSystem fs = new POIFSFileSystem(in);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		// 是否有效，true有效插入数据库,false无效不插入
		boolean isvalid = true;
		jianYanBO.delReport("DELETE  FROM INSPECTION_OPINION_TEMPLATE WHERE INSPECT_TYPE_ID='"+inspectTypeId+"'");
		HSSFSheet st = wb.getSheetAt(0);
		// 从第0行开始读取
		System.out.println("<br/>占用" + st.getLastRowNum() + "行");
		for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
			System.out.println("<br/>当前第" + rowIndex + "行");
			HSSFRow row = st.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			int tempRowSize = row.getLastCellNum();
			System.out.println("<br/>占用" + tempRowSize + "列");
			String lastFieldName="";
			for (int columnIndex = 0; columnIndex <= tempRowSize;) {
				cell = row.getCell(columnIndex);
				if(columnIndex == 0&&cell==null) {
					return;
				}
				if(cell==null) {
					columnIndex++;// 如果columnIndex单元格为null则执行下一个
					continue;
				}
				MyStateBean msb = this.checkhebing(cell, st);
				String cellValue = "";
				// 获取该单元格的值
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						// 第一列格式化为整型
						if (columnIndex == 0) {
							cellValue = String.valueOf((int) (cell.getNumericCellValue()));
						} else {
							cellValue = String.valueOf(cell.getNumericCellValue());
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						cellValue = String.valueOf(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						cellValue = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_FORMULA:
						cellValue = String.valueOf(cell.getCellFormula());
						break;
					default:
						break;
				}
				int width = 1;
				int height = 1;
				int widthnum = cell.getSheet().getColumnWidth(cell.getColumnIndex());
				// 如果是合并单元格则执行下循环步长
				if (msb.isCombine()) {
					width = msb.getLastC() - msb.getFirstC() + 1;
					height = msb.getLastR() - msb.getFirstR() + 1;
					columnIndex = msb.getLastC() + 1;
					// 如果是合并单元格的第一个单元格
					if (msb.isValid()) {
						widthnum = msb.getWidth();
					} else {
						isvalid = false;
					}
				} else {// 非合并单元格
					columnIndex++;
				}
				int isrowspan = 0;
				int iscolspan = 0;
				if (height > 1) {
					isrowspan = 1;
				}
				if (width > 1) {
					iscolspan = 1;
				}
				if (isvalid) {
//					System.out.println("+cellValue+"+cellValue);
					String maxlength = "100";
					Hashtable hashtable=new Hashtable();
					String id=UUID.randomUUID().toString().replaceAll("-", "");
					hashtable.put("ID",id );//主键id
					hashtable.put("REPORT_NAME", reportName);//报告名称
					hashtable.put("STAGE_ORDER", "1");//段落序号
					hashtable.put("FIELD_MAXLENGTH", maxlength);//段内序号
					hashtable.put("FIELD_ID", id);//单元格id
					if(rowIndex != ignoreRows
							&&columnIndex==tempRowSize) {
						hashtable.put("FIELD_CELLTYPE", "button");//字段类型title 标题，button 自动内容 。
						hashtable.put("FIELD_NAME", lastFieldName);//存储上一个单元格值
						hashtable.put("FIELD_VALUE", lastFieldName);//存储上一个单元格值
						hashtable.put("FIELD_COMMENT",cellValue);//存储最后一列的意见描述
					}else {
						hashtable.put("FIELD_NAME", cellValue);//单元格名称
						hashtable.put("FIELD_VALUE", cellValue);//单元格值
						hashtable.put("FIELD_CELLTYPE", "title");//字段类型title 标题，button 自动内容 。
						hashtable.put("FIELD_COMMENT", "");//标记字段的说明，用在可更改数据上
					}
					hashtable.put("FIELD_CELLCONTENT", "");//用于radio或者checkbox等类型的数据项展示
					hashtable.put("FIELD_RESULT", "");//本单元格结果，1表示对号，0表示叉号
					
					hashtable.put("FIELD_ISSTRETCHCOLUMNS", "0");//标记字段的说明，用在可更改数据上
					hashtable.put("FIELD_WIDTH", width);//本单元格的宽度倍数
					hashtable.put("FIELD_HEIGHT", height);//本单元格的宽度倍数
					hashtable.put("FIELD_HEIGHTOFFSET", "0");//本单元格的高度偏移量
					hashtable.put("FIELD_ISCOLSPAN", iscolspan);//本单元格是否为跨列
					hashtable.put("FIELD_ISROWSPAN", isrowspan);//本单元格是否为跨行
					hashtable.put("FIELD_COLSPAN", width);//占用列数
					hashtable.put("FIELD_ROWSPAN", height);//占用行数
					hashtable.put("FIELD_ORDERCLSID", cell.getColumnIndex() + 1);//列序号
					hashtable.put("FIELD_ORDERROWID", cell.getRowIndex() + 1);//行序号
					hashtable.put("UPDATE_DATE", now);//更新日期
					hashtable.put("PARENTID", "0");//父框的id
					hashtable.put("PARENTORDERCLSID","0");//父框的列序号
					hashtable.put("PARENTORDERROWID", "0");//父框的行序号
					hashtable.put("SE_TYPE",seTypeId);//设备类别
					hashtable.put("APPLY_TYPE", examineType);//检验类型
					hashtable.put("APPLY_KIND", examineType);//检验种类
					hashtable.put("INSPECT_TYPE_ID", inspectTypeId);//检验报告模板id，如000301001对应：定期检验，杂物电梯定期检验报告
					hashtable.put("GRAVITY", "2");//对齐方式1左2中3右
					hashtable.put("WIDTHNUM", widthnum);//单元格宽度的具体数值没有的使用倍数（FIELD_WIDTH）设置
					jianYanBO.InsertData(hashtable, "INSPECTION_OPINION_TEMPLATE");
					lastFieldName=cellValue;//上一个单元格值，必须在最下方
				}
				isvalid = true;// 是否有效标志恢复默认值
			}
		}
	}

	/**
	 * 验证是否合并单元格
	 */
	private MyStateBean checkhebing(Cell cell, HSSFSheet sheet) {
		MyStateBean msb = new MyStateBean();
		// 获得一个 sheet 中合并单元格的数量
		int sheetmergerCount = sheet.getNumMergedRegions();
		// System.out.println("共有合并单元格："+sheetmergerCount+"个");
		int cellr = cell.getRowIndex();
		int cellc = cell.getColumnIndex();
		// 遍历合并单元格
		for (int i = 0; i < sheetmergerCount; i++) {
			// 获得合并单元格
			CellRangeAddress ca = sheet.getMergedRegion(i);
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			int firstC = ca.getFirstColumn();
			int lastC = ca.getLastColumn();
			int firstR = ca.getFirstRow();
			int lastR = ca.getLastRow();
			// 在合并单元格的范围内则为合并单元格合并单元格的第一个单元格有值
			if (cellr == firstR && cellc == firstC) {
//				System.out.println("该单元格在合并单元格中");
//				System.out.println("起始行：" + firstR + ", 结束行：" + lastR + ", 起始列：" + firstC + ", 结束列：" + lastC);
				msb.setCombine(true);
				msb.setValid(true);// 有效
				msb.setFirstC(firstC);
				msb.setLastC(lastC);
				msb.setFirstR(firstR);
				msb.setLastR(lastR);
				int width = 0;
				// 设置长宽
				for (int idx = firstC; idx <= lastC; idx++) {
					width += cell.getSheet().getColumnWidth(idx);
				}
				msb.setWidth(width);
				break;
			} else if ((cellr > firstR && cellc > firstC || cellr > firstR && cellc == firstC
					|| cellr == firstR && cellc > firstC) && cellr <= lastR && cellc <= lastC) {// 非第一个无值0，0才有意义，其他无意义
				//System.out.println("XXXX该单元格在合并单元格中");
				//System.out.println("起始行：" + firstR + ", 结束行：" + lastR + ", 起始列：" + firstC + ", 结束列：" + lastC);
				msb.setCombine(true);
				msb.setValid(false);// 无效
				msb.setFirstC(firstC);
				msb.setLastC(lastC);
				msb.setFirstR(firstR);
				msb.setLastR(lastR);
				break;
			}
		}
		return msb;
	}
}
