package com.twis.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

/**
 * 
 * @author liufang
 *
 */

public class KafkaConsumer extends Thread {

	private ConsumerConnector consumer;

	private static Properties pro;

	private String _topic;
	private KafkaConsumerCallback _kafkaConsumerCallback;
	private static int i =0;
	private Properties getPro() {
		try {
			if (pro == null) {
				pro = new Properties();
				List<String> list = new RedisUtils().lrange("KafkaConsumer_s");
				for (String str : list) {
					pro.put(str.split("=")[0], str.split("=")[1]);
				}
				i=0;
			}
		} catch (Exception e) {
			pro = null;
			++i;
			if (i <= 100) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				return getPro();
			} else {
				e.printStackTrace();
			}
		}
		return pro;
	}

	public KafkaConsumer() {
		this(null);
	}

	public KafkaConsumer(String groupId) {
		super();
		getPro();
		if (groupId != null && !groupId.equals("")) {
			pro.put("group.id", groupId);
		}
		ConsumerConfig config = new ConsumerConfig(pro);
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);

	}

	public void consume(String topic, KafkaConsumerCallback kafkaConsumerCallback) {
		ConsumerConfig config = new ConsumerConfig(getPro());
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
		_topic = topic;
		_kafkaConsumerCallback = kafkaConsumerCallback;
	}

	public void run() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(_topic, new Integer(1));

		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,
				keyDecoder, valueDecoder);
		KafkaStream<String, String> stream = consumerMap.get(_topic).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext()) {
			_kafkaConsumerCallback.run(it.next().message());
		}
	}

	public static void main(String[] agr) {
		for (String temp : "192.168.1.42:6379;192.168.1.42:6389;192.168.1.42:6390;192.168.1.43:6379;192.168.1.43:6389;192.168.1.43:6390"
				.split(";")) {
			RedisUtils.addJedisShardInfo(temp.split(":")[0], Integer.valueOf(temp.split(":")[1]));
		}
		KafkaConsumer kafkaConsumer = new KafkaConsumer();
		kafkaConsumer.consume("test", new KafkaConsumerCallback() {

			public void run(String message) {
				System.out.println(message);
			}
		});
	}

}
