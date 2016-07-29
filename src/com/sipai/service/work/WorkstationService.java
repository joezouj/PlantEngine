package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkstationDao;
import com.sipai.entity.equipment.GeographyArea;
import com.sipai.entity.work.Workstation;
import com.sipai.tools.CommService;

@Service
public class WorkstationService implements CommService<Workstation>{
	@Resource
	private WorkstationDao workstationDao;
	
	@Override
	public Workstation selectById(String id) {
		return workstationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workstationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Workstation entity) {
		return workstationDao.insert(entity);
	}

	@Override
	public int update(Workstation t) {
		return workstationDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<Workstation> selectListByWhere(String wherestr) {
		Workstation workstation = new Workstation();
		workstation.setWhere(wherestr);
		return workstationDao.selectListByWhere(workstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		Workstation workstation = new Workstation();
		workstation.setWhere(wherestr);
		return workstationDao.deleteByWhere(workstation);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param serial
	 * @return
	 */
	public boolean checkNotOccupied(String id, String serial) {
		List<Workstation> list = this.selectListByWhere(" where serial='"+serial+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Workstation workstation :list){
					if(!id.equals(workstation.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
