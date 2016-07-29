package com.sipai.controller.msg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.sipai.entity.msg.Msg;


@SuppressWarnings("serial")
public class MsgPushServlet extends HttpServlet  {

	static protected Hashtable<String, HttpServletResponse> connections = new Hashtable<String, HttpServletResponse>();
	static String context;
	static protected MessageSender messageSender = null;

	@Override
	public void init() throws ServletException {
		context = getServletContext().getContextPath();
		messageSender = new MessageSender();
		Thread messageSenderThread = new Thread(messageSender, "MessageSender["
				+ getServletContext().getContextPath() + "]");
		messageSenderThread.setDaemon(true);
		messageSenderThread.start();
	}

	@Override
	public void destroy() {
		connections.clear();
		messageSender.stop();
		messageSender = null;
	}
	


//	public void event(CometEvent event) throws IOException, ServletException {
//		HttpServletRequest request = event.getHttpServletRequest();
//		HttpServletResponse response = event.getHttpServletResponse();
//
//		if ("POST".equals(request.getMethod())
//				&& request.getParameter("action") != null
//				&& request.getParameter("action").equalsIgnoreCase("proxy")) {
//			request.setCharacterEncoding("UTF-8");
//			Msg msg = new Msg();
//			msg.setContent(request.getParameter("content"));
//			com.sipai.user.User user = (com.sipai.user.User)request.getSession(true).getAttribute("cu");
//			msg.set_susername(user.getCaption());
//			msg.setSuserid(user.getId());
//			messageSender.send(request.getParameter("userid"), msg);
//			event.close();
//			return;
//		}
//
//		if (event.getEventType() == CometEvent.EventType.BEGIN) {
//			event.setTimeout(60 * 1000 * 60 * 24);
//			log("Begin for session: " + request.getSession(true).getId());
//
//			response.setContentType("text/html;charset=UTF-8");
//			PrintWriter writer = response.getWriter();
//			writer
//			.println("<%@ page contentType=\"text/html;charset=UTF-8\" pageEncoding=\"UTF-8\" language=\"java\"%>");
//			writer.println("<head>");
//			writer.println("<meta http-equiv=\"pragma\" content=\"no-cache\">");
//			writer.println("<meta http-equiv=\"refresh\" content=\"3600\"> ");
//			writer.println("<meta http-equiv=\"cache-control\" content=\"no-cache\">");
//			writer.println("<meta http-equiv=\"expires\" content=\"0\">");
//			writer
//			.println("<link rel=\"StyleSheet\" href=\"CSS/comm2.css\" type=\"text/css\" />");
//			writer
//			.println("<script type=\"text/javascript\" src=\"JS/comm.js\"></script>");
//			writer.println("<script type=\"text/javascript\">");
//			writer.println("function doedit(o){");
//			writer
//			.println("    top.mainframe.location=o;");
//			writer.println("}</script>");
//			writer.println("</head><body bgcolor=\"#FFFFFF\">");
//			
//			writer.flush();
//			
//			synchronized (connections) {
//
//				if (request.getSession().getAttribute("cu") != null) {
//					String sessionid = request.getSession(true).getId();
//					String userid = ((com.sipai.user.User) request.getSession()
//							.getAttribute("cu")).getId();
//					if (connections.get(sessionid + "," + userid) != null) {
//						connections.remove(sessionid + "," + userid);
//						connections.put(sessionid + "," + userid, response);
//						System.out.println("connections new add " + sessionid + ","
//								+ userid);
//					}else{
//						connections.put(sessionid + "," + userid, response);
//						System.out.println("connections add " + sessionid + ","
//								+ userid);
//					}
//					pendingMessage(userid);// 建立新连接时才pending
//
//					userid = null;
//					sessionid = null;
//				}
//			}
//			
//
//		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
//		} else if (event.getEventType() == CometEvent.EventType.END) {
//			PrintWriter writer = response.getWriter();
//			writer.println("</body></html>");
//			event.close();
//		} else if (event.getEventType() == CometEvent.EventType.READ) {
//		}
//	}

