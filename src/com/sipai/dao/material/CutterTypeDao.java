package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.CutterType;
@Repository
public class CutterTypeDao extends CommDaoImpl<CutterType> {
	public CutterTypeDao() {
		super();
		this.setMappernamespace("material.CutterTypeMapper");
	}

	public List<CutterType> getListByTypename(String typename) {
		List<CutterType> list = this.getSqlSession().selectList("material.CutterTypeMapper.getListByTypename", typename);
		return list;
	}
}
