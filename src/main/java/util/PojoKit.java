package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PojoKit {

/**
 * map转为对象
 * @param clzss
 * @param map
 * @return
 * @throws Exception
 */
public static <T> T mapTobject(Class<T> clzss, Map<String, Object> map) throws Exception {
		T t = clzss.newInstance();
		Field[] fields = clzss.getDeclaredFields();
		for (int i = 0, length = fields.length; i < length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			Set<String> keys = map.keySet();
			Iterator<String> keyI = keys.iterator();
			while (keyI.hasNext()) {
				String key = keyI.next();
				if (fieldName.equalsIgnoreCase(key)) {
					String firstFieldNameLetter = fieldName.substring(0, 1).toUpperCase();
					String setMethodName = "set" + firstFieldNameLetter + fieldName.substring(1);
					Method setMethod = clzss.getMethod(setMethodName, field.getType());
					setMethod.invoke(t, map.get(key));
				}
			}
		}
		return t;
	}
/**
 * 将任意pojo对象转化成map
 * 
 * @return
 */
public static <T>  Map<String, Object> convert2Map(T t){
	Map<String, Object> result = new HashMap<String, Object>();
	Method[] methods = t.getClass().getMethods();
	try {
		for (Method method : methods) {
			Class<?>[] paramClass = method.getParameterTypes();
			if (paramClass.length > 0) { // 如果方法带参数，则跳过
				continue;
			}
			String methodName = method.getName() ;
			if (methodName.startsWith("get")) {
				Object value = method.invoke(t);
				result.put(methodName, value);
			}
		}
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	} catch (SecurityException e) {
		e.printStackTrace();
	} 
		return result;
}
/**
 * 将任意pojo对象转化成map
 * 返回key为 pojo属性
 */
public static <T>  Map<String, Object> convertToMap(T t){
	Map<String, Object> result = new HashMap<String, Object>();
	Method[] methods = t.getClass().getMethods();
	try {
		for (Method method : methods) {
			Class<?>[] paramClass = method.getParameterTypes();
			if (paramClass.length > 0) { // 如果方法带参数，则跳过
				continue;
			}
			String methodName = method.getName() ;
			if (methodName.startsWith("get")) {
				Object value = method.invoke(t);
				String name=StrKit.toCamelCase(methodName.substring(3));
				result.put(name.substring(0,1).toLowerCase()+name.substring(1), value);
			}
		}
	} catch (IllegalArgumentException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		e.printStackTrace();
	} catch (SecurityException e) {
		e.printStackTrace();
	} 
		return result;
}
public static String toEntityName(String ss) {
	String s = StrKit.firstCharToUpperCase(ss.toLowerCase());
	if (StrKit.isBlank(s)) {
		return "";
	} else {
		StringBuffer sb = new StringBuffer();
		char[] cs = s.toCharArray();
		if (cs != null && cs.length > 0) {
			boolean o = false;
			for (char c : cs) {
				if (c == '_') {
					o = true;
				} else {
					if (o) {
						sb.append(Character.toUpperCase(c));
						o = false;
					} else {
						sb.append(c);
					}
				}
			}
		}
		return sb.toString();
	}
}


}

