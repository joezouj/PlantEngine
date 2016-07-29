package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.Group;

@Repository
public class GroupDao extends CommDaoImpl<Group>{
	public GroupDao() {
		super();
		this.setMappernamespace("work.GroupMapper");
	}
}