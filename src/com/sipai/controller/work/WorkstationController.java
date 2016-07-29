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
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Workstation;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.UserWorkStationService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workstation")
public class WorkstationController {
	@Resource
	private WorkstationService workstationService;
	@Resource
	private UnitService unitService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/workstationList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " serial ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/workstation/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_dept")!=null && !request.getParameter("search_dept").isEmpty()){
			List<Unit> unitlist=unitService.getUnitChildrenById(request.getParameter("search_dept"));
			String pidstr="";
			for(int i=0;i<unitlist.size();i++){
				pidstr += "'"+unitlist.get(i).getId()+"',";
			}
			if(pidstr!=""){
				pidstr = pidstr.substring(0, pidstr.length()-1);
				wherestr += "and deptid in ("+pidstr+") ";
			}
		}
		
		PageHelper.startPage(page, rows);
        List<Workstation> list = this.workstationService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Workstation> pi = new PageInfo<Workstation>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/workstationAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		Workstation workstation = this.workstationService.selectById(userId);
		model.addAttribute("workstation", workstation);
		return "work/workstationEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Workstation workstation){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.workstationService.checkNotOccupied(workstation.getId(), workstation.getSerial())){
			model.addAttribute("result", "{\"res\":\"工位编号重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		workstation.setId(id);
		workstation.setInsuser(cu.getId());
		workstation.setInsdt(CommUtil.nowDate());
		
		int result = this.workstationService.save(workstation);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Workstation workstation){
		if(!this.workstationService.checkNotOccupied(workstation.getId(), workstation.getSerial())){
			model.addAttribute("result", "{\"res\":\"工位编号重复\"}");
			return "result";
		}
		int result = this.workstationService.update(workstation);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workstation.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workstationService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workstationService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showlistForSelect.do")
	public String showlistForSelect(HttpServletRequest request,Model model) {
		return "/work/workstationListForSelect";
	}
	
	@RequestMapping("/showlistForSelects.do")
	public String showlistForSelects(HttpServletRequest request,Model model) {
		return "/work/workstationListForSelects";
	}
}
