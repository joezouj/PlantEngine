package com.sipai.controller.work;


//import com.battery.bean.CommunityExceptionRecord; 
import javax.annotation.Resource;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.stereotype.Controller;

import com.serotonin.modbus4j.ModbusFactory; 
import com.serotonin.modbus4j.ModbusMaster;  
import com.serotonin.modbus4j.exception.ModbusInitException;  
import com.serotonin.modbus4j.exception.ModbusTransportException; 
import com.serotonin.modbus4j.ip.IpParameters;  
import com.serotonin.modbus4j.msg.ModbusRequest; 
import com.serotonin.modbus4j.msg.ModbusResponse;  
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest; 
import com.serotonin.modbus4j.msg.WriteRegistersRequest; 
import com.serotonin.modbus4j.msg.WriteRegistersResponse; 
import com.serotonin.util.queue.ByteQueue; 
import com.sipai.entity.user.User;
import com.sipai.entity.work.ModbusRecord;
import com.sipai.service.work.ModbusFigService;
import com.sipai.service.work.ModbusRecordService;
import com.sipai.tools.CommUtil;


public class ReadAWriteUtil { 
	static ModbusMaster tcpMasterr; //读写使用不同的ModbusMaster，防止同时操作时候出乱
	static ModbusMaster tcpMasterw;
	static String errordetails="";
	static String paramsStr;
	public static void modbusWTCP(String ip, int port, int slaveId, int start, short[] values) { 

		ModbusFactory modbusFactory = new ModbusFactory(); 
		// 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502 
		boolean ch=false; //检测tcpMaster是否发生改变，若更改则重新初始化
		IpParameters params=new IpParameters(); 
		params.setHost(ip); 
		if(502!=port){params.setPort(port);}//设置端口，默认502 
		String ipport=ip+port;
		if(paramsStr!=null)
		{
			if(!paramsStr.equals(ipport))
			{
				ch=false;
			}else{
				ch=true;
			}
		}
		paramsStr=ipport; 
		if(tcpMasterw==null||ch==false)
		{
			tcpMasterw = modbusFactory.createTcpMaster(params, true);
		}
		try { 
			if(!tcpMasterw.isInitialized() || errordetails.contains("失败")||ch==false)
			{
				tcpMasterw.init(); 
			}					
		} catch (ModbusInitException e) {  
			// 如果出现了通信异常信息，则记录信息
			errordetails="连接服务器失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，连通结果：无法连接到modbus服务器!";
//			System.out.println("连接服务器失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，连通结果：无法连接到modbus服务器!");
		} 
		if(tcpMasterw.isInitialized())
		{
			try {  
				WriteRegistersRequest 	request = new WriteRegistersRequest(slaveId, start, values); 
				WriteRegistersResponse 	response =(WriteRegistersResponse) tcpMasterw.send(request); 
				if (response.isException()) {
					//记录modbus操作结果,保存到数据库
					errordetails="写入失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，站地址"+slaveId+"，写入寄存器"+start+","+values.length+"个字节失败!";
				}else{ 
					//System.out.println("Success"); 
					//记录modbus操作结果,保存到数据库
					errordetails="成功写入。服务器："+params.getHost()+"，端口号："+params.getPort()+"，站地址"+slaveId+"，写入寄存器"+start+","+values.length+"个字节成功!";
					//System.out.println("成功写入。服务器："+params.getHost()+"，端口号："+params.getPort()+"，站地址"+slaveId+"，写入寄存器"+start+","+values.length+"个字节成功!");
				}
			} catch (ModbusTransportException e) { 
				//e.printStackTrace(); 
				//记录modbus操作结果,保存到数据库
				errordetails="写入失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，站地址"+slaveId+"，写入寄存器"+start+","+values.length+"个字节失败!";
				//System.out.println("写入失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，站地址"+slaveId+"，写入寄存器"+start+","+values.length+"个字节失败!");
			}
		}
	} 

	public static ByteQueue modbusTCP(String ip, int port, int start,int readLenth) { 
		ModbusFactory modbusFactory = new ModbusFactory(); 
		// 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502 
		boolean ch=false; //检测tcpMaster是否发生改变，若更改则重新初始化
		IpParameters params=new IpParameters(); 
		params.setHost(ip); 
		if(502!=port){params.setPort(port);}//设置端口，默认502 
		String ipport=ip+port;
		if(paramsStr!=null)
		{
			if(!paramsStr.equals(ipport))
			{
				ch=false;
			}else{
				ch=true;
			}
		}
		paramsStr=ipport; 
		if(tcpMasterr==null||ch==false)
		{
			tcpMasterr = modbusFactory.createTcpMaster(params, true);
		}
		try { 
			if(!tcpMasterr.isInitialized() || errordetails.contains("失败")||ch==false)
			{
				tcpMasterr.init(); 
			}
		} catch (ModbusInitException e) { 
			// 如果出现了通信异常信息，则保存到数据库中
			errordetails="连接服务器失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，连通结果：无法连接到modbus服务器!";
			//System.out.println("连接服务器失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，连通结果：无法连接到modbus服务器!");
			return null;
		} 
		ModbusRequest modbusRequest=null; 
		try { 
		modbusRequest 	= 	new ReadHoldingRegistersRequest(1,start, readLenth);//功能码03 
		} catch (ModbusTransportException e) { 
			//记录modbus操作结果,保存到数据库
			errordetails="构造发送帧失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，读取寄存器"+start+","+readLenth+"个字节失败!";
			//System.out.println("构造发送帧失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，读取寄存器"+start+","+readLenth+"个字节失败!");
			return null;
		} 
		ModbusResponse modbusResponse=null; 
		try { 
			modbusResponse = tcpMasterr.send(modbusRequest); 
		} catch (ModbusTransportException e) { 
			//记录modbus操作结果,保存到数据库
			errordetails="读取失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，读取寄存器"+start+","+readLenth+"个字节失败!";
			//System.out.println("读取失败。服务器："+params.getHost()+"，端口号："+params.getPort()+"，读取寄存器"+start+","+readLenth+"个字节失败!");
			return null;
		} 
		ByteQueue byteQueue= new ByteQueue(12); 
		modbusResponse.write(byteQueue); 
//		System.out.println("功能码:"+modbusRequest.getFunctionCode());  
//		System.out.println("从站地址:"+modbusRequest.getSlaveId()); 
//		System.out.println("收到的响应信息大小:"+byteQueue.size()); 
		errordetails="读取成功！服务器："+params.getHost()+"，端口号："+params.getPort()+"，读取寄存器"+start+","+readLenth+"个字节成功!";
//System.out.println("收到的响应信息值:"+byteQueue); 
		return byteQueue;  
	}
	public static String returnMsg()
	{
		return errordetails;
	}

} 
