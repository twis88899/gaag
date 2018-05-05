package com.twis.common.utils.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileIOImpl implements FileIO {

	public boolean writeFileAppend(String filePath, String md5String, String hdfsPath) {
		try {
			
			FileWriter writer = new FileWriter(filePath, true);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String datetime = format.format(new Date());
			writer.write(md5String + "\t" + hdfsPath + "\t" + datetime + "\r\n");
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public Map<String, String> readFile(String filePath) {
		String line = "";
		String[] keyValues = new String[1];
		Map<String, String> md5PathMap = new HashMap<String, String>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			line = reader.readLine();
			while (line != null) {
				keyValues = line.split("\t");
				line = reader.readLine();
				md5PathMap.put(keyValues[0], keyValues[1]);
				
			}; 
				
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return md5PathMap;
	}

}
