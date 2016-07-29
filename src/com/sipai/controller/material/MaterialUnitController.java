package com.sipai.controller.material;

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
import com.sipai.entity.material.MaterialUnit;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialUnitService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/materialunit")
public class MaterialUnitController {
	@Resource
	private MaterialUnitService materialUnitService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialUnitList";
	}
	
	@RequestMapping("/getMaterialUnits.do")
	public ModelAndView getMaterialUnits(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
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
			sort = " unit ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "material/materialunit/getMaterialUnits.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and unit like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(pages, pagesize);
        List<MaterialUnit> list = this.materialUnitService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<MaterialUnit> page = new PageInfo<MaterialUnit>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonUnit.do")
	public ModelAndView getlist(HttpServletRequest request,Model model) {		
		String wherestr=" where 1=1 and status='1' ";
		String orderstr=" order by unit asc ";
        List<MaterialUnit> list = this.materialUnitService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/materialUnitAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialUnit") MaterialUnit materialUnit){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.materialUnitService.checkNotOccupied(materialUnit.getId(),materialUnit.getUnit())){
			model.addAttribute("result", "{\"res\":\"单位重复\"}");
			return "result";
		}
		
		String id = CommUtil.getUUID();
		materialUnit.setId(id);
		materialUnit.setInsuser(cu.getId());
		materialUnit.setInsdt(CommUtil.nowDate());
		materialUnit.setModify(cu.getId());
		materialUnit.setModifydt(CommUtil.nowDate());
		
		int result = this.materialUnitService.save(materialUnit);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialUnit materialUnit = this.materialUnitService.selectById(id);
		model.addAttribute("materialUnit", materialUnit);
		return "/material/materialUnitEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialUnit") MaterialUnit materialUnit){
		if(!this.materialUnitService.checkNotOccupied(materialUnit.getId(),materialUnit.getUnit())){
			model.addAttribute("result", "{\"res\":\"物料类型重复\"}");
			return "result";
		}
		
		int result = this.materialUnitService.update(materialUnit);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialUnit.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialUnit materialUnit = this.materialUnitService.selectById(id);
		model.addAttribute("materialUnit", materialUnit);
		return "/material/materialUnitView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialUnitService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialUnitService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
