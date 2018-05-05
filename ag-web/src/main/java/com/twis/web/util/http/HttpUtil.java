
package com.twis.web.util.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import com.twis.web.util.FileUtil;
import com.twis.web.util.SysHelper;
import com.twis.web.util.security.Token;

/**
 * @author yswh
 * @since 2015/6/18 13:55
 */
@Repository
public class HttpUtil {

	private static final String DEFAULT_CHARSET_UTF8 = "UTF-8";
	private static final String APPLICATION_JSON = "application/json";
	//private static final String FORM_DATA = "multipart/form-data";
	public static final String VERSION = "version";
	public static String VERSION_V = "v2.0.0";
	
	private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

	public HttpUtil() {
		VERSION_V = SysHelper.getProperty("service.version", "/fit-configs.properties");
	}

	public static String get(String url) {
		return get(url, null, null);
	}

	public static String get(String url, Map<String, String> queryParas) {
		return get(url, queryParas, null);
	}

	public static String postFile(CommonsMultipartFile files,Token token,String url,Map<String,String> headers) throws IOException, URISyntaxException {
    	
//		URIBuilder uri = new URIBuilder();
//		uri.setScheme("http").setHost("192.168.1.40").setPort(Integer.valueOf("8083")).setPath("/file/upload/343e-53f3-4f35-45f3-4fef");
//		HttpPost httpPost = new HttpPost(uri.build());
		
		HttpPost httpPost = new HttpPost(url);
        setHeadersFile(httpPost, headers);//设置头信息 这里无需设置CONTENT_TYPE 类型
			MultipartEntity reqEntity = new MultipartEntity();
        	byte[] encodeBase64 = Base64.encodeBase64(files.getBytes());
        	InputStream inputStream =files.getInputStream();
        	ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
        	byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据 
        	int rc = 0; 
        	while ((rc = inputStream.read(buff, 0, 100)) > 0) { 
        	swapStream.write(buff, 0, rc); 
        	} 
        	byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果 
			reqEntity.addPart("file_upload",new ByteArrayBody(in_b,files.getFileItem().getName()));
			
        reqEntity.addPart("token", new StringBody(token.getToken()));
		httpPost.setEntity(reqEntity);
		
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String result = "";
       // CloseableHttpResponse response = null;
        try {
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(
                        final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            result = httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
        	e.printStackTrace();
            log.error("请求出错,url = " + httpPost.getURI(), e);
        } finally {
        	if (httpClient != null)
        		httpClient.close();
           // HttpClientUtils.closeQuietly(response);
            log.debug("请求接口，url = " + httpPost.getURI()+ "\n");
        }
        return result;
    	
    }
	
	public static void main(String[] args) throws IOException, URISyntaxException {  
        // TODO Auto-generated method stub  
          
//        HttpUtil httpPostArgumentTest2 = new HttpUtil();  
//          
//        httpPostArgumentTest2.SubmitPost("http://192.168.1.166:8083/file/upload/5f744a98-21b7-492e-a001-4118a088be46?version=v2.0.0",  
//                "test.zip","f://1.png");  
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("version", "v2.0.0");
		System.out.println(HttpUtil.postFile(null, null, null, headers));
    }  
	
	
	public void SubmitPost(String url,String filename1, String filepath){  
        
        HttpClient httpclient = new DefaultHttpClient();  
          
        try {  
      
            HttpPost httppost = new HttpPost(url);  
              
            FileBody bin = new FileBody(new File(filepath));  
                
              
            StringBody comment = new StringBody(filename1);  
  
            MultipartEntity reqEntity = new MultipartEntity(); 
              
//            reqEntity.addPart("file1", bin);//file1为请求后台的File upload;属性      
//             reqEntity.addPart("filename1", comment);//filename1为请求后台的普通参数;属性     
//            httppost.setEntity(reqEntity);  
              
            
            
            
            HttpResponse response = httpclient.execute(httppost);  
              
                  
            int statusCode = response.getStatusLine().getStatusCode();  
              
                  
            if(statusCode == HttpStatus.SC_OK){  
                      
                System.out.println("服务器正常响应.....");  
                  
                HttpEntity resEntity = response.getEntity();  
                  
                  
                System.out.println(EntityUtils.toString(resEntity));//httpclient自带的工具类读取返回数据  
                  
                  
                  
                System.out.println(resEntity.getContent());     
  
                EntityUtils.consume(resEntity);  
            }  
                  
            } catch (ParseException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                try {   
                    httpclient.getConnectionManager().shutdown();   
                } catch (Exception ignore) {  
                      
                }  
            }  
	}
	
	/**
	 * 发送GET请求
	 */
	public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(buildUrlWithQueryString(url, queryParas));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		setHeaders(httpGet, headers);
		String result = "";
		//CloseableHttpResponse response = null;
		try {
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			result = httpClient.execute(httpGet, responseHandler);
		} catch (IOException e) {
			log.error("请求出错,url = " + httpGet.getURI(), e);
		} finally {
				try {
					
					if (httpClient != null)
						httpClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//HttpClientUtils.closeQuietly(response);
		}
		return result;
	}

	public static String post(String url) {
		return post(url, null, new HashMap<String, String>());
	}

	public static String post(String url, Map<String, Object> params) {
		return post(url, params, new HashMap<String, String>());
	}

	public static String post(String url, Map<String, Object> params, Token token) {
		params.put("token", token.getToken());
		return post(url, params, new HashMap<String, String>());
	}

	public static String post(String url, String params, Token token) {
		return postJson(url, params, new HashMap<String, String>(), token);
	}

	public static String post(String url, String params) {
		// params.substring(params.length()-1, params.length())+
		return postJson(url, params, null);
	}

	/**
	 * 发送POST请求
	 */
	public static String post(String url, Map<String, Object> queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);

		setHeaders(httpPost, headers);

		setEntity(httpPost, queryParas);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String result = "";
		//CloseableHttpResponse response = null;
		try {
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			result = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			log.error("请求出错,url = " + httpPost.getURI(), e);
		} finally {
			//HttpClientUtils.closeQuietly(response);
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.debug("请求接口，url = " + httpPost.getURI() + "\n" + "参数：" + queryParas.toString());
		}
		return result;
	}
	
