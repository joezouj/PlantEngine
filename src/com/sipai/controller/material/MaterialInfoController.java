package com.sipai.controller.material;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialInfoService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;

@Controller
@RequestMapping("/material/materialinfo")
public class MaterialInfoController {
	@Resource
	private MaterialInfoService materialInfoService;
	
	@RequestMapping("/selectMaterial.do")
	/**物料单选*/
	public String selectMaterial(HttpServletRequest request,Model model) {
		model.addAttribute("typename",request.getParameter("typename"));
		return "/material/materialForSelect";
	}
	@RequestMapping("/selectBomMaterial.do")
	/**对应BOM不重复物料单选*/
	public String selectBomMaterial(HttpServletRequest request,Model model) {
		model.addAttribute("typename",request.getParameter("typename"));
		model.addAttribute("productcode",request.getParameter("productcode"));
		return "/material/materialForBomSelect";
	}
	@RequestMapping("/selectMaterials.do")
	/**物料多选，返回物料代码 json 数据*/
	public String selectMaterials(HttpServletRequest request,Model model) {
		return "/material/materialsForSelect";
	}
	
	@RequestMapping("/selectMaterialIds.do")
	/**物料多选,返回id json 数据*/
	public String selectMaterialIds(HttpServletRequest request,Model model) {
		return "/material/materialIdsForSelect";
	}
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialInfoList";
	}
	
	@RequestMapping("/getMaterialInfos.do")
	public ModelAndView getMaterialInfos(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()&& !request.getParameter("sort").equals("materialtype")){
			sort = request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("materialtype")){
			sort = "T.typename";
		}else {
			sort = " I.materialname ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("I.insuser", "material/materialinfo/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and I.materialname like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and I.materialcode like '%"+request.getParameter("search_code")+"%' ";
		}
		//typename为选择物料时物料类型
		if(request.getParameter("typename")!=null && !request.getParameter("typename").isEmpty()){
			String tn=null;
			try {
				tn = java.net.URLDecoder.decode(request.getParameter("typename"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(tn.equals("1")){
				wherestr += " and T.typename not like '%成品%' ";
			}else{
				wherestr += " and T.typename like '%"+tn+"%' ";
			}
		}
		PageHelper.startPage(pages, pagesize);
        List<MaterialInfo> list = this.materialInfoService.getMaterialInfo(wherestr+orderstr);
        PageInfo<MaterialInfo> page = new PageInfo<MaterialInfo>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/getBomMaterialInfos.do")
	public ModelAndView getBomMaterialInfos(HttpServletRequest request,Model model) {
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
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()&& !request.getParameter("sort").equals("materialtype")){
			sort = request.getParameter("sort");
		}else if(request.getParameter("sort")!=null&&request.getParameter("sort").equals("materialtype")){
			sort = "T.typename";
		}else {
			sort = " I.materialname ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("I.insuser", "material/materialinfo/getMaterialInfos.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and I.materialname like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and I.materialcode like '%"+request.getParameter("search_code")+"%' ";
		}
		//typename为选择物料时物料类型
		if(request.getParameter("typename")!=null && !request.getParameter("typename").isEmpty()){
			String tn=null;
			try {
				tn = java.net.URLDecoder.decode(request.getParameter("typename"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(tn.equals("1")){
				wherestr += " and T.typename not like '%成品%' ";
			}else{
				wherestr += " and T.typename like '%"+tn+"%' ";
			}
		}
		String productcode = request.getParameter("productcode");
		String codeScope = " and I.materialcode in "+this.materialInfoService.getBomMaterialScope(productcode);
		wherestr+=codeScope+orderstr;
		PageHelper.startPage(pages, pagesize);
        List<MaterialInfo> list = this.materialInfoService.getMaterialInfo(wherestr);
        
        PageInfo<MaterialInfo> page = new PageInfo<MaterialInfo>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/materialInfoAdd";
	}
	
	@RequestMapping("/save.do")
	public String dosave(HttpServletRequest request,Model model,
			@ModelAttribute("materialInfo") MaterialInfo materialInfo){
		User cu= (User)request.getSession().getAttribute("cu");
		
		if(!this.materialInfoService.checkNotOccupied(materialInfo.getId(),materialInfo.getMaterialcode())){
			model.addAttribute("result", "{\"res\":\"物料代码重复\"}");
			return "result";
		}
		
		String id = CommUtil.getUUID();
		materialInfo.setId(id);
		materialInfo.setInsuser(cu.getId());
		materialInfo.setInsdt(CommUtil.nowDate());
		materialInfo.setModify(cu.getId());
		materialInfo.setModifydt(CommUtil.nowDate());
		materialInfo.setStatus("0");
		int result = this.materialInfoService.save(materialInfo);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialInfo materialInfo = this.materialInfoService.getMaterialInfo(" where I.id='"+id+"' order by I.id").get(0);		
		if(materialInfo.getMaterialtype()!=null)model.addAttribute("typeCode", materialInfo.getMaterialtype().getTypecode());		
		model.addAttribute("materialInfo", materialInfo);
		return "/material/materialInfoEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("materialInfo") MaterialInfo materialInfo){
		if(!this.materialInfoService.checkNotOccupied(materialInfo.getId(),materialInfo.getMaterialcode())){
			model.addAttribute("result", "{\"res\":\"物料代码重复\"}");
			return "result";
		}
		User cu= (User)request.getSession().getAttribute("cu");
		materialInfo.setStatus("0");
		materialInfo.setModify(cu.getId());
		materialInfo.setModifydt(CommUtil.nowDate());
		int result = this.materialInfoService.update(materialInfo);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+materialInfo.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialInfo materialInfo = this.materialInfoService.selectById(id);
		model.addAttribute("materialInfo", materialInfo);
		return "/material/materialInfoView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialInfoService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialInfoService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/doimport.do")
	public String doimport(HttpServletRequest request,Model model){
		return "/material/importMaterialInfo";
	}
	
	@RequestMapping(value = "downtemplate.do")
	public void downtemplate(HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
			//文件的实际地址
			String filepath = request.getSession().getServletContext().getRealPath("/");
			String filename = "物料信息数据导入模版.xls";
			//兼容Linux路径
			filepath = filepath+"Template"+System.getProperty("file.separator")+"material"+System.getProperty("file.separator")+filename;			
			FileUtil.downloadFile(response,filename,filepath);
	}
	
	@RequestMapping(value = "importMaterialInfo.do")
	public ModelAndView importMaterialInfo(@RequestParam MultipartFile[] file, HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
		User cu = (User)request.getSession().getAttribute("cu");
		//要存入的实际地址
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pjName = request.getContextPath().substring(1, request.getContextPath().length());
		realPath = realPath.replace(pjName,"Temp");
		
		String result = materialInfoService.doimport(realPath,file,cu);
		model.addAttribute("result",result);
    	return new ModelAndView("result");
	}
	
	@RequestMapping("/exportByResponse.do")
	public void exportByResponse(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException{
		//按照权限全部导出
		User cu=(User)request.getSession().getAttribute("cu");
		String wherestr = CommUtil.getwherestr("I.insuser", "material/materialinfo/getMaterialInfos.do", cu);
		wherestr += "order by  I.materialname   asc ";
        List<MaterialInfo> list = this.materialInfoService.getMaterialInfo(wherestr);
        //导出文件到指定目录,兼容Linux
        String fileName = "用户数据表.xls";
        this.materialInfoService.exportByResponse(response, fileName, list);
	}
	
	@RequestMapping("/exportByIds.do")
	public void exportByIds(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException{
		String idStr = request.getParameter("idStr");
		if(idStr!=null&&!idStr.isEmpty()&&idStr.indexOf(",")>1){
			idStr = idStr.replace(",","','");
			String wherestr = " where 1=1 and I.id in ('"+idStr+"') ";
			wherestr += "order by  I.materialname   asc ";
			List<MaterialInfo> list = this.materialInfoService.getMaterialInfo(wherestr);
			//导出文件到指定目录,兼容Linux
	        String fileName = "用户数据表.xls";
	        this.materialInfoService.exportByResponse(response, fileName, list);
		}
	}
}
