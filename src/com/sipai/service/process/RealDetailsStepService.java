package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsStepDao;
import com.sipai.entity.process.RealDetailsStep;
import com.sipai.tools.CommService;

@Service
public class RealDetailsStepService implements CommService<RealDetailsStep>{
	@Resource
	private RealDetailsStep realDetailsStep;

	@Resource
	private RealDetailsStepDao realDetailsStepDao;
	
	@Override
	public RealDetailsStep selectById(String id) {
		return realDetailsStepDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsStepDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsStep entity) {
		return realDetailsStepDao.insert(entity);
	}

	@Override
	public int update(RealDetailsStep t) {
		return realDetailsStepDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsStep> selectListByWhere(String wherestr) {
		realDetailsStep.setWhere(wherestr);
		return realDetailsStepDao.selectListByWhere(realDetailsStep);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsStep.setWhere(wherestr);
		return realDetailsStepDao.deleteByWhere(realDetailsStep);
	}


}
