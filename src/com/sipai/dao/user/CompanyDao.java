package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Company;

@Repository
public class CompanyDao extends CommDaoImpl<Company>{
	public CompanyDao() {
		super();
		this.setMappernamespace("user.CompanyMapper");
	}
}