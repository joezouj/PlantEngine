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
import com.sipai.entity.material.CutterPosition;
import com.sipai.entity.user.User;
import com.sipai.service.material.CutterPositionService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/cutterposition")
public class CutterPositionController {
	@Resource
	private CutterPositionService cutterPositionService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/cutterPositionList";
	}
	
	@RequestMapping("/getCutterPositions.do")
	public ModelAndView getCutterPositions(HttpServletRequest request,Model model) {
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
			sort = " name ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "material/CutterPosition/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		wherestr +=" and id!='-1' ";
		PageHelper.startPage(pages, pagesize);
        List<CutterPosition> list = this.cutterPositionService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<CutterPosition> page = new PageInfo<CutterPosition>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonPosition.do")
	public ModelAndView getJsonPosition(HttpServletRequest request,Model model) {		
		String wherestr=" where 1=1 and status='1' ";
		String orderstr=" order by name asc ";
        List<CutterPosition> list = this.cutterPositionService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/cutterPositionAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("cutterPosition") CutterPosition cutterPosition){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.cutterPositionService.checkNotOccupied(cutterPosition.getId(),cutterPosition.getName())){
			model.addAttribute("result", "{\"res\":\"位置信息重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		cutterPosition.setId(id);
		cutterPosition.setInsuser(cu.getId());
		cutterPosition.setInsdt(CommUtil.nowDate());
		cutterPosition.setModify(cu.getId());
		cutterPosition.setModifydt(CommUtil.nowDate());
		
		int result = this.cutterPositionService.save(cutterPosition);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterPosition cutterPosition = this.cutterPositionService.selectById(id);
		model.addAttribute("cutterPosition", cutterPosition);
		return "/material/cutterPositionEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("cutterPosition") CutterPosition cutterPosition){
		if(!this.cutterPositionService.checkNotOccupied(cutterPosition.getId(),cutterPosition.getName())){
			model.addAttribute("result", "{\"res\":\"位置信息重复\"}");
			return "result";
		}
		int result = this.cutterPositionService.update(cutterPosition);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+cutterPosition.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterPosition cutterPosition = this.cutterPositionService.selectById(id);
		model.addAttribute("cutterPosition", cutterPosition);
		if(cutterPosition!=null){
			CutterPosition parentPosition = this.cutterPositionService.selectById(cutterPosition.getParentid());
			model.addAttribute("parentPosition", parentPosition);
		}
		return "/material/cutterPositionView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.cutterPositionService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.cutterPositionService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/getMenusJsonActive.do")
	public String getMenusJsonActive(HttpServletRequest request,Model model){
		List<CutterPosition> list = this.cutterPositionService.selectListByWhere("where 1=1 order by insdt");
		
		List<Tree> tree = new ArrayList<Tree>();
		//增加根节点
		Tree root = new Tree();
		root.setText("根节点");
		root.setId("-1");
		root.setPid("-1");
		tree.add(root);
		for (CutterPosition resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);
			node.setText(resource.getName());
			node.setPid(resource.getParentid());
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		//System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}
}
