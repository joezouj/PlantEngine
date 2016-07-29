package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.LineWorkstation;
import com.sipai.entity.work.Workstation;

@Repository
public class LineWorkstationDao extends CommDaoImpl<LineWorkstation>{
	public LineWorkstationDao() {
		super();
		this.setMappernamespace("work.LineWorkstationMapper");
	}
	
	public List<Workstation> selectWorkstation(Workstation workstation) {
		List<Workstation> list = this.getSqlSession().selectList("work.LineWorkstationMapper.selectWorkstation", workstation);
		return list;
	}
}