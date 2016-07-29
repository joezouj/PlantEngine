package com.sipai.service.equipment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.equipment.EquipmentClassDao;
import com.sipai.entity.equipment.EquipmentCard;
import com.sipai.entity.equipment.EquipmentClass;
import com.sipai.tools.CommService;
@Service
public class EquipmentClassService implements CommService<EquipmentClass>{
	@Resource
	private EquipmentClassDao equipmentclassDao;
	

	public List<EquipmentClass> selectList() {
		// TODO Auto-generated method stub
		EquipmentClass equipmentclass = new EquipmentClass();
		return this.equipmentclassDao.selectList(equipmentclass);
	}


	@Override
	public EquipmentClass selectById(String id) {
		return this.equipmentclassDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.equipmentclassDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(EquipmentClass entity) {
		return this.equipmentclassDao.insert(entity);
	}

	@Override
	public int update(EquipmentClass t) {
		return this.equipmentclassDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<EquipmentClass> selectListByWhere(String wherestr) {
		EquipmentClass equipmentclass = new EquipmentClass();
		equipmentclass.setWhere(wherestr);
		return this.equipmentclassDao.selectListByWhere(equipmentclass);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		EquipmentClass equipmentclass = new EquipmentClass();
		equipmentclass.setWhere(wherestr);		
		return this.equipmentclassDao.deleteByWhere(equipmentclass);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<EquipmentClass> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(EquipmentClass equipmentclass :list){
					if(!id.equals(equipmentclass.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
