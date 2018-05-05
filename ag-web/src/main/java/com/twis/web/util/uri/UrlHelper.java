package com.twis.web.util.uri;

/**
 * 缺省的实现
 * @author yxm
 * @since 2018/3/18
 */
public class UrlHelper {

	/**
	 * 归一化请求URL
	 * 
	 * @param requestURL
	 * @return
	 */
	public static String normalizedUrl(String requestURL) {
		StringBuffer sb = new StringBuffer();
		if (!requestURL.startsWith("/")) {
			sb.append("/");
		}
		String url = requestURL;
		if (requestURL.endsWith("/") && requestURL.length() > 1) {
			int index = requestURL.lastIndexOf("/");
			url = requestURL.substring(0, index);
		}
		sb.append(url);
		return sb.toString();
	}

}