package com.sipai.controller.quality;

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
import com.sipai.entity.quality.LeaveFactoryCheck;
import com.sipai.entity.user.User;
import com.sipai.service.quality.LeaveFactoryCheckService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/quality/leavefactorycheck")
public class LeaveFactoryCheckController {
	@Resource
	private LeaveFactoryCheckService leaveFactoryCheckService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/quality/leaveFactoryCheckList";
	}	
	@RequestMapping("/getLeaveFactoryChecks.do")
	public ModelAndView getLeaveFactoryChecks(HttpServletRequest request,Model model) {
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
			sort = " insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "quality/leavefactorycheck/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and equipmentname like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(pages, pagesize);
        List<LeaveFactoryCheck> list = this.leaveFactoryCheckService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<LeaveFactoryCheck> page = new PageInfo<LeaveFactoryCheck>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		String nowdate=CommUtil.nowDate();
		model.addAttribute("nowdate", nowdate);
		return "/quality/leaveFactoryCheckAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("LeaveFactoryCheck") LeaveFactoryCheck leaveFactoryCheck){
		User cu= (User)request.getSession().getAttribute("cu");		
		
		String id = CommUtil.getUUID();
		leaveFactoryCheck.setId(id);
		leaveFactoryCheck.setInsuser(cu.getId());
		leaveFactoryCheck.setInsdt(CommUtil.nowDate());	
		String remarktb="";
		for(int i=1;i<=10;i++){
			remarktb+=request.getParameter("remark"+i)+" ;";
		}
		leaveFactoryCheck.setRemarktable(remarktb);
		int result = this.leaveFactoryCheckService.save(leaveFactoryCheck);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		LeaveFactoryCheck leavefactorycheck = this.leaveFactoryCheckService.selectById(id);
		String remarktb=leavefactorycheck.getRemarktable();
		//remarktb=remarktb.substring(0, remarktb.length()-1);
		String[] remarktbarr=remarktb.split(";");
		for(int i=0;i<10;i++){
			if(remarktbarr[i]!=null &&remarktbarr[i]!=" "){
				model.addAttribute("remark"+(i+1), remarktbarr[i]);
			}			
		}
		model.addAttribute("leavefactorycheck", leavefactorycheck);
		return "/quality/leaveFactoryCheckEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("LeaveFactoryCheck") LeaveFactoryCheck leavefactorycheck){
		User cu= (User)request.getSession().getAttribute("cu");
		leavefactorycheck.setInsuser(cu.getId());
		leavefactorycheck.setInsdt(CommUtil.nowDate());	
		String remarktb="";
		for(int i=1;i<=10;i++){
			remarktb+=request.getParameter("remark"+i)+" ;";
		}
		leavefactorycheck.setRemarktable(remarktb);
		int result = this.leaveFactoryCheckService.update(leavefactorycheck);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+leavefactorycheck.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		LeaveFactoryCheck leavefactorycheck = this.leaveFactoryCheckService.selectById(id);
		String remarktb=leavefactorycheck.getRemarktable();
		//remarktb=remarktb.substring(0, remarktb.length()-1);
		String[] remarktbarr=remarktb.split(";");
		for(int i=0;i<10;i++){
			if(remarktbarr[i]!=null &&remarktbarr[i]!=" "){
				model.addAttribute("remark"+(i+1), remarktbarr[i]);
			}
		}
		model.addAttribute("leavefactorycheck", leavefactorycheck);
		return "/quality/leaveFactoryCheckView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.leaveFactoryCheckService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.leaveFactoryCheckService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
