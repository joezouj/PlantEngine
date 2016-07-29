package com.sipai.dao.plan;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.DailyPlanSummary;

@Repository
public class DailyPlanSummaryDao extends CommDaoImpl<DailyPlanSummary>{
	public DailyPlanSummaryDao() {
		super();
		this.setMappernamespace("plan.DailyPlanSummaryMapper");
	}
	
}
	