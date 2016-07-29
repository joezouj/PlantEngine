package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkInterruptDao;
import com.sipai.dao.work.WorkOrderDao;
import com.sipai.entity.work.WorkInterrupt;
import com.sipai.entity.work.WorkInterrupt;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommService;

@Service
public class WorkInterruptService implements CommService<WorkInterrupt>{

	@Resource
	private WorkInterruptDao workInterruptDao;
	
	@Override
	public WorkInterrupt selectById(String id) {
		return workInterruptDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workInterruptDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkInterrupt entity) {
		return workInterruptDao.insert(entity);
	}

	@Override
	public int update(WorkInterrupt t) {
		return workInterruptDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkInterrupt> selectListByWhere(String wherestr) {
		WorkInterrupt workOrder = new WorkInterrupt();
		workOrder.setWhere(wherestr);
		return workInterruptDao.selectListByWhere(workOrder);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkInterrupt workOrder = new WorkInterrupt();
		workOrder.setWhere(wherestr);
		return workInterruptDao.deleteByWhere(workOrder);
	}
	
	
}
