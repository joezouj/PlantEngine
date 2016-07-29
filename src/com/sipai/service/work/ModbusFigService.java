package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.ModbusFigDao;
import com.sipai.entity.work.ModbusFig;
import com.sipai.tools.CommService;

@Service
public class ModbusFigService implements CommService<ModbusFig>{
	@Resource
	private ModbusFigDao modbusFigDao;
	
	@Override
	public ModbusFig selectById(String id) {
		return modbusFigDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return modbusFigDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(ModbusFig entity) {
		return modbusFigDao.insert(entity);
	}

	@Override
	public int update(ModbusFig t) {
		return modbusFigDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<ModbusFig> selectListByWhere(String wherestr) {
		ModbusFig workstation = new ModbusFig();
		workstation.setWhere(wherestr);
		return modbusFigDao.selectListByWhere(workstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		ModbusFig workstation = new ModbusFig();
		workstation.setWhere(wherestr);
		return modbusFigDao.deleteByWhere(workstation);
	}
	
}
