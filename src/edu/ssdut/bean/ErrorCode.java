package edu.ssdut.bean;

/**
 * 标准错误码表
 * @author lyu
 */
public enum ErrorCode {
	SUCCESS("0000","ok", "成功"),
	
	DEFAULT_ERROR("9000", "undefied", "服务器忙，请稍后再试"),
	
	REQUEST_EXPIRE("1002", "request expired", "请求已过期"),	
	REQUEST_DENY("1003", "request denied", "拒绝请求"),
	VERIFY_FAIL("1004", "verification failed", "验证失败"),
	PEMISSION_DENY("1005", "pemission denied", "无访问权限"),
	
	INVALID_USER_ID("2001", "invalid user id", "无效的用户ID"),
	INVALID_USER_TYPE("2002", "invalid user type", "无效的用户类型"),
	WRONG_PARAMS("2003", "wrong params", "参数错误"),
	INVALID_PROJECT_ID("2004", "invalid project id", "无效的项目ID"),
	
	
	USER_EXIST("3001", "user already exists", "该用户已存在"),
	PROJECT_EXIST("3002", "project already exists", "该项目已存在"),
	FILE_NOT_EXIST("3003", "file not exists", "文件不存在"),
	FIlE_ERROR("3004", "unknown file error", "未知文件错误");
	
	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	public String getZhMessage() {
		return zhMessage;
	}

	public static ErrorCode getErrorCode(String code) {
		for (ErrorCode errorCode : ErrorCode.values())
			if (errorCode.getCode().equals(code))
				return errorCode;
		return ErrorCode.DEFAULT_ERROR;
	}
	
	private final String code;
	private final String message;
	private final String zhMessage;
	
	private ErrorCode(String code, String message, String zhMessage) {
		this.code = code;
		this.message = message;
		this.zhMessage = zhMessage;
	}
}
