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
import com.serotonin.util.queue.ByteQueue;
import com.sipai.entity.user.User;
import com.sipai.entity.work.ModbusFig;
import com.sipai.entity.work.ModbusRecord;
import com.sipai.service.work.ModbusFigService;
import com.sipai.service.work.ModbusRecordService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/work/modbusfig")
public class ModbusFigController {
	@Resource
	private ModbusFigService modbusFigService;
	@Resource
	private ModbusRecordService modbusRecordService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/work/modbusfigList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " ipsever ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "work/modbusfig/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and name like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<ModbusFig> list = this.modbusFigService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<ModbusFig> pi = new PageInfo<ModbusFig>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "work/modbusfigAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		ModbusFig modbusFig = this.modbusFigService.selectById(userId);
		model.addAttribute("modbusFig", modbusFig);
		return "work/modbusfigEdit";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute ModbusFig modbusFig){
		User cu= (User)request.getSession().getAttribute("cu");
		
		String id = CommUtil.getUUID();
		modbusFig.setId(id);
		modbusFig.setInsuser(cu.getId());
		modbusFig.setInsdt(CommUtil.nowDate());
		
		int result = this.modbusFigService.save(modbusFig);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute ModbusFig modbusFig){
		int result = this.modbusFigService.update(modbusFig);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+modbusFig.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.modbusFigService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/testfun.do")
	public String testfun(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		ModbusFig modbusFig = this.modbusFigService.selectById(id);
		ByteQueue rece=new ByteQueue();
		rece=ReadAWriteUtil.modbusTCP(modbusFig.getIpsever(), Integer.parseInt(modbusFig.getPort()), 
				0, 2);
		if(rece==null){
			model.addAttribute("result", 0);
		}else{
			model.addAttribute("result", 1);
		}
		//保存通讯信息
		User cu= (User)request.getSession().getAttribute("cu");
		ModbusRecord modbusRecord=new ModbusRecord();
		modbusRecord.setId(CommUtil.getUUID());
		modbusRecord.setInsdt(CommUtil.nowDate());
		modbusRecord.setLastdate(CommUtil.nowDate());
		modbusRecord.setRecord(ReadAWriteUtil.errordetails);
		modbusRecord.setInsuser(cu.getId());
		modbusRecordService.save(modbusRecord);
		return "result";
	}
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.modbusFigService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showlistForSelect.do")
	public String showlistForSelect(HttpServletRequest request,Model model) {
		return "/work/workstationListForSelect";
	}
}
