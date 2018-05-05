package com.twis.common.utils.httpclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author yxm
 *
 */
public class HttpClient {

	private CloseableHttpClient client = HttpClients.createDefault();
	private HttpClientEnum httpClientEnum;

	
	public HttpClient(HttpClientEnum httpClientEnum) {
		this.httpClientEnum = httpClientEnum;
	}
	
	public String httpClientExecute(String url,String port, String version, String path, String json) {
		return httpClientExecute(url, port, version, path,json, false);
	}

	public String httpClientExecute(String url, String port, String version, String path, String json,Boolean isChekLogin) {
		String responseString =  "{\"success\": false,\"msg\": \"数据请求失败！\",\"errorCode\": \"-1\"}";
		
		try {
			HashMap<String, Object> parameter = new HashMap<String, Object>();
			if(StringUtils.hasText(json)){
				parameter = stringToMap(json);
			}
//			if(isChekLogin&&Authentication.token.equals("")){
//				if(!new Authentication().login()){
//					return "{\"success\": false,\"msg\": \"登陆失败！\",\"errorCode\": \"-1\"}";
//				}
//			}
//			parameter.put("token", Authentication.token);
			
			
			CloseableHttpClient client = HttpClients.createDefault();

			URIBuilder uri = new URIBuilder();
			uri.setScheme("http").setHost(url).setPort(Integer.parseInt(port)).setPath(path);
			

			HttpRequestBase httpRequestBase = null;
			if (httpClientEnum.equals(HttpClientEnum.GET)) {
				httpRequestBase = new HttpGet(uri.build());
				for (String key : parameter.keySet()) {
					uri.addParameter(key, parameter.toString());
				}
			} else if (httpClientEnum.equals(HttpClientEnum.POST)) {
				HttpPost httpPost = new HttpPost(uri.build());
				StringEntity stringEntity = new StringEntity(ObjectToJson(parameter), "UTF-8");
				stringEntity.setContentEncoding("UTF-8");    
				stringEntity.setContentType("application/json");
				httpPost.setEntity(stringEntity);
				httpRequestBase = httpPost;
			}

			httpRequestBase.setHeader("version", version);

			CloseableHttpResponse response = client.execute(httpRequestBase);
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "{\"success\": false,\"msg\": \"数据请求失败！\",\"errorCode\": \"-1\"}";
		}
		return responseString;
	}
	
	public String httpClientExecute(String url, String port, String version,String path, Map<String, Object> parameter)  {
		return httpClientExecute(url, port, version, path, parameter,false);
	}
	
	public String httpClientExecute(String url, String port, String version, String path, Map<String, Object> parameter,Boolean isChekLogin)  {
		try {
			return httpClientExecute(url, port, version, path,ObjectToJson(parameter),isChekLogin);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{\"success\": false,\"msg\": \"数据请求失败！\",\"errorCode\": \"-1\"}";
		}
	}
	
	private String ObjectToJson(Object parameter) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(parameter);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private HashMap<String,Object> stringToMap(String json) throws JsonParseException, JsonMappingException, IOException{
		return new ObjectMapper().readValue(json, HashMap.class);
	}

}
