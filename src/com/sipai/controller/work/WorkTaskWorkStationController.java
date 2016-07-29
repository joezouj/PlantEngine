package com.sipai.controller.work;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.model.ProcessModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.process.Real;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Line;
import com.sipai.entity.work.WorkOrder;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealService;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.LineService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.service.work.WorkTaskEquipmentService;
import com.sipai.service.work.WorkTaskMaterialService;
import com.sipai.service.work.WorkTaskWorkStationService;
import com.sipai.service.work.WorkstationService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.snaker.SnakerHelper;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/worktaskworkstation")
public class WorkTaskWorkStationController {
	@Resource
	private WorkTaskWorkStationService workTaskWorkStationService;
	@Resource
	private WorkstationService workstationService;
	
	@Resource
	private UnitService unitService;

	@Resource
	private WorkOrderService workOrderService;

	@Resource
	private LineService lineService;

	@Resource
	private RealService realService;

	@Resource
	private RealDetailsService realDetailsService;

	@Resource
	private WorkTaskMaterialService workTaskMaterialService;

	@Resource
	private WorkTaskEquipmentService workTaskEquipmentService;

	@Resource
	private SnakerEngineFacets facets;

	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/worktaskList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " productno ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;

		String wherestr=CommUtil.getwherestr("insuser", "work/workorder/showlist.do", cu);
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
		List<WorkTaskWorkStation> list = this.workTaskWorkStationService.selectListByWhere(wherestr+orderstr);

