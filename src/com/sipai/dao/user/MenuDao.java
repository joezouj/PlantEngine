package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Menu;

@Repository
public class MenuDao extends CommDaoImpl<Menu>{
	public MenuDao() {
		super();
		this.setMappernamespace("user.MenuMapper");
	}
}