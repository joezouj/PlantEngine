package com.sipai.controller.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.user.Unit;
import com.sipai.entity.user.User;
import com.sipai.service.user.UnitService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;
import com.sipai.tools.SessionManager;


@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;
	@Resource
	private UnitService unitService;
	
	@RequestMapping("/showUsers.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/user/userList";
	}
	@RequestMapping("/getUsers.do")
	public ModelAndView getUsers(HttpServletRequest request,Model model) {
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
			sort = " morder ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "user/getUsers.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_caption")!=null && !request.getParameter("search_caption").isEmpty()){
			wherestr += "and caption like '%"+request.getParameter("search_caption")+"%' ";
		}
		if(request.getParameter("search_sex")!=null && !request.getParameter("search_sex").isEmpty()){
			wherestr += "and sex = '"+request.getParameter("search_sex")+"' ";
		}
		if(request.getParameter("search_pid")!=null && !request.getParameter("search_pid").isEmpty()){
			List<Unit> unitlist=unitService.getUnitChildrenById(request.getParameter("search_pid"));
			String pidstr="";
			for(int i=0;i<unitlist.size();i++){
				pidstr += "'"+unitlist.get(i).getId()+"',";
			}
			if(pidstr!=""){
				pidstr = pidstr.substring(0, pidstr.length()-1);
				wherestr += "and pid in ("+pidstr+") ";
			}
		}
		
		PageHelper.startPage(pages, pagesize);
        List<User> list = this.userService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<User> page = new PageInfo<User>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";

		
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/addUser.do")
	public String doadd(HttpServletRequest request,Model model){
		return "user/userAdd";
	}
	
	@RequestMapping("/editUser.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.getUserById(userId);
		model.addAttribute("user", user);
		return "user/userEdit";
	}
	
	@RequestMapping("/viewUser.do")
	public String doview(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.getUserById(userId);
		model.addAttribute("user", user);
		return "user/userView";
	}
	
	@RequestMapping("/saveUser.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("user") User user){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.userService.checkNotOccupied(user.getId(), user.getName())){
			model.addAttribute("result", "{\"res\":\"用户名重复\"}");
			return "result";
		}
		if(user.getSerial()!=null&&!this.userService.checkSerialNotOccupied(user.getId(), user.getSerial())){
			model.addAttribute("result", "{\"res\":\"工号重复\"}");
			return "result";
		}
		if(user.getCardid()!=null&&!this.userService.checkCardidNotOccupied(user.getId(), user.getCardid())){
			model.addAttribute("result", "{\"res\":\"卡号重复\"}");
			return "result";
		}
		String userId = CommUtil.getUUID();
		user.setId(userId);
		user.setPassword("123456");
		user.setInsuser(cu.getId());
		user.setInsdt(CommUtil.nowDate());
		
		int result = this.userService.saveUser(user);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+userId+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/updateUser.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("user") User user){
		if(!this.userService.checkNotOccupied(user.getId(), user.getName())){
			model.addAttribute("result", "{\"res\":\"用户名重复\"}");
			return "result";
		}
		if(user.getSerial()!=null&&!this.userService.checkSerialNotOccupied(user.getId(), user.getSerial())){
			model.addAttribute("result", "{\"res\":\"工号重复\"}");
			return "result";
		}
		if(user.getCardid()!=null&&!this.userService.checkCardidNotOccupied(user.getId(), user.getCardid())){
			model.addAttribute("result", "{\"res\":\"卡号重复\"}");
			return "result";
		}
		int result = this.userService.updateUserById(user);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+user.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/deleteUser.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.userService.deleteUserById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deleteUsers.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.userService.deleteUserByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/doimport.do")
	public String doimport(HttpServletRequest request,Model model){
		return "user/importUsers";
	}
	
	@RequestMapping(value = "downtemplate.do")
	public void downtemplate(HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
			//文件的实际地址
			String filepath = request.getSession().getServletContext().getRealPath("/");
			String filename = "用户数据导入格式样本.xls";
			//兼容Linux路径
			filepath = filepath+"Template"+System.getProperty("file.separator")+"user"+System.getProperty("file.separator")+filename;			
			FileUtil.downloadFile(response,filename,filepath);
	}
	
	@RequestMapping(value = "importUsers.do")
	public ModelAndView importUsers(@RequestParam MultipartFile[] file, HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
		//要存入的实际地址
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pjName = request.getContextPath().substring(1, request.getContextPath().length());
		realPath = realPath.replace(pjName,"Temp");
		
		String result = userService.doimport(realPath, file);
		model.addAttribute("result",result);
    	return new ModelAndView("result");
	}
	
	@RequestMapping("/exportUsers.do")
	public String exportUsers(HttpServletRequest request,Model model) throws IOException{
		User cu = (User)request.getSession().getAttribute("cu");
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else {
			sort = " morder ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "user/getUsers.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_caption")!=null && !request.getParameter("search_caption").isEmpty()){
			wherestr += "and caption like '%"+request.getParameter("search_caption")+"%' ";
		}
		if(request.getParameter("search_sex")!=null && !request.getParameter("search_sex").isEmpty()){
			wherestr += "and sex = '"+request.getParameter("search_sex")+"' ";
		}
        List<User> list = this.userService.selectListByWhere(wherestr+orderstr);
        
		//导出文件到指定目录,兼容Linux
        String filePath = "";
        if("\\".equals(System.getProperty("file.separator"))){
        	filePath ="D:"+System.getProperty("file.separator")+"用户数据表.xls";
        }
        if("/".equals(System.getProperty("file.separator"))){
        	filePath ="/user"+System.getProperty("file.separator")+"用户数据表.xls";
        }
        
        String result = this.userService.exportUsers(filePath, list);
        model.addAttribute("result",result);
	    return "result";
	}
	
	@RequestMapping("/exportUsersByResponse.do")
	public void exportUsersByResponse(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException{
		User cu = (User)request.getSession().getAttribute("cu");
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else {
			sort = " morder ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "user/getUsers.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_caption")!=null && !request.getParameter("search_caption").isEmpty()){
			wherestr += "and caption like '%"+request.getParameter("search_caption")+"%' ";
		}
		if(request.getParameter("search_sex")!=null && !request.getParameter("search_sex").isEmpty()){
			wherestr += "and sex = '"+request.getParameter("search_sex")+"' ";
		}
        List<User> list = this.userService.selectListByWhere(wherestr+orderstr);
        
		//导出文件到指定目录,兼容Linux
        String fileName = "用户数据表.xls";
        this.userService.exportUsersByResponse(response, fileName, list);
	}
	
	@RequestMapping("/resetPwd.do")
	public String resetPwd(HttpServletRequest request,Model model,
			@RequestParam String id){
		User user=userService.getUserById(id);
		int result = this.userService.resetpassword(user, "123456");
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/queryUsers.do")
	public String doqueryUsers(HttpServletRequest request,Model model){
		String orderstr=" order by name";
		
		String wherestr="where 1=1 ";
		
		if(request.getParameter("queryusername")!=null && !request.getParameter("queryusername").isEmpty()){
			String n=null;
			try {
				n = java.net.URLDecoder.decode(request.getParameter("queryusername"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			wherestr += " and name like '%"+n+"%'  ";
		}
		if(request.getParameter("orgid")!=null && !request.getParameter("orgid").isEmpty()){
			wherestr += " and  pid='"+request.getParameter("orgid")+"'  ";
		}	
		List<User> list=new ArrayList<User>();
		List<User> listself=new ArrayList<User>();
		list=this.unitService.getChildrenUsersById(request.getParameter("orgid"));
		listself = this.userService.selectListByWhere(wherestr+orderstr);
		list.addAll(listself);
		JSONArray json=JSONArray.fromObject(list);
		String result = "{\"rows\":"+json+"}";
		model.addAttribute("result",result);
	    return "result";
	}
	@RequestMapping("/userForSelect.do")
	public String userForSelect(HttpServletRequest request,Model model){		
		model.addAttribute("recvidname",request.getParameter("recvidname"));		
		return "user/userForSelect";
	}
	@RequestMapping("/userForSingleSelect.do")
	public String userForSingleSelect(HttpServletRequest request,Model model){		
		model.addAttribute("recvidname",request.getParameter("recvidname"));		
		return "user/userForSingleSelect";
	}
	@RequestMapping("/updateJobByUserid.do")
	public String updateJobUser(HttpServletRequest request,Model model,
			@RequestParam String jobstr,@RequestParam String userid,@RequestParam String unitid){
		int result=userService.updateJobByUserid(jobstr, userid, unitid);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/getRecvusersJson.do")
	public String getRecvusersJson(HttpServletRequest request,Model model){
		String recvid=request.getParameter("recvid");
		String [] recvids=recvid.split(",");
		List<User> list=new ArrayList<User>();
		for(int i=0;i<recvids.length;i++){
			list.add(this.userService.getUserById(recvids[i]));
		}
		JSONArray json=JSONArray.fromObject(list);
		String result = "{\"rows\":"+json+"}";
		model.addAttribute("result",result);
	    return "result";
	}
	@RequestMapping("/isOnline.do")
	public String isOnline(HttpServletRequest request,Model model){
		boolean result = false;
		SessionManager sessionManager = new SessionManager();
		result=sessionManager.isOnline(request.getSession());
		model.addAttribute("result",result);
	    return "result";
	}
}
