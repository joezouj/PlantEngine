package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkScheduling;

@Repository
public class WorkSchedulingDao extends CommDaoImpl<WorkScheduling>{
	public WorkSchedulingDao() {
		super();
		this.setMappernamespace("work.WorkSchedulingMapper");
	}
	public List<WorkScheduling> getuserinfo(WorkScheduling workScheduling) {
		// TODO Auto-generated method stub
			List<WorkScheduling> list = getSqlSession().selectList("work.WorkSchedulingMapper.getuserinfo", workScheduling);
			return list;
	}
	public List<WorkScheduling> getWorkSation(WorkScheduling workScheduling) {
			
			List<WorkScheduling> list = getSqlSession().selectList("work.WorkSchedulingMapper.getWorkSation", workScheduling);
			return list;
	}
	public List<WorkScheduling> distinctWdByWhere(WorkScheduling workScheduling) {
		
		List<WorkScheduling> list = getSqlSession().selectList("work.WorkSchedulingMapper.distinctWdByWhere", workScheduling);
		return list;
}
	public List<WorkScheduling> selectTaskUserListByWhere(WorkScheduling workScheduling) {
		List<WorkScheduling> list = getSqlSession().selectList("work.WorkSchedulingMapper.selectTaskUserListByWhere", workScheduling);
		return list;
	}
}
