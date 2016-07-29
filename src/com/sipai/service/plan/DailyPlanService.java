package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.DailyPlanDao;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.work.WorkOrder;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.tools.CommService;

@Service("dailyplanService")
public class DailyPlanService implements CommService<DailyPlan>{
	@Resource
	private DailyPlanDao dailyPlanDao;
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Resource
	private WorkOrderService workOrderService;
	@Override
	public DailyPlan selectById(String id) {
		return dailyPlanDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return dailyPlanDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(DailyPlan dailyPlan) {
		List<DailyPlan> listdup= this.selectListByWhere(" where productionOrderNo='"+dailyPlan.getProductionorderno()+"'  and id!='"+dailyPlan.getId()+"' order by insdt");
		if(!listdup.isEmpty()){
			 return 2;
		}else{
			return dailyPlanDao.insert(dailyPlan);
		}		
	}

	@Override
	public int update(DailyPlan dailyPlan) {
		if(dailyPlan.getProductionorderno()!=null&&!dailyPlan.getProductionorderno().equals("")){
			//判断单号是否重复
			List<DailyPlan> listdup= this.selectListByWhere(" where productionOrderNo='"+dailyPlan.getProductionorderno()+"' and id!='"+dailyPlan.getId()+"' order by insdt");
			if(!listdup.isEmpty()){//重复
				return 2;
			}else{
				return dailyPlanDao.updateByPrimaryKeySelective(dailyPlan);
			}
		}else{
			return dailyPlanDao.updateByPrimaryKeySelective(dailyPlan);
		}
		
	}

	@Override
	public List<DailyPlan> selectListByWhere(String wherestr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(wherestr);
		return dailyPlanDao.selectListByWhere(dailyPlan);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(wherestr);
		return dailyPlanDao.deleteByWhere(dailyPlan);
	}
	public List<DailyPlan> getDailyPlanlist(String wherestr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(wherestr);
		return dailyPlanDao.getDailyPlanlist(dailyPlan);	
	}
	public List<DailyPlan> getDailyPlan(String wherestr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(wherestr);
		return dailyPlanDao.getDailyPlan(dailyPlan);	
	}
	public List<SalesOrderProduct> getSOPByPlan(String wherestr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(wherestr);
		return dailyPlanDao.getSOPByPlan(dailyPlan);	
	}
	public int updateBySetAndWhere(String setandwhere) {		
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setWhere(setandwhere);
		return this.dailyPlanDao.updateBySetAndWhere(dailyPlan);
	}
	//计划作废
	public int invalidate(String id) {		
		String setandwhere=" set status='3' where id='"+id+"'";
		this.recoverproductdetail(id);
		//任务作废:status=0
		DailyPlan dailyPlan=this.selectById(id);	
		List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+dailyPlan.getProductionorderno()+"'");
		for(int w=0;w<wtlist.size();w++){
			WorkOrder workorder=wtlist.get(w);
			workorder.setStatus("0");
			this.workOrderService.update(workorder);
		}
		return this.updateBySetAndWhere(setandwhere);
	}
	//计划作废后恢复产品明细的计划状态
	public int recoverproductdetail(String id) {
		DailyPlan dailyPlan=this.selectById(id);
		String setandwhere="  TB_material_OrderProductDetail "
				+ "set productionOrderNo=NULL,processrealid=NULL "
				+ "where pid='"+dailyPlan.getSalesorderid()+"' and productionOrderNo='"+dailyPlan.getProductionorderno()+"'  and (status is null or status!='1')";
		return this.orderProductDetailService.updateBySetAndWhere(setandwhere);
	}
	public DailyPlan selectValueBySql(String sqlstr) {
		DailyPlan dailyPlan = new DailyPlan();
		dailyPlan.setSql(sqlstr);
		return dailyPlanDao.selectValueBySql(dailyPlan);	
	}
	
}
