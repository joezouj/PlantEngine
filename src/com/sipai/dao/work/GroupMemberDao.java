package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.GroupMember;

@Repository
public class GroupMemberDao extends CommDaoImpl<GroupMember>{
	public GroupMemberDao() {
		super();
		this.setMappernamespace("work.GroupMemberMapper");
	}
	
	public List<GroupMember> selectListByGroupId(String groupid){
		return this.getSqlSession().selectList("work.GroupMemberMapper.selectListByGroupId", groupid);
	}
	
	public int deleteByGroupId(String groupid){
		return this.getSqlSession().delete("work.GroupMemberMapper.deleteByGroupId", groupid);
	}
	
	public List<GroupMember> selectListByScheduling(String wherestr){
		return this.getSqlSession().selectList("work.GroupMemberMapper.selectListByScheduling", wherestr);
	}
}