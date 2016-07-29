package com.sipai.service.work;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.GroupTypeDao;
import com.sipai.dao.work.WorkSchedulingDao;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.GroupType;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.tools.CommService;

@Service
public class GroupTypeService implements CommService<GroupType>{

	@Resource
	private GroupTypeDao groupTypeDao;
	
	@Override
	public GroupType selectById(String id) {
		return groupTypeDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return groupTypeDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(GroupType entity) {
		return groupTypeDao.insert(entity);
	}

	@Override
	public int update(GroupType t) {
		return groupTypeDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<GroupType> selectListByWhere(String wherestr) {
		GroupType grouptype = new GroupType();
		grouptype.setWhere(wherestr);
		return groupTypeDao.selectListByWhere(grouptype);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		GroupType grouptype = new GroupType();
		grouptype.setWhere(wherestr);
		return groupTypeDao.deleteByWhere(grouptype);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<GroupType> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(GroupType grouptype :list){
					if(!id.equals(grouptype.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
