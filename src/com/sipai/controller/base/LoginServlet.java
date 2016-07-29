package com.sipai.controller.base;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.user.User;
import com.sipai.entity.user.UserPower;
import com.sipai.service.base.LoginService;
import com.sipai.service.user.MenuService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.SessionManager;

@Controller
@RequestMapping(value = "Login")
public class LoginServlet {

	@Resource
	private LoginService loginService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	private MenuService menuService;
	
	@RequestMapping(value = "login.do", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (SessionManager.isOnline(session)){
			return new ModelAndView("main");
		}
		return new ModelAndView("login");
	}
	
	@RequestMapping(value = "logout.do", method = RequestMethod.POST)
	public ModelAndView logout(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		HttpSession session = request.getSession(false);
		if (null != session){
			session.invalidate();
		}
		return null;
	}
	
	
	@RequestMapping(value = "validate.do", method = RequestMethod.POST)
	public ModelAndView validate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "j_username" ,required=false) String j_username,
			@RequestParam(value = "j_password" ,required=false) String j_password,
			@RequestParam(value = "j_cardid" ,required=false) String j_cardid) {

		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}else if(j_cardid!=null){
			cu= loginService.CardLogin(j_cardid);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != cu) {
			//设置cu其他信息
			cu.setCurrentip(request.getRemoteAddr());
			cu.setLastlogintime(CommUtil.nowDate());
			
			HttpSession currentSession = request.getSession(false);
			if (null != currentSession){
				currentSession.invalidate();
			}
			
			//设置session
			HttpSession newSession = request.getSession(true);
			newSession.setAttribute("cu", cu);
			
			map.put("result", true);

		} else {
			map.put("result", false);
		}
		
		return new ModelAndView("result", map);
	}
	
	private boolean checkPassword(String psw) {
		boolean result = false;
		String regex = "^[a-zA-Z0-9]+$";
		if (null != psw) {
			result = psw.matches(regex);
		}
		return result;
	}

	@RequestMapping(value = "resetPwd.do", method = RequestMethod.POST)
	public ModelAndView resetPwd(HttpServletRequest request,
			HttpServletResponse response, Model model,
			@RequestParam(value = "loadNewPassword") String loadNewPassword) {
		User cu = (User)request.getSession().getAttribute("cu");
		String loadOldPassword=cu.getPassword();
				
		String result = "";
		if (checkPassword(loadOldPassword) && checkPassword(loadNewPassword)) {
			if (loadOldPassword.equals(loadNewPassword)) {
				result= "请不要与原密码相同";
			} else {
				if (userService.resetpassword(cu,loadNewPassword)==1) {
					result = "成功";
				} else {
					result = "用户不存在或原密码错误，请重新输入";
				}
			}
		} else {
			result= "请输入正确信息，密码只可以是字母或者数字";
		}
//		System.out.println(result);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
}
