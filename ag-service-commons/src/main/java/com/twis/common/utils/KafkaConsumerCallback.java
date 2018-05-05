package com.twis.common.utils;

/**
 * 消息订阅回调
 * @author liufang
 *
 */
public interface KafkaConsumerCallback {

	public void run(String message); 
}
