package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkOrderExcuteDao;
import com.sipai.entity.work.WorkOrderExcute;
import com.sipai.tools.CommService;

@Service
public class WorkOrderExcuteService implements CommService<WorkOrderExcute>{

	@Resource
	private WorkOrderExcuteDao workOrderExcuteDao;
	
	@Override
	public WorkOrderExcute selectById(String id) {
		return workOrderExcuteDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workOrderExcuteDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkOrderExcute entity) {
		return workOrderExcuteDao.insert(entity);
	}

	@Override
	public int update(WorkOrderExcute t) {
		return workOrderExcuteDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkOrderExcute> selectListByWhere(String wherestr) {
		WorkOrderExcute workOrderExcute = new WorkOrderExcute();
		workOrderExcute.setWhere(wherestr);
		return workOrderExcuteDao.selectListByWhere(workOrderExcute);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkOrderExcute workOrderExcute = new WorkOrderExcute();
		workOrderExcute.setWhere(wherestr);
		return workOrderExcuteDao.deleteByWhere(workOrderExcute);
	}
	
}
