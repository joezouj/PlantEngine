package com.sipai.controller.process;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sipai.entity.user.User;
import com.sipai.entity.process.Process;
import com.sipai.service.process.ProcessService;



@Controller("processesController")
@RequestMapping("/process/process")
public class ProcessController {
	@Resource
	private Process process;
	
	@Resource(name="processesService")
	private ProcessService processService;
	
	
	@RequestMapping("/enable.do")
	public String enable(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		User cu= (User)request.getSession().getAttribute("cu");
		
		Process process = processService.selectById(id);
		process.setState(1);
		int result = this.processService.update(process);
		model.addAttribute("result", result);
		return "result";
	}
}
