package com.sipai.tools;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * 用于过滤需要拦截的JSP文件
 */
public class SessionFilter implements Filter {

	private static final Logger logger = Logger.getLogger(SessionFilter.class);

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		String url = req.getRequestURI();
		logger.info(url);
		
		if (url.indexOf("/Login/login.do") < 0 &&
				url.indexOf("/Login/validate.do") < 0 &&
				url.indexOf("/Login/logout.do") < 0 &&
				url.indexOf("/app/") < 0 &&
				url.indexOf("/proapp/") < 0 &&
				url.indexOf("/msgapp/") < 0) {
			HttpSession session = req.getSession(false);
			if (SessionManager.isOnline(session)) {
				chain.doFilter(request, response);
			}else{
				if (req.getHeader("x-requested-with") == null){
					req.getRequestDispatcher("/timeout.jsp").forward(request, response);
				}else{
					if(req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
						resp.setHeader("sessionstatus", "timeout");//在响应头设置session状态 
						resp.setCharacterEncoding("UTF-8");
						resp.getWriter().print("请重新登陆！");
						chain.doFilter(request, response);
					}
				}
			}
		}else{
			chain.doFilter(request, response);
		}
		return;//不加return的话，doFilter会出现连接错误
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {
	}
}
