package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.GroupDao;
import com.sipai.dao.work.UserWorkStationDao;
import com.sipai.entity.work.UserWorkStation;
import com.sipai.entity.work.UserWorkStation;
import com.sipai.tools.CommService;

@Service
public class UserWorkStationService implements CommService<UserWorkStation>{
	@Resource
	private UserWorkStationDao userWorkStationDao;
	
	@Override
	public UserWorkStation selectById(String id) {
		return userWorkStationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return userWorkStationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(UserWorkStation entity) {
		return userWorkStationDao.insert(entity);
	}

	@Override
	public int update(UserWorkStation entity) {
		return userWorkStationDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<UserWorkStation> selectListByWhere(String wherestr) {
		UserWorkStation group = new UserWorkStation();
		group.setWhere(wherestr);
		return userWorkStationDao.selectListByWhere(group);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		UserWorkStation group = new UserWorkStation();
		group.setWhere(wherestr);
		return userWorkStationDao.deleteByWhere(group);
	}
	
}
