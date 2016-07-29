package com.sipai.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.base.Tree;
import com.sipai.entity.user.Menu;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.User;
import com.sipai.service.user.MenuService;
import com.sipai.service.user.RoleService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/user")
public class MenuController {
	@Resource
	private MenuService menuService;
	
	@Resource
	private RoleService roleService;
	
	@RequestMapping("/showMenuListByCu.do")
	public String showMenuListByCu(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");

		List<Menu> list = this.menuService.getFullPower(cu.getId());
		if(list!=null &&list.size()>0){
			for(int i=list.size()-1;i>=0;i--){
				Menu menu=list.get(i);
				if((menu.getType() !=null && menu.getType().equals("func"))
					|| (menu.getActive() !=null && menu.getActive().equals("禁用"))){
					list.remove(i);
				}
			}
		}

		model.addAttribute("list",list);
		return "west";
	}
	
	@RequestMapping("/showMenuTree.do")
	public String showMenuTree(HttpServletRequest request,Model model){
		return "user/menuTree";
	}
	
	@RequestMapping("/showMenuAdd.do")
	public String doadd(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String pid){
		if(pid!=null && !pid.equals("")){
			Menu menu= menuService.getMenuById(pid);
			model.addAttribute("pname",menu.getName());
		}
		return "user/menuAdd";
	}

	@RequestMapping("/showMenuEdit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		Menu menu= menuService.getMenuById(id);
		if(menuService.getMenuById(menu.getPid())!=null)
		menu.set_pname(menuService.getMenuById(menu.getPid()).getName());
		model.addAttribute("menu",menu );
		return "user/menuEdit";
	}

	@RequestMapping("/saveMenu.do")
	public ModelAndView dosave(HttpServletRequest request,Model model,
			@ModelAttribute("menu") Menu menu){
		menu.setId(CommUtil.getUUID());
		int result = this.menuService.saveMenu(menu);
		model.addAttribute("result","{\"res\":\""+result+"\",\"id\":\""+menu.getId()+"\"}");
		
		return new ModelAndView("result");
	}
	
	@RequestMapping("/saveDefaultFunc.do")
	public ModelAndView dosaveDefaultFunc(HttpServletRequest request,Model model,
			@ModelAttribute("menu") Menu menu){
		int result=0;
		//新增默认的功能：新增、删除、修改、查看全部、查看部门
		String base_action="";
		if(menu.getLocation()!=null && menu.getLocation().indexOf("/")>0){
			base_action = menu.getLocation().substring(0, menu.getLocation().lastIndexOf("/")+1);
		}
		String[][] str={{"新增",base_action+"add.do"},{"删除",base_action+"delete.do"},{"修改",base_action+"edit.do"},
				{"查看部门",base_action+"showlist.do?scope=dept"},{"查看全部",base_action+"showlist.do?scope=all"}};
		for(int i=0;i<str.length;i++){
			Menu menu1= new Menu();
			menu1.setId(CommUtil.getUUID());
			menu1.setPid(menu.getId());
			menu1.setName(str[i][0]);
			menu1.setLocation(str[i][1]);
			menu1.setActive("启用");
			menu1.setType("func");
			menu1.setMorder(i);
			
			result+=this.menuService.saveMenu(menu1);
		}
		model.addAttribute("result",result);
		
		return new ModelAndView("result");
	}

