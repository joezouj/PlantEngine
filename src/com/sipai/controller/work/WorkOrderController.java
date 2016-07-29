package com.sipai.controller.work;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.snaker.engine.access.Page;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.controller.snaker.OrderController;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkOrder;
import com.sipai.entity.work.WorkScheduling;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.service.process.RealService;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.service.work.WorkSchedulingService;
import com.sipai.service.work.WorkTaskEquipmentService;
import com.sipai.service.work.WorkTaskMaterialService;
import com.sipai.service.work.WorkTaskWorkStationService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workorder")
public class WorkOrderController {
	@Resource
	private WorkOrderService workorderService;
	@Resource
	private WorkSchedulingService workSchedulingService;
	@Resource
	private WorkTaskWorkStationService workTaskWorkStationService;
	@Resource
	private DailyPlanService dailyPlanService;
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Resource
	private UnitService unitService;
	@Resource
	private RealService realService;
	@Resource
	private SnakerEngineFacets facets;
	@Resource
	private WorkTaskMaterialService workTaskMaterialService;
	@Resource
	private WorkTaskEquipmentService workTaskEquipmentService;
	@Resource
	private OrderController orderController;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/workorderList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " eddt ";
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
			wherestr += " and productionorderno like '%"+request.getParameter("search_taskno")+"%' ";
		}
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += " and eddt <= '"+request.getParameter("edt")+"' ";
		}
		PageHelper.startPage(page, rows);
        List<WorkOrder> list = this.workorderService.selecttaskListByWhere(wherestr+orderstr);
        //统计产品数
        List<WorkOrder> list_distinct=new ArrayList<WorkOrder>();
        for(int i=0;i<list.size();i++){
        	WorkOrder workTask=list.get(i);
        	int num=0;
        	for(int j=0;j<list.size();j++){
        		if(workTask.getProductionorderno().equals(list.get(j).getProductionorderno())&&workTask.getEddt().equals(list.get(j).getEddt())){
        			num++;
        			list.remove(j);
        			j--;
        		}
        	}
        	i--;
        	workTask.set_productnum(num);
        	list_distinct.add(workTask);
        }
        PageInfo<WorkOrder> pi = new PageInfo<WorkOrder>(list);
		JSONArray json=JSONArray.fromObject(list_distinct);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_detail.do")
	public ModelAndView getlist_detail(HttpServletRequest request,Model model,
			@RequestParam(value = "productionorderno") String productionorderno,
			@RequestParam(value = "eddt") String eddt,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " productuid ";
		}
		if(order==null){
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/workorder/showlist.do", cu);
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += " and eddt <= '"+request.getParameter("edt")+"' ";
		}
		wherestr +="and productionorderno='"+productionorderno+"' and eddt='"+eddt+"'";
        List<WorkOrder> list = this.workorderService.selectDetailListByWhere(wherestr+orderstr);
        if(list!=null && list.size()>0){
        	for(int i=0;i<list.size();i++){
        		if(list.get(i).getWforderid()!=null){
        			Task task = facets.getActiveTask(list.get(i).getWforderid());
            		if(task.getId()!=null){
            			List<WorkScheduling> userlist = this.workSchedulingService.selectTaskUserListByTaskid(task.getId());
                		String username="";
                		if(userlist!=null && userlist.size()>0){
                			for(int j=0;j<userlist.size();j++){
                				username+=userlist.get(j).get_username()+", ";
                			}
                		}
                		list.get(i).setUsername(username);
            		}
        		}
            }
        	
        }
        PageInfo<WorkOrder> pi = new PageInfo<WorkOrder>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/workorderAdd";
	}
	@RequestMapping("/addbyplan.do")
	public String addbyplan(HttpServletRequest request,Model model,
			@RequestParam(value = "ids", required=false) String ids){
		if(ids==""){
			model.addAttribute("result", 0);
		}else{
			User cu= (User)request.getSession().getAttribute("cu");
			List<WorkOrder> list = this.workorderService.getNewTask("where c.productionOrderno in ('"+ids+"')");//计划表内的所有该下发的任务
			for(int i=0;i<list.size();i++){
				WorkOrder workorder=list.get(i);
				workorder.setId(CommUtil.getUUID());//getNewTask的结果是产品id作为其workorderid,此处更换为uuid【160325】
				this.workorderService.save(workorder);//任务id与TB_material_OrderProductDetail的id不一样
				this.workTaskWorkStationService.saveDefault(workorder,cu,i);//添加默认的工位以及对应的物料、设备
				
				this.orderProductDetailService.updateBySetAndWhere(
						" TB_material_OrderProductDetail set workorderId='"+workorder.getId()+"'  where productUid='"+workorder.getProductuid()+"'");
					
				DailyPlan dailyPlan= dailyPlanService.selectById(workorder.getPlanid());
				dailyPlan.setStatus("2");
				dailyPlan.setTaskchangedstatus("true");
				dailyPlanService.update(dailyPlan);
			}
			if(list!=null &&list.size()>0){
				model.addAttribute("result", 1);
			}else{
				model.addAttribute("result", 0);
			}
		}
		return "result";
	}
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		WorkOrder workorder = this.workorderService.selectById(userId);
		model.addAttribute("workorder", workorder);
		return "work/workorderEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrder workorder){
		User cu= (User)request.getSession().getAttribute("cu");
		
		String id = CommUtil.getUUID();
		workorder.setId(id);
		workorder.setInsuser(cu.getId());
		workorder.setInsdt(CommUtil.nowDate());
		
		int result = this.workorderService.save(workorder);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrder workorder){
		User cu= (User)request.getSession().getAttribute("cu");
		workorder.setUpdateuser(cu.getId());
		workorder.setUpdatedt(CommUtil.nowDate());
		int result = this.workorderService.update(workorder);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workorder.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		//删除相关工位、物料、设备
		this.workTaskWorkStationService.deleteByWhere("where taskid in ('"+id+"')");
		this.workTaskMaterialService.deleteByWhere("where taskid in ('"+id+"')");
		this.workTaskEquipmentService.deleteByWhere("where taskid in ('"+id+"')");
		
		WorkOrder workorder=this.workorderService.selectById(id);
		//更新计划表
		DailyPlan dailyPlan= dailyPlanService.selectById(workorder.getPlanid());
		dailyPlan.setTaskchangedstatus("true");
		dailyPlanService.update(dailyPlan);
		//更新销售订单表
		this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail set workorderId=NULL  where workorderid='"+id+"'");
		
		//删除流程
		if(workorder.getWforderid()!=null){
			orderController.cascadeRemove(request, model, workorder.getWforderid());
		}
		
		int result = this.workorderService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids,
			@RequestParam(value="eddts") String eddts){
		//删除相关工位、物料、设备
		String productionordernos=ids.replace(",","','");
		this.workTaskWorkStationService.deleteByWhere("where taskid in (select id from tb_work_order where productionorderno in ('"+productionordernos+"'))");
		this.workTaskMaterialService.deleteByWhere("where taskid in (select id from tb_work_order where productionorderno in ('"+productionordernos+"'))");
		this.workTaskEquipmentService.deleteByWhere("where taskid in (select id from tb_work_order where productionorderno in ('"+productionordernos+"'))");
		
		String[] idarr=ids.split(",");
		String[] eddtarr=eddts.split(",");
		int result=0;
		for(int i=0;i<idarr.length;i++){
			String iditem=idarr[i];
			String eddtitem=eddtarr[i];
//			OrderProductDetail orderProductDetail= this.orderProductDetailService.selectById(iditem);
//			orderProductDetail.setProductionorderno(null);
			List<WorkOrder> workorder=this.workorderService.selectListByWhere(" where productionorderno='"+iditem+"'");
			DailyPlan dailyPlan= dailyPlanService.selectById(workorder.get(0).getPlanid());
			dailyPlan.setTaskchangedstatus("true");
			dailyPlanService.update(dailyPlan);
			this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail set workorderId=NULL  where workorderid='"+iditem+"'");
			result = this.workorderService.deleteByWhere("where productionorderno ='"+iditem+"' and eddt ='"+eddtitem+"'");
			
			//删除流程
			for(int index=0;index<workorder.size();index++){
				if(workorder.get(index).getWforderid()!=null){
					orderController.cascadeRemove(request, model, workorder.get(index).getWforderid());
				}
			}
			
		}
		ids=ids.replace(",","','");
//		int result = this.workorderService.deleteByWhere("where productionorderno in ('"+ids+"') and eddt ='"+eddts+"'");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getnewtask.do")
	public String getnewtask(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//		String nowdate=df.format(new Date());// new Date()为获取当前系统时间
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		Date date = calendar.getTime();
		String nextdate=df.format(date);// new Date()为获取当前系统时间
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		date = calendar.getTime();
		String lastdate=df.format(date);// new Date()为获取当前系统时间
		List<WorkOrder> list = this.workorderService.getNewTask("where c.stdt >='"+lastdate+"' and c.stdt<'"
		+nextdate+"' and a.productionorderno=c.productionOrderNo and c.status='1' and a.productionorderno is not null and c.delflag!='true'");
		for(int i=0;i<list.size();i++){
			WorkOrder workorder=list.get(i);
			workorder.setId(CommUtil.getUUID());//getNewTask的结果是产品id作为其workorderid,此处更换为uuid【160325】
			WorkOrder workorderold = this.workorderService.selectById(workorder.getId());
			if(workorderold==null){
				this.workorderService.save(workorder);//任务id与产品明细的id不一样
				this.orderProductDetailService.updateBySetAndWhere(
						" TB_material_OrderProductDetail set workorderId='"+workorder.getId()+"'  where id='"+workorder.getId()+"'");
			}else{//更改后此处也没意义了【160325】
				workorder.setUpdateuser(cu.getId());
				workorder.setUpdatedt(CommUtil.nowDate());
				this.workorderService.update(workorder);
				this.orderProductDetailService.updateBySetAndWhere(
						" TB_material_OrderProductDetail set workorderId='"+workorder.getId()+"'  where id='"+workorder.getId()+"'");
			}
			DailyPlan dailyPlan= dailyPlanService.selectById(workorder.getPlanid());
			dailyPlan.setStatus("2");
			dailyPlan.setTaskchangedstatus("true");
			dailyPlanService.update(dailyPlan);
		}
		model.addAttribute("result", list);
		return "result";
	}
	
	/**
	 * 启动流程
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/startorder.do")
	public String start(HttpServletRequest request,Model model, 
			@RequestParam(value="id") String id) {
		User cu= (User)request.getSession().getAttribute("cu");
		int result=0;
		WorkOrder workOrder = this.workorderService.selectById(id);
		if(workOrder !=null && workOrder.getProductid() !=null){
			User user = (User)request.getSession().getAttribute("cu");
			Order order = facets.startInstanceById(realService.getProcessidByRealid(workOrder.getProcessrealid()),user.getId(), null);
			if(order !=null && order.getId() !=null){
				workOrder.setWforderid(order.getId());
				workOrder.setStatus("1");
				workOrder.setPstatus(facets.getActiveTask(order.getId()).getDisplayName());//设置工艺进度
				result = workorderService.update(workOrder);
				
				facets.updateActors(facets.getActiveTask(order.getId()).getId());
			}
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 任务查询列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showtasklist.do")
	public String showtasklist(HttpServletRequest request, Model model) {
		return "/work/workOrderTaskList";
	}

	/**
	 * 全部活动的任务列表获取，注意流程有自己的分页工具page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/gettasklist.do")
	public ModelAndView gettasklist(HttpServletRequest request, Model model) {
		User cu= (User)request.getSession().getAttribute("cu");
		// 页码
		int pages = 0;
		if (request.getParameter("page") != null
				&& !request.getParameter("page").isEmpty()) {
			pages = Integer.parseInt(request.getParameter("page"));
		} else {
			pages = 1;
		}
		// 每页数量
		int pagesize = 0;
		if (request.getParameter("rows") != null
				&& !request.getParameter("rows").isEmpty()) {
			pagesize = Integer.parseInt(request.getParameter("rows"));
		} else {
			pagesize = 20;
		}
	
		Page<Task> page = new Page<Task>();
		page.setPageNo(pages);
		page.setPageSize(pagesize);
		
		String whereStr = " where 1=1 ";
		if(request.getParameter("search_name")!=null&&!request.getParameter("search_name").isEmpty()){
			whereStr += " and task_Name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_displayname")!=null&&!request.getParameter("search_displayname").isEmpty()){
			whereStr += " and display_Name like '%"+request.getParameter("search_displayname")+"%' ";
		}
		//控制任务显示范围，当班人员所属工位
		String sql="select wf_task.* from wf_task left join wf_task_actor on wf_task.id=wf_task_actor.task_id "
				+ "where wf_task_actor.actor_id in "
				+ "(select workstationid from tb_work_scheduling where userid = '"+cu.getId()+"' and workstdt <='"+CommUtil.nowDate()+"' and workeddt >= '"+CommUtil.nowDate()+"') ";
		sql +="or '"+cu.getName()+"'='admin' ";//用于管理员登录时能看到全部
		List<Task> list = facets.getEngine().query().nativeQueryList(page, Task.class,sql);
//		System.out.println(sql);
		String wforderidStr="";
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				wforderidStr+="'"+list.get(i).getOrderId()+"',";
			}
			if(!wforderidStr.equals("")){
				wforderidStr=" and wforderid in ("+wforderidStr.substring(0, wforderidStr.lastIndexOf(","))+") and status <> '0' and status is not null ";
			}
		}else{
			wforderidStr = " and 1<>1 ";
		}
//		System.out.println(whereStr+wforderidStr);
		List<WorkOrder> workOrderlist = this.workorderService.selectActiveTasks(whereStr+wforderidStr);
		JSONArray json = JSONArray.fromObject(workOrderlist);
		String result = "{\"total\":" + page.getTotalCount() + ",\"rows\":" + json + "}";

		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	
	/**
	 * 作废任务
	 * @param request
	 * @param model
	 * @param workorder
	 * @return
	 */
	@RequestMapping("/discard.do")
	public String discard(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrder workorder){
		User cu= (User)request.getSession().getAttribute("cu");
		workorder.setStatus("0");
		workorder.setUpdateuser(cu.getId());
		workorder.setUpdatedt(CommUtil.nowDate());
		int result = this.workorderService.update(workorder);
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 切换产线查询列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showswitchlinelist.do")
	public String showswitchlinelist(HttpServletRequest request, Model model) {
		WorkOrder workOrder= this.workorderService.selectById(request.getParameter("id"));
		if(workOrder!=null){
			model.addAttribute("workOrder", workOrder);
		}
		return "/work/workOrderSwitchLineList";
	}
	
	@RequestMapping("/updateswitchline.do")
	public String updateswitchline(HttpServletRequest request,Model model,
			@ModelAttribute WorkOrder workorder,
			@RequestParam(value="id") String id,
			@RequestParam(value="lineid") String lineid){
		User cu= (User)request.getSession().getAttribute("cu");
		
		workorder.setId(id);
		workorder.setLineid(lineid);
		workorder.setUpdateuser(cu.getId());
		workorder.setUpdatedt(CommUtil.nowDate());
		
		int result = this.workorderService.update(workorder);
		
		if(result>0){
			WorkOrder workorder1= this.workorderService.selectById(id);
			//获取所有未执行的task_workstation数据
			List<WorkTaskWorkStation> list = this.workTaskWorkStationService.getFutureTask(workorder1);
			String ids="";
			String workstationids="";
			String processrealdetailids="";
			for(int i=0;i<list.size();i++){
				ids +=list.get(i).getId()+",";
				workstationids +=list.get(i).getWorkstationid()+",";
				processrealdetailids +=list.get(i).getProcessRealdetailid()+",";
			}
			if(!ids.equals("")){
				String idscope=ids.replace(",","','");
				String workstationidscope=workstationids.replace(",","','");
				String processrealdetailidscope=processrealdetailids.replace(",","','");
				//删除所有未执行的task_workstation及相关数据
				this.workTaskWorkStationService.deleteByWhere("where id in ('"+idscope+"')");
				this.workTaskMaterialService.deleteByWhere("where workstationid in ('"+workstationidscope+"') and processrealdetailid in ('"+processrealdetailidscope+"') and taskid='"+workorder1.getId()+"'");
				this.workTaskEquipmentService.deleteByWhere("where workstationid in ('"+workstationidscope+"') and processrealdetailid in ('"+processrealdetailidscope+"') and taskid='"+workorder1.getId()+"'");
				
				//更新当前的snaker_actor数据
				this.facets.updateActorsByOrderId(workorder1.getWforderid());
			}
		}
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
//		System.out.println(resstr);
		model.addAttribute("result", resstr);
		return "result";
	}
}
