package com.sipai.controller.process;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.snaker.engine.entity.Process;
import org.snaker.engine.helper.AssertHelper;
import org.snaker.engine.model.ProcessModel;
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
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.RealService;
import com.sipai.snaker.SnakerEngineFacets;
import com.sipai.snaker.SnakerHelper;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetails")
public class RealDetailsController {
	@Resource
	private RealDetails realDetails;
	
	@Resource
	private RealDetailsService realDetailsService;
	
	@Resource
	private SnakerEngineFacets facets;
	
	@Resource
	private Real real;
	
	@Resource
	private RealService realService;
	
	@RequestMapping("/showlist.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsList";
	}
	
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		if(sort==null){sort = " number ";}
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
        List<RealDetails> list = this.realDetailsService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetails> pi = new PageInfo<RealDetails>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realDetailsAdd";
	}
	
	@RequestMapping("/edit.do")
	public String edit(HttpServletRequest request,Model model,
			@RequestParam(value="pid") String pid, @RequestParam(value="taskname") String taskname){
		List<RealDetails> list= this.realDetailsService.selectListByWhere(" where pid='"+pid+"' and taskname='"+taskname+"'");
		if(list!=null && list.size()>0){
			model.addAttribute("realDetails", list.get(0));
		}
		return "process/realDetailsEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetails realDetails){
		User cu= (User)request.getSession().getAttribute("cu");
		
		String id = CommUtil.getUUID();
		realDetails.setId(id);
		realDetails.setInsdt(CommUtil.nowDate());
		realDetails.setUpdateuser(cu.getId());
		realDetails.setUpdatedt(CommUtil.nowDate());
		
		int result = this.realDetailsService.save(realDetails);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute RealDetails realDetails){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetails.setUpdateuser(cu.getId());
		realDetails.setUpdatedt(CommUtil.nowDate());
		
		int result = this.realDetailsService.update(realDetails);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetails.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		this.realDetailsService.deleteChildrenById(id);
		
		int result = this.realDetailsService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		this.realDetailsService.deleteChildrenByIds(ids);
		
		int result = this.realDetailsService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showsnakerlist.do")
	public String showsnakerlist(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		real= realService.selectById(id);
		
		if(StringUtils.isNotEmpty(real.getProcessid())) {
			Process process = facets.getEngine().process().getProcessById(real.getProcessid());
			AssertHelper.notNull(process);
			ProcessModel processModel = process.getModel();
			if(processModel != null) {
				String json = SnakerHelper.getModelJson(processModel);
				model.addAttribute("process", json);
			}
		}
		return "process/realDetailsSnakerList";
	}
	
}
