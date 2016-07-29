package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.plan.DailyPlan;

@Repository
public class DailyPlanDao extends CommDaoImpl<DailyPlan>{
	public DailyPlanDao() {
		super();
		this.setMappernamespace("plan.DailyPlanMapper");
	}
	public List<DailyPlan> getDailyPlanlist(DailyPlan dailyPlan) {
		List<DailyPlan> list = this.getSqlSession().selectList("plan.DailyPlanMapper.getDailyPlanlist", dailyPlan);
		return list;
	}
	public List<DailyPlan> getDailyPlan(DailyPlan dailyPlan) {
		List<DailyPlan> list = this.getSqlSession().selectList("plan.DailyPlanMapper.getDailyPlan", dailyPlan);
		return list;
	}
	public int updateBySetAndWhere(DailyPlan dailyPlan){
		return getSqlSession().update("plan.DailyPlanMapper.updateBySetAndWhere", dailyPlan);
	}
	public List<SalesOrderProduct> getSOPByPlan(DailyPlan dailyPlan){
		List<SalesOrderProduct> list = this.getSqlSession().selectList("plan.DailyPlanMapper.getSOPByPlan", dailyPlan);
		return list;
	}
}
	