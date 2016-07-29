package com.sipai.controller.material;

import java.util.ArrayList;
import java.util.List;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.base.Tree;
import com.sipai.entity.material.MaterialType;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialTypeService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/materialtype")
public class MaterialTypeController {
	@Resource
	private MaterialTypeService materialTypeService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialTypeList";
	}
	
	@RequestMapping("/getMaterialTypes.do")
	public ModelAndView getMaterialInfos(HttpServletRequest request,Model model) {
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
			sort = " typename ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "material/materialtype/getMaterialTypes.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and typename like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and typecode like '%"+request.getParameter("search_code")+"%' ";
		}
		wherestr +=" and id!='-1' ";
		PageHelper.startPage(pages, pagesize);
        List<MaterialType> list = this.materialTypeService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<MaterialType> page = new PageInfo<MaterialType>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonType.do")
	public ModelAndView getJsonType(HttpServletRequest request,Model model) {		
		String wherestr=" where 1=1 and status='1' ";
		String orderstr=" order by typename asc ";
        List<MaterialType> list = this.materialTypeService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/materialTypeAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialType") MaterialType materialType){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.materialTypeService.checkNotOccupied(materialType.getId(),materialType.getTypename())){
			model.addAttribute("result", "{\"res\":\"物料类型重复\"}");
			return "result";
		}
		if(!this.materialTypeService.checkCodeNotOccupied(materialType.getId(),materialType.getTypecode())){
			model.addAttribute("result", "{\"res\":\"类型代码重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		materialType.setId(id);
		materialType.setInsuser(cu.getId());
		materialType.setInsdt(CommUtil.nowDate());
		materialType.setModify(cu.getId());
		materialType.setModifydt(CommUtil.nowDate());
		
		int result = this.materialTypeService.save(materialType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialType materialType = this.materialTypeService.selectById(id);
		model.addAttribute("materialType", materialType);
		return "/material/materialTypeEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialType") MaterialType materialType){
		if(!this.materialTypeService.checkNotOccupied(materialType.getId(),materialType.getTypename())){
			model.addAttribute("result", "{\"res\":\"物料类型重复\"}");
			return "result";
		}
		if(!this.materialTypeService.checkCodeNotOccupied(materialType.getId(),materialType.getTypecode())){
			model.addAttribute("result", "{\"res\":\"类型代码重复\"}");
			return "result";
		}
		int result = this.materialTypeService.update(materialType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialType.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialType materialType = this.materialTypeService.selectById(id);
		model.addAttribute("materialType", materialType);
		if(materialType!=null){
			MaterialType parentType = this.materialTypeService.selectById(materialType.getParentid());
			model.addAttribute("parentType", parentType);
		}
		return "/material/materialTypeView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialTypeService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialTypeService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/getMenusJsonActive.do")
	public String getMenusJsonActive(HttpServletRequest request,Model model){
		List<MaterialType> list = this.materialTypeService.selectListByWhere("where 1=1 order by insdt");
		
		List<Tree> tree = new ArrayList<Tree>();
		for (MaterialType resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			
			node.setText(resource.getTypename());
			node.setPid(resource.getParentid());
			node.setAttributes("{\"typecode\":\""+resource.getTypecode()+"\"}");
//			Map<String, String> attributes = new HashMap<String, String>();
//			attributes.put("url", resource.getLocation());
//			attributes.put("target", resource.getTarget());
//			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		//System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
}
