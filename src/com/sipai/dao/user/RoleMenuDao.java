package com.sipai.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.RoleMenu;

@Repository
public class RoleMenuDao extends CommDaoImpl<RoleMenu>{
	public RoleMenuDao() {
		super();
		this.setMappernamespace("user.RoleMenuMapper");
	}
	
	public List<RoleMenu> selectMenuListByRole(String roleid){
		return this.getSqlSession().selectList("user.RoleMenuMapper.selectMenuListByRole", roleid);
	}
	
	public List<RoleMenu> selectFuncListByRole(String roleid){
		return this.getSqlSession().selectList("user.RoleMenuMapper.selectFuncListByRole", roleid);
	}
	
	public int deleteMenuByRole(String roleid){
		return this.getSqlSession().delete("user.RoleMenuMapper.deleteMenuByRole", roleid);
	}
	
	public int deleteFuncByRoleMenu(String roleid,String menuid){
		Map<String, Object> param=new HashMap<String, Object>();  
		param.put("roleid1", roleid);
		param.put("menuid1", menuid);
		return this.getSqlSession().delete("user.RoleMenuMapper.deleteFuncByRoleMenu",param);
	}
}