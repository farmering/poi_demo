package excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;

public class JsonUtil{
	public enum Message{ERROR,SUCCESS};
	
	public static void ajaxJsonMessage(HttpServletResponse response,Map<Object, Object> map) throws IOException {
        String json = JSON.toJSONString(map);
        ajax(response,json);
	}
	
	public static void ajaxJsonMessage(HttpServletResponse response,String message, Message msgType) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		String contenttype =null;
		if(Message.ERROR == msgType){
			contenttype = "error"; 
		} else if(Message.SUCCESS == msgType){
			contenttype = "success"; 
		}
		jsonMap.put("status", contenttype);
		jsonMap.put("message", message);
		
		String json = JSON.toJSONString(jsonMap);
		ajax(response,json);
	}
	/**
	 * AJAX输出，返回null
	 */
	private static void ajax(HttpServletResponse response,String content) {
		try {
			String contenttype= "application/json"; 
			response.setContentType(contenttype + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
            response.getWriter().write(content);  
            response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
