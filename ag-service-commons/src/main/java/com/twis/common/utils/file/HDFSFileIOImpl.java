package com.twis.common.utils.file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/*import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;*/
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class HDFSFileIOImpl implements HDFSFileIO {

	Configuration conf = new Configuration();
	static List<String> fileList = new ArrayList<String>();
	static String WORKDIR = "UPLOAD";
	String PREFIX_HDFS_PATH = null;

	/**
	 * 
	 * @param hostPort
	 *            hdfs的连接ip与端口
	 */
	public HDFSFileIOImpl(String hostPort) {
		PREFIX_HDFS_PATH = "hdfs://" + hostPort + "/" + WORKDIR;
	}
	
	public HDFSFileIOImpl(String hostPort, String rootdir) {
		if (rootdir == null) {
			rootdir = "/" + WORKDIR;
		}
		PREFIX_HDFS_PATH = "hdfs://" + hostPort + rootdir;
	}

	public String upLoad(InputStream in, String destPath)
			throws IllegalArgumentException, IOException {
		// 得到配置对象
		String hdfsPath = PREFIX_HDFS_PATH + destPath;
		OutputStream out = null;
		FileSystem fs = null;
		try {
			// 文件系统
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			// 输出流
			out = fs.create(new Path(hdfsPath), new Progressable() {
				public void progress() {

				}
			});
			// 连接两个流，形成通道，使输入流向输出流传输数据
			IOUtils.copyBytes(in, out, 4096,true);
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
			IOUtils.closeStream(out);
			fs.close();
		}
		
		return destPath;
	}

	public String upLoad(String localFilePath, String saveHDFSPath)
			throws IllegalArgumentException, IOException {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(
				localFilePath));
		return upLoad(inputStream, saveHDFSPath);
	}

	public FSDataInputStream downloadHdfsFile(String hdfsFilePath) throws IOException {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsFilePath;
		// 获取conf配置
		Configuration conf = new Configuration();
		// 实例化一个文件系统
		FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
		if (fs.exists(new Path(hdfsPath))) {
			// 读出流
			FSDataInputStream HDFS_IN = fs.open(new Path(hdfsPath));
			

//			byte[] ioBuffer = new byte[1024];
//			int readLen = HDFS_IN.read(ioBuffer);
//			while (readLen != -1) {
//				readLen = HDFS_IN.read(ioBuffer);
//			}
//			HDFS_IN.close();
//			fs.close();
			return HDFS_IN;
		}
		return null;

	}

	public boolean makeHdfsDir(String hdfsDir) {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsDir;
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			Path path = new Path(hdfsPath);
			fs.create(path);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean deleteHdfsDirOrFile(String hdfsDir) {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsDir;
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			Path path = new Path(hdfsPath);
			fs.delete(path, true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean writeHdfsFile(String hdfsFilePath, String content) {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsFilePath;
		FileSystem fs = null;
		FSDataOutputStream outputStream = null;
		try {
	        conf.setBoolean("dfs.support.append",true);
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			Path path =new Path(hdfsPath);
			if(fs.exists(path)){
				outputStream = fs.append(path);
				outputStream.write(content.getBytes("UTF-8"));
				outputStream.write("\r\n".getBytes());
			}else{
				outputStream = fs.create(path);
			}
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(outputStream!=null){
					outputStream.close();
				}
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public String readHdfsFile(String hdfsFilePath) {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsFilePath;
		String content = null;
		FileSystem fs = null;
		FSDataInputStream inputStream = null;
		try {
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			Path path = new Path(hdfsPath);
			if (fs.exists(path)) {
				inputStream = fs.open(path);
				FileStatus status = fs.getFileStatus(path);
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(status
						.getLen()))];
				inputStream.readFully(0, buffer);
				content = new String(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				inputStream.close();
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		return content;
	}

	public List<String> listHdfsChildFileOrDir(String hdfsDir) {
		String hdfsPath = PREFIX_HDFS_PATH + hdfsDir;
		List<String> fileList = new ArrayList<String>();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(hdfsPath), conf);
			Path path = new Path(hdfsPath);
			fileList.addAll(getFile(path, fs));
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileList;
	}

	private static List<String> getFile(Path path, FileSystem fs)
			throws IOException {
		FileStatus[] fileStatus = fs.listStatus(path);
		for (FileStatus f : fileStatus) {
			if (f.isDirectory()) {
				Path p = new Path(f.getPath().toString());
				getFile(p, fs);
			} else {
				fileList.add(f.getPath().toString());
			}
		}
		return fileList;
	}

}