		PageInfo<WorkTaskWorkStation> pi = new PageInfo<WorkTaskWorkStation>(list);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	@RequestMapping("/districtworkstation.do")
	public String districtworkstation(HttpServletRequest request,Model model){
		WorkOrder workorder = this.workOrderService.selectById(request.getParameter("taskid"));
		model.addAttribute("workorder",workorder);
		Line line= this.lineService.selectById(workorder.getLineid());
		model.addAttribute("linename",line.getName());
		return "work/districtworkstation";
	}
	@RequestMapping("/getworkstationlist.do")
	public ModelAndView getrealworkstationlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value="taskname") String taskname,
			@RequestParam(value="taskid") String taskid) {//task是工单id，不是snaker中的taskid
		if(sort==null){
			sort = " tb_work_workstation.serial ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;

		String wherestr="where 1=1";

		//获取产品工序id
		WorkOrder workorder = this.workOrderService.selectById(taskid);
		String lineid="";
		if(workorder!=null){
			//获取processrealdetailid
			List<RealDetails> realdetailslist = realDetailsService.selectListByWhere(" where pid='"+workorder.getProcessrealid()+"' and taskname='"+taskname+"'");
			if(realdetailslist!=null && realdetailslist.size()>0){
				String processrealdetailid=realdetailslist.get(0).getId();
				wherestr += " and tb_process_real_details.id='"+processrealdetailid+"' ";
				
				//检查是否有已生产过的产线
				List<WorkTaskWorkStation> wlist= this.workTaskWorkStationService.selectListByWhere2("where process_realdetailid = '"+processrealdetailid+"' and tb_work_task_workstation.taskid='"+workorder.getId()+"'");
				if(wlist!=null && wlist.size()>0){//如果有已生产过的产线，则显示已生产过的产线
					lineid=wlist.get(0).getLineid();
				}else{//如果没有已生产过的产线，则显示当前设置好的产线
					if(workorder.getLineid()!=null && !workorder.getLineid().equals("")){
						lineid=workorder.getLineid();
					}
				}
				if(request.getParameter("name_search")!=null && !request.getParameter("name_search").isEmpty()){
					lineid=request.getParameter("name_search");
				}
			}
		}
		if(!lineid.equals("")){
			wherestr += " and tb_work_line_workstation.lineid = '"+lineid+"' ";
		}
//		System.out.println(wherestr+orderstr);

		PageHelper.startPage(page, rows);
		List<WorkTaskWorkStation> list= this.workTaskWorkStationService.getWorkStation(wherestr+orderstr);
		PageInfo<WorkTaskWorkStation> pi = new PageInfo<WorkTaskWorkStation>(list);

		List<WorkTaskWorkStation> list_select = this.workTaskWorkStationService.selectListByWhere("where taskid='"+taskid+"'");
		for(int i=0;i<list.size();i++){
			list.get(i).set_checked(false);
			for(int j=0;j<list_select.size();j++){
				if(list.get(i).getWorkstationid().equals(list_select.get(j).getWorkstationid())){
					list.get(i).set_checked(true);
				}
			}
		}
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+",\"lineid\":\""+lineid+"\"}";
//		System.out.println(result);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		WorkTaskWorkStation worktask = this.workTaskWorkStationService.selectById(userId);
		model.addAttribute("worktask", worktask);
		return "work/worktaskEdit";
	}

	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="processrealdetailid") String processrealdetailid,
			@RequestParam(value="processrealid") String processrealid){
		User cu= (User)request.getSession().getAttribute("cu");
		int result=1;
		int resultflag=0;
		String[] item=ids.split(",");
		if(item!=null && item.length>0){
			//清除该工序下所有关联的物料信息和设备信息
			/*String wSql = " where 1=1 and tb_process_real_details.id = '"+processrealdetailid+"'"+" order by tb_work_workstation.serial ";
			List<WorkTaskWorkStation> wlist= this.workTaskWorkStationService.getWorkStation(wSql);
			for(int w=0;w<wlist.size();w++){
				this.workTaskMaterialService.deleteByWhere("where 1=1 and taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+wlist.get(w).getWorkstationid()+"'");
				this.workTaskEquipmentService.deleteByWhere("where 1=1 and taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+wlist.get(w).getWorkstationid()+"'");
			}*/
			
			//获取工位配置的旧数据
			List<WorkTaskWorkStation> oldWorkTaskWorkStations= this.workTaskWorkStationService.selectListByWhere("where taskid='"+taskid+"' and process_realdetailid ='"+processrealdetailid+"' ");
			//删除工位配置的旧数据
			this.workTaskWorkStationService.deleteByWhere("where taskid='"+taskid+"' and process_realdetailid ='"+processrealdetailid+"' ");
			for(int i=0;i<item.length;i++){
				String id = CommUtil.getUUID();
				WorkTaskWorkStation workStation=new WorkTaskWorkStation();
				workStation.setId(id);
				workStation.setInsdt(CommUtil.nowDate());
				workStation.setInsuser(cu.getId());
				workStation.setWorkstationid(item[i]);
				workStation.setTaskid(taskid);
				workStation.setProcessRealdetailid(processrealdetailid);
				workStation.setProcessRealid(processrealid);
				resultflag = this.workTaskWorkStationService.save(workStation);
				if(resultflag==0){
					result=0;
				}
				
				//更新所有旧工位为新工位
				for(int j=0;j<oldWorkTaskWorkStations.size();j++){
					this.workTaskMaterialService.executeSql("update tb_work_task_material set workstationid='"+item[i]+"' where taskid='"+taskid+"' and processrealdetailid ='"+processrealdetailid+"' and workstationid='"+oldWorkTaskWorkStations.get(j).getWorkstationid()+"'");
					this.workTaskEquipmentService.executeSql("update tb_work_task_equipment set workstationid='"+item[i]+"' where taskid='"+taskid+"' and processrealdetailid ='"+processrealdetailid+"' and workstationid='"+oldWorkTaskWorkStations.get(j).getWorkstationid()+"'");
				}
			}
		}
		
		String refstr="";
		//如果配置的工位为当前活动的任务，更新操作参与者信息
		if(taskid!=null&&!taskid.equals("")){
			WorkOrder workOrder = this.workOrderService.selectById(taskid);
			if(workOrder!=null&&workOrder.getWforderid()!=null){
				facets.updateActorsByOrderId(workOrder.getWforderid());
				
				//如果前配置的任务属于snaker的活动任务，则需要传递refresh的标识用于刷新
				RealDetails realDetails = this.realDetailsService.selectById(processrealdetailid);
				List<WorkOrder> list = this.workOrderService.selectActiveTasks(" where tb_work_order.id='"+taskid+"' and wf_task.task_name = '"+realDetails.getTaskname()+"'");
				if(list.size()>0){
					refstr="refresh";
				}
			}
		}
		String resstr="{\"res\":\""+result+"\",\"refresh\":\""+refstr+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}

	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkTaskWorkStation worktask){
		User cu= (User)request.getSession().getAttribute("cu");
		int result = this.workTaskWorkStationService.update(worktask);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+worktask.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}

	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workTaskWorkStationService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}

	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workTaskWorkStationService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}

	@RequestMapping("/snaker.do")
	public String snaker(HttpServletRequest request,Model model,
			@RequestParam(value = "id") String id){
		WorkOrder workorder = this.workOrderService.selectById(id);

		List<Real> reallist = realService.selectListByWhere(" where id='"+workorder.getProcessrealid()+"'");
		if(reallist!=null && reallist.size()>0){
			model.addAttribute("processrealname", reallist.get(0).getName());

			if(StringUtils.isNotEmpty(reallist.get(0).getProcessid())) {
				Process process = facets.getEngine().process().getProcessById(reallist.get(0).getProcessid());
				AssertHelper.notNull(process);
				ProcessModel processModel = process.getModel();
				if(processModel != null) {
					String json = SnakerHelper.getModelJson(processModel);
					model.addAttribute("process", json);
				}
			}
		}
		return "work/districtworkstationSnakerList";
	}

	@RequestMapping("/snakerview.do")
	public String snakerview(HttpServletRequest request,Model model,
			@RequestParam(value = "id") String id){
		WorkOrder workOrder = this.workOrderService.selectById(id);
		Real real = realService.selectById(workOrder.getProcessrealid());
		model.addAttribute("processrealname", real.getName());
		
		String processId = realService.getProcessidByRealid(workOrder.getProcessrealid());
		String orderId = workOrder.getWforderid();
		Process process = facets.getEngine().process().getProcessById(processId);
		AssertHelper.notNull(process);
		ProcessModel processModel = process.getModel();
		if(processModel != null) {
			model.addAttribute("process", SnakerHelper.getModelJson(processModel));
		}

		if(StringUtils.isNotEmpty(orderId)) {
			List<Task> tasks = facets.getEngine().query().getActiveTasks(new QueryFilter().setOrderId(orderId));
			List<HistoryTask> historyTasks = facets.getEngine().query().getHistoryTasks(new QueryFilter().setOrderId(orderId));
			model.addAttribute("state", SnakerHelper.getStateJson(processModel, tasks, historyTasks));
		}

		return "work/districtworkstationSnakerViewList";
	}
}
