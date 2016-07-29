package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.CutterInfo;
@Repository
public class CutterInfoDao extends CommDaoImpl<CutterInfo> {
	public CutterInfoDao() {
		super();
		this.setMappernamespace("material.CutterInfoMapper");
	}

	public List<CutterInfo> getListByCutterCode(String cuttercode) {
		List<CutterInfo> list = this.getSqlSession().selectList("material.CutterInfoMapper.getListByCutterCode", cuttercode);
		return list;
	}
	public List<CutterInfo> getCutterInfo(CutterInfo cutterInfo) {
		List<CutterInfo> list = getSqlSession().selectList("material.CutterInfoMapper.getCutterInfo", cutterInfo);
		return list;
	}
}
