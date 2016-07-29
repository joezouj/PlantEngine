package com.sipai.dao.plan;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.DailyPlanSummaryMaterial;

@Repository
public class DailyPlanSummaryMaterialDao extends CommDaoImpl<DailyPlanSummaryMaterial>{
	public DailyPlanSummaryMaterialDao() {
		super();
		this.setMappernamespace("plan.DailyPlanSummaryMaterialMapper");
	}
	
}
	