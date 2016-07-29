package com.sipai.service.user;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.user.RoleDao;
import com.sipai.dao.user.RoleMenuDao;
import com.sipai.dao.user.UserRoleDao;
import com.sipai.entity.user.Role;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.UserRole;
import com.sipai.entity.work.GroupType;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleDao roleDao;
	
	@Resource
	private RoleMenuDao roleMenuDao;
	
	@Resource
	private UserRoleDao userRoleDao;
	
	@Override
	public List<Role> getList(String where) {
		Role role = new Role();
		role.setWhere(where);
		return roleDao.selectListByWhere(role);
	}

	@Override
	public Role getObj(String userId) {
		return roleDao.selectByPrimaryKey(userId);
	}

	@Override
	public int dosave(Role role) {
		return roleDao.insert(role);
	}

	@Override
	public int doupdate(Role role) {
		return roleDao.updateByPrimaryKeySelective(role);
	}

	@Override
	public int dodel(String roleId) {
		return roleDao.deleteByPrimaryKey(roleId);
	}
	
	@Override
	public List<RoleMenu> getRoleMenu(String roleid){
		return roleMenuDao.selectMenuListByRole(roleid);
	}
	
	@Override
	public List<RoleMenu> getRoleFunc(String roleid){
		return roleMenuDao.selectFuncListByRole(roleid);
	}
	
	@Override
	public int updateRoleMenu(String roleid,String menustr){
		int insres=0;
		int delres=roleMenuDao.deleteMenuByRole(roleid);
		if(delres>=0 && !menustr.trim().equals("")){
			String[] menuids=menustr.split(",");
			for(String menuid:menuids){
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleid(roleid);
				roleMenu.setMenuid(menuid);
				
				insres += roleMenuDao.insert(roleMenu);
			}
		}
		return insres;
	}
	
	@Override
	public int updateFuncByRoleMenu(String roleid,String menuid,String funcstr){
		int insres=0;
		int delres=roleMenuDao.deleteFuncByRoleMenu(roleid, menuid);
		if(delres>=0 && !funcstr.trim().equals("")){
			String[] funcids=funcstr.split(",");
			for(String funcid:funcids){
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleid(roleid);
				roleMenu.setMenuid(funcid);
				
				insres += roleMenuDao.insert(roleMenu);
			}
		}
		return insres;
	}
	
	@Override
	public List<UserRole> getUserRole(String roleid){
		return userRoleDao.selectListByRole(roleid);
	}
	
	@Override
	public int updateUserRole(String roleid,String userstr){
		int insres=0;
		int delres=userRoleDao.deleteByRole(roleid);
		if(delres>=0 && !userstr.trim().equals("")){
			String[] userids=userstr.split(",");
			for(String userid:userids){
				UserRole userRole = new UserRole();
				userRole.setRoleid(roleid);
				userRole.setEmpid(userid);
				
				insres += userRoleDao.insert(userRole);
			}
		}
		return insres;
	}
	
	@Override
	public int updateRoleUser(String userid,String rolestr){
		int insres=0;
		int delres=userRoleDao.deleteByUser(userid);
		if(delres>=0 && !rolestr.trim().equals("")){
			String[] roleids=rolestr.split(",");
			for(String roleid:roleids){
				UserRole userRole = new UserRole();
				userRole.setRoleid(roleid);
				userRole.setEmpid(userid);
				
				insres += userRoleDao.insert(userRole);
			}
		}
		return insres;
	}

	@Override
	public int deleteRoleMenuByMenuId(String menuIds) {
		String[] menuids=menuIds.split(",");
		String menuidstr="";
		for(int i=0;i<menuids.length;i++){
			menuidstr += "'"+menuids[i]+"',";
		}
		if(!menuidstr.equals("")){
			menuidstr=" where menuid in ("+menuidstr.substring(0, menuidstr.length()-1)+")";
		}
		RoleMenu roleMenu= new RoleMenu();
		roleMenu.setWhere(menuidstr);
		return roleMenuDao.deleteByWhere(roleMenu);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<Role> list = this.getList(" where name='"+name+"' order by id");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Role role :list){
					if(!id.equals(role.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
