package com.sipai.controller.material;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.sipai.entity.material.Clients;
import com.sipai.entity.user.User;
import com.sipai.entity.work.GroupType;
import com.sipai.entity.work.WorkScheduling;
import com.sipai.entity.work.WorkTaskEquipment;
import com.sipai.entity.work.Workstation;
import com.sipai.service.material.ClientsService;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.GroupTypeService;
import com.sipai.service.work.WorkSchedulingService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/material/clients")
public class ClientController {
	@Resource
	private ClientsService clientsService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/clientsList";
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
		
		String wherestr=CommUtil.getwherestr("insuser", "material/clients/showlist.do", cu);
		
		PageHelper.startPage(page, rows);
        List<Clients> list = this.clientsService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Clients> pi = new PageInfo<Clients>(list);
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
		
		String wherestr=CommUtil.getwherestr("insuser", "material/clients/showlist.do", cu);
        List<Clients> list = this.clientsService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Clients> pi = new PageInfo<Clients>(list);
		JSONArray json=JSONArray.fromObject(list);
		
//		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",json);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "material/clientsAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		Clients clients  = this.clientsService.selectById(Id);
		model.addAttribute("clients", clients);
		return "material/clientsEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Clients clients){
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		clients.setId(id);
		clients.setInsdt(CommUtil.nowDate());
		clients.setInsuser(cu.getId());
		int result = this.clientsService.save(clients);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Clients clients){
		User cu= (User)request.getSession().getAttribute("cu");
		clients.setInsuser(cu.getId());
		clients.setInsdt(CommUtil.nowDate());
		int result = this.clientsService.update(clients);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+clients.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.clientsService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.clientsService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
}
