package com.sipai.controller.plan;

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
import com.sipai.entity.plan.MaterialRecover;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialInfoService;
import com.sipai.service.plan.MaterialRecoverService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/materialrecover")
public class MaterialRecoverController {
	@Resource
	private MaterialRecoverService materialRecoverService;
	@Resource
	private MaterialInfoService materialInfoService;
	@Resource
	private WorkstationService workstationService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/plan/materialRecoverList";
	}
	@RequestMapping("/getMaterialRecovers.do")
	public ModelAndView getMaterialRecovers(HttpServletRequest request,Model model) {
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
			sort = " recovertime ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = " where 1=1 ";
		if(request.getParameter("search_workstationserial")!=null && !request.getParameter("search_workstationserial").isEmpty()){
			wherestr += " and w.serial like '%"+request.getParameter("search_workstationserial")+"%'";
		}
		if(request.getParameter("search_materialcode")!=null && !request.getParameter("search_materialcode").isEmpty()){
			wherestr += " and m.materialcode like '%"+request.getParameter("search_materialcode")+"%'";
		}
		PageHelper.startPage(pages, pagesize);
        List<MaterialRecover> list = this.materialRecoverService.getMaterialRecover(wherestr+orderstr);
        
        PageInfo<MaterialRecover> page = new PageInfo<MaterialRecover>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/plan/materialRecoverAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialRecover") MaterialRecover materialRecover){
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		materialRecover.setId(id);
		materialRecover.setRecoverid(cu.getId());
		materialRecover.setRecovertime(CommUtil.nowDate());
		
		int result = this.materialRecoverService.save(materialRecover);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	

	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialRecover materialRecover = this.materialRecoverService.selectById(id);
		model.addAttribute("materialRecover", materialRecover);
		return "/plan/materialRecoverEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialRecover") MaterialRecover materialRecover){
		User cu= (User)request.getSession().getAttribute("cu");
		materialRecover.setRecoverid(cu.getId());
		materialRecover.setRecovertime(CommUtil.nowDate());
		int result = this.materialRecoverService.update(materialRecover);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialRecover.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialRecover materialRecover = this.materialRecoverService.selectById(id);
		model.addAttribute("materialRecover", materialRecover);
		return "/plan/materialRecoverView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialRecoverService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialRecoverService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
