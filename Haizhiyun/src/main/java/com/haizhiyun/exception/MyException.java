package com.haizhiyun.exception;
/**
 *自定义异常
 */

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

public class MyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;

	private Response response;

	/*
	 * 储存异常数据
	 */
	private Map<String, Object> variables;

	public MyException(ErrorCode errorCode, Exception e) {
		super(e);
		this.errorCode = errorCode;
	}

	public MyException(ErrorCode errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	// 获取code码
	public int code() {
		return response == null ? -1 : response.code();

	}

	// 获取信息
	public String getmessage() {
		if (response == null) {
			return super.getMessage();
		} else {
			switch (code()) {
			case 500:
				return "HTTP-Internal Server Error";
			case 404:
				return "Server Error";
			default:
				return response.message();
			}
		}

	}

	// 储存
	public MyException put(String key, Object value) {
		if (variables == null) {
			variables = new HashMap<>();
		}
		variables.put(key, value);
		return this;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	public MyException(String msg) {
		super(msg);

	}

}
