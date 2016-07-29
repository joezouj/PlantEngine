package com.sipai.dao.process;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.Real;
import com.sipai.entity.work.Line;


@Repository
public class RealDao extends CommDaoImpl<Real>{
	public RealDao() {
		super();
		this.setMappernamespace("process.RealMapper");
	}
	
	public List<Real> selectListByDetailsID(Real real) {
		List<Real> list = this.getSqlSession().selectList("process.RealMapper.selectListByDetailsID", real);
		return list;
	}
	
	public List<Line> getLineListByWhere(Real real) {
		List<Line> list = this.getSqlSession().selectList("process.RealMapper.getLineListByWhere", real);
		return list;
	}

	public List<Real> getListByName(String name) {
		List<Real> list = this.getSqlSession().selectList("process.RealMapper.getListByName", name);
		return list;
	}

}