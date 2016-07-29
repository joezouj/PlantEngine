package com.sipai.controller.process;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.process.TaskWorkstation;
import com.sipai.entity.user.User;
import com.sipai.service.process.TaskWorkstationService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/process/taskworkstation")
public class TaskWorkstationController {
	@Resource
	private TaskWorkstationService taskWorkstationService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		model.addAttribute("pid", request.getParameter("pid"));
		model.addAttribute("taskname", request.getParameter("taskname"));
		return "process/taskWorkstationList";
	}
	
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		if(sort==null){sort = " insdt ";}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "process/taskworkstation/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and processid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("taskname")!=null && !request.getParameter("taskname").isEmpty()){
			wherestr += " and taskname = '"+request.getParameter("taskname")+"' ";
		}
		PageHelper.startPage(page, rows);
        List<TaskWorkstation> list = this.taskWorkstationService.selectListByWhere(wherestr+orderstr);
        PageInfo<TaskWorkstation> pi = new PageInfo<TaskWorkstation>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getchecklist.do")
	public ModelAndView getchecklist(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		String orderstr=" order by id ";
		String wherestr=CommUtil.getwherestr("insuser", "process/taskworkstation/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and processid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("taskname")!=null && !request.getParameter("taskname").isEmpty()){
			wherestr += " and taskname = '"+request.getParameter("taskname")+"' ";
		}
		String result = "";
        List<TaskWorkstation> list = this.taskWorkstationService.selectListByWhere(wherestr+orderstr);
		for(int i=0;i<list.size();i++){
			result += list.get(i).getWorkstationid()+",";
		}
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		model.addAttribute("pid", request.getParameter("pid"));
		model.addAttribute("taskname", request.getParameter("taskname"));
		return "process/taskWorkstationAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute TaskWorkstation taskMaterial){
		User cu= (User)request.getSession().getAttribute("cu");		
		String wids = request.getParameter("wids");
		String pid = request.getParameter("pid");
		String taskname = request.getParameter("taskname");
		//保存前清空上次添加的内容
		String wherestr = "where 1=1 and processid='"+pid+"' and taskname='"+taskname+"'";
		taskWorkstationService.deleteByWhere(wherestr);
		
		String workstationids[] = {};
		if(wids!=null&&wids.length()>0){
			workstationids = wids.split(",");
		}
		int result = 0;
		int suc = 0;
		int fal = 0;
		for(int i=0;i<workstationids.length;i++){
			TaskWorkstation tEquipment = new TaskWorkstation();
			String id = CommUtil.getUUID();
			tEquipment.setId(id);
			tEquipment.setProcessid(pid);
			tEquipment.setTaskname(taskname);
			tEquipment.setWorkstationid(workstationids[i]);
			tEquipment.setInsdt(CommUtil.nowDate());
			tEquipment.setInsuser(cu.getId());
			tEquipment.setModifydt(CommUtil.nowDate());
			tEquipment.setModify(cu.getId());
			
			result = taskWorkstationService.save(tEquipment);
			if(result==1){
				suc++;
			}else{
				fal++;
			}
		}
		String resultStr = "";
		if(workstationids.length==0){
			resultStr += "已清除所有数据！";
		}
		if(suc>0){
			resultStr += "成功添加"+suc+"条数据！";
		}
		if(fal>0){
			resultStr += fal+"条数据添加失败！";
		}
		String resstr="{\"res\":\""+resultStr+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.taskWorkstationService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.taskWorkstationService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
