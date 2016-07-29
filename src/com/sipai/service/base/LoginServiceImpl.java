package com.sipai.service.base;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sipai.dao.user.UserDao;
import com.sipai.entity.user.User;
import com.sipai.tools.CommUtil;


@Service
@Transactional("transactionManager")
public class LoginServiceImpl implements LoginService {
	@Resource
	UserDao userDao;
	
	public String getPassword(String usrName) {
		if (usrName != null && !usrName.isEmpty()) {
			User user1 = new User();
			user1.setWhere(" where name = '"+usrName+"' ");
			User user = userDao.selectByWhere(user1);
			if (null != user) {
				return user.getPassword();
			}
		}
		return null;
	}

	public User Login(String usrName, String psw) {
		if (usrName != null && !usrName.isEmpty() && psw != null
				&& !psw.isEmpty()) {
			User user1 = new User();
			user1.setWhere(" where name = '"+usrName+"' ");
			User user = userDao.selectByWhere(user1);
			if (null != user) {
				psw=CommUtil.generatePassword(psw);
				if (psw.equals(user.getPassword())) {
					return user;
				}
			}
		}
		return null;
	}
	
	public User CardLogin(String cardid) {
		if (cardid != null && !cardid.isEmpty()) {
			User user1 = new User();
			user1.setWhere(" where cardid = '"+cardid+"' ");
			User user = userDao.selectByWhere(user1);
			if (null != user) {
				return user;
			}
		}
		return null;
	}

}
