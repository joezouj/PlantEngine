package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialRequisition;
@Repository
public class MaterialRequisitionDao extends CommDaoImpl<MaterialRequisition>{
	public MaterialRequisitionDao() {
		super();
		this.setMappernamespace("material.MaterialRequisitionMapper");
	}
	public List<MaterialRequisition> getMaterialRequisitionList(MaterialRequisition materialRequisition){
		List<MaterialRequisition> list = this.getSqlSession().selectList("material.MaterialRequisitionMapper.getMaterialRequisitionList", materialRequisition);
		return list;
	}
	public List<MaterialRequisition> getListByOrderno(String orderno) {
		List<MaterialRequisition> list = this.getSqlSession().selectList("material.MaterialRequisitionMapper.getListByOrderno", orderno);
		return list;
	}
}
