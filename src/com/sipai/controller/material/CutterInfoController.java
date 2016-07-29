package com.sipai.controller.material;

import java.io.UnsupportedEncodingException;
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
import com.sipai.entity.material.CutterInfo;
import com.sipai.entity.user.User;
import com.sipai.service.material.CutterInfoService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/cutterinfo")
public class CutterInfoController {
	@Resource
	private CutterInfoService cutterInfoService;
	
	@RequestMapping("/selectCutter.do")
	/**刀具单选*/
	public String selectCutter(HttpServletRequest request,Model model) {
		model.addAttribute("cuttername",request.getParameter("cuttername"));
		return "/material/cutterForSelect";
	}
	@RequestMapping("/selectMaterials.do")
	/**刀具多选，返回刀具编码 json 数据*/
	public String selectCutters(HttpServletRequest request,Model model) {
		return "/material/cuttersForSelect";
	}
	
	@RequestMapping("/selecCutterIds.do")
	/**刀具多选,返回id json 数据*/
	public String selectCutterIds(HttpServletRequest request,Model model) {
		return "/material/cutterIdsForSelect";
	}
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/cutterInfoList";
	}
	
	@RequestMapping("/getCutterInfos.do")
	public ModelAndView getCutterInfos(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()&& !request.getParameter("sort").equals("cutterType")&& !request.getParameter("sort").equals("cutterPosition")){
			sort = "I."+request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("cutterType")){
			sort = "T.typename";
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("cutterPosition")){
			sort = "P.name";
		}else {
			sort = " I.cuttername ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("I.insuser", "material/cutterinfo/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and I.cuttername like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and I.cuttercode like '%"+request.getParameter("search_code")+"%' ";
		}
		//typename为选择刀具时刀具类型
		if(request.getParameter("typename")!=null && !request.getParameter("typename").isEmpty()){
			String tn=null;
			try {
				tn = java.net.URLDecoder.decode(request.getParameter("typename"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			wherestr += " and T.typename like '%"+tn+"%' ";
		}
		PageHelper.startPage(pages, pagesize);
		//System.out.println(wherestr+orderstr);
        List<CutterInfo> list = this.cutterInfoService.getCutterInfo(wherestr+orderstr);
        PageInfo<CutterInfo> page = new PageInfo<CutterInfo>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/cutterInfoAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("cutterInfo") CutterInfo cutterInfo){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.cutterInfoService.checkNotOccupied(cutterInfo.getId(),cutterInfo.getCuttercode())){
			model.addAttribute("result", "{\"res\":\"刀具编码重复\"}");
			return "result";
		}
		
		String id = CommUtil.getUUID();
		cutterInfo.setId(id);
		cutterInfo.setInsuser(cu.getId());
		cutterInfo.setInsdt(CommUtil.nowDate());
		cutterInfo.setModify(cu.getId());
		cutterInfo.setModifydt(CommUtil.nowDate());
		cutterInfo.setStatus("1");
		int result = this.cutterInfoService.save(cutterInfo);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterInfo cutterInfo = this.cutterInfoService.getCutterInfo(" where I.id='"+id+"' order by I.id").get(0);		
		model.addAttribute("cutterInfo", cutterInfo);
		return "/material/cutterInfoEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("cutterInfo") CutterInfo cutterInfo){
		if(!this.cutterInfoService.checkNotOccupied(cutterInfo.getId(),cutterInfo.getCuttercode())){
			model.addAttribute("result", "{\"res\":\"刀具编码重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		cutterInfo.setModify(cu.getId());
		cutterInfo.setModifydt(CommUtil.nowDate());
		int result = this.cutterInfoService.update(cutterInfo);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+cutterInfo.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		CutterInfo cutterInfo = this.cutterInfoService.selectById(id);
		model.addAttribute("cutterInfo", cutterInfo);
		return "/material/cutterInfoView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.cutterInfoService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.cutterInfoService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
}
