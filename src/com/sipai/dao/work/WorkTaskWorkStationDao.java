package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkTaskWorkStation;

@Repository
public class WorkTaskWorkStationDao extends CommDaoImpl<WorkTaskWorkStation>{
	public WorkTaskWorkStationDao() {
		super();
		this.setMappernamespace("work.WorkTaskWorkStationMapper");
	}
	
	/**
	 * 链表删除
	 * @return
	 */
	public int deleteByWhere1(WorkTaskWorkStation workTaskWorkStation){
		return getSqlSession().delete("work.WorkTaskWorkStationMapper.deleteByWhere1",workTaskWorkStation);
	}
	/**
	 * 链表查询
	 * @return
	 */
	public List<WorkTaskWorkStation> selectListByWhere1(WorkTaskWorkStation workTaskWorkStation) {
		// TODO Auto-generated method stub
			List<WorkTaskWorkStation> list = getSqlSession().selectList("work.WorkTaskWorkStationMapper.selectListByWhere1", workTaskWorkStation);
			return list;
	}
	
	/**
	 * 链表查询line
	 * @return
	 */
	public List<WorkTaskWorkStation> selectListByWhere2(WorkTaskWorkStation workTaskWorkStation) {
		// TODO Auto-generated method stub
			List<WorkTaskWorkStation> list = getSqlSession().selectList("work.WorkTaskWorkStationMapper.selectListByWhere2", workTaskWorkStation);
			return list;
	}
	
	public List<WorkTaskWorkStation> selectListWithTasknameByWhere(WorkTaskWorkStation workTaskWorkStation) {
			List<WorkTaskWorkStation> list = getSqlSession().selectList("work.WorkTaskWorkStationMapper.selectListWithTasknameByWhere", workTaskWorkStation);
			return list;
	}
	
	public List<WorkTaskWorkStation> getWorkStation(WorkTaskWorkStation workTaskWorkStation) {
		// TODO Auto-generated method stub
			List<WorkTaskWorkStation> list = getSqlSession().selectList("work.WorkTaskWorkStationMapper.getWorkStation", workTaskWorkStation);
			return list;
	}
	public List<WorkTaskWorkStation> getWorkStationByUserid(WorkTaskWorkStation workTaskWorkStation) {
		// TODO Auto-generated method stub
			List<WorkTaskWorkStation> list = getSqlSession().selectList("work.WorkTaskWorkStationMapper.getWorkStationbyuserid", workTaskWorkStation);
			return list;
	}
}
