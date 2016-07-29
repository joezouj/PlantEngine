package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.MaterialBoxDao;
import com.sipai.entity.material.MaterialBox;
import com.sipai.tools.CommService;
@Service
public class MaterialBoxService implements CommService<MaterialBox> {
	@Resource
	private MaterialBoxDao materialBoxDao;
	@Override
	public MaterialBox selectById(String id) {
		return materialBoxDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialBoxDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialBox materialBox) {
		return materialBoxDao.insert(materialBox);
	}

	@Override
	public int update(MaterialBox materialBox) {
		return materialBoxDao.updateByPrimaryKeySelective(materialBox);
	}

	@Override
	public List<MaterialBox> selectListByWhere(String wherestr) {
		MaterialBox materialBox = new MaterialBox();
		materialBox.setWhere(wherestr);
		return materialBoxDao.selectListByWhere(materialBox);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialBox materialBox = new MaterialBox();
		materialBox.setWhere(wherestr);
		return materialBoxDao.deleteByWhere(materialBox);
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @return
	 */
	public boolean checkNotOccupied(String id,String boxnumber) {
		List<MaterialBox> list = this.materialBoxDao.getListByBoxnumber(boxnumber);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialBox materialBox :list){
					if(!id.equals(materialBox.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
