package util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
/**
 * 字符串工具类
 * @author Administrator
 *
 */
public class StrKit {

	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String EMPTY = "";
	public static final String CRLF = "\r\n";
	public static final String NEWLINE = "\n";
	public static final String UNDERLINE = "_";
	public static final String COMMA = ",";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";
	
	private static final String DES_ALGORITHM = "DES";
	private static String secretKey = "2c9f304c6795efdc01679b2e05e40014";
	/**
	 * 获取uuid
	 */
	public static String uuid(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}
	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	/**
	 * 字符串为 null 或者为 "" 时返回 true
	 */
	public static boolean isBlank(String str) {
		return (str == null || "".equals(str.trim()));
	}
	/**
	 * 字符串不为 null 而且不为 "" 时返回 true
	 */
	public static boolean notBlank(String str) {
		return (str != null && !"".equals(str.trim()));
	}
	/**
	 * 数组中有一个为null或者"" 返回true
	 */
	public static boolean notBlank(String... strs) {
		if (strs == null) {
			return false;
		}
		for (String str : strs) {
			if (str == null || "".equals(str.trim())) {
				return false;	
			}
		}
		return true;
	}
	/**
	 * 对象没有为null 返回true
	 */
	public static boolean notNull(Object... paras) {
		if (paras == null) {
			return false;
		}
		for (Object obj : paras) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否包含空字符串 true包含
	 */
	public static boolean hasBlank(String... strs) {
		if (strs == null || strs.length == 0) {
			return true;
		}
		for (String str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当给定字符串为null时，转换为Empty
	 * @param str  被转换的字符串
	 * @return  转换后的字符串
	 */
	public static String nullToEmpty(String str) {
		return str == null ? EMPTY : str;
	}
	/**
	 * 当给定字符串为空字符串时，转换为null
	 * @param str  被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyToNull(String str) {
		return isBlank(str) ? null : str;
	}

	/**
	 * 是否包含空字符串
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(String... strs) {
		for (String str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 去除字符串两边的空格符，如果为null返回null
	 * 
	 * @param str 字符串
	 * @return 处理后的字符串
	 */
	public static String trim(String str) {
		return (null == str) ? null : str.trim();
	}
    /**  
     * 检查字符串是否是数字  
     * @param num  
     * @param type  "0+":非负整数； "+":正整数； "-0":非正整数 ；
     * @param type "-":负整数； "-0+":整数  ；"":数字  
     * @return  
     */ 
    public static boolean checkNumber(String num,String type){   
        String  eL="";  
        if(type.equals("0+")) {
        	eL = "^\\d+$";//非负整数   （正整数，0）
        }else if(type.equals("+")) {
        	eL = "^\\d*[1-9]\\d*$";//正整数 (不含0)  
        }else if(type.equals("-0")) {
        	eL = "^((-\\d+)|(0+))$";//非正整数 	（负整数，0）
        }else if(type.equals("-")) {
        	eL = "^-\\d*[1-9]\\d*$";//负整数 
        }else if(type.equals("-0+")){
        	eL = "^-?\\d+$";//整数  （负整数，0，正整数）
        }else{
        	eL = "^\\d+$|^\\d+\\.\\d+$|-\\d+$|-\\d+\\.\\d+$";//浮点数和整数
        } 
        Pattern p = Pattern.compile(eL);   
        Matcher m = p.matcher(num);   
        return m.matches();   
    } 
    
    /**
	 * DES加密
	 * @param plainData
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	public static String encryption(String plainData) throws Exception{
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));
			byte[] buf = cipher.doFinal(plainData.getBytes());
			return Base64Utils.encode(buf);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * DES解密
	 */
	public static String decryption(String secretData) throws Exception{
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(DES_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
			byte[] buf = cipher.doFinal(Base64Utils.decode(secretData.toCharArray()));
			return new String(buf,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得密钥
	 */
	private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException,InvalidKeyException,InvalidKeySpecException{
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
		DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
		keyFactory.generateSecret(keySpec);
		return keyFactory.generateSecret(keySpec);
	}
	public static void main(String[] args) {
		try {
			String ss=StrKit.encryption("aaa");
			System.out.println(ss);
			String aa=StrKit.decryption("M3O6BfF6/BI=");
			System.out.println(aa);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	static class Base64Utils {

		static private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
		static private byte[] codes = new byte[256];
		static {
			for (int i = 0; i < 256; i++)
				codes[i] = -1;
			for (int i = 'A'; i <= 'Z'; i++)
				codes[i] = (byte) (i - 'A');
			for (int i = 'a'; i <= 'z'; i++)
				codes[i] = (byte) (26 + i - 'a');
			for (int i = '0'; i <= '9'; i++)
				codes[i] = (byte) (52 + i - '0');
			codes['+'] = 62;
			codes['/'] = 63;
		}
		
		/**
		 * 将原始数据编码为base64编码
		 */
		static public String encode(byte[] data) {
			char[] out = new char[((data.length + 2) / 3) * 4];
			for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
				boolean quad = false;
				boolean trip = false;
				int val = (0xFF & (int) data[i]);
				val <<= 8;
				if ((i + 1) < data.length) {
					val |= (0xFF & (int) data[i + 1]);
					trip = true;
				}
				val <<= 8;
				if ((i + 2) < data.length) {
					val |= (0xFF & (int) data[i + 2]);
					quad = true;
				}
				out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
				val >>= 6;
				out[index + 1] = alphabet[val & 0x3F];
				val >>= 6;
				out[index + 0] = alphabet[val & 0x3F];
			}
			
			return new String(out);
		}

		/**
		 * 将base64编码的数据解码成原始数据
		 */
		static public byte[] decode(char[] data) {
			int len = ((data.length + 3) / 4) * 3;
			if (data.length > 0 && data[data.length - 1] == '=')
				--len;
			if (data.length > 1 && data[data.length - 2] == '=')
				--len;
			byte[] out = new byte[len];
			int shift = 0;
			int accum = 0;
			int index = 0;
			for (int ix = 0; ix < data.length; ix++) {
				int value = codes[data[ix] & 0xFF];
				if (value >= 0) {
					accum <<= 6;
					shift += 6;
					accum |= value;
					if (shift >= 8) {
						shift -= 8;
						out[index++] = (byte) ((accum >> shift) & 0xff);
					}
				}
			}
			if (index != out.length)
				throw new Error("miscalculated data length!");
			return out;
		}
	}
	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world->HelloWorld
	 * @param name  转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(String name) {
		if (name == null) {
			return null;
		}
		if (name.contains(UNDERLINE)) {
			name = name.toLowerCase();
			StringBuilder sb = new StringBuilder(name.length());
			boolean upperCase = false;
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);
				if (c == '_') {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else
			return name;
	}
	/**
	 * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HelloWorld->hello_world
	 * @param camelCaseStr 转换前的驼峰式命名的字符串
	 * @return 转换后下划线大写方式命名的字符串
	 */
	public static String toUnderlineCase(String camelCaseStr) {
		if (camelCaseStr == null) {
			return null;
		}
		final int length = camelCaseStr.length();
		StringBuilder sb = new StringBuilder();
		char c;
		boolean isPreUpperCase = false;
		for (int i = 0; i < length; i++) {
			c = camelCaseStr.charAt(i);
			boolean isNextUpperCase = true;
			if (i < (length - 1)) {
				isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
			}
			if (Character.isUpperCase(c)) {
				if (!isPreUpperCase || !isNextUpperCase) {
					if (i > 0)
						sb.append(UNDERLINE);
				}
				isPreUpperCase = true;
			} else {
				isPreUpperCase = false;
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}
	/**
	 * 将byte数组转为字符串
	 * @param bytes byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(byte[] bytes, String charset) {
		return new String(bytes, Charset.forName(charset));
	}
	/**
	 * 编码字符串
	 * @param str 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] encode(String str, String charset) {
		if (str == null) {
			return null;
		}
		if (isBlank(charset)) {
			return str.getBytes();
		}
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(format("Charset [{}] unsupported!", charset));
		}
	}

	/**
	 * 解码字节码
	 * @param data   字符串
	 * @param charset  字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String decode(byte[] data, String charset) {
		if (data == null) {
			return null;
		}
		if (isBlank(charset)) {
			return new String(data);
		}
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(format("Charset [{}] unsupported!", charset));
		}
	}

	/**
	 * 格式化文本
	 * @param template  文本模板，被替换的部分用 {key} 表示
	 * @param map  参数值对
	 * @return 格式化后的文本
	 */
	public static String format(String template, Map<?, ?> map) {
		if (null == map || map.isEmpty()) {
			return template;
		}
		for (Entry<?, ?> entry : map.entrySet()) {
			template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
		}
		return template;
	}
	/**
	 * 格式化文本
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values 参数值
	 * @return 格式化后的文本
	 */
	public static String format(String template, Object... values) {
		if (values==null||values.length==0 || isBlank(template)) {
			return template;
		}
		final StringBuilder sb = new StringBuilder();
		final int length = template.length();
		int valueIndex = 0;
		char currentChar;
		for (int i = 0; i < length; i++) {
			if (valueIndex >= values.length) {
				sb.append(sub(template, i, length));
				break;
			}
			currentChar = template.charAt(i);
			if (currentChar == '{') {
				final char nextChar = template.charAt(++i);
				if (nextChar == '}') {
					sb.append(values[valueIndex++]);
				} else {
					sb.append('{').append(nextChar);
				}
			} else {
				sb.append(currentChar);
			}

		}
		return sb.toString();
	}
	/**
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" example: abcdefgh 2 3 -> c abcdefgh 2 -3 -> cde
	 * 
	 * @param string   String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex 结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(String string, int fromIndex, int toIndex) {
		int len = string.length();

		if (fromIndex < 0) {
			fromIndex = len + fromIndex;

			if (toIndex == 0) {
				toIndex = len;
			}
		}
		if (toIndex < 0) {
			toIndex = len + toIndex;
		}
		if (toIndex < fromIndex) {
			int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}
		if (fromIndex == toIndex) {
			return EMPTY;
		}

		char[] strArray = string.toCharArray();
		char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
		return new String(newStrArray);
	}
}
