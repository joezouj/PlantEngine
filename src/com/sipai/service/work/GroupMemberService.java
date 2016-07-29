package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.GroupMemberDao;
import com.sipai.entity.work.GroupMember;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;

@Service
public class GroupMemberService implements CommService<GroupMember>{
	@Resource
	private GroupMemberDao groupMemberDao;
	
	@Override
	public GroupMember selectById(String id) {
		return groupMemberDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return groupMemberDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(GroupMember entity) {
		return groupMemberDao.insert(entity);
	}

	@Override
	public int update(GroupMember entity) {
		return groupMemberDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<GroupMember> selectListByWhere(String wherestr) {
		return null;
	}

	@Override
	public int deleteByWhere(String wherestr) {
		return 0;
	}
	
	/**
	 * 按照班组id选择
	 * @param groupid
	 * @return
	 */
	public List<GroupMember> selectListByGroupId(String groupid) {
		return groupMemberDao.selectListByGroupId(groupid);
	}
	
	/**
	 * 按照班组id删除
	 * @param groupid
	 * @return
	 */
	public int deleteByGroupId(String groupid) {
		return groupMemberDao.deleteByGroupId(groupid);
	}
	
	/**
	 * 保存班组人员
	 * @param groupid
	 * @return
	 */
	public int saveMembers(String groupid,String members,String usertype) {
		int res=0;
		GroupMember leader = new GroupMember();
		leader.setGroupid(groupid);
		leader.setUsertype(usertype);
		String[] leaderids= members.split(",");
		for(int i=0;i<leaderids.length;i++){
			leader.setId(CommUtil.getUUID());
			leader.setUserid(leaderids[i]);
			res += this.save(leader);
		}
		return res;
	}
	
	/**
	 * 按照排班条件筛选
	 * @param groupid
	 * @return
	 */
	public List<GroupMember> selectListByScheduling(String wherestr ) {
		return groupMemberDao.selectListByScheduling(wherestr);
	}
	
}
