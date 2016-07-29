package com.sipai.service.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.controller.msg.Client;
import com.sipai.dao.msg.MsgDao;
import com.sipai.entity.msg.EmppAdmin;
import com.sipai.entity.msg.EmppSendUser;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.msg.MsgType;
import com.sipai.entity.user.User;
import com.sipai.entity.base.ServerObject;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;

@Service("msgService")
public class MsgServiceImpl implements MsgService{
	@Resource
	private MsgDao msgDao;	
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
	@Override
	public Msg getMsgById(String msgId) {
		// TODO Auto-generated method stub
		return this.msgDao.selectByPrimaryKey(msgId);
	}

	@Override
	public int saveMsg(Msg msg) {
		// TODO Auto-generated method stub
		return this.msgDao.insert(msg);
	}

	@Override
	public int updateMsgById(Msg msg) {
		// TODO Auto-generated method stub
		return this.msgDao.updateByPrimaryKeySelective(msg);
	}

	@Override
	public int deleteMsgById(String msgId) {
		// TODO Auto-generated method stub
		return this.msgDao.deleteByPrimaryKey(msgId);
	}

	@Override
	public int deleteMsgByWhere(String whereStr) {
		// TODO Auto-generated method stub
		Msg msg = new Msg();
		msg.setWhere(whereStr);
		System.out.println(msg.getWhere());
		return this.msgDao.deleteByWhere(msg);
	}

	@Override
	public List<Msg> selectList() {
		// TODO Auto-generated method stub
		Msg msg = new Msg();
		return this.msgDao.selectList(msg);
	}

	@Override
	public List<Msg> selectListByWhere(String wherestr) {
		// TODO Auto-generated method stub
		Msg msg = new Msg();
		msg.setWhere(wherestr);
		return this.msgDao.selectListByWhere(msg);
	}

	@Override
	//getMsgrecv联表查询取得符合条件的msg，不局限于接收或者发送者使用。
	public List<Msg> getMsgrecv(String wherestr) {
		// TODO Auto-generated method stub
		Msg msg = new Msg();
		msg.setWhere(wherestr);
		return this.msgDao.getMsgrecv(msg);
	}
	public List<Msg> getMsgsend(String wherestr) {
		Msg msg = new Msg();
		msg.setWhere(wherestr);
		return this.msgDao.getMsgsend(msg);
	}
	public List<Msg> getMsgsendlist(String wherestr) {
		Msg msg = new Msg();
		msg.setWhere(wherestr);
		return this.msgDao.getMsgsendlist(msg);
	}
	public List<Msg> getMsgrecvTop1(String wherestr) {
		// TODO Auto-generated method stub
		Msg msg = new Msg();
		msg.setWhere(wherestr);
		return this.msgDao.getMsgrecvTop1(msg);
	}
	@Override
	public int updateMsgBySetAndWhere(String setandwhere) {
		// TODO Auto-generated method stub
		Msg msg=new Msg();
		msg.setWhere(setandwhere);
		return this.msgDao.updateBySetAndWhere(msg);
	}
	/*public String proxy(Msg msg){
		if(ServerObject.getAtttable().get("OASERVER")!=null){
			CommSQL cs= new CommSQL();
			//cs.setDbservername(ServerObject.getAtttable().get("OASERVER"));
			cs.setXservername(ServerObject.getAtttable().get("OASERVER"));
			try{
				if(msg.get_recvid()!=null){
					for(int i = 0;i<msg.get_recvid().size();i++){
						cs.update("insert into SMS(FROM_ID,TO_ID,SMS_TYPE,CONTENT,SEND_TIME,REMIND_FLAG,REMIND_URL) values('"+msg.getSuserid()+"','"+msg.get_recvid().get(i)+"','7','"+msg.getContent()+"','"+msg.getSdt()+"','1','http://"+ServerObject.getAtttable().get("LOCALADDRESS")+"')");
						//cs.update("insert into SMS2(FROM_ID,PHONE,CONTENT,SEND_TIME,SEND_FLAG) values('"+msg.getSuserid()+"','"+msg.get_recvid().get(i)+"','7','"+msg.getContent()+"','"+msg.getSdt()+"','1','http://"+ServerObject.getAtttable().get("LOCALADDRESS")+"')");
						//ResultSet rs = cs.query("select LAST_INSERT_ID()");
						if (rs.next()){
							//File f = new File("D:\\MYOA\\attach\\new_sms\\"+msg.get_recvid().get(i)+"\\"+rs.getLong(1)+".sms");
							//new File("D:\\MYOA\\attach\\new_sms\\1\\"+rs.getLong(1)+".sms");
							//new File("D:\\"+rs.getInt(1)+".sms");
							new File("D:\\1.sms");
						}
					}
					return null;
				}else{
					return "消息插入出错";
				}
			}catch(Throwable e){
				e.printStackTrace();
			}
			return "读取自增id出错";
		}
		return "缺少配置信息OASERVER";

	}*/
	/**为根据消息类型中文名称获取msgtype的id的功能，以便调用发送消息函数
	 * @param msgtypename 消息类型中文名
	 * @return msgtypeid msgtype消息类型id
	 */
	public String getMsgTypeId( String msgtypename){
		String msgtypeid=this.msgtypeService.getMsgType(" where name like '%"+msgtypename+"%' order by insdt").get(0).getId();
		return msgtypeid;
	}
	
