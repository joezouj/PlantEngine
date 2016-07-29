package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.DailyPlanSummaryDao;
import com.sipai.entity.equipment.GeographyArea;
import com.sipai.entity.plan.DailyPlanSummary;
import com.sipai.tools.CommService;
@Service
public class DailyPlanSummaryService implements CommService<DailyPlanSummary>{
	@Resource
	private DailyPlanSummaryDao dailyplansummaryDao;
	

	public List<DailyPlanSummary> selectList() {
		// TODO Auto-generated method stub
		DailyPlanSummary dailyplansummary = new DailyPlanSummary();
		return this.dailyplansummaryDao.selectList(dailyplansummary);
	}


	@Override
	public DailyPlanSummary selectById(String id) {
		return this.dailyplansummaryDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.dailyplansummaryDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(DailyPlanSummary entity) {
		return this.dailyplansummaryDao.insert(entity);
	}

	@Override
	public int update(DailyPlanSummary t) {
		return this.dailyplansummaryDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<DailyPlanSummary> selectListByWhere(String wherestr) {
		DailyPlanSummary dailyplansummary = new DailyPlanSummary();
		dailyplansummary.setWhere(wherestr);
		return this.dailyplansummaryDao.selectListByWhere(dailyplansummary);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		DailyPlanSummary dailyplansummary = new DailyPlanSummary();
		dailyplansummary.setWhere(wherestr);		
		return this.dailyplansummaryDao.deleteByWhere(dailyplansummary);
	}	
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<DailyPlanSummary> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(DailyPlanSummary dailyplansummary :list){
					if(!id.equals(dailyplansummary.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
