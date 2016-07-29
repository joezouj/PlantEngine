package com.sipai.service.msg;

import java.math.BigInteger;
import java.util.ArrayList;

import com.wondertek.esmp.esms.empp.EMPPAnswer;
import com.wondertek.esmp.esms.empp.EMPPChangePassResp;
import com.wondertek.esmp.esms.empp.EMPPDeliver;
import com.wondertek.esmp.esms.empp.EMPPDeliverReport;
import com.wondertek.esmp.esms.empp.EMPPObject;
import com.wondertek.esmp.esms.empp.EMPPRecvListener;
import com.wondertek.esmp.esms.empp.EMPPReqNoticeResp;
import com.wondertek.esmp.esms.empp.EMPPSubmitSM;
import com.wondertek.esmp.esms.empp.EMPPSubmitSMResp;
import com.wondertek.esmp.esms.empp.EMPPSyncAddrBookResp;
import com.wondertek.esmp.esms.empp.EMPPTerminate;
import com.wondertek.esmp.esms.empp.EMPPUnAuthorization;
import com.wondertek.esmp.esms.empp.EmppApi;

/**
 * @author chensheng
 *
 * 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class RecvListener implements EMPPRecvListener {

	private static final long RECONNECT_TIME = 10 * 1000;
    
    private EmppApi emppApi = null;
    
    private int closedCount = 0;
    
    protected RecvListener(){
        
    }
    
    public RecvListener(EmppApi emppApi){
        this.emppApi = emppApi;
    }
  
 	 /*// 处理接收到的消息
    @SuppressWarnings("unchecked")
	public void onMessage(EMPPObject message) {
        if(message instanceof EMPPUnAuthorization){
            @SuppressWarnings("unused")
			EMPPUnAuthorization unAuth=(EMPPUnAuthorization)message;
//            System.out.println("客户端无权执行此操作 commandId="+unAuth.getUnAuthCommandId());
            return;
         }
        if(message instanceof EMPPSubmitSMResp){
        	EMPPSubmitSMResp resp=(EMPPSubmitSMResp)message;
//        	System.out.println("收到sumbitResp:");
        	@SuppressWarnings("unused")
			byte[] msgId=fiterBinaryZero(resp.getMsgId());
        	
//			System.out.println("移动公司收到的msgId="+new BigInteger(msgId)+"，手机号码是:"+resp.);
//        	System.out.println("result="+resp.getResult());
        	return;
        }
  		if(message instanceof EMPPDeliver){
  			EMPPDeliver deliver = (EMPPDeliver)message;
  			if(deliver.getRegister()==EMPPSubmitSM.EMPP_STATUSREPORT_TRUE){
  				//收到状态报告
  				EMPPDeliverReport report=deliver.getDeliverReport();
//  				System.out.println("收到状态报告:");
  				byte[] msgId=fiterBinaryZero(report.getMsgId());
  			    
//  				System.out.println("客户反馈的msgId11="+new BigInteger(msgId)+",手机号码是:"+report.getDstAddr());
//  			    System.out.println("status="+report.getStat());
			    
  			    String mobile=report.getDstAddr();//获得手机号码
  				try{
  					com.sipai.tools.EncryptionDecryption en=new com.sipai.tools.EncryptionDecryption("usertel");
  					mobile=en.encrypt(mobile);
  					en=null;	
  				}catch(Exception e){
  					e.printStackTrace();
  				}
  			    
  				com.sipai.fwk.CommSQL.updateStatic("update tb_EMPP_EmppSendUser set msgId='"+new BigInteger(msgId)+"',backdate='"+com.sipai.tools.CommUtil.nowDate()+"' where mobile='"+mobile+"' " +
  						" and (backdate is null or backdate='' )");
  				
			    
  			    
  			}else{
  				byte[] msgId1=fiterBinaryZero(deliver.getMsgId());
  				//收到手机回复
//	  			System.out.println("收到"+deliver.getSrcTermId()+"发送的短信,编号是:"+new BigInteger(msgId1)+",子账号是:"+deliver.getDstAddr());
//	  			System.out.println("短信内容为："+deliver.getMsgContent().debugString().replaceAll("sm: msg: ",""));
//	  			
	  			
	  			 * 先查询该id号得到的tb_EMPP_EmppAdmin
	  			 
	  			String emppSendid="";
	  			String accountId="";//为子账号
				try{
					com.sipai.tools.EncryptionDecryption en=new com.sipai.tools.EncryptionDecryption("usertel");	
					accountId=en.encrypt(deliver.getDstAddr());
					EmppAdminDAO emdao=new EmppAdminDAO();
					ArrayList<EmppAdmin> emlist=new ArrayList<EmppAdmin>();
					emlist=(ArrayList)emdao.loadList("select * from tb_EMPP_EmppAdmin where accountId='"+accountId+"' ");
					if(emlist!=null && emlist.size()>=1){
						EmppreplaceDAO epdao=new EmppreplaceDAO();
						emppSendid=emlist.get(0).getId();
						Emppreplace e=new Emppreplace();
						e.setId(com.sipai.tool.CommUtil.getUUID());
						e.setEmppSendid(emppSendid);
						e.setMobile(en.encrypt(deliver.getSrcTermId()));
						e.setBackdate(com.sipai.tool.CommUtil.nowDate());
						e.setMsgId(String.valueOf(new BigInteger(msgId1)));
						e.setRepcontent(en.encrypt(deliver.getMsgContent().debugString().replaceAll("sm: msg: ","")));
						
						epdao.save(e, "");
						epdao=null;
						e=null;
					}
					
					emlist=null;
					emdao=null;
					en=null;
				}catch(Exception e){
					e.printStackTrace();
				}
	  			
  			
  			}
  		    return;
  		}
  		 if(message instanceof EMPPSyncAddrBookResp){
  		      EMPPSyncAddrBookResp resp=(EMPPSyncAddrBookResp)message;
  		      if(resp.getResult()!=EMPPSyncAddrBookResp.RESULT_OK)
  		          System.out.println("同步通讯录失败");
  		      else{
  		          System.out.println("收到服务器发送的通讯录信息");
  		          System.out.println("通讯录类型为："+resp.getAddrBookType());
  		          System.out.println(resp.getAddrBook());
  		      }
          }
  		 if(message instanceof EMPPChangePassResp){
                EMPPChangePassResp resp=(EMPPChangePassResp)message;
                if(resp.getResult()==EMPPChangePassResp.RESULT_VALIDATE_ERROR)
                    System.out.println("更改密码：验证失败");
                if(resp.getResult()==EMPPChangePassResp.RESULT_OK)
                {
                    System.out.println("更改密码成功,新密码为："+resp.getPassword());
                    emppApi.setPassword(resp.getPassword());
                }
                return;
                
            } 
  		 if(message instanceof EMPPReqNoticeResp){
                EMPPReqNoticeResp response=(EMPPReqNoticeResp)message;
                if(response.getResult()!=EMPPReqNoticeResp.RESULT_OK)
                   System.out.println("查询运营商发布信息失败");
                else{
                   System.out.println("收到运营商发布的信息");
                   System.out.println(response.getNotice());
                }
                return;
           }
  		if(message instanceof EMPPAnswer){
  		    System.out.println("收到企业疑问解答");
             EMPPAnswer  answer=(EMPPAnswer)message;
             System.out.println(answer.getAnswer());
             
         }
  		    System.out.println(message);
  	    
       }
 	 *///处理连接断掉事件
     public void OnClosed(Object object) {
        // 该连接是被服务器主动断掉，不需要重连
        if(object instanceof EMPPTerminate){
            System.out.println("收到服务器发送的Terminate消息，连接终止");
            return;
        }
        //这里注意要将emppApi做为参数传入构造函数
        RecvListener listener = new RecvListener(emppApi)
		;
        System.out.println("连接断掉次数："+(++closedCount));
        for(int i = 1;!emppApi.isConnected();i++){
            try {
                System.out.println("重连次数:"+i);
                Thread.sleep(RECONNECT_TIME);
                emppApi.reConnect(listener);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("重连成功");
    }
 
 	//处理错误事件
    public void OnError(Exception e) {
        e.printStackTrace();
    }
    
    public static byte[] fiterBinaryZero(byte[] bytes) {
        byte[] returnBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            returnBytes[i] = bytes[i];
        }
        return returnBytes;
    }

	@Override
	public void onMessage(EMPPObject arg0) {
		// TODO Auto-generated method stub
		
	}
}
