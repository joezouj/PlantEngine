package com.sipai.controller.snaker;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.snaker.SnakerEngineFacets;

/**
 * Snaker流程实例相关Controller
 */
@Controller
@RequestMapping(value = "/snaker/order")
public class OrderController {
	@Resource
	private SnakerEngineFacets facets;

	/**
	 * 流程定义查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/snaker/orderList";
	}
	
	/**
	 * 流程定义历史查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/showhistorylist.do")
	public String showhistorylist(HttpServletRequest request,Model model) {
		return "/snaker/historyorderList";
	}
	
	/**
	 * 流程实例列表获取，注意流程有自己的分页工具page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getOrders.do")
	public ModelAndView getOrders(HttpServletRequest request,Model model) { 
		//页码
		int pages = 0;
		if(request.getParameter("page")!=null&&!request.getParameter("page").isEmpty()){
			pages = Integer.parseInt(request.getParameter("page"));
		}else {
			pages = 1;
		}
		//每页数量
		int pagesize = 0;
		if(request.getParameter("rows")!=null&&!request.getParameter("rows").isEmpty()){
			pagesize = Integer.parseInt(request.getParameter("rows"));
		}else {
			pagesize = 20;
		}
		
		Page<Order> page = new Page<Order>();
		page.setPageNo(pages);
		page.setPageSize(pagesize);
		QueryFilter filter = new QueryFilter();
		//List<Order> list = facets.getEngine().query().nativeQueryList(page, Order.class, "select * from wf_order ",filter);
		List<Order> list = facets.getEngine().query().getActiveOrders(page,filter);
		
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+page.getTotalCount()+",\"rows\":"+json+"}";
		
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	/**
	 * 流程实例活动任务列表获取，注意流程有自己的分页工具page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getActiveOrders.do")
	public ModelAndView getActiveOrders(HttpServletRequest request,Model model) {   
		Page<Order> page = new Page<Order>();
		QueryFilter filter = new QueryFilter();
		List<Order> list = facets.getEngine().query().getActiveOrders(page,filter);
		
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+page.getTotalCount()+",\"rows\":"+json+"}";
		
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	/**
	 * 流程实例历史任务列表获取，注意流程有自己的分页工具page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getHistoryOrders.do")
	public ModelAndView getHistoryOrders(HttpServletRequest request,Model model) {   
		Page<HistoryOrder> page = new Page<HistoryOrder>();
		QueryFilter filter = new QueryFilter();
		List<HistoryOrder> list = facets.getEngine().query().getHistoryOrders(page, filter);
		
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+page.getTotalCount()+",\"rows\":"+json+"}";
		
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	/**
	 * 根据orderId废弃流程实例
	 * @param id
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/terminate.do")
	public String terminate(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		int result = 0;
		try {
			facets.getEngine().order().terminate(id,facets.getEngine().ADMIN);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 根据orderId唤醒流程实例
	 * @param id
	 * @return
	 */
	@RequestMapping("/resume.do")
	public String resume(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		int result = 0;
		try {
			facets.getEngine().order().resume(id);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 根据orderId，级联删除主要用于流程实例的数据。一般情况下，不建议在正式环境里使用此功能，顾名思义，会删除所有的关联数据，谨慎使用。
	 * @param id
	 * @return
	 */
	@RequestMapping("/cascadeRemove.do")
	public String cascadeRemove(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		int result = 0;
		if(id!=null&&id.length()>1){
			String[] ids = id.split(",");
			for(int i=0;i<ids.length;i++){
				try {
					facets.getEngine().order().cascadeRemove(ids[i]);
					result++;
				} catch (Exception e) {
					e.printStackTrace();
					result--;
				}
			}	
		}
		model.addAttribute("result", result);
		return "result";
	}
}
