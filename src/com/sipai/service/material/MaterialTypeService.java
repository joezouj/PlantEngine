package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.MaterialTypeDao;
import com.sipai.entity.material.MaterialType;
import com.sipai.tools.CommService;
@Service
public class MaterialTypeService implements CommService<MaterialType>{
	@Resource
	private MaterialTypeDao materialTypeDao;
	@Override
	public MaterialType selectById(String id) {
		return materialTypeDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialTypeDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialType materialInfo) {
		return materialTypeDao.insert(materialInfo);
	}

	@Override
	public int update(MaterialType materialInfo) {
		return materialTypeDao.updateByPrimaryKeySelective(materialInfo);
	}

	@Override
	public List<MaterialType> selectListByWhere(String wherestr) {
		MaterialType materialInfo = new MaterialType();
		materialInfo.setWhere(wherestr);
		return materialTypeDao.selectListByWhere(materialInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialType materialType = new MaterialType();
		materialType.setWhere(wherestr);
		return materialTypeDao.deleteByWhere(materialType);
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @return
	 */
	public boolean checkNotOccupied(String id,String typename) {
		List<MaterialType> list = this.materialTypeDao.getListByTypename(typename);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialType materialType :list){
					if(!id.equals(materialType.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean checkCodeNotOccupied(String id, String typecode) {
		List<MaterialType> list = this.materialTypeDao.getListByTypecode(typecode);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialType materialType :list){
					if(!id.equals(materialType.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
