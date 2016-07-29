package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.Line;

@Repository
public class LineDao extends CommDaoImpl<Line>{
	public LineDao() {
		super();
		this.setMappernamespace("work.LineMapper");
	}

	public List<Line> getListBySerial(String serial) {
		List<Line> list = this.getSqlSession().selectList("work.LineMapper.getListBySerial", serial);
		return list;
	}
}