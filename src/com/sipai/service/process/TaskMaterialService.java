package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.TaskMaterialDao;
import com.sipai.entity.process.TaskMaterial;
import com.sipai.tools.CommService;
@Service
public class TaskMaterialService implements CommService<TaskMaterial> {
	@Resource
	private TaskMaterial taskMaterial;

	@Resource
	private TaskMaterialDao taskMaterialDao;
	
	@Override
	public TaskMaterial selectById(String id) {
		return taskMaterialDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return taskMaterialDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(TaskMaterial entity) {
		return taskMaterialDao.insert(entity);
	}

	@Override
	public int update(TaskMaterial t) {
		return taskMaterialDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<TaskMaterial> selectListByWhere(String wherestr) {
		taskMaterial.setWhere(wherestr);
		return taskMaterialDao.selectListByWhere(taskMaterial);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		taskMaterial.setWhere(wherestr);
		return taskMaterialDao.deleteByWhere(taskMaterial);
	}
	
	/**
	 * 关联了processreal系列表
	 */
	public List<TaskMaterial> selectListByWhere1(String wherestr) {
		taskMaterial.setWhere(wherestr);
		return taskMaterialDao.selectListByWhere1(taskMaterial);
	}
}
