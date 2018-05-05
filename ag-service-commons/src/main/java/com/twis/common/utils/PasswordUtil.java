package com.twis.common.utils;

import java.util.ArrayList;
import java.util.List;

import scala.util.Random;

public class PasswordUtil {

	public synchronized static List getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz");
		StringBuffer buffer1 = new StringBuffer("0123456789");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		int range1 = buffer1.length();
		for (int i = 0; i < length; i++) {
			if(i%2==0){
				sb.append(buffer.charAt(r.nextInt(range)));
			}else{
				sb.append(buffer1.charAt(r.nextInt(range1)));
			}
			
		}
		List<String> list =  new ArrayList<String>();
		list.add(Md5Util.getMd5ByStr(sb.toString()).toUpperCase());
		list.add(sb.toString());
		
		return list;
	}
	
	public synchronized static List getRandomString() {
		return getRandomString(6);
	}
	
	public static void main(String ags[]){
		System.out.println(getRandomString(6));
	}

}
