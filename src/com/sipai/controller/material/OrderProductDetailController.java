package com.sipai.controller.material;

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
import com.sipai.entity.material.OrderProductDetail;
import com.sipai.entity.material.OrderProductDetailconnect;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.user.User;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.material.OrderProductDetailconnectService;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.service.user.UnitService;
import com.sipai.tools.CommUtil;


@Controller
@RequestMapping("/material/orderproductdetail")
public class OrderProductDetailController {
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Resource
	private OrderProductDetailconnectService orderProductDetailconnectService;
	@Resource
	private UnitService unitService;
	@Resource
	private DailyPlanService dailyPlanService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/orderProductDetailList";
	}
	@RequestMapping("/getlist.do")
	public ModelAndView getlist(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " productUId ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr=CommUtil.getwherestr("insuser", "material/orderProductDetail/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += " and  productUId like '%"+request.getParameter("search_name")+"%' ";
		}
		
		PageHelper.startPage(page, rows);
        List<OrderProductDetail> list = this.orderProductDetailService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<OrderProductDetail> pi = new PageInfo<OrderProductDetail>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
//		System.out.println(result);
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/add.do")
	public String doadd(HttpServletRequest request,Model model){
		return "material/orderProductDetailAdd";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		OrderProductDetail orderProductDetail = this.orderProductDetailService.selectListByWhere(" where id='"+Id+"' order by productUId asc").get(0);
		model.addAttribute("orderProductDetail", orderProductDetail);
		return "material/orderProductDetailEdit";
	}
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String Id = request.getParameter("id");
		OrderProductDetail orderProductDetail = this.orderProductDetailService.selectListByWhere(" where id='"+Id+"' order by productUId asc").get(0);
		model.addAttribute("orderProductDetail", orderProductDetail);
		return "material/orderProductDetailView";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute OrderProductDetail orderProductDetail){
		User cu= (User)request.getSession().getAttribute("cu");		
		String id = CommUtil.getUUID();
		orderProductDetail.setId(id);
		orderProductDetail.setInsuser(cu.getId());
		orderProductDetail.setInsdt(CommUtil.nowDate());
		int result = this.orderProductDetailService.save(orderProductDetail);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute OrderProductDetail orderProductDetail){
		orderProductDetail.setInsdt(CommUtil.nowDate());
		int result = this.orderProductDetailService.update(orderProductDetail);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+orderProductDetail.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/delete.do")
	public String dodel(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.orderProductDetailService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.orderProductDetailService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/showlistForSelect.do")
	public String showlistForSelect(HttpServletRequest request,Model model) {
		model.addAttribute("salesorderid", request.getParameter("salesorderid"));
		model.addAttribute("totalnum", request.getParameter("totalnum"));
		return "/material/orderProductDetailForSelect";
	}
	@RequestMapping("/showlistForSelectAll.do")
	public String showlistForSelectAll(HttpServletRequest request,Model model) {
		model.addAttribute("salesorderid", request.getParameter("salesorderid"));
		model.addAttribute("totalnum", request.getParameter("totalnum"));
		return "/material/orderProductDetailForSelectAll";
	}
	//list页面树形菜单
	@RequestMapping("/getlistForSelect.do")
	public ModelAndView getlistForSelect(HttpServletRequest request,Model model,
			@RequestParam(value = "page") Integer page,
			@RequestParam(value = "rows") Integer rows,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " P.status asc ,P.insdt  ";
		}
		if(order==null){
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String wherestr = CommUtil.getwherestr("P.insuser", "plan/dailyplan/showlist.do", cu);
		wherestr += " and P.delflag!='true' ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
//		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
//			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
//		}
//		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
//			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
//		}
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
			if(request.getParameter("search_status").equals("completed")){
				wherestr += "and P.status= '6' ";
			}else if(request.getParameter("search_status").equals("edit")){
				wherestr += "and P.status= '0' ";
			}else{
				wherestr += "and P.status!='0' and P.status!='6' ";
			}
		}
		if(request.getParameter("salesorderid")!=null &&!request.getParameter("salesorderid").equals("")){
			wherestr+=" and P.salesorderid='"+request.getParameter("salesorderid")+"'";
		}		
		PageHelper.startPage(page, rows);
        List<DailyPlan> list = this.dailyPlanService.getDailyPlanlist(wherestr+orderstr);
        PageInfo<DailyPlan> pi = new PageInfo<DailyPlan>(list);
		JSONArray json=JSONArray.fromObject(list);		
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_detail.do")
	public ModelAndView getlist_detail(HttpServletRequest request,Model model,
			@RequestParam(value = "productionorderno") String productionorderno,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " D.productUId   ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = "where 1=1";//CommUtil.getwherestr("D.insuser", "plan/dailyplan/showlist.do", cu);
		
//		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
//			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
//		}
//		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
//			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
//		}
//		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
//			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
//		}
//		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
//			if(request.getParameter("search_status").equals("completed")){
//				wherestr += "and P.status= '6' ";
//			}else if(request.getParameter("search_status").equals("edit")){
//				wherestr += "and P.status= '0' ";
//			}else{
//				wherestr += "and P.status!='0' and P.status!='6' ";
//			}
//		}
		DailyPlan dailyplan=this.dailyPlanService.selectListByWhere(" where productionorderno='"+productionorderno+"'").get(0);
		List<OrderProductDetailconnect> listopdc=this.orderProductDetailconnectService.selectListByWhere(" where planid='"+dailyplan.getId()+"'");
		String idstring="";
		for(int i=0;i<listopdc.size();i++){
			idstring+="'"+listopdc.get(i).getProductdetailid()+"',";
		}
		idstring=idstring.substring(0, idstring.length()-1);
		wherestr+=" and D.id in("+idstring+")";
        List<OrderProductDetail> list = this.orderProductDetailService.getOrderProductDetail(wherestr+orderstr);
        PageInfo<OrderProductDetail> pi = new PageInfo<OrderProductDetail>(list);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlist_detailOfSalesOrder.do")
	public ModelAndView getlist_detailOfSalesOrder(HttpServletRequest request,Model model,
			@RequestParam(value = "salesorderid") String salesorderid,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " D.productUId   ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = "where 1=1";//CommUtil.getwherestr("D.insuser", "plan/dailyplan/showlist.do", cu);
		
//		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
//			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
//		}
//		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
//			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
//		}
//		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
//			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
//		}
//		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
//			if(request.getParameter("search_status").equals("completed")){
//				wherestr += "and P.status= '6' ";
//			}else if(request.getParameter("search_status").equals("edit")){
//				wherestr += "and P.status= '0' ";
//			}else{
//				wherestr += "and P.status!='0' and P.status!='6' ";
//			}
//		}		
		if(salesorderid!=null && !salesorderid.equals("")){
			wherestr+=" and D.pid='"+salesorderid+"'";
		}	
		wherestr+=" and D.productionorderno is not null ";
		wherestr+=" and O.wfOrderid is null";//已经启动的产品不允许重排(不予显示),只显示未启动(wforderid空)
		if(request.getParameter("plandate")!=null && !request.getParameter("plandate").isEmpty()){
			wherestr+=" and S.plandt<'"+request.getParameter("plandate")+"'";
		}
        List<OrderProductDetail> list = this.orderProductDetailService.getOrderProductDetail(wherestr+orderstr);
        PageInfo<OrderProductDetail> pi = new PageInfo<OrderProductDetail>(list);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getlistall_detailOfSalesOrder.do")
	public ModelAndView getlistall_detailOfSalesOrder(HttpServletRequest request,Model model,
			@RequestParam(value = "salesorderid") String salesorderid,
			@RequestParam(value = "sort", required=false) String sort,
			@RequestParam(value = "order", required=false) String order) {
		User cu=(User)request.getSession().getAttribute("cu");
		if(sort==null){
			sort = " D.productUId   ";
		}
		if(order==null){
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = "where 1=1";//CommUtil.getwherestr("D.insuser", "plan/dailyplan/showlist.do", cu);
		
//		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
//			wherestr += "and P.productionOrderNo like '%"+request.getParameter("search_name")+"%' ";
//		}
//		if(request.getParameter("sdt")!=null && !request.getParameter("sdt").isEmpty()){
//			wherestr += "and P.stdt>= '"+request.getParameter("sdt")+"' ";
//		}
//		if(request.getParameter("edt")!=null && !request.getParameter("edt").isEmpty()){
//			wherestr += "and P.stdt<= '"+request.getParameter("edt")+"' ";
//		}
//		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
//			if(request.getParameter("search_status").equals("completed")){
//				wherestr += "and P.status= '6' ";
//			}else if(request.getParameter("search_status").equals("edit")){
//				wherestr += "and P.status= '0' ";
//			}else{
//				wherestr += "and P.status!='0' and P.status!='6' ";
//			}
//		}		
		if(salesorderid!=null && !salesorderid.equals("")){
			wherestr+=" and D.pid='"+salesorderid+"'";
		}
		if(request.getParameter("plandate")!=null && !request.getParameter("plandate").isEmpty()&&request.getParameter("planid")!=null && !request.getParameter("planid").isEmpty()){
			wherestr+=" and (S.plandt<'"+request.getParameter("plandate")+"' or S.plandt is null or P.id='"+request.getParameter("planid")+"')";
		}
        List<OrderProductDetail> list = this.orderProductDetailService.getOrderProductDetail(wherestr+orderstr);
        PageInfo<OrderProductDetail> pi = new PageInfo<OrderProductDetail>(list);
		JSONArray json=JSONArray.fromObject(list);
		String result="{\"total\":"+pi.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getSysArrangeList.do")
	public ModelAndView getSysArrangeList(HttpServletRequest request,Model model,
			@RequestParam(value = "salesorderid") String salesorderid,
			@RequestParam(value = "sysnum") String sysnumstr) {       
        List<OrderProductDetail> list=this.orderProductDetailService.selectTopNByWhere(sysnumstr, "where pid='"+salesorderid+"' and productionorderno is NULL order by productUId asc ");
        JSONArray json=JSONArray.fromObject(list);
		String result="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
}
