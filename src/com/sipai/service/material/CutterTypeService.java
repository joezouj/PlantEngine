package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.CutterTypeDao;
import com.sipai.entity.material.CutterType;
import com.sipai.tools.CommService;
@Service
public class CutterTypeService implements CommService<CutterType> {
	@Resource
	private CutterTypeDao cutterTypeDao;
	@Override
	public CutterType selectById(String id) {
		return cutterTypeDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return cutterTypeDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(CutterType cutterType) {
		return cutterTypeDao.insert(cutterType);
	}

	@Override
	public int update(CutterType cutterType) {
		return cutterTypeDao.updateByPrimaryKeySelective(cutterType);
	}

	@Override
	public List<CutterType> selectListByWhere(String wherestr) {
		CutterType cutterType = new CutterType();
		cutterType.setWhere(wherestr);
		return cutterTypeDao.selectListByWhere(cutterType);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		CutterType cutterType = new CutterType();
		cutterType.setWhere(wherestr);
		return cutterTypeDao.deleteByWhere(cutterType);
	}
	
	/**
	 * typename是否已存在
	 * @param id,typename
	 * @return
	 */
	public boolean checkNotOccupied(String id,String typename) {
		List<CutterType> list = this.cutterTypeDao.getListByTypename(typename);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(CutterType cutterType :list){
					if(!id.equals(cutterType.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

}
