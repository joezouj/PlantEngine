package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.DeliverDetailDao;
import com.sipai.dao.plan.DeliverProcessorDao;
import com.sipai.entity.plan.DeliverDetail;
import com.sipai.entity.plan.DeliverProcessor;
import com.sipai.tools.CommService;
@Service
public class DeliverProcessorService implements CommService<DeliverProcessor>{
	@Resource
	private DeliverProcessorDao deliverProcessorDao;

	public List<DeliverProcessor> selectList() {
		DeliverProcessor deliverProcessor = new DeliverProcessor();
		return this.deliverProcessorDao.selectList(deliverProcessor);
	}

	@Override
	public DeliverProcessor selectById(String id) {
		return this.deliverProcessorDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.deliverProcessorDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(DeliverProcessor entity) {
		return this.deliverProcessorDao.insert(entity);
	}

	@Override
	public int update(DeliverProcessor t) {
		return this.deliverProcessorDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<DeliverProcessor> selectListByWhere(String wherestr) {
		DeliverProcessor deliverProcessor = new DeliverProcessor();
		deliverProcessor.setWhere(wherestr);
		return this.deliverProcessorDao.selectListByWhere(deliverProcessor);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		DeliverProcessor deliverProcessor = new DeliverProcessor();
		deliverProcessor.setWhere(wherestr);		
		return this.deliverProcessorDao.deleteByWhere(deliverProcessor);
	}
	
	public List<DeliverProcessor> getDeliverProcessor(String wherestr) {
		DeliverProcessor deliverProcessor=new DeliverProcessor();
		deliverProcessor.setWhere(wherestr);		
		return this.deliverProcessorDao.getDeliverProcessor(deliverProcessor);
	}
}
