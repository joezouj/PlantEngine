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
import com.sipai.entity.material.CutterType;
import com.sipai.entity.user.User;
import com.sipai.service.material.CutterTypeService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/cuttertype")
public class CutterTypeController {
	@Resource
	private CutterTypeService cutterTypeService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/cutterTypeList";
	}
	
	@RequestMapping("/getCutterTypes.do")
	public ModelAndView getCutterTypes(HttpServletRequest request,Model model) {
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
		
		String wherestr = CommUtil.getwherestr("insuser", "material/cuttertype/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and typename like '%"+request.getParameter("search_name")+"%' ";
		}
		wherestr +=" and id!='-1' ";
		PageHelper.startPage(pages, pagesize);
        List<CutterType> list = this.cutterTypeService.selectListByWhere(wherestr+orderstr);
        PageInfo<CutterType> page = new PageInfo<CutterType>(list);
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
        List<CutterType> list = this.cutterTypeService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/cutterTypeAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("cutterType") CutterType cutterType){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.cutterTypeService.checkNotOccupied(cutterType.getId(),cutterType.getTypename())){
			model.addAttribute("result", "{\"res\":\"刀具类型重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		cutterType.setId(id);
		cutterType.setInsuser(cu.getId());
		cutterType.setInsdt(CommUtil.nowDate());
		cutterType.setModify(cu.getId());
		cutterType.setModifydt(CommUtil.nowDate());
		
		int result = this.cutterTypeService.save(cutterType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterType cutterType = this.cutterTypeService.selectById(id);
		model.addAttribute("cutterType", cutterType);
		return "/material/cutterTypeEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("cutterType") CutterType cutterType){
		if(!this.cutterTypeService.checkNotOccupied(cutterType.getId(),cutterType.getTypename())){
			model.addAttribute("result", "{\"res\":\"刀具类型重复\"}");
			return "result";
		}
		int result = this.cutterTypeService.update(cutterType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+cutterType.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterType cutterType = this.cutterTypeService.selectById(id);
		model.addAttribute("cutterType", cutterType);
		if(cutterType!=null){
			System.out.println(cutterType.getParentid());
			CutterType parentType = this.cutterTypeService.selectById(cutterType.getParentid());
			model.addAttribute("parentType", parentType);
		}
		return "/material/cutterTypeView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.cutterTypeService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.cutterTypeService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/getMenusJsonActive.do")
	public String getMenusJsonActive(HttpServletRequest request,Model model){
		List<CutterType> list = this.cutterTypeService.selectListByWhere("where 1=1 order by insdt");
		
		List<Tree> tree = new ArrayList<Tree>();
		//增加根节点
		Tree root = new Tree();
		root.setText("根节点");
		root.setId("-1");
		root.setPid("-1");
		tree.add(root);
		for (CutterType resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			node.setText(resource.getTypename());
			node.setPid(resource.getParentid());
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		//System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
}
