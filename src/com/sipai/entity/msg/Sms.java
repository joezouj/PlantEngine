package com.sipai.entity.msg;

public class Sms {
	private int state;
	private String content;
	private String date;
	private String number;
	private String flag;

	public Sms(int state, String content, String date, String flag,String number) {
		super();
		this.state = state;
		this.content = content;
		this.date = date;
		this.number = number;
		this.flag=flag;
	}

}
