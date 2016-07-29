package com.sipai.controller.work;

import java.util.ArrayList;
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
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkTaskEquipment;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.service.process.RealDetailsEquipmentService;
import com.sipai.service.work.WorkTaskEquipmentService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/workTaskEquipment")
public class WorkTaskEquipmentController {
	@Resource
	private WorkTaskEquipment workTaskEquipment;
	@Resource
	private RealDetailsEquipmentService realDetailsEquipmentService;
	@Resource
	private WorkTaskEquipmentService workTaskEquipmentService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "work/districtequipment";
	}
	
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="workstationid") String workstationid) {
		String processrealdetailid= request.getParameter("processrealdetailid");
		
		User cu=(User)request.getSession().getAttribute("cu");
		
		if(sort==null){sort = " insdt ";}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "process/real/showlist.do", cu);
		if(request.getParameter("processrealdetailid")!=null && !request.getParameter("processrealdetailid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("processrealdetailid")+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
		List<RealDetailsEquipment> list_real = this.realDetailsEquipmentService.selectListByWhere(wherestr+orderstr);
        
        List<WorkTaskEquipment> list_select = this.workTaskEquipmentService.selectListByWhere("where taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+workstationid+"'");
		
        List<WorkTaskEquipment> list=new ArrayList<WorkTaskEquipment>();
        for(int i=0;i<list_real.size();i++){
        	WorkTaskEquipment item=new WorkTaskEquipment();
        	item.setId(CommUtil.getUUID());//必须设置id不然gird只能获取勾选的一条记录
        	item.setEquipment(list_real.get(i).getEquipment());
        	item.setEquipmentid(list_real.get(i).getEquipmentid());
        	list.add(item);
        }
        for(int i=0;i<list.size();i++){
			list.get(i).set_checked(false);
			for(int j=0;j<list_select.size();j++){
				if(list.get(i).getEquipmentid().equals(list_select.get(j).getEquipmentid())){
					list.get(i).set_checked(true);
					break;
				}
			}
		}
        PageInfo<RealDetailsEquipment> pi = new PageInfo<RealDetailsEquipment>(list_real);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realDetailsEquipmentAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		workTaskEquipment = this.workTaskEquipmentService.selectById(id);
		model.addAttribute("realDetailsEquipment", workTaskEquipment);
		return "process/realDetailsEquipmentEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="workstationid") String workstationid){
		String processrealdetailid= request.getParameter("processrealdetailid");
		
		User cu= (User)request.getSession().getAttribute("cu");
		int result=1;
		int resultflag=0;
		String[] item=ids.split(",");
		if(item!=null && item.length>0){
			this.workTaskEquipmentService.deleteByWhere("where 1=1 and taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+workstationid+"'");
			for(int i=0;i<item.length;i++){
				String id = CommUtil.getUUID();
				WorkTaskEquipment worktaskequipment=new WorkTaskEquipment();
				worktaskequipment.setId(id);
				worktaskequipment.setInsdt(CommUtil.nowDate());
				worktaskequipment.setInsuser(cu.getId());
				worktaskequipment.setEquipmentid(item[i]);
				worktaskequipment.setTaskid(taskid);
				worktaskequipment.setProcessrealdetailid(processrealdetailid);
				worktaskequipment.setWorkstationid(workstationid);
				resultflag = this.workTaskEquipmentService.save(worktaskequipment);
				if(resultflag==0){
					result=0;
				}
			}
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute WorkTaskEquipment workTaskEquipment){
		User cu= (User)request.getSession().getAttribute("cu");
		
		workTaskEquipment.setInsuser(cu.getId());
		workTaskEquipment.setInsdt(CommUtil.nowDate());
		
		int result = this.workTaskEquipmentService.update(workTaskEquipment);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workTaskEquipment.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workTaskEquipmentService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workTaskEquipmentService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
