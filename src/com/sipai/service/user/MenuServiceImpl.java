package com.sipai.service.user;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.user.MenuDao;
import com.sipai.dao.user.UserPowerDao;
import com.sipai.entity.user.Menu;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.UserPower;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

	@Resource
	private MenuDao menuDao;
	
	
	@Resource
	private UserPowerDao userPowerDao;
	
	@Resource
	private RoleService roleService;
	
	@Override
	public Menu getMenuById(String menuId) {
		return this.menuDao.selectByPrimaryKey(menuId);
	}

	@Override
	public int saveMenu(Menu menu) {
		return this.menuDao.insert(menu);
	}

	@Override
	public int updateMenu(Menu menu) {
		return this.menuDao.updateByPrimaryKeySelective(menu);
	}

	@Override
	public int deleteMenuById(String menuId) {
		return this.menuDao.deleteByPrimaryKey(menuId);
	}

	@Override
	public List<Menu> selectAll() {
		Menu menu= new Menu();
		return this.menuDao.selectList(menu);
	}
	
	@Override
	public List<Menu> selectListByWhere(String where) {
		Menu menu= new Menu();
		menu.setWhere(where);
		return this.menuDao.selectListByWhere(menu);
	}
	
	@Override
	public List<UserPower> selectListByUserId(String userid) {
		return this.userPowerDao.selectListByUserId(userid);
	}
	
	@Override
	public List<UserPower> selectMenuByUserId(String userid) {
		return this.userPowerDao.selectMenuByUserId(userid);
	}
	
	@Override
	public List<UserPower> selectFuncByUserId(String userid) {
		return this.userPowerDao.selectFuncByUserId(userid);
	}
	
	List<UserPower> powerlist= new ArrayList<UserPower>();
	List<Menu> list = new ArrayList<Menu>();
	@Override
	public List<Menu> getFullPower(String userid){
		powerlist = selectListByUserId(userid);
		list=selectAll();
		for(int i=0;i<powerlist.size();i++){
			UserPower obj = powerlist.get(i);
			getParent(obj);
		}
		
		//按照菜单顺序再次排序
		List<Menu> reslist = new ArrayList<Menu>();
		for(Menu menu :list){
			for(UserPower userPower:powerlist){
				if(menu.getId().equals(userPower.getId())){
					reslist.add(menu);
				}
			}
		}
		return reslist;
	}
	
	public void getParent(UserPower obj){
		if(obj!=null){
			UserPower up= new UserPower();
			for(int i=0;i<list.size();i++){
				Menu menu= list.get(i);
				if(obj.getPid()!=null && obj.getPid().equals(menu.getId())){
					up.setId(menu.getId());
					up.setName(menu.getName());
					up.setPid(menu.getPid());
					
					if(check(up.getId())==false){
						powerlist.add(up);
					}
					getParent(up);
				}
			}
		}
	}
	//检查对象是否在列表中
	public boolean check(String id){
		for(int i=0;i<powerlist.size();i++){
			if(powerlist.get(i).getId().equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<Menu> getFinalMenu(){
		List<Menu> list1=this.selectListByWhere("where type='menu'");
		List<Menu> list2=list1;
		if(list1!=null && list1.size()>0){
			for(int i=list1.size()-1 ;i>=0;i--){
				Menu menu1=list1.get(i);
				for(int j=0 ;j<list2.size();j++){
					Menu menu2=list2.get(j);
					if(menu2.getPid()!=null && menu1.getId().equals(menu2.getPid())){
						list1.remove(menu1);
					}
					menu2=null;
				}
				menu1=null;
			}
		}
		list2=null;
		return list1;
	}
	
	@Override
	public List<RoleMenu> getFinalMenuByRoleId(String roleid){
		List<RoleMenu> list = roleService.getRoleMenu(roleid);
		List<Menu> list1 = this.getFinalMenu();
		List<RoleMenu> listres= new ArrayList<RoleMenu>();
		if(list!=null &&list.size()>0){
			for(int i=0;i<list.size();i++){
				RoleMenu roleMenu=list.get(i);
				if(list1!=null &&list1.size()>0){
					for(int j=0;j<list1.size();j++){
						Menu menu = list1.get(j);
						if(roleMenu.getMenuid().equals(menu.getId())){
							listres.add(roleMenu);
						}
						menu=null;
					}
				}
				roleMenu=null;
			}
		}
		return listres;
	}
	
	@Override
	public List<RoleMenu> getFuncByRoleId(String roleid){
		List<RoleMenu> list = roleService.getRoleFunc(roleid);
		return list;
	}
	
	@Override
	public List<Menu> getFuncByMenuId(String roleid){
		Menu menu= new Menu();
		menu.setWhere(" where pid = '"+roleid+"' and type='func' order by morder");
		List<Menu> list = this.menuDao.selectListByWhere(menu);
		return list;
	}

	@Override
	public int deleteByPid(String pid) {
		Menu menu= new Menu();
		menu.setWhere(" where pid='"+pid+"' ");
		return this.menuDao.deleteByWhere(menu);
	}

}
