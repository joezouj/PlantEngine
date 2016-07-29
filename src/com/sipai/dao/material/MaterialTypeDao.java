package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialType;
@Repository
public class MaterialTypeDao extends CommDaoImpl<MaterialType> {
	public MaterialTypeDao() {
		super();
		this.setMappernamespace("material.MaterialTypeMapper");
	}

	public List<MaterialType> getListByTypename(String typename) {
		List<MaterialType> list = this.getSqlSession().selectList("material.MaterialTypeMapper.getListByTypename", typename);
		return list;
	}

	public List<MaterialType> getListByTypecode(String typecode) {
		List<MaterialType> list = this.getSqlSession().selectList("material.MaterialTypeMapper.getListByTypecode", typecode);
		return list;
	}
}
