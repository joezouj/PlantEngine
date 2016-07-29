package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialUnit;
@Repository
public class MaterialUnitDao extends CommDaoImpl<MaterialUnit> {
	public MaterialUnitDao() {
		super();
		this.setMappernamespace("material.MaterialUnitMapper");
	}

	public List<MaterialUnit> getListByUnit(String unit) {
		List<MaterialUnit> list = this.getSqlSession().selectList("material.MaterialUnitMapper.getListByUnit", unit);
		return list;
	}
}
