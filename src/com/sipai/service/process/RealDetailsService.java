package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDetailsDao;
import com.sipai.dao.process.RealDetailsEquipmentDao;
import com.sipai.dao.process.RealDetailsMaterialDao;
import com.sipai.dao.process.RealDetailsStepDao;
import com.sipai.dao.process.RealDetailsWorkstationDao;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.process.RealDetailsStep;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.tools.CommService;

@Service
public class RealDetailsService implements CommService<RealDetails>{
	@Resource
	private RealDetails realDetails;
	@Resource
	private RealDetailsEquipment realDetailsEquipment;
	@Resource
	private RealDetailsMaterial realDetailsMaterial;
	@Resource
	private RealDetailsStep realDetailsStep;
	@Resource
	private RealDetailsWorkstation realDetailsWorkstation;
	
	@Resource
	private RealDetailsDao realDetailsDao;
	@Resource
	private RealDetailsEquipmentDao realDetailsEquipmentDao;
	@Resource
	private RealDetailsMaterialDao realDetailsMaterialDao;
	@Resource
	private RealDetailsStepDao realDetailsStepDao;
	@Resource
	private RealDetailsWorkstationDao realDetailsWorkstationDao;
	
	@Override
	public RealDetails selectById(String id) {
		return realDetailsDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDetailsDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(RealDetails entity) {
		return realDetailsDao.insert(entity);
	}

	@Override
	public int update(RealDetails t) {
		return realDetailsDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<RealDetails> selectListByWhere(String wherestr) {
		realDetails.setWhere(wherestr);
		return realDetailsDao.selectListByWhere(realDetails);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		realDetails.setWhere(wherestr);
		return realDetailsDao.deleteByWhere(realDetails);
	}

	/**
	 * 删除所有关联的子表数据
	 * @param id
	 * @return
	 */
	public int deleteChildrenById(String id) {
		String wherestr="where pid='"+id+"'";
		realDetailsEquipment.setWhere(wherestr);
		realDetailsEquipmentDao.deleteByWhere(realDetailsEquipment);
		realDetailsMaterial.setWhere(wherestr);
		realDetailsMaterialDao.deleteByWhere(realDetailsMaterial);
		realDetailsStep.setWhere(wherestr);
		realDetailsStepDao.deleteByWhere(realDetailsStep);
		realDetailsWorkstation.setWhere(wherestr);
		realDetailsWorkstationDao.deleteByWhere(realDetailsWorkstation);
		return -1;
	}
	
	/**
	 * 删除所有关联的子表数据
	 * @param id
	 * @return
	 */
	public int deleteChildrenByIds(String ids) {
		String wherestr="where pid in ('"+ids+"')";
		realDetailsEquipment.setWhere(wherestr);
		realDetailsEquipmentDao.deleteByWhere(realDetailsEquipment);
		realDetailsMaterial.setWhere(wherestr);
		realDetailsMaterialDao.deleteByWhere(realDetailsMaterial);
		realDetailsStep.setWhere(wherestr);
		realDetailsStepDao.deleteByWhere(realDetailsStep);
		realDetailsWorkstation.setWhere(wherestr);
		realDetailsWorkstationDao.deleteByWhere(realDetailsWorkstation);
		return -1;
	}
}
