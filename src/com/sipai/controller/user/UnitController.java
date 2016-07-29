package com.sipai.controller.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.base.Tree;
import com.sipai.entity.user.Company;
import com.sipai.entity.user.Dept;
import com.sipai.entity.user.Unit;
import com.sipai.service.user.UnitService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/user")
public class UnitController {
	@Resource
	private UnitService unitService;
	
	@RequestMapping("/showUnitTree.do")
	public String showUnitTree(HttpServletRequest request,Model model){
		return "user/unitTree";
	}
	
	@RequestMapping("/getUnitsJson.do")
	public String getUnitsJson(HttpServletRequest request,Model model){
		List<Unit> list = this.unitService.selectList();
		
		List<Tree> tree = new ArrayList<Tree>();
		for (Unit resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			
			node.setText(this.unitService.getSnameById(resource.getId()));
			
			if(!resource.getId().equals("-1")){
				if(resource.getType().equals("D")){
					node.setIconCls("ext-icon-group");
				}else if(resource.getType().equals("C")){
					node.setIconCls("ext-icon-house");
				}
			}
			
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("type", resource.getType());
			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
//		System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/showCompanyAdd.do")
	public String showCompanyAdd(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String pid){
		model.addAttribute("pname",this.unitService.getSnameById(pid));
		return "user/companyAdd";
	}
	
	@RequestMapping("/showCompanyEdit.do")
	public String showCompanyEdit(HttpServletRequest request,Model model,
			@RequestParam String id){
		Company company = unitService.getCompById(id);
		model.addAttribute("company",company );
		model.addAttribute("pname",this.unitService.getSnameById(company.getPid()));
		return "user/companyEdit";
	}
	
	@RequestMapping("/saveCompany.do")
	public ModelAndView doCompanySave(HttpServletRequest request,Model model, 
			@ModelAttribute Company t){
		t.setId(CommUtil.getUUID());
		int result = this.unitService.saveComp(t);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}

	@RequestMapping("/updateCompany.do")
	public ModelAndView doCompanyUpdate(HttpServletRequest request,Model model,
			@ModelAttribute Company t){
		int result = this.unitService.updateComp(t);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/deleteCompany.do")
	public ModelAndView doCompanyDelete(HttpServletRequest request,Model model, 
			@RequestParam String id){
		int result = this.unitService.deleteCompById(id);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/showDeptAdd.do")
	public String showDeptAdd(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String pid){
		model.addAttribute("pname",this.unitService.getSnameById(pid));
		return "user/deptAdd";
	}
	
	@RequestMapping("/showDeptEdit.do")
	public String showDeptEdit(HttpServletRequest request,Model model,
			@RequestParam String id){
		Dept dept = unitService.getDeptById(id);
		model.addAttribute("dept",dept );
		model.addAttribute("pname",this.unitService.getSnameById(dept.getPid()));
		return "user/deptEdit";
	}
	
	@RequestMapping("/saveDept.do")
	public ModelAndView doDeptSave(HttpServletRequest request,Model model, 
			@ModelAttribute Dept t){
		t.setId(CommUtil.getUUID());
		int result = this.unitService.saveDept(t);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}

	@RequestMapping("/updateDept.do")
	public ModelAndView doDeptUpdate(HttpServletRequest request,Model model,
			@ModelAttribute Dept t){
		int result = this.unitService.updateDept(t);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/deleteDept.do")
	public ModelAndView doDeptDelete(HttpServletRequest request,Model model, 
			@RequestParam String id){
		int result = this.unitService.deleteDeptById(id);
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
	
	/**
	 * 保存顺序
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/saveUnitOrder.do")
	public ModelAndView saveUnitOrder(HttpServletRequest request,Model model, 
			@RequestParam String target,@RequestParam String source,@RequestParam String point){
		Unit unitSource = this.unitService.getUnitById(source);
		Unit unitTarget = this.unitService.getUnitById(target);
		
		switch (point){
			case "append":
				unitSource.setPid(unitTarget.getId());
				if(unitTarget.getMorder()==null){
					unitSource.setMorder(0);
				}else{
					unitSource.setMorder(unitTarget.getMorder()+1);
				}
				break;
			case "top":
				unitSource.setPid(unitTarget.getPid());
				if(unitTarget.getMorder()==null){
					unitSource.setMorder(0);
				}else{
					unitSource.setMorder(unitTarget.getMorder()-1);
				}
				break;
			case "bottom":
				unitSource.setPid(unitTarget.getPid());
				if(unitTarget.getMorder()==null){
					unitSource.setMorder(0);
				}else{
					unitSource.setMorder(unitTarget.getMorder()+1);
				}
				break;
		}
		
		int result = 0;
		switch (unitSource.getType()){
			case "C":
				Company compSource = new Company();
				compSource.setId(unitSource.getId());
				compSource.setPid(unitSource.getPid());
				compSource.setMorder(unitSource.getMorder());
				
				result = this.unitService.updateComp(compSource);
				break;
			case "D":
				Dept deptSource = new Dept();
				deptSource.setId(unitSource.getId());
				deptSource.setPid(unitSource.getPid());
				deptSource.setMorder(unitSource.getMorder());
				
				result = this.unitService.updateDept(deptSource);
				break;
		}
		
		model.addAttribute("result",result);
		return new ModelAndView("result");
	}
}
