package com.twis.common.utils.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/*import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;*/
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.fs.FSDataInputStream;
/*import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;*/

import com.twis.common.utils.RedisUtils;

public class FileTransfer {
	private HDFSFileIO hdfsFileIO = null;
	private static Properties pro;
	private static int i = 0;

	private Properties getPro() {
		try {
			if (pro == null) {
				pro = new Properties();
				List<String> list = new RedisUtils().lrange("Hadoop_s");
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
	/*
	 * static { pro = new Properties(); try { pro.load(FileTransfer.class
	 * .getResourceAsStream("/com/Hadoop.properties")); } catch (Exception e) {
	 * e.getCause(); } }
	 */

	/**
	 * 
	 * @param hostPort
	 *            hdfs连接ip与port（192.168.1.X:9000）
	 * @param redisHost
	 *            redis服务ip
	 * @param redisPort
	 *            redis端口
	 * @param dataBackupPathStr
	 *            本地保存md5码与hdfs目录的存放目录
	 * @throws IOException
	 */
	private String getHDFDConnectStr() {
		return getPro().getProperty("host", "192.168.1.30") + ":" + getPro().getProperty("port", "9000");
	}
	public FileTransfer() {
		// RedisUtils redisUtils = new RedisUtils();
		hdfsFileIO = new HDFSFileIOImpl(getHDFDConnectStr());

		// hdfsFileIO = new HDFSFileIOImpl(pro);
	}
	
	
	public FileTransfer(String rootDir) {
		// RedisUtils redisUtils = new RedisUtils();
		hdfsFileIO = new HDFSFileIOImpl(getHDFDConnectStr(), rootDir);

		// hdfsFileIO = new HDFSFileIOImpl(pro);
	}

	/**
	 * 上传文件或目录
	 * 
	 * @param md5Str
	 *            文件的md5码
	 * @param srcPath
	 *            需要上传文件的路径
	 * @param destPath
	 *            上传文件的保存路径
	 * @return 在hdfs中的保存路径
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void uploadFile(InputStream in, String destPath) throws IllegalArgumentException, IOException {
		hdfsFileIO.upLoad(in, destPath);
	}
	
	public void deleteFile(String destPath) throws IllegalArgumentException, IOException {
		hdfsFileIO.deleteHdfsDirOrFile(destPath);
	}

	public void uploadFile(String srcPath, String destPath) throws IllegalArgumentException, IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(srcPath));
		uploadFile(in, destPath);
	}

	public byte[] downloadFile(String hdfsPath) throws IOException {
		FSDataInputStream fSDataInputStream = hdfsFileIO.downloadHdfsFile(hdfsPath);
		try {
			if (fSDataInputStream != null) {
				byte[] buffer = input2byte(fSDataInputStream);
				return buffer;
			} else {
				return null;
			}
		} finally {
			if(fSDataInputStream!=null) fSDataInputStream.close();
		}
	}

	private final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] in2b = null;
		try {
			byte[] buff = new byte[100];
			int rc = 0;
			if (inStream != null) {
				while ((rc = inStream.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
			}
			in2b = swapStream.toByteArray();
		} finally {
			swapStream.close();
		}
		return in2b;
	}

	public boolean writeHdfsFile(String hdfsPath, String content) {
		return hdfsFileIO.writeHdfsFile(hdfsPath, content);
	}

	/**
	 * 读hdfs文件
	 * 
	 * @param hdfsPath
	 *            文件路径
	 * @return
	 */
	public String readHdfsFile(String hdfsPath) {
		String str = hdfsFileIO.readHdfsFile(hdfsPath);
		return str;
	}

}
