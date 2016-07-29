package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkProcessOrderDao;
import com.sipai.dao.work.WorkSchedulingDao;
import com.sipai.entity.work.WorkProcessOrder;
import com.sipai.entity.work.WorkProcessOrder;
import com.sipai.tools.CommService;

@Service
public class WorkProcessOrderService implements CommService<WorkProcessOrder>{

	@Resource
	private WorkProcessOrderDao workProcessOrderDao;
	
	@Override
	public WorkProcessOrder selectById(String id) {
		return workProcessOrderDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workProcessOrderDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkProcessOrder entity) {
		return workProcessOrderDao.insert(entity);
	}

	@Override
	public int update(WorkProcessOrder t) {
		return workProcessOrderDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkProcessOrder> selectListByWhere(String wherestr) {
		WorkProcessOrder workScheduling = new WorkProcessOrder();
		workScheduling.setWhere(wherestr);
		return workProcessOrderDao.selectListByWhere(workScheduling);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkProcessOrder workScheduling = new WorkProcessOrder();
		workScheduling.setWhere(wherestr);
		return workProcessOrderDao.deleteByWhere(workScheduling);
	}
	
}
