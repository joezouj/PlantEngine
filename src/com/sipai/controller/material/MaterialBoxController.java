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
import com.sipai.entity.material.MaterialBox;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialBoxService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/materialbox")
public class MaterialBoxController {
	@Resource
	private MaterialBoxService materialBoxService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialBoxList";
	}
	@RequestMapping("/selectMaterialBox.do")
	/**料盒单选*/
	public String selectMaterialBox(HttpServletRequest request,Model model) {
		return "/material/materialBoxForSelect";
	}
	@RequestMapping("/getMaterialBoxes.do")
	public ModelAndView getMaterialBoxes(HttpServletRequest request,Model model) {
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
			sort = " boxnumber ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "material/materialbox/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 and status='0' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and name like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and boxnumber like '%"+request.getParameter("search_code")+"%' ";
		}
		
		PageHelper.startPage(pages, pagesize);
        List<MaterialBox> list = this.materialBoxService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<MaterialBox> page = new PageInfo<MaterialBox>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getJsonBoxes.do")
	public ModelAndView getJsonBoxes(HttpServletRequest request,Model model) {		
		String wherestr=" where 1=1 and status='1' ";
		String orderstr=" order by boxnumber asc ";
        List<MaterialBox> list = this.materialBoxService.selectListByWhere(wherestr+orderstr);
        
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/materialBoxAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialBox") MaterialBox materialBox){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.materialBoxService.checkNotOccupied(materialBox.getId(),materialBox.getBoxnumber())){
			model.addAttribute("result", "{\"res\":\"料盒编号重复\"}");
			return "result";
		}
		
		String id = CommUtil.getUUID();
		materialBox.setId(id);
		materialBox.setInsuser(cu.getId());
		materialBox.setInsdt(CommUtil.nowDate());
		materialBox.setModify(cu.getId());
		materialBox.setModifydt(CommUtil.nowDate());
		
		int result = this.materialBoxService.save(materialBox);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialBox materialBox = this.materialBoxService.selectById(id);
		model.addAttribute("materialBox", materialBox);
		return "/material/materialBoxEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialBox") MaterialBox materialBox){
		if(!this.materialBoxService.checkNotOccupied(materialBox.getId(),materialBox.getBoxnumber())){
			model.addAttribute("result", "{\"res\":\"料盒编号重复\"}");
			return "result";
		}
		
		int result = this.materialBoxService.update(materialBox);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialBox.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialBox materialBox = this.materialBoxService.selectById(id);
		model.addAttribute("materialBox", materialBox);
		return "/material/materialBoxView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialBoxService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialBoxService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