	@RequestMapping("/updateMenu.do")
	public ModelAndView doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("menu") Menu menu){
		int result = this.menuService.updateMenu(menu);
		model.addAttribute("result",result);
		
		return new ModelAndView("result");
	}
	
	@RequestMapping("/deleteMenu.do")
	public ModelAndView dodel(HttpServletRequest request,Model model,
			@ModelAttribute("menu") Menu menu){
		String msg = "";
		int result = 0;
		List<Menu> mlist = this.menuService.selectListByWhere(" where pid = '"+menu.getId()+"' and type='menu' ");
		//判断是否有子菜单，有的话不能删除
		if(mlist!=null && mlist.size()>0){
			msg="请先删除子菜单";
		}else{
			result = this.menuService.deleteMenuById(menu.getId());
			if(result>0){
				//删除菜单下的按钮
				this.menuService.deleteByPid(menu.getId());
				
				//删除菜单对应的权限
				List<Menu> funclist = this.menuService.selectListByWhere(" where pid = '"+menu.getId()+"' and type='func' ");
				funclist.add(menu);
				String menuids="";
				for(int i=0;i<funclist.size();i++){
					menuids += funclist.get(i).getId()+",";
				}
				this.roleService.deleteRoleMenuByMenuId(menuids);
			}else{
				msg="删除失败";
			}
		}
		
		model.addAttribute("result","{\"res\":\""+result+"\",\"msg\":\""+msg+"\"}");
		
		return new ModelAndView("result");
	}

	@RequestMapping("/getMenusJson.do")
	public String getMenusJson(HttpServletRequest request,Model model){
		List<Menu> list = this.menuService.selectListByWhere("where type='menu' order by morder");
		
		List<Tree> tree = new ArrayList<Tree>();
		for (Menu resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			
			node.setText(resource.getName());
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("url", resource.getLocation());
			attributes.put("target", resource.getTarget());
			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/getMenusJsonWithFuncByRoleID.do")
	public String getMenusJsonWithFunc(HttpServletRequest request,Model model,
			@RequestParam String roleid){
		List<Menu> list = this.menuService.selectListByWhere("where type='menu' order by morder");
		List<RoleMenu> list1 = menuService.getFuncByRoleId(roleid);
		
		List<Tree> tree = new ArrayList<Tree>();
		for (Menu resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			
			String str="";
			for(int j=0;j<list1.size();j++){
				if(resource.getId().equals(list1.get(j).getMenu().getPid())){
					str += " ["+list1.get(j).getMenu().getName()+"]";
				}
			}
			node.setText(resource.getName()+str);
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("url", resource.getLocation());
			attributes.put("target", resource.getTarget());
			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
//		System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/getFuncJson.do")
	public String getFuncJson(HttpServletRequest request,Model model,
			@RequestParam(value = "id") String id){
		List<Menu> list = this.menuService.selectListByWhere("where type='func' and pid='"+id+"' order by morder");
		
		JSONArray json = JSONArray.fromObject(list);
		model.addAttribute("result", json);
		return "result";
	}
	
	/**
	 * 根据菜单ID获取功能权限
	 * @param request
	 * @param model
	 * @param menuid
	 * @return
	 */
	@RequestMapping("/getFuncByMenuid.do")
	public String getFuncByMenuid(HttpServletRequest request,Model model,
			@RequestParam(value = "menuid") String menuid){
		List<Menu> menulist = this.menuService.getFuncByMenuId(menuid);
		JSONArray menujson=JSONArray.fromObject(menulist);
		model.addAttribute("result","{\"total\":\""+menulist.size()+"\",\"rows\":"+menujson+"}");

		return "result";
	}
	
	@RequestMapping("/getMenusJsonActive.do")
	public String getMenusJsonActive(HttpServletRequest request,Model model){
		List<Menu> list = this.menuService.selectListByWhere("where active='启用' and type='menu' order by morder");
		
		List<Tree> tree = new ArrayList<Tree>();
		for (Menu resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			
			node.setText(resource.getName());
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("url", resource.getLocation());
			attributes.put("target", resource.getTarget());
			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
//		System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/saveFunc.do")
	public String dosaveFunc(HttpServletRequest request,Model model,
			@ModelAttribute Menu menu){
		menu.setId(CommUtil.getUUID());
		menu.setType("func");
		int result = this.menuService.saveMenu(menu);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+menu.getId()+"\"}");
		return "result";
	}
	
	@RequestMapping("/updateFunc.do")
	public String doupdateFunc(HttpServletRequest request,Model model,
			@ModelAttribute Menu menu){
		int result = this.menuService.updateMenu(menu);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+menu.getId()+"\"}");
		return "result";
	}
	
}
