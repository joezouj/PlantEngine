package com.sipai.entity.user;

import com.sipai.entity.base.SQLAdapter;

public class Role extends SQLAdapter{
    private String id;

    private String name;

    private String description;
    
    private int morder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public int getMorder() {
		return morder;
	}

	public void setMorder(int morder) {
		this.morder = morder;
	}
    
}