package com.sipai.controller.snaker;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.HistoryOrder;
import org.snaker.engine.entity.HistoryTask;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.ProcessModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.user.User;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.snaker.SnakerHelper;


/**
 * 流程定义
 */
@Controller
@RequestMapping(value = "/snaker/process")
public class ProcessController {
	@Resource
	private SnakerEngineFacets facets;

	/**
	 * 初始化流程定义
	 * @return
	 */
	@RequestMapping("/init.do")
	public String init() {
		facets.initFlows();
		return "/snaker/processList";
	}
	
	/**
	 * 流程定义查询列表
	 * @param model
	 * @return
	 */
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/snaker/processList";
	}
	/**
	 * 流程列表获取，注意流程有自己的分页工具page
	 * @param model
	 * @return
	 */
	@RequestMapping("/getProcess.do")
	public ModelAndView getProcess(HttpServletRequest request,Model model) {
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
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
			if(sort.equals("displayName")){
				sort = "display_Name";
			}
		}else {
			sort = " create_Time ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = "asc";
		}
		Page<Process> page = new Page<Process>();
		page.setPageSize(pagesize);
		page.setPageNo(pages);
		QueryFilter filter = new QueryFilter();
		filter.setOrder(order);
		filter.setOrderBy(sort);
		if(StringHelper.isNotEmpty(request.getParameter("displayName"))) {
			filter.setDisplayName(request.getParameter("displayName"));
		}
		List<Process> list = facets.getEngine().process().getProcesss(page, filter);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+page.getTotalCount()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	/**
	 * 编辑流程定义（文字型，XML文件）
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit.do")
	public String edit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		Process process = facets.getEngine().process().getProcessById(id);
		model.addAttribute("process", process);
		if(process.getDBContent() != null) {
            try {
                model.addAttribute("content", StringHelper.textXML(new String(process.getDBContent(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
		return "snaker/processEdit";
	}
	
	/**
	 * 新建流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	@RequestMapping("/design.do")
	public String design(HttpServletRequest request,Model model) {
		return "snaker/processDesign";
	}
	
	/**
	 * 重用流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	@RequestMapping("/reuse.do")
	public String reuse(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		if(StringUtils.isNotEmpty(id)) {
			Process process = facets.getEngine().process().getProcessById(id);
			AssertHelper.notNull(process);
			ProcessModel processModel = process.getModel();
			if(processModel != null) {
				String json = SnakerHelper.getModelJson(processModel);
				model.addAttribute("process", json);
			}
			model.addAttribute("processId", id);
		}
		return "snaker/processReuse";
	}
	
	/**
	 * 保存流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	@RequestMapping("/deployXml.do")
	@ResponseBody
	public boolean deployXml(String model) {
		InputStream input = null;
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + SnakerHelper.convertXml(model);
			input = StreamHelper.getStreamFromString(xml);
			if(input != null){
				facets.getEngine().process().deploy(input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * 编辑流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	@RequestMapping("/redesign.do")
	public String redesign(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		if(StringUtils.isNotEmpty(id)) {
			Process process = facets.getEngine().process().getProcessById(id);
			AssertHelper.notNull(process);
			ProcessModel processModel = process.getModel();
			if(processModel != null) {
				String json = SnakerHelper.getModelJson(processModel);
				model.addAttribute("process", json);
			}
			model.addAttribute("processId", id);
		}
		return "snaker/processReDesign";
	}

	/**
	 * 重新发布流程定义[web流程设计器]
	 * @param model
	 * @return
	 */
	@RequestMapping("/redeployXml.do")
	@ResponseBody
	public boolean redeployXml(String model, @RequestParam(value="id") String id) {
		InputStream input = null;
		try {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + SnakerHelper.convertXml(model);
			input = StreamHelper.getStreamFromString(xml);
			if(StringUtils.isNotEmpty(id)) {
				facets.getEngine().process().redeploy(id, input);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据流程定义ID，卸载流程定义
	 * @param id
	 * @return
	 */
	@RequestMapping("/undeploy.do")
	public String undeploy(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id) {
		int result = 0;
		try {
			facets.getEngine().process().undeploy(id);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	/**
	 * 根据流程定义ID，级联删除主要用于流程定义、流程实例的数据。一般情况下，不建议在正式环境里使用此功能，顾名思义，会删除所有的关联数据，谨慎使用。
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
					facets.getEngine().process().cascadeRemove(ids[i]);
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

	/**
	 * 根据流程定义部署
	 * @param model
	 * @return
	 */
	@RequestMapping("/deploy.do")
	public String deploy(Model model) {
		return "snaker/processDeploy";
	}
	
	/**
	 * 添加流程定义后的部署
	 * @param snakerFile
	 * @param id
	 * @return
	 */
	@RequestMapping("/deploy.do")
	public String deploy(@RequestParam(value = "snakerFile") MultipartFile snakerFile,
			@RequestParam(value="id") String id) {
		InputStream input = null;
		try {
			input = snakerFile.getInputStream();
			if(StringUtils.isNotEmpty(id)) {
				facets.getEngine().process().redeploy(id, input);
			} else {
				facets.getEngine().process().deploy(input);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "snaker/processList";
	}
	
	
	@RequestMapping("/start.do")
	public String start(HttpServletRequest request,Model model, 
			@RequestParam(value="id") String id) {
		
		User user = (User)request.getSession().getAttribute("cu");
		//开始流程实例，并且运行到第一个任务
		facets.startInstanceById(id,user.getId(), null);
		//开始流程实例，运行并且执行第一个任务，直接运行到第二个任务
		//facets.startAndExecute(id,user.getId(), null);
		return "snaker/orderList";
	}
	
	@RequestMapping("/json.do")
	@ResponseBody
	public Object json(@RequestParam(value="orderId") String orderId) {
		HistoryOrder order = facets.getEngine().query().getHistOrder(orderId);
		List<Task> tasks = facets.getEngine().query().getActiveTasks(new QueryFilter().setOrderId(orderId));
		Process process = facets.getEngine().process().getProcessById(order.getProcessId());
		AssertHelper.notNull(process);
		ProcessModel model = process.getModel();
		Map<String, String> jsonMap = new HashMap<String, String>();
		if(model != null) {
			jsonMap.put("process", SnakerHelper.getModelJson(model));
		}
		
		if(tasks != null && !tasks.isEmpty()) {
			jsonMap.put("active", SnakerHelper.getActiveJson(tasks));
		}
		return jsonMap;
	}
	
	@RequestMapping("/display.do")
	public String display(Model model,
			@RequestParam(value="orderId") String orderId) {
		HistoryOrder order = facets.getEngine().query().getHistOrder(orderId);
		model.addAttribute("order", order);
		List<HistoryTask> tasks = facets.getEngine().query().getHistoryTasks(new QueryFilter().setOrderId(orderId));
		model.addAttribute("tasks", tasks);
		return "snaker/processView";
	}
	
	/**
	 * 流程列表获取，用于选择
	 * @param model
	 * @return
	 */
	@RequestMapping("/getListForSelect.do")
	public ModelAndView getListForSelect(HttpServletRequest request,Model model) {
		QueryFilter filter = new QueryFilter();
		filter.setOrderBy(" create_Time ");
		filter.setOrder("asc");
		filter.setState(1);
		
		List<Process> list = facets.getEngine().process().getProcesss(filter);
		if(list!=null){
			for(int i=0;i<list.size();i++){
				list.get(i).setDisplayName(list.get(i).getDisplayName()+" [V"+list.get(i).getVersion()+"]");
			}
		}
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
		return new ModelAndView("result");
	}
}
