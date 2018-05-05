package com.twis.common.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;


public class RC4DECODE
{
  
   public static final String RC4_KEY = "zhitehxb";
    private static final String charSet = "gb2312";
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    public static Number format(Number number, String aMask) {
        DecimalFormat myformat1;
        String str = "0";
        if (aMask == null) {
            myformat1 = new DecimalFormat(".0");//
        } else {
            myformat1 = new DecimalFormat(aMask);
        }
        try {
            str = myformat1.format(number.doubleValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Number) (new Double(str));
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }


    public static byte[] RC4_Decrypt(String key, String data) {
        if (data == null || key == null) {
            return null;
        }

        byte[] b_key = key.getBytes();
        byte[] b_data = HexString2Bytes(data);
        return RC4(b_key, b_data);
    }

    public static byte[] RC4_Encrypt(String key, String data) {
        if (data == null || key == null) {
            return null;
        }
        try {
            byte[] b_key = key.getBytes();
            byte[] b_data = data.getBytes(charSet);
            return RC4(b_key, b_data);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static byte[] RC4(byte[] b_key, byte[] buf) {
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        byte tmp;
        int x = 0;
        int y = 0;

        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }

        int xorIndex;

        if (buf == null) {
            return null;
        }

        byte[] result = new byte[buf.length];

        for (int i = 0; i < buf.length; i++) {
            x = (x + 1) & 0xff;
            y = ((state[x] & 0xff) + y) & 0xff;
            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;
            xorIndex = ((state[x] & 0xff) + (state[y] & 0xff)) & 0xff;
            result[i] = (byte) (buf[i] ^ state[xorIndex]);
        }
        return result;
    }

    public static String asString(byte[] buf) {
        try {
            if (buf == null)
                return null;
            return new String(buf, 0, buf.length / 2, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{
                src0
        })).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{
                src1
        })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static String asHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    public static String pisRC4Decrypt(String encryptString) {
        try {


            if (null == encryptString) {
                return "";
            } else if (encryptString.trim().length() == 0) {
                return "";
            } else {
                String str = asString(RC4_Decrypt(RC4_KEY, encryptString));
                String newStr = str;
                //数字型的位置做转换    解时4567 --> 5764 1237465->1234567  

                if (isNumeric(newStr) && (newStr.length() >= 7)) {
                    newStr = str.substring(0, 3) + str.substring(4, 5) + str.substring(6, 7) + str.substring(5, 6) + str.substring(3, 4) + str.substring(7);
                }
                return newStr;
            }
        } catch (Exception e) {
            System.out.println(encryptString);
            e.printStackTrace();
            return "";
        }
    }


    //test
    public static void main(String[] args) throws UnsupportedEncodingException {
        String data = "抓获时2013啦";

        //System.setOut(new java.io.PrintStream(System.out, true, "gb2312"));
        byte[] b = RC4_Encrypt(RC4_KEY, data);
        String dataInHex = asHex(b);
        System.out.println(dataInHex);

        System.out.println(asString(RC4_Decrypt(RC4_KEY, dataInHex)));

        dataInHex = "B8D7B8C2";  //1237465
        System.out.println(pisRC4Decrypt("5F0D3A58A71586CFA45E3F"));  //1234567

        System.out.println("============================================================");

    }
}