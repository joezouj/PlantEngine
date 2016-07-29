package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.CutterPosition;
@Repository
public class CutterPositionDao extends CommDaoImpl<CutterPosition> {
	public CutterPositionDao() {
		super();
		this.setMappernamespace("material.CutterPositionMapper");
	}

	public List<CutterPosition> getListByName(String name) {
		List<CutterPosition> list = this.getSqlSession().selectList("material.CutterPositionMapper.getListByName", name);
		return list;
	}
}
