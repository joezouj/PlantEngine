package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryTask;
import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkTaskWorkStationDao;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkOrder;
import com.sipai.entity.work.WorkTaskEquipment;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.service.process.RealDetailsEquipmentService;
import com.sipai.service.process.RealDetailsMaterialService;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealDetailsWorkstationService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;

@Service
public class WorkTaskWorkStationService implements CommService<WorkTaskWorkStation>{

	@Resource
	private WorkTaskWorkStationDao workTaskWorkStationDao;
	
	@Resource
	private RealDetailsService realDetailsService;
	@Resource
	private RealDetailsWorkstationService realDetailsWorkstationService;
	@Resource
	private RealDetailsMaterialService realDetailsMaterialService;
	@Resource
	private RealDetailsEquipmentService realDetailsEquipmentService;
	@Resource
	private WorkTaskMaterialService workTaskMaterialService;
	@Resource
	private WorkTaskEquipmentService workTaskEquipmentService;
	
	@Resource
	private SnakerEngineFacets facets;
	
	@Override
	public WorkTaskWorkStation selectById(String id) {
		return workTaskWorkStationDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workTaskWorkStationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkTaskWorkStation entity) {
		return workTaskWorkStationDao.insert(entity);
	}

	@Override
	public int update(WorkTaskWorkStation t) {
		return workTaskWorkStationDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkTaskWorkStation> selectListByWhere(String wherestr) {
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskWorkStationDao.selectListByWhere(workTaskWorkStation);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskWorkStationDao.deleteByWhere(workTaskWorkStation);
	}
	
	public int deleteByWhere1(String wherestr) {
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskWorkStationDao.deleteByWhere1(workTaskWorkStation);
	}
	
	public List<WorkTaskWorkStation> selectListByWhere1(String wherestr) {
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskWorkStationDao.selectListByWhere1(workTaskWorkStation);
	}
	
	public List<WorkTaskWorkStation> selectListByWhere2(String wherestr) {
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return workTaskWorkStationDao.selectListByWhere2(workTaskWorkStation);
	}
	
	public List<WorkTaskWorkStation> getWorkStation(String wherestr) {
		// TODO Auto-generated method stub
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return this.workTaskWorkStationDao.getWorkStation(workTaskWorkStation);
	}
	public List<WorkTaskWorkStation> getWorkStationByUserid(String wherestr) {
		// TODO Auto-generated method stub
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return this.workTaskWorkStationDao.getWorkStationByUserid(workTaskWorkStation);
	}
	
	/**
	 * 返回带有snaker的taskname数据的列表
	 * @param wherestr
	 * @return
	 */
	public List<WorkTaskWorkStation> selectListWithTasknameByWhere(String wherestr){
		WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
		workTaskWorkStation.setWhere(wherestr);
		return this.workTaskWorkStationDao.selectListWithTasknameByWhere(workTaskWorkStation);
	}
	
	/**
	 * 返回未开始过的工位(包括：当前的工位以及还没执行到的工位)
	 * @param wfOrderid
	 * @return
	 */
	public List<WorkTaskWorkStation> getFutureTask(WorkOrder workOrder) {
		List<HistoryTask> tasks = facets.getEngine().query().getHistoryTasks(new QueryFilter().setOrderId(workOrder.getWforderid()));
		List<WorkTaskWorkStation> list = this.selectListWithTasknameByWhere("where tb_work_task_workstation.taskid='"+workOrder.getId()+"'");
		for(int i=0;i<list.size();i++){
			for(int j=0;j<tasks.size();j++){
				if(list.get(i).getWftaskname().equals(tasks.get(j).getTaskName())){
					list.remove(i);//将存在于histTask中有的数据移除
				}
			}
		}
		return list;
	}
	
	/**
	 * 添加默认的工位以及对应的物料、设备
	 * @param workOrder
	 * @param cu
	 * @return
	 */
	public int saveDefault(WorkOrder workOrder,User cu,int index) {
		int result=0;
		List<RealDetailsMaterial> RealDetailsMaterialList = this.realDetailsMaterialService.selectListByWhere(
				"where pid in ( select id from tb_process_real_details where pid = '"+workOrder.getProcessrealid()+"')");
		List<RealDetailsEquipment> RealDetailsEquipmentList = this.realDetailsEquipmentService.selectListByWhere(
				"where pid in ( select id from tb_process_real_details where pid = '"+workOrder.getProcessrealid()+"')");
		
		List<RealDetails> RealDetailsList = this.realDetailsService.selectListByWhere("where pid='"+workOrder.getProcessrealid()+"'");
		if(RealDetailsList!=null && RealDetailsList.size()>0){
			for(int i=0;i<RealDetailsList.size();i++){
				List<RealDetailsWorkstation> workStationList = this.realDetailsWorkstationService.selectListByWhere1(
						"where tb_work_line.id = '"+workOrder.getLineid()+"' and pid='"+RealDetailsList.get(i).getId()+"'");
				//添加默认的工位
				if(workStationList!=null && workStationList.size()>0){
					RealDetailsWorkstation realDetailsWorkstation = workStationList.get(index % workStationList.size());//交替分布
					
//					System.out.println(index+" "+ workStationList.size() +" "+index % workStationList.size()+" "+workOrder.getId()+" "+RealDetailsList.get(i).getName()+" "+realDetailsWorkstation.getWorkstation().getName());
					WorkTaskWorkStation workTaskWorkStation = new WorkTaskWorkStation();
					workTaskWorkStation.setWorkstationid(realDetailsWorkstation.getWorkstationid());
					workTaskWorkStation.setWorkstationname(realDetailsWorkstation.getWorkstation().getName());
					workTaskWorkStation.setProcessRealid(workOrder.getProcessrealid());
					workTaskWorkStation.setProcessRealdetailid(realDetailsWorkstation.getPid());
					workTaskWorkStation.setId(CommUtil.getUUID());
					workTaskWorkStation.setInsuser(cu.getId());
					workTaskWorkStation.setInsdt(CommUtil.nowDate());
					workTaskWorkStation.setTaskid(workOrder.getId());
					result += this.workTaskWorkStationDao.insert(workTaskWorkStation);
					
					//添加默认的物料
					if(RealDetailsMaterialList!=null && RealDetailsMaterialList.size()>0){
						for(int j=0;j<RealDetailsMaterialList.size();j++){
							if(RealDetailsMaterialList.get(j).getPid().equals(workTaskWorkStation.getProcessRealdetailid())){
								WorkTaskMaterial workTaskMaterial = new WorkTaskMaterial();
								workTaskMaterial.setId(CommUtil.getUUID());
								workTaskMaterial.setWorkstationid(workTaskWorkStation.getWorkstationid());
								workTaskMaterial.setMaterialid(RealDetailsMaterialList.get(j).getMaterialid());
								workTaskMaterial.setAmount(String.valueOf(RealDetailsMaterialList.get(j).getAmount()));
								workTaskMaterial.setTaskid(workOrder.getId());
								workTaskMaterial.setInsuser(cu.getId());
								workTaskMaterial.setInsdt(CommUtil.nowDate());
								this.workTaskMaterialService.save(workTaskMaterial);
							}
						}
					}
					
					//添加默认的设备
					if(RealDetailsEquipmentList!=null && RealDetailsEquipmentList.size()>0){
						for(int k=0;k<RealDetailsEquipmentList.size();k++){
							if(RealDetailsEquipmentList.get(k).getPid().equals(workTaskWorkStation.getProcessRealdetailid())){
								WorkTaskEquipment workTaskEquipment = new WorkTaskEquipment();
								workTaskEquipment.setId(CommUtil.getUUID());
								workTaskEquipment.setWorkstationid(workTaskWorkStation.getWorkstationid());
								workTaskEquipment.setEquipmentid(RealDetailsEquipmentList.get(k).getEquipmentid());
								workTaskEquipment.setTaskid(workOrder.getId());
								workTaskEquipment.setInsuser(cu.getId());
								workTaskEquipment.setInsdt(CommUtil.nowDate());
								this.workTaskEquipmentService.save(workTaskEquipment);
							}
						}
					}
				}
			}
		}
		return result;
	}
}
