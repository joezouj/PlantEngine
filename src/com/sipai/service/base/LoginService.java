package com.sipai.service.base;

import com.sipai.entity.user.User;

public interface LoginService {
	User Login(String usrName, String psw);

	User CardLogin(String j_cardid);
	
}
