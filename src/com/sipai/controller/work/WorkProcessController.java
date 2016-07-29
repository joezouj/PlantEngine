package com.sipai.controller.work;

import java.util.ArrayList;
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
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkProcess;
import com.sipai.entity.work.WorkProcessOrder;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.service.work.WorkProcessOrderService;
import com.sipai.service.work.WorkProcessService;
import com.sipai.service.work.WorkTaskWorkStationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workprocess")
public class WorkProcessController {
	@Resource
	private WorkProcessService workProcessService;
	@Resource
	private WorkProcessOrderService workProcessOrderService;
	@Resource
	private WorkTaskWorkStationService workTaskWorkStationService;
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid) {
		model.addAttribute("taskid", taskid);
		return "/work/workprocessList";
	}
	@RequestMapping("/getlist_process.do")
	public ModelAndView getlist_process(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid) {
		User cu=(User)request.getSession().getAttribute("cu");
        List<RealDetails> list_details = this.workProcessService.getprocessdetails("where wtask.id='"+taskid+"' order by number asc");
        List<WorkProcessOrder> list=this.workProcessOrderService.selectListByWhere("where taskid='"+taskid+"' order by insdt asc");
        if(list_details.size()>0 && list.size()==0){
        	WorkProcessOrder workProcessOrder=new WorkProcessOrder();
        	workProcessOrder.setId(CommUtil.getUUID());
        	workProcessOrder.setTaskid(taskid);
        	workProcessOrder.setProcessRealid(list_details.get(0).getPid());
        	workProcessOrder.setProcessRealdetailid(list_details.get(0).getId());
        	workProcessOrder.setInsdt(CommUtil.nowDate());
        	workProcessOrder.setInsuser(cu.getId());
        	this.workProcessOrderService.save(workProcessOrder);
        	list=this.workProcessOrderService.selectListByWhere("where taskid='"+taskid+"' order by insdt asc");
        }
        
        
        PageInfo<WorkProcessOrder> pi = new PageInfo<WorkProcessOrder>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/showlist_detail.do")
	public String showlist_detail(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value = "processdetailid") String processdetailid,
			@RequestParam(value = "processid") String processid,
			@RequestParam(value = "processorderid") String processorderid) {
		model.addAttribute("taskid", taskid);
		model.addAttribute("processdetailid", processdetailid);
		model.addAttribute("processid", processid);
		model.addAttribute("processorderid", processorderid);
		return "/work/workprocessdetail";
	}
	@RequestMapping("/getlist_agency.do")
	public ModelAndView getlist_agency(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid) {
		User cu=(User)request.getSession().getAttribute("cu");
		List<WorkTaskWorkStation> list=new ArrayList<WorkTaskWorkStation>();
		if(cu.getId().equals("emp01")){
			list=workTaskWorkStationService.getWorkStationByUserid("where wtws.taskid='"+taskid+"' and wp.edt is null");
		}else{
			list=workTaskWorkStationService.getWorkStationByUserid("where wtws.taskid='"+taskid+
					"'and ws.userid ='"+cu.getId()+"' and wp.edt is null");
		}
		
		JSONArray json=JSONArray.fromObject(list);
		PageInfo<WorkTaskWorkStation> pi = new PageInfo<WorkTaskWorkStation>(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_process_detail.do")
	public ModelAndView getlist_process_detail(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid) {
		List<WorkTaskWorkStation> list=workTaskWorkStationService.selectListByWhere("where taskid='"+taskid+"'");
		JSONArray json=JSONArray.fromObject(list);
		PageInfo<WorkTaskWorkStation> pi = new PageInfo<WorkTaskWorkStation>(list);
		model.addAttribute("workstaion", list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getworkstation.do")
	public ModelAndView getworkstation(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid,
			@RequestParam(value="processdetailid") String processdetailid,
			@RequestParam(value="processid") String processid,
			@RequestParam(value="processorderid") String processorderid) {
		User cu=(User)request.getSession().getAttribute("cu");
		List<WorkTaskWorkStation> list=workTaskWorkStationService.selectListByWhere("where taskid='"+taskid+"'and process_realdetailid ='"+
							processdetailid+"' and process_realid ='"+processid+"'");
		for(int i=0;i<list.size();i++){
			list.get(i).setWorkstationname(list.get(i).getWorkstation().getName());
			WorkTaskWorkStation workTaskWorkStation=list.get(i);
			List<WorkProcess> list_processList=this.workProcessService.selectListByWhere(
					"where taskid='"+taskid+"' and userid ='"+cu.getId()+"' and process_realdetailid ='"+
							processdetailid+"' and process_realid ='"+processid+"' and workstationid='"+workTaskWorkStation.getWorkstationid()+
							"' and processorderid='"+processorderid+"'");
			if(list_processList==null || list_processList.size()==0||list_processList.get(0).getStdt()==null){
				list.get(i).set_taskstatue("0");//0未开始，1开始，2，结束
			}else if(list_processList.get(0).getEdt()==null){
				list.get(i).set_taskstatue("1");
			}else{
				list.get(i).set_taskstatue("2");
			}
		}
		//model.addAttribute("stationlist", list); //用于查看当前人员的任务状态
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result", json);//用于展示工位列表
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getprocess.do")
	public ModelAndView getprocess(HttpServletRequest request,Model model,
			@RequestParam(value = "taskid") String taskid) {
		User cu=(User)request.getSession().getAttribute("cu");
		List<RealDetails> list = this.workProcessService.getprocessdetails("where wtask.id='"+taskid+"' order by number asc");
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result", json);//用于展示工位列表
        return new ModelAndView("result");
	}
	@RequestMapping("/starttask.do")
	public String starttask(HttpServletRequest request,Model model,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="workstationid") String workstationid,
			@RequestParam(value="processdetailid") String processdetailid,
			@RequestParam(value="processid") String processid,
			@RequestParam(value="processorderid") String processorderid){
//		List<WorkProcess> list=this.workProcessService.selectListByWhere("where taskid='"+taskid+"' and process_realid='"+
//				processid+"' and process_realdetailid='"+processdetailid+"' and workstationid='"+workstationid+ "'");
		int result=0;
		User cu=(User)request.getSession().getAttribute("cu");
//		if(list==null || list.size()==0){
			String id = CommUtil.getUUID();
			WorkProcess workProcess =new WorkProcess();
			workProcess.setId(id);
			workProcess.setInsuser(cu.getId());
			workProcess.setInsdt(CommUtil.nowDate());
			workProcess.setTaskid(taskid);
			workProcess.setProcessRealid(processid);
			workProcess.setProcessRealdetailid(processdetailid);
			workProcess.setWorkstationid(workstationid);
			workProcess.setStdt(CommUtil.nowDate());
			workProcess.setUserid(cu.getId());
			workProcess.setProcessorderid(processorderid);
			result = this.workProcessService.save(workProcess);
//		}else{
//			WorkProcess workProcess=list.get(0);
//			workProcess.setInsuser(cu.getId());
//			workProcess.setInsdt(CommUtil.nowDate());
//			workProcess.setStdt(CommUtil.nowDate());
//			workProcess.setUserid(cu.getId());
//			result = this.workProcessService.update(workProcess);
//		}
		
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/endtask.do")
	public String endtask(HttpServletRequest request,Model model,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="workstationid") String workstationid,
			@RequestParam(value="processdetailid") String processdetailid,
			@RequestParam(value="processid") String processid){
		User cu=(User)request.getSession().getAttribute("cu");
		List<WorkProcess>  list =this.workProcessService.selectListByWhere("where taskid='"+taskid+"' and process_realid='"+
				processid+"' and process_realdetailid='"+processdetailid+"' and workstationid='"+workstationid+ "' and userid='"+cu.getId()+"'");
		WorkProcess workProcess=list.get(0);
		workProcess.setUpdatedt(CommUtil.nowDate());
		workProcess.setUpdateuser(cu.getId());
		workProcess.setEdt(CommUtil.nowDate());
		int result = this.workProcessService.update(workProcess);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/nexttask.do")
	public String nexttask(HttpServletRequest request,Model model,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="processdetailid") String processdetailid,
			@RequestParam(value="processid") String processid){
		User cu=(User)request.getSession().getAttribute("cu");
		WorkProcessOrder workProcessOrder=new WorkProcessOrder();
    	workProcessOrder.setId(CommUtil.getUUID());
    	workProcessOrder.setTaskid(taskid);
    	workProcessOrder.setProcessRealid(processid);
    	workProcessOrder.setProcessRealdetailid(processdetailid);
    	workProcessOrder.setInsdt(CommUtil.nowDate());
    	workProcessOrder.setInsuser(cu.getId());
    	int result=this.workProcessOrderService.save(workProcessOrder);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/delprocess.do")
	public String delprocess(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workProcessService.deleteByWhere("where processorderid in ('"+ids+"')");
		result = this.workProcessOrderService.deleteByWhere("where id in ('"+ids+"')");
		
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " stdt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/process/showlist.do", cu);
		if(request.getParameter("search_productno")!=null && !request.getParameter("search_productno").isEmpty()){
			wherestr += " and productNo like '%"+request.getParameter("search_productno")+"%' ";
		}
		if(request.getParameter("search_taskno")!=null && !request.getParameter("search_taskno").isEmpty()){
			wherestr += " and taskno like '%"+request.getParameter("search_taskno")+"%' ";
		}
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += " and eddt <= '"+request.getParameter("edt")+"' ";
		}
		
		PageHelper.startPage(page, rows);
        List<WorkProcess> list = this.workProcessService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<WorkProcess> pi = new PageInfo<WorkProcess>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/worktaskAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		WorkProcess workProcess = this.workProcessService.selectById(userId);
		model.addAttribute("worktask", workProcess);
		return "work/worktaskEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute WorkProcess workProcess){
		User cu= (User)request.getSession().getAttribute("cu");
		
		String id = CommUtil.getUUID();
		workProcess.setId(id);
		workProcess.setInsuser(cu.getId());
		workProcess.setInsdt(CommUtil.nowDate());
		
		int result = this.workProcessService.save(workProcess);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkProcess workProcess){
		User cu= (User)request.getSession().getAttribute("cu");
		workProcess.setInsuser(cu.getId());
		workProcess.setInsdt(CommUtil.nowDate());
		int result = this.workProcessService.update(workProcess);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workProcess.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workProcessService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workProcessService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
}
