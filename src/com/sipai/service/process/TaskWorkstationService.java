package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.TaskWorkstationDao;
import com.sipai.entity.process.TaskWorkstation;
import com.sipai.tools.CommService;
@Service
public class TaskWorkstationService implements CommService<TaskWorkstation> {
	@Resource
	private TaskWorkstation taskWorkstation;

	@Resource
	private TaskWorkstationDao taskWorkstationDao;
	
	@Override
	public TaskWorkstation selectById(String id) {
		return taskWorkstationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return taskWorkstationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(TaskWorkstation entity) {
		return taskWorkstationDao.insert(entity);
	}

	@Override
	public int update(TaskWorkstation t) {
		return taskWorkstationDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<TaskWorkstation> selectListByWhere(String wherestr) {
		taskWorkstation.setWhere(wherestr);
		return taskWorkstationDao.selectListByWhere(taskWorkstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		taskWorkstation.setWhere(wherestr);
		return taskWorkstationDao.deleteByWhere(taskWorkstation);
	}
	
	/**
	 * 关联了line\workstation\processreal系列表
	 */
	public List<TaskWorkstation> selectListByWhere1(String wherestr) {
		taskWorkstation.setWhere(wherestr);
		return taskWorkstationDao.selectListByWhere1(taskWorkstation);
	}
}
