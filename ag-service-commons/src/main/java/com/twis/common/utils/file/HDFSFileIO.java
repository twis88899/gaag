package com.twis.common.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.hadoop.fs.FSDataInputStream;


/**
 * @author root
 *
 */
public interface HDFSFileIO
{

	String upLoad(InputStream in, String destPath) throws IllegalArgumentException, IOException;
	
	String upLoad(String localFilePath, String saveHDFSPath) throws IllegalArgumentException, IOException;
	
	/**
	 * 从hdfs下载文件到本地文件系统
	 * @param hdfsFilePath
	 * @param saveLocalPath
	 * @return	
	 * @throws IOException 
	 */
	public FSDataInputStream downloadHdfsFile(String hdfsFilePath) throws IOException;
	
	/**
	 * 创建hdfs目录
	 * @param hdfsDir	创建目录的路径
	 * @return
	 */
	public boolean makeHdfsDir(String hdfsDir);
	
	/**
	 * 删除hdfs目录
	 * @param hdfsDir	删除目录的路径
	 * @return
	 */
	public boolean deleteHdfsDirOrFile(String hdfsDir);
	
	/**
	 * 写hdfs文件
	 * @param hdfsPath	文件路径
	 * @param content	写入内容
	 * @return
	 */
	public boolean writeHdfsFile(String hdfsPath, String content);
	
	/**
	 * 读hdfs文件
	 * @param hdfsPath	文件路径
	 * @return
	 */
	public String readHdfsFile(String hdfsPath);
	
	/**
	 * 列出给定目录下的所有子目录及子文件
	 * @param hdfsDir	给定目录路径
	 * @return
	 */
	public List<String> listHdfsChildFileOrDir(String hdfsDir);
	
	
	
}
