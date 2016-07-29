package com.sipai.dao.equipment;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.equipment.EquipmentClass;
@Repository
public class EquipmentClassDao extends CommDaoImpl<EquipmentClass>{
	public EquipmentClassDao() {
		super();
		this.setMappernamespace("equipment.EquipmentClassMapper");
	}
}
