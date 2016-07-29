package com.sipai.dao.equipment;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.equipment.GeographyArea;
@Repository
public class GeographyAreaDao extends CommDaoImpl<GeographyArea>{
	public GeographyAreaDao() {
		super();
		this.setMappernamespace("equipment.GeographyAreaMapper");
	}
}
