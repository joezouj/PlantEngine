package com.sipai.controller.app;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.snaker.engine.entity.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.sipai.entity.document.Data;
import com.sipai.entity.material.MaterialShort;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.user.User;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.GroupMember;
import com.sipai.entity.work.Line;
import com.sipai.entity.work.WorkInterrupt;
import com.sipai.entity.work.WorkOrderExcute;
import com.sipai.entity.work.WorkTaskMaterial;
import com.sipai.entity.work.Workstation;
import com.sipai.service.base.LoginService;
import com.sipai.service.material.MaterialShortService;
import com.sipai.service.msg.MsgRecvService;
import com.sipai.service.msg.MsgServiceImpl;
import com.sipai.service.work.LineService;
import com.sipai.service.work.WorkInterruptService;
import com.sipai.service.work.WorkOrderExcuteService;
import com.sipai.service.work.WorkOrderService;
import com.sipai.service.work.WorkTaskMaterialService;
import com.sipai.service.work.WorkstationService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping(value = "proapp")
public class ProAppController {

	@Resource
	private LoginService loginService;
	@Resource
	private WorkTaskMaterialService workTaskMaterialService;
	@Resource
	private LineService lineService;
	@Resource
	private WorkstationService workstationService;
	
	@Resource
	private WorkOrderExcuteService workOrderExcuteService;
	@Resource
	private SnakerEngineFacets facets;
	@Resource
	private WorkInterruptService workInterruptService;
	@Resource
	private MaterialShortService materialShortService;
	@Resource
	private WorkOrderService workOrderService;
	
