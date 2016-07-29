package com.sipai.controller.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sipai.entity.base.CommonFile;
import com.sipai.entity.base.Tree;
import com.sipai.entity.document.Data;
import com.sipai.entity.user.User;
import com.sipai.service.base.CommonFileService;
import com.sipai.service.document.DataService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;

@Controller
@RequestMapping("/document")
public class DataController {
	@Resource
	private DataService dataService;
	@Resource
	private CommonFileService commonFileService;

	@RequestMapping("/showDataTree.do")
	public String showUnitTree(HttpServletRequest request, Model model) {
		return "document/dataTree";
	}

	@RequestMapping("/getDataJson.do")
	public String getDataJson(HttpServletRequest request, Model model) {

		List<Data> list = this.dataService.selectList();

		List<Tree> tree = new ArrayList<Tree>();
		for (Data resource : list) {
			Tree node = new Tree();
			BeanUtils.copyProperties(resource, node);

			node.setText(this.dataService.getNameById(resource.getId()));

			if (!resource.getId().equals("-1")) {
				// System.out.println(resource.getDoctype());
				if (resource.getDoctype().equals("A")) {
					node.setIconCls("ext-icon-folder_lightbulb");
				} else if (resource.getDoctype().equals("B")) {
					node.setIconCls("ext-icon-folder_image");
				} else {
					node.setIconCls("ext-icon-folder_page");
				}
			}

			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("type", resource.getDoctype());
			node.setAttributes(attributes);
			tree.add(node);
		}
		JSONArray json = JSONArray.fromObject(tree);
		// System.out.println(json);
		model.addAttribute("result", json);
		return "result";
	}

	@RequestMapping("/addData.do")
	public String doadd(HttpServletRequest request, Model model) {
		return "document/dataAdd";
	}

	@RequestMapping("/showWorkOrderAdd.do")
	public String doWorkOrderadd(HttpServletRequest request, Model model,
			@RequestParam(value = "pid", required = false) String pid) {
		model.addAttribute("pname", this.dataService.getNameById(pid));
		model.addAttribute("level", this.dataService.getLevelById(pid));
		model.addAttribute("fileid", CommUtil.getUUID());
		model.addAttribute("id", CommUtil.getUUID());
		return "document/workorderAdd";
	}

	@RequestMapping("/showWorkOrderEdit.do")
	public String doWorkOrderEdit(HttpServletRequest request, Model model,
			@RequestParam String id) {
		Data data = dataService.selectById(id);
		model.addAttribute("data", data);
		model.addAttribute("pname", this.dataService.getNameById(data.getPid()));
		return "document/workorderEdit";
	}

