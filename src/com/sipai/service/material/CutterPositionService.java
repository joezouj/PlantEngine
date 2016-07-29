package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.CutterPositionDao;
import com.sipai.entity.material.CutterPosition;
import com.sipai.tools.CommService;
@Service
public class CutterPositionService implements CommService<CutterPosition> {
	@Resource
	private CutterPositionDao cutterPositionDao;
	@Override
	public CutterPosition selectById(String id) {
		return cutterPositionDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return cutterPositionDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(CutterPosition cutterPosition) {
		return cutterPositionDao.insert(cutterPosition);
	}

	@Override
	public int update(CutterPosition cutterPosition) {
		return cutterPositionDao.updateByPrimaryKeySelective(cutterPosition);
	}

	@Override
	public List<CutterPosition> selectListByWhere(String wherestr) {
		CutterPosition cutterPosition = new CutterPosition();
		cutterPosition.setWhere(wherestr);
		return cutterPositionDao.selectListByWhere(cutterPosition);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		CutterPosition cutterPosition = new CutterPosition();
		cutterPosition.setWhere(wherestr);
		return cutterPositionDao.deleteByWhere(cutterPosition);
	}
	
	/**
	 * name是否已存在
	 * @param id,name
	 * @return
	 */
	public boolean checkNotOccupied(String id,String name) {
		List<CutterPosition> list = this.cutterPositionDao.getListByName(name);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(CutterPosition cutterPosition :list){
					if(!id.equals(cutterPosition.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

}
