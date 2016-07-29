package com.sipai.tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.sipai.entity.user.User;
import com.sipai.entity.user.UserPower;
import com.sipai.service.user.MenuService;

public class SessionManager implements HttpSessionListener {

	private static Map<String, HttpSession> onlineSessions = Collections.synchronizedMap(new HashMap<String, HttpSession>());
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		onlineSessions.put(event.getSession().getId(), event.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		onlineSessions.remove(event.getSession().getId());
	}

	public static Map<String, HttpSession> getOnlineSessions(){
		return onlineSessions;
	}
	
	public static boolean isOnline(HttpSession session) {
		User cu = getCu(session);
		if (cu != null && cu.getId() != null && cu.getName() != null) {
			return true;
		}
		return false;
	}
	
	public static User getCu(HttpSession session) {
		if (null != session) {
			if (null != session.getAttribute("cu")) {
				return (User) session.getAttribute("cu");
			}
		}
		return null;
	}
	
	public static void setAccount(HttpSession session, User cu) {
		if (null != session) {
			session.setAttribute("cu", cu);
		}
	}
	
	public boolean havePermission(HttpSession session, String url){
		User cu=(User) session.getAttribute("cu");
		if(cu!=null){
			MenuService menuService = (MenuService) SpringContextUtil.getBean("menuService");
			List<UserPower> list=menuService.selectFuncByUserId(cu.getId());
			for(UserPower obj:list){
				if(obj.getLocation().equals(url)){
					return true;
				}
			}
		}
		return false;
	}
}
