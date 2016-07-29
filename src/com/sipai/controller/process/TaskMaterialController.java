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
import com.sipai.entity.process.TaskMaterial;
import com.sipai.entity.user.User;
import com.sipai.service.process.TaskMaterialService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/process/taskmaterial")
public class TaskMaterialController {
	@Resource
	private TaskMaterialService taskMaterialService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		model.addAttribute("pid", request.getParameter("pid"));
		model.addAttribute("taskname", request.getParameter("taskname"));
		return "process/taskMaterialList";
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
		
		String wherestr=CommUtil.getwherestr("insuser", "process/taskmaterial/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and processid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("taskname")!=null && !request.getParameter("taskname").isEmpty()){
			wherestr += " and taskname = '"+request.getParameter("taskname")+"' ";
		}
		PageHelper.startPage(page, rows);
        List<TaskMaterial> list = this.taskMaterialService.selectListByWhere(wherestr+orderstr);
        PageInfo<TaskMaterial> pi = new PageInfo<TaskMaterial>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getchecklist.do")
	public ModelAndView getchecklist(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		String orderstr=" order by id ";
		String wherestr=CommUtil.getwherestr("insuser", "process/taskmaterial/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and processid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("taskname")!=null && !request.getParameter("taskname").isEmpty()){
			wherestr += " and taskname = '"+request.getParameter("taskname")+"' ";
		}
		String result = "";
		List<TaskMaterial> list = this.taskMaterialService.selectListByWhere(wherestr+orderstr);
		for(int i=0;i<list.size();i++){
			result += list.get(i).getMaterialid()+",";
		}
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		model.addAttribute("pid", request.getParameter("pid"));
		model.addAttribute("taskname", request.getParameter("taskname"));
		return "process/taskMaterialAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute TaskMaterial taskMaterial){
		User cu= (User)request.getSession().getAttribute("cu");		
		String mids = request.getParameter("mids");
		String pid = request.getParameter("pid");
		String taskname = request.getParameter("taskname");
		//保存前清空上次添加的内容
		String wherestr = "where 1=1 and processid='"+pid+"' and taskname='"+taskname+"'";
		taskMaterialService.deleteByWhere(wherestr);
		
		String materialids[] = {};
		if(mids!=null&&mids.length()>0){
			materialids = mids.split(",");
		}
		int result = 0;
		int suc = 0;
		int fal = 0;
		for(int i=0;i<materialids.length;i++){
			TaskMaterial tMaterial = new TaskMaterial();
			String id = CommUtil.getUUID();
			tMaterial.setId(id);
			tMaterial.setProcessid(pid);
			tMaterial.setTaskname(taskname);
			tMaterial.setMaterialid(materialids[i]);
			tMaterial.setInsdt(CommUtil.nowDate());
			tMaterial.setInsuser(cu.getId());
			tMaterial.setModifydt(CommUtil.nowDate());
			tMaterial.setModify(cu.getId());
			
			result = taskMaterialService.save(tMaterial);
			if(result==1){
				suc++;
			}else{
				fal++;
			}
		}
		String resultStr = "";
		if(materialids.length==0){
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
		int result = this.taskMaterialService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.taskMaterialService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
