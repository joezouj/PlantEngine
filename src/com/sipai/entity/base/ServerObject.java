package com.sipai.entity.base;

import java.util.Hashtable;

/**
 * @author IBM 服务器对象
 */

public class ServerObject {

	/**
	 * 记录所有服务器属性的hashtable，从serverconfig.xml读取
	 */
	public static Hashtable<String, String> atttable = new Hashtable<String, String>();

	public static Hashtable<String, String> getAtttable() {
		return atttable;
	}

	public static void setAtttable(Hashtable<String, String> atttable) {
		ServerObject.atttable = atttable;
	}
}
