package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.MaterialDeliver;

@Repository
public class MaterialDeliverDao extends CommDaoImpl<MaterialDeliver>{
	public MaterialDeliverDao() {
		super();
		this.setMappernamespace("plan.MaterialDeliverMapper");
	}
	public List<MaterialDeliver> getMaterialDeliver(MaterialDeliver materialDeliver) {
		List<MaterialDeliver> list = getSqlSession().selectList("plan.MaterialDeliverMapper.getMaterialDeliver", materialDeliver);
		return list;
	}
	public List<MaterialDeliver> getProcessorDeliver(MaterialDeliver materialDeliver) {
		List<MaterialDeliver> list = getSqlSession().selectList("plan.MaterialDeliverMapper.getProcessorDeliver", materialDeliver);
		return list;
	}
}
	