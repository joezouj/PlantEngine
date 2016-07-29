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
import com.sipai.entity.plan.MaterialDeliver;
import com.sipai.entity.plan.MaterialPlan;
import com.sipai.entity.user.User;
import com.sipai.service.plan.DeliverDetailService;
import com.sipai.service.plan.MaterialDeliverService;
import com.sipai.service.plan.MaterialPlanService;
import com.sipai.service.work.WorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/deliverdetail")
public class DeliverDetailController {
	@Resource
	private DeliverDetailService deliverDetailService;
	@Resource
	private MaterialDeliverService materialDeliverService;
	@Resource
	private WorkstationService workstationService;
	@Resource
	private MaterialPlanService materialPlanService;

	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		String id = request.getParameter("pid");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		if(materialDeliver!=null){
			model.addAttribute("workstationid", materialDeliver.getWorkstationid());
			model.addAttribute("planid", materialDeliver.getDailyplanid());
		}
		return "/plan/deliverDetailList";
	}
	
	@RequestMapping("/showviewlist.do")
	public String showviewlist(HttpServletRequest request,Model model) {
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		String id = request.getParameter("pid");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		if(materialDeliver!=null){
			model.addAttribute("workstationid", materialDeliver.getWorkstationid());
			model.addAttribute("planid", materialDeliver.getDailyplanid());
		}
		return "/plan/deliverDetailViewList";
	}
	
	@RequestMapping("/showdeliverlist.do")
	public String showdeliverlist(HttpServletRequest request,Model model) {
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		String id = request.getParameter("pid");
		MaterialDeliver materialDeliver = this.materialDeliverService.selectById(id);
		if(materialDeliver!=null){
			model.addAttribute("workstationid", materialDeliver.getWorkstationid());
			model.addAttribute("planid", materialDeliver.getDailyplanid());
		}
		return "/plan/deliverList";
	}
	
	@RequestMapping("/getDeliverDetails.do")
	public ModelAndView getDeliverDetails(HttpServletRequest request,Model model) {
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
			sort = " d.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = " where 1=1 ";
		if(request.getParameter("search_materialname")!=null && !request.getParameter("search_materialname").isEmpty()){
			wherestr += " and m.materialname like '%"+request.getParameter("search_materialname")+"%'";
		}
		if(request.getParameter("search_materialcode")!=null && !request.getParameter("search_materialcode").isEmpty()){
			wherestr += " and m.materialcode like '%"+request.getParameter("search_materialcode")+"%'";
		}
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and d.pid = '"+request.getParameter("pid")+"'";
			model.addAttribute("pid",request.getParameter("pid"));
		}
		PageHelper.startPage(pages, pagesize);
        List<DeliverDetail> list = this.deliverDetailService.getDeliverDetail(wherestr+orderstr);
        
        PageInfo<DeliverDetail> page = new PageInfo<DeliverDetail>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		if(request.getParameter("planid")!=null && !request.getParameter("planid").isEmpty()){
			model.addAttribute("planid",request.getParameter("planid"));
		}
		if(request.getParameter("workstationid")!=null && !request.getParameter("workstationid").isEmpty()){
			model.addAttribute("workstationid",request.getParameter("workstationid"));
		}
		return "/plan/deliverDetailAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("deliverDetail") DeliverDetail deliverDetail){
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		deliverDetail.setId(id);
		deliverDetail.setDeliverst(0);
		deliverDetail.setInsuser(cu.getId());
		deliverDetail.setInsdt(CommUtil.nowDate());
		int result = this.deliverDetailService.save(deliverDetail);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	

	@RequestMapping("/deliver.do")
	public String dodeliver(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DeliverDetail deliverDetail = this.deliverDetailService.selectById(id);
		model.addAttribute("deliverDetail", deliverDetail);
		return "/plan/deliverDetail";
	}

	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DeliverDetail deliverDetail = this.deliverDetailService.selectById(id);
		if(deliverDetail!=null&&deliverDetail.getPid()!=null){
			String mdid = deliverDetail.getPid();
			MaterialDeliver materialDeliver = this.materialDeliverService.selectById(mdid);
			if(materialDeliver!=null){
				model.addAttribute("pid", materialDeliver.getId());
				model.addAttribute("planid", materialDeliver.getDailyplanid());
				model.addAttribute("workstationid", materialDeliver.getWorkstationid());
				//获取统计信息
				//总量
				String wherestr = " where 1=1 and  workstationid is not null "
						+" and id= '"+materialDeliver.getDailyplanid()+"'"
						+" and workstationid = '"+materialDeliver.getWorkstationid()+"'"
						+" and materialid = '"+deliverDetail.getMaterialid()+"'";
				String groupstr = " group by id,name,plandt,workstationid, workstationname,workstationserial, materialid, materialcode, materialname,amount ,unit";
		        String orderstr = " order by materialid ";
				List<MaterialPlan> list = this.materialPlanService.getStatisticListByWhere(wherestr+groupstr+orderstr);
				Double totalplanamount = 0.0;
				String unit = "";
				if(list.size()>0){
					MaterialPlan mplan = list.get(0);
					totalplanamount = mplan.getAmount()==null?0.0:mplan.getAmount();
					unit = mplan.getUnit()==null?"":mplan.getUnit();
				}
				//已安排
				DeliverDetail undeliver = this.deliverDetailService.getPlanedAmount(materialDeliver.getDailyplanid(),deliverDetail.getMaterialid());
				Double planamount = undeliver.getPlanamount()==null?0.0:undeliver.getPlanamount();
				Double undeliveramount = totalplanamount-planamount;
				String undeliverInfo = "尚未分配物料数量："+undeliveramount+unit;
				model.addAttribute("undeliveramount", undeliveramount);
				model.addAttribute("undeliverInfo", undeliverInfo);
			}
		}
		model.addAttribute("deliverDetail", deliverDetail);
		return "/plan/deliverDetailEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("deliverDetail") DeliverDetail deliverDetail){
		User cu= (User)request.getSession().getAttribute("cu");
		deliverDetail.setModify(cu.getId());
		deliverDetail.setModifydt(CommUtil.nowDate());
		//修改单条配送状态
		Double pamount = deliverDetail.getPlanamount()==null?0.0:deliverDetail.getPlanamount();
		Double damount = deliverDetail.getDeliveramount()==null?0.0:deliverDetail.getDeliveramount();
		if(damount>=pamount){
			deliverDetail.setDeliverst(1);
		}else{
			deliverDetail.setDeliverst(0);
		}
		int result = this.deliverDetailService.update(deliverDetail);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+deliverDetail.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	/**
	 * 配料员配料时更新使用，同步更新任务状态
	 * 
	 * */
	@RequestMapping("/deliverupdate.do")
	public String deliverupdate(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");
		String id = request.getParameter("id");
		String deliveramount = request.getParameter("deliveramount");
		Double damount = Double.valueOf(deliveramount);
		String boxnumber = request.getParameter("boxnumber");
		String boxname = request.getParameter("boxname");
		String resstr= "";
		int result = 0;
		if(id!=null&&!id.isEmpty()){
			DeliverDetail deliverDetail = this.deliverDetailService.selectById(id);
			deliverDetail.setModify(cu.getId());
			deliverDetail.setModifydt(CommUtil.nowDate());
			deliverDetail.setDeliveramount(damount);
			deliverDetail.setBoxnumber(boxnumber);
			deliverDetail.setBoxname(boxname);
			//修改单条数配送状态
			Double pamount = deliverDetail.getPlanamount()==null?0.0:deliverDetail.getPlanamount();
			
			if(damount>=pamount){
				deliverDetail.setDeliverst(1);
			}else{
				deliverDetail.setDeliverst(0);
			}
			result = this.deliverDetailService.update(deliverDetail);
			//联动更新配总任务主表状态
			if(result==1){
				String status = this.deliverDetailService.getMaterialDeliverStatus(deliverDetail.getPid());
				if(status!=""){
					MaterialDeliver materialDeliver = materialDeliverService.selectById(deliverDetail.getPid());
					if(materialDeliver!=null&&materialDeliver.getId()!=null){
						materialDeliver.setStatus(status);
						materialDeliverService.update(materialDeliver);
					}
				}
			}
		}
		resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DeliverDetail deliverDetail = this.deliverDetailService.selectById(id);
		model.addAttribute("deliverDetail", deliverDetail);
		return "/plan/deliverDetailView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.deliverDetailService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.deliverDetailService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getPlanedAmount.do")
	public String getPlanedAmount(HttpServletRequest request,Model model,
			@RequestParam(value="planid") String planid,
			@RequestParam(value="materialid") String materialid){
		DeliverDetail deliverDetail = this.deliverDetailService.getPlanedAmount(planid,materialid);
		JSONArray json=JSONArray.fromObject(deliverDetail);
		String resstr="{\"deliverDetail\":"+json+"}";
		model.addAttribute("result", resstr);
		return "result";
	}
}
