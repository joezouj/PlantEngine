package com.sipai.dao.plan;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.plan.DeliverDetail;

@Repository
public class DeliverDetailDao extends CommDaoImpl<DeliverDetail>{
	public DeliverDetailDao() {
		super();
		this.setMappernamespace("plan.DeliverDetailMapper");
	}
	public List<DeliverDetail> getMaterialRecover(DeliverDetail deliverDetail) {
		List<DeliverDetail> list = getSqlSession().selectList("plan.DeliverDetailMapper.getDeliverDetail", deliverDetail);
		return list;
	}
	public DeliverDetail getPlanedAmount(DeliverDetail deliverDetail) {
		List<DeliverDetail> list = getSqlSession().selectList("plan.DeliverDetailMapper.getPlanedAmount", deliverDetail);
		DeliverDetail detail = new DeliverDetail();
		if(list.size()>0){
			detail = list.get(0);
		}
		return detail;
	}
	public String getMaterialDeliverStatus(DeliverDetail deliverDetail) {
		List<DeliverDetail> list = getSqlSession().selectList("plan.DeliverDetailMapper.getMaterialDeliverStatus", deliverDetail);
		DeliverDetail detail = new DeliverDetail();
		if(list.size()>0){
			detail = list.get(0);
			switch(detail.getDeliverst()){
			//未完成，对应任务中status 1 已下发
			case 0: return "1";
			//已完成，对应任务中status 3 配送完毕
			case 1: return "3";
			//部分完成，对应任务中status 2 配送中
			case 2: return "2";
			default: return "";
			}
		}
		return "";
	}
	
}
	