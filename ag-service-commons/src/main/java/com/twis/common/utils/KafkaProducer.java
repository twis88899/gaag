package com.twis.common.utils;

import java.util.Base64;
import java.util.List;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 
 * @author yxm
 *
 */

public class KafkaProducer {

	@SuppressWarnings("unused")
	private static Producer<String, String> producer;
	private static Properties pro;
	private static int i =0;
	public KafkaProducer() {
		super();

	}

	private static Producer<String, String> getProducer() {
		try{
			if (producer == null) {			
				pro = new Properties();
				List<String> list = new RedisUtils().lrange("KafkaProducer_s");
				for(String str : list){
					pro.put(str.split("=")[0], str.split("=")[1]);
				}
				producer = new Producer<String, String>(new ProducerConfig(pro));
				i=0;
			}
		}catch(Exception e){
			producer=null;
			++i;
			if(i<=100){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}  
				return getProducer();
			}else{
				e.printStackTrace();
			}
		}
		return producer;
	}

	public static synchronized void produce(KeyedMessage<String, String> message) {
		getProducer().send(message);
//		getProducer().close();
	}

	public static synchronized void produce(List<KeyedMessage<String, String>> message) {
		getProducer().send(message);
//		getProducer().close();
	}

	public static synchronized void produce(String topic, String key, String message) {
		getProducer().send(new KeyedMessage<String, String>(topic, key, message));
//		getProducer().close();
	}

	public static synchronized void produce(String topic, String message) {
		try {
			getProducer().send(new KeyedMessage<String, String>(topic, message));
//			getProducer().close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
//			getProducer().close();
		}

	}

	public static synchronized void produce(String topic, String key, String partKey, String message) {
		getProducer().send(new KeyedMessage<String, String>(topic, key, partKey, message));
//		getProducer().close();
	}
	

	public static void main(String[] agr) {
		for(String temp : "192.168.1.45:6379;192.168.1.45:6389;192.168.1.45:6390;192.168.1.46:6379;192.168.1.46:6389;192.168.1.46:6390;192.168.1.47:6379;192.168.1.47:6389;192.168.1.47:6390".split(";")){
			RedisUtils.addJedisShardInfo(temp.split(":")[0], Integer.valueOf(temp.split(":")[1]));
		}
////		for (int i = 0; i < 1; i++) {
////			KafkaProducer.produce("log", "{\"id\": \"\",\"type\": \"1\",\"context\": \"{\"title\":\"女大学生扶老人被讹 目击者公开现场照片\",\"content\":\"9月10日报道，8日上午，女大学生小袁有四节课要上，所以一早就从同学家赶回学校。“我当时骑着自行车，沿着学校北门对面的马路走的。”小袁回忆说，距离学校北门还有几百米时，她看到一位老人在她前面走，“我看她走路不稳，有点像S形，当时也没太在意，就从老人旁边超过去了。”没过一会儿，小袁听到身后传来“哎哟”一声，她扭头一看，那位老人摔倒在地。“当时7点多钟，旁边的商店都还没开门，路上也没有其他人。”小袁感觉老人伤得不轻，便将自行车停在路边，回头去看看那位老人。\"}\",\"objectType\": \"\",\"objectId\": \"\",\"systemType\": \"2\",\"userId\": \"\",\"createDate\": \"\",\"version\": \"\"}");
////
////		}
//		//      [{"title":"香洲区人民医院通知","imgurl":"1445089065908.jpg","name":"王永琪","days":"75","nums":"3"}]
//		//		[{"title":"广东省疾控疾控中心通知","imgurl":"1445089024812.jpg","name":"王永琪","days":"75","nums":"3"}]
//		//		[{"title":"珠海市疾控中心通知","imgurl":"1445089088261.jpg","name":"王永琪","days":"75","nums":"3"}]
//
//		//String message =getBase64("{\"title\":\"香洲区人民医院通知\",\"imgurl\":\"1445089065908.jpg\",\"name\":\"王永琪\",\"days\":\"75\",\"nums\":\"3\"}");
////		String message =getBase64("{\"title\":\"香洲区人民医院通知:预约提醒\",\"content\":\"尊敬的家长，您的宝宝xxx已预约2015-10-28 09:10～09:20到香洲区人民医院接种门诊接种疫苗，请您在预约接种日当天提前5分钟于 09:05前到达，否则过号预约将被取消。\n接种前注意事项：\n1、接种前保证宝宝有充足休息，并提前为宝宝洗澡或洗净手臂，换上柔软宽大的衣服。\n2、接种后请留观30分钟。\n3、接种时携带《预防接种证》。\"}");
//		String message =getBase64("{\"title\":\"珠海市疾控中心通知\",\"imgurl\":\"1445089088261.jpg\",\"name\":\"王永琪\",\"days\":\"75\",\"nums\":\"3\"}");
//		Date nowTime=new Date();
//		
//		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
//		
//		String content="尊敬的家长，您的宝宝xxx已预约香洲区人民医院接种门诊接种疫苗，请您在预约接种日当天提前5分钟到达，否则过号预约将被取消。\n接种前注意事项：\n1、接种前保证宝宝有充足休息，并提前为宝宝洗澡或洗净手臂，换上柔软宽大的衣服。\n2、接种后请留观30分钟。\n3、接种时携带《预防接种证》。";
//		MessageToJson messageToJson = new MessageToJson();
//		messageToJson.setContent(content);
//
//		messageToJson.setTitle("香洲区人民医院通知");
//		messageToJson.setSubTitle("迁入迁出确认通知");
//		messageToJson.setName("王永琪");
//		messageToJson.setDays("3");
//		messageToJson.setNums("75");
//		messageToJson.setObjectId(4714325l);
//		messageToJson.setType(6);
//		
//		messageToJson.setSystemType(2);
////		for (int i = 0; i < 5; i++) {
//			messageToJson.setLoginName("18125074890");
//			KafkaProducer.produce("test",messageToJson.toString());
//			saveToMongo("18125074890",messageToJson.toString());
////		}
		KafkaProducer.produce("uploaduser", "45b1eee6-2daf-458b-b3ea-a7680a10d6bc");
		
	}
	
	private static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		b = str.getBytes();
		if (b != null) {
			s = Base64.getEncoder().encodeToString(b);
		}
		return s;
	}

}
