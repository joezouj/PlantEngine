package com.sipai.controller.work;

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
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.GroupMember;
import com.sipai.entity.work.UserWorkStation;
import com.sipai.entity.work.Workstation;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.GroupMemberService;
import com.sipai.service.work.GroupService;
import com.sipai.service.work.UserWorkStationService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/group")
public class GroupController {
	@Resource
	private GroupService groupService;
	@Resource
	private GroupMemberService groupMemberService;
	@Resource
	private WorkstationService workstationService;
	@Resource
	private UnitService unitService;
	@Resource
	private UserWorkStationService userWorkStationService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/groupList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " deptid ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/group/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
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
        List<Group> list = this.groupService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Group> pi = new PageInfo<Group>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/groupAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		Group group = this.groupService.selectById(userId);
		model.addAttribute("group", group);
		return "work/groupEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Group group){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.groupService.checkNotOccupied(group.getId(), group.getName())){
			model.addAttribute("result", "{\"res\":\"班组名称重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		group.setId(id);
		group.setInsuser(cu.getId());
		group.setInsdt(CommUtil.nowDate());
		
		int result = this.groupService.save(group);
		if(result>0){
			groupMemberService.saveMembers(group.getId(), request.getParameter("leaderid"), "leader");
			groupMemberService.saveMembers(group.getId(), request.getParameter("memberid"), "member");
		}
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/saveuserworkstation.do")
	public String dosaveuserworkstation(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids,
			@RequestParam(value="stationids") String stationids){
		User cu= (User)request.getSession().getAttribute("cu");
		int result=1;
		int resultflag=0;
		String[] item_userids=ids.split(",");
		String[] item_stationids=stationids.split(",");
		for(int i=0;i<item_userids.length;i++){
			List<UserWorkStation> list=userWorkStationService.selectListByWhere(" where userid='"+item_userids[i]+"'");
			if(list==null||list.size()==0){
				String id = CommUtil.getUUID();
				UserWorkStation userWorkStation=new UserWorkStation();
				userWorkStation.setId(id);
				userWorkStation.setUserid(item_userids[i]);
				userWorkStation.setInsuser(cu.getId());
				userWorkStation.setWorkstationid(item_stationids[i]);
				resultflag=userWorkStationService.save(userWorkStation);
				userWorkStation=null;
			}else{
				UserWorkStation userWorkStation=list.get(0);
				userWorkStation.setInsuser(cu.getId());
				userWorkStation.setWorkstationid(item_stationids[i]);
				resultflag=userWorkStationService.update(userWorkStation);
			}
			if(resultflag==0){
				result=0;
			}
		}
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Group group){
		if(!this.groupService.checkNotOccupied(group.getId(), group.getName())){
			model.addAttribute("result", "{\"res\":\"班组名称重复\"}");
			return "result";
		}
		int result = this.groupService.update(group);
		if(result>0){
			groupMemberService.deleteByGroupId(group.getId());
			groupMemberService.saveMembers(group.getId(), request.getParameter("leaderid"), "leader");
			groupMemberService.saveMembers(group.getId(), request.getParameter("memberid"), "member");
		}
		String resstr="{\"res\":\""+result+"\",\"id\":\""+group.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.groupService.deleteById(id);
		if(result>0){
			this.groupMemberService.deleteByGroupId(id);
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.groupService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showlistForSelect.do")
	public String showlistForSelect(HttpServletRequest request,Model model) {
		return "/work/groupListForSelect";
	}
	
	@RequestMapping("/edit4scheduling.do")
	public String edit4scheduling(HttpServletRequest request,Model model) {
		String groupid = request.getParameter("id");
		String userids = request.getParameter("userids");
		String workstationids = request.getParameter("workstationids");
		model.addAttribute("groupid", groupid);
		model.addAttribute("userids", userids);
		model.addAttribute("workstationids", workstationids);
		return "/work/groupMemberListForScheduling";
	}
	@RequestMapping("/getworkstation.do")
	public ModelAndView getworkstation(HttpServletRequest request,Model model) {
		List<Workstation> list=workstationService.selectListByWhere("where 1=1 order by name");
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result", json);//用于展示工位列表
        return new ModelAndView("result");
	}
	@RequestMapping("/getMemberListByGroupId.do")
	public ModelAndView getMemberListByGroupId(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows) {
		
		PageHelper.startPage(page, rows);
        List<GroupMember> list = this.groupMemberService.selectListByGroupId(request.getParameter("groupid"));
        String userids=request.getParameter("userids");
        String workstationids=request.getParameter("workstationids");
        
       
        if(userids!=null&&!userids.isEmpty()){
        	String[] item=userids.split(",");
        	String[] item_ws=new String[item.length];
            if(workstationids!=null && !workstationids.isEmpty()){
            	 item_ws=workstationids.split(","); 
            	 System.out.println();
            }
            for(int i=0;i<list.size();i++){
            	for(int j=0;j<item.length;j++){
            		if(list.get(i).getUserid().equals(item[j])){
            			list.get(i).set_checkflag("1");
            			if(j<item_ws.length){
            				list.get(i).setWorkstationid(item_ws[j]);
            			}
            			break;
            		}
            	}
            }
           
        }
        PageInfo<GroupMember> pi = new PageInfo<GroupMember>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
