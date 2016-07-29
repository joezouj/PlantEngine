package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.DeliverDetailDao;
import com.sipai.entity.plan.DeliverDetail;
import com.sipai.tools.CommService;
@Service
public class DeliverDetailService implements CommService<DeliverDetail>{
	@Resource
	private DeliverDetailDao deliverDetailDao;

	public List<DeliverDetail> selectList() {
		DeliverDetail deliverDetail = new DeliverDetail();
		return this.deliverDetailDao.selectList(deliverDetail);
	}

	@Override
	public DeliverDetail selectById(String id) {
		return this.deliverDetailDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.deliverDetailDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(DeliverDetail entity) {
		return this.deliverDetailDao.insert(entity);
	}

	@Override
	public int update(DeliverDetail t) {
		return this.deliverDetailDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<DeliverDetail> selectListByWhere(String wherestr) {
		DeliverDetail deliverDetail = new DeliverDetail();
		deliverDetail.setWhere(wherestr);
		return this.deliverDetailDao.selectListByWhere(deliverDetail);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		DeliverDetail deliverDetail = new DeliverDetail();
		deliverDetail.setWhere(wherestr);		
		return this.deliverDetailDao.deleteByWhere(deliverDetail);
	}
	
	public List<DeliverDetail> getDeliverDetail(String wherestr) {
		DeliverDetail deliverDetail=new DeliverDetail();
		deliverDetail.setWhere(wherestr);		
		return this.deliverDetailDao.getMaterialRecover(deliverDetail);
	}

	public DeliverDetail getPlanedAmount(String planid, String materialid) {
		String wherestr=" where deliver.dailyplanid='"+planid+"' and detail.materialid='"+materialid+"' ";
		DeliverDetail deliverDetail=new DeliverDetail();
		deliverDetail.setWhere(wherestr);
		return this.deliverDetailDao.getPlanedAmount(deliverDetail);
	}
	
	public String getMaterialDeliverStatus(String pid) {
		String wherestr=" where pid='"+pid+"' ";
		DeliverDetail deliverDetail=new DeliverDetail();
		deliverDetail.setWhere(wherestr);
		return this.deliverDetailDao.getMaterialDeliverStatus(deliverDetail);
	}
}
