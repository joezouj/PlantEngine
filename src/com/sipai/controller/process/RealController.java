package com.sipai.controller.process;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.model.ProcessModel;
import org.snaker.engine.model.TaskModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.process.Real;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.TaskWorkstation;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Line;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealDetailsWorkstationService;
import com.sipai.service.process.RealService;
import com.sipai.service.process.TaskWorkstationService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/real")
public class RealController {
	@Resource
	private Real real;
	
	@Resource
	private RealService realService;
	
	@Resource
	private SnakerEngineFacets facets;
	
	@Resource
	private RealDetailsService realDetailsService;
	
	@Resource
	private TaskWorkstationService taskWorkstationService;
	
	@Resource
	private RealDetailsWorkstation realDetailsWorkstation;
	@Resource
	private RealDetailsWorkstationService realDetailsWorkstationService;
	
	@RequestMapping("/showlist.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realList";
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
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<Real> list = this.realService.selectListByWhere(wherestr+orderstr);
        PageInfo<Real> pi = new PageInfo<Real>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getlistByProductid.do")
	public ModelAndView getlistByProductid(HttpServletRequest request,Model model) {
		String whereStr="";
		if(request.getParameter("productid")!=null){
			whereStr = "where productid = '"+request.getParameter("productid")+"'";
		}
        List<Real> list = this.realService.selectListByWhere(whereStr);
		JSONArray json=JSONArray.fromObject(list);
		
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getLineListByRealid.do")
	public ModelAndView getLineListByRealid(HttpServletRequest request,Model model) {
		
        List<Line> list = this.realService.getLineListByRealid(request.getParameter("realid"));
		JSONArray json=JSONArray.fromObject(list);
		
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		real = this.realService.selectById(id);
		model.addAttribute("real", real);
		return "process/realEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Real real){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.realService.checkNotOccupied(real.getId(),real.getName())){
			model.addAttribute("result", "{\"res\":\"产品工艺名称已存在\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		real.setId(id);
		real.setInsuser(cu.getId());
		real.setInsdt(CommUtil.nowDate());
		
		int result = this.realService.save(real);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		
		//添加已关联的工序、工位
		Process process = facets.getEngine().process().getProcessById(real.getProcessid());
		AssertHelper.notNull(process);
		ProcessModel processModel = process.getModel();
		if(processModel != null) {
			List<TaskWorkstation> workstationList = this.taskWorkstationService.selectListByWhere(" where processid='"+process.getId()+"'");
			
			List<TaskModel> tasklist = processModel.getTaskModels();
			for(int i=0;i<tasklist.size();i++){
				RealDetails realDetails = new RealDetails();
				realDetails.setId(CommUtil.getUUID());
				realDetails.setPid(id);
				realDetails.setTaskname(tasklist.get(i).getName());
				realDetails.setName(tasklist.get(i).getDisplayName());
				realDetails.setInsuser(cu.getId());
				realDetails.setInsdt(CommUtil.nowDate());
				
				int res = this.realDetailsService.save(realDetails);
				if(res>0){
					for(int j=0;j<workstationList.size();j++){
						if(workstationList.get(j).getTaskname().equals(tasklist.get(i).getName())){
							this.realDetailsWorkstation.setId(CommUtil.getUUID());
							this.realDetailsWorkstation.setPid(realDetails.getId());
							this.realDetailsWorkstation.setWorkstationid(workstationList.get(j).getWorkstationid());
							this.realDetailsWorkstation.setInsuser(cu.getId());
							this.realDetailsWorkstation.setInsdt(CommUtil.nowDate());
							this.realDetailsWorkstationService.save(realDetailsWorkstation);
						}
					}
				}
			}
		}
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Real real){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.realService.checkNotOccupied(real.getId(),real.getName())){
			model.addAttribute("result", "{\"res\":\"产品工艺名称已存在\"}");
			return "result";
		}
		real.setUpdateuser(cu.getId());
		real.setUpdatedt(CommUtil.nowDate());
		
		int result = this.realService.update(real);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+real.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		this.realService.deleteChildrenById(id);
		
		int result = this.realService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		this.realService.deleteChildrenByIds(ids);
		
		int result = this.realService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
