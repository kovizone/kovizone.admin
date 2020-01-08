package com.kovizone.admin.util;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http工具类
 *
 * @author KoviChen
 * @version 0.0.1 2019-08-13 KoviChen 新建类
 */
public class HttpUtils {

	private static final String XML_HTTP_REQUEST = "XMLHttpRequest";

	/**
	 * 判断Http请求是否为AJAX请求
	 * 
	 * @param request Http请求
	 * @return 是否为AJAX请求
	 */
	public static boolean isAJAX(ServletRequest request) {
		String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
		if (XML_HTTP_REQUEST.equalsIgnoreCase(header)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 简化session id
	 * 
	 * @param session 会话
	 * @return 简化session id
	 */
	public static String getSessionSimpleId(HttpSession session) {
		if (session == null) {
			return "Sessionless";
		}
		String sessionId = session.getId();
		return sessionId.substring(0, 6) + sessionId.substring(sessionId.length() - 5);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	public static String post(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
