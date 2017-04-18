package edu.ssdut.bean;

public class ResultBean<T> {
	

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	public static <T> ResultBean<T> success(T data) {
		ResultBean<T> resultBean = new ResultBean<T>();
		resultBean.setCode(ErrorCode.SUCCESS.getCode());
		resultBean.setMsg(ErrorCode.SUCCESS.getZhMessage());
		resultBean.setData(data);
		return resultBean;
	}
	
	public static <T> ResultBean<T> success() {
		ResultBean<T> resultBean = new ResultBean<T>();
		resultBean.setCode(ErrorCode.SUCCESS.getCode());
		resultBean.setMsg(ErrorCode.SUCCESS.getZhMessage());
		return resultBean;
	}
	
	public static <T> ResultBean<T> failure(ErrorCode errorCode) {
		ResultBean<T> resultBean = new ResultBean<T>();
		resultBean.setCode(errorCode.getCode());
		resultBean.setMsg(errorCode.getZhMessage());
		return resultBean;
	}
	
	public static <T> ResultBean<T> failure(String code) {
		ErrorCode errorCode = ErrorCode.getErrorCode(code);
		return failure(errorCode);
	}
	
	private String code;
	private String msg;
	private T data;
}
