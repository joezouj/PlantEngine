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
import com.sipai.entity.plan.DailyPlanSummaryMaterial;
import com.sipai.entity.user.User;
import com.sipai.service.plan.DailyPlanSummaryMaterialService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/dailyplansummarymaterial")
public class DailyPlanSummaryMaterialController {
	@Resource
	private DailyPlanSummaryMaterialService dailyPlanSummaryMaterialService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/plan/dailyPlanSummaryMaterialList";
	}
	@RequestMapping("/getDailyPlanSummaryMaterials.do")
	public ModelAndView getDailyPlanSummaryMaterials(HttpServletRequest request,Model model) {
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
			sort = " recovertime ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = " where 1=1 ";
//		if(request.getParameter("search_workstationserial")!=null && !request.getParameter("search_workstationserial").isEmpty()){
//			wherestr += " and w.serial like '%"+request.getParameter("search_workstationserial")+"%'";
//		}
//		if(request.getParameter("search_materialcode")!=null && !request.getParameter("search_materialcode").isEmpty()){
//			wherestr += " and m.materialcode like '%"+request.getParameter("search_materialcode")+"%'";
//		}
		PageHelper.startPage(pages, pagesize);
        List<DailyPlanSummaryMaterial> list = this.dailyPlanSummaryMaterialService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<DailyPlanSummaryMaterial> page = new PageInfo<DailyPlanSummaryMaterial>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/plan/dailyPlanSummaryMaterialAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");
		String resstr="";
		String resultstr="";
		int suc = 0;
		int fal = 0;
		if(request.getParameter("dailyplansummaryid")!=null&&!request.getParameter("dailyplansummaryid").equals("")){
			if(request.getParameter("materialid")!=null&&!request.getParameter("materialid").equals("")){
				if(request.getParameter("workstationid")!=null&&!request.getParameter("workstationid").equals("")){
					if(request.getParameter("amount")!=null&&!request.getParameter("amount").equals("")){
						String materialids[]=request.getParameter("materialid").split(",");
						String workstationids[] = request.getParameter("workstationid").split(",");
						String amounts[] = request.getParameter("amount").split(",");
						String remarks[] = request.getParameter("remark").split(",");
						for(int i=0;i<materialids.length;i++){
							DailyPlanSummaryMaterial dailyPlanSummaryMaterial=new DailyPlanSummaryMaterial();
							String id = CommUtil.getUUID();
							dailyPlanSummaryMaterial.setId(id);
							dailyPlanSummaryMaterial.setInsuser(cu.getId());
							dailyPlanSummaryMaterial.setInsdt(CommUtil.nowDate());
							dailyPlanSummaryMaterial.setPlanid(request.getParameter("dailyplansummaryid"));
							dailyPlanSummaryMaterial.setMaterialid(materialids[i]);						
							dailyPlanSummaryMaterial.setWorkstationid(workstationids[i]);
							dailyPlanSummaryMaterial.setAmount(Double.valueOf(amounts[i]));
							if(!remarks[i].equals(" ")){
								dailyPlanSummaryMaterial.setRemark(remarks[i]);
							}							
							int result = this.dailyPlanSummaryMaterialService.save(dailyPlanSummaryMaterial);
							if(result==1){
								suc++;
							}else{
								fal++;
							}
						}
						if(suc>0){
							resultstr += "成功保存"+suc+"条数据！";
						}
						if(fal>0){
							resultstr += fal+"条数据保存失败！";
						}
						resstr="{\"res\":\""+resultstr+"\"}";
					}else{
						resstr="{\"res\":\"总计划量为空\"}";
					}
				}else{
					resstr="{\"res\":\"工位id为空\"}";
				}
			}else{
				resstr="{\"res\":\"物料id为空\"}";
			}
		}else{
			resstr="{\"res\":\"计划单id为空\"}";
		}		
		model.addAttribute("result", resstr);
		return "result";
	}
	

	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DailyPlanSummaryMaterial dailyPlanSummaryMaterial = this.dailyPlanSummaryMaterialService.selectById(id);
		model.addAttribute("dailyPlanSummaryMaterial", dailyPlanSummaryMaterial);
		return "/plan/dailyPlanSummaryMaterialEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("dailyPlanSummaryMaterial") DailyPlanSummaryMaterial dailyPlanSummaryMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		dailyPlanSummaryMaterial.setModify(cu.getId());
		dailyPlanSummaryMaterial.setModifydt(CommUtil.nowDate());
		int result = this.dailyPlanSummaryMaterialService.update(dailyPlanSummaryMaterial);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+dailyPlanSummaryMaterial.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DailyPlanSummaryMaterial dailyPlanSummaryMaterial = this.dailyPlanSummaryMaterialService.selectById(id);
		model.addAttribute("dailyPlanSummaryMaterial", dailyPlanSummaryMaterial);
		return "/plan/dailyPlanSummaryMaterialView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.dailyPlanSummaryMaterialService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.dailyPlanSummaryMaterialService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}

}
