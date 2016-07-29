package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.ProcessDao;
import com.sipai.entity.process.Process;
import com.sipai.tools.CommService;

@Service("processesService")
public class ProcessService implements CommService<Process>{
	@Resource
	private Process process;
	
	@Resource
	private ProcessDao processDao;
	
	@Override
	public Process selectById(String id) {
		return processDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return processDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Process entity) {
		return processDao.insert(entity);
	}

	@Override
	public int update(Process t) {
		return processDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<Process> selectListByWhere(String wherestr) {
		process.setWhere(wherestr);
		return processDao.selectListByWhere(process);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		process.setWhere(wherestr);
		return processDao.deleteByWhere(process);
	}

}
