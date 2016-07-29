package com.sipai.entity.process;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.sipai.entity.base.SQLAdapter;
import com.sipai.entity.document.Data;

@Component
public class RealDetailsBook extends SQLAdapter{
	private String id;

	private String pid;

	private String bookid;

	private String insuser;

	private String insdt;

	private String upduser;

	private String upddt;

	private Data book;

	public Data getBook() {
		return book;
	}

	public void setBook(Data book) {
		this.book = book;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getBookid() {
		return bookid;
	}

	public void setBookid(String bookid) {
		this.bookid = bookid;
	}

	public String getInsuser() {
		return insuser;
	}

	public void setInsuser(String insuser) {
		this.insuser = insuser;
	}

	public String getInsdt() {
		return insdt;
	}

	public void setInsdt(String insdt) {
		this.insdt = insdt;
	}

	public String getUpduser() {
		return upduser;
	}

	public void setUpduser(String upduser) {
		this.upduser = upduser;
	}

	public String getUpddt() {
		return upddt;
	}

	public void setUpddt(String upddt) {
		this.upddt = upddt;
	}
}