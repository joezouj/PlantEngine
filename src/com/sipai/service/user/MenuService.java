package com.sipai.service.user;

import java.util.List;

import com.sipai.entity.user.Menu;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.UserPower;

public interface MenuService {
	public Menu getMenuById(String menuId);
	
	public int saveMenu(Menu menu);
	
	public int updateMenu(Menu menu);
	
	public int deleteMenuById(String menuId);
	
	public List<Menu> selectAll();

	public List<Menu> selectListByWhere(String where);
	
	public int deleteByPid(String pid);

	/**
	 * 获取用户菜单和功能
	 */
	public List<UserPower> selectListByUserId(String userid);
	
	/**
	 * 获取用户菜单（不包括功能）
	 */
	public List<UserPower> selectMenuByUserId(String userid);

	/**
	 * 获取用户功能（不包括菜单）
	 */
	public List<UserPower> selectFuncByUserId(String userid);

	/**
	 * 获取用户菜单（包括父节点）
	 */
	public List<Menu> getFullPower(String userid);

	/**
	 * 获取最终节点的菜单（不包括父节点）
	 */
	public List<Menu> getFinalMenu();

	public List<RoleMenu> getFinalMenuByRoleId(String roleid);

	/**
	 * 获取角色功能权限
	 * @param roleid
	 * @return
	 */
	public List<RoleMenu> getFuncByRoleId(String roleid);

	public List<Menu> getFuncByMenuId(String roleid);

}
