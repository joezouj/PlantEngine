package com.sipai.webservice.server.user;

import com.sipai.entity.user.User;
import com.sipai.service.user.UserService;
import com.sipai.tools.SpringContextUtil;
public class UserServer {
	
	public User getUserById(String userId) {
		UserService userService = (UserService) SpringContextUtil.getBean("userService");
		return userService.getUserById(userId);
	}
}
