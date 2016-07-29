package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.OrderProductDetailconnectDao;
import com.sipai.entity.material.OrderProductDetailconnect;
import com.sipai.tools.CommService;
@Service
public class OrderProductDetailconnectService implements CommService<OrderProductDetailconnect>{
	@Resource
	private OrderProductDetailconnectDao orderproductdetailconnectDao;
	

	public List<OrderProductDetailconnect> selectList() {
		// TODO Auto-generated method stub
		OrderProductDetailconnect orderproductdetailconnect = new OrderProductDetailconnect();
		return this.orderproductdetailconnectDao.selectList(orderproductdetailconnect);
	}


	@Override
	public OrderProductDetailconnect selectById(String id) {
		return this.orderproductdetailconnectDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.orderproductdetailconnectDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(OrderProductDetailconnect entity) {
		return this.orderproductdetailconnectDao.insert(entity);
	}

	@Override
	public int update(OrderProductDetailconnect t) {
		return this.orderproductdetailconnectDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<OrderProductDetailconnect> selectListByWhere(String wherestr) {
		OrderProductDetailconnect orderproductdetailconnect = new OrderProductDetailconnect();
		orderproductdetailconnect.setWhere(wherestr);
		return this.orderproductdetailconnectDao.selectListByWhere(orderproductdetailconnect);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		OrderProductDetailconnect orderproductdetailconnect = new OrderProductDetailconnect();
		orderproductdetailconnect.setWhere(wherestr);		
		return this.orderproductdetailconnectDao.deleteByWhere(orderproductdetailconnect);
	}	
}
