package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.SalesOrderProductDao;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.work.WorkOrder;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.tools.CommService;
@Service
public class SalesOrderProductService implements CommService<SalesOrderProduct>{
	@Resource
	private SalesOrderProductDao salesorderproductDao;
	@Resource
	private DailyPlanService dailyPlanService;
	

	public List<SalesOrderProduct> selectList() {
		// TODO Auto-generated method stub
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		return this.salesorderproductDao.selectList(salesorderproduct);
	}


	@Override
	public SalesOrderProduct selectById(String id) {
		return this.salesorderproductDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.salesorderproductDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(SalesOrderProduct entity) {
		return this.salesorderproductDao.insert(entity);
	}

	@Override
	public int update(SalesOrderProduct t) {
		return this.salesorderproductDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<SalesOrderProduct> selectListByWhere(String wherestr) {
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		salesorderproduct.setWhere(wherestr);
		return this.salesorderproductDao.selectListByWhere(salesorderproduct);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		salesorderproduct.setWhere(wherestr);		
		return this.salesorderproductDao.deleteByWhere(salesorderproduct);
	}
	public List<SalesOrderProduct> getSalesOrderProductlist(String wherestr) {
		// TODO Auto-generated method stub
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		salesorderproduct.setWhere(wherestr);
		return this.salesorderproductDao.getSalesOrderProductlist(salesorderproduct);
	}
	//getSalesOrderProduct与getSalesOrderProductlist区别在于前者包含产品信息便于统计产品完成、计划数量，后者为了列表显示记录数量准确
	public List<SalesOrderProduct> getSalesOrderProduct(String wherestr) {
		// TODO Auto-generated method stub
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		salesorderproduct.setWhere(wherestr);
		return this.salesorderproductDao.getSalesOrderProduct(salesorderproduct);
	}
	public List<SalesOrderProduct> getDistinctOrderByWhere(String wherestr) {
		SalesOrderProduct salesorderproduct = new SalesOrderProduct();
		salesorderproduct.setWhere(wherestr);
		return this.salesorderproductDao.selectDistinctOrderByWhere(salesorderproduct);
	}
	//订单作废
		public int invalidate(String id) {				
			SalesOrderProduct sop=new SalesOrderProduct();
			sop.setId(id);
			sop.setStatus("2");
			int result=this.salesorderproductDao.updateByPrimaryKeySelective(sop);
			if(result==1){
				return this.dailyPlanService.updateBySetAndWhere(" set status='3' where salesorderid="+id);//仅作废关联的计划，工单暂不处理
			}else{
				return result;
			}			
		}
}
