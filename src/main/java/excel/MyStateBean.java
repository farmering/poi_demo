package excel;

public class MyStateBean {
	private boolean isCombine = false;// 是否为合并单元格
	private boolean valid = false;// 是否有效
	private int firstC;// 起始
	private int lastC;// 结束
	private int firstR;// 起始
	private int lastR;// 结束
	private int width;// 宽度
	private int height;// 高度
	private boolean isbold = false;// 字体是否加粗
	private int border_left = 0;// 左边
	private int border_right = 0;// 右边
	private int border_top = 0;// 上边
	private int border_bottm = 0;// 下边
	private int align_h = 0;// 0左对1居中对齐2右对
	private int align_v = 0;// 0顶对1居中对齐2底对
	private int font_size = 12;// 字号

	public MyStateBean() {
		super();
	}

	public MyStateBean(boolean isCombine, boolean valid, int firstC, int lastC, int firstR, int lastR, int width, int height, boolean isbold, int border_left, int border_right, int border_top, int border_bottm, int align_h, int align_v, int font_size) {
		super();
		this.isCombine = isCombine;
		this.valid = valid;
		this.firstC = firstC;
		this.lastC = lastC;
		this.firstR = firstR;
		this.lastR = lastR;
		this.width = width;
		this.height = height;
		this.isbold = isbold;
		this.border_left = border_left;
		this.border_right = border_right;
		this.border_top = border_top;
		this.border_bottm = border_bottm;
		this.align_h = align_h;
		this.align_v = align_v;
		this.font_size = font_size;
	}

	public int getFirstC() {
		return firstC;
	}

	public void setFirstC(int firstC) {
		this.firstC = firstC;
	}

	public int getLastC() {
		return lastC;
	}

	public void setLastC(int lastC) {
		this.lastC = lastC;
	}

	public int getFirstR() {
		return firstR;
	}

	public void setFirstR(int firstR) {
		this.firstR = firstR;
	}

	public int getLastR() {
		return lastR;
	}

	public void setLastR(int lastR) {
		this.lastR = lastR;
	}

	public boolean isCombine() {
		return isCombine;
	}

	public void setCombine(boolean isCombine) {
		this.isCombine = isCombine;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isIsbold() {
		return isbold;
	}

	public void setIsbold(boolean isbold) {
		this.isbold = isbold;
	}

	public int getBorder_left() {
		return border_left;
	}

	public void setBorder_left(int border_left) {
		this.border_left = border_left;
	}

	public int getBorder_right() {
		return border_right;
	}

	public void setBorder_right(int border_right) {
		this.border_right = border_right;
	}

	public int getBorder_top() {
		return border_top;
	}

	public void setBorder_top(int border_top) {
		this.border_top = border_top;
	}

	public int getBorder_bottm() {
		return border_bottm;
	}

	public void setBorder_bottm(int border_bottm) {
		this.border_bottm = border_bottm;
	}

	public int getAlign_h() {
		return align_h;
	}

	public void setAlign_h(int align_h) {
		this.align_h = align_h;
	}

	public int getAlign_v() {
		return align_v;
	}

	public void setAlign_v(int align_v) {
		this.align_v = align_v;
	}

	public int getFont_size() {
		return font_size;
	}

	public void setFont_size(int font_size) {
		this.font_size = font_size;
	}

}
