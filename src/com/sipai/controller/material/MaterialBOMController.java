package com.sipai.controller.material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sipai.entity.base.Tree;
import com.sipai.entity.material.MaterialBOM;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.user.User;
import com.sipai.service.material.MaterialBOMService;
import com.sipai.service.material.MaterialInfoService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;

@Controller
@RequestMapping("/material/materialbom")
public class MaterialBOMController {
	@Resource
	private MaterialBOMService materialBOMService;
	@Resource
	private MaterialInfoService materialInfoService;
	
	@RequestMapping("/showlist.do")
	public String showlist(HttpServletRequest request,Model model) {
		return "/material/materialbomList";
	}
	
	@RequestMapping("/getMaterialBOMs.do")
	public ModelAndView getMaterialBOMs(HttpServletRequest request,Model model) {
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
			sort = request.getParameter("sort");
		}else{
			sort = "materialcode";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = CommUtil.getwherestr("insuser", "material/materialbom/showlist.do", cu);
		if(request.getParameter("querytype")!=null&&request.getParameter("querytype").equals("select")){
			wherestr = " where 1=1 ";
		}
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and  materialname like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and materialcode like '%"+request.getParameter("search_code")+"%' ";
		}
		if(request.getParameter("bomid")!=null && !request.getParameter("bomid").isEmpty()){
			wherestr += "and id != '"+request.getParameter("bomid")+"' ";
		}	
		wherestr += " and pid='-1'";
		PageHelper.startPage(pages, pagesize);
        List<MaterialBOM> list = this.materialBOMService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<MaterialBOM> page = new PageInfo<MaterialBOM>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	
	@RequestMapping("/selectBOMs.do")
	public String selectBOMs(HttpServletRequest request,Model model) {
		if(request.getParameter("id")!=null && !request.getParameter("id").isEmpty()){
			request.setAttribute("bomid", request.getParameter("id"));
		}
		return "/material/bomsForSelect";
	}
	
	@RequestMapping("/selectBOMids.do")
	public String selectBOMids(HttpServletRequest request,Model model) {
		if(request.getParameter("id")!=null && !request.getParameter("id").isEmpty()){
			request.setAttribute("bomid", request.getParameter("id"));
		}
		return "/material/bomidsForSelect";
	}
	
