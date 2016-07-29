package com.sipai.entity.process;

import com.sipai.entity.base.SQLAdapter;

public class Processes  extends SQLAdapter {
    private String id;

    private String pid;

    private String name;
    
    private String display_Name;

    private String description;

    private String creator;

    private String create_time;

    private String state;

    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getDisplay_Name() {
		return display_Name;
	}

	public void setDisplay_Name(String display_Name) {
		this.display_Name = display_Name;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}