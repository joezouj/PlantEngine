package com.sipai.service.user;

import java.util.List;

import com.sipai.entity.user.Job;

public interface JobService {
	
	public List<Job> selectListByWhere(String where);
	
	public List<Job> selectListByUnitid(String unitid);

	public Job selectById(String id);

	public int deleteById(String id);

	public int save(Job t);

	public int update(Job t);

}
