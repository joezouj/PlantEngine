package com.sipai.dao.equipment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.equipment.EquipmentCard;
import com.sipai.entity.user.User;
@Repository
public class EquipmentCardDao extends CommDaoImpl<EquipmentCard>{
	public EquipmentCardDao() {
		super();
		this.setMappernamespace("equipment.EquipmentCardMapper");
	}
	public List<EquipmentCard> getEquipmentCard(EquipmentCard equipmentcard){
		List<EquipmentCard> list= getSqlSession().selectList("equipment.EquipmentCardMapper.getEquipmentCard", equipmentcard);
		return list;
	}
	public List<EquipmentCard> getListByName(String equipmentcardid){
		List<EquipmentCard> list = this.getSqlSession().selectList("equipment.EquipmentCardMapper.getEquipmentCardByCardId", equipmentcardid);
		return list;
	}
}
