package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialShort;
import com.sipai.entity.material.MaterialType;
@Repository
public class MaterialShortDao extends CommDaoImpl<MaterialShort> {
	public MaterialShortDao() {
		super();
		this.setMappernamespace("material.MaterialShortMapper");
	}

}
