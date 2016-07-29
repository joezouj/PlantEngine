package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsMaterialDao;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.tools.CommService;

@Service
public class RealDetailsMaterialService implements CommService<RealDetailsMaterial>{
	@Resource
	private RealDetailsMaterial realDetailsMaterial;

	@Resource
	private RealDetailsMaterialDao realDetailsMaterialDao;
	
	@Override
	public RealDetailsMaterial selectById(String id) {
		return realDetailsMaterialDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsMaterialDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsMaterial entity) {
		return realDetailsMaterialDao.insert(entity);
	}

	@Override
	public int update(RealDetailsMaterial t) {
		return realDetailsMaterialDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsMaterial> selectListByWhere(String wherestr) {
		realDetailsMaterial.setWhere(wherestr);
		return realDetailsMaterialDao.selectListByWhere(realDetailsMaterial);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsMaterial.setWhere(wherestr);
		return realDetailsMaterialDao.deleteByWhere(realDetailsMaterial);
	}


}
