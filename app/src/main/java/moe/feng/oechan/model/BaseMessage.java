package moe.feng.oechan.model;

public class BaseMessage<T> {

	/*
	* BaseMessage，自定义的类，基本数据，携带code和数据
	* 数据存储类型
	* 创建方式有code，object
	* 自定义常量OK值200， ERROR值-1
	* 使用get/set Code / Data 可以获取和设置本数据模型中的code和<T>object*/
	private int code;
	private T object;

	public final static int CODE_OKAY = 200, CODE_ERROR = -1;

	public BaseMessage(int code, T object) {
		this.code = code;
		this.object = object;
	}

	public BaseMessage(int code) {
		this.code = code;
	}

	public BaseMessage() {

	}

	public int getCode() {
		return this.code;
	}

	public T getData() {
		return this.object;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setData(T object) {
		this.object = object;
	}

}
