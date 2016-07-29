package com.sipai.service.user;

import java.util.List;

import com.sipai.entity.user.Role;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.UserRole;

public interface RoleService {
	
	public List<Role> getList(String where);
	
	public Role getObj(String userId);
	
	public int dosave(Role role);
	
	public int doupdate(Role role);
	
	public int dodel(String roleId);

	/**
	 * 更新角色和菜单关系表
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public int updateRoleMenu(String roleid, String menustr);

	/**
	 * 获得角色相关菜单
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public List<RoleMenu> getRoleMenu(String roleid);
	
	/**
	 * 获得角色相关功能
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public List<RoleMenu> getRoleFunc(String roleid);
	
	/**
	 * 根据角色，更新用户关系
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public int updateUserRole(String roleid, String menustr);

	/**
	 * 根据用户，更新角色关系
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public int updateRoleUser(String userid, String rolestr);
	
	/**
	 * 获得角色相关用户
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public List<UserRole> getUserRole(String roleid);

	/**
	 * 根据角色和菜单，更新功能权限
	 * @param roleid
	 * @param menustr
	 * @return
	 */
	public int updateFuncByRoleMenu(String roleid, String menuid, String funcstr);

	public int deleteRoleMenuByMenuId(String menuIds);
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name);

}
