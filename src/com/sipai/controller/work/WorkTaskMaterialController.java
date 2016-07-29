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
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.user.User;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.WorkTaskWorkStation;
import com.sipai.service.process.RealDetailsMaterialService;
import com.sipai.service.work.WorkTaskMaterialService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/work/workTaskMaterial")
public class WorkTaskMaterialController {
	@Resource
	private WorkTaskMaterial worktaskmaterial;
	
	@Resource
	private WorkTaskMaterialService workTaskMaterialService;
	
	@Resource
	private RealDetailsMaterialService realDetailsMaterialService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "work/districtmaterial";
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
        List<RealDetailsMaterial> list_real = this.realDetailsMaterialService.selectListByWhere(wherestr+orderstr);
        List<WorkTaskMaterial> list=new ArrayList<WorkTaskMaterial>();
        for(int i=0;i<list_real.size();i++){
        	WorkTaskMaterial item=new WorkTaskMaterial();
        	item.setId(CommUtil.getUUID());//必须设置id不然gird只能获取勾选的一条记录
        	item.setMaterialinfo(list_real.get(i).getMaterial());
        	item.setMaterialid(list_real.get(i).getMaterialid());
        	//默认配置量
        	item.setAmount(list_real.get(i).getAmount().toString());
        	list.add(item);
        }
        List<WorkTaskMaterial> list_select = this.workTaskMaterialService.selectListByWhere("where taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+workstationid+"'");
		for(int i=0;i<list.size();i++){
			list.get(i).set_checked(false);
			for(int j=0;j<list_select.size();j++){
				if(list.get(i).getMaterialid().equals(list_select.get(j).getMaterialid())){
					list.get(i).set_checked(true);
					if(list_select.get(j).getAmount()!=null){
						list.get(i).setAmount(list_select.get(j).getAmount());
					}
					break;
				}
			}
		}
        PageInfo<RealDetailsMaterial> pi = new PageInfo<RealDetailsMaterial>(list_real);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realDetailsMaterialAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		worktaskmaterial = this.workTaskMaterialService.selectById(id);
		model.addAttribute("realDetailsMaterial", worktaskmaterial);
		return "process/realDetailsMaterialEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids,
			@RequestParam(value="taskid") String taskid,
			@RequestParam(value="workstationid") String workstationid,
			@RequestParam(value="amounts") String amounts){
		String processrealdetailid= request.getParameter("processrealdetailid");
		
		User cu= (User)request.getSession().getAttribute("cu");
		int result=1;
		int resultflag=0;
		String[] item=ids.split(",");
		String[] item_amounts=amounts.split(",");
		if(item!=null && item.length>0){
			this.workTaskMaterialService.deleteByWhere("where 1=1 and taskid='"+taskid+"' and processrealdetailid='"+processrealdetailid+"' and workstationid='"+workstationid+"'");
			for(int i=0;i<item.length;i++){
				String id = CommUtil.getUUID();
				WorkTaskMaterial worktaskmaterial=new WorkTaskMaterial();
				worktaskmaterial.setId(id);
				worktaskmaterial.setInsdt(CommUtil.nowDate());
				worktaskmaterial.setInsuser(cu.getId());
				worktaskmaterial.setMaterialid(item[i]);
				worktaskmaterial.setTaskid(taskid);
				worktaskmaterial.setProcessrealdetailid(processrealdetailid);
				worktaskmaterial.setAmount(item_amounts[i]);
				worktaskmaterial.setWorkstationid(workstationid);
				resultflag = this.workTaskMaterialService.save(worktaskmaterial);
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
			@ModelAttribute WorkTaskMaterial workTaskMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		
		
		int result = this.workTaskMaterialService.update(workTaskMaterial);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workTaskMaterial.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.workTaskMaterialService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.workTaskMaterialService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