	public class MessageSender implements Runnable {
		protected boolean running = true;
		protected Hashtable<String, ArrayList<Msg>> messages = new Hashtable<String, ArrayList<Msg>>();

		public MessageSender() {
		}

		public void stop() {
			running = false;
		}


		/**给指定的用户发送消息，-1时为所有在线的用户，对于系统发出的消息默认像系统用户发送一次
		 * @param user
		 * @param msg
		 */
		public void send(String user, Msg msg) {
			synchronized (messages) {
				if (user.equalsIgnoreCase("-1")) {
					synchronized (connections) {
						Iterator<String> csi = connections.keySet().iterator();
						while (csi.hasNext()) {
							String cid = csi.next().split(",")[1];
							if (messages.get(cid) == null) {
								messages.put(cid,
										new ArrayList<Msg>());
							}

							messages.get(cid).add(msg);
							cid = null;
						}
						csi = null;
					}
				} else {
					if (messages.get(user) == null) {
						messages.put(user, new ArrayList<Msg>());
					}
					messages.get(user).add(msg);
				}
				/*if(msg.getSuserid().equalsIgnoreCase("SYSTEM")){
					if (messages.get("SYSTEM") == null) {
						messages.put("SYSTEM", new ArrayList<Msg>());
					}
					messages.get("SYSTEM").add(msg);	
				}*/
				messages.notify();
			}
		}

		/**直接根据msg对象的内容进行消息的发送，根据msg.get_recvid()确定发送的对象，对于系统发出的消息默认像系统用户发送一次
		 * @param msg
		 */
		public void send(Msg msg,ArrayList recvid) {
			synchronized (messages) {
				ArrayList<String> userlist = recvid;
				if(userlist!=null){
					for(int i=0;i<userlist.size();i++){
						if(userlist.get(i).trim().length()>0){
							if (messages.get(userlist.get(i).trim()) == null) {
								messages.put(userlist.get(i).trim(), new ArrayList<Msg>());
							}
							msg.setContent("1");
							msg.setSuserid("-1");
							messages.get(userlist.get(i).trim()).add(msg);							
						}
					}
					messages.notify();
				}
			}
		}		

