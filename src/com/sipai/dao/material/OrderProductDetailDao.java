package com.sipai.dao.material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.OrderProductDetail;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.msg.Msg;
@Repository
public class OrderProductDetailDao extends CommDaoImpl<OrderProductDetail>{
	public OrderProductDetailDao() {
		super();
		this.setMappernamespace("material.OrderProductDetailMapper");
	}
	public int updateBySetAndWhere(OrderProductDetail orderProductDetail){
		return getSqlSession().update("material.OrderProductDetailMapper.updateBySetAndWhere", orderProductDetail);
	}
	public List<OrderProductDetail> getOrderProductDetail(OrderProductDetail orderProductDetail) {
		List<OrderProductDetail> list = this.getSqlSession().selectList("material.OrderProductDetailMapper.getOrderProductDetail", orderProductDetail);
		return list;
	}
	public List<OrderProductDetail> selectTopNByWhere(String n,String wherestr) {
		Map<String, Object> param=new HashMap<String, Object>();  
		param.put("numN", n);
		param.put("wherestr", wherestr);
		List<OrderProductDetail> list = this.getSqlSession().selectList("material.OrderProductDetailMapper.selectTopNByWhere", param);
		return list;
	}
}
