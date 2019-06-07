package com.haizhiyun.util;

import java.util.Random;

public class UuidUtil {
	public static String getUuid() { 
		//定义字符
		StringBuffer buffer = new StringBuffer("abcdefghijklmnopqrstuvwxyz"); 
		StringBuffer sb = new StringBuffer(); 
		Random r = new Random(); 
		int range = buffer.length(); 
		//循环字符长度
		for (int i = 0; i < 8; i ++) { 
			//生成随机字符
			sb.append(buffer.charAt(r.nextInt(range))); 
		} 
		return sb.toString(); 
	}
	public static void main(String[] args) {
		
		System.err.println(UuidUtil.getUuid());
	}
}
