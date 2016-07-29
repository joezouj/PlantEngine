package com.sipai.controller.work;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.GroupMember;
import com.sipai.entity.work.GroupType;
import com.sipai.entity.work.WorkScheduling;
import com.sipai.entity.work.WorkTaskEquipment;
import com.sipai.entity.work.Workstation;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.GroupMemberService;
import com.sipai.service.work.GroupService;
import com.sipai.service.work.GroupTypeService;
import com.sipai.service.work.WorkSchedulingService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workscheduling")
public class WorkSchedulingController {
	@Resource
	private WorkSchedulingService workSchedulingService;
	@Resource
	private GroupTypeService groupTypeService;
	@Resource
	private WorkstationService workstationService;
	@Resource
	private GroupService groupService;
	@Resource
	private GroupMemberService groupMemberService;
	@Resource
	private UnitService unitService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/workschedulingList";
	}
	@RequestMapping("/schedulingtogroup.do")
	public String schedulingtogroup(HttpServletRequest request,Model model) {
		String dt = request.getParameter("dt");
		String grouptypeid = request.getParameter("grouptypeid");
		model.addAttribute("dt", dt);
		model.addAttribute("grouptypeid", grouptypeid);
		return "/work/groupListForScheduling";
	}
	@RequestMapping("/getGTScheduling.do")
	public ModelAndView getGTScheduling(HttpServletRequest request,Model model,
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
			sort = " sdt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/grouptype/showlist.do", cu);
		if(request.getParameter("wdt")!=null && !request.getParameter("wdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("wdt")+"' ";
		}
		
        List<GroupType> list_gt = this.groupTypeService.selectListByWhere(wherestr+orderstr);
		JSONArray json_gt=JSONArray.fromObject(list_gt);
		//获取有排班的workdate记录
		List<WorkScheduling> list_ws=this.workSchedulingService.distinctWdByWhere("where "+
				 "CONVERT(varchar(100), workstdt, 23)>'"+lastmonth+"-22' and CONVERT(varchar(100), workstdt, 23)<'"+nextmonth+"-13'");
		JSONArray json_ws=JSONArray.fromObject(list_ws);
		String result="{\"grouptype\":"+json_gt+",\"workscheduling\":"+json_ws+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value = "dt", required=false) String dt,
			@RequestParam(value = "grouptypeid", required=false) String grouptypeid) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " deptid ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/workscheduling/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_pid")!=null && !request.getParameter("search_pid").isEmpty()){
			List<Unit> unitlist=unitService.getUnitChildrenById(request.getParameter("search_pid"));
			String pidstr="";
			for(int i=0;i<unitlist.size();i++){
				pidstr += "'"+unitlist.get(i).getId()+"',";
			}
			if(pidstr!=""){
				pidstr = pidstr.substring(0, pidstr.length()-1);
				wherestr += " and deptid in ("+pidstr+") ";
			}
			
		}
		PageHelper.startPage(page, rows);
        List<Group> list_group = this.groupService.selectListByWhere(wherestr+orderstr);
        List<Group> list =new ArrayList<Group>();
        List<Group> list_temp =new ArrayList<Group>();//存储未排班的班组信息，最后加到已排班的班组后面
        for(int i=0;i<list_group.size();i++){
        	Group group=list_group.get(i);
        	List<GroupMember> groupMembers=this.groupMemberService.selectListByScheduling("where 1=1 and gm.groupid='"+group.getId()
        			+"' and CONVERT(varchar(100), ws.workstdt, 23)='"+dt+"' and ws.grouptypeid='"+grouptypeid+"' order by gm.usertype");
        	if(groupMembers!=null&&groupMembers.size()>0){ //如果排班表里有group,则将结果保存到新的list中，并将该group标记为已排班
        		group.setGroupmembers(groupMembers);
        		group.setSchedulingflag("1");
        		list.add(group);
        	}else{
        		list_temp.add(group);
        	}
        }
        list.addAll(list_temp);
        list_temp=null;
        PageInfo<Group> pi = new PageInfo<Group>(list_group);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getstationlist.do")
	public ModelAndView getstationlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " lineid,serial ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/workscheduling/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_line")!=null && !request.getParameter("search_line").isEmpty()){
			wherestr += " and lineid = '"+request.getParameter("search_line")+"' ";
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
		return "work/workschedulingAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		WorkScheduling workScheduling = this.workSchedulingService.selectById(userId);
		model.addAttribute("worktask", workScheduling);
		return "work/worktaskEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@RequestParam(value="res") String res,
			@RequestParam(value="dt") String dt,
			@RequestParam(value="grouptypeid") String grouptypeid){
		User cu= (User)request.getSession().getAttribute("cu");
//	    System.out.println(res);
	    JSONArray jaArray=JSONArray.fromObject(res);
	    Map<String, Class<GroupMember>> classMap = new HashMap<String, Class<GroupMember>>(); 
	    classMap.put("groupmembers", GroupMember.class); 
	    List<Group> list=JSONArray.toList(jaArray, Group.class, classMap);
		int result=1;
		int resultflag=0;
		List<GroupType> list_gt=this.groupTypeService.selectListByWhere("where 1=1");
		if (list_gt==null || list_gt.size()==0) {//无班次信息直接返回
			model.addAttribute("result", result);
			return "result";
		}
		//如果更改班组，则先删除以前排班记录
		if(list!=null&&list.size()>0){
			this.workSchedulingService.deleteByWhere("where 1=1 and grouptypeid='"+grouptypeid+
					"' and CONVERT(varchar(100), workstdt, 23)='"+dt+"'");
		}
		for(int i=0;i<list.size();i++){
			Group group=list.get(i);
			List<GroupMember> groupMembers=group.getGroupmembers();
			for(int j=0;j<groupMembers.size();j++){
				GroupMember groupMember= groupMembers.get(j);
				String id = CommUtil.getUUID();
				WorkScheduling workScheduling=new WorkScheduling();
				workScheduling.setId(id);
				workScheduling.setInsdt(CommUtil.nowDate());
				workScheduling.setInsuser(cu.getId());
				workScheduling.setUserid(groupMember.getUserid());
				String workstdt="";
				String workeddt="";
				for (GroupType itemGroupType : list_gt) {
					if (itemGroupType.getId().equals(grouptypeid)) {
						workstdt=dt+" "+itemGroupType.getSdt();
						workeddt=dt+" "+itemGroupType.getEdt();
						if(itemGroupType.getSdt().compareTo(itemGroupType.getEdt())>0)//如果下班时间小于上班时间，则下班时间加一天
						{
							SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
							try {
								Date date = sdf.parse(workeddt);
								Calendar   calendar   =   new   GregorianCalendar(); 
							    calendar.setTime(date); 
							    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
							    date=calendar.getTime();   //这个时间就是日期往后推一天的结果 
								workeddt=sdf.format(date);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				workScheduling.setWorkstdt(workstdt);
				workScheduling.setWorkeddt(workeddt);
				workScheduling.setWorkstationid(groupMember.getWorkstationid());
				workScheduling.setGrouptypeid(grouptypeid);
				resultflag = this.workSchedulingService.save(workScheduling);
				if(resultflag==0){
					result=0;
				}
			}
		}
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkScheduling workScheduling){
		User cu= (User)request.getSession().getAttribute("cu");
		workScheduling.setInsuser(cu.getId());
		workScheduling.setInsdt(CommUtil.nowDate());
		int result = this.workSchedulingService.update(workScheduling);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workScheduling.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workSchedulingService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workSchedulingService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
}
