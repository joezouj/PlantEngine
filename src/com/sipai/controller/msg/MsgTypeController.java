package com.sipai.controller.msg;

import java.util.ArrayList;
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
import com.sipai.entity.msg.MsgType;
import com.sipai.entity.user.User;
import com.sipai.service.msg.MsgTypeService;
import com.sipai.service.user.RoleService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/msg")
public class MsgTypeController {
	@Resource
	private MsgTypeService msgtypeService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@RequestMapping("/showMsgType.do")
	public String showlisttype(HttpServletRequest request,Model model) {
		return "/msg/msgTypeList";
	}
	@RequestMapping("/getMsgType.do")
	public ModelAndView getmsgtype(HttpServletRequest request,Model model) {
		User cu = (User) request.getSession().getAttribute("cu");
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
			sort = " insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		//String orderstr=" order by T."+sort+" "+order;
		String orderstr=" order by "+sort+" "+order;
		String wherestr=CommUtil.getwherestr("T.insuser", "msg/getMsgType.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		PageHelper.startPage(pages, pagesize);
        //List<MsgType> list = this.msgtypeService.getMsgType(wherestr+orderstr);
		List<MsgType> list = this.msgtypeService.selectListByWhere(wherestr+orderstr);
        PageInfo<MsgType> page = new PageInfo<MsgType>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/addMsgType.do")
	public String doadd(HttpServletRequest request,Model model){
		return "msg/msgTypeAdd";
	}
	@RequestMapping("/editMsgType.do")
	public String doedit(HttpServletRequest request,Model model){
		String msgTypeId = request.getParameter("id");
		String orderstr=" order by T.insdt ";
		String wherestr=" where T.id='"+msgTypeId+"'";
		List<MsgType> list = this.msgtypeService.getMsgType(wherestr+orderstr);
		MsgType msgType=list.get(0);
		String msgrolename = "";
		String msgroleids = "";
		
		String msgusername = "";
		String msguserids = "";		
		String msguseridname="";
		
		String smsusername = "";
		String smsuserids = "";
		String smsuseridname="";
		for(int i=0;i<list.size();i++){
			for(int j=0;j<msgType.getRole().size();j++){
				msgrolename+=this.roleService.getObj(msgType.getRole().get(j).getRoleid()).getName()+",";				
				msgroleids+=msgType.getRole().get(j).getRoleid()+",";
			}
			for(int j=0;j<msgType.getMsguser().size();j++){
				msgusername+=this.userService.getUserById(msgType.getMsguser().get(j).getUserid()).getCaption()+",";				
				msguserids+=msgType.getMsguser().get(j).getUserid()+",";
				msguseridname+=msgType.getMsguser().get(j).getUserid()+","+this.userService.getUserById(msgType.getMsguser().get(j).getUserid()).getCaption()+";";
			}
			for(int j=0;j<msgType.getSmsuser().size();j++){
				smsusername+=this.userService.getUserById(msgType.getSmsuser().get(j).getUserid()).getCaption()+",";				
				smsuserids+=msgType.getSmsuser().get(j).getUserid()+",";
				smsuseridname+=msgType.getSmsuser().get(j).getUserid()+","+this.userService.getUserById(msgType.getSmsuser().get(j).getUserid()).getCaption()+";";
			}
		}
		model.addAttribute("msgType", msgType);
		model.addAttribute("msgrolename",msgrolename.substring(0, msgrolename.length()-1));
		model.addAttribute("msgroleids", msgroleids);
		
		model.addAttribute("msgusername",msgusername.substring(0, msgusername.length()-1) );
		model.addAttribute("msguserids", msguserids);
		model.addAttribute("msguseridname", msguseridname.substring(0, msguseridname.length()-1));		
		model.addAttribute("smsusername",smsusername.substring(0, smsusername.length()-1));
		model.addAttribute("smsuserids", smsuserids);	
		model.addAttribute("smsuseridname", smsuseridname.substring(0, smsuseridname.length()-1));
		return "msg/msgTypeEdit";
	}
	@RequestMapping("/viewMsgType.do")
	public String doview(HttpServletRequest request,Model model){
		String msgTypeId = request.getParameter("id");
		String orderstr=" order by T.insdt ";
		String wherestr=" where T.id='"+msgTypeId+"'";
		List<MsgType> list = this.msgtypeService.getMsgType(wherestr+orderstr);
		MsgType msgType=list.get(0);
		String msgrolename = "";
		String msgroleids = "";
		String msgusername = "";
		String msguserids = "";
		String smsusername = "";
		String smsuserids = "";
		for(int i=0;i<list.size();i++){
			for(int j=0;j<msgType.getRole().size();j++){
				msgrolename+=this.roleService.getObj(msgType.getRole().get(j).getRoleid()).getName()+",";				
				msgroleids+=msgType.getRole().get(j).getRoleid()+",";
			}
			for(int j=0;j<msgType.getMsguser().size();j++){
				msgusername+=this.userService.getUserById(msgType.getMsguser().get(j).getUserid()).getCaption()+",";				
				msguserids+=msgType.getMsguser().get(j).getUserid()+",";
			}
			for(int j=0;j<msgType.getSmsuser().size();j++){
				smsusername+=this.userService.getUserById(msgType.getSmsuser().get(j).getUserid()).getCaption()+",";				
				smsuserids+=msgType.getSmsuser().get(j).getUserid()+",";
			}
		}
		model.addAttribute("msgType", msgType);
		model.addAttribute("msgrolename",msgrolename);
		model.addAttribute("msgroleids", msgroleids);
		model.addAttribute("msgusername",msgusername );
		model.addAttribute("msguserids", msguserids);
		model.addAttribute("smsusername",smsusername);
		model.addAttribute("smsuserids", smsuserids);		
		return "msg/msgTypeView";
	}
	@RequestMapping("/deleteMsgType.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.msgtypeService.deleteMsgTypeById(id);		
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteMsgTypes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.msgtypeService.deleteMsgTypeByWhere("where id in ('"+ids+"')",ids);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/saveMsgType.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("msgtype") MsgType msgtype){
		User cu = (User) request.getSession().getAttribute("cu");
		boolean checkres=this.msgtypeService.checkNotOccupied(request.getParameter("id"));
		int result=0;
		if(checkres){
			msgtype.setInsdt(new Date());
			msgtype.setInsuser(cu.getId());		
			msgtype.setStatus("启用");
			msgtype.setSendway(request.getParameter("sendway"));
			msgtype.setRemark(request.getParameter("remark"));
			result = this.msgtypeService.saveMsgType(msgtype);		
			this.msgtypeService.saveMsgRole(request.getParameter("msgroleids"),msgtype.getId());
			this.msgtypeService.saveMsguser(request.getParameter("msguserids"),msgtype.getId());
			this.msgtypeService.saveSmsuser(request.getParameter("smsuserids"),msgtype.getId());
		}else{
			//id重复，已被占用
			result=2;
		}
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/updateMsgType.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("msgtype") MsgType msgtype){
		User cu = (User) request.getSession().getAttribute("cu");					
		msgtype.setInsdt(new Date());
		msgtype.setInsuser(cu.getId());		
		msgtype.setStatus("启用");
		msgtype.setSendway(request.getParameter("sendway"));
		msgtype.setRemark(request.getParameter("remark"));
		int result = this.msgtypeService.updateMsgTypeById(msgtype);
		this.msgtypeService.saveMsgRole(request.getParameter("msgroleids"),msgtype.getId());
		this.msgtypeService.saveMsguser(request.getParameter("msguserids"),msgtype.getId());
		this.msgtypeService.saveSmsuser(request.getParameter("smsuserids"),msgtype.getId());
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/msgTypeForSelect.do")
	public String msgtypeforselect(HttpServletRequest request,Model model){
		return "msg/msgTypeForSelect";
	}
	@RequestMapping("/getMsgTypeForSelect.do")
	public ModelAndView getmsgtypeforselect(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
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
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by T.insdt "+order;
		
		String wherestr=CommUtil.getwherestr("T.insuser", "msg/getMsgType.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and T.name like '%"+request.getParameter("search_name")+"%' ";
		}
		PageHelper.startPage(pages, pagesize);
        List<MsgType> list = this.msgtypeService.getMsgType(wherestr+orderstr);
        List<MsgType> listcopy=new ArrayList<MsgType>();
        listcopy.addAll(list);
        //发送权限判断
        for(MsgType mtype:list){
        	if(!mtype.getMsguser().isEmpty() && mtype.getSendway()!=null && !mtype.getSendway().equals("sms")){
        		int flagmu=0;
        		for(int j=0;j<mtype.getMsguser().size();j++){
        			if(mtype.getMsguser().get(j).getId().equals(cu.getId())){
        				flagmu=1;
        				break;
        			}
        		}
        		if(flagmu==0){
        			listcopy.remove(mtype);
        			continue;
        		}
        	}
        	if(!mtype.getSmsuser().isEmpty() && mtype.getSendway()!=null && !mtype.getSendway().equals("msg")){
        		int flagsu=0;
        		for(int z=0;z<mtype.getSmsuser().size();z++){
        			if(mtype.getSmsuser().get(z).getId().equals(cu.getId())){
        				flagsu=1;
        				break;
        			}
        		}
        		if(flagsu==0){
        			listcopy.remove(mtype);
        		}
        	}
        }
        PageInfo<MsgType> page = new PageInfo<MsgType>(listcopy);
		JSONArray json=JSONArray.fromObject(listcopy);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
