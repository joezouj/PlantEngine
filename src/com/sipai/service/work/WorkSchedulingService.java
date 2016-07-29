package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkSchedulingDao;
import com.sipai.entity.work.WorkScheduling;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;

@Service
public class WorkSchedulingService implements CommService<WorkScheduling>{

	@Resource
	private WorkSchedulingDao workSchedulingDao;
	
	@Override
	public WorkScheduling selectById(String id) {
		return workSchedulingDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workSchedulingDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkScheduling entity) {
		return workSchedulingDao.insert(entity);
	}

	@Override
	public int update(WorkScheduling t) {
		return workSchedulingDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkScheduling> selectListByWhere(String wherestr) {
		WorkScheduling workScheduling = new WorkScheduling();
		workScheduling.setWhere(wherestr);
		return workSchedulingDao.selectListByWhere(workScheduling);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkScheduling workScheduling = new WorkScheduling();
		workScheduling.setWhere(wherestr);
		return workSchedulingDao.deleteByWhere(workScheduling);
	}
	public List<WorkScheduling> getuserinfo(String wherestr) {
		// TODO Auto-generated method stub
		WorkScheduling workScheduling = new WorkScheduling();
		workScheduling.setWhere(wherestr);
		return this.workSchedulingDao.getuserinfo(workScheduling);
	}
	
	public List<WorkScheduling> distinctWdByWhere(String wherestr) {
		// TODO Auto-generated method stub
		WorkScheduling workScheduling = new WorkScheduling();
		workScheduling.setWhere(wherestr);
		return this.workSchedulingDao.distinctWdByWhere(workScheduling);
	}
	
	public List<WorkScheduling> selectTaskUserListByTaskid(String taskid) {
		WorkScheduling workScheduling = new WorkScheduling();
		workScheduling.setWhere("where wf_task_actor.task_id='"+taskid+"' "
				+ "and tb_work_scheduling.workstdt <='"+CommUtil.nowDate()+"' and tb_work_scheduling.workeddt >='"+CommUtil.nowDate()+"'");
		return this.workSchedulingDao.selectTaskUserListByWhere(workScheduling);
	}
	
	/**
	 * @param operator 操作人id
	 * @return 返回 操作人所属 workstation id 列表 
	 */
	public List<WorkScheduling> getWorkSation(String operator) {
		WorkScheduling workScheduling = new WorkScheduling();
		operator = " and userid = '"+operator+"' ";
		workScheduling.setWhere(operator);
		return this.workSchedulingDao.getWorkSation(workScheduling);
	}
}