	@RequestMapping("/saveWorkOrder.do")
	public ModelAndView doWorkOrderSave(HttpServletRequest request,
			Model model, @ModelAttribute Data t) {
		// t.setId(CommUtil.getUUID());
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"工作指令编号重复\"}");
			return new ModelAndView("result");
		}
		User cu = (User) request.getSession().getAttribute("cu");
		t.setInsuser(cu.getId());
		t.setInsdt(CommUtil.nowDate());
		int result = this.dataService.save(t);
		model.addAttribute("result","{\"res\":\""+result+"\",\"id\":\""+t.getId()+"\"}");
		return new ModelAndView("result");
	}

	@RequestMapping("/updateWorkOrder.do")
	public ModelAndView doWorkOrderUpdate(HttpServletRequest request,
			Model model, @ModelAttribute Data t) {
		User cu = (User) request.getSession().getAttribute("cu");
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"工作指令编号重复\"}");
			return new ModelAndView("result");
		}
		t.setUpdateuser(cu.getId());
		t.setUpdatedt(CommUtil.nowDate());
		int result = this.dataService.update(t);
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

	@RequestMapping("/deleteWorkOrder.do")
	public ModelAndView doWorkOrderDelete(HttpServletRequest request,
			Model model, @RequestParam(value = "id") String id)
			throws IOException {
		String mappernamespace = "document.DocFileMapper";
		int result = this.dataService.deleteById(id);
		List<CommonFile> commfile = this.commonFileService.selectByMasterId(id,
				mappernamespace);
		int res = this.commonFileService.deleteByMasterId(id, mappernamespace);
		if (res > 0) {
			for (int i = 0; i < commfile.size(); i++) {
				FileUtil.deleteFile(commfile.get(i).getAbspath());
			}
		}
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

	@RequestMapping("/showDrawingAdd.do")
	public String doDrawingadd(HttpServletRequest request, Model model,
			@RequestParam(value = "pid", required = false) String pid) {
		model.addAttribute("pname", this.dataService.getNameById(pid));
		model.addAttribute("level", this.dataService.getLevelById(pid));
		model.addAttribute("fileid", CommUtil.getUUID());
		model.addAttribute("id", CommUtil.getUUID());
		return "document/drawingAdd";
	}

	@RequestMapping("/showDrawingEdit.do")
	public String doDrawingEdit(HttpServletRequest request, Model model,
			@RequestParam String id) {
		Data data = dataService.selectById(id);
		model.addAttribute("data", data);
		model.addAttribute("pname", this.dataService.getNameById(data.getPid()));
		return "document/drawingEdit";
	}

	@RequestMapping("/saveDrawing.do")
	public ModelAndView doDrawingSave(HttpServletRequest request, Model model,
			@ModelAttribute Data t) {
		// t.setId(CommUtil.getUUID());
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"图纸编号重复\"}");
			return new ModelAndView("result");
		}
		User cu = (User) request.getSession().getAttribute("cu");
		t.setInsuser(cu.getId());
		t.setInsdt(CommUtil.nowDate());
		int result = this.dataService.save(t);
		model.addAttribute("result","{\"res\":\""+result+"\",\"id\":\""+t.getId()+"\"}");
		return new ModelAndView("result");
	}

	@RequestMapping("/updateDrawing.do")
	public ModelAndView doDrawingUpdate(HttpServletRequest request,
			Model model, @ModelAttribute Data t) {
		User cu = (User) request.getSession().getAttribute("cu");
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"图纸编号重复\"}");
			return new ModelAndView("result");
		}
		t.setUpdateuser(cu.getId());
		t.setUpdatedt(CommUtil.nowDate());
		int result = this.dataService.update(t);
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

	@RequestMapping("/deleteDrawing.do")
	public ModelAndView doDrawingDelete(HttpServletRequest request,
			Model model, @RequestParam(value = "id") String id)
			throws IOException {
		String mappernamespace = "document.DocFileMapper";
		int result = this.dataService.deleteById(id);
		List<CommonFile> commfile = this.commonFileService.selectByMasterId(id,
				mappernamespace);
		int res = this.commonFileService.deleteByMasterId(id, mappernamespace);
		if (res > 0) {
			for (int i = 0; i < commfile.size(); i++) {
				FileUtil.deleteFile(commfile.get(i).getAbspath());
			}
		}
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}
	
	@RequestMapping("/selectDrawing.do")
	public String selectDrawing(HttpServletRequest request,Model model) {
		return "/document/drawingForSelect";
	}

	@RequestMapping("/selectBook.do")
	public String selectBook(HttpServletRequest request,Model model) {
		return "/document/bookForSelect";
	}
	
	@RequestMapping("/getDatasForSelect.do")
	public ModelAndView getDatasForSelect(HttpServletRequest request,Model model) {
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
		}else {
			sort = " insdt ";
		}
		String order="";
		if(request.getParameter("order")!=null&&!request.getParameter("order").isEmpty()){
			order = request.getParameter("order");
		}else {
			order = " asc ";
		}
		String orderstr=" order by "+sort+" "+order;
		
		String wherestr = " where 1=1 ";
		if(request.getParameter("search_name")!=null && !request.getParameter("search_name").isEmpty()){
			wherestr += "and docname like '%"+request.getParameter("search_name")+"%' ";
		}
		if(request.getParameter("search_code")!=null && !request.getParameter("search_code").isEmpty()){
			wherestr += "and number like '%"+request.getParameter("search_code")+"%' ";
		}
		if(request.getParameter("doctype")!=null && !request.getParameter("doctype").isEmpty()){
			wherestr += "and doctype = '"+request.getParameter("doctype")+"' ";
		}
		PageHelper.startPage(pages, pagesize);
        List<Data> list = this.dataService.selectListByWhere(wherestr+orderstr);
        
        PageInfo<Data> page = new PageInfo<Data>(list);
		JSONArray json=JSONArray.fromObject(list);
		
		String result="{\"total\":"+page.getTotal()+",\"rows\":"+json+"}";
		model.addAttribute("result",result);
        return new ModelAndView("result");
	}
	@RequestMapping("/showBookAdd.do")
	public String doBookadd(HttpServletRequest request, Model model,
			@RequestParam(value = "pid", required = false) String pid) {
		model.addAttribute("pname", this.dataService.getNameById(pid));
		model.addAttribute("level", this.dataService.getLevelById(pid));
		model.addAttribute("fileid", CommUtil.getUUID());
		model.addAttribute("id", CommUtil.getUUID());
		return "document/bookAdd";
	}

	@RequestMapping("/showBookEdit.do")
	public String doBookEdit(HttpServletRequest request, Model model,
			@RequestParam String id) {
		Data data = dataService.selectById(id);
		model.addAttribute("data", data);
		model.addAttribute("pname", this.dataService.getNameById(data.getPid()));
		return "document/bookEdit";
	}
	
	@RequestMapping("/showBookView.do")
	public String doBookView(HttpServletRequest request, Model model,
			@RequestParam String id) {
		Data data = dataService.selectById(id);
		model.addAttribute("data", data);
		model.addAttribute("pname", this.dataService.getNameById(data.getPid()));
		return "document/bookView";
	}

	@RequestMapping("/saveBook.do")
	public ModelAndView doBookSave(HttpServletRequest request, Model model,
			@ModelAttribute Data t) {
		// t.setId(CommUtil.getUUID());
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"作业指导书编号重复\"}");
			return new ModelAndView("result");
		}
		User cu = (User) request.getSession().getAttribute("cu");
		t.setInsuser(cu.getId());
		t.setInsdt(CommUtil.nowDate());
		int result = this.dataService.save(t);
		model.addAttribute("result","{\"res\":\""+result+"\",\"id\":\""+t.getId()+"\"}");
		return new ModelAndView("result");
	}

	@RequestMapping("/updateBook.do")
	public ModelAndView doBookUpdate(HttpServletRequest request, Model model,
			@ModelAttribute Data t) {
		if(!this.dataService.checkNotOccupied(t.getId(),t.getNumber(),t.getDoctype())){
			model.addAttribute("result", "{\"res\":\"作业指导书编号重复\"}");
			return new ModelAndView("result");
		}
		User cu = (User) request.getSession().getAttribute("cu");
		t.setUpdateuser(cu.getId());
		t.setUpdatedt(CommUtil.nowDate());
		int result = this.dataService.update(t);
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

	@RequestMapping("/deleteBook.do")
	public ModelAndView doBookDelete(HttpServletRequest request, Model model,
			@RequestParam(value = "id") String id) throws IOException {
		String mappernamespace = "document.DocFileMapper";
		int result = this.dataService.deleteById(id);
		List<CommonFile> commfile = this.commonFileService.selectByMasterId(id,
				mappernamespace);
		int res = this.commonFileService.deleteByMasterId(id, mappernamespace);
		if (res > 0) {
			for (int i = 0; i < commfile.size(); i++) {
				FileUtil.deleteFile(commfile.get(i).getAbspath());
			}
		}
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

	@RequestMapping("/doimport.do")
	public ModelAndView doimport(@RequestParam MultipartFile[] file,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException {
		// 要存入的实际地址
		String realPath = request.getSession().getServletContext()
				.getRealPath("/");
		String pjName = request.getContextPath().substring(1,
				request.getContextPath().length());
		realPath = realPath.replace(pjName, "Temp");
		String result = "";
		model.addAttribute("result", result);
		return new ModelAndView("result");
	}

}// end
