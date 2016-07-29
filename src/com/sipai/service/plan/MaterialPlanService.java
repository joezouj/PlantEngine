package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.MaterialPlanDao;
import com.sipai.entity.plan.MaterialPlan;

@Service("materialplanService")
public class MaterialPlanService{
	@Resource
	private MaterialPlanDao materialPlanDao;
	
	public MaterialPlan selectById(String id) {
		return materialPlanDao.selectByPrimaryKey(id);
	}
	
	public List<MaterialPlan> getStatisticListByWhere(String wherestr) {
		MaterialPlan materialPlan = new MaterialPlan();
		materialPlan.setWhere(wherestr);
		return materialPlanDao.getStatisticListByWhere(materialPlan);
	}
	
	public List<MaterialPlan> selectListByWhere(String wherestr) {
		MaterialPlan materialPlan = new MaterialPlan();
		materialPlan.setWhere(wherestr);
		return materialPlanDao.selectListByWhere(materialPlan);
	}
	
	public List<MaterialPlan> selectListBySql(String sql) {
		return materialPlanDao.getMaterialPlanListBySql(sql);
	}
}
