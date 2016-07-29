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
import com.sipai.entity.material.SalesOrderProduct;
import com.sipai.entity.plan.DailyPlan;
import com.sipai.entity.user.User;
import com.sipai.service.material.OrderProductDetailService;
import com.sipai.service.material.SalesOrderProductService;
import com.sipai.service.plan.DailyPlanService;
import com.sipai.tools.CommUtil;

@Controller
@RequestMapping("/material/salesorderproduct")
public class SalesOrderProductController {
	@Resource
	private SalesOrderProductService salesOrderProductService;
	@Resource
	private OrderProductDetailService orderProductDetailService;
	@Resource
	private DailyPlanService dailyPlanService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/salesOrderProductList";
	}
	@RequestMapping("/getSalesOrders.do")
	public ModelAndView getSalesOrders(HttpServletRequest request,Model model,
			@RequestParam(value = "sort", required=false) String sortStr,
			@RequestParam(value = "order", required=false) String orderStr) {
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
		if(sortStr!=null&&!sortStr.isEmpty()){
			switch(sortStr){
				case "materialcode":sort = "I.materialcode";
				case "product":sort = "I.materialname";
				default:sort = "P."+sortStr;
			}
			
		}else {
			sort = " P.SalesOrderNo ";
		}
		String order="";
		if(orderStr!=null&&!orderStr.isEmpty()){
			order = orderStr;
		}else {
			order = " desc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("P.insuser", "material/salesorderproduct/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.SalesOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_status")!=null && !request.getParameter("search_status").isEmpty()){			
			if(request.getParameter("search_status").equals("deliverd")){
				wherestr += "and P.status= '3' ";
			}else if(request.getParameter("search_status").equals("edit")){
				wherestr += "and P.status= '0' ";
			}else if(request.getParameter("search_status").equals("completed")){
				wherestr += "and P.status= '1' ";
			}else if(request.getParameter("search_status").equals("invalid")){
				wherestr += "and P.status= '2' ";
			}
		}
		PageHelper.startPage(pages, pagesize);
        List<SalesOrderProduct> list = this.salesOrderProductService.getSalesOrderProductlist(wherestr+orderstr);

        PageInfo<SalesOrderProduct> page = new PageInfo<SalesOrderProduct>(list);
		JSONArray json=JSONArray.fromObject(list);

		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getSalesOrderProducts.do")
	public ModelAndView getSalesOrderProducts(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = " P.deliverydate ";//暂缓，许多数量是统计出来的无法排序
		}else {
			sort = " P.status,P.deliverydate ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("P.insuser", "material/salesorderproduct/showlist.do", cu);
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and P.SalesOrderNo like '%"+request.getParameter("search_name")+"%' ";
		}
		//选择页面不显示禁用和完成的订单，只选择下发的
		if(request.getParameter("flag")!=null && request.getParameter("flag").equals("select")){
			wherestr += " and (P.status='3' ";
			if(request.getParameter("displayfinished")!=null && !request.getParameter("displayfinished").equals("")){
				wherestr += " or P.status='1' ";			
			}
			wherestr+=")";
		}
		PageHelper.startPage(pages, pagesize);
        List<SalesOrderProduct> list = this.salesOrderProductService.getSalesOrderProductlist(wherestr+orderstr);//后塞关联对象,防止页数、数量不对

        for(SalesOrderProduct sop:list){//为了统计完成、计划数，产品列表塞入对象
        	List<OrderProductDetail> listopd=this.orderProductDetailService.getOrderProductDetail(" where D.pid='"+sop.getId()+"' order by D.productUId");
        	sop.setOrderproductdetail(listopd);        	
        }
        PageInfo<SalesOrderProduct> page = new PageInfo<SalesOrderProduct>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}

	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/salesOrderProductAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("SalesOrderProduct") SalesOrderProduct salesOrderProduct){
		User cu= (User)request.getSession().getAttribute("cu");		
		
		String id = CommUtil.getUUID();
		salesOrderProduct.setId(id);
		salesOrderProduct.setInsuser(cu.getId());
		salesOrderProduct.setInsdt(CommUtil.nowDate());
		//初始状态0编辑,1完成，2作废，3下发，		
		
		int result = this.salesOrderProductService.save(salesOrderProduct);
		if(result==1){
			int i=1;
			String productnoi=request.getParameter("product"+i);
			do{
				OrderProductDetail orderproductdetail=new OrderProductDetail();
				orderproductdetail.setId(CommUtil.getUUID());
				orderproductdetail.setInsuser(cu.getId());
				orderproductdetail.setInsdt(CommUtil.nowDate());
				orderproductdetail.setPid(id);
				orderproductdetail.setStatus("0");
				orderproductdetail.setProductuid(productnoi);
				orderProductDetailService.save(orderproductdetail);
				i++;
				productnoi=request.getParameter("product"+i);
			}while(productnoi!=null &&!productnoi.equals("") );
		}
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		SalesOrderProduct salesorderproduct = this.salesOrderProductService.getSalesOrderProduct(" where P.id='"+id+"' order by D.productUId").get(0);		
		model.addAttribute("salesorderproduct", salesorderproduct);
		return "/material/salesOrderProductEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("SalesOrderProduct") SalesOrderProduct salesorderproduct){
		User cu= (User)request.getSession().getAttribute("cu");
		salesorderproduct.setInsuser(cu.getId());
		salesorderproduct.setInsdt(CommUtil.nowDate());	
		
		int result = this.salesOrderProductService.update(salesorderproduct);
		if(result==1){
			int i=1;
			String productnoi=request.getParameter("product"+i);
			orderProductDetailService.deleteByWhere(" where pid='"+salesorderproduct.getId()+"' ");
			do{
				OrderProductDetail orderproductdetail=new OrderProductDetail();
				orderproductdetail.setId(CommUtil.getUUID());
				orderproductdetail.setInsuser(cu.getId());
				orderproductdetail.setInsdt(CommUtil.nowDate());
				orderproductdetail.setPid(salesorderproduct.getId());
				orderproductdetail.setProductuid(productnoi);
				orderproductdetail.setStatus("0");
				orderProductDetailService.save(orderproductdetail);
				i++;
				productnoi=request.getParameter("product"+i);
			}while(productnoi!=null &&!productnoi.equals("") );
		}
		String resstr="{\"res\":\""+result+"\",\"id\":\""+salesorderproduct.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	@RequestMapping("/dofinish.do")
	public String dofinish(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		if(status!=null && status.equals("2")){
			String result0="{\"res\":\"计划已作废，不可更改。\"}";
			model.addAttribute("result", result0);
			return "result";
		}else{
			SalesOrderProduct orderproduct=new SalesOrderProduct();			
			orderproduct.setId(id);
			orderproduct.setStatus("1");
			int result=this.salesOrderProductService.update(orderproduct);
			model.addAttribute("result", result);
			return "result";
		}
		
	}
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		SalesOrderProduct salesorderproduct = this.salesOrderProductService.getSalesOrderProduct(" where P.id='"+id+"' order by D.productUId").get(0);		
		model.addAttribute("salesorderproduct", salesorderproduct);
		return "/material/salesOrderProductView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.salesOrderProductService.deleteById(id);
		//仅作废关联的计划，删除未排计划的产品，工单暂不处理
		if(result==1){
			this.dailyPlanService.updateBySetAndWhere(" set status='3' where salesOrderID='"+id+"'");
			this.orderProductDetailService.deleteByWhere("where pid='"+id+"' and productionOrderNo is null");
		}
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.salesOrderProductService.deleteByWhere("where id in ('"+ids+"')");
		//仅作废关联的计划，工单暂不处理
		if(result>0){
			this.dailyPlanService.updateBySetAndWhere(" set status='3' where salesOrderID in('"+ids+"')");
			this.orderProductDetailService.deleteByWhere("where pid in('"+ids+"') and productionOrderNo is null");
		}
		model.addAttribute("result", result);
		return "result";
	}
	@RequestMapping("/select.do")
	public String selectSalesOrderProduct(HttpServletRequest request,Model model) {		
		return "/material/salesOrderProductForSelect";
	}
	@RequestMapping("/getSOPJson.do")
	public String getSOPJson(HttpServletRequest request,Model model){
		String id=request.getParameter("salesorderproductid");
		SalesOrderProduct salesorderproduct =this.salesOrderProductService.getSalesOrderProduct(" where P.id='"+id+"' order by D.productUId").get(0);
		JSONArray json=JSONArray.fromObject(salesorderproduct);
		List<OrderProductDetail> pdlist=salesorderproduct.getOrderproductdetail();
		int pdtotalnum=pdlist.size();
		int pdplanednum=0;
		for(int i=0;i<pdtotalnum;i++){
			if(pdlist.get(i).getProductionorderno()!=null && !pdlist.get(i).getProductionorderno().isEmpty()){
				pdplanednum++;
			}
		}
		int remainednum=pdtotalnum-pdplanednum;
		String result = "{\"rows\":"+json+",\"pdtotalnum\":\""+pdtotalnum+"\",\"pdplanednum\":\""+pdplanednum+"\",\"remainednum\":\""+remainednum+"\"}";
		model.addAttribute("result",result);
	    return "result";
	}
	/*
	 * view浏览界面grid显示产品序列号
	 * */
	@RequestMapping("/getPDetails.do")
	public String getPDetails(HttpServletRequest request,Model model,@RequestParam(value="pid") String pid){
		List<OrderProductDetail> list=this.salesOrderProductService.getSalesOrderProduct(" where P.id='"+pid+"' order by D.productUId").get(0).getOrderproductdetail();
		JSONArray json=JSONArray.fromObject(list);
		String result ="{\"rows\":"+json+"}";
		model.addAttribute("result",result);
	    return "result";
	}
	@RequestMapping("/invalidate.do")
	public String invalidate(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){		
		int result = this.salesOrderProductService.invalidate(id);
		model.addAttribute("result", result);
		return "result";
	}
}
