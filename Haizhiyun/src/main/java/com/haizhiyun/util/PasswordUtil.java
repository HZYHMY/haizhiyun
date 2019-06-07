package com.haizhiyun.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.util.ObjectUtils;

/**
 * 生成加密后密码 规则：SHA256+盐
 */
public class PasswordUtil {
	/**
	 * 利用java原生的类实现SHA256加密
	 * 
	 * @param str 加密后的报文
	 * @return
	 */
	public static String getPassword(String password, String salt) {
		MessageDigest messageDigest;
		String encodestr = "";
		if (!ObjectUtils.isEmpty(salt)) {
			try {
				messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update((password + salt).getBytes("UTF-8"));
				encodestr = byte2Hex(messageDigest.digest());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return encodestr;
	}

	/**
	 * 将byte转为16进制
	 * 
	 * @param bytes
	 * @return
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				// 1得到一位的进行补0操作
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	public static void main(String[] args) {
		String password = PasswordUtil.getPassword("test", "HlsFSmqroQ");
		System.err.println(password);
	}
}

