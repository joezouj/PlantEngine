package com.sipai.controller.work;

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
import com.sipai.entity.user.User;
import com.sipai.entity.work.GroupType;
import com.sipai.service.work.GroupTypeService;
import com.sipai.service.work.WorkSchedulingService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/grouptype")
public class GroupTypeController {
	@Resource
	private GroupTypeService groupTypeService;
	@Resource
	private WorkSchedulingService workSchedulingService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/grouptypeList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " insdt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/grouptype/showlist.do", cu);
		if(request.getParameter("wdt")!=null && !request.getParameter("wdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("wdt")+"' ";
		}
		
		PageHelper.startPage(page, rows);
        List<GroupType> list = this.groupTypeService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<GroupType> pi = new PageInfo<GroupType>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlistForSelect.do")
	public ModelAndView getlistForSelect(HttpServletRequest request,Model model,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " insdt ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/grouptype/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("wdt")!=null && !request.getParameter("wdt").isEmpty()){
			wherestr += " and eddt >= '"+request.getParameter("wdt")+"' ";
		}
		
        List<GroupType> list = this.groupTypeService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<GroupType> pi = new PageInfo<GroupType>(list);
		JSONArray json=JSONArray.fromObject(list);
		
//		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/showlistforselect.do")
	public String showlistforselect(HttpServletRequest request,Model model) {
		return "/work/grouptypeListForSelect";
	}
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/grouptypeAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		GroupType groupType = this.groupTypeService.selectById(Id);
		model.addAttribute("groupType", groupType);
		return "work/grouptypeEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute GroupType groupType){
		if(!this.groupTypeService.checkNotOccupied(groupType.getId(), groupType.getName())){
			model.addAttribute("result", "{\"res\":\"班次名称重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		groupType.setId(id);
		groupType.setInsdt(CommUtil.nowDate());
		groupType.setInsuser(cu.getId());
		int result = this.groupTypeService.save(groupType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute GroupType groupType){
		if(!this.groupTypeService.checkNotOccupied(groupType.getId(), groupType.getName())){
			model.addAttribute("result", "{\"res\":\"班次名称重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		groupType.setInsuser(cu.getId());
		groupType.setInsdt(CommUtil.nowDate());
		int result = this.groupTypeService.update(groupType);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+groupType.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.groupTypeService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.groupTypeService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
}
