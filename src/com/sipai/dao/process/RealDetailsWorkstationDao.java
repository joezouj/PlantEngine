package com.sipai.dao.process;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsWorkstation;


@Repository
public class RealDetailsWorkstationDao extends CommDaoImpl<RealDetailsWorkstation>{
	public RealDetailsWorkstationDao() {
		super();
		this.setMappernamespace("process.RealDetailsWorkstationMapper");
	}
	/**
	 * 链表查询
	 * @return
	 */
	public List<RealDetailsWorkstation> selectListByWhere1(RealDetailsWorkstation realDetailsWorkstation) {
		// TODO Auto-generated method stub
			List<RealDetailsWorkstation> list = getSqlSession().selectList("process.RealDetailsWorkstationMapper.selectListByWhere1", realDetailsWorkstation);
			return list;
	}
}