	@RequestMapping("/getBOMsJson.do")
	public String getBOMsJson(HttpServletRequest request,Model model){
		List<MaterialBOM> list = this.materialBOMService.selectListByWhere("order by ordernumber");
		
		List<Tree> tree = new ArrayList<Tree>();
		for (MaterialBOM bom : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(bom, node);			
			node.setText(bom.getMaterialcode());
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/getBOMDetailsJson.do")
	public String getBOMDetailsJson(HttpServletRequest request,Model model){
		String sort="";
		if(request.getParameter("sort")!=null&&!request.getParameter("sort").isEmpty()){
			sort = request.getParameter("sort");
		}else{
			sort = "materialcode";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		String pid = request.getParameter("pid");
		String whereString = "where pid ='"+pid+"'"+orderstr;
		List<MaterialBOM> list = this.materialBOMService.selectListByWhere(whereString);

		JSONArray json = JSONArray.fromObject(list);
		model.addAttribute("result", json);
		return "result";
	}
	
	@RequestMapping("/getBOMDetailsTree.do")
	public String getBOMDetailsTree(HttpServletRequest request,Model model){
		String pid = request.getParameter("pid");
		//List<MaterialBOM> list = this.materialBOMService.selectListByPid(pid);
		String jsonString = this.materialBOMService.getBOMTreeGridData(pid);
		model.addAttribute("result", jsonString);
		return "result";
	}
	
	@RequestMapping("/add.do")
	public String add(HttpServletRequest request,Model model){
		return "/material/materialbomAdd";
	}
	
	@RequestMapping("/doimport.do")
	public String doimport(HttpServletRequest request,Model model){
		return "/material/importMaterialBOM";
	}
	
	@RequestMapping("/save.do")
	public ModelAndView dosave(HttpServletRequest request,Model model,
			@ModelAttribute("bom") MaterialBOM bom){
		if(!this.materialBOMService.checkNotOccupied(bom.getId(),bom.getMaterialcode(),bom.getVersion())){
			model.addAttribute("result", "{\"res\":\"此版本BOM已存在\"}");
			return new ModelAndView("result");
		}
		User cu= (User)request.getSession().getAttribute("cu");
		String id = CommUtil.getUUID();
		bom.setId(id);
		bom.setInsuser(cu.getId());
		bom.setInsdt(CommUtil.nowDate());
		bom.setModify(cu.getId());
		bom.setModifydt(CommUtil.nowDate());
		
		int result = this.materialBOMService.save(bom);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+id+"\"}";
		model.addAttribute("result", resstr);
		//return new ModelAndView("redirect:/material/bom/edit.do?id="+id);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/initsave.do")
	public ModelAndView doinitsave(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");
		String pid = request.getParameter("pid");
		String materialcode = request.getParameter("materialcode");
		String mcode[] = {};
		if(materialcode!=null&&materialcode.length()>0){
			mcode = materialcode.split(",");
		}
		int result = 0;
		for(int i=0;i<mcode.length;i++){
			MaterialBOM bom = new MaterialBOM();
			String id = CommUtil.getUUID();
			bom.setId(id);
			bom.setPid(pid);
			bom.setMaterialcode(mcode[i]);
			MaterialInfo mInfo = new MaterialInfo();
			mInfo = materialInfoService.getMaterialInfoByCode(mcode[i]);
			if(mInfo!=null){
				bom.setMaterialname(mInfo.getMaterialname());
				bom.setUnit(mInfo.getUnit());
			}
			bom.setNum(1.0);
			bom.setVersion(0);
			bom.setInsuser(cu.getId());
			bom.setInsdt(CommUtil.nowDate());
			bom.setModify(cu.getId());
			bom.setModifydt(CommUtil.nowDate());
			
			result += this.materialBOMService.save(bom);
		}
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/initbomsave.do")
	public ModelAndView doinitbomsave(HttpServletRequest request,Model model){
		User cu= (User)request.getSession().getAttribute("cu");
		String pid = request.getParameter("pid");
		String bomid = request.getParameter("bomid");
		String bomids[] = {};
		if(bomid.length()>0){
			bomids = bomid.split(",");
		}
		int result = 0;
		for(int i=0;i<bomids.length;i++){
			MaterialBOM bom = new MaterialBOM();
			String id = CommUtil.getUUID();
			bom.setId(id);
			bom.setPid(pid);
			
			MaterialBOM mInfo = new MaterialBOM();
			mInfo = materialBOMService.selectById(bomids[i]);
			if(mInfo!=null){
				bom.setMaterialcode(mInfo.getMaterialcode());
				bom.setMaterialname(mInfo.getMaterialname());
				bom.setUnit(mInfo.getUnit());
				bom.setVersion(mInfo.getVersion());
			}
			bom.setNum(1.0);
			bom.setInsuser(cu.getId());
			bom.setInsdt(CommUtil.nowDate());
			bom.setModify(cu.getId());
			bom.setModifydt(CommUtil.nowDate());
			
			result += this.materialBOMService.save(bom);
		}
		String resstr="{\"res\":\""+result+"\"}";
		model.addAttribute("result", resstr);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/edit.do")
	public String doedit(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialBOM materialBOM = this.materialBOMService.selectById(id);
		model.addAttribute("materialBOM", materialBOM);
		return "/material/materialbomEdit";
	}
	
	@RequestMapping("/update.do")
	public String doupdate(HttpServletRequest request,Model model,
			@ModelAttribute("bom") MaterialBOM bom){
		if(!this.materialBOMService.checkNotOccupied(bom.getId(),bom.getMaterialcode(),bom.getVersion())){
			model.addAttribute("result", "{\"res\":\"此版本BOM已存在\"}");
			return "result";
		}
		int result = this.materialBOMService.update(bom);
		String resstr="{\"res\":\""+result+"\",\"id\":\""+bom.getId()+"\"}";
		model.addAttribute("result", resstr);
		return "result";
	}
	
	@RequestMapping("/view.do")
	public String doview(HttpServletRequest request,Model model){
		String id = request.getParameter("id");
		MaterialBOM materialBOM = this.materialBOMService.selectById(id);
		model.addAttribute("materialBOM", materialBOM);
		return "/material/materialbomView";
	}
	
	@RequestMapping("/delete.do")
	public String delete(HttpServletRequest request,Model model,
			@RequestParam(value="id") String id){
		int result = this.materialBOMService.deleteById(id);
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping("/deletes.do")
	public String dodels(HttpServletRequest request,Model model,
			@RequestParam(value="ids") String ids){
		ids=ids.replace(",","','");
		int result = this.materialBOMService.deleteByWhere("where id in ('"+ids+"')");
		model.addAttribute("result", result);
		return "result";
	}
	
	@RequestMapping(value = "downtemplate.do")
	public void downtemplate(HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
			//文件的实际地址
			String filepath = request.getSession().getServletContext().getRealPath("/");
			String filename = "BOM导入模版.xls";
			//兼容Linux路径
			filepath = filepath+"Template"+System.getProperty("file.separator")+"material"+System.getProperty("file.separator")+filename;			
			FileUtil.downloadFile(response,filename,filepath);
	}
	
	@RequestMapping(value = "importMaterialBOM.do")
	public ModelAndView importMaterialBOM(@RequestParam MultipartFile[] file, HttpServletRequest request,
			HttpServletResponse response,Model model) throws IOException {
		User cu = (User)request.getSession().getAttribute("cu");
		//要存入的实际地址
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pjName = request.getContextPath().substring(1, request.getContextPath().length());
		realPath = realPath.replace(pjName,"Temp");
		
		String result = materialBOMService.importByExcel(realPath,file,cu);
		model.addAttribute("result",result);
    	return new ModelAndView("result");
	}
	
	@RequestMapping("/exportByResponse.do")
	public void exportByResponse(HttpServletRequest request,
			HttpServletResponse response, Model model) throws IOException{
		String idStr = request.getParameter("idStr");
		if(idStr!=null&&!idStr.isEmpty()&&idStr.indexOf(",")>1){
			String[] ids = idStr.substring(0, idStr.length()-1).split(",");
			//导出文件到指定目录,兼容Linux
	        String fileName = "BOM表.xls";
	        this.materialBOMService.exportByResponse(response, fileName, ids);
		}
	}
}
