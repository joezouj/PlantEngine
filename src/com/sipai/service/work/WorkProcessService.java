package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkProcessDao;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.work.WorkProcess;
import com.sipai.tools.CommService;

@Service
public class WorkProcessService implements CommService<WorkProcess>{

	@Resource
	private WorkProcessDao workProcessDao;
	
	@Override
	public WorkProcess selectById(String id) {
		return workProcessDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workProcessDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkProcess entity) {
		return workProcessDao.insert(entity);
	}

	@Override
	public int update(WorkProcess t) {
		return workProcessDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkProcess> selectListByWhere(String wherestr) {
		WorkProcess workProcess = new WorkProcess();
		workProcess.setWhere(wherestr);
		return workProcessDao.selectListByWhere(workProcess);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkProcess workProcess = new WorkProcess();
		workProcess.setWhere(wherestr);
		return workProcessDao.deleteByWhere(workProcess);
	}
	
	public List<RealDetails> getprocessdetails(String wherestr) {
		// TODO Auto-generated method stub
		RealDetails processdetails = new RealDetails();
		processdetails.setWhere(wherestr);
		return this.workProcessDao.getprocessdetails(processdetails);
	}
	
}
