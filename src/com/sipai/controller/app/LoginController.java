package com.sipai.controller.app;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.user.User;
import com.sipai.service.base.LoginService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping(value = "app")
public class LoginController {

	@Resource
	private LoginService loginService;
	
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
		String j_username= request.getParameter("user");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if (null != cu) {
			JSONObject jsob = JSONObject.fromObject(cu);
			request.setAttribute("result", "{\"result\":\"pass\",\"re1\":" + 
					jsob.toString()	+ "}");
			jsob=null;
		} else {
			request.setAttribute("result", "{\"result\":\"no\"}");
		}
		return new ModelAndView("result");
	}
	
}
