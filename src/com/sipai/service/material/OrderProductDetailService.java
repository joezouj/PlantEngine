package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.OrderProductDetailDao;
import com.sipai.entity.material.OrderProductDetail;
import com.sipai.tools.CommService;
@Service
public class OrderProductDetailService implements CommService<OrderProductDetail>{
	@Resource
	private OrderProductDetailDao orderproductdetailDao;
	

	public List<OrderProductDetail> selectList() {
		// TODO Auto-generated method stub
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		return this.orderproductdetailDao.selectList(orderproductdetail);
	}


	@Override
	public OrderProductDetail selectById(String id) {
		return this.orderproductdetailDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.orderproductdetailDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(OrderProductDetail entity) {
		return this.orderproductdetailDao.insert(entity);
	}

	@Override
	public int update(OrderProductDetail t) {
		return this.orderproductdetailDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<OrderProductDetail> selectListByWhere(String wherestr) {
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		orderproductdetail.setWhere(wherestr);
		return this.orderproductdetailDao.selectListByWhere(orderproductdetail);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		orderproductdetail.setWhere(wherestr);		
		return this.orderproductdetailDao.deleteByWhere(orderproductdetail);
	}
	//setandwherestr 需含：数据表名 set .. where..
	public int updateBySetAndWhere(String setandwherestr){
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		orderproductdetail.setWhere(setandwherestr);		
		return this.orderproductdetailDao.updateBySetAndWhere(orderproductdetail);
	}
	public List<OrderProductDetail> getOrderProductDetail(String wherestr){
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		orderproductdetail.setWhere(wherestr);		
		return this.orderproductdetailDao.getOrderProductDetail(orderproductdetail);
	}
	public List<OrderProductDetail> selectTopNByWhere(String n,String wherestr) {
		return this.orderproductdetailDao.selectTopNByWhere(n,wherestr);
	}
	/*更改产品完成状态--完成：status=1
	 * 参数id:产品明细的id,
	 * 返回值:1表示成功
	*/
	public int productdetailfinish(String id) {
		OrderProductDetail orderproductdetail = new OrderProductDetail();
		orderproductdetail.setId(id);	
		orderproductdetail.setStatus("1");		
		return this.orderproductdetailDao.updateByPrimaryKeySelective(orderproductdetail);
	}
}
