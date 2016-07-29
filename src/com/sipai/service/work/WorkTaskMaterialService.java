package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkTaskMaterialDao;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.Workstation;
import com.sipai.tools.CommService;

@Service
public class WorkTaskMaterialService implements CommService<WorkTaskMaterial>{

	@Resource
	private WorkTaskMaterialDao workTaskMaterialDao;
	
	@Override
	public WorkTaskMaterial selectById(String id) {
		return workTaskMaterialDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workTaskMaterialDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkTaskMaterial entity) {
		return workTaskMaterialDao.insert(entity);
	}

	@Override
	public int update(WorkTaskMaterial t) {
		return workTaskMaterialDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkTaskMaterial> selectListByWhere(String wherestr) {
		WorkTaskMaterial workTaskWorkStation = new WorkTaskMaterial();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskMaterialDao.selectListByWhere(workTaskWorkStation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkTaskMaterial workTaskWorkStation = new WorkTaskMaterial();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskMaterialDao.deleteByWhere(workTaskWorkStation);
	}
	
	public List<WorkTaskMaterial> batchinglistByWhere(String wherestr) {
		WorkTaskMaterial workTaskWorkStation = new WorkTaskMaterial();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskMaterialDao.batchinglistByWhere(workTaskWorkStation);
	}
	
	public int executeSql(String sql) {
		WorkTaskMaterial workTaskWorkStation = new WorkTaskMaterial();
		workTaskWorkStation.setSql(sql);
		return workTaskMaterialDao.executeSql(workTaskWorkStation);
	}
}
