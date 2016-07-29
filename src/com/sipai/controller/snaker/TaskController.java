package com.sipai.controller.snaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.user.User;
import com.sipai.snaker.SnakerEngineFacets;

/**
 * Snaker流程实例相关Controller
 */
@Controller
@RequestMapping(value = "/snaker/task")
public class TaskController {
	@Resource
	private SnakerEngineFacets facets;

	/**
	 * 任务查询列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request, Model model) {
		return "/snaker/taskList";
	}

	/**
	 * 全部活动的任务列表获取，注意流程有自己的分页工具page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getTasks.do")
	public ModelAndView getTasks(HttpServletRequest request, Model model) {
		User cu= (User)request.getSession().getAttribute("cu");
		// 页码
		int pages = 0;
		if (request.getParameter("page") != null
				&& !request.getParameter("page").isEmpty()) {
			pages = Integer.parseInt(request.getParameter("page"));
		} else {
			pages = 1;
		}
		// 每页数量
		int pagesize = 0;
		if (request.getParameter("rows") != null
				&& !request.getParameter("rows").isEmpty()) {
			pagesize = Integer.parseInt(request.getParameter("rows"));
		} else {
			pagesize = 20;
		}
	
		Page<Task> page = new Page<Task>();
		page.setPageNo(pages);
		page.setPageSize(pagesize);
		//引擎封装查询
//		QueryFilter filter = new QueryFilter();
//		if(request.getParameter("search_name")!=null&&!request.getParameter("search_name").isEmpty()){
//			filter.setName(request.getParameter("search_name"));
//		}
//		if(request.getParameter("search_displayname")!=null&&!request.getParameter("search_displayname").isEmpty()){
//			filter.setDisplayName(request.getParameter("search_displayname"));
//		}
//		List<Task> list = facets.getEngine().query()
//				.getActiveTasks(page, filter);
		//自定义sql 查询
		String whereStr = " where 1=1 ";
		if(request.getParameter("search_name")!=null&&!request.getParameter("search_name").isEmpty()){
			whereStr += " and task_Name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_displayname")!=null&&!request.getParameter("search_displayname").isEmpty()){
			whereStr += " and display_Name like '%"+request.getParameter("search_displayname")+"%' ";
		}
		//传参示例，多个参数自行扩展
		//Object[] args = {"电动机安装"};
		//List<Task> list = facets.getEngine().query().nativeQueryList(page, Task.class, "select * from wf_task "+whereStr+" and display_Name=?",args);
		List<Task> list = facets.getEngine().query().nativeQueryList(page, Task.class, "select * from wf_task "+whereStr);
		JSONArray json = JSONArray.fromObject(list);
		String result = "{\"total\":" + page.getTotalCount() + ",\"rows\":" + json + "}";

		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	
	/**
	 * 个人任务查询列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showplist.do")
	public String showplist(HttpServletRequest request, Model model) {
		return "/snaker/ptaskList";
	}
	/**
	 * 个人活动的任务列表获取
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/getPersonalTasks.do")
	public ModelAndView getPersonalTasks(HttpServletRequest request, Model model) {
		User cu= (User)request.getSession().getAttribute("cu");
		// 页码
		int pages = 0;
		if (request.getParameter("page") != null
				&& !request.getParameter("page").isEmpty()) {
			pages = Integer.parseInt(request.getParameter("page"));
		} else {
			pages = 1;
		}
		// 每页数量
		int pagesize = 0;
		if (request.getParameter("rows") != null
				&& !request.getParameter("rows").isEmpty()) {
			pagesize = Integer.parseInt(request.getParameter("rows"));
		} else {
			pagesize = 20;
		}

		Page<Task> page = new Page<Task>();
		page.setPageNo(pages);
		page.setPageSize(pagesize);
		QueryFilter filter = new QueryFilter();
		//业务判断是否为设定的操作人，可以写个过滤的类
		if(cu.getId().equals("有权限的处理人")){
			filter.setOperator("符合条件的值");
		}
		
		List<Task> list = facets.getEngine().query().getActiveTasks(page, filter);

		JSONArray json = JSONArray.fromObject(list);
		String result = "{\"total\":" + page.getTotalCount() + ",\"rows\":"+ json + "}";

		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	
	/**
	 * 任务执行
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/excute.do")
	public ModelAndView excute(HttpServletRequest request, Model model) {
		String taskId = request.getParameter("taskId");
		User cu= (User)request.getSession().getAttribute("cu");
		Map<String,Object> args = new HashMap<String,Object>();
		facets.getEngine().executeTask(taskId, cu.getId(), args);
		return new ModelAndView("/snaker/taskList");
	}
}
