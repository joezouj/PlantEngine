package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.Clients;
import com.sipai.entity.material.MaterialShort;
import com.sipai.entity.material.MaterialType;
@Repository
public class ClientsDao extends CommDaoImpl<Clients> {
	public ClientsDao() {
		super();
		this.setMappernamespace("material.ClientsMapper");
	}

}
