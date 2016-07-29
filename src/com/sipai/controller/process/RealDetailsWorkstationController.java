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
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.entity.process.TaskWorkstation;
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealDetailsWorkstationService;
import com.sipai.service.process.RealService;
import com.sipai.service.process.TaskWorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetailsWorkstation")
public class RealDetailsWorkstationController {
	@Resource
	private TaskWorkstationService taskWorkstationService;
	
	@Resource
	private RealDetailsWorkstation realDetailsWorkstation;
	
	@Resource
	private RealDetailsWorkstationService realDetailsWorkstationService;
	
	@Resource
	private RealService realService;
	@Resource
	private RealDetailsService realDetailsService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsWorkstationList";
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
		
		String wherestr=CommUtil.getwherestr("insuser", "process/real/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}

		PageHelper.startPage(page, rows);
        List<RealDetailsWorkstation> list = this.realDetailsWorkstationService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetailsWorkstation> pi = new PageInfo<RealDetailsWorkstation>(list);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String realdetailsid){
		return "process/realDetailsWorkstationAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		realDetailsWorkstation = this.realDetailsWorkstationService.selectById(id);
		model.addAttribute("realDetailsWorkstation", realDetailsWorkstation);
		return "process/realDetailsWorkstationEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsWorkstation realDetailsWorkstation){
		User cu= (User)request.getSession().getAttribute("cu");
		//清除所有旧数据
		this.realDetailsWorkstationService.deleteByWhere("where pid in ('"+request.getParameter("pid")+"')");
		
		String resultstr="";
		int result = 0;
		int suc = 0;
		int fal = 0;
		if(!request.getParameter("wids").equals("")){
			String wids[]=request.getParameter("wids").split(",");
			if(wids.length>0){
				for(int i=0;i<wids.length;i++){
					String id = CommUtil.getUUID();
					realDetailsWorkstation.setId(id);
					realDetailsWorkstation.setPid(request.getParameter("pid"));
					realDetailsWorkstation.setWorkstationid(wids[i]);
					realDetailsWorkstation.setInsuser(cu.getId());
					realDetailsWorkstation.setInsdt(CommUtil.nowDate());
					
					result = this.realDetailsWorkstationService.save(realDetailsWorkstation);
					if(result==1){
						suc++;
					}else{
						fal++;
					}
				}
			}
		}
		
		if(request.getParameter("wids").equals("")){
			resultstr += "已清除所有数据！";
		}
		if(suc>0){
			resultstr += "成功添加"+suc+"条数据！";
		}
		if(fal>0){
			resultstr += fal+"条数据添加失败！";
		}
//		System.out.println(resultstr);
		model.addAttribute("result", resultstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsWorkstation realDetailsWorkstation){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetailsWorkstation.setUpduser(cu.getId());
		realDetailsWorkstation.setUpddt(CommUtil.nowDate());
		
		int result = this.realDetailsWorkstationService.update(realDetailsWorkstation);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetailsWorkstation.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.realDetailsWorkstationService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.realDetailsWorkstationService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getTaskWorkstation.do")
	public ModelAndView getTaskWorkstation(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		
		if(sort==null){
			sort = " tb_work_workstation.serial ";
		}else{
			switch (sort) {
			case "serial":
				sort = " tb_work_workstation.serial ";
				break;
			case "name":
				sort = " tb_work_workstation.name ";
				break;
			case "typename":
				sort = " tb_work_workstation.typename ";
				break;
			case "deptname":
				sort = " tb_dept.name ";
				break;
			case "linename":
				sort = " tb_work_line.name ";
				break;
			default:
				break;
			}
		}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and tb_process_real_details.id='"+request.getParameter("pid")+"'";
			RealDetails realDetail= this.realDetailsService.selectById(request.getParameter("pid"));
			wherestr += " and tb_process_task_workstation.taskname = '"+realDetail.getTaskname()+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and tb_work_workstation.name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += " and tb_work_workstation.serial like '%"+request.getParameter("search_code")+"%' ";
		}
		PageHelper.startPage(page, rows);
        List<TaskWorkstation> list = this.taskWorkstationService.selectListByWhere1(wherestr+orderstr);
        PageInfo<TaskWorkstation> pi = new PageInfo<TaskWorkstation>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getlist1.do")
	public ModelAndView getlist1(HttpServletRequest request,Model model) {
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}

        List<RealDetailsWorkstation> list = this.realDetailsWorkstationService.selectListByWhere(wherestr);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
