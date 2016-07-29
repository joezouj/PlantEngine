package com.sipai.controller.app;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.sql.Result;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.msg.Sms;
import com.sipai.service.msg.MsgService;
import com.sipai.service.msg.MsgServiceImpl;
import com.sipai.service.user.MenuService;
import com.sipai.tools.SpringContextUtil;

public class PushSmsServlet extends HttpServlet {
	private static int index = 1;
	private static ArrayList<String> idlistlast;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//System.out.println(index);
		// List<Sms> smsList = new ArrayList<Sms>();
		Sms sms=null;
		String orderstr=" order by M.insdt desc";		
		String wherestr="  where 1=1";
		wherestr+=" and M.issms!='sms' and M.delflag!='TRUE' and V.delflag!='TRUE' and V.unitid='"+request.getParameter("userid")+"' and V.status='U'";
		MsgService msgService = (MsgService) SpringContextUtil.getBean("msgService");
		List<Msg> list = msgService.getMsgrecv(wherestr+orderstr);
		Boolean flag=false;
		ArrayList<String> idlist=new ArrayList<String>();
		int i,j;
//		System.out.println(list.size());
 		for(i=0;i<list.size();i++){
 			String iditem=list.get(i).getId().toString();
			idlist.add(iditem);
			if(idlistlast==null){
				flag=true;
			}else{				
				for(j=0;j<idlistlast.size();j++){
					if(iditem.contains(idlistlast.get(j))){
						break;
					}	
				}
				if(j==idlistlast.size()){
					flag=true;
					
				}
			}
		}
		//request.setAttribute("pageinfo", id.getPi());
		if( list!=null&&list.size()>0){
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			sms = new Sms(index,
					"您有新消息，请及时查收!", dateString,
					flag.toString(),String.valueOf(list.size()));
			
			
			idlistlast=idlist;
		}else{
			////
		}	
		index++;
		// smsList.add(sms);
		// sms = new Sms(0, "����İ���", "2013-04-04", "13522224444");
		// smsList.add(sms);
		// sms = new Sms(1, "�������İ���", "2013-05-05", "13522224444");
		// smsList.add(sms);

		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		String json = gson.toJson(sms);
		out.write(json);
		out.flush();
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
