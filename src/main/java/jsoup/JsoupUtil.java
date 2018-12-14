package jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class JsoupUtil {
	private static final String username = "test";
	private static final String password = "aaaaaa";
	private static final String baseUrl = "http://www.test.com/";
	private static final String host = "127.0.0.1:9080";
	private static final String cookieUrl = baseUrl;
	private static final String loginUrl = baseUrl + "login";
	private static final String listUrl = baseUrl + "list";
	private static final String infoUrl = baseUrl + "detail?id={0}&name={1}";

	public static final Map<String, String> initCookies() {
		try {
			Connection conn = Jsoup.connect(cookieUrl);
			conn.method(Method.GET);
			conn.followRedirects(false);
			return conn.execute().cookies();
		} catch (Exception e) {
			return null;
		}
	}

	public static int login(Map<String, String> cookies) {

		try {
			Connection conn = Jsoup.connect(loginUrl);
			initHeader(conn);
			conn.cookies(cookies);
			conn.method(Method.POST);
			conn.followRedirects(false);
			conn.ignoreContentType(true);
			conn.data("name", username);
			conn.data("pwd", password);
			Response response = conn.execute();
			return response.statusCode();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static final void initHeader(Connection conn) {
		conn.header("Host", host);
		conn.header("Connection", "keep-alive");
		conn.header("Cache-Control", "max-age=0");
		conn.header("Upgrade-Insecure-Requests", "1");
		conn.header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		conn.header("Accept-Language", "zh-CN,zh;q=0.8");
	}

	/**
	 * 列表信息
	 */
	public static Response list(Map<String, String> cookies) throws IOException {
		Connection conn = Jsoup.connect(listUrl);
		initHeader(conn);
		conn.method(Method.POST);
		conn.cookies(cookies);
		Map<String, String> datas = new HashMap<String, String>();
		datas.put("createdate", "desc");
		conn.data(datas);
		conn.timeout(120000);
		return conn.execute();
	}

	/**
	 * 详细信息
	 */
	public static Response detail(Map<String, String> cookies, String id) throws IOException {
		Connection conn = Jsoup.connect(infoUrl);
		initHeader(conn);
		conn.cookies(cookies);
		conn.method(Method.GET);
		conn.timeout(120000);
		return conn.execute();
	}

	public static void main(String[] args) throws Exception {
            Map<String, String> cookies = null;
            cookies = initCookies(); 
            int status = login(cookies);
            Response resp = list(cookies);
            String body = resp.body();   
    }
}