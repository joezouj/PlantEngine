package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.MaterialRecoverDao;
import com.sipai.entity.plan.MaterialRecover;
import com.sipai.tools.CommService;
@Service
public class MaterialRecoverService implements CommService<MaterialRecover>{
	@Resource
	private MaterialRecoverDao materialRecoverDao;

	public List<MaterialRecover> selectList() {
		MaterialRecover materialRecover = new MaterialRecover();
		return this.materialRecoverDao.selectList(materialRecover);
	}

	@Override
	public MaterialRecover selectById(String id) {
		return this.materialRecoverDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.materialRecoverDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialRecover entity) {
		return this.materialRecoverDao.insert(entity);
	}

	@Override
	public int update(MaterialRecover t) {
		return this.materialRecoverDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<MaterialRecover> selectListByWhere(String wherestr) {
		MaterialRecover materialRecover = new MaterialRecover();
		materialRecover.setWhere(wherestr);
		return this.materialRecoverDao.selectListByWhere(materialRecover);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialRecover materialRecover = new MaterialRecover();
		materialRecover.setWhere(wherestr);		
		return this.materialRecoverDao.deleteByWhere(materialRecover);
	}
	
	public List<MaterialRecover> getMaterialRecover(String wherestr) {
		MaterialRecover materialRecover=new MaterialRecover();
		materialRecover.setWhere(wherestr);		
		return this.materialRecoverDao.getMaterialRecover(materialRecover);
	}
}
