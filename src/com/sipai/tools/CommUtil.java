package com.sipai.tools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.user.User;
import com.sipai.entity.user.UserPower;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.service.user.MenuService;
import com.sipai.service.user.UserService;



/**
 * @author Administrator
 * 
 */
public class CommUtil {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);

	private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
			"hh:mm:ss", Locale.SIMPLIFIED_CHINESE);

	private static final SimpleDateFormat longDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);

	private static final CommUtil util = new CommUtil();

	public CommUtil() {
	}

	public static String getwherestr(String unitid){
		String wherestr="";
		String []str=null;
		if(unitid==null || unitid.trim().equals("") || unitid.trim().equals(",")){
			wherestr="";
		}else{
			str=unitid.split(",");
			for(int i=0;i<str.length;i++){
				wherestr+="'"+str[i].trim()+"',";
			}
			wherestr+="''";
		}
		str=null;
		return wherestr;
	}
	/**
	 * 获取部门or自己or全部的范围
	 * scope=null为自己，dept为部门，all为所有
	 * @param column
	 * @param funcstr
	 * @param cu
	 * @return
	 */
	public static String getwherestr(String column, String funcstr, User cu){
		String wherestr=" where 1=1 ";
		int lvl=0;//0自己，1部门，2全部
		if(cu!=null){
			MenuService menuService = (MenuService) SpringContextUtil.getBean("menuService");
			List<UserPower> list= menuService.selectFuncByUserId(cu.getId());
			for(UserPower up:list){
				if(up.getLocation().indexOf(funcstr)>=0){
					if(up.getLocation().indexOf("scope=dept")>=0){
						if(lvl<1){
							lvl=1;
						}
					}
					if(up.getLocation().indexOf("scope=all")>=0){
						if(lvl<2){
							lvl=2;
						}
					}
				}
			}
			if(lvl==0){
				wherestr += " and "+column+" = '"+cu.getId()+"' ";
			}else if(lvl==1){
				UserService userService = (UserService) SpringContextUtil.getBean("userService");
				List<User> userlist= userService.getUserListByPid(cu.getPid());
				String str="";
				for(User user:userlist){
					str+="'"+user.getId()+"',";
				}
				if(!str.equals("")){
					str=str.substring(0, str.length()-1);
				}
				wherestr += " and "+column+" in ("+str+") ";
			}
		}else{
			wherestr =" where 1<>1 ";
		}
		
		return wherestr;
	}

	/**
	 * @return 当前函数值
	 */
	public static CommUtil getInstance() {
		return util;
	}

	/**
	 * 得到yyyy-MM-dd的日期格式
	 * 
	 * @return yyyy-MM-dd的日期格式
	 */
	public static SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * 转换对象成所需日期格式的字符串
	 * 
	 * @param format
	 *            日期格式
	 * @param v
	 *            需要转换日期格式的对象
	 * @return 所需要日期类型的字符串
	 */
	public static String formatDate(String format, Object v) {
		if (v == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format,
					Locale.SIMPLIFIED_CHINESE);
			return df.format(v);
		}
	}

	/**
	 * 转换字符串成所需日期格式的日期对象
	 * 
	 * @param format
	 *            日期格式
	 * @param v
	 *            需要转换日期格式的字符串
	 * @return 所需要日期类型的日期对象
	 */
	public static Date formatDate(String format, String v) {
		if (v == null) {
			return null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat(format,
					Locale.SIMPLIFIED_CHINESE);
			Date d = null;
			try {
				d = df.parse(v);
			} catch (Exception exception) {
			}
			return d;
		}
	}

	/**
	 * 转换对象成yyyy-MM-dd格式的字符串
	 * 
	 * @param v
	 *            需转换短日期格式的字符串
	 * @return 转换成yyyy-MM-dd格式的字符串
	 */
	public static String format(Object v) {
		if (v == null)
			return null;
		else
			return dateFormat.format(v);
	}

	/**
	 * 转换对象成hh:mm:ss格式的字符串
	 * 
	 * @param v
	 *            需转换时间格式的对象
	 * @return 转换成hh:mm:ss格式的字符串
	 */
	public static String sortTime(Object v) {
		if (v == null)
			return null;
		else
			return timeFormat.format(v);
	}

	/**
	 * * 转换对象成yyyy-MM-dd hh:mm:ss格式的字符串
	 * 
	 * @param v
	 *            需转换完整日期格式的对象
	 * @return 转换成yyyy-MM-dd hh:mm:ss格式的字符串
	 */
	public static String longDate(Object v) {
		if (v == null)
			return null;
		else
			return longDateFormat.format(v);
	}

	/**
	 * 得到当前时间
	 * 
	 * @return 返回当前时间的字符串 格式为yyyy-MM-dd hh:mm:ss
	 */
	public static String nowDate() {
		return longDateFormat.format(new Date());
	}
	//目的是获得当前年
	public static String thisYear() {

		return  Calendar.getInstance().get(Calendar.YEAR)+"-01-01";
	}

	/**
	 * 得到字符串的时间对象
	 * 
	 * @param s
	 *            需转换为日期对象的字符串
	 * @return 转换为yyyy-MM-dd的日期对象
	 */
	public static Date formatDate(String s) {
		Date d = null;
		try {
			d = dateFormat.parse(s);
		} catch (Exception exception) {
		}
		return d;
	}

	/**
	 * 判断对象是否为空，返回对象字符串或者没有字符
	 * 
	 * @param s
	 *            判断是否为空的对象
	 * @return 不为空时返回对象的字符串，为空返回没有字符
	 */
	public static String null2String(Object s) {
		return s != null ? s.toString() : "";
	}

	/**
	 * 判断字符串是否为空，返回字符串或者没有字符
	 * 
	 * @param s
	 *            判断是否为空的字符串
	 * @return 不为空时返回字符串，为空返回没有字符
	 */
	public static String null2String(String s) {
		return s != null ? s.toString() : "";
	}

	/**
	 * 判断字符串是否为空，返回int型数字或者0
	 * 
	 * @param s
	 *            判断是否为空的字符串
	 * @return 不为空时返回数字，为空返回0
	 */
	public static int null2Int(Object s) {
		int v = 0;
		if (s != null)
			try {
				v = Integer.parseInt(s.toString());
			} catch (Exception exception) {
			}
		return v;
	}



	/**
	 * 得到随机字符串
	 * 
	 * @param length
	 *            需要字符串的长度
	 * @return 返回所需长度的字符串
	 */
	public static String getRandString(int length) {
		StringBuffer s = new StringBuffer();
		Random r = new Random(10L);
		s.append(Math.abs(r.nextInt()));
		if (s.toString().length() > length)
			s.substring(0, length);
		return s.toString();
	}

	/**
	 * 得到唯一值
	 * 
	 * @return 返回一个当前时间为主的唯一值字符串
	 */
	public static String getOnlyID() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMDDhhmmss",
				Locale.SIMPLIFIED_CHINESE);
		double dblTmp;
		for (dblTmp = Math.random() * 100000D; dblTmp < 10000D; dblTmp = Math
				.random() * 100000D)
			;
		String strRnd = String.valueOf(dblTmp).substring(0, 4);
		String s = df.format(new Date()) + strRnd;
		return s;
	}

	/**
	 * 把字符串解析为中文
	 * 
	 * @param strvalue
	 *            中文字符串
	 * @return 返回GBK的中文字符串
	 */
	public static String toChinese(String strvalue) {
		try {
			if (strvalue == null) {
				return null;
			} else {
				strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
				return strvalue;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 转换成所需的编码格式的字符串
	 * 
	 * @param strvalue
	 *            需要转换的字符串
	 * @param charset
	 *            需要的编码格式
	 * @return 返回编码格式的字符串
	 */
	public static String toChinese(String strvalue, String charset) {
		try {
			if (charset == null || charset.equals("")) {
				return toChinese(strvalue);
			} else {
				strvalue = new String(strvalue.getBytes("ISO8859_1"), charset);
				return strvalue;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算两个时间的天数差值
	 * 
	 * @param sd
	 *            第一个时间字符串 日期格式为yyyy-MM-dd
	 * @param ed
	 *            第二个时间字符串 日期格式为yyyy-MM-dd
	 * @return 返回两个时间差值，值为整型
	 */
	public static int getDays(String sd, String ed) {
		long l = 0;
		try {
			Date dt1 = dateFormat.parse(sd);
			Date dt2 = dateFormat.parse(ed);
			l = (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
		} catch (Exception e) {
			System.out.println("exception" + e.toString());
		}
		return (int) l;
	}

	/**
	 * 计算两个时间的差值
	 * 
	 * @param sd
	 *            第一个时间字符串 日期格式为yyyy-MM-dd
	 * @param ed
	 *            第二个时间字符串 日期格式为yyyy-MM-dd
	 * @param type
	 *            需要差值的是，分钟、小时、天、周、月，年
	 * @return 返回两个时间差值，值为整型
	 */
	public static int getDays(String sd, String ed,String type) {
		long l = 0;
		try {
			Date dt1 = dateFormat.parse(sd);
			Date dt2 = dateFormat.parse(ed);
			l = (dt1.getTime() - dt2.getTime()) / (3600 * 24 * 1000);
			if ("min".equals(type)) {
				l = (dt1.getTime() - dt2.getTime()) / (60 *  1000);
			} else if ("hour".equals(type)) {
				l = (dt1.getTime() - dt2.getTime()) / (360 *  1000);
			} else if ("week".equals(type)) {
				l = (dt1.getTime() - dt2.getTime()) / (3600 * 24 *7 * 1000);
			} 

		} catch (Exception e) {
			System.out.println("exception" + e.toString());
		}
		return (int) l;
	}

	/**
	 * 得到日期加减
	 * 
	 * @param datestr
	 *            日期字符串
	 * @param dateval
	 *            需要加减的数量
	 * @param type
	 *            需要加减的是，小时、周、月，年
	 * @return 加减后的日期字符串
	 */
	public static String subplus(String datestr, String dateval, String type) {
		String sreturn = null;
		try {
			Calendar calendar = Calendar.getInstance();
			int datetype = Calendar.DATE;
			Date dt = dateFormat.parse(datestr);
			if ("hour".equals(type)) {
				datetype = Calendar.HOUR_OF_DAY;
				SimpleDateFormat chineseDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm", Locale.SIMPLIFIED_CHINESE);
				dt = chineseDateFormat.parse(datestr);
			} else if ("week".equals(type)) {
				datetype = Calendar.DAY_OF_WEEK_IN_MONTH;
			} else if ("month".equals(type)) {
				datetype = Calendar.MONTH;
			} else if ("year".equals(type)) {
				datetype = Calendar.YEAR;
			}
			int val = Integer.valueOf(dateval);
			calendar.setTime(dt);
			calendar.add(datetype, val);
			sreturn = longDateFormat.format(calendar.getTime());
		} catch (Exception e) {
			System.out.println("exception" + e.toString());
		}
		return sreturn;
	}

	/**
	 * 得到当前年月
	 * 
	 * @return 当前年月
	 */
	public static String getMonth() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day > 22) {
			cal.add(Calendar.MONTH, 1);
		}
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String sreturn = year + "年" + (month + 1) + "月";
		return sreturn;
	}

	/**
	 * 得到参数的年月
	 * 
	 * @param val
	 *            提前月份数
	 * @return 返回提前当前月份数的年月字符串
	 */
	public static String getMonth(String val) {
		Calendar cal = Calendar.getInstance();
		int dateval;
		if (val.equals("")) {
			dateval = 0;
		} else {
			dateval = -Integer.valueOf(val);
		}
		cal.add(Calendar.MONTH, dateval);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		String sreturn = year + "年" + (month + 1) + "月";
		return sreturn;
	}

	/**
	 * 得到唯一值
	 * 
	 * @return 返回java的util中唯一值函数的值
	 */
	public static String getUUID() {
		String uuid = null;
		uuid = java.util.UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}

	/**
	 * @param list
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public static void removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		list.clear();
		list.addAll(newList);
	}

	/**
	 * String形式的replace
	 * 
	 * @param from
	 * @param to
	 * @param source
	 * @return
	 */
	public static String strReplace(String from, String to, String source) {
		StringBuffer bf = new StringBuffer("");
		if (source != null) {

			StringTokenizer st = new StringTokenizer(source, from, true);
			while (st.hasMoreTokens()) {
				String tmp = st.nextToken();
				if (tmp.equals(from)) {
					bf.append(to);
				} else {
					bf.append(tmp);
				}
			}
		}
		return bf.toString();
	}

	public static String genDelMsg(int total,int suc){
		String msg="";
		if(total==1){
			if(suc==1){
				msg="删除成功！";
			}else{
				msg="删除失败，该信息已被使用！";
			}
		}else{
			if(total==suc){
				msg="删除成功";	
			}else{
				msg="共需删除"+String.valueOf(total)+"条,成功删除"+String.valueOf(suc)+"条,另有"+String.valueOf(total-suc)+"条信息被使用无法删除！";
			}
		}		
		return msg;
	}

	/**为需要审批的数量
	 * @param total
	 * @param suc
	 * @return
	 */
	public static String gencheck(int total,int suc){
		String msg="";
		if(total==1){
			if(suc==1){
				msg="审批完成！";
			}else{
				msg="审批失败，该信息有问题！";
			}
		}else{
			if(total==suc){
				msg="审批完成";	
			}else{
				msg="共需审批"+String.valueOf(total)+"条,成功审批"+String.valueOf(suc)+"条,另有"+String.valueOf(total-suc)+"条信息有问题审批！";
			}
		}		
		return msg;
	}

	public static String genDelMsg1(int total,int suc){
		String msg="";
		if(total==1){
			if(suc==1){
				msg="标志成功";
			}else{
				msg="标志失败";
			}
		}else{
			msg="共需标志"+String.valueOf(total)+"条,成功标志"+String.valueOf(suc)+"条";
		}		
		return msg;
	}
	public static String genDelMsg2(int total,int suc){
		String msg="";
		if(total==1){
			if(suc==1){
				msg="启用成功";
			}else{
				msg="启用失败";
			}
		}else{
			msg="共需启用"+String.valueOf(total)+"条,成功启用"+String.valueOf(suc)+"条";
		}		
		return msg;
	}


	/**比较两个日期大小(2010-12-15 snd)
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_date(String DATE1, String DATE2) {      
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				//System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				//System.out.println("dt1在dt2后");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**比较两个日期时间大小
	 * @param DATE1
	 * @param DATE2
	 * @return
	 */
	public static int compare_time(String DATE1, String DATE2) {      
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				//System.out.println("dt1 在dt2后");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				//System.out.println("dt1在dt2前");
				return -1;
			} else {
				//System.out.println("dt1=dt2");
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	//生成随机颜色
	public static String getRandColor(Random random,int fc,int bc){
		if(fc>255)fc=255;
		if(bc>255)bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		Color color=new Color(r,g,b);
		String R = Integer.toHexString(color.getRed());
		R = R.length()<2?('0'+R):R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length()<2?('0'+B):B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length()<2?('0'+G):G;
		return '#'+R+B+G;
	} 

	//生成随机颜色（为）
	public static String getRandColornew(Random random,int fc,int bc){
		if(fc>255)fc=255;
		if(bc>255)bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		Color color=new Color(r,g,b);
		String R = Integer.toHexString(color.getRed());
		R = R.length()<2?('0'+R):R;
		String B = Integer.toHexString(color.getBlue());
		B = B.length()<2?('0'+B):B;
		String G = Integer.toHexString(color.getGreen());
		G = G.length()<2?('0'+G):G;
		return R+B+G;
	} 

	//*==============================================================
	public static String getLocalMAC(){  
		InetAddress addr;  
		try {  
			addr = InetAddress.getLocalHost();  
			String ip=addr.getHostAddress().toString();//获得本机IP  
			return getMAC(ip);  
		} catch (UnknownHostException e) {  
			e.printStackTrace();  
		}  
		return "ERROR";  
	} 
	public static String getMAC(String ipAddress) {  
		if(ipAddress.equalsIgnoreCase("localhost")||ipAddress.equalsIgnoreCase("127.0.0.1")){  
			return getLocalMAC();  
		}  
		String address = "ERROR";  
		String os = System.getProperty("os.name");  
		if (os != null && os.startsWith("Windows")) {  
			try {  
				String command = "cmd.exe /c nbtstat -a "+ipAddress;  
				Process p = Runtime.getRuntime().exec(command);  
				BufferedReader br =  
						new BufferedReader(  
								new InputStreamReader(p.getInputStream()));  
				String line;  
				while ((line = br.readLine()) != null) {  
					if (line.indexOf("MAC") > 0) {  
						int index = line.indexOf("=");  
						index+=2;  
						address = line.substring(index);  
						break;  
					}  
				}  
				br.close();  
				return address.trim();  
			} catch (IOException e) {}  
		}
		else if (os.startsWith("Linux")) {
			StringTokenizer tokenizer = new StringTokenizer(ipAddress, "\n");

			while (tokenizer.hasMoreTokens()) {
				String line = tokenizer.nextToken().trim();

				// see if line contains MAC address
				int macAddressPosition = line.indexOf("HWaddr");
				if (macAddressPosition <= 0)
					continue;

				address = line.substring(macAddressPosition + 6)
						.trim();
			}
		}

		return address;  
	}  

	/**
	 * 获取节点集合
	 * @param doc : Doument 对象
	 * @param tagName : 节点名
	 * @param index : 找到的第几个
	 * @return
	 */
	private static NodeList getNode(Document doc, String tagName, int index) {
		return doc.getElementsByTagName(tagName).item(index).getChildNodes();
	}

	/**
	 * 获取节点内容
	 * @param node : nodelist
	 * @param index : 节点索引, 也可使用 getNamedItem(String name) 节点名查找
	 * @param item : 属性的索引
	 * @return
	 */
	private static String getAttributeValue(NodeList node, int index, int item) {
		return node.item(index).getAttributes().item(item).getNodeValue();
	}	

	//*==============================================================

	/**
	 * 得到字符串的真实长度（汉字算2位）
	 * @param str
	 * @return
	 */
	public static int getRealLength(String str){
		int length = 0;
		for(int i=0;i<str.length();i++){
			char c = str.charAt(i);
			int ascii=(int)c;
			if(ascii>=0&&ascii<=127){
				length++;
			}else{
				length+=2;
			}
		}
		return length;
	}    

	//*==============================================================
	//默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;
	//这个类不能实例化

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double add(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 * 小数点以后10位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static double div(double v1,double v2){
		return div(v1,v2,DEF_DIV_SCALE);
	}
	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 * 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double div(double v1,double v2,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	/**
	 * 提供精确的小数位四舍五入处理。
	 * @param v 需要四舍五入的数字
	 * @param scale 小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v,int scale){
		if(scale<0){
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}	
	/**
	 * 保留几位小数
	 * 
	 * @param inNumber
	 *            需要处理的双精度浮点数字
	 * @param param
	 *            小数点后需要保留的位数
	 * @return 返回需要保留位数的字符串
	 */
	//	public static String round(double inNumber, int param) {
	//	String format = "#.";
	//	for (int i = 0; i < param; i++)
	//	format = format.concat("#");

	//	if (param == 0)
	//	format = format.substring(0, format.toString().length() - 1);
	//	DecimalFormat df = new DecimalFormat(format);
	//	return df.format(inNumber);
	//	}	
	//*==============================================================

	//*==============================================================

	public static String[] chineseDigits = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};       

	/**     
	 * 把金额转换为汉字表示的数量，小数点后四舍五入保留两位     
	 * @param amount     
	 * @return     
	 */      
	public static String amountToChinese(double amount) {       

		if(amount > 99999999999999.99 || amount < -99999999999999.99)       
			throw new IllegalArgumentException("参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");       

		boolean negative = false;       
		if(amount < 0) {       
			negative = true;       
			amount = amount * (-1);       
		}       

		long temp = Math.round(amount * 100);       
		int numFen = (int)(temp % 10); // 分       
		temp = temp / 10;       
		int numJiao = (int)(temp % 10); //角       
		temp = temp / 10;       
		//temp 目前是金额的整数部分       

		int[] parts = new int[20]; // 其中的元素是把原来金额整数部分分割为值在 0~9999 之间的数的各个部分       
		int numParts = 0; // 记录把原来金额整数部分分割为了几个部分（每部分都在 0~9999 之间）       
		for(int i=0; ; i++) {       
			if(temp ==0)       
				break;       
			int part = (int)(temp % 10000);       
			parts[i] = part;       
			numParts ++;       
			temp = temp / 10000;       
		}       

		boolean beforeWanIsZero = true; // 标志“万”下面一级是不是 0       

		String chineseStr = "";       
		for(int i=0; i<numParts; i++) {       

			String partChinese = partTranslate(parts[i]);       
			if(i % 2 == 0) {       
				if("".equals(partChinese))       
					beforeWanIsZero = true;       
				else      
					beforeWanIsZero = false;       
			}       

			if(i != 0) {       
				if(i % 2 == 0)       
					chineseStr = "亿" + chineseStr;       
				else {       
					if("".equals(partChinese) && !beforeWanIsZero)   // 如果“万”对应的 part 为 0，而“万”下面一级不为 0，则不加“万”，而加“零”       
						chineseStr = "零" + chineseStr;       
					else {       
						if(parts[i-1] < 1000 && parts[i-1] > 0) // 如果"万"的部分不为 0, 而"万"前面的部分小于 1000 大于 0， 则万后面应该跟“零”       
							chineseStr = "零" + chineseStr;       
						chineseStr = "万" + chineseStr;       
					}       
				}       
			}       
			chineseStr = partChinese + chineseStr;       
		}       

		if("".equals(chineseStr))  // 整数部分为 0, 则表达为"零元"       
			chineseStr = chineseDigits[0];       
		else if(negative) // 整数部分不为 0, 并且原金额为负数       
			chineseStr = "负" + chineseStr;       

		chineseStr = chineseStr + "元";       

		if(numFen == 0 && numJiao == 0) {       
			chineseStr = chineseStr + "整";       
		}       
		else if(numFen == 0) { // 0 分，角数不为 0       
			chineseStr = chineseStr + chineseDigits[numJiao] + "角";       
		}       
		else { // “分”数不为 0       
			if(numJiao == 0)       
				chineseStr = chineseStr + "零" + chineseDigits[numFen] + "分";       
			else      
				chineseStr = chineseStr + chineseDigits[numJiao] + "角" + chineseDigits[numFen] + "分";       
		}       

		return chineseStr;       

	}       


	/**     
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""     
	 * @param amountPart     
	 * @return     
	 */      
	private static String partTranslate(int amountPart) {       

		if(amountPart < 0 || amountPart > 10000) {       
			throw new IllegalArgumentException("参数必须是大于等于 0，小于 10000 的整数！");       
		}       


		String[] units = new String[] {"", "拾", "佰", "仟"};       

		int temp = amountPart;       

		String amountStr = new Integer(amountPart).toString();       
		int amountStrLength = amountStr.length();       
		boolean lastIsZero = true; //在从低位往高位循环时，记录上一位数字是不是 0       
		String chineseStr = "";       

		for(int i=0; i<amountStrLength; i++) {       
			if(temp == 0)  // 高位已无数据       
				break;       
			int digit = temp % 10;       
			if(digit == 0) { // 取到的数字为 0       
				if(!lastIsZero)  //前一个数字不是 0，则在当前汉字串前加“零”字;       
					chineseStr = "零" + chineseStr;       
				lastIsZero = true;       
			}       
			else { // 取到的数字不是 0       
				chineseStr = chineseDigits[digit] + units[i] + chineseStr;       
				lastIsZero = false;       
			}       
			temp = temp / 10;       
		}       
		return chineseStr;       
	}       

	//十六进制下数字到字符的映射数组  
	private final static String[] hexDigits = {"0", "1", "2", "3", "4",  
		"5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};  

	/** * 把inputString加密     */  
	public static String generatePassword(String inputString){  
		return encodeByMD5(inputString);  
	}  

	/** 
	 * 验证输入的密码是否正确 
	 * @param password    加密后的密码 
	 * @param inputString    输入的字符串 
	 * @return    验证结果，TRUE:正确 FALSE:错误 
	 */  
	public static boolean validatePassword(String password, String inputString){  
		if(password.equals(encodeByMD5(inputString))){  
			return true;  
		} else{  
			return false;  
		}  
	}  
	/**  对字符串进行MD5加密     */  
	private static String encodeByMD5(String originString){  
		if (originString != null){  
			try{  
				//创建具有指定算法名称的信息摘要  
				MessageDigest md = MessageDigest.getInstance("MD5");  
				//使用指定的字节数组对摘要进行最后更新，然后完成摘要计算  
				byte[] results = md.digest(originString.getBytes());  
				//将得到的字节数组变成字符串返回  
				String resultString = byteArrayToHexString(results);  
				return resultString.toUpperCase();  
			} catch(Exception ex){  
				ex.printStackTrace();  
			}  
		}  
		return null;  
	}  

	/**  
	 * 转换字节数组为十六进制字符串 
	 * @param     字节数组 
	 * @return    十六进制字符串 
	 */  
	private static String byteArrayToHexString(byte[] b){  
		StringBuffer resultSb = new StringBuffer();  
		for (int i = 0; i < b.length; i++){  
			resultSb.append(byteToHexString(b[i]));  
		}  
		return resultSb.toString();  
	}  

	/** 将一个字节转化成十六进制形式的字符串     */  
	private static String byteToHexString(byte b){  
		int n = b;  
		if (n < 0)  
			n = 256 + n;  
		int d1 = n / 16;  
		int d2 = n % 16;  
		return hexDigits[d1] + hexDigits[d2];  
	}  

	//*==============================================================
	/**
	 * @param fileName 文件名
	 * @return xml的文本
	 * @throws DocumentException
	 */
	public static org.dom4j.Document read(String fileName) throws DocumentException {
		SAXReader reader = new SAXReader();
		org.dom4j.Document document = reader.read(new File(fileName));
		return document;
	}

	public static org.dom4j.Document readMemo(String memo) throws DocumentException {
		SAXReader reader = new SAXReader();
		org.dom4j.Document document = reader.read(memo);
		return document;
	}

	/**
	 * @param doc 文件
	 * @return 文件
	 */
	public static Element getRootElement(org.dom4j.Document doc) {
		return doc.getRootElement();
	}

	//*==============================================================
	@SuppressWarnings("unchecked")
	private static LinkedHashMap spellMap = null;

	static {
		if (spellMap == null) {
			spellMap = new LinkedHashMap(400);
		}
		initialize();
	}


	private static void spellPut(String spell, int ascii) {
		spellMap.put(spell, new Integer(ascii));
	}

	private static void initialize() {
		spellPut("a", -20319);
		spellPut("ai", -20317);
		spellPut("an", -20304);
		spellPut("ang", -20295);
		spellPut("ao", -20292);
		spellPut("ba", -20283);
		spellPut("bai", -20265);
		spellPut("ban", -20257);
		spellPut("bang", -20242);
		spellPut("bao", -20230);
		spellPut("bei", -20051);
		spellPut("ben", -20036);
		spellPut("beng", -20032);
		spellPut("bi", -20026);
		spellPut("bian", -20002);
		spellPut("biao", -19990);
		spellPut("bie", -19986);
		spellPut("bin", -19982);
		spellPut("bing", -19976);
		spellPut("bo", -19805);
		spellPut("bu", -19784);
		spellPut("ca", -19775);
		spellPut("cai", -19774);
		spellPut("can", -19763);
		spellPut("cang", -19756);
		spellPut("cao", -19751);
		spellPut("ce", -19746);
		spellPut("ceng", -19741);
		spellPut("cha", -19739);
		spellPut("chai", -19728);
		spellPut("chan", -19725);
		spellPut("chang", -19715);
		spellPut("chao", -19540);
		spellPut("che", -19531);
		spellPut("chen", -19525);
		spellPut("cheng", -19515);
		spellPut("chi", -19500);
		spellPut("chong", -19484);
		spellPut("chou", -19479);
		spellPut("chu", -19467);
		spellPut("chuai", -19289);
		spellPut("chuan", -19288);
		spellPut("chuang", -19281);
		spellPut("chui", -19275);
		spellPut("chun", -19270);
		spellPut("chuo", -19263);
		spellPut("ci", -19261);
		spellPut("cong", -19249);
		spellPut("cou", -19243);
		spellPut("cu", -19242);
		spellPut("cuan", -19238);
		spellPut("cui", -19235);
		spellPut("cun", -19227);
		spellPut("cuo", -19224);
		spellPut("da", -19218);
		spellPut("dai", -19212);
		spellPut("dan", -19038);
		spellPut("dang", -19023);
		spellPut("dao", -19018);
		spellPut("de", -19006);
		spellPut("deng", -19003);
		spellPut("di", -18996);
		spellPut("dian", -18977);
		spellPut("diao", -18961);
		spellPut("die", -18952);
		spellPut("ding", -18783);
		spellPut("diu", -18774);
		spellPut("dong", -18773);
		spellPut("dou", -18763);
		spellPut("du", -18756);
		spellPut("duan", -18741);
		spellPut("dui", -18735);
		spellPut("dun", -18731);
		spellPut("duo", -18722);
		spellPut("e", -18710);
		spellPut("en", -18697);
		spellPut("er", -18696);
		spellPut("fa", -18526);
		spellPut("fan", -18518);
		spellPut("fang", -18501);
		spellPut("fei", -18490);
		spellPut("fen", -18478);
		spellPut("feng", -18463);
		spellPut("fo", -18448);
		spellPut("fou", -18447);
		spellPut("fu", -18446);
		spellPut("ga", -18239);
		spellPut("gai", -18237);
		spellPut("gan", -18231);
		spellPut("gang", -18220);
		spellPut("gao", -18211);
		spellPut("ge", -18201);
		spellPut("gei", -18184);
		spellPut("gen", -18183);
		spellPut("geng", -18181);
		spellPut("gong", -18012);
		spellPut("gou", -17997);
		spellPut("gu", -17988);
		spellPut("gua", -17970);
		spellPut("guai", -17964);
		spellPut("guan", -17961);
		spellPut("guang", -17950);
		spellPut("gui", -17947);
		spellPut("gun", -17931);
		spellPut("guo", -17928);
		spellPut("ha", -17922);
		spellPut("hai", -17759);
		spellPut("han", -17752);
		spellPut("hang", -17733);
		spellPut("hao", -17730);
		spellPut("he", -17721);
		spellPut("hei", -17703);
		spellPut("hen", -17701);
		spellPut("heng", -17697);
		spellPut("hong", -17692);
		spellPut("hou", -17683);
		spellPut("hu", -17676);
		spellPut("hua", -17496);
		spellPut("huai", -17487);
		spellPut("huan", -17482);
		spellPut("huang", -17468);
		spellPut("hui", -17454);
		spellPut("hun", -17433);
		spellPut("huo", -17427);
		spellPut("ji", -17417);
		spellPut("jia", -17202);
		spellPut("jian", -17185);
		spellPut("jiang", -16983);
		spellPut("jiao", -16970);
		spellPut("jie", -16942);
		spellPut("jin", -16915);
		spellPut("jing", -16733);
		spellPut("jiong", -16708);
		spellPut("jiu", -16706);
		spellPut("ju", -16689);
		spellPut("juan", -16664);
		spellPut("jue", -16657);
		spellPut("jun", -16647);
		spellPut("ka", -16474);
		spellPut("kai", -16470);
		spellPut("kan", -16465);
		spellPut("kang", -16459);
		spellPut("kao", -16452);
		spellPut("ke", -16448);
		spellPut("ken", -16433);
		spellPut("keng", -16429);
		spellPut("kong", -16427);
		spellPut("kou", -16423);
		spellPut("ku", -16419);
		spellPut("kua", -16412);
		spellPut("kuai", -16407);
		spellPut("kuan", -16403);
		spellPut("kuang", -16401);
		spellPut("kui", -16393);
		spellPut("kun", -16220);
		spellPut("kuo", -16216);
		spellPut("la", -16212);
		spellPut("lai", -16205);
		spellPut("lan", -16202);
		spellPut("lang", -16187);
		spellPut("lao", -16180);
		spellPut("le", -16171);
		spellPut("lei", -16169);
		spellPut("leng", -16158);
		spellPut("li", -16155);
		spellPut("lia", -15959);
		spellPut("lian", -15958);
		spellPut("liang", -15944);
		spellPut("liao", -15933);
		spellPut("lie", -15920);
		spellPut("lin", -15915);
		spellPut("ling", -15903);
		spellPut("liu", -15889);
		spellPut("long", -15878);
		spellPut("lou", -15707);
		spellPut("lu", -15701);
		spellPut("lv", -15681);
		spellPut("luan", -15667);
		spellPut("lue", -15661);
		spellPut("lun", -15659);
		spellPut("luo", -15652);
		spellPut("ma", -15640);
		spellPut("mai", -15631);
		spellPut("man", -15625);
		spellPut("mang", -15454);
		spellPut("mao", -15448);
		spellPut("me", -15436);
		spellPut("mei", -15435);
		spellPut("men", -15419);
		spellPut("meng", -15416);
		spellPut("mi", -15408);
		spellPut("mian", -15394);
		spellPut("miao", -15385);
		spellPut("mie", -15377);
		spellPut("min", -15375);
		spellPut("ming", -15369);
		spellPut("miu", -15363);
		spellPut("mo", -15362);
		spellPut("mou", -15183);
		spellPut("mu", -15180);
		spellPut("na", -15165);
		spellPut("nai", -15158);
		spellPut("nan", -15153);
		spellPut("nang", -15150);
		spellPut("nao", -15149);
		spellPut("ne", -15144);
		spellPut("nei", -15143);
		spellPut("nen", -15141);
		spellPut("neng", -15140);
		spellPut("ni", -15139);
		spellPut("nian", -15128);
		spellPut("niang", -15121);
		spellPut("niao", -15119);
		spellPut("nie", -15117);
		spellPut("nin", -15110);
		spellPut("ning", -15109);
		spellPut("niu", -14941);
		spellPut("nong", -14937);
		spellPut("nu", -14933);
		spellPut("nv", -14930);
		spellPut("nuan", -14929);
		spellPut("nue", -14928);
		spellPut("nuo", -14926);
		spellPut("o", -14922);
		spellPut("ou", -14921);
		spellPut("pa", -14914);
		spellPut("pai", -14908);
		spellPut("pan", -14902);
		spellPut("pang", -14894);
		spellPut("pao", -14889);
		spellPut("pei", -14882);
		spellPut("pen", -14873);
		spellPut("peng", -14871);
		spellPut("pi", -14857);
		spellPut("pian", -14678);
		spellPut("piao", -14674);
		spellPut("pie", -14670);
		spellPut("pin", -14668);
		spellPut("ping", -14663);
		spellPut("po", -14654);
		spellPut("pu", -14645);
		spellPut("qi", -14630);
		spellPut("qia", -14594);
		spellPut("qian", -14429);
		spellPut("qiang", -14407);
		spellPut("qiao", -14399);
		spellPut("qie", -14384);
		spellPut("qin", -14379);
		spellPut("qing", -14368);
		spellPut("qiong", -14355);
		spellPut("qiu", -14353);
		spellPut("qu", -14345);
		spellPut("quan", -14170);
		spellPut("que", -14159);
		spellPut("qun", -14151);
		spellPut("ran", -14149);
		spellPut("rang", -14145);
		spellPut("rao", -14140);
		spellPut("re", -14137);
		spellPut("ren", -14135);
		spellPut("reng", -14125);
		spellPut("ri", -14123);
		spellPut("rong", -14122);
		spellPut("rou", -14112);
		spellPut("ru", -14109);
		spellPut("ruan", -14099);
		spellPut("rui", -14097);
		spellPut("run", -14094);
		spellPut("ruo", -14092);
		spellPut("sa", -14090);
		spellPut("sai", -14087);
		spellPut("san", -14083);
		spellPut("sang", -13917);
		spellPut("sao", -13914);
		spellPut("se", -13910);
		spellPut("sen", -13907);
		spellPut("seng", -13906);
		spellPut("sha", -13905);
		spellPut("shai", -13896);
		spellPut("shan", -13894);
		spellPut("shang", -13878);
		spellPut("shao", -13870);
		spellPut("she", -13859);
		spellPut("shen", -13847);
		spellPut("sheng", -13831);
		spellPut("shi", -13658);
		spellPut("shou", -13611);
		spellPut("shu", -13601);
		spellPut("shua", -13406);
		spellPut("shuai", -13404);
		spellPut("shuan", -13400);
		spellPut("shuang", -13398);
		spellPut("shui", -13395);
		spellPut("shun", -13391);
		spellPut("shuo", -13387);
		spellPut("si", -13383);
		spellPut("song", -13367);
		spellPut("sou", -13359);
		spellPut("su", -13356);
		spellPut("suan", -13343);
		spellPut("sui", -13340);
		spellPut("sun", -13329);
		spellPut("suo", -13326);
		spellPut("ta", -13318);
		spellPut("tai", -13147);
		spellPut("tan", -13138);
		spellPut("tang", -13120);
		spellPut("tao", -13107);
		spellPut("te", -13096);
		spellPut("teng", -13095);
		spellPut("ti", -13091);
		spellPut("tian", -13076);
		spellPut("tiao", -13068);
		spellPut("tie", -13063);
		spellPut("ting", -13060);
		spellPut("tong", -12888);
		spellPut("tou", -12875);
		spellPut("tu", -12871);
		spellPut("tuan", -12860);
		spellPut("tui", -12858);
		spellPut("tun", -12852);
		spellPut("tuo", -12849);
		spellPut("wa", -12838);
		spellPut("wai", -12831);
		spellPut("wan", -12829);
		spellPut("wang", -12812);
		spellPut("wei", -12802);
		spellPut("wen", -12607);
		spellPut("weng", -12597);
		spellPut("wo", -12594);
		spellPut("wu", -12585);
		spellPut("xi", -12556);
		spellPut("xia", -12359);
		spellPut("xian", -12346);
		spellPut("xiang", -12320);
		spellPut("xiao", -12300);
		spellPut("xie", -12120);
		spellPut("xin", -12099);
		spellPut("xing", -12089);
		spellPut("xiong", -12074);
		spellPut("xiu", -12067);
		spellPut("xu", -12058);
		spellPut("xuan", -12039);
		spellPut("xue", -11867);
		spellPut("xun", -11861);
		spellPut("ya", -11847);
		spellPut("yan", -11831);
		spellPut("yang", -11798);
		spellPut("yao", -11781);
		spellPut("ye", -11604);
		spellPut("yi", -11589);
		spellPut("yin", -11536);
		spellPut("ying", -11358);
		spellPut("yo", -11340);
		spellPut("yong", -11339);
		spellPut("you", -11324);
		spellPut("yu", -11303);
		spellPut("yuan", -11097);
		spellPut("yue", -11077);
		spellPut("yun", -11067);
		spellPut("za", -11055);
		spellPut("zai", -11052);
		spellPut("zan", -11045);
		spellPut("zang", -11041);
		spellPut("zao", -11038);
		spellPut("ze", -11024);
		spellPut("zei", -11020);
		spellPut("zen", -11019);
		spellPut("zeng", -11018);
		spellPut("zha", -11014);
		spellPut("zhai", -10838);
		spellPut("zhan", -10832);
		spellPut("zhang", -10815);
		spellPut("zhao", -10800);
		spellPut("zhe", -10790);
		spellPut("zhen", -10780);
		spellPut("zheng", -10764);
		spellPut("zhi", -10587);
		spellPut("zhong", -10544);
		spellPut("zhou", -10533);
		spellPut("zhu", -10519);
		spellPut("zhua", -10331);
		spellPut("zhuai", -10329);
		spellPut("zhuan", -10328);
		spellPut("zhuang", -10322);
		spellPut("zhui", -10315);
		spellPut("zhun", -10309);
		spellPut("zhuo", -10307);
		spellPut("zi", -10296);
		spellPut("zong", -10281);
		spellPut("zou", -10274);
		spellPut("zu", -10270);
		spellPut("zuan", -10262);
		spellPut("zui", -10260);
		spellPut("zun", -10256);
		spellPut("zuo", -10254);
	}


	public static Integer getCnAscii(char cn) {
		byte[] bytes = (String.valueOf(cn)).getBytes();
		if (bytes == null || bytes.length > 2 || bytes.length <= 0) { //错误
			return 0;
		}
		if (bytes.length == 1) { //英文字符
			return Integer.parseInt(bytes[0]+"");
		}
		if (bytes.length == 2) { //中文字符
			int hightByte = 256 + bytes[0];
			int lowByte = 256 + bytes[1];

			int ascii = (256 * hightByte + lowByte) - 256 * 256;

			//			System.out.println("ASCII=" + ascii);

			return ascii;
		}

		return 0; //错误
	}


	public static String getSpellByAscii(int ascii) {
		if (ascii > 0 && ascii < 160) { //单字符
			return String.valueOf((char) ascii);
		}

		if (ascii < -20319 || ascii > -10247) { //不知道的字符
			return null;
		}

		Set keySet = spellMap.keySet();
		Iterator it = keySet.iterator();

		String spell0 = null; ;
		String spell = null;

		int asciiRang0 = -20319;
		int asciiRang;
		while (it.hasNext()) {

			spell = (String) it.next();
			Object valObj = spellMap.get(spell);
			if (valObj instanceof Integer) {
				asciiRang = ((Integer) valObj).intValue();

				if (ascii >= asciiRang0 && ascii < asciiRang) { //区间找到
					return (spell0 == null) ? spell : spell0;
				} else {
					spell0 = spell;
					asciiRang0 = asciiRang;
				}
			}
		}

		return null;

	}


	public static String getFullSpell(String cnStr) {
		if (null == cnStr || "".equals(cnStr.trim())) {
			return cnStr;
		}

		char[] chars = cnStr.toCharArray();
		StringBuffer retuBuf = new StringBuffer();
		for (int i = 0, Len = chars.length; i < Len; i++) {
			int ascii = getCnAscii(chars[i]);
			if (ascii == 0) { //取ascii时出错
				retuBuf.append(chars[i]);
			} else {
				String spell = getSpellByAscii(ascii);
				if (spell == null) {
					retuBuf.append(chars[i]);
				} else {
					retuBuf.append(spell);
				} // end of if spell == null
			} // end of if ascii <= -20400
		} // end of for

		return retuBuf.toString();
	}

	public static String getFirstSpell(String cnStr) {
		return null;
	}

	/** 
	 * 从一个JSON 对象字符格式中得到一个java对象 
	 *  
	 * @param jsonString 
	 * @param pojoCalss 
	 * @return 
	 */  
	@SuppressWarnings("unchecked")
	public static Object getObject4JsonString(String jsonString, Class pojoCalss) {  
		Object pojo;  
		JSONObject jsonObject = JSONObject.fromObject(jsonString);  
		pojo = JSONObject.toBean(jsonObject, pojoCalss);  
		return pojo;  
	}  


	/** 
	 * 从 json HASH表达式中获取一个map，改map支持嵌套功能 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	@SuppressWarnings("unchecked")
	public static Map getMap4Json(String jsonString) {  
		JSONObject jsonObject = JSONObject.fromObject(jsonString);  
		Iterator keyIter = jsonObject.keys();  
		String key;  
		Object value;  
		Map valueMap = new HashMap();  

		while (keyIter.hasNext()) {  
			key = (String) keyIter.next();  
			value = jsonObject.get(key);  
			valueMap.put(key, value);  
		}  

		return valueMap;  
	}  


	/** 
	 * 从 json数组中得到相应java数组 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	public static Object[] getObjectArray4Json(String jsonString) {  
		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		return jsonArray.toArray();  

	}  


	/** 
	 * 从 json对象集合表达式中得到一个java对象列表 
	 *  
	 * @param jsonString 
	 * @param pojoClass 
	 * @return 
	 */  
	@SuppressWarnings("unchecked")
	public static List getList4Json(String jsonString, Class pojoClass) {  

		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		JSONObject jsonObject;  
		Object pojoValue;  

		List list = new ArrayList();  
		for (int i = 0; i < jsonArray.size(); i++) {  

			jsonObject = jsonArray.getJSONObject(i);  
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);  
			list.add(pojoValue);  

		}  
		return list;  

	}  


	/** 
	 * 从 json数组中解析出java字符串数组 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	public static String[] getStringArray4Json(String jsonString) {  

		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		String[] stringArray = new String[jsonArray.size()];  
		for (int i = 0; i < jsonArray.size(); i++) {  
			stringArray[i] = jsonArray.getString(i);  

		}  

		return stringArray;  
	}  


	/** 
	 * 从 json数组中解析出javaLong型对象数组 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	public static Long[] getLongArray4Json(String jsonString) {  

		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		Long[] longArray = new Long[jsonArray.size()];  
		for (int i = 0; i < jsonArray.size(); i++) {  
			longArray[i] = jsonArray.getLong(i);  

		}  
		return longArray;  
	}  


	/** 
	 * 从 json数组中解析出java Integer型对象数组 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	public static Integer[] getIntegerArray4Json(String jsonString) {  

		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		Integer[] integerArray = new Integer[jsonArray.size()];  
		for (int i = 0; i < jsonArray.size(); i++) {  
			integerArray[i] = jsonArray.getInt(i);  

		}  
		return integerArray;  
	}  

	/** 
	 * 从 json数组中解析出java Integer型对象数组 
	 *  
	 * @param jsonString 
	 * @return 
	 */  
	public static Double[] getDoubleArray4Json(String jsonString) {  

		JSONArray jsonArray = JSONArray.fromObject(jsonString);  
		Double[] doubleArray = new Double[jsonArray.size()];  
		for (int i = 0; i < jsonArray.size(); i++) {  
			doubleArray[i] = jsonArray.getDouble(i);  

		}  
		return doubleArray;  
	}  


	/**解决由于精度问题引起两个数相乘出现计算结果有偏差(2011-10-29 snd)
	 * @param number1
	 * @param number2
	 * @return
	 */
	public static double doubleMultiply(double number1, double number2) {      
		double resultnumber=0;

		BigDecimal numb1 = new BigDecimal(Double.toString(number1));
		BigDecimal numb2 = new BigDecimal(Double.toString(number2));

		resultnumber = numb1.multiply(numb2).doubleValue();

		return resultnumber;
	}

	/**把HH:mm格式的字符串转化成小数，用于excel模板中格式设置为[h]:mm的求运行小时的应用
	 * @param str
	 * @return
	 */
	public static double getstrtodouble(String str) {
		double temp=0;
		String[] strarray = str.split(":");
		temp = (Double.parseDouble(strarray[0])*60+Double.parseDouble(strarray[1]))/(60*24);
		return temp;
	}

	/**根据输入的年份，获得前10年和后10年
	 * @param nowyear
	 * @return
	 */
	public static ArrayList<String> yearlist(String nowyear){
		ArrayList<String> array=new ArrayList<String>();
		int startyear=Integer.valueOf(nowyear)-10;
		int endyear=Integer.valueOf(nowyear)+10;
		for(int i=startyear;i<=endyear;i++){
			array.add(String.valueOf(i));
		}

		return array;
	}

	public static double myRound(double d, int n) {
		d = d * Math.pow(10, n);
		d += 0.5d;
		d = (long)d;
		d = d / Math.pow(10d, n);
		return d;

	}
	/**通过前缀+日期自动生成ID编号,如PL-160223-01D
	 * @param str前缀
	 * @param length流水号长度
	 * @param fieldName字段名
	 * @param tableName数据表名
	 * @param nowDate日期
	 * @param addstr后缀
	 * @param cmp公司
	 * @return
	 */
	public static   String getAutoID(String str,int length,String fieldName,String tableName,String nowDate,
			String addstr,String cmp){	
//		公司名筛选,保留
//		String cmpstr="UE-";
//		ResultSet rs=CommSQL.queryStatic("select ename from tb_company where id='"+cmp+"'");
//		try {
//			if(rs.next()){
//				cmpstr+=rs.getString("ename")+"-";
//			}
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		String orderno="";
		String [] tempd = nowDate.substring(0,10).split("-");
		String tempStr =str+"-"+tempd[0].substring(2,4)+tempd[1]+tempd[2]+"-";		
		String sql="select max(replace("+fieldName+",'"+addstr+"','')) as productionOrderNo from "+tableName+" where "+fieldName+" like '"+tempStr+"%'";
		//System.out.println(sql);
		DailyPlanService dailyPlanService=(DailyPlanService)SpringContextUtil.getBean("dailyplanService");
		DailyPlan dailyplan=dailyPlanService.selectValueBySql(sql);
		if(dailyplan!=null){			
			String[] idstr=dailyplan.getProductionorderno().split("-");
			int tempId=Integer.parseInt(idstr[2])+1;
			String a=String.valueOf(tempId);
			for(int i=0;i<length-a.length();i++){
				tempStr+="0";
			}
			orderno+=tempStr+a+addstr;
		}else{			
			for(int i=0;i<length-1;i++){
				tempStr+="0";
			}
			orderno +=tempStr+"1"+addstr;
		}
		return orderno;			
	}

}





