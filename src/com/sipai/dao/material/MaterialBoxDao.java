package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialBox;
@Repository
public class MaterialBoxDao extends CommDaoImpl<MaterialBox> {
	public MaterialBoxDao() {
		super();
		this.setMappernamespace("material.MaterialBoxMapper");
	}
	public List<MaterialBox> getListByBoxnumber(String boxnumber) {
		List<MaterialBox> list = getSqlSession().selectList("material.MaterialBoxMapper.getMaterialBoxByNumber", boxnumber);
		return list;
	}
	
}
