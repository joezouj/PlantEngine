package com.sipai.controller.base;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sipai.entity.base.CommonFile;
import com.sipai.entity.user.User;
import com.sipai.service.base.CommonFileService;
import com.sipai.tools.FileUtil;

@Controller
@RequestMapping(value = "/base")
public class FileUploadHelper {
	@Resource
	CommonFileService commonFileService;
	
	/**
	 * 显示上传页面
	 * @param request
	 * @param response
	 * @param model
	 * @param masterid
	 * @param mappernamespace
	 * @return
	 */
	@RequestMapping(value = "fileupload.do", method = RequestMethod.GET)
	public String fileupload(HttpServletRequest request,
			HttpServletResponse response,Model model,@RequestParam(value="masterid") String masterid,
			@RequestParam(value="mappernamespace") String mappernamespace) {
		model.addAttribute("mappernamespace",mappernamespace);
		model.addAttribute("masterid",masterid);
		return "base/fileupload";
	}
	
	/**
	 * 上传文件
	 * @param file
	 * @param request
	 * @param response
	 * @param model
	 * @param masterid
	 * @param mappernamespace
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadfile.do")
	public ModelAndView uploadFile(@RequestParam MultipartFile[] file, HttpServletRequest request,
			HttpServletResponse response,Model model,@RequestParam(value="masterid") String masterid,
			@RequestParam(value="mappernamespace") String mappernamespace) throws IOException {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		String pjName = request.getContextPath().substring(1, request.getContextPath().length());
		realPath = realPath.replace(pjName,"UploadFile");

    	User user = (User)request.getSession().getAttribute("cu");
    	String result = this.commonFileService.uploadFile(realPath, mappernamespace, masterid, user, file);
		model.addAttribute("result",result);
    	return new ModelAndView("result");
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @param mappernamespace
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "downloadfile.do")
	public ModelAndView downloadfile(HttpServletRequest request,
			HttpServletResponse response,Model model,@RequestParam(value="id") String id,
			@RequestParam(value="mappernamespace") String mappernamespace) throws IOException {
		CommonFile commfile = this.commonFileService.selectById(id, mappernamespace);
		FileUtil.downloadFile(response, commfile.getFilename(), commfile.getAbspath());
    	return null;
	}
	
	/**
	 * 删除文件
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @param mappernamespace
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "deletefile.do")
	public ModelAndView deletefile(HttpServletRequest request,
			HttpServletResponse response,Model model,@RequestParam(value="id") String id,
			@RequestParam(value="mappernamespace") String mappernamespace) throws IOException {
		CommonFile commfile = this.commonFileService.selectById(id, mappernamespace);
		int res = this.commonFileService.deleteById(id, mappernamespace);
		if(res>0){
			FileUtil.deleteFile(commfile.getAbspath());
			model.addAttribute("result", "删除成功");
		}
    	return new ModelAndView("result");
	}
	
	/**
	 * 获取主ID下的文件列表
	 * @param request
	 * @param response
	 * @param model
	 * @param masterid
	 * @param mappernamespace
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getFileList.do")
	public ModelAndView getFileList(HttpServletRequest request,
			HttpServletResponse response,Model model,@RequestParam(value="masterid") String masterid,
			@RequestParam(value="mappernamespace") String mappernamespace) throws IOException {
		List<CommonFile> list = this.commonFileService.selectByMasterId(masterid, mappernamespace);
		JSONArray json=JSONArray.fromObject(list);
		model.addAttribute("result",json);
    	return new ModelAndView("result");
	}
}
