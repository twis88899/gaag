package com.twis.common.utils.sms;

import java.util.Map;

import com.twis.common.utils.SendResult;

public interface SmsService {
	public SendResult sendSMS(Map<String,Object> map) throws Exception;
}
