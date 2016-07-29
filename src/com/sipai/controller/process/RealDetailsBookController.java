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
import com.sipai.entity.process.Real;
import com.sipai.entity.process.RealDetailsBook;
import com.sipai.entity.user.User;
import com.sipai.service.process.RealDetailsBookService;
import com.sipai.service.process.RealService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/process/realDetailsBook")
public class RealDetailsBookController {
	@Resource
	private RealService realService;
	@Resource
	private RealDetailsBook realDetailsBook;
	
	@Resource
	private RealDetailsBookService realDetailsBookService;
	
	@RequestMapping("/showList.do")
	public String showList(HttpServletRequest request,Model model){
		return "process/realDetailsBookList";
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
        List<RealDetailsBook> list = this.realDetailsBookService.selectListByWhere(wherestr+orderstr);
        PageInfo<RealDetailsBook> pi = new PageInfo<RealDetailsBook>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		String detailsId = request.getParameter("pid");
		List<Real> realList = realService.getListByDetailsID(detailsId);
		if(realList!=null&&realList.size()>0){
			model.addAttribute("productcode", realList.get(0).getProduct().getMaterialcode());
		}
		return "process/realDetailsBookAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		
		realDetailsBook = this.realDetailsBookService.selectById(id);
		
		String detailsId = realDetailsBook.getPid();
		List<Real> realList = realService.getListByDetailsID(detailsId);
		if(realList!=null&&realList.size()>0){
			model.addAttribute("productcode", realList.get(0).getProduct().getMaterialcode());
		}
		model.addAttribute("realDetailsBook", realDetailsBook);
		return "process/realDetailsBookEdit";
	}
	
//	@RequestMapping("/save.do")
//	public String dosave(HttpServletRequest request,Model model,
//			@ModelAttribute RealDetailsBook realDetailsBook){
//		User cu= (User)request.getSession().getAttribute("cu");
//		
//		String id = CommUtil.getUUID();
//		realDetailsBook.setId(id);
//		realDetailsBook.setInsuser(cu.getId());
//		realDetailsBook.setInsdt(CommUtil.nowDate());
//		
//		int result = this.realDetailsBookService.save(realDetailsBook);
//		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
//		model.addAttribute("result", resstr);
//		return "result";
//	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute RealDetailsBook realDetailsBook){
		User cu= (User)request.getSession().getAttribute("cu");
		//清除所有旧数据
		this.realDetailsBookService.deleteByWhere("where pid in ('"+request.getParameter("pid")+"')");
		
		String resultstr="";
		int result = 0;
		int suc = 0;
		int fal = 0;
		if(!request.getParameter("bids").equals("")){
			String bids[]=request.getParameter("bids").split(",");
			if(bids.length>0){
				for(int i=0;i<bids.length;i++){
					String id = CommUtil.getUUID();
					realDetailsBook.setId(id);
					realDetailsBook.setPid(request.getParameter("pid"));
					realDetailsBook.setBookid(bids[i]);
					realDetailsBook.setInsuser(cu.getId());
					realDetailsBook.setInsdt(CommUtil.nowDate());
					
					result = this.realDetailsBookService.save(realDetailsBook);
					if(result==1){
						suc++;
					}else{
						fal++;
					}
				}
			}
		}
		
		if(request.getParameter("bids").equals("")){
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
			@ModelAttribute RealDetailsBook realDetailsBook){
		User cu= (User)request.getSession().getAttribute("cu");
		
		realDetailsBook.setUpduser(cu.getId());
		realDetailsBook.setUpddt(CommUtil.nowDate());
		
		int result = this.realDetailsBookService.update(realDetailsBook);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+realDetailsBook.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.realDetailsBookService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.realDetailsBookService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/getchecklist.do")
	public ModelAndView getchecklist(HttpServletRequest request,Model model) {		
		String wherestr="where 1=1 ";
		if(request.getParameter("pid")!=null && !request.getParameter("pid").isEmpty()){
			wherestr += " and pid = '"+request.getParameter("pid")+"' ";
		}

        List<RealDetailsBook> list = this.realDetailsBookService.selectListByWhere(wherestr);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
