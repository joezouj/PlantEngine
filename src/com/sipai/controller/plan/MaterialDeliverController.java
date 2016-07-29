package com.sipai.controller.plan;

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
import com.sipai.entity.plan.DeliverDetail;
import com.sipai.entity.plan.DeliverProcessor;
import com.sipai.entity.plan.MaterialDeliver;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialInfoService;
import com.sipai.service.plan.DeliverDetailService;
import com.sipai.service.plan.DeliverProcessorService;
import com.sipai.service.plan.MaterialDeliverService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/materialdeliver")
public class MaterialDeliverController {
	@Resource
	private MaterialDeliverService materialDeliverService;
	@Resource
	private MaterialInfoService materialInfoService;
	@Resource
	private WorkstationService workstationService;
	@Resource
	private DeliverDetailService deliverDetailService;
	@Resource
	private DeliverProcessorService deliverProcessorService;
	
	@RequestMapping("/addMaterialMission.do")
	public String addMaterialMission(HttpServletRequest request,Model model) {
		return "/plan/materialMission";
	}
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/plan/materialDeliverList";
	}
	@RequestMapping("/showProcessorlist.do")
	public String showProcesslist(HttpServletRequest request,Model model) {
		return "/plan/processorList";
	}
	@RequestMapping("/getMaterialDelivers.do")
	public ModelAndView getMaterialDelivers(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		int pages = 0;
		if(request.getParameter("page")!=null&&!request.getParameter("page").isEmpty()){
			pages = Integer.parseInt(request.getParameter("page"));
		}else {
			pages = 1;
		}
		int pagesize = 0;
		if(request.getParameter("rows")!=null&&!request.getParameter("rows").isEmpty()){
			pagesize = Integer.parseInt(request.getParameter("rows"));
		}else {
			pagesize = 20;
		}
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else {
			sort = " deliver.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("deliver.insuser", "plan/materialdeliver/showlist.do", cu);
		if(request.getParameter("search_workstationname")!=null && !request.getParameter("search_workstationname").isEmpty()){
			wherestr += " and workstation.name like '%"+request.getParameter("search_workstationname")+"%'";
		}
		if(request.getParameter("search_workstationserial")!=null && !request.getParameter("search_workstationserial").isEmpty()){
			wherestr += " and workstation.serial like '%"+request.getParameter("search_workstationserial")+"%'";
		}
		PageHelper.startPage(pages, pagesize);
        List<MaterialDeliver> list = this.materialDeliverService.getMaterialDeliver(wherestr+orderstr);
        
        PageInfo<MaterialDeliver> page = new PageInfo<MaterialDeliver>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getProcessorDeliver.do")
	public ModelAndView getProcessorDeliver(HttpServletRequest request,Model model) {
		User cu=(User)request.getSession().getAttribute("cu");
		int pages = 0;
		if(request.getParameter("page")!=null&&!request.getParameter("page").isEmpty()){
			pages = Integer.parseInt(request.getParameter("page"));
		}else {
			pages = 1;
		}
		int pagesize = 0;
		if(request.getParameter("rows")!=null&&!request.getParameter("rows").isEmpty()){
			pagesize = Integer.parseInt(request.getParameter("rows"));
		}else {
			pagesize = 20;
		}
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else {
			sort = " deliver.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = " where 1=1 and deliver.status in ('1','2') and processor.processorid='"+cu.getId()+"'";
		if(request.getParameter("search_workstationname")!=null && !request.getParameter("search_workstationname").isEmpty()){
			wherestr += " and workstation.name like '%"+request.getParameter("search_workstationname")+"%'";
		}
		if(request.getParameter("search_workstationserial")!=null && !request.getParameter("search_workstationserial").isEmpty()){
			wherestr += " and workstation.serial like '%"+request.getParameter("search_workstationserial")+"%'";
		}
		PageHelper.startPage(pages, pagesize);
        List<MaterialDeliver> list = this.materialDeliverService.getProcessorDeliver(wherestr+orderstr);
        
        PageInfo<MaterialDeliver> page = new PageInfo<MaterialDeliver>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		model.addAttribute("nowdate", CommUtil.nowDate());
		return "/plan/materialDeliverAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialDeliver") MaterialDeliver materialDeliver){
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		materialDeliver.setId(id);
		materialDeliver.setStatus("0");
		materialDeliver.setInsuser(cu.getId());
		materialDeliver.setInsdt(CommUtil.nowDate());
		int result = this.materialDeliverService.save(materialDeliver);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	

	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		model.addAttribute("materialDeliver", materialDeliver);
		return "/plan/materialDeliverEdit";
	}
	
	@RequestMapping("/distribute.do")
	public String dodistribute(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		model.addAttribute("materialDeliver", materialDeliver);
		return "/plan/materialDeliverDistribute";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialDeliver") MaterialDeliver materialDeliver){
		User cu= (User)request.getSession().getAttribute("cu");
		materialDeliver.setInsuser(cu.getId());
		materialDeliver.setInsdt(CommUtil.nowDate());
		int result = this.materialDeliverService.update(materialDeliver);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialDeliver.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		model.addAttribute("materialDeliver", materialDeliver);
		return "/plan/materialDeliverView";
	}
	
	@RequestMapping("/excutemission.do")
	public String excutemission(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		model.addAttribute("materialDeliver", materialDeliver);
		return "/plan/excuteMission";
	}
	
	@RequestMapping("/deliver.do")
	public String deliver(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		User cu= (User)request.getSession().getAttribute("cu");
		int result = 0;
		String feedback = "";
		String dsql = "where 1=1 and d.pid ='"+id+"' order by d.insdt";
        List<DeliverDetail> dlist = this.deliverDetailService.getDeliverDetail(dsql);
        if(dlist.size()==0){
        	feedback += "物料未分配，";
        }
        String psql = "where 1=1 and p.pid ='"+id+"' order by p.insdt";
        List<DeliverProcessor> plist = this.deliverProcessorService.getDeliverProcessor(psql);
        if(plist.size()==0){
        	feedback += "人员未分配，";
        }
        if(feedback.length()>0){
        	feedback += "请分配后下发任务！";
        }
		if(dlist.size()>0&&plist.size()>0){
			MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
			if(materialDeliver!=null&&!materialDeliver.getId().isEmpty()){
				materialDeliver.setStatus("1");
				materialDeliver.setModify(cu.getId());
				materialDeliver.setModifydt(CommUtil.nowDate());
				result = this.materialDeliverService.update(materialDeliver);
			}
		}
		String resstr="{\"result\":"+result+",\"feedback\":\""+feedback+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/revoke.do")
	public String revoke(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = 0;
		User cu= (User)request.getSession().getAttribute("cu");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		if(materialDeliver!=null&&!materialDeliver.getId().isEmpty()){
			materialDeliver.setStatus("0");
			materialDeliver.setModify(cu.getId());
			materialDeliver.setModifydt(CommUtil.nowDate());
			result = this.materialDeliverService.update(materialDeliver);
		}
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialDeliverService.deleteById(id);
		if(result==1){
			String wherestr = " where pid = '"+id+"' ";
			deliverDetailService.deleteByWhere(wherestr);
			deliverProcessorService.deleteByWhere(wherestr);
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialDeliverService.deleteByWhere("where id in ('"+ids+"')");
		if(result==1){
			String wherestr = " where pid in ('"+ids+"')";
			deliverDetailService.deleteByWhere(wherestr);
			deliverProcessorService.deleteByWhere(wherestr);
		}
		model.addAttribute("result", result);
		return "result";
	}
}
