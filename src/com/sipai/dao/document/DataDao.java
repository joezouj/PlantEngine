package com.sipai.dao.document;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.document.Data;

@Repository
public class DataDao extends CommDaoImpl<Data>{
	public DataDao() {
		super();
		this.setMappernamespace("document.DataMapper");
	}

	public List<Data> getListByNumberAndType(String number, String doctype) {
		Data data = new Data();
		data.setNumber(number);
		data.setDoctype(doctype);
		List<Data> list = this.getSqlSession().selectList("document.DataMapper.getListByNumberAndType", data);
		return list;
	}
}