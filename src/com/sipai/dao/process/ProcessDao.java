package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.Process;


@Repository
public class ProcessDao extends CommDaoImpl<Process>{
	public ProcessDao() {
		super();
		this.setMappernamespace("process.ProcessMapper");
	}

}