package bluetooth;

import android.annotation.SuppressLint;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 * 16进制值与String/Byte之间的转换
 * 
 * @author JerryLi
 * @email lijian@dzs.mobi
 * @data 2011-10-16
 * */

public class CHexConver {
    private final static char[] mChars = "0123456789ABCDEF".toCharArray();
    private final static String mHexStr = "0123456789ABCDEF";
    /**
     * 检查16进制字符串是否有效
     * 
     * @param String
     *            sHex 16进制字符串
     * @return boolean
     */
    @SuppressLint("DefaultLocale")
    public static boolean checkHexStr(String sHex) {
	String sTmp = sHex.toString().trim().replace(" ", "")
		.toUpperCase(Locale.US);
	int sLen = sTmp.length();

	if (sLen > 1 && sLen % 2 == 0) {
	    for (int i = 0; i < sLen; i++)
		if (!mHexStr.contains(sTmp.substring(i, i + 1)))
		    return false;
	    return true;
	} else
	    return false;
    }

    private static String toHexUtil(int n) {
	String rt = "";
	switch (n) {
	case 10:
	    rt += "A";
	    break;
	case 11:
	    rt += "B";
	    break;
	case 12:
	    rt += "C";
	    break;
	case 13:
	    rt += "D";
	    break;
	case 14:
	    rt += "E";
	    break;
	case 15:
	    rt += "F";
	    break;
	default:
	    rt += n;
	}
	return rt;
    }

    public static String toHex(int n) {
	StringBuilder sb = new StringBuilder();
	if (n / 16 == 0) {
	    return toHexUtil(n);
	} else {
	    String t = toHex(n / 16);
	    int nn = n % 16;
	    sb.append(t).append(toHexUtil(nn));
	}
	return sb.toString();
    }

    public static String printHexString(String hint, byte[] b) {
	System.out.print(hint);
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < b.length; i++) {
	    String hex = Integer.toHexString(b[i] & 0xFF);
	    if (hex.length() == 1) {
		hex = '0' + hex;
	    }
	    sb.append(hex.toUpperCase());
	    System.out.print(hex.toUpperCase() + " ");
	}
	System.out.println("");
	return sb.toString();
    }

    /**
     * bytes字符串转换为Byte值
     * 
     * @param String
     *            src Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F)
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
	/* 对输入值进行规范化整理 */
	src = src.trim().replace(" ", "").toUpperCase(Locale.US);
	// 处理值初始化
	int m = 0, n = 0;
	int l = src.length() / 2; // 计算长度
	byte[] ret = new byte[l]; // 分配存储空间

	for (int i = 0; i < l; i++) {
	    m = i * 2 + 1;
	    n = m + 1;
	    ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m)
		    + src.substring(m, n)) & 0xFF);
	}
	return ret;
    }

    public static String parseAscii(String str) {
	StringBuilder sb = new StringBuilder();
	byte[] bs = str.getBytes();
	for (int i = 0; i < bs.length; i++)
	    sb.append(toHex(bs[i]));
	return sb.toString();
    }
    /*
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    public static String decode(String bytes)
    {
    ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);
    //将每2位16进制整数组装成一个字节
    for(int i=0;i<bytes.length();i+=2)
    baos.write((mHexStr.indexOf(bytes.charAt(i))<<4 |mHexStr.indexOf(bytes.charAt(i+1))));
    return new String(baos.toByteArray());
    }
}
