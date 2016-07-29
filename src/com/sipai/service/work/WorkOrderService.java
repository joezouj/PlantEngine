package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Task;
import org.springframework.stereotype.Service;

import com.sipai.dao.work.WorkOrderDao;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkOrder;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;

@Service
public class WorkOrderService implements CommService<WorkOrder>{

	@Resource
	private WorkOrderDao workOrderDao;
	
	@Resource
	private SnakerEngineFacets facets;
	@Resource
	private WorkTaskWorkStationService workTaskWorkStationService;
	@Resource
	private DailyPlanService dailyPlanService;
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Override
	public WorkOrder selectById(String id) {
		return workOrderDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return workOrderDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(WorkOrder entity) {
		return workOrderDao.insert(entity);
	}

	@Override
	public int update(WorkOrder t) {
		return workOrderDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<WorkOrder> selectListByWhere(String wherestr) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return workOrderDao.selectListByWhere(workOrder);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return workOrderDao.deleteByWhere(workOrder);
	}
	
	public List<WorkOrder> getNewTask(String wherestr) {
		// TODO Auto-generated method stub
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return this.workOrderDao.getNewTask(workOrder);
	}
	public List<WorkOrder> selecttaskListByWhere(String wherestr) {
		// TODO Auto-generated method stub
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return this.workOrderDao.selecttaskListByWhere(workOrder);
	}
	
	public List<WorkOrder> selectActiveTasks(String wherestr) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return this.workOrderDao.selectActiveTasks(workOrder);
	}
	
	public List<WorkOrder> selectDetailListByWhere(String wherestr) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setWhere(wherestr);
		return workOrderDao.selectDetailListByWhere(workOrder);
	}
	
	/**
	 * 更新状态
	 * @param wftaskid
	 * @param pstatus
	 * @return
	 */
	public int updateStatusByWftaskid(String wftaskid, String pstatus){
		String orderId="";
		Task task = facets.getEngine().query().getTask(wftaskid);
		if(task != null){
			orderId = task.getOrderId();
		}
		HistoryTask historyTask = facets.getEngine().query().getHistTask(wftaskid);
		if(historyTask != null){
			orderId = historyTask.getOrderId();
		}
		
		List<WorkOrder> orderList = this.selectListByWhere("where wforderid = '"+orderId+"' ");
		if(orderList!=null && orderList.size()>0){
			WorkOrder order= orderList.get(0);
			order.setPstatus(pstatus);
			if(pstatus.equals("已完成")){
				order.setStatus("2");
			}
			return this.workOrderDao.updateByPrimaryKeySelective(order);
		}else{
			return 0;
		}
		
	}
	/**
	 * 计划下发工单addbyplan
	 * @param nos是计划的productionOrderno，格式  "no1"+"','"
	 * @param cu
	 * @return
	 */
	public String addbyplan(User cu,String nos){
		List<WorkOrder> list = this.getNewTask("where c.productionOrderno in ('"+nos+"')");//计划表内的所有该下发的任务
		for(int i=0;i<list.size();i++){
			WorkOrder workorder=list.get(i);
			workorder.setId(CommUtil.getUUID());//getNewTask的结果是产品id作为其workorderid,此处更换为uuid【160325】
			this.save(workorder);//任务id与TB_material_OrderProductDetail的id不一样
			this.workTaskWorkStationService.saveDefault(workorder,cu,i);//添加默认的工位以及对应的物料、设备
			
			this.orderProductDetailService.updateBySetAndWhere(
					" TB_material_OrderProductDetail set workorderId='"+workorder.getId()+"'  where productUid='"+workorder.getProductuid()+"'");
				
			DailyPlan dailyPlan= dailyPlanService.selectById(workorder.getPlanid());
			dailyPlan.setStatus("2");
			dailyPlan.setTaskchangedstatus("true");
			dailyPlanService.update(dailyPlan);
		}
		if(list!=null &&list.size()>0){
			return "1";
		}else{
			return "0";
		}
	}
}
