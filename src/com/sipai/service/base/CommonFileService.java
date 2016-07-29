package com.sipai.service.base;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sipai.entity.base.CommonFile;
import com.sipai.entity.user.User;

public interface CommonFileService {
	public CommonFile selectById(String id, String mappernamespace);

	public List<CommonFile> selectByMasterId(String masterid, String mappernamespace);
	
	public int saveCommonFile(CommonFile commonFile, String mappernamespace);
	
	public int deleteById(String id, String mappernamespace);
	
	public int deleteByMasterId(String masterid, String mappernamespace);
	
	public String uploadFile(String realPath,String mappernamespace, String masterid,User user,MultipartFile[] file) throws IOException;

}
