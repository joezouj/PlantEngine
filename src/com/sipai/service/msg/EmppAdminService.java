package com.sipai.service.msg;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.msg.EmppAdminDao;
import com.sipai.entity.msg.EmppAdmin;
import com.sipai.tools.CommService;

@Service
public class EmppAdminService implements CommService<EmppAdmin>{
	@Resource
	private EmppAdminDao emppadminDao;
	
	@Override
	public EmppAdmin selectById(String id) {
		return emppadminDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return emppadminDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(EmppAdmin entity) {
		return emppadminDao.insert(entity);
	}

	@Override
	public int update(EmppAdmin t) {
		return emppadminDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<EmppAdmin> selectListByWhere(String wherestr) {
		EmppAdmin emppadmin = new EmppAdmin();
		emppadmin.setWhere(wherestr);
		return emppadminDao.selectListByWhere(emppadmin);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		EmppAdmin emppadmin = new EmppAdmin();
		emppadmin.setWhere(wherestr);
		return emppadminDao.deleteByWhere(emppadmin);
	}
	
}
