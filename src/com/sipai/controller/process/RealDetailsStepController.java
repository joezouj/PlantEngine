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
import com.sipai.entity.process.RealDetailsStep;
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsStepService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetailsStep")
public class RealDetailsStepController {
	@Resource
	private RealDetailsStep realDetailsStep;
	
	@Resource
	private RealDetailsStepService realDetailsStepService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsStepList";
	}
	
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		if(sort==null){sort = " ord ";}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "process/real/showlist.do", cu);
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and content like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<RealDetailsStep> list = this.realDetailsStepService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetailsStep> pi = new PageInfo<RealDetailsStep>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realDetailsStepAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		realDetailsStep = this.realDetailsStepService.selectById(id);
		model.addAttribute("realDetailsStep", realDetailsStep);
		return "process/realDetailsStepEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsStep realDetailsStep){
		User cu= (User)request.getSession().getAttribute("cu");
		
		String id = CommUtil.getUUID();
		realDetailsStep.setId(id);
		realDetailsStep.setInsuser(cu.getId());
		realDetailsStep.setInsdt(CommUtil.nowDate());
		
		int result = this.realDetailsStepService.save(realDetailsStep);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsStep realDetailsStep){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetailsStep.setUpduser(cu.getId());
		realDetailsStep.setUpddt(CommUtil.nowDate());
		
		int result = this.realDetailsStepService.update(realDetailsStep);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetailsStep.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.realDetailsStepService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.realDetailsStepService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
