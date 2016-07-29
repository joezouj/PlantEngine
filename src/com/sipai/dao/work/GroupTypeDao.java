package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.GroupType;
import com.sipai.entity.work.WorkScheduling;

@Repository
public class GroupTypeDao extends CommDaoImpl<GroupType>{
	public GroupTypeDao() {
		super();
		this.setMappernamespace("work.GroupTypeMapper");
	}
}
