package com.sipai.controller.plan;

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
import com.sipai.entity.plan.DeliverProcessor;
import com.sipai.entity.user.User;
import com.sipai.service.plan.DeliverProcessorService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/plan/deliverprocessor")
public class DeliverProcessorController {
	@Resource
	private DeliverProcessorService deliverProcessorService;

	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		return "/plan/deliverProcessorList";
	}
	
	@RequestMapping("/showviewlist.do")
	public String showviewlist(HttpServletRequest request,Model model) {
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
		}
		return "/plan/deliverProcessorViewList";
	}
	
	@RequestMapping("/getDeliverProcessors.do")
	public ModelAndView getDeliverProcessors(HttpServletRequest request,Model model) {
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
			sort = " p.insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = "where 1=1 ";
		if(request.getParameter("search_username")!=null && !request.getParameter("search_username").isEmpty()){
			wherestr += " and u.name like '%"+request.getParameter("search_username")+"%'";
		}
		if(request.getParameter("search_userserial")!=null && !request.getParameter("search_userserial").isEmpty()){
			wherestr += " and u.serial like '%"+request.getParameter("search_userserial")+"%'";
		}
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and p.pid = '"+request.getParameter("pid")+"'";
			model.addAttribute("pid",request.getParameter("pid"));
		}
		PageHelper.startPage(pages, pagesize);
        List<DeliverProcessor> list = this.deliverProcessorService.getDeliverProcessor(wherestr+orderstr);
        PageInfo<DeliverProcessor> page = new PageInfo<DeliverProcessor>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
		//System.out.println(result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			model.addAttribute("pid",request.getParameter("pid"));
			String wherestr = " where 1=1 and p.pid = '"+request.getParameter("pid")+"' order by p.insdt ";
			List<DeliverProcessor> list = this.deliverProcessorService.getDeliverProcessor(wherestr);
			String processorids = "";
			for(int i=0;i<list.size();i++){
				DeliverProcessor p = list.get(i);
				processorids += p.getProcessorid()+",";
			}
			if(processorids.length()>1){
				processorids = processorids.substring(0, processorids.length()-1);
				model.addAttribute("processorids",processorids);
			}
		}
		return "/plan/deliverProcessorAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");		
		String processorids = request.getParameter("processorids");
		String pid = request.getParameter("pid");
		//保存前清空上次添加的内容
		String wherestr = "where 1=1 and pid='"+pid+"'";
		deliverProcessorService.deleteByWhere(wherestr);
		
		String manids[] = {};
		if(processorids!=null&&processorids.length()>0){
			manids = processorids.split(",");
		}
		int result = 0;
		int suc = 0;
		int fal = 0;
		for(int i=0;i<manids.length;i++){
			DeliverProcessor processor = new DeliverProcessor();
			String id = CommUtil.getUUID();
			processor.setId(id);
			processor.setPid(pid);
			processor.setProcessorid(manids[i]);
			processor.setInsdt(CommUtil.nowDate());
			processor.setInsuser(cu.getId());
			result = deliverProcessorService.save(processor);
			if(result==1){
				suc++;
			}else{
				fal++;
			}
		}
		String resultStr = "";
		if(manids.length==0){
			resultStr += "已清除所有配料员！";
		}
		if(suc>0){
			resultStr += "成功添加"+suc+"位配料员！";
		}
		if(fal>0){
			resultStr += fal+"位配料员添加失败！";
		}
		String resstr="{\"res\":\""+resultStr+"\"}";
		model.addAttribute("result", resstr);
		
		return "result";
	}
	

	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DeliverProcessor deliverProcessor = this.deliverProcessorService.selectById(id);
		model.addAttribute("deliverProcessor", deliverProcessor);
		return "/plan/deliverProcessorEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("deliverProcessor") DeliverProcessor deliverProcessor){
		User cu= (User)request.getSession().getAttribute("cu");
		deliverProcessor.setInsuser(cu.getId());
		deliverProcessor.setInsdt(CommUtil.nowDate());
		int result = this.deliverProcessorService.update(deliverProcessor);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+deliverProcessor.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		DeliverProcessor deliverProcessor = this.deliverProcessorService.selectById(id);
		model.addAttribute("deliverProcessor", deliverProcessor);
		return "/plan/deliverProcessorView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.deliverProcessorService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.deliverProcessorService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
