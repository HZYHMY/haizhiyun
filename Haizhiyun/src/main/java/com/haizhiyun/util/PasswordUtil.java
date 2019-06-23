package com.haizhiyun.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 生成加密后密码 规则：SHA256+盐
 */
public class PasswordUtil {
	 /**
	  * 获取shiro使用MD5对密码进行，加盐加密后的密码值
	  * @param salt
	  * @param pwd
	  * @return
	  */
	 public static String getPasswordAfterSaltBySHA256(String salt , String pwd) {
			//加密方式
	     String hashAlgorithName = "SHA-256";
	     //加密次数
	     int hashIterations = 1;
	     ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
	     //Md5加盐后密码
	     String  password= new SimpleHash(hashAlgorithName, pwd, credentialsSalt, hashIterations).toString();
	     return password;
	 }
	 public static void main(String[] args) {
		 String passwordAfterSaltBySHA256 = PasswordUtil.getPasswordAfterSaltBySHA256("ekdeyciu", "test");
	     System.err.println(passwordAfterSaltBySHA256);
	 }
}

