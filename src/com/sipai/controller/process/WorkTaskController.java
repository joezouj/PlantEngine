package com.sipai.controller.process;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.model.ProcessModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.snaker.SnakerHelper;

@Controller
@RequestMapping("/process/worktask")
public class WorkTaskController {
	@Resource
	private SnakerEngineFacets facets;
	
	@RequestMapping("/snakertasklist.do")
	public String snakertasklist(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		Process process = facets.getEngine().process().getProcessById(id);
		AssertHelper.notNull(process);
		ProcessModel processModel = process.getModel();
		if(processModel != null) {
			String json = SnakerHelper.getModelJson(processModel);
			model.addAttribute("process", json);
		}
		return "process/snakerTaskList";
	}
	
	@RequestMapping("/configSnakerTask.do")
	public String configSnakerTask(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String pid, @RequestParam(value="taskname") String taskname){
		model.addAttribute("pid", pid);
		model.addAttribute("taskname", taskname);
		return "process/configSnakerTask";
	}
}
