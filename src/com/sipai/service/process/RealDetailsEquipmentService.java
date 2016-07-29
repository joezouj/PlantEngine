package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsEquipmentDao;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.tools.CommService;

@Service
public class RealDetailsEquipmentService implements CommService<RealDetailsEquipment>{
	@Resource
	private RealDetailsEquipment realDetailsEquipment;

	@Resource
	private RealDetailsEquipmentDao realDetailsEquipmentDao;
	
	@Override
	public RealDetailsEquipment selectById(String id) {
		return realDetailsEquipmentDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsEquipmentDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetailsEquipment entity) {
		return realDetailsEquipmentDao.insert(entity);
	}

	@Override
	public int update(RealDetailsEquipment t) {
		return realDetailsEquipmentDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetailsEquipment> selectListByWhere(String wherestr) {
		realDetailsEquipment.setWhere(wherestr);
		return realDetailsEquipmentDao.selectListByWhere(realDetailsEquipment);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetailsEquipment.setWhere(wherestr);
		return realDetailsEquipmentDao.deleteByWhere(realDetailsEquipment);
	}


}
