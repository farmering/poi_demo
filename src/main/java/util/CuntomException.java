package util;

/**
 * 自定义runtimeExcepiton回滚事物
 *
 */
public class CuntomException extends RuntimeException{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4267203288084125033L;

	public CuntomException (String message){
		
		super(message);
	}
	public static void testEp() {
		String s=null;
		try {
			System.out.println(s.toString());
		} catch (Exception e) {
			throw new CuntomException("空指针异常");
		}
	}
	//print:空指针异常
	public static void main(String[] args) {
		try {
			CuntomException.testEp();
		} catch (Exception e) {
			if(e instanceof CuntomException) {
				System.out.println(e.getMessage());
			}else {
				e.printStackTrace();
			}
		}
	}
}
