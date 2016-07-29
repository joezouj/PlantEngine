package com.sipai.controller.equipment;

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
import com.sipai.entity.equipment.EquipmentClass;
import com.sipai.entity.user.User;
import com.sipai.service.equipment.EquipmentClassService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/equipment")
public class EquipmentClassController {
	@Resource
	private EquipmentClassService equipmentclassService;
	@RequestMapping("/showEquipmentClass.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/equipment/equipmentClassList";
	}
	@RequestMapping("/getEquipmentClass.do")
	public ModelAndView getequipmentclass(HttpServletRequest request,Model model) {
		int pages = 0;
		if(request.getParameter("page")!=null&&!request.getParameter("page").isEmpty()){
			pages = Integer.parseInt(request.getParameter("page"));
		}else {
			pages = 1;
		}
		int pagesize = 0;
		if(request.getParameter("rows")!=null&&!request.getParameter("rows").isEmpty()){
			pagesize = Integer.parseInt(request.getParameter("rows"));
		}else {
			pagesize = 20;
		}
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else {
			sort = " name ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by  "+sort+" "+order;
		User cu= (User)request.getSession().getAttribute("cu");	
		String wherestr=CommUtil.getwherestr("insuser", "equipment/showEquipmentClass.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}				
		PageHelper.startPage(pages, pagesize);
        List<EquipmentClass> list = this.equipmentclassService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<EquipmentClass> page = new PageInfo<EquipmentClass>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/addEquipmentClass.do")
	public String doadd(HttpServletRequest request,Model model){
		return "equipment/equipmentClassAdd";
	}
	@RequestMapping("/deleteEquipmentClass.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.equipmentclassService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteEquipmentClasses.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.equipmentclassService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/saveEquipmentClass.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("equipmentclass") EquipmentClass equipmentclass){
		User cu= (User)request.getSession().getAttribute("cu");		
		if(!this.equipmentclassService.checkNotOccupied(equipmentclass.getId(), equipmentclass.getName())){
			model.addAttribute("result", "{\"res\":\"类型名称重复\"}");
			return "result";
		}		
		String equipmentclassId = CommUtil.getUUID();
		equipmentclass.setId(equipmentclassId);		
		equipmentclass.setInsuser(cu.getId());
		equipmentclass.setInsdt(CommUtil.nowDate());	
		int result = this.equipmentclassService.save(equipmentclass);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+equipmentclassId+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/viewEquipmentClass.do")
	public String doview(HttpServletRequest request,Model model){
		String equipmentclassId = request.getParameter("id");
		EquipmentClass equipmentclass = this.equipmentclassService.selectById(equipmentclassId);
		model.addAttribute("equipmentclass", equipmentclass);
		return "equipment/equipmentClassView";
	}
	@RequestMapping("/editEquipmentClass.do")
	public String doedit(HttpServletRequest request,Model model){
		String equipmentclassId = request.getParameter("id");
		EquipmentClass equipmentclass = this.equipmentclassService.selectById(equipmentclassId);
		model.addAttribute("equipmentclass", equipmentclass);		
		return "equipment/equipmentClassEdit";
	}
	@RequestMapping("/updateEquipmentClass.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("equipmentclass") EquipmentClass equipmentclass){		
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.equipmentclassService.checkNotOccupied(equipmentclass.getId(), equipmentclass.getName())){
			model.addAttribute("result", "{\"res\":\"类型名称重复\"}");
			return "result";
		}
		equipmentclass.setInsuser(cu.getId());
		equipmentclass.setInsdt(CommUtil.nowDate());
		int result = this.equipmentclassService.update(equipmentclass);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+equipmentclass.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
}
