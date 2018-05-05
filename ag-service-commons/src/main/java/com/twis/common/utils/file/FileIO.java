package com.twis.common.utils.file;

import java.util.Map;

public interface FileIO {
	/**
	 * 以追加方式写文件
	 * @param filePath		文件路径
	 * @param md5String		
	 * @param hdfsPath		
	 * @return
	 */
	public boolean writeFileAppend(String filePath, String md5String, String hdfsPath);
	
	/**
	 * 读取文件
	 * @param filePath
	 * @return
	 */
	public Map<String, String> readFile(String filePath);
}