	/**
	 *获取请求的二进制
	 */
	public static ByteArrayOutputStream postByByteArrayOutputStream(String url, Map<String, Object> queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);
		setHeaders(httpPost, headers);
		setEntity(httpPost, queryParas);
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpResponse result = null;
		try {
			result = httpClient.execute(httpPost);
			int status = result.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300) {
				return FileUtil.getByteArrayOutputStream(result.getEntity().getContent());
			} else {
				throw new ClientProtocolException("Unexpected response status: " + status);
			}	
		} catch (IOException e) {
			log.error("请求出错,url = " + httpPost.getURI(), e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//HttpClientUtils.closeQuietly(response);
			log.debug("请求接口，url = " + httpPost.getURI() + "\n" + "参数：" + queryParas.toString());
		}
		return null;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param queryParas
	 * @param headers
	 * @return
	 */
	public static String postJson(String url, String queryParas, Map<String, String> headers) {
		HttpPost httpPost = new HttpPost(url);

		setHeaders(httpPost, headers);

		StringEntity entity = new StringEntity(queryParas, DEFAULT_CHARSET_UTF8);
		entity.setContentType(APPLICATION_JSON);
		entity.setContentEncoding(DEFAULT_CHARSET_UTF8);
		httpPost.setEntity(entity);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String result = "";
		//CloseableHttpResponse response = null;
		try {
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			result = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			log.error("请求出错,url = " + httpPost.getURI(), e);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//HttpClientUtils.closeQuietly(response);
			log.debug("请求接口，url = " + httpPost.getURI() + "\n" + "参数：" + queryParas);
		}
		return result;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param queryParas
	 * @param headers
	 * @return
	 */
	public static String postJson(String url, String queryParas, Map<String, String> headers, Token token) {
		HttpPost httpPost = new HttpPost(url);

		setHeaders(httpPost, headers);
		JSONObject jsonObject = JSON.parseObject(queryParas);
		jsonObject.put("token", token.getToken());
		String in = jsonObject.toJSONString();

		StringEntity entity = new StringEntity(in, DEFAULT_CHARSET_UTF8);
		entity.setContentType(APPLICATION_JSON);
		entity.setContentEncoding(DEFAULT_CHARSET_UTF8);
		httpPost.setEntity(entity);

		CloseableHttpClient httpClient = HttpClients.createDefault();

		String result = "";
		//CloseableHttpResponse response = null;
		try {
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			result = httpClient.execute(httpPost, responseHandler);
		} catch (IOException e) {
			log.error("请求出错,url = " + httpPost.getURI(), e);
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//HttpClientUtils.closeQuietly(response);
			log.debug("请求接口，url = " + httpPost.getURI() + "\n" + "参数：" + queryParas);
		}
		return result;
	}

	private static void setHeaders(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		// 设置默认Header
		// httpRequestBase.setHeader(VERSION, VERSION_V);
		httpRequestBase.addHeader(VERSION, VERSION_V);
		httpRequestBase.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);

		// 设置传入的Header
		if (MapUtils.isNotEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (VERSION.equals(entry.getKey())) {
					httpRequestBase.setHeader(VERSION, VERSION_V);
					continue;
				}
				httpRequestBase.addHeader(entry.getKey(), entry.getValue());
			}
		}

	}
	
	private static void setHeadersFile(HttpRequestBase httpRequestBase, Map<String, String> headers) {
		// 设置默认Header
		// httpRequestBase.setHeader(VERSION, VERSION_V);
		httpRequestBase.addHeader(VERSION, VERSION_V);
//		httpRequestBase.addHeader(HTTP.CONTENT_TYPE,FORM_DATA);

		// 设置传入的Header
		if (MapUtils.isNotEmpty(headers)) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (VERSION.equals(entry.getKey())) {
					httpRequestBase.setHeader(VERSION, VERSION_V);
					continue;
				}
				httpRequestBase.addHeader(entry.getKey(), entry.getValue());
			}
		}

	}

	/**
	 * 构建URL的查询参数
	 */
	private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
		if (queryParas == null || queryParas.isEmpty())
			return url;

		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf("?") == -1) {
			isFirst = true;
			sb.append("?");
		} else {
			isFirst = false;
		}

		for (Map.Entry<String, String> entry : queryParas.entrySet()) {
			if (isFirst)
				isFirst = false;
			else
				sb.append("&");

			String key = entry.getKey();
			String value = entry.getValue();
			if (StringUtils.isNotBlank(value))
				try {
					value = URLEncoder.encode(value, DEFAULT_CHARSET_UTF8);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}

	/**
	 * 读取返回结果
	 *
	 * @param httpEntity
	 * @return
	 */
	@SuppressWarnings("unused")
	private static String readResponseString(HttpEntity httpEntity) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_CHARSET_UTF8));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void setEntity(HttpPost httpPost, Map<String, Object> queryParas) {
		// json参数
		JSONObject json = new JSONObject();
		if (MapUtils.isNotEmpty(queryParas)) {
			json.putAll(queryParas);
		}
		StringEntity entity = new StringEntity(json.toString(), DEFAULT_CHARSET_UTF8);
		entity.setContentType(APPLICATION_JSON);
		entity.setContentEncoding(DEFAULT_CHARSET_UTF8);
		httpPost.setEntity(entity);

	}

//	public static void main(String[] args) {
		/*
		 * Map<String, Object> params = new HashMap<String, Object>();
		 * params.put("currentPage", "1"); String responseStr = HttpUtil.post(
		 * "http://172.30.134.109:8081/servGrid/queryServGridAndGetCompanyInfo",
		 * params,null); System.out.println(responseStr+"   << ");
		 */
//	}
}