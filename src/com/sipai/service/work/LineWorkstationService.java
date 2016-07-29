package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.LineWorkstationDao;
import com.sipai.entity.work.LineWorkstation;
import com.sipai.entity.work.Workstation;
import com.sipai.tools.CommService;

@Service
public class LineWorkstationService implements CommService<LineWorkstation>{
	@Resource
	private LineWorkstationDao lineWorkstationDao;
	
	@Override
	public LineWorkstation selectById(String id) {
		return lineWorkstationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return lineWorkstationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(LineWorkstation entity) {
		return lineWorkstationDao.insert(entity);
	}

	@Override
	public int update(LineWorkstation entity) {
		return lineWorkstationDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<LineWorkstation> selectListByWhere(String wherestr) {
		LineWorkstation lineWorkstation = new LineWorkstation();
		lineWorkstation.setWhere(wherestr);
		return lineWorkstationDao.selectListByWhere(lineWorkstation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		LineWorkstation lineWorkstation = new LineWorkstation();
		lineWorkstation.setWhere(wherestr);
		return lineWorkstationDao.deleteByWhere(lineWorkstation);
	}
	
	public List<Workstation> selectWorkstation(String wherestr) {
		Workstation workstation = new Workstation();
		workstation.setWhere(wherestr);
		return lineWorkstationDao.selectWorkstation(workstation);
	}
	
}
