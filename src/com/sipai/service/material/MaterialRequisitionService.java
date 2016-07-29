package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.MaterialRequisitionDao;
import com.sipai.entity.material.MaterialRequisition;
import com.sipai.tools.CommService;
@Service
public class MaterialRequisitionService implements CommService<MaterialRequisition>{
	@Resource
	private MaterialRequisitionDao materialRequisitionDao;
	

	public List<MaterialRequisition> selectList() {
		// TODO Auto-generated method stub
		MaterialRequisition materialRequisition = new MaterialRequisition();
		return this.materialRequisitionDao.selectList(materialRequisition);
	}


	@Override
	public MaterialRequisition selectById(String id) {
		return this.materialRequisitionDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.materialRequisitionDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialRequisition entity) {
		return this.materialRequisitionDao.insert(entity);
	}

	@Override
	public int update(MaterialRequisition t) {
		return this.materialRequisitionDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<MaterialRequisition> selectListByWhere(String wherestr) {
		MaterialRequisition materialRequisition = new MaterialRequisition();
		materialRequisition.setWhere(wherestr);
		return this.materialRequisitionDao.selectListByWhere(materialRequisition);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialRequisition materialRequisition = new MaterialRequisition();
		materialRequisition.setWhere(wherestr);		
		return this.materialRequisitionDao.deleteByWhere(materialRequisition);
	}
	public List<MaterialRequisition> getMaterialRequisitionList(String wherestr) {
		// TODO Auto-generated method stub
		MaterialRequisition materialRequisition = new MaterialRequisition();
		materialRequisition.setWhere(wherestr);
		return this.materialRequisitionDao.getMaterialRequisitionList(materialRequisition);
	}


	public boolean checkNotOccupied(String id, String orderno) {
		List<MaterialRequisition> list = this.materialRequisitionDao.getListByOrderno(orderno);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialRequisition materialRequisition :list){
					if(!id.equals(materialRequisition.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
