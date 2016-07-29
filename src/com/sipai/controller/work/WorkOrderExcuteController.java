package com.sipai.controller.work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.JsonHelper;
import org.snaker.engine.helper.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkOrderExcute;
import com.sipai.service.work.WorkOrderExcuteService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workOrderExcute")
public class WorkOrderExcuteController {
	@Resource
	private WorkOrderExcute workOrderExcute;
	@Resource
	private WorkOrderExcuteService workOrderExcuteService;
	@Resource
	private SnakerEngineFacets facets;
	@Resource
	private WorkOrderService workOrderService;
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("taskId");
		this.workOrderExcute = this.workOrderExcuteService.selectById(id);
		
		WorkOrderExcute workOrderExcute = new WorkOrderExcute();
		if(this.workOrderExcute==null){
			workOrderExcute.setId(id);
			workOrderExcute.setWftaskid(id);
		}else{
			workOrderExcute = this.workOrderExcute;
		}
		model.addAttribute("workOrderExcute", workOrderExcute);
		return "work/workOrderExcuteEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrderExcute workOrderExcute){
		this.workOrderExcute = this.workOrderExcuteService.selectById(workOrderExcute.getId());
		
		User cu= (User)request.getSession().getAttribute("cu");
		int result = 0;
		if(this.workOrderExcute == null){
			workOrderExcute.setInsuser(cu.getId());
			workOrderExcute.setInsdt(CommUtil.nowDate());
			result = this.workOrderExcuteService.save(workOrderExcute);
		}else{
			workOrderExcute.setUpdateuser(cu.getId());
			workOrderExcute.setUpdatedt(CommUtil.nowDate());
			result = this.workOrderExcuteService.update(workOrderExcute);
		}
		
		//执行流程
		if(result>0){
			Map<String,Object> args = new HashMap<String,Object>();
			//偷懒未添加班组--使用超级管理员
			//facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), cu.getId(), args);
			List<Task> tasks = facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), "snaker.admin", args);
			if(tasks!=null && tasks.size()>0){
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), tasks.get(0).getDisplayName());
			}else{
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), "已完成");
			}
		}
		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workOrderExcute.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workOrderExcuteService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/highCurrentTest.do")
	public String highCurrentTest(HttpServletRequest request,Model model){
		String id = request.getParameter("taskId");
		this.workOrderExcute = this.workOrderExcuteService.selectById(id);
		
		WorkOrderExcute workOrderExcute = new WorkOrderExcute();
		if(this.workOrderExcute==null){
			workOrderExcute.setId(id);
			workOrderExcute.setWftaskid(id);
		}else{
			workOrderExcute = this.workOrderExcute;
		}
		model.addAttribute("workOrderExcute", workOrderExcute);
		return "work/wfHighCurrentTest";
	}
	
	@RequestMapping("/excuteHighCurrentTest.do")
	public String excuteHighCurrentTest(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrderExcute workOrderExcute){
		this.workOrderExcute = this.workOrderExcuteService.selectById(workOrderExcute.getId());
		User cu= (User)request.getSession().getAttribute("cu");
		int result = 0;
		if(this.workOrderExcute == null){
			workOrderExcute.setInsuser(cu.getId());
			workOrderExcute.setInsdt(CommUtil.nowDate());
			result = this.workOrderExcuteService.save(workOrderExcute);
		}else{
			workOrderExcute.setUpdateuser(cu.getId());
			workOrderExcute.setUpdatedt(CommUtil.nowDate());
			result = this.workOrderExcuteService.update(workOrderExcute);
		}
		boolean passFlag = false;
		String pass = request.getParameter("pass");
		if(pass.equals("1")){
			passFlag = true;
		}
		//执行流程
		if(result>0){
			Map<String,Object> args = new HashMap<String,Object>();
			args.put("pass", passFlag);
			//facets.getEngine().executeTask(workOrderExcute.getWftaskid(), cu.getId(), args);
			//偷懒未添加班组--使用超级管理员
			//System.out.println("UserID-----"+cu.getId());
			//facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), cu.getId(), args);
			List<Task> tasks=facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), "snaker.admin", args);
			if(tasks!=null && tasks.size()>0){
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), tasks.get(0).getDisplayName());
			}else{
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), "已完成");
			}
		}
		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workOrderExcute.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
}
