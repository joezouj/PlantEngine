package com.sipai.service.user;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.user.CompanyDao;
import com.sipai.dao.user.DeptDao;
import com.sipai.dao.user.UnitDao;
import com.sipai.entity.user.Company;
import com.sipai.entity.user.Dept;
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;

@Service
public class UnitServiceImpl implements UnitService {

	@Resource
	private UnitDao unitDao;
	@Resource
	private CompanyDao compDao;
	@Resource
	private DeptDao deptDao;
	@Resource
	private UserService userService;
	@Override
	public List<Unit> selectList() {
		Unit unit= new Unit();
		return this.unitDao.selectList(unit);
	}
	
	@Override
	public Unit getUnitById(String id) {
		return this.unitDao.getUnitById(id);
	}
	
	@Override
	public String getSnameById(String id) {
		String res="";
		Unit unit=this.getUnitById(id);
		if(unit !=null){
			if(unit.getSname()==null){
				res = unit.getName();
			}else{
				if(unit.getSname().trim().equals("")){
					res = unit.getName();
				}else{
					res = unit.getSname();
				}
			}
		}
		return res;
	}
	
	@Override
	public Company getCompById(String id) {
		return this.compDao.selectByPrimaryKey(id);
	}
	
	@Override
	public int deleteCompById(String id) {
		return this.compDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public int saveComp(Company t) {
		return this.compDao.insert(t);
	}
	
	@Override
	public int updateComp(Company t) {
		return this.compDao.updateByPrimaryKeySelective(t);
	}
	
	@Override
	public Dept getDeptById(String id) {
		return this.deptDao.selectByPrimaryKey(id);
	}
	
	@Override
	public int deleteDeptById(String id) {
		return this.deptDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public int saveDept(Dept t) {
		return this.deptDao.insert(t);
	}
	
	@Override
	public int updateDept(Dept t) {
		return this.deptDao.updateByPrimaryKeySelective(t);
	}
	@Override
	public List<Unit> selectListByWhere(String wherestr) {
		Unit unit= new Unit();
		unit.setWhere(wherestr);
		return this.unitDao.selectListByWhere(unit);
	}
	
	
	
	
	
	
	List<Unit> t = new ArrayList<Unit>();
	@Override
	public List<Unit> getUnitChildrenById(String id){
		t=selectList();
		List<Unit> listRes = getChildren(id);
		listRes.add(getUnitById(id));
		return listRes;
	}
	
	/**
	 * 递归获取子节点
	 * @param list
	 */
	public List<Unit> getChildren(String pid){
		List<Unit> t2 = new ArrayList<Unit>();
		for(int i=0;i<t.size();i++){
			if(t.get(i).getPid()!=null && t.get(i).getPid().equalsIgnoreCase(pid)){
//				t.get(i).setName(t.get(i).getName());
				t2.add(t.get(i));
				if(getChildren(t.get(i).getId())!=null){
					t2.addAll(getChildren(t.get(i).getId()));
				}
			}
		}
		return t2;
	}
	/**
	 * 获取子节点下所有用户user
	 * @param list
	 */
	public List<User> getChildrenUsersById(String pid){
		t=selectList();
		List<Unit> unitlist = getChildren(pid);		
		String pidstr="";
		String wherestr=" where 1=1 ";
		String orderstr=" order by name asc ";
		for(int i=0;i<unitlist.size();i++){
			pidstr += "'"+unitlist.get(i).getId()+"',";
		}
		List<User> listRes=new ArrayList();
		if(!pidstr.equals("")){
			pidstr = pidstr.substring(0, pidstr.length()-1);
			wherestr += "and pid in ("+pidstr+") ";
			listRes = this.userService.selectListByWhere(wherestr+orderstr);
		}		
		return listRes;
	}

}
