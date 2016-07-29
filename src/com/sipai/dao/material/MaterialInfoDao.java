package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialInfo;
@Repository
public class MaterialInfoDao extends CommDaoImpl<MaterialInfo> {
	public MaterialInfoDao() {
		super();
		this.setMappernamespace("material.MaterialInfoMapper");
	}

	public List<MaterialInfo> getListByMaterialCode(String materialcode) {
		List<MaterialInfo> list = this.getSqlSession().selectList("material.MaterialInfoMapper.getListByMaterialCode", materialcode);
		return list;
	}
	public List<MaterialInfo> getMaterialInfo(MaterialInfo materialinfo) {
		List<MaterialInfo> list = getSqlSession().selectList("material.MaterialInfoMapper.getMaterialInfo", materialinfo);
		return list;
	}
}
