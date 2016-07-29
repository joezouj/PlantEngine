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
import com.sipai.entity.work.Line;
import com.sipai.entity.work.LineWorkstation;
import com.sipai.entity.work.Workstation;
import com.sipai.service.user.UnitService;
import com.sipai.service.work.LineService;
import com.sipai.service.work.LineWorkstationService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/line")
public class LineController {
	@Resource
	private LineService lineService;
	@Resource
	private UnitService unitService;
	@Resource
	private LineWorkstation lineWorkstation;
	@Resource
	private LineWorkstationService lineWorkstationService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/lineList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " serial ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/line/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<Line> list = this.lineService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Line> pi = new PageInfo<Line>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/lineAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		Line line = this.lineService.selectById(id);
		model.addAttribute("line", line);
		
		List<Workstation> lineWorkstationList= this.lineWorkstationService.selectWorkstation("where lineid='"+id+"'");
		JSONArray json=JSONArray.fromObject(lineWorkstationList);
		model.addAttribute("workstationList", json);
		return "work/lineEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute Line line){
		User cu= (User)request.getSession().getAttribute("cu");
		if(!this.lineService.checkNotOccupied(line.getId(),line.getSerial())){
			model.addAttribute("result", "{\"res\":\"产线编号重复\"}");
			return "result";
		}
		String id = CommUtil.getUUID();
		line.setId(id);
		line.setInsuser(cu.getId());
		line.setInsdt(CommUtil.nowDate());
		
		int result = this.lineService.save(line);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute Line line){
		if(!this.lineService.checkNotOccupied(line.getId(),line.getSerial())){
			model.addAttribute("result", "{\"res\":\"产线编号重复\"}");
			return "result";
		}
		int result = this.lineService.update(line);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+line.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.lineService.deleteById(id);
		if(result>0){
			this.lineWorkstationService.deleteByWhere("where lineid='"+id+"'");
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.lineService.deleteByWhere("where id in ('"+ids+"')");
		if(result>0){
			this.lineWorkstationService.deleteByWhere("where lineid in ('"+ids+"')");
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/saveWorkstation.do")
	public String dosaveWorkstation(HttpServletRequest request,Model model,
			@RequestParam(value="lineid") String lineid,@RequestParam(value="wids") String wids){
		this.lineWorkstationService.deleteByWhere("where lineid='"+lineid+"'");
		
		String[] widarr = wids.split(",");
		int result=0;
		for(int i=0;i<widarr.length;i++){
			if(!widarr[i].equals("")){
				lineWorkstation.setLineid(lineid);
				lineWorkstation.setWorkstationid(widarr[i]);
				result += this.lineWorkstationService.save(lineWorkstation);
			}
		}
		
		String resstr="{\"res\":\""+result+"\",\"lineid\":\""+lineid+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
}
