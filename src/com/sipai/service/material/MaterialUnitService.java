package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.MaterialUnitDao;
import com.sipai.entity.material.MaterialUnit;
import com.sipai.tools.CommService;
@Service
public class MaterialUnitService implements CommService<MaterialUnit>{
	@Resource
	private MaterialUnitDao materialUnitDao;
	@Override
	public MaterialUnit selectById(String id) {
		return materialUnitDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialUnitDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialUnit materialInfo) {
		return materialUnitDao.insert(materialInfo);
	}

	@Override
	public int update(MaterialUnit materialInfo) {
		return materialUnitDao.updateByPrimaryKeySelective(materialInfo);
	}

	@Override
	public List<MaterialUnit> selectListByWhere(String wherestr) {
		MaterialUnit materialInfo = new MaterialUnit();
		materialInfo.setWhere(wherestr);
		return materialUnitDao.selectListByWhere(materialInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialUnit materialType = new MaterialUnit();
		materialType.setWhere(wherestr);
		return materialUnitDao.deleteByWhere(materialType);
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @return
	 */
	public boolean checkNotOccupied(String id,String typename) {
		List<MaterialUnit> list = this.materialUnitDao.getListByUnit(typename);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialUnit materialUnit :list){
					if(!id.equals(materialUnit.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
