package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.work.WorkOrder;
import com.sipai.entity.work.Workstation;

@Repository
public class WorkOrderDao extends CommDaoImpl<WorkOrder>{
	public WorkOrderDao() {
		super();
		this.setMappernamespace("work.WorkOrderMapper");
	}
	public List<WorkOrder> getNewTask(WorkOrder workOrder) {
		// TODO Auto-generated method stub
			List<WorkOrder> list = getSqlSession().selectList("work.WorkOrderMapper.getNewTask", workOrder);
			return list;
	}
	//获取grid一级列表，即任务列表
	public List<WorkOrder> selecttaskListByWhere(WorkOrder workOrder) {
		// TODO Auto-generated method stub
			List<WorkOrder> list = getSqlSession().selectList("work.WorkOrderMapper.selecttaskListByWhere", workOrder);
			return list;
	}
	
	//获取当前任务任务列表
	public List<WorkOrder> selectActiveTasks(WorkOrder workOrder) {
		List<WorkOrder> list = getSqlSession().selectList("work.WorkOrderMapper.selectActiveTasks", workOrder);
		return list;
	}
	
	//获取grid二级列表，即任务列表
	public List<WorkOrder> selectDetailListByWhere(WorkOrder workOrder) {
		// TODO Auto-generated method stub
		List<WorkOrder> list = getSqlSession().selectList("work.WorkOrderMapper.selectDetailListByWhere", workOrder);
		return list;
	}
	
}