		public void run() {
			while (running) {
				if (messages.size() == 0) {
					try {
						synchronized (messages) {
							messages.wait();
						}
					} catch (InterruptedException e) {
						// Ignore
					}
				}
				synchronized (connections) {
					try{
						synchronized (messages) {
							//System.out.println(messages.size());
							Iterator<String> si = messages.keySet().iterator();
							while (si.hasNext()) {
								String userid = si.next();
								StringBuffer pendingMessages = new StringBuffer();
								if(messages.get(userid).size()==0){
									continue;
								}
								for (int i = 0; i < messages.get(userid).size(); i++) {
									//System.out.println(messages.get(userid)+"");
									/*if("blank".equals(messages.get(userid).get(i).getUrl())){
										pendingMessages.delete(0, pendingMessages.length());
									}else{*/
										pendingMessages.append("<table class=\"listtablenofix\" width=\"100%\" cellspacing=\"0\" id=\"listtable\">");
										if(messages.get(userid).get(i).getUrl()!=null && messages.get(userid).get(i).getUrl().trim().length()>0){
											
											pendingMessages.append("<tr onclick=\"doedit('");
											pendingMessages.append(messages.get(userid).get(i).getUrl());
											pendingMessages.append("')\" style=\"cursor:point;\"><td>");										
										
										}else{
											pendingMessages.append("<tr><td>");	
										}
										pendingMessages.append(messages.get(userid).get(i).getSdt());
										pendingMessages.append("</td></tr>");
										pendingMessages.append("<tr><td>");
										//pendingMessages.append(messages.get(userid).get(i).get_susername());
										pendingMessages.append("</td></tr>");								
										pendingMessages.append("<tr>");
										if(messages.get(userid).get(i).getUrl()!=null && messages.get(userid).get(i).getUrl().trim().length()>0){
											pendingMessages.append("<td onclick=\"mainopen('");
											pendingMessages.append(messages.get(userid).get(i).getUrl());
											pendingMessages.append("')\">");
											pendingMessages.append(messages.get(userid).get(i).getContent());
										}else{
											pendingMessages.append("<td>");
											pendingMessages.append(messages.get(userid).get(i).getContent());
										}
										pendingMessages.append("</td></tr>");
										pendingMessages.append("</table><br>");
										pendingMessages.append("<script>document.body.scrollTop = document.body.scrollHeight</script>");
										
										//2010-02-23原来使用每个消息的弹出方式，目前需要修改为系统提示您有几条短消息
//										if(messages.get(userid).get(i).getAlerturl()!=null && messages.get(userid).get(i).getAlerturl().trim().length()>0){
//											pendingMessages.append("<script>window.showModelessDialog('");
//											pendingMessages.append(messages.get(userid).get(i).getAlerturl());
//											pendingMessages.append("',window,'help:off;status:on;dialogWidth:500px;dialogHeight:250px;dialogTop:498px;dialogLeft:0px')</script>");
//										}else{
//											pendingMessages.append("<script>window.showModelessDialog('");
//											pendingMessages.append("msgaction.do?method=alert&id="+messages.get(userid).get(i).getId());
//											pendingMessages.append("',window,'help:off;status:on;dialogWidth:500px;dialogHeight:250px;dialogTop:498px;dialogLeft:0px')</script>");											
//										}
										//2010-02-23原来使用每个消息的弹出方式，目前需要修改为系统提示您有几条短消息
										pendingMessages.append("<script>window.showModelessDialog('");
										pendingMessages.append("msgaction.do?method=alert&count="+messages.get(userid).get(i).getContent());
										pendingMessages.append("',window,'help:off;status:0;scroll:0;resizable;1')</script>");											
										
										
										
									//}

								}
								messages.clear();
								if(pendingMessages.length()==0){
									continue;
								}
								Iterator<String> csi = connections.keySet()
								.iterator();
								while (csi.hasNext()) {
									String cid = csi.next();
									if (cid.split(",")[1].equalsIgnoreCase(userid)) {
										try {
											PrintWriter writer = connections.get(
													cid).getWriter();
											writer.println(pendingMessages.toString());											
											writer.println("<br>");
											writer.flush();
										} catch (IOException e) {
											//log("IOExeption sending message", e);
										}
									}
									cid = null;
								}
								csi = null;
								userid = null;
								pendingMessages.delete(0, pendingMessages.length());
								pendingMessages=null;
								
								//messages.remove(userid);
							}
						}
					}catch(Throwable e){
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**按照userid的指定发送消息，忽略消息中的recvid数组，userid为-1时想所有在线用户发送
	 * @param userid
	 * @param msg
	 */
	public static String sendMessage(String userid, Msg msg) {

		if(messageSender!=null){
			messageSender.send(userid,msg);
			return "sent";
		}else{
			return "false";
		}
	}

	/**按照msgrecvid数组中指定的用户发送消息
	 * @param msg
	 */
	public static String sendMessage(Msg msg,ArrayList recvid) {
		if(messageSender!=null){
			messageSender.send(msg,recvid);
			return "sent";
		}else{
			return "false";
		}
	}	

	/**读取用所有未读的信息
	 * @param userid
	 */
/*	public void pendingMessage(String userid) {
//		ArrayList<Msg> ma = new ArrayList<Msg>();// 为得到消息的信息
		MsgDAO md = new MsgDAO();
//		ma = md.getUserUnreadMsg(userid);
//		md = null;
//		if (ma != null) {
//			for (int i = 0; i < ma.size(); i++) {
//				if(messageSender!=null){
//					messageSender.send(userid, ma.get(i));
//				}
//			}
//		}
//		ma = null;
		int count = md.getUserUnreadMsgCount(userid);
		if(count>0){
			Msg msg= new Msg();
			msg.setContent(String.valueOf(count));
			messageSender.send(userid, msg);
		}
	}*/

}
