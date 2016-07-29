package com.sipai.controller.work;

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
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkstationType;
import com.sipai.service.work.WorkstationTypeService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workstationType")
public class WorkstationTypeController {
	@Resource
	private WorkstationTypeService workstationTypeService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/workstationTypeList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " name ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/workstationType/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<WorkstationType> list = this.workstationTypeService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<WorkstationType> pi = new PageInfo<WorkstationType>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getselectlist.do")
	public ModelAndView getselectlist(HttpServletRequest request,Model model) {
        List<WorkstationType> list = this.workstationTypeService.selectListByWhere("");
		JSONArray json=JSONArray.fromObject(list);
		
//		System.out.println(result);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/workstationTypeAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		WorkstationType workstationType = this.workstationTypeService.selectById(userId);
		model.addAttribute("workstationType", workstationType);
		return "work/workstationTypeEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute WorkstationType workstationType){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.workstationTypeService.checkNotOccupied(workstationType.getId(),workstationType.getSerial())){
			model.addAttribute("result", "{\"res\":\"工位类型编号重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		workstationType.setId(id);
		workstationType.setInsuser(cu.getId());
		workstationType.setInsdt(CommUtil.nowDate());
		
		int result = this.workstationTypeService.save(workstationType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkstationType workstationType){
		if(!this.workstationTypeService.checkNotOccupied(workstationType.getId(),workstationType.getSerial())){
			model.addAttribute("result", "{\"res\":\"工位类型编号重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		workstationType.setInsuser(cu.getId());
		workstationType.setInsdt(CommUtil.nowDate());
		int result = this.workstationTypeService.update(workstationType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workstationType.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workstationTypeService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workstationTypeService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
