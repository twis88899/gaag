package com.twis.common.utils.sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.util.StringUtils;

import com.twis.common.utils.SendResult;

public class SmsLTServiceImpl implements SmsService {
	@Override
	public SendResult sendSMS(Map<String, Object> map) throws Exception {
		String account = map.get("account").toString();
		String pwd = map.get("password").toString();
		String url = map.get("url").toString();
		String content = map.get("content").toString();
		String ext = map.get("ext").toString();
		String telephone = map.get("telephone").toString();
		String repcode = map.get("repcode").toString();
		return send(account, url, pwd, telephone, content, repcode, ext);
	}

	public SendResult send(String account, String url, String pwd, String to, String content, String repcode,
			String ext) {
		SendResult sendResult = new SendResult();
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod(url);
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "gbk");
			post.addParameter("SpCode", repcode);
			post.addParameter("LoginName", account);
			post.addParameter("Password", pwd);
			post.addParameter("MessageContent", content);
			post.addParameter("UserNumber", to);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSS");
			post.addParameter("SerialNumber", sdf.format(new Date()));
			post.addParameter("ScheduleTime", "");
			post.addParameter("ExtendAccessNum", ext);
			post.addParameter("f", "1");
			httpclient.executeMethod(post);
			sendResult = parseInfo(new String(post.getResponseBody(), "gbk"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResult;
	}

	public SendResult parseInfo(String info) {
		SendResult sendResult = new SendResult();
		if (info.contains("&")) {
			String[] res = info.split("&");
			if (res.length == 2) {
				if (StringUtils.hasText(res[0]) && res[0].contains("=")) {
					String result = res[0].split("=")[0];
					String resultVal = res[0].split("=")[1];
					if (result.equals("result")) {
						sendResult.setStatus(Integer.parseInt(resultVal));
						sendResult.setStatusCode(Integer.parseInt(resultVal));
					}
				}
				if (StringUtils.hasText(res[1]) && res[1].contains("=")) {
					String description = res[1].split("=")[0];
					String descriptionVal = res[1].split("=")[1];
					if (description.equals("description")) {
						sendResult.setDescription(descriptionVal);
					}
				}
			} else if (res.length == 3) {
				if (StringUtils.hasText(res[0]) && res[0].contains("=")) {
					String result = res[0].split("=")[0];
					String resultVal = res[0].split("=")[1];
					if (result.equals("result")) {
						sendResult.setStatus(Integer.parseInt(resultVal));
						sendResult.setStatusCode(Integer.parseInt(resultVal));
					}
				}
				if (StringUtils.hasText(res[1]) && res[1].contains("=")) {
					String description = res[1].split("=")[0];
					String descriptionVal = res[1].split("=")[1];
					if (description.equals("description")) {
						sendResult.setDescription(descriptionVal);
					}
				}
				if (StringUtils.hasText(res[2]) && res[2].contains("=")) {
					String[] taskidsOrFailList = res[2].split("=");
					if (taskidsOrFailList[0].equals("taskid")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setTaskid("");
						} else {
							sendResult.setTaskid(taskidsOrFailList[1]);
						}
					} else if (taskidsOrFailList[0].equals("faillist")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setFaillist("");
						} else {
							sendResult.setFaillist(taskidsOrFailList[1]);
						}
					}
				}
			} else if (res.length > 3) {
				if (StringUtils.hasText(res[0]) && res[0].contains("=")) {
					String result = res[0].split("=")[0];
					String resultVal = res[0].split("=")[1];
					if (result.equals("result")) {
						sendResult.setStatus(Integer.parseInt(resultVal));
						sendResult.setStatusCode(Integer.parseInt(resultVal));
					}
				}
				if (StringUtils.hasText(res[1]) && res[1].contains("=")) {
					String description = res[1].split("=")[0];
					String descriptionVal = res[1].split("=")[1];
					if (description.equals("description")) {
						sendResult.setDescription(descriptionVal);
					}
				}
				if (StringUtils.hasText(res[2]) && res[2].contains("=")) {
					String[] taskidsOrFailList = res[2].split("=");
					if (taskidsOrFailList[0].equals("taskid")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setTaskid("");
						} else {
							sendResult.setTaskid(taskidsOrFailList[1]);
						}
					} else if (taskidsOrFailList[0].equals("faillist")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setFaillist("");
						} else {
							sendResult.setFaillist(taskidsOrFailList[1]);
						}
					}
				}

				if (StringUtils.hasText(res[3]) && res[3].contains("=")) {
					String[] taskidsOrFailList = res[3].split("=");
					if (taskidsOrFailList[0].equals("taskid")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setTaskid("");
						} else {
							sendResult.setTaskid(taskidsOrFailList[1]);
						}
					} else if (taskidsOrFailList[0].equals("faillist")) {
						if (taskidsOrFailList.length == 1) {
							sendResult.setFaillist("");
						} else {
							sendResult.setFaillist(taskidsOrFailList[1]);
						}
					}

				}
			}

		}
		return sendResult;
	}

}
