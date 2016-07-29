package com.sipai.controller.plan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.plan.DailyPlanSummary;
import com.sipai.entity.plan.DailyPlanSummaryMaterial;
import com.sipai.entity.plan.MaterialPlan;
import com.sipai.entity.user.User;
import com.sipai.entity.work.GroupType;
import com.sipai.entity.work.WorkScheduling;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.service.plan.DailyPlanSummaryMaterialService;
import com.sipai.service.plan.DailyPlanSummaryService;
import com.sipai.service.plan.MaterialPlanService;
import com.sipai.service.user.UnitService;
import com.sipai.service.user.UserService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/dailyplansummary")
public class DailyPlanSummaryController {
	@Resource
	private DailyPlanSummaryService dailyPlanSummaryService;
	@Resource
	private UnitService unitService;
	@Resource
	private DailyPlanService dailyPlanService;
	@Resource
	private WorkOrderService workorderService;
	@Resource
	private UserService userService;
	@Resource
	private MaterialPlanService materialPlanService;
	@Resource
	private DailyPlanSummaryMaterialService dailyPlanSummaryMaterialService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		request.setAttribute("wsdt", request.getParameter("sdt"));
		request.setAttribute("wedt", request.getParameter("edt"));
		if(request.getParameter("sdt")!=null &&!request.getParameter("sdt").equals("") &&request.getParameter("edt")!=null &&!request.getParameter("edt").equals("")&&request.getParameter("id")!=null &&!request.getParameter("id").equals("")){
			String wherestr=" where plandt>='"+request.getParameter("sdt")+"' and plandt <='"+request.getParameter("edt")+"' and id='"+request.getParameter("id")+"' and status!='2' order by plandt asc";
			
			List<DailyPlanSummary> listdps = this.dailyPlanSummaryService.selectListByWhere(wherestr);
			if(listdps.size()>0){
				DailyPlanSummary dailyPlanSummary=listdps.get(0);
				model.addAttribute("dailyplansummary",dailyPlanSummary);
				if(dailyPlanSummary.getAuditor()!=null && !dailyPlanSummary.getAuditor().equals("")){
					model.addAttribute("auditorname", this.userService.getUserById(dailyPlanSummary.getAuditor()).getCaption());
				}
				List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status!='3'");
				int sum=0;
				int editnum=0;
				if(dplist.size()>0){
					int flag=0;
					for(int i=0;i<dplist.size();i++){
						sum+=Integer.parseInt(dplist.get(i).getProductquantity());	
						switch(dplist.get(i).getStatus()){
						case "0":editnum++;flag=0;break;
						case "1":flag=1;break;//提交
						case "2":flag=1;break;//下发
						case "5":flag=1;break;//审核
						case "6":flag=1;break;//完成
						}
					}
					if(flag==0){
						model.addAttribute("addpermit",1);
					}else{
						model.addAttribute("addpermit",0);
					}
					model.addAttribute("totalnum",sum);
					model.addAttribute("editnum",editnum);
				}else{
					model.addAttribute("totalnum",0);
					model.addAttribute("editnum",0);
					model.addAttribute("addpermit",1);
				}
			}
		}		
		return "/plan/dailyPlanSummaryList";
	}
	@RequestMapping("/showorderlist.do")
	public String showorderlist(HttpServletRequest request,Model model) {
		request.setAttribute("wsdt", request.getParameter("sdt"));
		request.setAttribute("wedt", request.getParameter("edt"));
//		if(request.getParameter("sdt")!=null &&!request.getParameter("sdt").equals("") &&request.getParameter("edt")!=null &&!request.getParameter("edt").equals("")){
//			List<DailyPlanSummary> listdps = this.dailyPlanSummaryService.selectListByWhere(" where plandt>='"+request.getParameter("sdt")+"' and plandt <='"+request.getParameter("edt")+"'  and status!='2' order by plandt asc");
//			if(listdps.size()>0){
//				DailyPlanSummary dailyPlanSummary=listdps.get(0);
//				model.addAttribute("dailyplansummary",dailyPlanSummary);
//				if(dailyPlanSummary.getAuditor()!=null && !dailyPlanSummary.getAuditor().equals("")){
//					model.addAttribute("auditorname", this.userService.getUserById(dailyPlanSummary.getAuditor()).getCaption());
//				}
//				List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status!='3'");
//				int sum=0;
//				if(dplist.size()>0){
//					for(int i=0;i<dplist.size();i++){
//						sum+=Integer.parseInt(dplist.get(i).getProductquantity());						
//					}
//					model.addAttribute("totalnum",sum);
//				}else{
//					model.addAttribute("totalnum",0);
//				}
//			}
//		}		
		return "/plan/dailyPlanSummaryOrderList";
	}
	@RequestMapping("/showlistoperate.do")
	public String showlistOperate(HttpServletRequest request,Model model) {
		request.setAttribute("wsdt", request.getParameter("sdt"));
		request.setAttribute("wedt", request.getParameter("edt"));
		if(request.getParameter("sdt")!=null &&!request.getParameter("sdt").equals("") &&request.getParameter("edt")!=null &&!request.getParameter("edt").equals("")&&request.getParameter("id")!=null &&!request.getParameter("id").equals("")){
			String wherestr=" where plandt>='"+request.getParameter("sdt")+"' and plandt <='"+request.getParameter("edt")+"' and id='"+request.getParameter("id")+"' and status!='2' order by plandt asc";
			
			List<DailyPlanSummary> listdps = this.dailyPlanSummaryService.selectListByWhere(wherestr);
			if(listdps.size()>0){
				DailyPlanSummary dailyPlanSummary=listdps.get(0);
				model.addAttribute("dailyplansummary",dailyPlanSummary);
				if(dailyPlanSummary.getAuditor()!=null && !dailyPlanSummary.getAuditor().equals("")){
					model.addAttribute("auditorname", this.userService.getUserById(dailyPlanSummary.getAuditor()).getCaption());
				}
				List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status!='3'");
				int sum=0;
				int editnum=0;
				int submitnum=0;
				int auditnum=0;				
				if(dplist.size()>0){
					for(int i=0;i<dplist.size();i++){
						sum+=Integer.parseInt(dplist.get(i).getProductquantity());
						switch(dplist.get(i).getStatus()){
						case "0":editnum++;break;
						case "1":submitnum++;break;
						case "5":auditnum++;break;
						}
					}
					model.addAttribute("totalnum",sum);
					model.addAttribute("editnum",editnum);
					model.addAttribute("submitnum",submitnum);
					model.addAttribute("auditnum",auditnum);
				}else{
					model.addAttribute("totalnum",0);
					model.addAttribute("editnum",0);
					model.addAttribute("submitnum",0);
					model.addAttribute("auditnum",0);
				}
			}
		}		
		return "/plan/dailyPlanSummaryListOperate";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		String orderstr=" order by insdt desc";
		
		String wherestr=CommUtil.getwherestr("insuser", "plan/dailyplansummary/showlist.do", cu);
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += " and  plandt>='"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += " and  plandt<='"+request.getParameter("edt")+"' ";
		}
		if(request.getParameter("nostatus")!=null && !request.getParameter("nostatus").isEmpty()){
			wherestr += " and  status!='"+request.getParameter("nostatus")+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){
			switch(request.getParameter("search_status")){
				case "unaudit": wherestr += "and P.status= '0' ";break;
				case "finished": wherestr += "and P.status= '1' ";break;
				case "audited": wherestr += "and P.status= '3' ";break;				
			default: break;
		   }
		}
        List<DailyPlanSummary> list = this.dailyPlanSummaryService.selectListByWhere(wherestr+orderstr);

		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		String date=request.getParameter("date");
		String autoname="";
		if(date!=null && !date.equals("")){
			autoname=CommUtil.getAutoID("PLSummary",1,"name","TB_plan_dailyplanSummary",date,"","");
		}else{
			autoname=CommUtil.getAutoID("PLSummary",1,"name","TB_plan_dailyplanSummary",CommUtil.nowDate(),"","");
		}
		model.addAttribute("autoname", autoname);	
		return "plan/dailyPlanSummaryAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		DailyPlanSummary dailyPlanSummary = this.dailyPlanSummaryService.selectListByWhere(" where id='"+Id+"' order by plandt asc").get(0);
		model.addAttribute("dailyplansummary", dailyPlanSummary);
		return "plan/dailyPlanSummaryEdit";
	}
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		DailyPlanSummary dailyPlanSummary = this.dailyPlanSummaryService.selectListByWhere(" where id='"+Id+"' order by plandt asc").get(0);
		model.addAttribute("dailyplansummary", dailyPlanSummary);
		return "plan/dailyPlanSummaryView";
	}
	@RequestMapping("/audit.do")
	public String doaudit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		DailyPlanSummary dailyPlanSummary = this.dailyPlanSummaryService.selectListByWhere(" where id='"+Id+"' order by plandt asc").get(0);
		if(dailyPlanSummary.getAuditor()!=null && !dailyPlanSummary.getAuditor().equals("")){
			model.addAttribute("auditorname", this.userService.getUserById(dailyPlanSummary.getAuditor()).getCaption());
		}
		model.addAttribute("dailyplansummary", dailyPlanSummary);
		User cu= (User)request.getSession().getAttribute("cu");
		model.addAttribute("cuid", cu.getId());
		model.addAttribute("cuname", cu.getCaption());
		model.addAttribute("nowdate", CommUtil.nowDate());
		return "plan/dailyPlanSummaryAudit";
	}
	/*0初始1完成2作废3审核4下发*/
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute DailyPlanSummary dailyPlanSummary){
		if(!this.dailyPlanSummaryService.checkNotOccupied(dailyPlanSummary.getId(), dailyPlanSummary.getName())){
			model.addAttribute("result", "{\"res\":\"日计划单号重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");		
		String id = CommUtil.getUUID();
		dailyPlanSummary.setId(id);
		dailyPlanSummary.setInsuser(cu.getId());
		dailyPlanSummary.setInsdt(CommUtil.nowDate());
		dailyPlanSummary.setStatus("0");
		int result = this.dailyPlanSummaryService.save(dailyPlanSummary);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute DailyPlanSummary dailyPlanSummary){
		if(!this.dailyPlanSummaryService.checkNotOccupied(dailyPlanSummary.getId(), dailyPlanSummary.getName())){
			model.addAttribute("result", "{\"res\":\"日计划单号重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		dailyPlanSummary.setModifier(cu.getId());
		dailyPlanSummary.setModifydt(CommUtil.nowDate());
		int result = this.dailyPlanSummaryService.update(dailyPlanSummary);		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}	
	@RequestMapping("/auditsave.do")
	public String auditsave(HttpServletRequest request,Model model,
			@ModelAttribute DailyPlanSummary dailyPlanSummary){
		int result=0;
		String resstr="";		
		List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status='1'");
		List<DailyPlan> dplist0=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status='0'");
		if(dplist.size()==0 ||dplist.isEmpty()){
			resstr="{\"res\":\"无可审核计划\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		}else if(dplist0.size()>0){
			resstr="{\"res\":\"有未提交计划，请先完成提交操作。\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		}else{
			this.dailyPlanService.updateBySetAndWhere(" set status='5' where pid='"+dailyPlanSummary.getId()+"' and status='1'");
			User cu= (User)request.getSession().getAttribute("cu");
			dailyPlanSummary.setModifier(cu.getId());
			dailyPlanSummary.setModifydt(CommUtil.nowDate());
			result = this.dailyPlanSummaryService.update(dailyPlanSummary);	
			resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		}			
		
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/distributeday.do")
	public String distributeday(HttpServletRequest request,Model model,
			@ModelAttribute DailyPlanSummary dailyPlanSummary){
		String resstr="";
		String res="";
		User cu= (User)request.getSession().getAttribute("cu");
		List<DailyPlan> listdp=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status='5'");
		List<DailyPlan> listdp01=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and (status='0' or status='1')");
		if(listdp01.size()>0){
			res="有未提交或未审核计划，请先完成提交或审核操作。";
		}else{
			String nos="";
			for(int i=0;i<listdp.size();i++){
				nos+=listdp.get(i).getProductionorderno()+"','";
			}
			String result=this.workorderService.addbyplan(cu, nos);//下发工单
			res=result;
		}
		if(res.equals("1")){
			//工单下发后，生成物料计划单
			int suc = 0;
			int fal = 0;
			String mpwherestr =" where 1=1 and  workstationid is not null and id ='"+dailyPlanSummary.getId()+"'";
			String mpgroupstr = " group by id,name,plandt,workstationid, workstationname,workstationserial, materialid, materialcode, materialname,amount ,unit";
			String mporderstr=" order by workstationserial desc";
	        List<MaterialPlan> mplist = this.materialPlanService.getStatisticListByWhere(mpwherestr+mpgroupstr+mporderstr);
	        for(int i=0;i<mplist.size();i++){
	        	DailyPlanSummaryMaterial dailyPlanSummaryMaterial=new DailyPlanSummaryMaterial();
				String id = CommUtil.getUUID();
				dailyPlanSummaryMaterial.setId(id);
				dailyPlanSummaryMaterial.setInsuser(cu.getId());
				dailyPlanSummaryMaterial.setInsdt(CommUtil.nowDate());
				dailyPlanSummaryMaterial.setPlanid(dailyPlanSummary.getId());
				dailyPlanSummaryMaterial.setMaterialid(mplist.get(i).getMaterialid());						
				dailyPlanSummaryMaterial.setWorkstationid(mplist.get(i).getWorkstationid());
				dailyPlanSummaryMaterial.setAmount(Double.valueOf(mplist.get(i).getAmount()));										
				int result = this.dailyPlanSummaryMaterialService.save(dailyPlanSummaryMaterial);
				if(result==1){
					suc++;
				}else{
					fal++;
				}
	        }
	        if(suc>0){
	        	res="计划下发成功，物料计划单"+suc+"条数据保存成功！";
	        }
	        if(fal>0){
	        	res="计划下发成功，物料计划单"+fal+"条数据保存失败！";
	        }
		}
		resstr="{\"res\":\""+res+"\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/submitday.do")
	public String submitday(HttpServletRequest request,Model model,
			@ModelAttribute DailyPlanSummary dailyPlanSummary){
		User cu= (User)request.getSession().getAttribute("cu");
		List<DailyPlan> listdp=this.dailyPlanService.selectListByWhere(" where pid='"+dailyPlanSummary.getId()+"' and status='0'");
		int result=0;
		if(listdp.size()!=0 &&!listdp.isEmpty()){			
			result=this.dailyPlanService.updateBySetAndWhere(" set status='1' where pid='"+dailyPlanSummary.getId()+"' and status='0'");
			
		}		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlanSummary.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.dailyPlanSummaryService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.dailyPlanSummaryService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/select.do")
	public String showlistForSelect(HttpServletRequest request,Model model) {
		return "/plan/dailyPlanSummaryForSelect";
	}
	
	@RequestMapping("/getlistForSelect.do")
	public ModelAndView getlistForSelect(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " plandt ";
		}
		if(order==null){
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String wherestr = CommUtil.getwherestr("insuser", "plan/dailyplansummary/showlist.do", cu);
		if(request.getParameter("querytype")!=null && request.getParameter("querytype").equals("select")){
			wherestr=" where 1=1";
		}
		wherestr += " and status!='2' ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<DailyPlanSummary> list = this.dailyPlanSummaryService.selectListByWhere(wherestr+orderstr);
        PageInfo<DailyPlanSummary> pi = new PageInfo<DailyPlanSummary>(list);
		JSONArray json=JSONArray.fromObject(list);		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getDPSforCalerdar.do")
	public ModelAndView getDPSforCalerdar(HttpServletRequest request,Model model,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		String year=request.getParameter("year");
		String month=request.getParameter("month");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date nowdate;
		String lastmonth="";
		String nextmonth="";
		try {
			nowdate = sdf.parse(year+"-"+month);
			Calendar cal = Calendar.getInstance();
	        cal.setTime(nowdate);
	        cal.add(Calendar.MONTH, -1);
	        lastmonth=sdf.format(cal.getTime());
	        cal.add(Calendar.MONTH, 2);
	        nextmonth=sdf.format(cal.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " plandt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "plan/dailyplansummary/showlist.do", cu);
//		if(request.getParameter("wdt")!=null && !request.getParameter("wdt").isEmpty()){
//			wherestr += " and plandt >= '"+request.getParameter("wdt")+"' ";
//		}
		//获取记录
		List<DailyPlanSummary> list_dps=this.dailyPlanSummaryService.selectListByWhere("where "+
				 "plandt>'"+lastmonth+"-22' and plandt<'"+nextmonth+"-14' and status!='2'");
		JSONArray json_dps=JSONArray.fromObject(list_dps);
		String result="{\"jsondps\":"+json_dps+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/invalidateday.do")
	public String invalidateday(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){		
		User cu=(User)request.getSession().getAttribute("cu");
		DailyPlanSummary dps=new DailyPlanSummary();
		dps.setId(id);
		dps.setStatus("2");
		dps.setModifier(cu.getId());
		dps.setModifydt(CommUtil.nowDate());
		int result =this.dailyPlanSummaryService.update(dps);
		int res=0;
		int result2=0;
		if(result==1){
			List<DailyPlan> dplist=this.dailyPlanService.selectListByWhere(" where pid='"+id+"' order by id");
			for(int i=0;i<dplist.size();i++){
				result2=this.dailyPlanService.invalidate(dplist.get(i).getId());
				if(result2!=1){
					res=0;
				}else{
					res=1;
				}
			}
			if(dplist.size()==0){
				res=1;
			}
		}
		model.addAttribute("result", res);
		return "result";
	}
	
	@RequestMapping("/getinvalidDPS.do")
	public String getinvalidDPS(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		String orderstr=" order by insdt asc";
		
		String wherestr=CommUtil.getwherestr("insuser", "plan/dailyplansummary/showlist.do", cu);
		
		if(request.getParameter("search_nameiv")!=null &&!request.getParameter("search_nameiv").equals("")){
			wherestr+=" and name like '%"+request.getParameter("search_nameiv")+"%'";
		}
		if(request.getParameter("sdt")!=null &&!request.getParameter("sdt").equals("") &&request.getParameter("edt")!=null &&!request.getParameter("edt").equals("")){
			wherestr+=" and plandt>='"+request.getParameter("sdt")+"' and plandt <='"+request.getParameter("edt")+"'  and status='2' ";
		}
        List<DailyPlanSummary> list = this.dailyPlanSummaryService.selectListByWhere(wherestr+orderstr);

		JSONArray json=JSONArray.fromObject(list);
		//System.out.println(json);
		String result ="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
	    return "result";
	
	}
}