	/**为亿美短信发送消息和手机短信的功能,发送成功的消息插入表tb_message，消息接受者列表tb_message_recv，短信接受者列表tb_empp_emppsenduser
	 * @param msgtypeid 为发送类型id,使用时可调用消息类型选择界面，或使用获得typeid函数
	 * @param content 为发送手机的信息内容
	 * @param recvid 为接收手机的用户id,用,区分多个
	 * @param cuid 为发送手机用户id，手机发送人不允许为空，判断权限 
	 * @return result 发送成功，发送失败，无权限
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public String insertMsgSend(String msgtypeid,String content,String recvid,String cuid){		
		String mtypeid="";		
		int result=0;
		MsgType mtype=new MsgType();		
		mtype=this.msgtypeService.getMsgType(" where T.id='"+msgtypeid+"' order by insdt").get(0);
		String sendway=mtype.getSendway();
		String[] recvids = recvid.split(",");
		//判断权限
		List<String> unsendusers=new ArrayList();
		if(!mtype.getRole().isEmpty()){				
			for(int i=0;i<recvids.length;i++){
				User useri=this.userService.getUserById(recvids[i]);
				List mtyperoles=mtype.getRole();
				mtyperoles.retainAll(useri.getRoles());
					if(mtyperoles.isEmpty()){
						unsendusers.add(recvids[i]);
						recvids[i]="";
					}
			}			
		}
	/*	if(!mtype.getMsguser().isEmpty()){
			int flagmu=0;
			for(int j=0;j<mtype.getMsguser().size();j++){
				if(mtype.getMsguser().get(j).getId().equals(cuid)){
					flagmu=1;
					break;
				}
			}
			if(flagmu==0){
				return "发送失败，此类型消息当前用户无发送权限";
			}
		}
		if(!mtype.getSmsuser().isEmpty()){
			int flagsu=0;
			for(int z=0;z<mtype.getSmsuser().size();z++){
				if(mtype.getSmsuser().get(z).getId().equals(cuid)){
					flagsu=1;
					break;
				}
			}
			if(flagsu==0){
				return "发送失败，此类型短信当前用户无发送权限";
			}
		}*/
		
