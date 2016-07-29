package com.sipai.controller.plan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.sipai.entity.material.OrderProductDetail;
import com.sipai.entity.material.OrderProductDetailconnect;
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.plan.DailyPlanSummary;
import com.sipai.entity.process.Real;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkOrder;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.material.OrderProductDetailconnectService;
import com.sipai.service.material.SalesOrderProductService;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.service.plan.DailyPlanSummaryService;
import com.sipai.service.process.RealService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/plan/dailyplan")
public class DailyPlanController {
	@Resource
	private DailyPlanService dailyPlanService;
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Resource
	private OrderProductDetailconnectService orderProductDetailconnectService;
	@Resource
	private SalesOrderProductService salesOrderProductService;
	@Resource
	private WorkOrderService workOrderService;
	@Resource
	private RealService realService;
	@Resource
	private DailyPlanSummaryService dailyPlanSummaryService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		request.setAttribute("wsdt", request.getParameter("sdt"));
		request.setAttribute("wedt", request.getParameter("edt"));
		return "/plan/dailyPlanList";
	}	
	@RequestMapping("/showcalendar.do")
	public String showcalendar(HttpServletRequest request,Model model) {
		return "/plan/dailyPlanCalendar";
	}
	@RequestMapping("/showcalendaroperate.do")
	public String showcalendaroperate(HttpServletRequest request,Model model) {
		return "/plan/dailyPlanCalendarOperate";
	}
	@RequestMapping("/showDaylist.do")
	public String showDaylist(HttpServletRequest request,Model model) {
		request.setAttribute("wsdt", request.getParameter("sdt"));
		request.setAttribute("wedt", request.getParameter("edt"));
		return "/plan/planDayList";
	}
	@RequestMapping("/getdailyplans.do")
	public ModelAndView getDailyPlans(HttpServletRequest request,Model model) {
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
			sort = " P.porder asc , P.status asc ,P.insdt   ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("P.insuser", "plan/dailyplan/showlist.do", cu);
		wherestr += " and P.delflag!='true' ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
		}
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += "and P.pid= '"+request.getParameter("pid")+"' ";
		}
		
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){
			switch(request.getParameter("search_status")){
				case "edit": wherestr += "and P.status= '0' ";break;
				case "submit": wherestr += "and P.status= '1' ";break;
				case "audited": wherestr += "and P.status= '5' ";break;
				case "inprogress": wherestr += "and P.status= '2' ";break;
				case "completed": wherestr += "and P.status= '6' ";break;
				case "rearrenged": wherestr += "and P.status= '4' ";break;
				case "invalid": wherestr += "and P.status= '3' ";break;
			default: break;
		   }
		}
		if(request.getParameter("notdisplayinvalid")!=null && request.getParameter("notdisplayinvalid").equals("1")){
			wherestr +="and P.status!='3' and P.status!='4' ";
		}
		PageHelper.startPage(pages, pagesize);
        List<DailyPlan> list = this.dailyPlanService.getDailyPlanlist(wherestr+orderstr);        
        PageInfo<DailyPlan> page = new PageInfo<DailyPlan>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		String date=request.getParameter("date");
		String orderno="";
		if(date!=null && !date.equals("")){
			orderno=CommUtil.getAutoID("PL",3,"productionOrderNo","tb_plan_day",date,"D","");
			model.addAttribute("nowDate",date);
			String nowdateend=date.substring(0, 10)+" 23:59:59";
			model.addAttribute("tomDate",nowdateend);
		}else{
			orderno=CommUtil.getAutoID("PL",3,"productionOrderNo","tb_plan_day",CommUtil.nowDate(),"D","");
			model.addAttribute("nowDate",CommUtil.nowDate());
			String nowdateend=CommUtil.nowDate().substring(0, 10)+" 23:59:59";
			model.addAttribute("tomDate",nowdateend);
		}		
		model.addAttribute("orderno", orderno);	
		
		return "/plan/dailyPlanAdd";
	}
	/*日计划状态：
	 * 0编辑待提交，1提交待审核，5审核待下发，2下发，3作废，4重排，6完成*/
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("DailyPlan") DailyPlan dailyPlan,
			@RequestParam(value="pdtotalnum") String pdtotalnumstr,
			@RequestParam(value="sysarrangenum") String sysarrangenumstr,
			@RequestParam(value="pdplanednum") String pdplanednumstr,
			@RequestParam(value="capacity") String capacitystr,
			@RequestParam(value="selectedProducts") String selectedProducts){
		User cu= (User)request.getSession().getAttribute("cu");
		int pdtotalnum= Integer.parseInt(pdtotalnumstr);
		int pdplanednum= Integer.parseInt(pdplanednumstr);
		int planpdq=Integer.parseInt(dailyPlan.getProductquantity());
		int sysarrangenum=Integer.parseInt(sysarrangenumstr);//排产总数=系统分配数量+手选已排计划产品数
		int capacity=0;
		if(capacitystr!=null && !capacitystr.equals("")){//防止抛出异常
			capacity=Integer.parseInt(capacitystr);
		}		
		String id = CommUtil.getUUID();
		String resstr="";
		int result=0;
		if(sysarrangenum>pdtotalnum-pdplanednum){//系统分配数量不允许超过未排数
			resstr="{\"res\":\"日计划排产数量不允许超过销售订单产品剩余未排数。\",\"id\":\""+id+"\"}";
		}else if(capacity>0 && planpdq>capacity){//排产总数不允许超过产能
			resstr="{\"res\":\"日计划排产数量不允许超过产线产能。\",\"id\":\""+id+"\"}";
		}else{
			String porder="0";
			List<DailyPlan> listmaxp=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlan.getPid()+"' order by porder desc");
			if( listmaxp!=null&&!listmaxp.isEmpty()){
				String maxporder=listmaxp.get(0).getPorder();
				porder=""+(Integer.parseInt(maxporder)+1);
			}			
			dailyPlan.setId(id);
			dailyPlan.setInsuser(cu.getId());
			dailyPlan.setInsdt(CommUtil.nowDate());
			dailyPlan.setDelflag("false");
			dailyPlan.setTaskchangedstatus("false");
			dailyPlan.setPorder(porder);
			result = this.dailyPlanService.save(dailyPlan);
			DailyPlanSummary dailyPlanSummary=new DailyPlanSummary();//新增计划时把计划单状态改为未审核
			dailyPlanSummary.setId(dailyPlan.getPid());
			dailyPlanSummary.setStatus("0");
			this.dailyPlanSummaryService.update(dailyPlanSummary);
			if(result==1){//更改产品明细计划状态，绑定生产订单id
				this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
						+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',processrealid = '"+dailyPlan.getProcessrealid()+"' "
						+ " from ( select top "+sysarrangenum+" * from TB_material_OrderProductDetail where pid='"+dailyPlan.getSalesorderid()+"' and productionorderno is NULL order by productUId asc ) as D"
						+ " where D.id=TB_material_OrderProductDetail.id ");
				//计划和产品的对应关系
				List<OrderProductDetail> listopd=this.orderProductDetailService.selectListByWhere(" where productionorderno='"+dailyPlan.getProductionorderno()+"' order by productUId asc ");
				for(OrderProductDetail opd:listopd){
					OrderProductDetailconnect opdc=new OrderProductDetailconnect();
					opdc.setId(CommUtil.getUUID());
					opdc.setInsdt(CommUtil.nowDate());
					opdc.setInsuser(cu.getId());
					opdc.setProductdetailid(opd.getId());
					opdc.setPlanid(dailyPlan.getId());
					this.orderProductDetailconnectService.save(opdc);
				}
				resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
			}else if(result==2){
				resstr="{\"res\":\"生产订单号重复。\",\"id\":\""+id+"\"}";
			}else{
				resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
			}
		}		
		//若含已排计划产品，保存成功后：作废已排计划产品的计划/任务（工单），绑定新计划
		if(result==1&&selectedProducts!=null &&!selectedProducts.equals("")){
			JSONArray sparr = JSONArray.fromObject("["+selectedProducts+"]");
			for(int i=0;i<sparr.size();i++){
				    Map o=(Map)sparr.get(i);
				    //System.out.println(o.get("productuid")+" "+o.get("productionorderno"));
				    //1、作废已排计划产品的计划,status=4计划重排
				    	//DailyPlan dailyplan=this.dailyPlanService.selectListByWhere("where productionorderno='"+o.get("productionorderno")+"'order by id").get(0);
				    String setandwhere=" set status='4' where productionorderno='"+o.get("productionorderno")+"'";				
				    this.dailyPlanService.updateBySetAndWhere(setandwhere);
				    //2、任务（工单）作废:status=0					
					List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+o.get("productionorderno")+"' and productUId='"+o.get("productuid")+"'");
					for(int w=0;w<wtlist.size();w++){
						WorkOrder workorder=wtlist.get(w);
						workorder.setStatus("0");
						this.workOrderService.update(workorder);
					}
					//3、绑定新计划status=8 重排状态
					this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
							+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',status='8' "
							+ " where  productUId='"+o.get("productuid")+"'");
					//4、计划和产品的对应关系
					OrderProductDetailconnect opdc2=new OrderProductDetailconnect();
					opdc2.setId(CommUtil.getUUID());
					opdc2.setInsdt(CommUtil.nowDate());
					opdc2.setInsuser(cu.getId());
					opdc2.setProductdetailid((String) o.get("id"));
					opdc2.setPlanid(dailyPlan.getId());
					this.orderProductDetailconnectService.save(opdc2);
			}
		}		
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DailyPlan dailyplan = this.dailyPlanService.getDailyPlan(" where P.id='"+id+"' order by P.insdt").get(0);		
		model.addAttribute("dailyplan", dailyplan);
		//得到提示数目		
		//String productinfo=salesorderproduct.getProduct().getSpec()+salesorderproduct.getProduct().getMaterialname();
		List<OrderProductDetail> listopd=this.orderProductDetailService.getOrderProductDetail(" where D.pid='"+dailyplan.getSalesorderid()+"' order by D.productUId");
		int pdtotalnum=listopd.size();
		int pdplanednum=0;
		int finishednum=0;
		for(int i=0;i<pdtotalnum;i++){
			if(listopd.get(i).getProductionorderno()!=null && !listopd.get(i).getProductionorderno().isEmpty()){
				pdplanednum++;
			}
			if(listopd.get(i).getWorkorderid()!=null&&listopd.get(i).getWorkorderid()!=""){//已排任务
				if(listopd.get(i).getFinishflag_wfo()=="0"){//产品已完成
					finishednum++;
				}							
			}
		}		
		int remainednum=pdtotalnum-pdplanednum;
		DailyPlanSummary dps=this.dailyPlanSummaryService.selectById(dailyplan.getPid());
		if(dps!=null){
			model.addAttribute("plandt", dps.getPlandt());
		}		
		model.addAttribute("pdtotalnum", pdtotalnum);
		model.addAttribute("pdplanednum", pdplanednum);
		model.addAttribute("remainednum", remainednum);
		model.addAttribute("finishednum", finishednum);		
		//model.addAttribute("productinfo", productinfo);
		return "/plan/dailyPlanEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("DailyPlan") DailyPlan dailyPlan,
			@RequestParam(value="pdtotalnum") String pdtotalnumstr,
			@RequestParam(value="sysarrangenum") String sysarrangenumstr,
			@RequestParam(value="pdplanednum") String pdplanednumstr,
			@RequestParam(value="capacity") String capacitystr,
			@RequestParam(value="selectedProducts") String selectedProducts){
		User cu= (User)request.getSession().getAttribute("cu");			
		
		String pqOld=this.dailyPlanService.selectById(dailyPlan.getId()).getProductquantity();
		int pdtotalnum= Integer.parseInt(pdtotalnumstr);
		int pdplanednum= Integer.parseInt(pdplanednumstr);
		int planpdq=Integer.parseInt(dailyPlan.getProductquantity());
		int pqOldnum=Integer.parseInt(pqOld);
		int sysarrangenum=Integer.parseInt(sysarrangenumstr);//排产总数=系统分配数量+手选已排计划产品数
		int unplaned=pdtotalnum-pdplanednum+pqOldnum;		
		int capacity=0;
		if(capacitystr!=null && !capacitystr.equals("")){
			capacity=Integer.parseInt(capacitystr);
		}
		String resstr="";
		int result=0;
		if(dailyPlan.getStatus()!=null&&dailyPlan.getStatus().equals("3")){//作废，与invalidate()相似，省去了日计划重复update
			this.dailyPlanService.recoverproductdetail(dailyPlan.getId());
			//任务作废:status=0
			List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+dailyPlan.getProductionorderno()+"'");
			for(int w=0;w<wtlist.size();w++){
				WorkOrder workorder=wtlist.get(w);
				workorder.setStatus("0");
				this.workOrderService.update(workorder);
			}
			
		}else if(sysarrangenum>unplaned){//系统分配数量不允许超过未排数
			resstr="{\"res\":\"日计划排产数量不允许超过销售订单产品剩余未排数。\",\"id\":\""+dailyPlan.getId()+"\"}";
		}else if(capacity>0 && planpdq>capacity){//排产总数不允许超过产能
			resstr="{\"res\":\"日计划排产数量不允许超过产线产能。\",\"id\":\""+dailyPlan.getId()+"\"}";
		}else{		
			dailyPlan.setInsuser(cu.getId());
			dailyPlan.setInsdt(CommUtil.nowDate());				
			dailyPlan.setDelflag("false");	
			dailyPlan.setTaskchangedstatus("false");
			result = this.dailyPlanService.update(dailyPlan);
			if(result==1){
				/*//更改产品明细计划状态，绑定生产订单id
				//先还原系统分配的产品
				this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
						+ "set productionorderno=NULL,processrealid=NULL "
						+ "where pid='"+dailyPlan.getSalesorderid()+"' and productionorderno='"+dailyPlan.getProductionorderno()+"'");
				this.orderProductDetailconnectService.deleteByWhere(" where planid='"+dailyPlan.getId()+"' ");
				//按照修改后的数量重新分配产品
				this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
						+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',processrealid = '"+dailyPlan.getProcessrealid()+"' "
						+ " from ( select top "+sysarrangenum+" * from TB_material_OrderProductDetail where pid='"+dailyPlan.getSalesorderid()+"' and productionorderno is NULL order by productUId asc ) as D"
						+ " where D.id=TB_material_OrderProductDetail.id ");
				//计划和产品的对应关系
				List<OrderProductDetail> listopd=this.orderProductDetailService.selectListByWhere(" where productionorderno='"+dailyPlan.getProductionorderno()+"' order by productUId asc ");
				//System.out.println("---"+listopd.size());
				for(OrderProductDetail opd:listopd){
					OrderProductDetailconnect opdc=new OrderProductDetailconnect();
					opdc.setId(CommUtil.getUUID());
					opdc.setInsdt(CommUtil.nowDate());
					opdc.setInsuser(cu.getId());
					opdc.setProductdetailid(opd.getId());
					opdc.setPlanid(dailyPlan.getId());
					this.orderProductDetailconnectService.save(opdc);
				}
				resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlan.getId()+"\"}";
				//若含已排计划产品，保存成功后：作废已排计划产品的计划/任务（工单），绑定新计划
				if(selectedProducts!=null &&!selectedProducts.equals("")){
					JSONArray sparr = JSONArray.fromObject("["+selectedProducts+"]");
					for(int i=0;i<sparr.size();i++){
						    Map o=(Map)sparr.get(i);
						    System.out.println(o.get("productuid")+" "+o.get("productionorderno"));
						    //1、作废已排计划产品的计划,status=4计划重排
						    	//DailyPlan dailyplan=this.dailyPlanService.selectListByWhere("where productionorderno='"+o.get("productionorderno")+"'order by id").get(0);
						    String setandwhere=" set status='4' where productionorderno='"+o.get("productionorderno")+"'";				
						    this.dailyPlanService.updateBySetAndWhere(setandwhere);
						    //2、任务（工单）作废:status=0					
							List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+o.get("productionorderno")+"' and productUId='"+o.get("productuid")+"'");
							for(int w=0;w<wtlist.size();w++){
								WorkOrder workorder=wtlist.get(w);
								workorder.setStatus("0");
								this.workOrderService.update(workorder);
							}
							//3、绑定新计划
							this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
									+ " set productionorderno='"+dailyPlan.getProductionorderno()+"' "
									+ " where  productUId='"+o.get("productuid")+"'");
							//4、计划和产品的对应关系
							OrderProductDetailconnect opdc2=new OrderProductDetailconnect();
							opdc2.setId(CommUtil.getUUID());
							opdc2.setInsdt(CommUtil.nowDate());
							opdc2.setInsuser(cu.getId());
							opdc2.setProductdetailid((String) o.get("id"));
							opdc2.setPlanid(dailyPlan.getId());
							this.orderProductDetailconnectService.save(opdc2);
					}
				}*/
				resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlan.getId()+"\"}";				
				//0、先判断selectedProducts是否空
				if(selectedProducts!=null &&!selectedProducts.equals("")){
					//1、先删除旧的关联信息，按新修改的重新保存（旧产品明细生产订单号字段，对应关系表记录）
					//还原系统分配的产品
					this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
							+ "set productionorderno=NULL,processrealid=NULL "
							+ "where pid='"+dailyPlan.getSalesorderid()+"' and productionorderno='"+dailyPlan.getProductionorderno()+"'");
					this.orderProductDetailconnectService.deleteByWhere(" where planid='"+dailyPlan.getId()+"' ");
					JSONArray sparr = JSONArray.fromObject("["+selectedProducts+"]");
					for(int i=0;i<sparr.size();i++){
						    Map o=(Map)sparr.get(i);
						    //2、再判断明细，是否未排计划/已排计划
						    //未排计划，则绑定生产订单号，生成对应关系表记录
						    if(o.get("productionorderno")==null || o.get("productionorderno").equals("")){
						    	//绑定产品
								this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
										+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',processrealid = '"+dailyPlan.getProcessrealid()+"' "
										+ " from TB_material_OrderProductDetail "
										+ " where productUId='"+o.get("productuid")+"'");
								//计划和产品的对应关系								
								OrderProductDetailconnect opdc=new OrderProductDetailconnect();
								opdc.setId(CommUtil.getUUID());
								opdc.setInsdt(CommUtil.nowDate());
								opdc.setInsuser(cu.getId());
								opdc.setProductdetailid((String) o.get("id"));
								opdc.setPlanid(dailyPlan.getId());
								this.orderProductDetailconnectService.save(opdc);							
						    	
						    }else{
						    	//3、已排计划，先判断生产订单号是不是本订单，是没改过的,绑定生产订单号,status=8，生成对应关系表记录(不作废)
						    	if(o.get("productionorderno").equals(dailyPlan.getProductionorderno())){						    		
						    		//(i)绑定产品
									this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
											+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',processrealid = '"+dailyPlan.getProcessrealid()+"' "
											+ " from TB_material_OrderProductDetail "
											+ " where productUId='"+o.get("productuid")+"'");
									//(ii)计划和产品的对应关系								
									OrderProductDetailconnect opdc=new OrderProductDetailconnect();
									opdc.setId(CommUtil.getUUID());
									opdc.setInsdt(CommUtil.nowDate());
									opdc.setInsuser(cu.getId());
									opdc.setProductdetailid((String) o.get("id"));
									opdc.setPlanid(dailyPlan.getId());
									this.orderProductDetailconnectService.save(opdc);
						    	}else{
						    		//3、已排计划，则作废原计划工单，绑定生产订单号,status=8，生成对应关系表记录	
							    	//(i)作废已排计划产品的计划,status=4计划重排
							    	//DailyPlan dailyplan=this.dailyPlanService.selectListByWhere("where productionorderno='"+o.get("productionorderno")+"'order by id").get(0);
								    String setandwhere=" set status='4' where productionorderno='"+o.get("productionorderno")+"'";				
								    this.dailyPlanService.updateBySetAndWhere(setandwhere);
								    //(ii)任务（工单）作废:status=0					
									List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+o.get("productionorderno")+"' and productUId='"+o.get("productuid")+"'");
									for(int w=0;w<wtlist.size();w++){
										WorkOrder workorder=wtlist.get(w);
										workorder.setStatus("0");
										this.workOrderService.update(workorder);
									}
						    		//(iii)绑定产品
									this.orderProductDetailService.updateBySetAndWhere(" TB_material_OrderProductDetail "
											+ " set productionorderno='"+dailyPlan.getProductionorderno()+"',processrealid = '"+dailyPlan.getProcessrealid()+"',status='8' "
											+ " from TB_material_OrderProductDetail "
											+ " where productUId='"+o.get("productuid")+"'");
									//(iv)计划和产品的对应关系								
									OrderProductDetailconnect opdc=new OrderProductDetailconnect();
									opdc.setId(CommUtil.getUUID());
									opdc.setInsdt(CommUtil.nowDate());
									opdc.setInsuser(cu.getId());
									opdc.setProductdetailid((String) o.get("id"));
									opdc.setPlanid(dailyPlan.getId());
									this.orderProductDetailconnectService.save(opdc);
						    	}
						    }
					}
				}
				
			}else if(result==2){
				resstr="{\"res\":\"生产订单号重复。\",\"id\":\""+dailyPlan.getId()+"\"}";
			}else{
				resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlan.getId()+"\"}";
			}			
		}		
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/dofinish.do")
	public String dofinish(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		if(status!=null && status.equals("3")){
			String result0="{\"res\":\"计划已作废，不可更改。\"}";
			model.addAttribute("result", result0);
			return "result";
		}else{
			DailyPlan dailyplan=new DailyPlan(); 
			dailyplan.setId(id);
			dailyplan.setStatus("6");
			int result=this.dailyPlanService.update(dailyplan);
			model.addAttribute("result", result);
			return "result";
		}
		
	}
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DailyPlan dailyplan = this.dailyPlanService.getDailyPlan(" where P.id='"+id+"' order by P.insdt").get(0);		
		model.addAttribute("dailyplan", dailyplan);
		dailyplan.setTaskchangedstatus("false");
		//更新提示消息为空
		this.dailyPlanService.update(dailyplan);
		//得到提示数目
		List<OrderProductDetail> listopd=this.orderProductDetailService.getOrderProductDetail(" where D.pid='"+dailyplan.getSalesorderid()+"' order by D.productUId");
		int pdtotalnum=listopd.size();
		int pdplanednum=0;
		int finishednum=0;
		for(int i=0;i<pdtotalnum;i++){
			if(listopd.get(i).getProductionorderno()!=null && !listopd.get(i).getProductionorderno().isEmpty()){
				pdplanednum++;
			}
			if(listopd.get(i).getWorkorderid()!=null&&listopd.get(i).getWorkorderid()!=""){//已排任务
				if(listopd.get(i).getFinishflag_wfo()=="0"){//产品已完成
					finishednum++;
				}							
			}
		}
		int remainednum=pdtotalnum-pdplanednum;
		String whereStr="";		
		whereStr = "where id = '"+dailyplan.getProcessrealid()+"'";		
        List<Real> list = this.realService.selectListByWhere(whereStr);
        String processrealname="";
        if(!list.isEmpty()){
        	processrealname=list.get(0).getName();
        }else{
        	processrealname=dailyplan.getProcessrealid();
        }
		DailyPlanSummary dps=this.dailyPlanSummaryService.selectById(dailyplan.getPid());
		if(dps!=null){
			model.addAttribute("plandt", dps.getPlandt());
		}		
		model.addAttribute("pdtotalnum", pdtotalnum);
		model.addAttribute("pdplanednum", pdplanednum);
		model.addAttribute("remainednum", remainednum);	
		model.addAttribute("finishednum", finishednum);
		model.addAttribute("processrealname", processrealname);
		return "/plan/dailyPlanView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		String setandwhere=" set delflag='TRUE',status='3' where id='"+id+"'";
		int result = this.dailyPlanService.updateBySetAndWhere(setandwhere);
		model.addAttribute("result", result);
		this.dailyPlanService.recoverproductdetail(id);//作废，与invalidate()相似
		//任务作废:status=0
		DailyPlan dailyPlan=this.dailyPlanService.selectById(id);	
		List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+dailyPlan.getProductionorderno()+"'");
		for(int w=0;w<wtlist.size();w++){
			WorkOrder workorder=wtlist.get(w);
			workorder.setStatus("0");
			this.workOrderService.update(workorder);
		}
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){		
		ids=ids.replace(",","','");
		String setandwhere=" set delflag='TRUE',status='3' where id in ('"+ids+"')";
		int result = this.dailyPlanService.updateBySetAndWhere(setandwhere);		
		model.addAttribute("result", result);
		List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where id in ('"+ids+"')");
		for(int i=0;i<dplist.size();i++){//作废，与invalidate()相似
			this.dailyPlanService.recoverproductdetail(dplist.get(i).getId());//还原产品状态
			//任务作废:status=0						
			List<WorkOrder> wtlist =this.workOrderService.selectListByWhere(" where productionOrderNo='"+dplist.get(i).getProductionorderno()+"'");
			for(int w=0;w<wtlist.size();w++){
				WorkOrder workorder=wtlist.get(w);
				workorder.setStatus("0");
				this.workOrderService.update(workorder);
			}			
		}
		return "result";
	}
	
	@RequestMapping("/invalidate.do")
	public String invalidate(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){		
		int result = this.dailyPlanService.invalidate(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 甘特图
	 */
	@RequestMapping("/gantt.do")
	public String showlistGantt(HttpServletRequest request,Model model) {
		String orderstr=" order by P.stdt asc";
		
		String wherestr = " where P.delflag <> 'true' and P.stdt>'1900-01-01'";
		if(request.getParameter("search_stdt1")!=null && !request.getParameter("search_stdt1").isEmpty()){
			wherestr += "and P.stdt >= '"+request.getParameter("search_stdt1")+"' ";
		}
		if(request.getParameter("search_stdt2")!=null && !request.getParameter("search_stdt2").isEmpty()){
			wherestr += "and P.stdt <= '"+request.getParameter("search_stdt2")+"' ";
		}
		
        List<DailyPlan> list = this.dailyPlanService.getDailyPlan(wherestr+orderstr);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		if(list !=null && list.size()>0){
			String jsonStr="";
			for(int i=0;i<list.size();i++){
				String startDate="";
				String startDate1="";
				if(list.get(i).getStdt()!=null){
					try {
						startDate=sdf.format(DateFormat.getDateInstance().parse(list.get(i).getStdt()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startDate1=String.valueOf(new Date(startDate).getTime());
				}
				String finishDate="";
				String finishDate1="";
				String customClass="";
				if(list.get(i).getEddt()!=null){
					try {
						finishDate=sdf.format(DateFormat.getDateInstance().parse(list.get(i).getEddt()));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finishDate1=String.valueOf(new Date(finishDate).getTime());
					
					//如果当前时间超过计划完成时间，显示红色
					if(CommUtil.compare_date(CommUtil.nowDate(),finishDate.replace("/", "-"))==1){
						customClass="ganttRed";
					}
				}
				
				jsonStr+="{name:\""+list.get(i).getProductionorderno()+"\",desc:\""+list.get(i).getProductname()+"\","
						+ "values:[{from:\""+startDate1+"\",to:\""+finishDate1+"\","
						+ "desc:\""+startDate+" - "+finishDate+"\",customClass:\""+customClass+"\"}]},";
			}
			jsonStr="["+jsonStr.substring(0, jsonStr.length()-1)+"]";
			//System.out.println(jsonStr);
			
			request.setAttribute("result", jsonStr);
		}
		
		list=null;
		
		return "/plan/dailyPlanGantt";
	}
	@RequestMapping("/showlist_task.do")
	public String showlist_task(HttpServletRequest request,Model model) {
		return "/plan/dailyPlanList_task";
	}	
	@RequestMapping("/getdailyplans_task.do")
	public ModelAndView getDailyPlans_task(HttpServletRequest request,Model model) {
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
			sort = " P.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("P.insuser", "plan/dailyplan/showlist.do", cu);
		wherestr += " and P.delflag!='true' and P.status!='3' and P.status!='0'";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(pages, pagesize);
        List<DailyPlan> list = this.dailyPlanService.getDailyPlanlist(wherestr+orderstr);
        
        PageInfo<DailyPlan> page = new PageInfo<DailyPlan>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	/*
	 * view浏览界面grid显示产品序列号
	 * */
	@RequestMapping("/getPDetails.do")
	public String getPDetails(HttpServletRequest request,Model model,@RequestParam(value="pid") String pid){
		DailyPlan dailyplan = this.dailyPlanService.getDailyPlan(" where P.id='"+pid+"' order by D.ProductUId asc").get(0);
//		List<SalesOrderProduct> listsop = this.salesOrderProductService.getSalesOrderProduct(" where P.id='"+dailyplan.getSalesorderid()+"' and D.productionorderno='"+dailyplan.getProductionorderno()+"' order by D.productUId");	
//		String result="";
//		if(!listsop.isEmpty()){
//			SalesOrderProduct salesorderproduct=listsop.get(0);
//			List<OrderProductDetail> list=salesorderproduct.getOrderproductdetail();
//			JSONArray json=JSONArray.fromObject(list);
//			result ="{\"rows\":"+json+"}";
//		}
		List<OrderProductDetail> list=dailyplan.getOrderproductdetail();
		String ids="";
		for(int i=0;i<list.size();i++){
			ids+=list.get(i).getId()+"','";
		}
		List<OrderProductDetail> listopd=this.orderProductDetailService.getOrderProductDetail(" where D.id in('"+ids+"') order by D.productUId");
		JSONArray json=JSONArray.fromObject(listopd);
		String result ="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
	    return "result";
	}
	//list页面树形菜单
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " S.OrderFinishDate ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String wherestr =  "where  S.id in(select distinct salesOrderID from tb_plan_day ";
		wherestr +=CommUtil.getwherestr("insuser", "plan/dailyplan/showlist.do", cu);
		wherestr +=" and delflag!='true' ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}else if(request.getParameter("search_nameAll")!=null && !request.getParameter("search_nameAll").isEmpty()){
			wherestr += "and productionOrderNo like '%"+request.getParameter("search_nameAll")+"%' ";
		}		
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += "and stdt>= '"+request.getParameter("sdt")+"' ";
		}else if(request.getParameter("sdtAll")!=null && !request.getParameter("sdtAll").isEmpty()){
			wherestr += "and stdt>= '"+request.getParameter("sdtAll")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += "and stdt<= '"+request.getParameter("edt")+"' ";
		}else if(request.getParameter("edtAll")!=null && !request.getParameter("edtAll").isEmpty()){
			wherestr += "and stdt<= '"+request.getParameter("edtAll")+"' ";
		}
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){//属于该日计划汇总
			wherestr += "and pid= '"+request.getParameter("pid")+"' ";
		}
		
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
			switch(request.getParameter("search_status")){
				case "edit": wherestr += "and status= '0' ";break;
				case "submit": wherestr += "and status= '1' ";break;
				case "audited": wherestr += "and status= '5' ";break;
				case "inprogress": wherestr += "and status= '2' ";break;
				case "completed": wherestr += "and status= '6' ";break;
				case "rearrenged": wherestr += "and status= '4' ";break;
				case "invalid": wherestr += "and status= '3' ";break;
				default: break;
			}
		}else if(request.getParameter("search_statusAll")!=null && !request.getParameter("search_statusAll").isEmpty()){			
			switch(request.getParameter("search_statusAll")){
				case "edit": wherestr += "and status= '0' ";break;
				case "submit": wherestr += "and status= '1' ";break;
				case "audited": wherestr += "and status= '5' ";break;
				case "inprogress": wherestr += "and status= '2' ";break;
				case "completed": wherestr += "and status= '6' ";break;
				case "rearrenged": wherestr += "and status= '4' ";break;
				case "invalid": wherestr += "and status= '3' ";break;
				default: break;
			}
		}
		wherestr += ") ";	
		PageHelper.startPage(page, rows);
        List<SalesOrderProduct> list = this.dailyPlanService.getSOPByPlan(wherestr+orderstr);
	    for(SalesOrderProduct sop:list){//为了统计完成、计划数，产品列表塞入对象
	    	List<OrderProductDetail> listopd=this.orderProductDetailService.getOrderProductDetail(" where D.pid='"+sop.getId()+"' order by D.productUId");
	    	sop.setOrderproductdetail(listopd);        	
	    }	    
		JSONArray json=JSONArray.fromObject(list);
		PageInfo<SalesOrderProduct> pi = new PageInfo<SalesOrderProduct>(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_detail.do")
	public ModelAndView getlist_detail(HttpServletRequest request,Model model,
			@RequestParam(value = "salesOrderID") String salesOrderID,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " P.status asc ,P.insdt  ";
		}
		if(order==null){
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("P.insuser", "plan/dailyplan/showlist.do", cu);
		wherestr += " and P.delflag!='true' ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
		
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
		}
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += "and P.pid= '"+request.getParameter("pid")+"' ";
		}
		
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
			switch(request.getParameter("search_status")){
				case "edit": wherestr += "and P.status= '0' ";break;
				case "submit": wherestr += "and P.status= '1' ";break;
				case "audited": wherestr += "and P.status= '5' ";break;
				case "inprogress": wherestr += "and P.status= '2' ";break;
				case "completed": wherestr += "and P.status= '6' ";break;
				case "rearrenged": wherestr += "and P.status= '4' ";break;
				case "invalid": wherestr += "and P.status= '3' ";break;
			default: break;
		}
		}
		wherestr+=" and P.salesOrderID='"+salesOrderID+"'";
        List<DailyPlan> list = this.dailyPlanService.getDailyPlanlist(wherestr+orderstr);
/*        //筛选当前用户可查看内容
        for(int i=0;i<list.size();i++){
        	if(cu.getId().equals("emp01")){
        		break;
        	}
        	if (list.get(i).getWorkscheduling()==null ||list.get(i).getWorkscheduling().size()==0) {
        		list.remove(i);
        		i--;
        		continue;
			}
        	int j=0;
        	for(j=0;j<list.get(i).getWorkscheduling().size();j++){
	        	if(list.get(i).getWorkscheduling().get(j).getUserid()!=null && list.get(i).getWorkscheduling().get(j).getUserid().equals(cu.getId())){
	        		break;
	        	}else{
	        		continue;
	        	}
	        	
        	}
        	if(j==list.get(i).getWorkscheduling().size()){
        		list.remove(i);
        		i--;
        	}
        }*/
        PageInfo<DailyPlan> pi = new PageInfo<DailyPlan>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/drag.do")
	public String dodragsave(HttpServletRequest request,Model model){
		String order_n0 = request.getParameter("order_n0");
		String order_n = request.getParameter("order_n");
		String pid = request.getParameter("pid");
		String id = request.getParameter("id");
		int res=0;
		if(order_n!=null && !order_n.equals("") && order_n!=null && !order_n.equals("")){
			if(order_n.equals(order_n0)){//原位置，不处理
				res=0;
			}else{//根据移动位置，相应处理[page*50]
				int n0=Integer.parseInt(order_n0);
				int n=Integer.parseInt(order_n);
				if(n<n0){
					//n~n0-1  +1
					this.dailyPlanService.updateBySetAndWhere("set porder=porder+1 where pid='"+pid+"' and porder>='"+n+"' and porder<='"+(n0-1)+"'");
					//porder=n
					res= this.dailyPlanService.updateBySetAndWhere("set porder='"+n+"' where id='"+id+"'");
				}else if(n>n0){
					//n0+1~n  -1
					this.dailyPlanService.updateBySetAndWhere("set porder=porder-1 where pid='"+pid+"' and porder>='"+(n0+1)+"' and porder<='"+n+"'");
					//porder=n
					res= this.dailyPlanService.updateBySetAndWhere("set porder='"+n+"' where id='"+id+"'");
				}
			}
		}
		model.addAttribute("result", res);
		return "result";
	}
}
