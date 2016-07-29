package com.sipai.webservice.client.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import net.sf.json.JSONArray;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import com.sipai.entity.user.Role;
import com.sipai.entity.user.User;

public class UserClient {
	@SuppressWarnings("rawtypes")
	public User getUserByWS(String userId) throws AxisFault {
		EndpointReference targetEPR = new EndpointReference("http://132.120.136.236:8080/PlantEngine/services/userService");
		RPCServiceClient sender = new RPCServiceClient();
		Options options = sender.getOptions();
		options.setTimeOutInMilliSeconds(2*20000L);//超时时间20s
		options.setTo(targetEPR);
		/**
		 * RPCServiceClient类的invokeBlocking方法调用了WebService中的方法。
		 * invokeBlocking方法有三个参数
		 * 第一个参数的类型是QName对象，表示要调用的方法名；
		 * 第二个参数表示要调用的WebService方法的参数值，参数类型为Object[]；
		 * 第三个参数表示WebService方法的返回值类型的Class对象，参数类型为Class[]。
		 * 
		 * 当方法没有参数时，invokeBlocking方法的第二个参数值不能是null，而要使用new Object[]{}。
		 */
		QName qname = new QName("http://user.server.webservice.sipai.com", "getUserById");
		Object[] param = new Object[]{userId};
		//这是针对返值类型的
		Class<?>[] types = new Class[]{User.class};  
		
		//方法一 传递参数，调用服务，获取服务返回结果集 【需要自己解析xml,如下：】   
		OMElement element = sender.invokeBlocking(qname, param);
		//获取跟路径
		Iterator iterator = element.getChildElements();
		User user1 = new User();
		while (iterator.hasNext()) {
			//一级子节点【本例到达返回参数区】
			OMNode omNode = (OMNode) iterator.next();			
			if (omNode.getType() == OMNode.ELEMENT_NODE) { 
				OMElement omElement = (OMElement) omNode;
				if (omElement.getLocalName().equals("return")) {
					//二级子节点【本例到达User对象区】
					Iterator userIterator = omElement.getChildElements();  
					while(userIterator.hasNext()){
						OMNode userNode = (OMNode) userIterator.next();
						if (userNode.getType() == OMNode.ELEMENT_NODE) { 
							OMElement userElement = (OMElement) userNode;
							//本例为演示使用，字段不再一一写出，注意不同数据类型的转换
							if (userElement.getLocalName().equals("id")){
								String id = userElement.getText().trim();
								user1.setName(id);
							}
							if (userElement.getLocalName().equals("name")){
								String name = userElement.getText().trim(); 
								user1.setName(name);
							}
							if (userElement.getLocalName().equals("cardid")){
								String cardid = userElement.getText().trim(); 
								user1.setName(cardid);
							}
							if (userElement.getLocalName().equals("roles")){
								//三级子节点【本例到达Role对象区】
								Iterator roleIterator = userElement.getChildElements(); 
								List<Role> roles = new ArrayList<Role>();
								while(roleIterator.hasNext()){
									OMNode roleNode = (OMNode) roleIterator.next();
									if (roleNode.getType() == OMNode.ELEMENT_NODE) {
										Role role = new Role();
										OMElement roleElement = (OMElement) roleNode;
										if (roleElement.getLocalName().equals("id")){
											String id = roleElement.getText().trim(); 
											role.setName(id);
										}
										if (roleElement.getLocalName().equals("name")){
											String name = roleElement.getText().trim();  
											role.setName(name);
										}
										roles.add(role);
									}
								}
								user1.setRoles(roles);
							}
						}												
					}
			 	}
			}
			
		}
		//测试方法一
		JSONArray json=JSONArray.fromObject(user1);
		String result="{\"rows\":"+json+"}";
		//System.out.println(result);
		//方法二 传递参数，调用服务，获取服务返回对象集 		
		Object[] response = sender.invokeBlocking(qname, param, types);
		User user2 = (User)response[0];
		/**
		 * 说明：实际使用中请根据需要选择者方法，本案例中返回值方法一对应user1,方法二对应user2
		 */
		return user2;	
	}
}
