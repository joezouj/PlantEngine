package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.MaterialPlan;

@Repository
public class MaterialPlanDao extends CommDaoImpl<MaterialPlan>{
	public MaterialPlanDao() {
		super();
		this.setMappernamespace("plan.MaterialPlanMapper");
	}
	
	public List<MaterialPlan> getStatisticListByWhere(MaterialPlan materialPlan) {
		List<MaterialPlan> list = this.getSqlSession().selectList("plan.MaterialPlanMapper.selectStatisticListByWhere", materialPlan);
		return list;
	}
	
	public List<MaterialPlan> getMaterialPlanListByWhere(MaterialPlan materialPlan) {
		List<MaterialPlan> list = this.getSqlSession().selectList("plan.MaterialPlanMapper.selectListByWhere", materialPlan);
		return list;
	}
	
	public List<MaterialPlan> getMaterialPlanListBySql(String sql) {
		List<MaterialPlan> list = this.getSqlSession().selectList("plan.MaterialPlanMapper.selectListByWhere", sql);
		return list;
	}
}
	