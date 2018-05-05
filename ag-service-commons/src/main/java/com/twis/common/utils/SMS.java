/**
 * Beijing ZhangMeng online technology co.,LTD,SMS platform JAVA SDK file
 * Copyright(R)：2012--2016 Beijing ZhangMeng online technology co.,LTD.All rights reserved. 
 * @author Tongwei wu
 * @version 1.0
 *
 */

package com.twis.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONObject;


/**
 * 向指定 URL 发送POST方法的请求类
 */

public class SMS {
  
  
  private static Properties getSendInfoFromConfig() {
    Properties pro = new Properties();
    List<String> list = new RedisUtils().lrange("sms");
    for(String str : list){
      pro.put(str.split("=")[0], str.split("=")[1]);
    }
    return pro;
  }
  
  private static String getSendUrl() {
    Properties pro = getSendInfoFromConfig();
    return pro.getProperty("scheme")+"://"+pro.getProperty("host")
    +":"+pro.getProperty("port")
    +pro.getProperty("path")
    +"/"+pro.getProperty("action");
  }
  
  private static String getSendUser() {
    Properties pro = getSendInfoFromConfig();
    return pro.getProperty("account");
  }
  
  private static String getSendPwd() {
    Properties pro = getSendInfoFromConfig();
    return pro.getProperty("password");
  }

  /**
   * 该方法接收客户传递的参数，重新整理后将数据传递给运营商，下发给短信接收用户。
   * 
   * @param account
   *            账户名
   * @param password
   *            密码
   * @param to
   *            目的号码
   * @param content
   *            短信内容
   * @param reference
   *            参考信息
   * @return 所代表远程资源的响应结果
   * @throws UnsupportedEncodingException
   */
  public static SendResult send(String to, String content, String reference, String ext)
    {
    PrintWriter out = null;
    BufferedReader in = null;
    String param = null;
    String result = null;
    SendResult sdres = new SendResult();
    String con = null;
    String contenttext = null;
    StringBuffer buf = new StringBuffer();
    StringBuffer sb = new StringBuffer();

    // 获取当前时间
    Date dt = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String seed = sdf.format(dt);
    String account = getSendUser();
    // 处理数据
    con = account + getSendPwd() + seed;
    String token = new SHA1().getDigestOfString(con.getBytes());

    String[] contentArray = convertStrToArray(content);

    // 发送接收数据
    try {
      if (contentArray.length == 1) {
        contenttext = contentArray[0];
      } else {
        for (int i = 0; i < contentArray.length; i++) {
          sb.append(URLDecoder.decode(contentArray[i], "utf-8"));
          if (i < contentArray.length - 1) {
            sb.append("|||");
          }
        }
        contenttext = sb.toString();
      }
      
      buf.append("account=" + URLEncoder.encode(account, "UTF-8") + "&ts=" + URLEncoder.encode(seed, "UTF-8")
      + "&token=" + URLEncoder.encode(token, "UTF-8") + "&dest=" + URLEncoder.encode(to, "UTF-8")
      + "&content=" + URLEncoder.encode(contenttext, "UTF-8"));
      if (!(reference == null || reference.isEmpty())) {
        buf.append("&ref=" + URLEncoder.encode(reference, "UTF-8"));
      }
      if (!(ext == null || ext.isEmpty())) {
        buf.append("&ext=" + URLEncoder.encode(ext, "UTF-8"));
      }
      param = buf.toString();
      // System.out.println(param);
      buf.setLength(0);
  
      URL realUrl = new URL(getSendUrl());
      // 打开和URL之间的连接
      URLConnection conn = realUrl.openConnection();
      //
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(10000);
      // 设置通用的请求属性
      conn.setRequestProperty("accept", "application/x-www-form-urlencoded");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());
      // 发送请求参数
      out.print(param);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      String line;
      while ((line = in.readLine()) != null) {
        buf.append(line);
      }
      result = buf.toString();
      JSONObject jo =(JSONObject)JSONObject.fromObject(result);
      int error_code = jo.getInt("status");
      sdres.setStatusCode(error_code);
      if (error_code == 0) {
        String error_msg = jo.getString("data");
        JSONObject jo1 =(JSONObject)JSONObject.fromObject(error_msg);
        String msgid = jo1.getString("ticket");
        sdres.setTicket(msgid);
      }
      // System.out.println(result);
    } catch (Exception e) {
      e.printStackTrace();
    }
    // 使用finally块来关闭输出流、输入流
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    
    return sdres;
  }

  private static String[] convertStrToArray(String str) {
    String[] strArray = null;
    strArray = str.split("\\|\\|\\|"); // 拆分字符为"|||" ,然后把结果交给数组strArray
    return strArray;
  }
  
  public static void main(String[] args) throws Exception {
    SMS sst=new SMS();
        try {
          SendResult result = sst.send("15876678922", "【冷链通知】hello2", "zhzt_sms", "ext");
          System.out.println(result);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
