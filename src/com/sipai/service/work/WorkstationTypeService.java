package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkstationTypeDao;
import com.sipai.entity.work.WorkstationType;
import com.sipai.tools.CommService;

@Service
public class WorkstationTypeService implements CommService<WorkstationType>{
	@Resource
	private WorkstationTypeDao workstationTypeDao;
	
	@Override
	public WorkstationType selectById(String id) {
		return workstationTypeDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workstationTypeDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkstationType entity) {
		return workstationTypeDao.insert(entity);
	}

	@Override
	public int update(WorkstationType t) {
		return workstationTypeDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkstationType> selectListByWhere(String wherestr) {
		WorkstationType workstationType = new WorkstationType();
		workstationType.setWhere(wherestr);
		return workstationTypeDao.selectListByWhere(workstationType);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkstationType workstationType = new WorkstationType();
		workstationType.setWhere(wherestr);
		return workstationTypeDao.deleteByWhere(workstationType);
	}

	public boolean checkNotOccupied(String id, String serial) {
		List<WorkstationType> list = this.workstationTypeDao.getListBySerial(serial);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(WorkstationType workstationType :list){
					if(!id.equals(workstationType.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
