package com.sipai.service.process;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.process.RealDao;
import com.sipai.dao.process.RealDetailsDao;
import com.sipai.dao.process.RealDetailsEquipmentDao;
import com.sipai.dao.process.RealDetailsMaterialDao;
import com.sipai.dao.process.RealDetailsStepDao;
import com.sipai.dao.process.RealDetailsWorkstationDao;
import com.sipai.entity.process.Real;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.process.RealDetailsStep;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.entity.work.Line;
import com.sipai.tools.CommService;

@Service
public class RealService implements CommService<Real>{
	@Resource
	private Real real;
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
	private RealDao realDao;
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
	public Real selectById(String id) {
		return realDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return realDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Real entity) {
		return realDao.insert(entity);
	}

	@Override
	public int update(Real t) {
		return realDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<Real> selectListByWhere(String wherestr) {
		real.setWhere(wherestr);
		return realDao.selectListByWhere(real);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		real.setWhere(wherestr);
		return realDao.deleteByWhere(real);
	}

	/**
	 * 删除所有关联的子表数据
	 * @param id
	 * @return
	 */
	public int deleteChildrenById(String id) {
		String wherestr="where pid in (select id from tb_process_real_details where pid='"+id+"')";
		realDetailsEquipment.setWhere(wherestr);
		realDetailsEquipmentDao.deleteByWhere(realDetailsEquipment);
		realDetailsMaterial.setWhere(wherestr);
		realDetailsMaterialDao.deleteByWhere(realDetailsMaterial);
		realDetailsStep.setWhere(wherestr);
		realDetailsStepDao.deleteByWhere(realDetailsStep);
		realDetailsWorkstation.setWhere(wherestr);
		realDetailsWorkstationDao.deleteByWhere(realDetailsWorkstation);
		
		realDetails.setWhere("where pid='"+id+"'");
		realDetailsDao.deleteByWhere(realDetails);
		return -1;
	}
	
	/**
	 * 删除所有关联的子表数据
	 * @param id
	 * @return
	 */
	public int deleteChildrenByIds(String ids) {
		String wherestr="where pid in (select id from tb_process_real_details where pid in ('"+ids+"'))";
		realDetailsEquipment.setWhere(wherestr);
		realDetailsEquipmentDao.deleteByWhere(realDetailsEquipment);
		realDetailsMaterial.setWhere(wherestr);
		realDetailsMaterialDao.deleteByWhere(realDetailsMaterial);
		realDetailsStep.setWhere(wherestr);
		realDetailsStepDao.deleteByWhere(realDetailsStep);
		realDetailsWorkstation.setWhere(wherestr);
		realDetailsWorkstationDao.deleteByWhere(realDetailsWorkstation);
		
		realDetails.setWhere("where pid in ('"+ids+"')");
		realDetailsDao.deleteByWhere(realDetails);
		return -1;
	}

	/**
	 * 根据产品流程id获取流程id
	 * @param productid
	 * @return
	 */
	public String getProcessidByRealid(String realid) {
		Real real = realDao.selectByPrimaryKey(realid);
		if(real!=null){
			return real.getProcessid();
		}else{
			return "";
		}
	}
	public List<Real> getListByDetailsID(String detailId) {
		String wherestr = " where 1=1 and b.id='"+detailId+"' order by a.id ";
		real.setWhere(wherestr);
		return realDao.selectListByDetailsID(real);
	}
	 /**
	  * 根据产品流程id获取产线list
	  * @param detailId
	  * @return
	  */
	public List<Line> getLineListByRealid(String realid) {
		String wherestr = " where tb_process_real_details.pid='"+realid+"' order by tb_work_line.serial ";
		real.setWhere(wherestr);
		return realDao.getLineListByWhere(real);
	}

	public boolean checkNotOccupied(String id, String name) {
		List<Real> list = this.realDao.getListByName(name);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Real real :list){
					if(!id.equals(real.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
