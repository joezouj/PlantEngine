package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsBookDao;
import com.sipai.entity.process.RealDetailsBook;
import com.sipai.tools.CommService;

@Service
public class RealDetailsBookService implements CommService<RealDetailsBook>{
	@Resource
	private RealDetailsBook realDetailsBook;

	@Resource
	private RealDetailsBookDao realDetailsBookDao;
	
	@Override
	public RealDetailsBook selectById(String id) {
		return realDetailsBookDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsBookDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsBook entity) {
		return realDetailsBookDao.insert(entity);
	}

	@Override
	public int update(RealDetailsBook t) {
		return realDetailsBookDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsBook> selectListByWhere(String wherestr) {
		realDetailsBook.setWhere(wherestr);
		return realDetailsBookDao.selectListByWhere(realDetailsBook);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsBook.setWhere(wherestr);
		return realDetailsBookDao.deleteByWhere(realDetailsBook);
	}


}
