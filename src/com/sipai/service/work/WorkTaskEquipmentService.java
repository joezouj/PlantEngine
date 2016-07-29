package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkTaskEquipmentDao;
import com.sipai.entity.work.WorkTaskEquipment;
import com.sipai.tools.CommService;

@Service
public class WorkTaskEquipmentService implements CommService<WorkTaskEquipment>{
	@Resource
	private WorkTaskEquipment workTaskEquipment;

	@Resource
	private WorkTaskEquipmentDao workTaskEquipmentDao;
	
	@Override
	public WorkTaskEquipment selectById(String id) {
		return workTaskEquipmentDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workTaskEquipmentDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkTaskEquipment entity) {
		return workTaskEquipmentDao.insert(entity);
	}

	@Override
	public int update(WorkTaskEquipment t) {
		return workTaskEquipmentDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkTaskEquipment> selectListByWhere(String wherestr) {
		workTaskEquipment.setWhere(wherestr);
		return workTaskEquipmentDao.selectListByWhere(workTaskEquipment);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		workTaskEquipment.setWhere(wherestr);
		return workTaskEquipmentDao.deleteByWhere(workTaskEquipment);
	}

	public int executeSql(String sql) {
		workTaskEquipment.setSql(sql);
		return workTaskEquipmentDao.executeSql(workTaskEquipment);
	}
}
