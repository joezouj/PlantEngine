package com.sipai.tools;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.sipai.entity.base.ServerObject;
import com.sipai.entity.user.User;
import com.sipai.service.user.UserService;



/**
 * @author IBM 在线用户监听器
 */
public class Listener implements HttpSessionAttributeListener {

	public static HashMap<HttpSession, User> sessionlist = new HashMap<HttpSession, User>();//sessionid和用户id

	public void attributeAdded(HttpSessionBindingEvent se) {
		User cu;
		if (se.getName().equals("cu")) {
			cu = (User) se.getValue();
			Iterator<HttpSession> is = Listener.sessionlist.keySet().iterator();
			while (is.hasNext()) {
				HttpSession key = is.next();				
				if (Listener.sessionlist.get(key).getId().equalsIgnoreCase(cu.getId())&&!Listener.sessionlist.get(key).getId().equalsIgnoreCase("emp01")) {
					if(multilogin(Listener.sessionlist.get(key).getName())){
						//设置登录时间
						UserService userService = (UserService)SpringContextUtil.getBean("userService");
						int res = userService.saveUserTime(Listener.sessionlist.get(key));
						if(res>0){
							userService.updateTotaltimeByUserId(Listener.sessionlist.get(key).getId());	
						}
						System.out.println(key.getId()+":remove");
						key.removeAttribute("cu");
						break;
					}
				}
			}
			System.out.println(se.getSession().getId()+":add");	
			sessionlist.put(se.getSession(),cu);
			se.getSession().setAttribute("userlist", Listener.sessionlist);

		}
	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		User cu = null;
		if (se.getName().equals("cu")) {
			cu = (User) se.getValue();
			//设置登录时间
			UserService userService = (UserService)SpringContextUtil.getBean("userService");
			int res = userService.saveUserTime(cu);
			if(res>0){
				userService.updateTotaltimeByUserId(cu.getId());	
			}
			System.out.println(se.getSession().getId()+":remove");		
			sessionlist.remove(se.getSession());			
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
	}

	public boolean multilogin(String se) {
		boolean brtn=true;
		if(ServerObject.atttable.get("MULTILOGIN")!=null){
			String[] str=ServerObject.atttable.get("MULTILOGIN").split(",");
			for(int i=0;i<str.length;i++){
				if(se.equalsIgnoreCase(str[i])){
					brtn=false;
				}
			}
		}
		return brtn;
	}
}
