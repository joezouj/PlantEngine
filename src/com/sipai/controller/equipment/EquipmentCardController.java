package com.sipai.controller.equipment;

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
import com.sipai.entity.equipment.EquipmentCard;
import com.sipai.entity.equipment.EquipmentClass;
import com.sipai.entity.equipment.GeographyArea;
import com.sipai.entity.user.User;
import com.sipai.service.equipment.EquipmentCardService;
import com.sipai.service.equipment.EquipmentClassService;
import com.sipai.service.equipment.GeographyAreaService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/equipment")
public class EquipmentCardController {
	@Resource
	private EquipmentCardService equipmentcardService;
	@Resource
	private EquipmentClassService equipmentclassService;
	@Resource
	private GeographyAreaService geographyareaService;
	@RequestMapping("/showEquipmentCard.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/equipment/equipmentCardList";
	}
	@RequestMapping("/getEquipmentCard.do")
	public ModelAndView getequipmentcard(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()&&!request.getParameter("sort").equals("equipmentclass")&&!request.getParameter("sort").equals("geographyarea")){
			sort = "E."+request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("equipmentclass")){
			sort = " C.name ";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("geographyarea")){
			sort = " G.name ";
		}else{
			sort = " equipmentcardid ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by  "+sort+" "+order;
		User cu= (User)request.getSession().getAttribute("cu");	
		String wherestr=CommUtil.getwherestr("E.insuser", "equipment/showEquipmentCard.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and equipmentname like '%"+request.getParameter("search_name")+"%' ";
		}				
		PageHelper.startPage(pages, pagesize);
        List<EquipmentCard> list = this.equipmentcardService.getEquipmentCard(wherestr+orderstr);
        
        PageInfo<EquipmentCard> page = new PageInfo<EquipmentCard>(list);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/addEquipmentCard.do")
	public String doadd(HttpServletRequest request,Model model){
		List<EquipmentClass> equipmentClassList=this.equipmentclassService.selectListByWhere(" where status!='禁用' order by insdt");
		List<GeographyArea> geographyAreaList=this.geographyareaService.selectListByWhere(" where status!='禁用' order by insdt");
		model.addAttribute("geographyAreaList",geographyAreaList);
		model.addAttribute("equipmentClassList",equipmentClassList);
		return "equipment/equipmentCardAdd";
	}
	
	@RequestMapping("/deleteEquipmentCard.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.equipmentcardService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/deleteEquipmentCards.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.equipmentcardService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/saveEquipmentCard.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("equipmentcard") EquipmentCard equipmentcard){
		User cu= (User)request.getSession().getAttribute("cu");		
		if(!this.equipmentcardService.checkNotOccupied(equipmentcard.getId(), equipmentcard.getEquipmentcardid())){
			model.addAttribute("result", "{\"res\":\"设备编号重复\"}");
			return "result";
		}		
		String equipmentcardId = CommUtil.getUUID();
		equipmentcard.setId(equipmentcardId);		
		equipmentcard.setInsuser(cu.getId());
		equipmentcard.setInsdt(CommUtil.nowDate());		
		int result = this.equipmentcardService.save(equipmentcard);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+equipmentcardId+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/viewEquipmentCard.do")
	public String doview(HttpServletRequest request,Model model){
		String equipmentcardId = request.getParameter("id");
		EquipmentCard equipmentcard = this.equipmentcardService.getEquipmentCardById(equipmentcardId);
		model.addAttribute("equipmentcard", equipmentcard);
		return "equipment/equipmentCardView";
	}
	@RequestMapping("/editEquipmentCard.do")
	public String doedit(HttpServletRequest request,Model model){
		String equipmentcardId = request.getParameter("id");
		EquipmentCard equipmentcard = this.equipmentcardService.getEquipmentCardById(equipmentcardId);
		model.addAttribute("equipmentcard", equipmentcard);
		List<EquipmentClass> equipmentClassList=this.equipmentclassService.selectListByWhere(" where status!='禁用' order by insdt");
		List<GeographyArea> geographyAreaList=this.geographyareaService.selectListByWhere(" where status!='禁用' order by insdt");
		model.addAttribute("geographyAreaList",geographyAreaList);
		model.addAttribute("equipmentClassList",equipmentClassList);
		return "equipment/equipmentCardEdit";
	}
	@RequestMapping("/updateEquipmentCard.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("equipmentcard") EquipmentCard equipmentcard){
		if(!this.equipmentcardService.checkNotOccupied(equipmentcard.getId(), equipmentcard.getEquipmentcardid())){
			model.addAttribute("result", "{\"res\":\"设备编号重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");		
		equipmentcard.setInsuser(cu.getId());
		equipmentcard.setInsdt(CommUtil.nowDate());
		int result = this.equipmentcardService.update(equipmentcard);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+equipmentcard.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/showEquipmentCardForSelect.do")
	public String showEquipmentCardForSelect(HttpServletRequest request,Model model) {
		return "/equipment/equipmentCardListForSelect";
	}
}
