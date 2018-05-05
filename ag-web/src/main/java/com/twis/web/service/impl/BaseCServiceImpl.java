package com.twis.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twis.common.utils.JsonResult;
import com.twis.web.service.BaseService;
import com.twis.web.util.JsonMapper;
import com.twis.web.util.ServletsUtil;
import com.twis.web.util.http.HttpUtil;
import com.twis.web.util.uri.ServiceEnum;
import com.twis.web.util.uri.UrlHelper;

import lombok.Getter;
import lombok.Setter;

@Service
public abstract class BaseCServiceImpl implements BaseService {
	
	@Value("${service.protocol}")
	@Getter @Setter private String protocol;
	@Value("${service.address}")
	@Getter @Setter private String address;
	@Value("${service.port}")
	@Getter @Setter private String port;
	@Value("${service.version}")
	@Getter @Setter private String version;
	@Value("${service.filePort}")
	@Getter @Setter private String filePort;
	@Value("${remoteService}")
	@Getter @Setter private String remoteService;


	private String getFullAddress() {
		return this.protocol + "://" + this.address + ":" + port;
	}
	
	public ServiceEnum removeService() {
		if(this.remoteService.equalsIgnoreCase("local")) {
			return ServiceEnum.Local;
		} else if (this.remoteService.equalsIgnoreCase("remote")) {
			return ServiceEnum.Remote;
		}
		return null;
	}

	public String buildUrl(String url) {
		String normalizedUrl = UrlHelper.normalizedUrl(url);
		return getFullAddress() + normalizedUrl;
	}
	public String buildUrl(String url,String port) {
		String normalizedUrl = UrlHelper.normalizedUrl(url);
		return this.protocol+"://"+ this.address + ":" + port + normalizedUrl;
	}
	public String buildImgUrl(String url,String fileName,String token) {
		return buildUrl(url)+"/"+fileName+"/"+token+"?version="+version;
	}

	public String buildFileUrl(String url, String token) {
		return buildUrl(url).replace(":"+port,":"+filePort)+"/"+token+"?version="+version;
	}

	public String buildFileDownloadUrl(String url, String token) {
		return buildUrl(url).replace(":"+port,":"+filePort)+"/";
	}
	
	/**
	 * 用于分布式远程服务调用-是否需要拼version
	 * 应自定义ServiceException 
	 * @param obj
	 * @param url
	 * @return
	 */
	public JsonResult invokeRemoteService(Map<String,Object> parameter, String url) throws Exception {
		String res = HttpUtil.post(buildUrl(url),new HashMap<String, Object>(),ServletsUtil.getToken());
		return JsonMapper.buildNormalMapper().fromJson(res, JsonResult.class);
	}
	
}
