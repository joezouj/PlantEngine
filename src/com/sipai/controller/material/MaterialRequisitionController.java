package com.sipai.controller.material;

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
import com.sipai.entity.material.MaterialRequisition;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialRequisitionService;
import com.sipai.service.user.UnitService;
import com.sipai.service.user.UserService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/material/materialrequisition")
public class MaterialRequisitionController {
	@Resource
	private MaterialRequisitionService materialRequisitionService;
	@Resource
	private UnitService unitService;
	@Resource
	private UserService userService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialrequisitionList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " R.insdt ";
		}
		if(order==null){
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("R.insuser", "material/materialrequisition/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and U.caption like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<MaterialRequisition> list = this.materialRequisitionService.getMaterialRequisitionList(wherestr+orderstr);
        
        PageInfo<MaterialRequisition> pi = new PageInfo<MaterialRequisition>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		model.addAttribute("nowdate", CommUtil.nowDate());
		return "material/materialrequisitionAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		MaterialRequisition materialrequisition = this.materialRequisitionService.getMaterialRequisitionList(" where R.id='"+Id+"' order by R.insdt").get(0);
		model.addAttribute("materialrequisition", materialrequisition);
		return "material/materialrequisitionEdit";
	}
	@RequestMapping("/editDelivery.do")
	public String doeditDelivery(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");	
		String Id = request.getParameter("id");
		MaterialRequisition materialrequisition = this.materialRequisitionService.getMaterialRequisitionList(" where R.id='"+Id+"' order by R.insdt").get(0);
		model.addAttribute("materialrequisition", materialrequisition);
		model.addAttribute("cuid", cu.getId());
		model.addAttribute("nowdate", CommUtil.nowDate());
		User deliverman=this.userService.getUserById(materialrequisition.getDeliverman());		
		if(deliverman!=null){
			model.addAttribute("delivermanname", deliverman.getCaption());
		}	
		return "material/materialrequisitionEditDelivery";
	}
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		MaterialRequisition materialrequisition = this.materialRequisitionService.getMaterialRequisitionList(" where R.id='"+Id+"' order by R.insdt").get(0);
		model.addAttribute("materialrequisition", materialrequisition);
		User deliverman=this.userService.getUserById(materialrequisition.getDeliverman());		
		if(deliverman!=null){
			model.addAttribute("delivermanname", deliverman.getCaption());
		}		
		return "material/materialrequisitionView";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute MaterialRequisition materialrequisition,
			@RequestParam(value = "requirequantity", required=false) String requirequantitystr){//status:0申请状态1部分发货2发货完毕
		User cu= (User)request.getSession().getAttribute("cu");	
		if(!this.materialRequisitionService.checkNotOccupied(materialrequisition.getId(),materialrequisition.getOrderno())){
			model.addAttribute("result", "{\"res\":\"工位编号重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		materialrequisition.setId(id);
		materialrequisition.setInsuser(cu.getId());
		materialrequisition.setInsdt(CommUtil.nowDate());
		materialrequisition.setApplyuserid(cu.getId());
		if(requirequantitystr!=null &&!requirequantitystr.equals("") &&materialrequisition.getDeliverquantity()!=null &&!materialrequisition.getDeliverquantity().equals("")){
			int requirequantity= Integer.parseInt(requirequantitystr);
			int delivernum= Integer.parseInt(materialrequisition.getDeliverquantity());
			if(delivernum>=requirequantity){
				materialrequisition.setStatus("2");
			}else{
				materialrequisition.setStatus("1");
			}
		}
		int result = this.materialRequisitionService.save(materialrequisition);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute MaterialRequisition materialrequisition,
			@RequestParam(value = "requirequantity", required=false) String requirequantitystr){//status:0申请状态1部分发货2发货完毕
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.materialRequisitionService.checkNotOccupied(materialrequisition.getId(),materialrequisition.getOrderno())){
			model.addAttribute("result", "{\"res\":\"工位编号重复\"}");
			return "result";
		}
		materialrequisition.setModify(cu.getId());
		materialrequisition.setModifydt(CommUtil.nowDate());
		if(requirequantitystr!=null &&!requirequantitystr.equals("") &&materialrequisition.getDeliverquantity()!=null &&!materialrequisition.getDeliverquantity().equals("")){
			int requirequantity= Integer.parseInt(requirequantitystr);
			int delivernum= Integer.parseInt(materialrequisition.getDeliverquantity());
			if(delivernum>=requirequantity){
				materialrequisition.setStatus("2");
			}else{
				materialrequisition.setStatus("1");
			}
		}
		int result = this.materialRequisitionService.update(materialrequisition);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialrequisition.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialRequisitionService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialRequisitionService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
//	@RequestMapping("/showlistForSelect.do")
//	public String showlistForSelect(HttpServletRequest request,Model model) {
//		return "/material/materialrequisitionListForSelect";
//	}
}
