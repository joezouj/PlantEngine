package com.sipai.controller.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.user.Role;
import com.sipai.entity.user.RoleMenu;
import com.sipai.entity.user.UserRole;
import com.sipai.service.user.MenuService;
import com.sipai.service.user.RoleService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/user")
public class RoleController {
	@Resource
	private RoleService roleService;
	
	@Resource
	private MenuService menuService;
	
	@RequestMapping("/showListRole.do")
	public String showlist(HttpServletRequest request,Model model){
		return "user/roleList";
	}
	
	@RequestMapping("/getListRole.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		if(sort==null){
			sort = " morder ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=" where 1=1 ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<Role> list = this.roleService.getList(wherestr+orderstr);
        
        PageInfo<Role> pi = new PageInfo<Role>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonRole.do")
	public ModelAndView getlist(HttpServletRequest request,Model model) {
		String orderstr=" order by morder asc ";
		String wherestr=" where 1=1 ";
        List<Role> list = this.roleService.getList(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
//		System.out.println(json);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/saveRole.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("role") Role role){
		if(!this.roleService.checkNotOccupied(role.getId(), role.getName())){
			model.addAttribute("result", "{\"res\":\"权限名称重复\"}");
			return "result";
		}
		role.setId(CommUtil.getUUID());
		int result = this.roleService.dosave(role);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+role.getId()+"\"}");
		return "result";
	}
	
	@RequestMapping("/updateRole.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("role") Role role){
		if(!this.roleService.checkNotOccupied(role.getId(), role.getName())){
			model.addAttribute("result", "{\"res\":\"权限名称重复\"}");
			return "result";
		}
		int result = this.roleService.doupdate(role);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+role.getId()+"\"}");
		return "result";
	}
	
	@RequestMapping("/deleteRole.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.roleService.dodel(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showRoleMenu.do")
	public String showRoleMenu(HttpServletRequest request,Model model,
			@RequestParam(value="roleid") String roleid){
		List<RoleMenu> list = menuService.getFinalMenuByRoleId(roleid);
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("json",json);
		
		model.addAttribute("roleid",roleid);
		return "user/roleMenu";
	}
	
	@RequestMapping("/updateRoleMenu.do")
	public String updateRoleMenu(HttpServletRequest request,Model model,
			@RequestParam("menustr") String menustr,@RequestParam("roleid") String roleid){
		int result = this.roleService.updateRoleMenu(roleid, menustr);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showRoleUser.do")
	public String showRoleUser(HttpServletRequest request,Model model,
			@RequestParam(value="roleid") String roleid){
		List<UserRole> list = this.roleService.getUserRole(roleid);
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("json",json);
		model.addAttribute("roleid",roleid);
		return "user/roleUser";
	}
	
	@RequestMapping("/updateUserRole.do")
	public String updateUserRole(HttpServletRequest request,Model model,
			@RequestParam("userstr") String userstr,@RequestParam("roleid") String roleid){
		int result = this.roleService.updateUserRole(roleid, userstr);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/updateRoleUser.do")
	public String updateRoleUser(HttpServletRequest request,Model model,
			@RequestParam("rolestr") String rolestr,@RequestParam("userid") String userid){
		int result = this.roleService.updateRoleUser(userid,rolestr);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/roleForSelect.do")
	public String roleforselect(HttpServletRequest request,Model model){
		if(request.getParameter("roleids")!=null){
			model.addAttribute("roleids",request.getParameter("roleids"));
		}
		return "user/roleForSelect";
	}
	@RequestMapping("/getRolesForSelect.do")
	public ModelAndView getrolesforselect(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		if(sort==null){
			sort = " morder ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=" where 1=1 ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<Role> list = this.roleService.getList(wherestr+orderstr);
        
        PageInfo<Role> pi = new PageInfo<Role>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/roleFuncForSelect.do")
	public String roleFuncForSelect(HttpServletRequest request,Model model,
			@RequestParam(value="roleid") String roleid){
		List<RoleMenu> list = this.roleService.getRoleFunc(roleid);
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("json",json);
		
		return "user/roleFuncForSelect";
	}
	
	@RequestMapping("/updateFuncByRoleMenu.do")
	public String updateFuncByRoleMenu(HttpServletRequest request,Model model,
			@RequestParam(value="menuid") String menuid,@RequestParam(value="roleid") String roleid,
			@RequestParam(value="funcstr") String funcstr){
		int result = this.roleService.updateFuncByRoleMenu(roleid, menuid, funcstr);
		model.addAttribute("result",result);
		return "result";
	}
	
}
