package com.haizhiyun.exception;

public enum ErrorCode {
    UNKNOW(10001),//未知错误
    PARAMETER(10002),//参数错误
    USER_PASSWORD(10003),//用户名或密码错误
	USER_EXIST(10004),//用户令牌失效
	USER_INFO(10005);//用户信息
  
	//错误码
	private int errorCode;

	private ErrorCode() {
	}

	private ErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
    public static final ErrorCode  getObjectEnum(String key) {
    	
    	return  Enum.valueOf(ErrorCode.class, key);
    	
    }
	@Override
	public String toString() {
		return String.valueOf(this.errorCode);
	}

}
