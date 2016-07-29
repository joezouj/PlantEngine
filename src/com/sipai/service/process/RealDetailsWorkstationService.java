package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsWorkstationDao;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.tools.CommService;

@Service
public class RealDetailsWorkstationService implements CommService<RealDetailsWorkstation>{
	@Resource
	private RealDetailsWorkstation realDetailsWorkstation;

	@Resource
	private RealDetailsWorkstationDao realDetailsWorkstationDao;
	
	@Override
	public RealDetailsWorkstation selectById(String id) {
		return realDetailsWorkstationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsWorkstationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsWorkstation entity) {
		return realDetailsWorkstationDao.insert(entity);
	}

	@Override
	public int update(RealDetailsWorkstation t) {
		return realDetailsWorkstationDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsWorkstation> selectListByWhere(String wherestr) {
		realDetailsWorkstation.setWhere(wherestr);
		return realDetailsWorkstationDao.selectListByWhere(realDetailsWorkstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsWorkstation.setWhere(wherestr);
		return realDetailsWorkstationDao.deleteByWhere(realDetailsWorkstation);
	}

	public List<RealDetailsWorkstation> selectListByWhere1(String wherestr) {
		realDetailsWorkstation.setWhere(wherestr);
		return realDetailsWorkstationDao.selectListByWhere1(realDetailsWorkstation);
	}
}
