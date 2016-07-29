package com.sipai.service.base;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sipai.dao.base.CommonFileDao;
import com.sipai.entity.base.CommonFile;
import com.sipai.entity.user.User;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;
@Service
public class CommonFileServiceImpl implements CommonFileService {
	@Resource
	private CommonFileDao commonFileDao;
	
	@Override
	public CommonFile selectById(String id, String mappernamespace) {
		this.commonFileDao.setMappernamespace(mappernamespace);
		return this.commonFileDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<CommonFile> selectByMasterId(String masterid, String mappernamespace) {
		this.commonFileDao.setMappernamespace(mappernamespace);
		return this.commonFileDao.selectByMasterId(masterid);
	}

	@Override
	public int saveCommonFile(CommonFile commonFile,String mappernamespace) {
		this.commonFileDao.setMappernamespace(mappernamespace);
		return this.commonFileDao.insert(commonFile);
	}
	
	@Override
	public int deleteById(String id, String mappernamespace) {
		this.commonFileDao.setMappernamespace(mappernamespace);
		return this.commonFileDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public int deleteByMasterId(String masterid, String mappernamespace) {
		this.commonFileDao.setMappernamespace(mappernamespace);
		return this.commonFileDao.deleteByMasterId(masterid);
	}
	
	/**
	 * 上传文件到服务器并保存到数据库
	 * @param realPath 服务器保存路径
	 * @param masterid 附件归属业务对象id
	 * @param user 上传者、操作者
	 * @param file 文件对象
	 * @return CommonFileDao实例
	 */
	@Override
	public String uploadFile(String realPath, String mappernamespace, String masterid, User user,MultipartFile[] file) throws IOException {
		if(masterid.equals("")){
			return "{\"feedback\":\"MasterId不得为空\"}";
		}
		String result = "";
        String feedback = "";
        int filecount = 0; 
        String originalFilename = null;
        String serverPath = null;
        CommonFile commonFile = new CommonFile();
        for(MultipartFile myfile:file){
        	 if(myfile.isEmpty()){ 
        		 feedback = "请选择文件后上传!";
             }else{
            	 String id=CommUtil.getUUID();
            	 originalFilename = myfile.getOriginalFilename();
            	 //兼容linux路径
            	 serverPath = realPath+System.getProperty("file.separator")+commonFileDao.getMappernamespace()+"\\\\"+id+"_"+originalFilename;
            	 commonFile.setId(id);
            	 commonFile.setMasterid(masterid);
            	 commonFile.setFilename(originalFilename);
            	 commonFile.setAbspath(serverPath);
            	 commonFile.setSize(Integer.valueOf(String.valueOf(myfile.getSize())));
            	 if(user!=null){
            		 commonFile.setInsuser(user.getId());
            	 }
            	 commonFile.setInsdt(CommUtil.nowDate());
            	 commonFile.setType(originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length()).toUpperCase());
            	 int saveResult = this.saveCommonFile(commonFile, mappernamespace);
            	 if(saveResult>0){
            		 System.out.println("文件上传中......");
                	 FileUtil.saveFile(myfile.getInputStream(), serverPath);
                	 System.out.println("文件上传完成！");
                	 filecount++;
            	 }else{
            		 feedback = "MapperNamespace有误！";
            	 }
             }
        }
        if(filecount>0){
        	feedback = "成功上传"+filecount+"个文件！";
        }
    	result="{\"feedback\":\""+feedback+"\"}";
    	return result;
	}

}
