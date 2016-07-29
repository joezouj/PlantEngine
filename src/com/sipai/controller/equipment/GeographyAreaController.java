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
import com.sipai.entity.equipment.GeographyArea;
import com.sipai.entity.user.User;
import com.sipai.service.equipment.GeographyAreaService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/equipment")
public class GeographyAreaController {
	@Resource
	private GeographyAreaService geographyareaService;
	@RequestMapping("/showGeographyArea.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/equipment/geographyAreaList";
	}
	@RequestMapping("/getGeographyArea.do")
	public ModelAndView getgeographyarea(HttpServletRequest request,Model model) {
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
		String wherestr=CommUtil.getwherestr("insuser", "equipment/showGeographyArea.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}				
		PageHelper.startPage(pages, pagesize);
        List<GeographyArea> list = this.geographyareaService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<GeographyArea> page = new PageInfo<GeographyArea>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/addGeographyArea.do")
	public String doadd(HttpServletRequest request,Model model){
		return "equipment/geographyAreaAdd";
	}
	@RequestMapping("/deleteGeographyArea.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.geographyareaService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteGeographyAreas.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.geographyareaService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/saveGeographyArea.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("geographyarea") GeographyArea geographyarea){
		User cu= (User)request.getSession().getAttribute("cu");		
		if(!this.geographyareaService.checkNotOccupied(geographyarea.getId(), geographyarea.getName())){
			model.addAttribute("result", "{\"res\":\"位置名称重复\"}");
			return "result";
		}		
		String geographyareaId = CommUtil.getUUID();
		geographyarea.setId(geographyareaId);		
		geographyarea.setInsuser(cu.getId());
		geographyarea.setInsdt(CommUtil.nowDate());	
		int result = this.geographyareaService.save(geographyarea);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+geographyareaId+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/viewGeographyArea.do")
	public String doview(HttpServletRequest request,Model model){
		String geographyareaId = request.getParameter("id");
		GeographyArea geographyarea = this.geographyareaService.selectById(geographyareaId);
		model.addAttribute("geographyarea", geographyarea);
		return "equipment/geographyAreaView";
	}
	@RequestMapping("/editGeographyArea.do")
	public String doedit(HttpServletRequest request,Model model){
		String geographyareaId = request.getParameter("id");
		GeographyArea geographyarea = this.geographyareaService.selectById(geographyareaId);
		model.addAttribute("geographyarea", geographyarea);		
		return "equipment/geographyAreaEdit";
	}
	@RequestMapping("/updateGeographyArea.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("geographyarea") GeographyArea geographyarea){		
		User cu= (User)request.getSession().getAttribute("cu");		
		if(!this.geographyareaService.checkNotOccupied(geographyarea.getId(), geographyarea.getName())){
			model.addAttribute("result", "{\"res\":\"位置名称重复\"}");
			return "result";
		}	
		geographyarea.setInsuser(cu.getId());
		geographyarea.setInsdt(CommUtil.nowDate());
		int result = this.geographyareaService.update(geographyarea);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+geographyarea.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
}
