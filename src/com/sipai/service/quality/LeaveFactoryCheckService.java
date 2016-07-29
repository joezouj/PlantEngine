package com.sipai.service.quality;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.quality.LeaveFactoryCheckDao;
import com.sipai.entity.quality.LeaveFactoryCheck;
import com.sipai.tools.CommService;
@Service
public class LeaveFactoryCheckService implements CommService<LeaveFactoryCheck>{
	@Resource
	private LeaveFactoryCheckDao leaveFactoryCheckDao;
	@Override
	public LeaveFactoryCheck selectById(String id) {
		return leaveFactoryCheckDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return leaveFactoryCheckDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(LeaveFactoryCheck leaveFactoryCheck) {
		return leaveFactoryCheckDao.insert(leaveFactoryCheck);
	}

	@Override
	public int update(LeaveFactoryCheck leaveFactoryCheck) {
		return leaveFactoryCheckDao.updateByPrimaryKeySelective(leaveFactoryCheck);
	}

	@Override
	public List<LeaveFactoryCheck> selectListByWhere(String wherestr) {
		LeaveFactoryCheck leaveFactoryCheck = new LeaveFactoryCheck();
		leaveFactoryCheck.setWhere(wherestr);
		return leaveFactoryCheckDao.selectListByWhere(leaveFactoryCheck);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		LeaveFactoryCheck leaveFactoryCheck = new LeaveFactoryCheck();
		leaveFactoryCheck.setWhere(wherestr);
		return leaveFactoryCheckDao.deleteByWhere(leaveFactoryCheck);
	}

}