	@RequestMapping("/getlist_batching.do")
	public ModelAndView getlist_batching(HttpServletRequest request,Model model) {
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			model.addAttribute("result","");
	        return new ModelAndView("result");
		}
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
		}else{
			sort = " wtm.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " desc ";
		}
		String orderstr=" order by  "+sort+" "+order;
		
		String wherestr="";//CommUtil.getwherestr("insuser", "proapp/getlist_batching.do", cu);		
		wherestr+="where CONVERT(varchar(100), pd.eddt, 120)>CONVERT(varchar(100), GETDATE(), 120) and CONVERT(varchar(100), pd.stdt, 120)<CONVERT(varchar(100), GETDATE(), 120)";
		wherestr+=" and ('"+cu.getId()+"' ='emp01' or tu.id='"+cu.getId()+"')";
		PageHelper.startPage(pages, pagesize);
		List<WorkTaskMaterial> list = this.workTaskMaterialService.batchinglistByWhere(wherestr+orderstr);
        PageInfo<WorkTaskMaterial> page = new PageInfo<WorkTaskMaterial>(list);
		JSONArray json=JSONArray.fromObject(list);
		String deptid="";
		if (list!=null&&list.size()>0) {
			deptid=list.get(0).get_deptid();
		}
		List<Line> list_line = this.lineService.selectListByWhere(" where deptid='"+deptid+"' order by name asc");
		JSONArray json_line=JSONArray.fromObject(list_line);
		
		String result="{\"pi\":[{\"pageNum\":\""+page.getPageNum()+"\",\"pageCount\":\""+page.getPages()+"\"}],\"re1\":"+json+",\"re2\":"+json_line+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_ws.do")
	public ModelAndView getlist_ws(HttpServletRequest request,Model model){
		String lineid= request.getParameter("lineid");
		List<Workstation> list = this.workstationService.selectListByWhere("where lineid='"+lineid+"'");		
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"re1\":"+json+"}";
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	/*
	 * 更新分配物料信息
	 *
	 */
	@RequestMapping("/upmaterial_batching.do")
	public ModelAndView upmaterial_batching(HttpServletRequest request,Model model){
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			model.addAttribute("result","");
	        return new ModelAndView("result");
		}
		String worktaskmaterialid = request.getParameter("id");		
		String finishnum = request.getParameter("finishnum");	
		WorkTaskMaterial workTaskMaterial = this.workTaskMaterialService.selectById(worktaskmaterialid);		
		workTaskMaterial.setBatchingflag("2");
		workTaskMaterial.setBatchingamount(finishnum);
		workTaskMaterial.setBuser(cu.getId());
		workTaskMaterial.setBdt(CommUtil.nowDate());
		int result=this.workTaskMaterialService.update(workTaskMaterial);
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	
	/*
	 * 更新到料信息
	 */
	@RequestMapping("/upmaterial_arrival.do")
	public ModelAndView upmaterial_arrival(HttpServletRequest request,Model model){
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			model.addAttribute("result","");
	        return new ModelAndView("result");
		}
		String worktaskmaterialid = request.getParameter("id");		
		String arrivalamount = request.getParameter("arrivalamount");	
		WorkTaskMaterial workTaskMaterial = this.workTaskMaterialService.selectById(worktaskmaterialid);	
		workTaskMaterial.setBatchingflag("1");
		workTaskMaterial.setArrivalamount(arrivalamount);
		workTaskMaterial.setBuser(cu.getId());
		workTaskMaterial.setBdt(CommUtil.nowDate());
		int result=this.workTaskMaterialService.update(workTaskMaterial);
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	/*
	 * snaker执行“下一步”操作
	 * */
	@RequestMapping("/snakerfun1.do")
	public String snakerfun1(HttpServletRequest request,Model model){
		String id = request.getParameter("taskId");
		String wftaskid=request.getParameter("wftaskId");
		String productId=request.getParameter("productId");
		WorkOrderExcute workOrderExcute = this.workOrderExcuteService.selectById(id);
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			String resstr="{\"res\":\"0\"}";
			model.addAttribute("result",resstr);
	        return "result";
		}
		int result = 0;
		if(workOrderExcute == null){
			workOrderExcute=new WorkOrderExcute();
			workOrderExcute.setId(id);
			workOrderExcute.setInsuser(cu.getId());
			workOrderExcute.setInsdt(CommUtil.nowDate());
			workOrderExcute.setWftaskid(wftaskid);
			result = this.workOrderExcuteService.save(workOrderExcute);
		}else{
			workOrderExcute.setUpdateuser(cu.getId());
			workOrderExcute.setUpdatedt(CommUtil.nowDate());
			workOrderExcute.setWftaskid(wftaskid);
			result = this.workOrderExcuteService.update(workOrderExcute);
		}
		
		//执行流程
		if(result>0){
			Map<String,Object> args = new HashMap<String,Object>();
			//偷懒未添加班组--使用超级管理员
			//facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), cu.getId(), args);
			List<Task> tasks=facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), "snaker.admin", args);
			if(tasks!=null && tasks.size()>0){
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), tasks.get(0).getDisplayName());
			}else{
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), "已完成");
			}
			this.workInterruptService.deleteByWhere("where productid='"+productId+"' and wftaskid='"+wftaskid+"'");//清除挂起数据
		}
		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workOrderExcute.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	/*
	 * snaker执行“跳转”操作
	 * */
	@RequestMapping("/snakerfun2.do")
	public String snakerfun2(HttpServletRequest request,Model model){
		String id = request.getParameter("taskId");
		String wftaskid=request.getParameter("wftaskId");
		
		WorkOrderExcute workOrderExcute = this.workOrderExcuteService.selectById(id);
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			String resstr="{\"res\":\"0\"}";
			model.addAttribute("result",resstr);
	        return "result";
		}
		int result = 0;
		if(workOrderExcute == null){
			workOrderExcute=new WorkOrderExcute();
			workOrderExcute.setId(id);
			workOrderExcute.setInsuser(cu.getId());
			workOrderExcute.setInsdt(CommUtil.nowDate());
			workOrderExcute.setWftaskid(wftaskid);
			result = this.workOrderExcuteService.save(workOrderExcute);
		}else{
			workOrderExcute.setUpdateuser(cu.getId());
			workOrderExcute.setUpdatedt(CommUtil.nowDate());
			workOrderExcute.setWftaskid(wftaskid);
			result = this.workOrderExcuteService.update(workOrderExcute);
		}
		boolean passFlag = false;
		String pass = request.getParameter("pass");
		if(pass.equals("1")){
			passFlag = true;
		}
		//执行流程
		if(result>0){
			Map<String,Object> args = new HashMap<String,Object>();
			args.put("pass", passFlag);
			//facets.getEngine().executeTask(workOrderExcute.getWftaskid(), cu.getId(), args);
			//偷懒未添加班组--使用超级管理员
			//System.out.println("UserID-----"+cu.getId());
			//facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), cu.getId(), args);
			List<Task> tasks=facets.executeAndUpdateActors(workOrderExcute.getWftaskid(), "snaker.admin", args);
			if(tasks!=null && tasks.size()>0){
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), tasks.get(0).getDisplayName());
			}else{
				this.workOrderService.updateStatusByWftaskid(workOrderExcute.getWftaskid(), "已完成");
			}
		}
		
		String resstr="{\"res\":\""+result+"\",\"id\":\""+workOrderExcute.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	/*
	 * 挂起操作
	 * */
	@RequestMapping("/dointerrupt.do")
	public String dointerrupt(HttpServletRequest request,Model model){
		
		
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			String resstr="{\"res\":\"0\"}";
			model.addAttribute("result",resstr);
	        return "result";
		}
		String jsonstr= request.getParameter("jsonstr").toLowerCase();
		JSONArray jaArray=JSONArray.fromObject(jsonstr);
		List<WorkInterrupt> workInterrupts=JSONArray.toList(jaArray, WorkInterrupt.class);
		int result = 1;
		if (workInterrupts!=null && workInterrupts.size()>0) {
			String wftaskid=workInterrupts.get(0).getWftaskid();
			String productid=workInterrupts.get(0).getProductid();
			this.workInterruptService.deleteByWhere(" where wftaskid='"+wftaskid+"' and productid='"+productid+"'");
			for (int i = 0; i < workInterrupts.size(); i++) {
				WorkInterrupt workInterrupt =new WorkInterrupt();
				workInterrupt.setId(CommUtil.getUUID());
				workInterrupt.setInsuser(cu.getId());
				workInterrupt.setInsdt(CommUtil.nowDate());
				workInterrupt.setProductid(productid);
				workInterrupt.setWftaskid(wftaskid);
				workInterrupt.setMaterialid(workInterrupts.get(i).getMaterialid());
				workInterrupt.setAmount(workInterrupts.get(i).getAmount());
				int resultf = this.workInterruptService.save(workInterrupt);
				if (resultf==0) {
					result=0;
				}
			}
			
		}
		
		
		
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	/*
	 * 保存拉料信息
	 * */
	@RequestMapping("/domaterialshort.do")
	public String domaterialshort(HttpServletRequest request,Model model){
		
		
		String j_username= request.getParameter("username");
		String j_password= request.getParameter("pwd");
		User cu = new User();
		if(j_username!=null && j_password!=null){
			cu= loginService.Login(j_username, j_password);
		}
		if(cu==null){
			String resstr="{\"res\":\"0\"}";
			model.addAttribute("result",resstr);
	        return "result";
		}
		String jsonstr= request.getParameter("jsonstr").toLowerCase();
		JSONArray jaArray=JSONArray.fromObject(jsonstr);
		List<MaterialShort> materialShorts=JSONArray.toList(jaArray, MaterialShort.class);
		int result = 1;
		if (materialShorts!=null && materialShorts.size()>0) {
			String wftaskid=materialShorts.get(0).getWftaskid();
			String productid=materialShorts.get(0).getProductid();
			String workstationserial=materialShorts.get(0).getWorkstationserial();
			for (int i = 0; i < materialShorts.size(); i++) {
				MaterialShort materialShort =new MaterialShort();
				materialShort.setId(CommUtil.getUUID());
				materialShort.setInsuser(cu.getId());
				materialShort.setInsdt(CommUtil.nowDate());
				materialShort.setProductid(productid);
				materialShort.setWftaskid(wftaskid);
				materialShort.setMaterialid(materialShorts.get(i).getMaterialid());
				materialShort.setAmount(materialShorts.get(i).getAmount());
				materialShort.setWorkstationserial(workstationserial);
				int resultf = this.materialShortService.save(materialShort);
				if (resultf==0) {
					result=0;
				}
			}
			
		}
		
		
		
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
}
