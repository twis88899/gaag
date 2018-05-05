package com.twis.common.utils;
public class RC4Util {
	
	public final static String key = "open20160501";

    //加密方法
    public static String encryRC4String(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encryRC4Byte(data, key)));
    }

    //解密方法
    public static String decryRC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static String decryRC4(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    public static byte[] encryRC4Byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte bData[] = data.getBytes();
        return RC4Base(bData, key);
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] bkey = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (bkey == null || bkey.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((bkey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % bkey.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
    
    public static void main(String[] arg) throws Exception {
    	String data = "{\"extraInfo\":{\"parameter1\":\"业务参数1\",\"parameter2\":\"业务参数2\"},\"policyHolderCertiNo\":\"230902198007185484\",\"policyHolderCertiType\":\"I\",\"policyHolderPhone\":\"18812312312\",\"policyHolderUserName\":\"投保人姓名\"}";

    	System.out.println("bizContent=" + data);
    	String key = "open20160501"; //key一定要用这个，因为两边的key需要一直才能正常的加解密
    	String s = RC4Util.encryRC4String(data, key);
    	System.out.println("加密后的字串="+s);
    	System.out.println("解密后的字串="+RC4Util.decryRC4(s, key));

    }
}