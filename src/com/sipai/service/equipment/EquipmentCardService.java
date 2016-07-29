package com.sipai.service.equipment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.equipment.EquipmentCardDao;
import com.sipai.entity.equipment.EquipmentCard;
import com.sipai.entity.user.User;
import com.sipai.tools.CommService;


@Service
public class EquipmentCardService implements CommService<EquipmentCard>{
	@Resource
	private EquipmentCardDao equipmentcardDao;
	

	public List<EquipmentCard> selectList() {
		// TODO Auto-generated method stub
		EquipmentCard equipmentcard = new EquipmentCard();
		return this.equipmentcardDao.selectList(equipmentcard);
	}


	@Override
	public EquipmentCard selectById(String id) {
		return this.equipmentcardDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.equipmentcardDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(EquipmentCard entity) {
		return this.equipmentcardDao.insert(entity);
	}

	@Override
	public int update(EquipmentCard t) {
		return this.equipmentcardDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<EquipmentCard> selectListByWhere(String wherestr) {
		EquipmentCard equipmentcard = new EquipmentCard();
		equipmentcard.setWhere(wherestr);
		return this.equipmentcardDao.selectListByWhere(equipmentcard);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		EquipmentCard equipmentcard = new EquipmentCard();
		equipmentcard.setWhere(wherestr);		
		return this.equipmentcardDao.deleteByWhere(equipmentcard);
	}

	public List<EquipmentCard> getEquipmentCard(String wherestr){
		EquipmentCard equipmentcard=new EquipmentCard();
		equipmentcard.setWhere(wherestr);
		return this.equipmentcardDao.getEquipmentCard(equipmentcard);
	}
	public EquipmentCard getEquipmentCardById( String equipmentcardid){
		EquipmentCard equipmentcard=new EquipmentCard();
		equipmentcard.setWhere(" where E.id='"+equipmentcardid+"' order by E.equipmentcardid");
		return this.equipmentcardDao.getEquipmentCard(equipmentcard).get(0);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param equipmentcardid
	 * @return
	 */
	public boolean checkNotOccupied(String id, String equipmentcardid) {
		List<EquipmentCard> list = this.equipmentcardDao.getListByName(equipmentcardid);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(EquipmentCard equipmentcard :list){
					if(!id.equals(equipmentcard.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		
		return true;
	}
}