package com.sipai.service.msg;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.entity.msg.EmppAdmin;
import com.sipai.dao.msg.EmppSendDao;
import com.sipai.entity.msg.EmppSend;
import com.sipai.tools.CommService;
import com.wondertek.esmp.esms.empp.EMPPConnectResp;
import com.wondertek.esmp.esms.empp.EmppApi;

@Service
public class EmppSendService implements CommService<EmppSend>{
	@Resource
	private EmppSendDao emppsendDao;
	
	@Override
	public EmppSend selectById(String id) {
		return emppsendDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return emppsendDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(EmppSend entity) {
		return emppsendDao.insert(entity);
	}

	@Override
	public int update(EmppSend t) {
		return emppsendDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<EmppSend> selectListByWhere(String wherestr) {
		EmppSend emppsend = new EmppSend();
		emppsend.setWhere(wherestr);
		return emppsendDao.selectListByWhere(emppsend);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		EmppSend emppsend = new EmppSend();
		emppsend.setWhere(wherestr);
		return emppsendDao.deleteByWhere(emppsend);
	}
	public void dosendmsg(ArrayList<EmppAdmin> elist,ArrayList<String> alist,String message){
		String host =elist.get(0).getHost();
		int port = elist.get(0).getPort();
		String accountId = elist.get(0).getAccountid();
		String password = elist.get(0).getPassword();
		String serviceId = elist.get(0).getServiceid();

		

		EmppApi emppApi = new EmppApi();
		RecvListener listener = new RecvListener(emppApi);

		try {
			//建立同服务器的连接
			EMPPConnectResp response = emppApi.connect(host, port, accountId,
					password, listener);
			System.out.println(response);
			if (response == null) {
				System.out.println("连接超时失败");
				return;
			}
			if (!emppApi.isConnected()) {
				System.out.println("连接失败:响应包状态位=" + response.getStatus());
				return;
			}
		} catch (Exception e) {
			System.out.println("发生异常，导致连接失败");
			e.printStackTrace();
			return;
		}
		
//		发送短信
		if (emppApi.isSubmitable()) {
//			String mobile="";
			for(int i=0;i<alist.size();i++){
//				System.out.println("短信名单为:"+alist.get(i).toString());
				//简单方式发送短信,支持长短信
				try{
					emppApi.submitMsgAsync(message,new String[]{alist.get(i).toString()},serviceId);
					
					//同步发送方式update 20060307
					//EMPPSubmitSMResp []  resp = emppApi.submitMsg(content,mobiles,serviceId);
					//System.out.println("resp result:"+resp[0].getResult());

				}catch (Exception e1) {
					e1.printStackTrace();
				} 
			}
			
			
////			详细设置短信的各个属性,不支持长短信
//			EMPPSubmitSM msg = (EMPPSubmitSM) EMPPObject.createEMPP(EMPPData.EMPP_SUBMIT);
//			List dstId = new ArrayList();
//			for(int i=0;i<alist.size();i++){
//				dstId.add(alist.get(i).toString());	
//			}
//			msg.setDstTermId(dstId);
//			msg.setSrcTermId(accountId);
//			msg.setServiceId(serviceId);
//
//			EMPPShortMsg msgContent = new EMPPShortMsg(EMPPShortMsg.EMPP_MSG_CONTENT_MAXLEN);
//			
//			try {
//				msgContent.setMaxLength(2000);
//				msgContent.setMessage(message);
//				msg.setShortMessage(msgContent);
//				msg.assignSequenceNumber();
//				emppApi.submitMsgAsync(msg);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
		}
	}
	
}
