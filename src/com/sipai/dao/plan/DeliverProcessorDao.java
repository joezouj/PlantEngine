package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.DeliverProcessor;

@Repository
public class DeliverProcessorDao extends CommDaoImpl<DeliverProcessor>{
	public DeliverProcessorDao() {
		super();
		this.setMappernamespace("plan.DeliverProcessorMapper");
	}
	public List<DeliverProcessor> getDeliverProcessor(DeliverProcessor deliverProcessor) {
		List<DeliverProcessor> list = getSqlSession().selectList("plan.DeliverProcessorMapper.getDeliverProcessor", deliverProcessor);
		return list;
	}
	
}
	