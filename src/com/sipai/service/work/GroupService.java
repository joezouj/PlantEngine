package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.GroupDao;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.Workstation;
import com.sipai.tools.CommService;

@Service
public class GroupService implements CommService<Group>{
	@Resource
	private GroupDao groupDao;
	
	@Override
	public Group selectById(String id) {
		return groupDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return groupDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Group entity) {
		return groupDao.insert(entity);
	}

	@Override
	public int update(Group entity) {
		return groupDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<Group> selectListByWhere(String wherestr) {
		Group group = new Group();
		group.setWhere(wherestr);
		return groupDao.selectListByWhere(group);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		Group group = new Group();
		group.setWhere(wherestr);
		return groupDao.deleteByWhere(group);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<Group> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Group group :list){
					if(!id.equals(group.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
