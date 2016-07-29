package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.ModbusFigDao;
import com.sipai.dao.work.ModbusRecordDao;
import com.sipai.entity.work.ModbusRecord;
import com.sipai.entity.work.ModbusRecord;
import com.sipai.tools.CommService;

@Service
public class ModbusRecordService implements CommService<ModbusRecord>{
	@Resource
	private ModbusRecordDao modbusRecordDao;
	
	@Override
	public ModbusRecord selectById(String id) {
		return modbusRecordDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return modbusRecordDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(ModbusRecord entity) {
		return modbusRecordDao.insert(entity);
	}

	@Override
	public int update(ModbusRecord t) {
		return modbusRecordDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<ModbusRecord> selectListByWhere(String wherestr) {
		ModbusRecord workstation = new ModbusRecord();
		workstation.setWhere(wherestr);
		return modbusRecordDao.selectListByWhere(workstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		ModbusRecord workstation = new ModbusRecord();
		workstation.setWhere(wherestr);
		return modbusRecordDao.deleteByWhere(workstation);
	}
	
}
