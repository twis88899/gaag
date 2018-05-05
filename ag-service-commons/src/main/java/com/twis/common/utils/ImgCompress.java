package com.twis.common.utils;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
/**
 * 图片压缩处理
 * @author 崔素强
 */
public class ImgCompress {
	private Image img;
	private int width;
	private int height;
	private String _fileName;
	/**
	 * 构造函数
	 */
	public ImgCompress(InputStream inputStream) throws IOException {
		this(input2byte(inputStream),null);
	}
	
	public ImgCompress(byte[] buffer) throws IOException {
		this(new ByteArrayInputStream(buffer)  ,null);
	}
	
	public ImgCompress(InputStream inputStream,String fileName) throws IOException {
		this(input2byte(inputStream),fileName);
	}
	
	public ImgCompress(byte[] buffer,String fileName) throws IOException {
		_fileName=fileName;
		img = ImageIO.read(new ByteArrayInputStream(buffer));      // 构造Image对象
		width = img.getWidth(null);    // 得到源图宽
		height = img.getHeight(null);  // 得到源图长
	}

	private synchronized final static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}
	
	/**
	 * 按照宽度还是高度进行压缩
	 * @param w int 最大宽度
	 * @param h int 最大高度
	 */
	public byte[] resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			return resizeByWidth(w);
		} else {
			return resizeByHeight(h);
		}
	}
	/**
	 * 以宽度为基准，等比例放缩图片
	 * @param w int 新宽度
	 */
	public byte[] resizeByWidth(int w) throws IOException {
		int h = (int) (height * w / width);
		return resize(w, h);
	}
	/**
	 * 以高度为基准，等比例缩放图片
	 * @param h int 新高度
	 */
	public byte[] resizeByHeight(int h) throws IOException {
		int w = (int) (width * h / height);
		return resize(w, h);
	}
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 */
	public byte[] resize(int w, int h) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
//		BufferedImage image = new BufferedImage(w, h,BufferedImage.SCALE_SMOOTH ); 
//		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
//		image.getGraphics().drawImage( img.getScaledInstance(w, h,  java.awt.Image.SCALE_SMOOTH), 0, 0, null); 
		
		
		BufferedImage result = null;
		/* 新生成结果图片 */
		result = new BufferedImage(w, h,
				BufferedImage.SCALE_SMOOTH);
		result.getGraphics().drawImage(
				img.getScaledInstance(w, h,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);

		img.flush();
		img=null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		String str = this._fileName==null?"png":_fileName.substring(_fileName.lastIndexOf(".")+1);
		ImageIO.write(result,str, os);
		os.close();
		result.flush();
		
		return os.toByteArray();
	}
}
