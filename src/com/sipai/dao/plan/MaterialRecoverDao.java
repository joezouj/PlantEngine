package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.MaterialRecover;

@Repository
public class MaterialRecoverDao extends CommDaoImpl<MaterialRecover>{
	public MaterialRecoverDao() {
		super();
		this.setMappernamespace("plan.MaterialRecoverMapper");
	}
	public List<MaterialRecover> getMaterialRecover(MaterialRecover materialRecover) {
		List<MaterialRecover> list = getSqlSession().selectList("plan.MaterialRecoverMapper.getMaterialRecover", materialRecover);
		return list;
	}
	
}
	