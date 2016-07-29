package com.sipai.entity.user;

import com.sipai.entity.base.SQLAdapter;

public class RoleMenu extends SQLAdapter {
    private String roleid;

    private String menuid;
    
    private Menu menu;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
    
}