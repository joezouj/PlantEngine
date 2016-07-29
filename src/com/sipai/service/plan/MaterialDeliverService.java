package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.MaterialDeliverDao;
import com.sipai.entity.plan.MaterialDeliver;
import com.sipai.tools.CommService;
@Service
public class MaterialDeliverService implements CommService<MaterialDeliver>{
	@Resource
	private MaterialDeliverDao materialDeliverDao;

	public List<MaterialDeliver> selectList() {
		MaterialDeliver materialDeliver = new MaterialDeliver();
		return this.materialDeliverDao.selectList(materialDeliver);
	}

	@Override
	public MaterialDeliver selectById(String id) {
		return this.materialDeliverDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.materialDeliverDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialDeliver entity) {
		return this.materialDeliverDao.insert(entity);
	}

	@Override
	public int update(MaterialDeliver t) {
		return this.materialDeliverDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<MaterialDeliver> selectListByWhere(String wherestr) {
		MaterialDeliver materialDeliver = new MaterialDeliver();
		materialDeliver.setWhere(wherestr);
		return this.materialDeliverDao.selectListByWhere(materialDeliver);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialDeliver materialDeliver = new MaterialDeliver();
		materialDeliver.setWhere(wherestr);		
		return this.materialDeliverDao.deleteByWhere(materialDeliver);
	}
	
	public List<MaterialDeliver> getMaterialDeliver(String wherestr) {
		MaterialDeliver materialDeliver=new MaterialDeliver();
		materialDeliver.setWhere(wherestr);		
		return this.materialDeliverDao.getMaterialDeliver(materialDeliver);
	}
	
	public List<MaterialDeliver> getProcessorDeliver(String wherestr) {
		MaterialDeliver materialDeliver=new MaterialDeliver();
		materialDeliver.setWhere(wherestr);		
		return this.materialDeliverDao.getProcessorDeliver(materialDeliver);
	}
	
}
