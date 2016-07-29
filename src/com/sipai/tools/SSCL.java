package com.sipai.tools;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dom4j.Document;
import org.dom4j.Element;

import com.sipai.entity.base.ServerObject;
import com.sipai.tools.CommUtil;

/**
 * @author IBM 服务器配置监听程序
 */
public class SSCL implements ServletContextListener {

	public static String cont="";

	@SuppressWarnings("unchecked")
	public void contextInitialized(ServletContextEvent event) {

		ServletContext sc = event.getServletContext();
		cont = sc.getRealPath("//").substring(0,sc.getRealPath("//").lastIndexOf("\\"));

		try {
			Document dc = CommUtil.read(sc.getRealPath("/") + "/WEB-INF/ServerConfig.xml");
			Element rt = dc.getRootElement();
			List<Element> al = rt.elements("Attribute");
			Iterator<Element> it = al.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				ServerObject.getAtttable().put(
						e.attributeValue("name").toUpperCase(),
						e.getData().toString());
				e = null;
			}
			it = null;
			rt = null;
			dc = null;

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