		mtypeid=mtype.getId();		
		Msg msg=new Msg();
		msg.setContent(content);
		String msgId = CommUtil.getUUID();
		msg.setId(msgId);
		msg.setSdt(CommUtil.nowDate());
		msg.setInsdt(CommUtil.nowDate());
		msg.setInsuser(cuid);
		msg.setDelflag("FALSE");
		msg.setTypeid(mtypeid);
		msg.setSuserid(cuid);
		msg.setIssms("msg");
		if(!sendway.equals("sms")){
			//仅消息
			result = this.saveMsg(msg);		
			this.msgrecvService.saveRecv(recvid, msg.getId());
			if(sendway.equals("both")){
			//消息+短信：				
				String smsid=CommUtil.getUUID();								
				//发送短信
					//获得手机短信发送名单				
				//ArrayList<String> recvidlist=new ArrayList<String>();				
				List<User> userlist=new ArrayList();
				String[] mobilearr=new String[recvids.length];
				for(int i=0;i<recvids.length;i++){
					if (recvids[i].length() > 0){
						userlist.add(this.userService.getUserById(recvids[i]));
						mobilearr[i]=this.userService.getUserById(recvids[i]).getMobile();
						//recvidlist.add(recvids[i]);
					}
				}			
				int i1 = 9999;
				try {
					i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").registEx("234147");
					System.out.println("testTegistEx:" + i1);
					i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").sendSMS(mobilearr,
							title + content, "", 5);// 带扩展码
					System.out.println("testSendSMS=====" + i1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//发送成功
				//保存短信
				if(i1 ==0){				
					msg.setId(smsid);
					msg.setIssms("sms");
					result = this.saveMsg(msg);
					//手机用户表
					for(int ii=0;ii<userlist.size();ii++){
						EmppSendUser esu=new EmppSendUser();
						esu.setId(CommUtil.getUUID());
						esu.setEmppsendid(smsid);
						esu.setSenduserid(cuid);
						esu.setSenddate(new Date());
						esu.setRecuserid(userlist.get(ii).getId());
						esu.setMobile(userlist.get(ii).getMobile());
						esu.setBackdate(null);
						esu.setInsertuserid(cuid);
						esu.setInsertdate(new Date());
						esu.setUpdateuserid(cuid);
						esu.setUpdatedate(new Date());
						this.emppsenduserService.save(esu);
						esu=null;
					}
			}
		  }
		}else{
		//仅发短信
			String smsid=CommUtil.getUUID();
			//发送短信
				//获得手机短信发送名单			
			//ArrayList<String> recvidlist=new ArrayList<String>();				
			List<User> userlist=new ArrayList();
			String[] mobilearr=new String[recvids.length];
			for(int i=0;i<recvids.length;i++){
				if (recvids[i].length() > 0){
					userlist.add(this.userService.getUserById(recvids[i]));
					mobilearr[i]=this.userService.getUserById(recvids[i]).getMobile();
					//recvidlist.add(recvids[i]);
				}
			}			
			int i1 = 9999;
			try {
				i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").registEx("234147");
				System.out.println("testTegistEx:" + i1);
				i1 = new Client("6SDK-EMY-6688-KJTSR", "zaqwsx").sendSMS(mobilearr,
						title + content, "", 5);// 带扩展码
				System.out.println("testSendSMS=====" + i1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//发送成功					
			if(i1 ==0){				
				msg.setId(smsid);
				msg.setIssms("sms");
				result = this.saveMsg(msg);
				//手机用户表
				for(int ii=0;ii<userlist.size();ii++){
					EmppSendUser esu=new EmppSendUser();
					esu.setId(CommUtil.getUUID());
					esu.setEmppsendid(smsid);
					esu.setSenduserid(cuid);
					esu.setSenddate(new Date());
					esu.setRecuserid(userlist.get(ii).getId());
					esu.setMobile(userlist.get(ii).getMobile());
					esu.setBackdate(null);
					esu.setInsertuserid(cuid);
					esu.setInsertdate(new Date());
					esu.setUpdateuserid(cuid);
					esu.setUpdatedate(new Date());
					this.emppsenduserService.save(esu);
					esu=null;
				}
				
		   }
		}
		String unsendusername = "";
		if(!unsendusers.isEmpty()){			 
			for(int z=0;z<unsendusers.size() ;z++){
				unsendusername+=this.userService.getUserById(unsendusers.get(z)).getCaption()+",";
			}
		}
		if(result==1){
			if(sendway.equals("sms")){
				if(!unsendusername.equals("")){
					return "短信未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "短信发送成功";
				}
			}else if(sendway.equals("msg")){				
				if(!unsendusername.equals("")){
					return "消息未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "消息发送成功";
				}
			}else{
				if(!unsendusername.equals("")){
					return "未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "发送成功";
				}
			}			
		}else{
			return "发送失败";
		}
	}
	/**为empp发送消息和手机短信的功能,发送成功的消息插入表tb_message，消息接受者列表tb_message_recv，短信接受者列表tb_empp_emppsenduser
	 * @param msgtypeid 为发送类型id,使用时可调用消息类型选择界面，或使用获得typeid函数
	 * @param content 为发送手机的信息内容
	 * @param recvid 为接收手机的用户id,用,区分多个
	 * @param cuid 为发送手机用户id，手机发送人不允许为空，判断权限 
	 * @return result 发送成功，发送失败，无权限
	 *//*
	@SuppressWarnings("unused")
	public String insertMsgSendEmpp(String msgtypeid,String content,String recvid,String cuid){	
		String mtypeid="";		
		int result=0;
		MsgType mtype=new MsgType();		
		mtype=this.msgtypeService.getMsgType(" where T.id='"+msgtypeid+"'").get(0);
		String sendway=mtype.getSendway();
		String[] recvids = recvid.split(",");
		//判断权限
		List<String> unsendusers=new ArrayList();
		if(!mtype.getRole().isEmpty()){				
			for(int i=0;i<recvids.length;i++){
				User useri=this.userService.getUserById(recvids[i]);
				List mtyperoles=mtype.getRole();
				mtyperoles.retainAll(useri.getRoles());
					if(mtyperoles.isEmpty()){
						unsendusers.add(recvids[i]);
						recvids[i]="";
					}
			}			
		}
		mtypeid=mtype.getId();
		Msg msg=new Msg();
		msg.setContent(content);
		String msgId = CommUtil.getUUID();
		msg.setId(msgId);
		msg.setSdt(CommUtil.nowDate());
		msg.setInsdt(CommUtil.nowDate());
		msg.setInsuser(cuid);
		msg.setDelflag("FALSE");
		msg.setTypeid(mtypeid);
		msg.setSuserid(cuid);
		msg.setIssms("msg");
		if(!sendway.equals("sms")){			
			result = this.saveMsg(msg);		
			this.msgrecvService.saveRecv(recvid, msg.getId());
			if(sendway.equals("both")){
				//消息+短信：0保存短信
				String smsid=CommUtil.getUUID();
				msg.setId(smsid);
				msg.setIssms("sms");
				result = this.saveMsg(msg);
					//发送短信:1获得手机短信管理
				String emppAdminid="";
				String emppadminwhere=" where emppname='部门短信'";
				String emppadminorder=" order by id";
				ArrayList<EmppAdmin> emppadminlist = (ArrayList)this.emppadminService.selectListByWhere(emppadminwhere+emppadminorder);
				if(emppadminlist!=null && emppadminlist.size()>=1){
					emppAdminid=emppadminlist.get(0).getId();
					try{
						com.sipai.tools.EncryptionDecryption en=new com.sipai.tools.EncryptionDecryption("usertel");
						emppadminlist.get(0).setAccountid(en.decrypt(emppadminlist.get(0).getAccountid()));	
						emppadminlist.get(0).setAccname(en.decrypt(emppadminlist.get(0).getAccname()));	
						emppadminlist.get(0).setPassword(en.decrypt(emppadminlist.get(0).getPassword()));	
						emppadminlist.get(0).setServiceid(en.decrypt(emppadminlist.get(0).getServiceid()));	
						en=null;
					}catch(Exception e){
						e.printStackTrace();
					}
				}
						//2获得手机短信发送名单recvids				
						//3手机用户表
				List<User> userlist=new ArrayList();
				for(int i=0;i<recvids.length;i++){
					if (recvids[i].length() > 0){
						userlist.add(this.userService.getUserById(recvids[i]));
					}
				}
				for(int ii=0;ii<userlist.size();ii++){
					EmppSendUser esu=new EmppSendUser();
					esu.setId(CommUtil.getUUID());
					esu.setEmppsendid(smsid);
					esu.setSenduserid(cuid);
					esu.setSenddate(new Date());
					esu.setRecuserid(userlist.get(ii).getId());
					esu.setMobile(userlist.get(ii).getMobile());
					esu.setBackdate(null);
					esu.setInsertuserid(cuid);
					esu.setInsertdate(new Date());
					esu.setUpdateuserid(cuid);
					esu.setUpdatedate(new Date());
					this.emppsenduserService.save(esu);
					esu=null;
				}
						//4发手机短信
				ArrayList<String> slist=new ArrayList<String>();
				if(emppadminlist!=null && emppadminlist.size()>=1 && recvids.length>=1){
					for(int i=0;i<userlist.size();i++){
						if((i%5)<5){ //如果取余数小于5，则添加如数组
							slist.add(userlist.get(i).getMobile());							
						}
						if((i%5)==4 || (recvids.length)==(i+1) ){ //如果取余数正好为4，则需要发送数组
							 this.emppsendService.dosendmsg(emppadminlist, slist,content);	
							slist=new ArrayList<String>();
							try{ //需要延迟1秒
						        Thread.sleep(1000);   //延时1秒
							}catch(InterruptedException   e){} 
						}
					}
					
				}
				slist=null;
				emppadminlist=null;
					//
			}
		}else{
			//仅发短信
			msg.setIssms("sms");
			result = this.saveMsg(msg);
				//发送短信
			//短信：0保存短信
			String smsid=CommUtil.getUUID();
			msg.setId(smsid);
			msg.setIssms("sms");
			result = this.saveMsg(msg);
				//发送短信:1获得手机短信管理
			String emppAdminid="";
			String emppadminwhere=" where emppname='部门短信'";
			String emppadminorder=" order by id";
			ArrayList<EmppAdmin> emppadminlist = (ArrayList)this.emppadminService.selectListByWhere(emppadminwhere+emppadminorder);
			if(emppadminlist!=null && emppadminlist.size()>=1){
				emppAdminid=emppadminlist.get(0).getId();
				try{
					com.sipai.tools.EncryptionDecryption en=new com.sipai.tools.EncryptionDecryption("usertel");
					emppadminlist.get(0).setAccountid(en.decrypt(emppadminlist.get(0).getAccountid()));	
					emppadminlist.get(0).setAccname(en.decrypt(emppadminlist.get(0).getAccname()));	
					emppadminlist.get(0).setPassword(en.decrypt(emppadminlist.get(0).getPassword()));	
					emppadminlist.get(0).setServiceid(en.decrypt(emppadminlist.get(0).getServiceid()));	
					en=null;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
					//2获得手机短信发送名单recvids
					//3手机用户表
			List<User> userlist=new ArrayList();
			for(int i=0;i<recvids.length;i++){
				if (recvids[i].length() > 0){
					userlist.add(this.userService.getUserById(recvids[i]));
				}
			}
			for(int ii=0;ii<userlist.size();ii++){
				EmppSendUser esu=new EmppSendUser();
				esu.setId(CommUtil.getUUID());
				esu.setEmppsendid(smsid);
				esu.setSenduserid(cuid);
				esu.setSenddate(new Date());
				esu.setRecuserid(userlist.get(ii).getId());
				esu.setMobile(userlist.get(ii).getMobile());
				esu.setBackdate(null);
				esu.setInsertuserid(cuid);
				esu.setInsertdate(new Date());
				esu.setUpdateuserid(cuid);
				esu.setUpdatedate(new Date());
				this.emppsenduserService.save(esu);
				esu=null;
			}
					//4发手机短信
			ArrayList<String> slist=new ArrayList<String>();
			if(emppadminlist!=null && emppadminlist.size()>=1 && recvids.length>=1){
				for(int i=0;i<userlist.size();i++){
					if((i%5)<5){ //如果取余数小于5，则添加如数组
						slist.add(userlist.get(i).getMobile());							
					}
					if((i%5)==4 || (recvids.length)==(i+1) ){ //如果取余数正好为4，则需要发送数组
						 this.emppsendService.dosendmsg(emppadminlist, slist, content);	
						slist=new ArrayList<String>();
						try{ //需要延迟1秒
					        Thread.sleep(1000);   //延时1秒
						}catch(InterruptedException   e){} 
					}
				}
				
			}
			slist=null;
			emppadminlist=null;
			//
		}
		String unsendusername = "";
		if(!unsendusers.isEmpty()){			 
			for(int z=0;z<unsendusers.size() ;z++){
				unsendusername+=this.userService.getUserById(unsendusers.get(z)).getCaption()+",";
			}
		}
		if(result==1){
			if(sendway.equals("sms")){
				if(!unsendusername.equals("")){
					return "短信未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "短信发送成功";
				}
			}else if(sendway.equals("msg")){				
				if(!unsendusername.equals("")){
					return "消息未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "消息发送成功";
				}
			}else{
				if(!unsendusername.equals("")){
					return "未全部发送，其中 【"+unsendusername+"】未向其发送，原因：消息类型角色未包含。";
				}else{
					return "发送成功";
				}
			}			
		}else{
			return "发送失败";
		}
	}*/

	
}
