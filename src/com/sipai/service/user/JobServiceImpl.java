package com.sipai.service.user;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.user.CompanyDao;
import com.sipai.dao.user.DeptDao;
import com.sipai.dao.user.JobDao;
import com.sipai.dao.user.UnitDao;
import com.sipai.entity.user.Company;
import com.sipai.entity.user.Dept;
import com.sipai.entity.user.Job;
import com.sipai.entity.user.Unit;

@Service
public class JobServiceImpl implements JobService {

	@Resource
	private JobDao jobDao;
	
	@Override
	public List<Job> selectListByWhere(String where) {
		Job job= new Job();
		job.setWhere(where);
		return this.jobDao.selectListByWhere(job);
	}
	
	@Override
	public Job selectById(String id) {
		return this.jobDao.selectByPrimaryKey(id);
	}
	
	@Override
	public int deleteById(String id) {
		return this.jobDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public int save(Job t) {
		return this.jobDao.insert(t);
	}
	
	@Override
	public int update(Job t) {
		return this.jobDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<Job> selectListByUnitid(String unitid) {
		return this.jobDao.selectListByUnitid(unitid);
	}
	
}
