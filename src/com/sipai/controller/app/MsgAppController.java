package com.sipai.controller.app;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.user.User;
import com.sipai.service.base.LoginService;
import com.sipai.service.msg.MsgRecvService;
import com.sipai.service.msg.MsgServiceImpl;
import com.sipai.service.msg.MsgTypeService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping(value = "msgapp")
public class MsgAppController {

	@Resource
	private LoginService loginService;
	@Resource
	private MsgRecvService msgrecvService;
	@Resource
	private MsgServiceImpl msgService;
	@Resource
	private MsgTypeService msgtypeService;
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model) {
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			model.addAttribute("result","");
	        return new ModelAndView("result");
		}
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
		String result1="{\"pi\":[{\"pageNum\":\""+page.getPageNum()+"\",\"pageCount\":\""+page.getPages()+"\"}],\"re1\":"+json+"}";
		String result="{\"total\":"+page.getTotal()+",\"re1\":"+json+"}";
		model.addAttribute("result",result1);
        return new ModelAndView("result");
	}
	@RequestMapping("/delete.do")
	public ModelAndView dodel(HttpServletRequest request,Model model){
		String id= request.getParameter("id");
		String setandwhere=" set delflag='TRUE' where masterid='"+id+"'";
		int result = this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
//		model.addAttribute("result", result);
		
		if (result==1) {
			request.setAttribute("result", "{\"result\":\"yes\"}");
		} else {
			request.setAttribute("result", "{\"result\":\"no\"}");
		}
		return new ModelAndView("result");
	}
	@RequestMapping("/viewMsg.do")
	public ModelAndView viewMsg(HttpServletRequest request,Model model){
		String j_userid= request.getParameter("userid");
		String msgId = request.getParameter("id");		
		String orderstr=" order by M.insdt ";		
		String wherestr="where 1=1";		
		wherestr+=" and M.id='"+msgId+"' and V.delflag!='TRUE' and V.unitid='"+j_userid+"' ";
		List<Msg> list = this.msgService.getMsgrecv(wherestr+orderstr);		
		int result=0;
		if(request.getParameter("send")==null){
			String readstatus=list.get(0).getMrecv().get(0).getStatus();
			if(readstatus.equals("U")){
			//第一次浏览
				String setandwhere=" set status='R' ,readtime= '"+CommUtil.nowDate()+"' where id='"+list.get(0).getMrecv().get(0).getId()+"' ";
				result=this.msgrecvService.updateMsgRecvBySetAndWhere(setandwhere);
			}
		}
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	/**为亿美短信方式保存消息和手机短信	
	 * @return result 发送成功，发送失败，无权限
	 */
	@RequestMapping("/saveMsgYM.do")
	public String saveMsgYM(HttpServletRequest request,Model model){
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			model.addAttribute("result","{\"res\":\"0\"}");
	        return "result";
		}
		String mtypeid="";
		String result="";
		String mtypename=request.getParameter("mtypename");
		mtypeid=this.msgtypeService.getMsgType(" where name='"+mtypename+"'").get(0).getId();
		//String msgtype=request.getParameter("mtype");
		String content=request.getParameter("content");
		String recvid=request.getParameter("recvid");
		String cuid=cu.getId();
		result=this.msgService.insertMsgSend(mtypeid,content,recvid,cuid);
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
}
