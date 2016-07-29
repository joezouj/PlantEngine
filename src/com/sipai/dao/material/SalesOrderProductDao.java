package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialType;
import com.sipai.entity.material.SalesOrderProduct;
@Repository
public class SalesOrderProductDao extends CommDaoImpl<SalesOrderProduct>{
	public SalesOrderProductDao() {
		super();
		this.setMappernamespace("material.SalesOrderProductMapper");
	}
	public List<SalesOrderProduct> getSalesOrderProductlist(SalesOrderProduct salesorderproduct) {
		List<SalesOrderProduct> list = this.getSqlSession().selectList("material.SalesOrderProductMapper.getSalesOrderProductlist", salesorderproduct);
		return list;
	}
	public List<SalesOrderProduct> getSalesOrderProduct(SalesOrderProduct salesorderproduct) {
		List<SalesOrderProduct> list = this.getSqlSession().selectList("material.SalesOrderProductMapper.getSalesOrderProduct", salesorderproduct);
		return list;
	}
	public List<SalesOrderProduct> selectDistinctOrderByWhere(SalesOrderProduct salesorderproduct) {
		List<SalesOrderProduct> list = this.getSqlSession().selectList("material.SalesOrderProductMapper.selectDistinctOrderByWhere", salesorderproduct);
		return list;
	}
}
