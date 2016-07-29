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
import com.sipai.entity.user.Job;
import com.sipai.service.user.JobService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/user")
public class JobController {
	@Resource
	private JobService jobService;
	
	@RequestMapping("/showListJob.do")
	public String showlist(HttpServletRequest request,Model model){
		return "user/jobList";
	}
	
	@RequestMapping("/getListJob.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		if(sort==null){
			sort = " pri ";
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
        List<Job> list = this.jobService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Job> pi = new PageInfo<Job>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonJob.do")
	public ModelAndView getJsonJob(HttpServletRequest request,Model model) {
		String orderstr=" order by pri asc ";
		String wherestr=" where 1=1 ";
        List<Job> list = this.jobService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonJobByUnit.do")
	public ModelAndView getJsonJobByUnit(HttpServletRequest request,Model model) {
        List<Job> list = this.jobService.selectListByUnitid(request.getParameter("unitid"));
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/saveJob.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Job job){
		job.setId(CommUtil.getUUID());
		int result = this.jobService.save(job);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+job.getId()+"\"}");
		return "result";
	}
	
	@RequestMapping("/updateJob.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Job job){
		int result = this.jobService.update(job);
		model.addAttribute("result", "{\"res\":\""+result+"\",\"id\":\""+job.getId()+"\"}");
		return "result";
	}
	
	@RequestMapping("/deleteJob.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.jobService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
}
