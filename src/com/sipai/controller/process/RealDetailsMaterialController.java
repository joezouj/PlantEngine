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
import com.sipai.entity.process.Real;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.process.TaskMaterial;
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsMaterialService;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealService;
import com.sipai.service.process.TaskMaterialService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetailsMaterial")
public class RealDetailsMaterialController {
	@Resource
	private RealService realService;
	@Resource
	private RealDetailsMaterial realDetailsMaterial;
	@Resource
	private RealDetailsService realDetailsService;
	@Resource
	private RealDetailsMaterialService realDetailsMaterialService;
	@Resource
	private TaskMaterialService taskMaterialService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsMaterialList";
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
        List<RealDetailsMaterial> list = this.realDetailsMaterialService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetailsMaterial> pi = new PageInfo<RealDetailsMaterial>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		String detailsId = request.getParameter("pid");
		List<Real> realList = realService.getListByDetailsID(detailsId);
		if(realList!=null&&realList.size()>0){
			model.addAttribute("productcode", realList.get(0).getProduct().getMaterialcode());
		}
		return "process/realDetailsMaterialAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		
		realDetailsMaterial = this.realDetailsMaterialService.selectById(id);
		
		String detailsId = realDetailsMaterial.getPid();
		List<Real> realList = realService.getListByDetailsID(detailsId);
		if(realList!=null&&realList.size()>0){
			model.addAttribute("productcode", realList.get(0).getProduct().getMaterialcode());
		}
		model.addAttribute("realDetailsMaterial", realDetailsMaterial);
		return "process/realDetailsMaterialEdit";
	}
	
//	@RequestMapping("/save.do")
//	public String dosave(HttpServletRequest request,Model model,
//			@ModelAttribute RealDetailsMaterial realDetailsMaterial){
//		User cu= (User)request.getSession().getAttribute("cu");
//		
//		String id = CommUtil.getUUID();
//		realDetailsMaterial.setId(id);
//		realDetailsMaterial.setInsuser(cu.getId());
//		realDetailsMaterial.setInsdt(CommUtil.nowDate());
//		
//		int result = this.realDetailsMaterialService.save(realDetailsMaterial);
//		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
//		model.addAttribute("result", resstr);
//		return "result";
//	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsMaterial realDetailsMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		//清除所有旧数据
		this.realDetailsMaterialService.deleteByWhere("where pid in ('"+request.getParameter("pid")+"')");
		
		String resultstr="";
		int result = 0;
		int suc = 0;
		int fal = 0;
		if(!request.getParameter("mids").equals("")){
			String mids[]=request.getParameter("mids").split(",");
			if(mids.length>0){
				for(int i=0;i<mids.length;i++){
					String id = CommUtil.getUUID();
					realDetailsMaterial.setId(id);
					realDetailsMaterial.setPid(request.getParameter("pid"));
					realDetailsMaterial.setMaterialid(mids[i]);
					realDetailsMaterial.setAmount(1.00);
					realDetailsMaterial.setInsuser(cu.getId());
					realDetailsMaterial.setInsdt(CommUtil.nowDate());
					
					result = this.realDetailsMaterialService.save(realDetailsMaterial);
					if(result==1){
						suc++;
					}else{
						fal++;
					}
				}
			}
		}
		
		if(request.getParameter("mids").equals("")){
			resultstr += "已清除所有数据！";
		}
		if(suc>0){
			resultstr += "成功添加"+suc+"条数据！";
		}
		if(fal>0){
			resultstr += fal+"条数据添加失败！";
		}
		model.addAttribute("result", resultstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsMaterial realDetailsMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetailsMaterial.setUpduser(cu.getId());
		realDetailsMaterial.setUpddt(CommUtil.nowDate());
		
		int result = this.realDetailsMaterialService.update(realDetailsMaterial);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetailsMaterial.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/updateamount.do")
	public String updateamount(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsMaterial realDetailsMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		String resultstr="";
		int result = 0;
		int suc = 0;
		int fal = 0;
		if(!request.getParameter("ids").equals("")){
			String ids[]=request.getParameter("ids").split(",");
			String amounts[] = request.getParameter("amounts").split(",");
			if(ids.length>0){
				for(int i=0;i<ids.length;i++){
					realDetailsMaterial = this.realDetailsMaterialService.selectById(ids[i]);
					if(amounts[i]!=null&&amounts[i]!=""){
						Double amount = Double.valueOf(amounts[i]);
						realDetailsMaterial.setAmount(amount);
					}else{
						realDetailsMaterial.setAmount(0.00);
					}
					realDetailsMaterial.setUpduser(cu.getId());
					realDetailsMaterial.setUpddt(CommUtil.nowDate());
					
					result = this.realDetailsMaterialService.update(realDetailsMaterial);
					if(result==1){
						suc++;
					}else{
						fal++;
					}
				}
			}
		}
		if(suc>0){
			resultstr += "成功保存"+suc+"条数据！";
		}
		if(fal>0){
			resultstr += fal+"条数据保存失败！";
		}
		model.addAttribute("result", resultstr);
		return "result";
	}
	
	@RequestMapping("/saveamount.do")
	public String saveamount(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsMaterial realDetailsMaterial){
		User cu= (User)request.getSession().getAttribute("cu");
		String resultstr="";
		int result = 0;
		if(!request.getParameter("id").equals("")){
			String id=request.getParameter("id");
			String amountStr = request.getParameter("amount");
			realDetailsMaterial = this.realDetailsMaterialService.selectById(id);
			if(amountStr!=null&&amountStr!=""){
				Double amount = Double.valueOf(amountStr);
				realDetailsMaterial.setAmount(amount);
			}else{
				realDetailsMaterial.setAmount(0.00);
			}
			realDetailsMaterial.setUpduser(cu.getId());
			realDetailsMaterial.setUpddt(CommUtil.nowDate());
			
			result = this.realDetailsMaterialService.update(realDetailsMaterial);
		}
		if(result>0){
			resultstr += "保存成功！";
		}else{
			resultstr += "保存失败！";
		}
		model.addAttribute("result", resultstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.realDetailsMaterialService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.realDetailsMaterialService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getTaskMaterial.do")
	public ModelAndView getTaskMaterial(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		
		if(sort==null){sort = " materialcode ";}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and tb_process_real_details.id='"+request.getParameter("pid")+"'";
			RealDetails realDetail= this.realDetailsService.selectById(request.getParameter("pid"));
			wherestr += " and tb_process_task_material.taskname = '"+realDetail.getTaskname()+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and tb_material_info.materialname like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += " and tb_material_info.materialcode like '%"+request.getParameter("search_code")+"%' ";
		}
		PageHelper.startPage(page, rows);
        List<TaskMaterial> list = this.taskMaterialService.selectListByWhere1(wherestr+orderstr);
        PageInfo<TaskMaterial> pi = new PageInfo<TaskMaterial>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getchecklist.do")
	public ModelAndView getchecklist(HttpServletRequest request,Model model) {		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}

        List<RealDetailsMaterial> list = this.realDetailsMaterialService.selectListByWhere(wherestr);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
