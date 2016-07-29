package com.sipai.service.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sipai.entity.user.User;

public interface UserService {
	public User getUserById(String userId);
	
	public int saveUser(User user);
	
	public int updateUserById(User user);
	
	public int deleteUserById(String userId);
	
	public int deleteUserByWhere(String whereStr);
	
	public int resetpassword(User user,String pwd);

	public List<User> selectList();

	public List<User> selectListByWhere(String wherestr);

	public boolean checkNotOccupied(String id, String name);
	
	public String doimport(String realPath, MultipartFile[] file) throws IOException;
	
	public String exportUsers(String filePath,List<User> list) throws IOException;
	
	public void exportUsersByResponse(HttpServletResponse response,String filename,List<User> list) throws IOException;

	public List<User> getUserListByPid(String pid);

	public int saveUserTime(User cu);

	public int updateTotaltimeByUserId(String userId);
	
	/**
	 * 根据用户id，更新用户所在部门的职位
	 * @param userId
	 * @return
	 */
	public int updateJobByUserid(String jobstr,String userid,String unitid);

	public boolean checkSerialNotOccupied(String id, String serial);

	public boolean checkCardidNotOccupied(String id, String cardid);

}
