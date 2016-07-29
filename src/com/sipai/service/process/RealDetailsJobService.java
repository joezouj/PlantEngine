package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsJobDao;
import com.sipai.entity.process.RealDetailsJob;
import com.sipai.tools.CommService;

@Service
public class RealDetailsJobService implements CommService<RealDetailsJob>{
	@Resource
	private RealDetailsJob realDetailsJob;

	@Resource
	private RealDetailsJobDao realDetailsJobDao;
	
	@Override
	public RealDetailsJob selectById(String id) {
		return realDetailsJobDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsJobDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsJob entity) {
		return realDetailsJobDao.insert(entity);
	}

	@Override
	public int update(RealDetailsJob t) {
		return realDetailsJobDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsJob> selectListByWhere(String wherestr) {
		realDetailsJob.setWhere(wherestr);
		return realDetailsJobDao.selectListByWhere(realDetailsJob);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsJob.setWhere(wherestr);
		return realDetailsJobDao.deleteByWhere(realDetailsJob);
	}


}
