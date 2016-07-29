package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.TaskEquipmentDao;
import com.sipai.entity.process.TaskEquipment;
import com.sipai.tools.CommService;
@Service
public class TaskEquipmentService implements CommService<TaskEquipment> {
	@Resource
	private TaskEquipment taskEquipment;

	@Resource
	private TaskEquipmentDao taskEquipmentDao;
	
	@Override
	public TaskEquipment selectById(String id) {
		return taskEquipmentDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return taskEquipmentDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(TaskEquipment entity) {
		return taskEquipmentDao.insert(entity);
	}

	@Override
	public int update(TaskEquipment t) {
		return taskEquipmentDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<TaskEquipment> selectListByWhere(String wherestr) {
		taskEquipment.setWhere(wherestr);
		return taskEquipmentDao.selectListByWhere(taskEquipment);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		taskEquipment.setWhere(wherestr);
		return taskEquipmentDao.deleteByWhere(taskEquipment);
	}

	/**
	 * 关联了processreal系列表
	 */
	public List<TaskEquipment> selectListByWhere1(String wherestr) {
		taskEquipment.setWhere(wherestr);
		return taskEquipmentDao.selectListByWhere1(taskEquipment);
	}
}
