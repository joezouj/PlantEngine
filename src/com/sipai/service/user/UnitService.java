package com.sipai.service.user;

import java.util.List;

import com.sipai.entity.user.Company;
import com.sipai.entity.user.Dept;
import com.sipai.entity.user.Menu;
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;

public interface UnitService {
	
	public List<Unit> selectList();

	public Unit getUnitById(String id);
	
	/**
	 * 获取简称，如果没有返回全称
	 * @param unit
	 * @return
	 */
	public String getSnameById(String id);

	public Company getCompById(String id);

	public int deleteCompById(String id);

	public int saveComp(Company t);

	public int updateComp(Company t);

	public Dept getDeptById(String id);

	public int deleteDeptById(String id);

	public int saveDept(Dept t);

	public int updateDept(Dept t);

	public List<Unit> selectListByWhere(String wherestr);

	/**
	 * 获得当前节点所有的子元素（包含当前节点）
	 * @param id
	 * @return
	 */
	public List<Unit> getUnitChildrenById(String id);
	public List<User> getChildrenUsersById(String id);

}
