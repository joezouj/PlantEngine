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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.plan.DailyPlanSummary;
import com.sipai.entity.plan.DeliverDetail;
import com.sipai.entity.plan.MaterialPlan;
import com.sipai.service.plan.DailyPlanSummaryService;
import com.sipai.service.plan.DeliverDetailService;
import com.sipai.service.plan.MaterialPlanService;

@Controller
@RequestMapping("/plan/materialplan")
public class MaterialPlanController {
	@Resource
	private MaterialPlanService materialPlanService;
	@Resource
	private DailyPlanSummaryService dailyPlanSummaryService;
	@Resource
	private DeliverDetailService deliverDetailService;
	
	@RequestMapping("/showcalendar.do")
	public String showcalendar(HttpServletRequest request,Model model) {
		return "/plan/materialPlanCalendar";
	}
	@RequestMapping("/showplan.do")
	public String showplan(HttpServletRequest request,Model model) {
		DailyPlanSummary dailyPlanSummary = this.dailyPlanSummaryService.selectById(request.getParameter("planid"));
		model.addAttribute("dailyPlanSummary", dailyPlanSummary);
		return "/plan/materialPlanView";
	}
	@RequestMapping("/materialPlanForSelect.do")
	public String materialPlanForSelect(HttpServletRequest request,Model model) {
		model.addAttribute("pid", request.getParameter("pid"));
		model.addAttribute("planid", request.getParameter("planid"));
		model.addAttribute("workstationid", request.getParameter("workstationid"));
		return "/plan/materialPlanForSelect";
	}
	@RequestMapping("/getmaterialplan.do")
	public ModelAndView getMaterialPlan(HttpServletRequest request,Model model) {
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
			sort = " workstationserial ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String wherestr = " where 1=1 and  workstationid is not null ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			//获取已排料的任务
			String ddstr = " where 1=1 and d.pid='"+request.getParameter("pid")+"' order by d.insdt";
			List<DeliverDetail> ddlist = deliverDetailService.getDeliverDetail(ddstr);
			String materialids = "";
			for(int i=0;i<ddlist.size();i++){
				materialids += "'"+ddlist.get(i).getMaterialid()+"',";
			}
			if(materialids.length()>1){
				materialids = materialids.substring(0,materialids.length()-1);
				wherestr += " and materialid not in ("+materialids+")";
			}
		}
		if(request.getParameter("planid")!=null && !request.getParameter("planid").isEmpty()){
			wherestr += " and id= '"+request.getParameter("planid")+"'";
		}
		if(request.getParameter("workstationid")!=null && !request.getParameter("workstationid").isEmpty()){
			wherestr += " and workstationid = '"+request.getParameter("workstationid")+"'";
		}
		if(request.getParameter("search_workstationserial")!=null && !request.getParameter("search_workstationserial").isEmpty()){
			wherestr += " and workstationserial like '%"+request.getParameter("search_workstationserial")+"%'";
		}
		if(request.getParameter("search_materialcode")!=null && !request.getParameter("search_materialcode").isEmpty()){
			wherestr += " and materialcode like '%"+request.getParameter("search_materialcode")+"%'";
		}
		String groupstr = " group by id,name,plandt,workstationid, workstationname,workstationserial, materialid, materialcode, materialname,amount ,unit";
		PageHelper.startPage(pages, pagesize);
        List<MaterialPlan> list = this.materialPlanService.getStatisticListByWhere(wherestr+groupstr+orderstr);        
        PageInfo<MaterialPlan> page = new PageInfo<MaterialPlan>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getCalendarData.do")
	public ModelAndView getCalendarData(HttpServletRequest request,Model model,
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
			e.printStackTrace();
		} 
		if(sort==null){
			sort = " plandt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String whereStr = "where CONVERT(varchar(100), plandt, 23)>'"
				+lastmonth+"-22' and CONVERT(varchar(100), plandt, 23)<'"+nextmonth+"-13'";		
		//获取日历界面范围内的物料计划数据
		//System.out.println(whereStr+orderstr);
		List<MaterialPlan> mPlanList=this.materialPlanService.selectListByWhere(whereStr+orderstr);
		JSONArray mPlanJson=JSONArray.fromObject(mPlanList);
		String result="{\"materialPlan\":"+mPlanJson+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
