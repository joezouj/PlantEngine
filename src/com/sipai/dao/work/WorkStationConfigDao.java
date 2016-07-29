package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkStationConfig;
@Repository
public class WorkStationConfigDao extends CommDaoImpl<WorkStationConfig> {
	public WorkStationConfigDao() {
		super();
		this.setMappernamespace("work.WorkStationConfigMapper");
	}
	public List<WorkStationConfig> selectListByOrderId(String orderid){
		List<WorkStationConfig> list = this.getSqlSession().selectList("work.WorkStationConfigMapper.selectListByOrderId", orderid);
		return list;
	}
	
	public List<WorkStationConfig> selectListByWorkorderId(String workorderid){
		List<WorkStationConfig> list = this.getSqlSession().selectList("work.WorkStationConfigMapper.selectListByWorkorderId", workorderid);
		return list;
	}
}
