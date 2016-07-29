package com.sipai.controller.msg;

import java.util.ArrayList;
import java.util.Collections;
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
import com.sipai.entity.msg.EmppSendUser;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.msg.MsgRecv;
import com.sipai.entity.msg.MsgType;
import com.sipai.entity.user.User;
import com.sipai.service.msg.EmppAdminService;
import com.sipai.service.msg.EmppSendService;
import com.sipai.service.msg.EmppSendUserService;
import com.sipai.service.msg.MsgRecvService;
import com.sipai.service.msg.MsgServiceImpl;
import com.sipai.service.msg.MsgTypeService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/msg")
public class MsgController {
	@Resource
	private MsgServiceImpl msgService;
	@Resource
	private MsgRecvService msgrecvService;
	@Resource
	private UserService userService;
	@Resource
	private MsgTypeService msgtypeService;
	@Resource
	private EmppAdminService emppadminService;
	@Resource
	private EmppSendService emppsendService;
	@Resource
	private EmppSendUserService emppsenduserService;
	
	public static String title = "【亭南污水】";
	@RequestMapping("/showMsgrecv.do")
	public String showlistrecv(HttpServletRequest request,Model model) {
		return "/msg/msgListrecv";
	}
	@RequestMapping("/showMsgsend.do")
	public String showlistsend(HttpServletRequest request,Model model) {
		return "/msg/msgListsend";
	}
	@RequestMapping("/getMsgrecv.do")
	public ModelAndView getMsgrecv(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()
				&&!request.getParameter("sort").equals("mrecv")&&!request.getParameter("sort").equals("susername")
				&&!request.getParameter("sort").equals("typename")){
			sort = request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("mrecv")){
			sort = " V.status ";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("susername")){
			sort = " U.caption ";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("typename")){
			sort = " T.name ";
		}else{
			sort = " M.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by  "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("M.insuser", "msg/getMsgrecv.do", cu);		
		wherestr+=" and M.issms!='sms' and M.delflag!='TRUE' and V.delflag!='TRUE' and V.unitid='"+cu.getId()+"' ";		
		if(request.getParameter("search_susername")!=null && !request.getParameter("search_susername").isEmpty()){
			wherestr += "and U.caption like '%"+request.getParameter("search_susername")+"%' ";
		}
		if(request.getParameter("search_content")!=null && !request.getParameter("search_content").isEmpty()){
			wherestr += "and M.content like '%"+request.getParameter("search_content")+"%' ";
		}
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += "and M.sdt>= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += "and M.sdt<= '"+request.getParameter("edt")+"' ";
		}	
		PageHelper.startPage(pages, pagesize);
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);
        PageInfo<Msg> page = new PageInfo<Msg>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getMsgsend.do")
	public ModelAndView getMsgsend(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()
				&&!request.getParameter("sort").equals("issms")&&!request.getParameter("sort").equals("susername")
				&&!request.getParameter("sort").equals("typename")){
			sort = request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("issms")){
			sort = " M.issms ";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("susername")){
			sort = " U.caption ";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("typename")){
			sort = " T.name ";
		}else{
			sort = " M.insdt ";
		}
		String order="";		
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by  "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("M.insuser", "msg/getMsgsend.do", cu);
		wherestr+=" and M.delflag!='TRUE' and M.suserid='"+cu.getId()+"' ";
		if(request.getParameter("search_susername")!=null && !request.getParameter("search_susername").isEmpty()){
			wherestr += "and U.caption like '%"+request.getParameter("search_susername")+"%' ";
		}
		if(request.getParameter("search_content")!=null && !request.getParameter("search_content").isEmpty()){
			wherestr += "and M.content like '%"+request.getParameter("search_content")+"%' ";
		}
		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
			wherestr += "and M.sdt>= '"+request.getParameter("sdt")+"' ";
		}
		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
			wherestr += "and M.sdt<= '"+request.getParameter("edt")+"' ";
		}
		PageHelper.startPage(pages, pagesize);
       	List<Msg> list = this.msgService.getMsgsendlist(wherestr+orderstr);
        PageInfo<Msg> page = new PageInfo<Msg>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/addMsg.do")
	public String doadd(HttpServletRequest request,Model model){
		return "msg/msgAdd";
	}
	@RequestMapping("/deleteMsg.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		String setandwhere=" set delflag='TRUE' where id='"+id+"'";
		int result = this.msgService.updateMsgBySetAndWhere(setandwhere);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteMsgs.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		String setandwhere=" set delflag='TRUE' where id in ('"+ids+"')";
		int result = this.msgService.updateMsgBySetAndWhere(setandwhere);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteMsgRecv.do")
	public String dodelrecv(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		String setandwhere=" set delflag='TRUE' where masterid='"+id+"'";
		int result = this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteMsgRecvs.do")
	public String dodelrecvs(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		String setandwhere=" set delflag='TRUE' where masterid in ('"+ids+"')";
		int result = this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
		model.addAttribute("result", result);
		return "result";
	}
	/**为亿美短信方式保存消息和手机短信	
	 * @return result 发送成功，发送失败，无权限
	 */
	@RequestMapping("/saveMsgYM.do")
	public String dosaveym(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String mtypeid="";
		String result="";
		if(request.getParameter("mtype").equals("personal")){
			mtypeid=this.msgtypeService.getMsgType(" where name='个人信息'").get(0).getId();
		/*}else if(request.getParameter("mtype").equals("public")){
			mtypeid=this.msgtypeService.getMsgType(" where name='公告提醒'").get(0).getId();*/
		}else{
			mtypeid=request.getParameter("mtype");
		}
		//String msgtype=request.getParameter("mtype");
		String content=request.getParameter("content");
		String recvid=request.getParameter("recvid");
		String cuid=cu.getId();
		result=this.msgService.insertMsgSend(mtypeid,content,recvid,cuid);
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	/**为empp短信方式保存消息和手机短信	
	 * @return result 发送成功，发送失败，无权限
	 *//*
	@RequestMapping("/saveMsgEMPP.do")
	public String dosaveempp(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String mtypeid="";
		int result=0;
		if(request.getParameter("mtype").equals("personal")){
			mtypeid=this.msgtypeService.getMsgType(" where name='个人信息'").get(0).getId();
		}else if(request.getParameter("mtype").equals("public")){
			mtypeid=this.msgtypeService.getMsgType(" where name='公告提醒'").get(0).getId();
		}else{
			mtypeid=request.getParameter("mtype");
		}
		String content=request.getParameter("content");
		String recvid=request.getParameter("recvid");
		String cuid=cu.getId();
		result=this.msgService.insertMsgSendEmpp(mtypeid,content,recvid,cuid);
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}*/
	@RequestMapping("/viewMsgRecv.do")
	public String doviewrecv(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String msgId = request.getParameter("id");		
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1";		
		wherestr+=" and M.id='"+msgId+"' and V.delflag!='TRUE' and V.unitid='"+cu.getId()+"' ";
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);		
		if(request.getParameter("send")==null){
			String readstatus=list.get(0).getMrecv().get(0).getStatus();
			if(readstatus.equals("U")){
			//第一次浏览
				String setandwhere=" set status='R' ,readtime= '"+CommUtil.nowDate()+"' where id='"+list.get(0).getMrecv().get(0).getId()+"' ";
				this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
			}
		}
		User suser=this.userService.getUserById(list.get(0).getSuserid());
		model.addAttribute("msg", list.get(0));
		model.addAttribute("recv", cu.getCaption());
		model.addAttribute("suser", suser.getCaption());
		return "msg/msgViewRecv";
	}
	@RequestMapping("/viewMsgSend.do")
	public String doviewsend(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String msgId = request.getParameter("id");		
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1";		
		wherestr+=" and M.id='"+msgId+"' and M.suserid='"+cu.getId()+"' ";
		List<Msg> list = this.msgService.getMsgsend(wherestr+orderstr);		
		User suser=this.userService.getUserById(list.get(0).getSuserid());
		model.addAttribute("msg", list.get(0));
		model.addAttribute("recv", cu.getCaption());
		model.addAttribute("suser", suser.getCaption());
		return "msg/msgViewSend";
	}
	@RequestMapping("/viewMsgFast.do")
	public ModelAndView doviewfast(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String msgId = request.getParameter("id");
		String morder= request.getParameter("morder");
		String sdt=request.getParameter("sdt");
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1 ";
		wherestr+="and M.issms!='sms' and M.id<>'"+msgId+"' and M.delflag!='TRUE'";
		if(request.getParameter("send")!=null && request.getParameter("send").equals("true")){
		//发消息-查看消息
			wherestr+=" and M.suserid='"+cu.getId()+"'";
		}else{
		//收消息-查看消息
			wherestr+=" and V.delflag!='TRUE' and V.unitid='"+cu.getId()+"' ";
		}
		if(morder.equals("pre")){			
			orderstr+=" desc ";
			wherestr+=" and M.sdt<='"+sdt+"' ";
		}else if(morder.equals("next")){			
			orderstr+=" asc ";
			wherestr+=" and M.sdt>='"+sdt+"' ";
		}		
		List<Msg> list = this.msgService.getMsgrecvTop1(wherestr+orderstr);
		String result=null;	
		if(list.size()!=0){
			if(request.getParameter("send")!=null && request.getParameter("send").equals("false")){				
				String readstatus=list.get(0).getMrecv().get(0).getStatus();		
				if(readstatus.equals("U")){
				//收消息-第一次浏览
					String setandwhere=" set status='R' ,readtime= '"+CommUtil.nowDate()+"' where id='"+list.get(0).getMrecv().get(0).getId()+"' ";
					this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
				}
			}
			User suser=this.userService.getUserById(list.get(0).getSuserid());
			JSONArray json=JSONArray.fromObject(list.get(0));
			result="{\"recv\":\""+cu.getCaption()+"\",\"suser\":\""+suser.getCaption()+"\",\"rows\":"+json+"}";
		}else{
			if(morder.equals("next")){				
				result="{\"rows\":\"\",\"res\":\"已是最后一条消息\"}";
			}else{				
				result="{\"rows\":\"\",\"res\":\"已是第一条消息\"}";
			}
			
		}		
		
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/viewMsgStatus.do")
	public String doviewstatus(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String msgId = request.getParameter("id");		
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1";		
		wherestr+=" and M.id='"+msgId+"'  and V.masterid='"+msgId+"' ";
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);
		List<MsgRecv> msgrecvlist=list.get(0).getMrecv();
		String checker="";//已浏览者
		String notchecker="";//未浏览者
		for(int i=0;i<msgrecvlist.size();i++){
			String readstatus=msgrecvlist.get(i).getStatus();
			String accepterid=msgrecvlist.get(i).getUnitid();
			String readtime=CommUtil.formatDate("yyyy-MM-dd hh:mm:ss",  msgrecvlist.get(i).getReadtime());			
			User accepter=this.userService.getUserById(accepterid);
			if(readstatus.equals("U")){
				notchecker+=accepter.getCaption()+"，";
			}else{
				checker+=accepter.getCaption()+"["+readtime+"]，";
			}
		}		
		if(notchecker.length()>1){
			notchecker = notchecker.substring(0, notchecker.length()-1);
		}
		if(checker.length()>1){
			checker = checker.substring(0, checker.length()-1);
		}
		model.addAttribute("checker", checker);
		model.addAttribute("notchecker", notchecker);
		
		return "msg/msgViewstatus";
	}
	@RequestMapping("/getUnreadMsgNum.do")
	public String getunreadmsgnum(HttpServletRequest request,Model model){
		User cu= (User) request.getSession().getAttribute("cu");
		int res=0;
		String orderstr=" order by M.insdt desc";		
		String wherestr="  where 1=1";
		wherestr+=" and M.delflag!='TRUE' and V.delflag!='TRUE' and V.unitid='"+cu.getId()+"' and V.status='U'";		
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);
		model.addAttribute("result", list.size());
		return "result";
	}
	@SuppressWarnings("null")
	@RequestMapping("/viewSms.do")
	public String doviewsms(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");
		String msgId = request.getParameter("id");
		String recvid="";
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1";		
		wherestr+=" and M.id='"+msgId+"' ";
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);
		List<EmppSendUser> listesu=this.emppsenduserService.selectListByWhere(" where emppsendid='"+msgId+"'");
		List<User> listuserrecv= new ArrayList<User>();
		for(int i=0;i<listesu.size();i++){
			User recvuser=this.userService.getUserById(listesu.get(i).getRecuserid());
			listuserrecv.add(recvuser);//短信名单
			recvid+=recvuser.getId()+",";//短信名单
		}		
		User suser=this.userService.getUserById(list.get(0).getSuserid());
		List<Msg> viewlist = new ArrayList();
		viewlist.add(list.get(0));
		String parentid=msgId;
		String insdttmp=list.get(0).getInsdt();
		String wherestrtmp=" where 1=1 and M.pid='"+parentid+"' and M.insdt>'"+insdttmp+"' and M.issms='sms'";
		do{
			List<Msg> listtmp = this.msgService.getMsgrecv(wherestrtmp+orderstr);
			if(listtmp.isEmpty()){
				break;
		    }
			for(int i=0;i<listtmp.size();i++){
				viewlist.add(listtmp.get(i));
				parentid=listtmp.get(i).getId();
				insdttmp=listtmp.get(i).getInsdt();
		    }
			wherestrtmp=" where 1=1 and M.pid='"+parentid+"' and M.insdt>'"+insdttmp+"' and M.issms='sms'";
		}while(true);
		model.addAttribute("msg", list.get(0));	
		model.addAttribute("viewlist", viewlist);	
		model.addAttribute("listuserrecv", listuserrecv);	
		model.addAttribute("recvid", recvid);	
		model.addAttribute("suser", suser);		
		return "msg/smsView";
	}
	@RequestMapping("/sendnewSms.do")
	public String sendnewym(HttpServletRequest request,Model model){
		User cu = (User) request.getSession().getAttribute("cu");		
		String smsid=CommUtil.getUUID();
		int result=0;
			//发送短信
				//获得手机短信发送名单
			String[] recvids = request.getParameter("recvid").split(",");						
			List<User> userlist=new ArrayList();
			String[] mobilearr=new String[recvids.length];
			for(int i=0;i<recvids.length;i++){
				if (recvids[i].length() > 0){
					userlist.add(this.userService.getUserById(recvids[i]));
					mobilearr[i]=this.userService.getUserById(recvids[i]).getMobile();					
				}
			}			
			int i1 = 9999;
			try {
				i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").registEx("234147");				
				i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").sendSMS(mobilearr,
						title + request.getParameter("content"), "", 5);// 带扩展码				
			} catch (Exception e) {
				e.printStackTrace();
			}
			JSONArray json=null;
			//发送成功					
			if(i1 ==0){
				Msg sms=new Msg();
				sms.setContent(request.getParameter("content"));				
				sms.setId(smsid);
				sms.setSdt(CommUtil.nowDate());
				sms.setInsdt(CommUtil.nowDate());
				sms.setInsuser(cu.getId());
				sms.setDelflag("FALSE");
				sms.setTypeid(request.getParameter("typeid"));
				sms.setSuserid(cu.getId());	
				sms.setIssms("sms");
				sms.setPid(request.getParameter("parentid"));
				result = this.msgService.saveMsg(sms);
				json=JSONArray.fromObject(sms);
				//手机用户表
				for(int ii=0;ii<userlist.size();ii++){
					EmppSendUser esu=new EmppSendUser();
					esu.setId(CommUtil.getUUID());
					esu.setEmppsendid(smsid);
					esu.setSenduserid(cu.getId());
					esu.setSenddate(new Date());
					esu.setRecuserid(userlist.get(ii).getId());
					esu.setMobile(userlist.get(ii).getMobile());
					esu.setBackdate(null);
					esu.setInsertuserid(cu.getId());
					esu.setInsertdate(new Date());
					esu.setUpdateuserid(cu.getId());
					esu.setUpdatedate(new Date());
					this.emppsenduserService.save(esu);
					esu=null;
				}				
		   }		
		String resstr="{\"res\":\""+result+"\",\"parentid\":\""+smsid+"\",\"rows\":"+json+",\"susername\":\""+cu.getCaption()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}	
		@RequestMapping("/replymsg.do")
		public String doreplymsg(HttpServletRequest request,Model model){
			User cu = (User) request.getSession().getAttribute("cu");
			String msgId = request.getParameter("id");
			String recvid="";
			String orderstr=" order by M.insdt ";		
			String wherestr="where 1=1";		
			wherestr+=" and M.id='"+msgId+"' ";
			List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);	
			recvid=list.get(0).getSuserid();
			User suser=this.userService.getUserById(recvid);
			//该msg之后列表
			List<Msg> viewlist = new ArrayList();
			viewlist.add(list.get(0));
			String parentid=msgId;
			String insdttmp=list.get(0).getInsdt();
			String wherestrtmp=" where 1=1 and M.pid='"+parentid+"' and M.insdt>'"+insdttmp+"' and M.issms='msg'";
			do{
				List<Msg> listtmp = this.msgService.getMsgrecv(wherestrtmp+orderstr);
				if(listtmp.isEmpty()){
					break;
			    }
				for(int i=0;i<listtmp.size();i++){
					viewlist.add(listtmp.get(i));
					parentid=listtmp.get(i).getId();
					insdttmp=listtmp.get(i).getInsdt();
			    }
				wherestrtmp=" where 1=1 and M.pid='"+parentid+"' and M.insdt>'"+insdttmp+"' and M.issms='msg'";
			}while(true);
			//该msg之前列表
			List<Msg> viewlistpre = new ArrayList();
			String preparentid=list.get(0).getPid();
			String preinsdttmp=list.get(0).getInsdt();
			String prewherestrtmp=" where 1=1 and M.id='"+preparentid+"' and M.insdt<'"+preinsdttmp+"' and M.issms='msg'";
			String preorderstr=" order by M.insdt desc";
			do{
				List<Msg> listtmp2 = this.msgService.getMsgrecv(prewherestrtmp+preorderstr);
				if(listtmp2.isEmpty()){
					break;
			    }
				for(int i=0;i<listtmp2.size();i++){
					viewlistpre.add(listtmp2.get(i));
					preparentid=listtmp2.get(i).getPid();
					preinsdttmp=listtmp2.get(i).getInsdt();
			    }
				prewherestrtmp=" where 1=1 and M.id='"+preparentid+"' and M.insdt<'"+preinsdttmp+"' and M.issms='msg'";
			}while(true);
			if(!viewlistpre.isEmpty()){
				Collections.reverse(viewlistpre);
			}			
			viewlistpre.addAll(viewlist);
			model.addAttribute("msg", list.get(0));	
			model.addAttribute("viewlist", viewlistpre);	
			//model.addAttribute("listuserrecv", listuserrecv);	
			model.addAttribute("recvid", recvid);	
			model.addAttribute("suser", suser);//联系人	
			model.addAttribute("cu", cu);//用户本人
			return "msg/msgViewReply";
		}
		@RequestMapping("/replynewMsg.do")
		public String replynewmsg(HttpServletRequest request,Model model){
			User cu = (User) request.getSession().getAttribute("cu");
			String mtypeid="";
			int result=0;			
			mtypeid=this.msgtypeService.getMsgType(" where name='个人信息'").get(0).getId();			
			String content=request.getParameter("content");
			String recvid=request.getParameter("recvid");
			String cuid=cu.getId();
			
			MsgType mtype=new MsgType();		
			mtype=this.msgtypeService.getMsgType(" where T.id='"+mtypeid+"'").get(0);			
			mtypeid=mtype.getId();
			
			Msg msg=new Msg();
			msg.setContent(content);			
			msg.setId(CommUtil.getUUID());
			msg.setSdt(CommUtil.nowDate());
			msg.setInsdt(CommUtil.nowDate());
			msg.setInsuser(cuid);
			
			msg.setDelflag("FALSE");
			msg.setTypeid(mtypeid);
			msg.setSuserid(cuid);
			msg.setIssms("msg");
			msg.setPid(request.getParameter("parentid"));
			result = this.msgService.saveMsg(msg);		
			this.msgrecvService.saveRecv(recvid, msg.getId());	
			JSONArray json=JSONArray.fromObject(msg);
			String resstr="{\"res\":\""+result+"\",\"parentid\":\""+msg.getId()+"\",\"rows\":"+json+",\"susername\":\""+cu.getCaption()+"\"}";;
			model.addAttribute("result", resstr);
			return "result";
		}
//end	
}
