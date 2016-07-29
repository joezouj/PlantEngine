package com.sipai.service.msg;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.sipai.dao.msg.EmppSendUserDao;
import com.sipai.entity.msg.EmppSendUser;
import com.sipai.tools.CommService;

@Service
public class EmppSendUserService implements CommService<EmppSendUser>{
	@Resource
	private EmppSendUserDao emppsenduserDao;
	
	@Override
	public EmppSendUser selectById(String id) {
		return emppsenduserDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return emppsenduserDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(EmppSendUser entity) {
		return emppsenduserDao.insert(entity);
	}

	@Override
	public int update(EmppSendUser t) {
		return emppsenduserDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<EmppSendUser> selectListByWhere(String wherestr) {
		EmppSendUser emppsenduser = new EmppSendUser();
		emppsenduser.setWhere(wherestr);
		return emppsenduserDao.selectListByWhere(emppsenduser);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		EmppSendUser emppsenduser = new EmppSendUser();
		emppsenduser.setWhere(wherestr);
		return emppsenduserDao.deleteByWhere(emppsenduser);
	}
	
}
