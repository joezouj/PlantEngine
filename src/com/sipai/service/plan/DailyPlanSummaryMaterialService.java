package com.sipai.service.plan;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.plan.DailyPlanSummaryMaterialDao;
import com.sipai.entity.plan.DailyPlanSummaryMaterial;
import com.sipai.tools.CommService;
@Service
public class DailyPlanSummaryMaterialService implements CommService<DailyPlanSummaryMaterial>{
	@Resource
	private DailyPlanSummaryMaterialDao dailyplansummarymaterialDao;
	

	public List<DailyPlanSummaryMaterial> selectList() {
		// TODO Auto-generated method stub
		DailyPlanSummaryMaterial dailyplansummarymaterial = new DailyPlanSummaryMaterial();
		return this.dailyplansummarymaterialDao.selectList(dailyplansummarymaterial);
	}


	@Override
	public DailyPlanSummaryMaterial selectById(String id) {
		return this.dailyplansummarymaterialDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.dailyplansummarymaterialDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(DailyPlanSummaryMaterial entity) {
		return this.dailyplansummarymaterialDao.insert(entity);
	}

	@Override
	public int update(DailyPlanSummaryMaterial t) {
		return this.dailyplansummarymaterialDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<DailyPlanSummaryMaterial> selectListByWhere(String wherestr) {
		DailyPlanSummaryMaterial dailyplansummarymaterial = new DailyPlanSummaryMaterial();
		dailyplansummarymaterial.setWhere(wherestr);
		return this.dailyplansummarymaterialDao.selectListByWhere(dailyplansummarymaterial);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		DailyPlanSummaryMaterial dailyplansummarymaterial = new DailyPlanSummaryMaterial();
		dailyplansummarymaterial.setWhere(wherestr);		
		return this.dailyplansummarymaterialDao.deleteByWhere(dailyplansummarymaterial);
	}	
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<DailyPlanSummaryMaterial> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(DailyPlanSummaryMaterial dailyplansummarymaterial :list){
					if(!id.equals(dailyplansummarymaterial.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
