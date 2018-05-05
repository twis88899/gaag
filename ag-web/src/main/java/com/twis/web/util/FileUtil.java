/**

 * @Description:TODO

 * @author:ben

 * @time:2016年6月3日 上午10:17:25

 */


package com.twis.web.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	 public static void saveToFile(byte[] bfile, String filePath, String fileName) {  
	        BufferedOutputStream bos = null;  
	        FileOutputStream fos = null;  
	        File file = null;  
	        try {  
	            File dir = new File(filePath);  
	            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
	                dir.mkdirs();  
	            }  
	            file = new File(filePath+"\\"+fileName);  
	            fos = new FileOutputStream(file);  
	            bos = new BufferedOutputStream(fos);  
	            bos.write(bfile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (bos != null) {  
	                try {  
	                    bos.close();  
	                } catch (IOException e1) {  
	                    e1.printStackTrace();  
	                }  
	            }  
	            if (fos != null) {  
	                try {  
	                    fos.close();  
	                } catch (IOException e1) {  
	                    e1.printStackTrace();  
	                }  
	            }  
	        }  
		 } 
		 
		 public static ByteArrayOutputStream getByteArrayOutputStream(InputStream inStream) {
			 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			 try {
				 byte[] buffer = new byte[1024]; 
				 int len = 0; 
				 try {
					 if (inStream != null) {
						while( (len=inStream.read(buffer)) != -1 ){ 
							 outStream.write(buffer, 0, len); 
						 }
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			 } finally {	
			 }
			 return outStream;
		 }
		 
		 public static void saveToFile(ByteArrayOutputStream inStream, String filePath,String fileName) {
			 saveToFile(inStream.toByteArray(), filePath, fileName);
		 }
		 
		 public static void saveToFile(InputStream inStream, String filePath,String fileName) {
			 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			 try {
				 byte[] buffer = new byte[1024]; 
				 int len = 0; 
				 try {
					 if (inStream != null) {
						while( (len=inStream.read(buffer)) != -1 ){ 
							 outStream.write(buffer, 0, len); 
						 }
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				 
				saveToFile(outStream.toByteArray(), filePath, fileName);
				 
			 } finally {
				 try {
					 if (outStream != null)
						 outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			 }
		 }
}

