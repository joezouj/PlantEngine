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
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.process.RealDetailsMaterial;
import com.sipai.entity.process.TaskEquipment;
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsEquipmentService;
import com.sipai.service.process.RealDetailsService;
import com.sipai.service.process.TaskEquipmentService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetailsEquipment")
public class RealDetailsEquipmentController {
	@Resource
	private RealDetailsEquipment realDetailsEquipment;
	@Resource
	private RealDetailsService realDetailsService;
	@Resource
	private TaskEquipmentService taskEquipService;
	@Resource
	private RealDetailsEquipmentService realDetailsEquipmentService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsEquipmentList";
	}
	
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		
		if(sort==null){sort = " insdt ";}
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
        List<RealDetailsEquipment> list = this.realDetailsEquipmentService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetailsEquipment> pi = new PageInfo<RealDetailsEquipment>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "process/realDetailsEquipmentAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		realDetailsEquipment = this.realDetailsEquipmentService.selectById(id);
		model.addAttribute("realDetailsEquipment", realDetailsEquipment);
		return "process/realDetailsEquipmentEdit";
	}
	
//	@RequestMapping("/save.do")
//	public String dosave(HttpServletRequest request,Model model,
//			@ModelAttribute RealDetailsEquipment realDetailsEquipment){
//		User cu= (User)request.getSession().getAttribute("cu");
//		
//		String id = CommUtil.getUUID();
//		realDetailsEquipment.setId(id);
//		realDetailsEquipment.setInsuser(cu.getId());
//		realDetailsEquipment.setInsdt(CommUtil.nowDate());
//		
//		int result = this.realDetailsEquipmentService.save(realDetailsEquipment);
//		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
//		model.addAttribute("result", resstr);
//		return "result";
//	}
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsEquipment realDetailsEquipment){
		User cu= (User)request.getSession().getAttribute("cu");
		//清除所有旧数据
		this.realDetailsEquipmentService.deleteByWhere("where pid in ('"+request.getParameter("pid")+"')");
		
		String resultstr="";
		int result = 0;
		int suc = 0;
		int fal = 0;
		if(!request.getParameter("eids").equals("")){
			System.out.println(request.getParameter("eids").equals(""));
			String eids[]=request.getParameter("eids").split(",");
			if(eids.length>0){
				for(int i=0;i<eids.length;i++){
					String id = CommUtil.getUUID();
					realDetailsEquipment.setId(id);
					realDetailsEquipment.setPid(request.getParameter("pid"));
					realDetailsEquipment.setEquipmentid(eids[i]);
					realDetailsEquipment.setInsuser(cu.getId());
					realDetailsEquipment.setInsdt(CommUtil.nowDate());
					
					result = this.realDetailsEquipmentService.save(realDetailsEquipment);
					if(result==1){
						suc++;
					}else{
						fal++;
					}
				}
			}
		}
		
		if(request.getParameter("eids").equals("")){
			resultstr += "已清除所有数据！";
		}
		if(suc>0){
			resultstr += "成功添加"+suc+"条数据！";
		}
		if(fal>0){
			resultstr += fal+"条数据添加失败！";
		}
		model.addAttribute("result", resultstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsEquipment realDetailsEquipment){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetailsEquipment.setUpduser(cu.getId());
		realDetailsEquipment.setUpddt(CommUtil.nowDate());
		
		int result = this.realDetailsEquipmentService.update(realDetailsEquipment);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetailsEquipment.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.realDetailsEquipmentService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.realDetailsEquipmentService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getTaskEquipment.do")
	public ModelAndView getTaskEquipment(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		
		if(sort==null){sort = " equipmentCardID ";}
		if(order==null){order = " asc ";}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and tb_process_real_details.id='"+request.getParameter("pid")+"'";
			RealDetails realDetail= this.realDetailsService.selectById(request.getParameter("pid"));
			wherestr += " and tb_process_task_equipment.taskname = '"+realDetail.getTaskname()+"' ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and TB_EM_EquipmentCard.equipmentName like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += " and TB_EM_EquipmentCard.equipmentCardID like '%"+request.getParameter("search_code")+"%' ";
		}
		PageHelper.startPage(page, rows);
        List<TaskEquipment> list = this.taskEquipService.selectListByWhere1(wherestr+orderstr);
        PageInfo<TaskEquipment> pi = new PageInfo<TaskEquipment>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/getchecklist.do")
	public ModelAndView getchecklist(HttpServletRequest request,Model model) {		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}

        List<RealDetailsEquipment> list = this.realDetailsEquipmentService.selectListByWhere(wherestr);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
