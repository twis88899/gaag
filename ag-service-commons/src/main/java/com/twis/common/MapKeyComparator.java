package com.twis.common;

import java.util.Comparator;
import java.util.regex.Pattern;


public class MapKeyComparator implements Comparator<String>{
    private static Pattern pattern = null;
    static{
    	try{
        	pattern = Pattern.compile("[0-9]*"); 
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    
    }
	public int compare(String str1, String str2) { 
		if(pattern.matcher(str1).matches()&&pattern.matcher(str2).matches()){
			return Integer.parseInt(str1)- Integer.parseInt(str2);
		}else{
			return 0;
		}
	}
}
