package util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;


/**
 * 客户端 IP获取工具
 *
 */
public class IPUtil {

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public static final String getIp(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")){
				InetAddress inetAddress = null;
            	try {
            		inetAddress = InetAddress.getLocalHost();
            	} catch (UnknownHostException e) {
            		e.printStackTrace();
            	}
            	ip = inetAddress.getHostAddress();
           }
		}
	    if(null != ip && ip.length() > 15){
           if(ip.indexOf(",") > 0){
        	   ip = ip.substring(0, ip.indexOf(","));
           }
        }
		return ip;
	}
	/**
	 * 获取Mac地址
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public static final String getMAC() throws UnknownHostException, SocketException {
		InetAddress ia = InetAddress.getLocalHost();
	    byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
         //下面代码是把mac地址拼装成String  
         StringBuffer sb = new StringBuffer();  
         for(int i=0;i<mac.length;i++){  
             if(i!=0){  
                 sb.append("-");  
             }  
             //mac[i] & 0xFF 是为了把byte转化为正整数  
             String s = Integer.toHexString(mac[i] & 0xFF);  
             sb.append(s.length()==1?0+s:s);  
         }
		return sb.toString().toUpperCase();    
	}
	/**
	 * 获取浏览器信息 及版本号
	 * @param request
	 * @return
	 * @throws UnknownHostException 
	 * @throws SocketException 
	 */
	public static final String getBrowser(HttpServletRequest request) throws IOException{
		//获取浏览器信息
		Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
		//获取浏览器版本号
		Version version = browser.getVersion(request.getHeader("User-Agent"));
		String info = browser.getName() + "/" + version.getVersion();
		return info;
	}

}
