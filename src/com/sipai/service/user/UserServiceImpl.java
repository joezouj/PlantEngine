package com.sipai.service.user;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sipai.dao.user.UserDao;
import com.sipai.dao.user.UserJobDao;
import com.sipai.dao.user.UserTimeDao;
import com.sipai.entity.user.User;
import com.sipai.entity.user.UserJob;
import com.sipai.entity.user.UserTime;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;
	
	@Resource UserTimeDao userTimeDao;
	
	@Resource UserJobDao userJobDao;
	
	@Override
	public User getUserById(String userId) {
		return this.userDao.selectByPrimaryKey(userId);
	}
	
	@Override
	public int saveUser(User user) {
		user.setPassword(CommUtil.generatePassword(user.getPassword()));
		return this.userDao.insert(user);
	}
	
	@Override
	public int saveUserTime(User cu) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String edate = CommUtil.nowDate();
		java.util.Date begin;
		java.util.Date end;
		long between=0;
		try {
			begin = sdf.parse(cu.getLastlogintime());
			end = sdf.parse(edate);
			between=(end.getTime() - begin.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		
		Double val=Double.valueOf(df.format(between/3600000.00));
		
		UserTime userTime = new UserTime();
		userTime.setId(CommUtil.getUUID());
		userTime.setUserid(cu.getId());
		userTime.setIp(cu.getCurrentip());
		userTime.setSdate(cu.getLastlogintime());
		userTime.setEdate(edate);
		userTime.setLogintime(val);
		userTime.setInsuser(cu.getId());
		userTime.setInsdt(CommUtil.nowDate());
		
		return this.userTimeDao.insert(userTime);
		
	}
	
	@Override
	public int updateUserById(User user) {
		return this.userDao.updateByPrimaryKeySelective(user);
	}
	
	@Override
	public int updateTotaltimeByUserId(String userId) {
		User user = new User();
		user.setId(userId);
		user.setTotaltime(userTimeDao.getTotaltimeByUserId(userId));
		return this.userDao.updateByPrimaryKeySelective(user);
	}

	@Override
	public int resetpassword(User user,String newpwd) {
		newpwd= CommUtil.generatePassword(newpwd);
		user.setPassword(newpwd);
		return this.userDao.updateByPrimaryKeySelective(user);
	}
	
	@Override
	public List<User> selectListByWhere(String wherestr) {
		User user= new User();
		user.setWhere(wherestr);
		return this.userDao.selectListByWhere(user);
	}

	@Override
	public List<User> selectList() {
		User user = new User();
		return this.userDao.selectList(user);
	}

	@Override
	public int deleteUserById(String userId) {
		return this.userDao.deleteByPrimaryKey(userId);
	}

	@Override
	public int deleteUserByWhere(String whereStr) {
		User user = new User();
		user.setWhere(whereStr);
		return this.userDao.deleteByWhere(user);
	}
	
	@Override
	public List<User> getUserListByPid(String pid) {
		User user= new User();
		user.setWhere("where pid ='"+pid+"'");
		return this.userDao.selectListByWhere(user);
	}

	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	@Override
	public boolean checkNotOccupied(String id, String name) {
		List<User> list = this.userDao.getListByLoginName(name);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(User user :list){
					if(!id.equals(user.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		
		return true;
	}

	@SuppressWarnings("resource")
	@Override
	public String doimport(String realPath, MultipartFile[] file) throws IOException {  
        String result = "";
        int suc = 0;
	    int fal = 0;
	    //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        //服务器路径
        String serverPath = null;
        ArrayList<User> list = new ArrayList<User>();
        for(MultipartFile myfile:file){
        	 if(myfile.isEmpty()){
                 return null;  
             }else{
            	 originalFilename = myfile.getOriginalFilename();
            	 //兼容linux路径
            	 serverPath = realPath+System.getProperty("file.separator")+CommUtil.getUUID()+originalFilename;
            	 FileUtil.saveFile(myfile.getInputStream(), serverPath);
            	 System.out.println("-->>临时文件上传完成！");
            	 
 	           	FileInputStream is = new FileInputStream(serverPath);
 				try {
 					POIFSFileSystem fs  = new POIFSFileSystem(is);
 					HSSFWorkbook wb = new HSSFWorkbook(fs);
 					HSSFSheet sheet = wb.getSheetAt(0);
 			        // 得到总行数
 			        int rowNum = sheet.getPhysicalNumberOfRows();
 			        HSSFRow row = sheet.getRow(0);
 			        //以导入时列名长度为需要导入数据的长度，超过部分程序将忽略
 			        int colNum = row.getPhysicalNumberOfCells();
 			        // 正文内容应该从第二行开始,第一行为表头的标题
 			        
 			        for (int i = 1; i < rowNum; i++) {
 			        	row = sheet.getRow(i);
 			        	User user = new User();
 			        	
 			    		String UUID = CommUtil.getUUID();
 			        	user.setId(UUID);
 			        	user.setActive("1");
 			        	int j = 0;
 			            while (j < colNum) {
 			            	if(j==0){
 			            		user.setCaption(row.getCell(j).getStringCellValue());
 			            	}else if(j==1){
 			            		user.setName(row.getCell(j).getStringCellValue());
 			            	}else if(j==2){
 			            		String sex = row.getCell(j).getStringCellValue();
 			            		if(sex!=null){
 			            			if(sex.equals("男")){
 			            				sex = "1";
 			            			}else if(sex.equals("女")){
 			            				sex = "0";
 			            			}else{
 			            				
 			            			}
 			            		}
 			            		user.setSex(sex);
 			            	}else if(j==3){
 			            		user.setTotaltime(row.getCell(j).getNumericCellValue());
 			            	}else{}
 			
 			                j++;
 			            }
 			        	int res = this.saveUser(user);
 			        	user = null;
 			        	if(res>0){
 			        		suc++;
 			        		list.add(user);
 			        	}else{
 			        		fal++;
 			        	}
 			        }
 			        //导入动作完成后，删除导入文件的临时文件
 			       FileUtil.deleteFile(serverPath);
 			       System.out.println("<<--临时文件已删除！");
 			        //关闭流文件
 			        is.close();
 			    } catch (IOException e) {
 			        e.printStackTrace();
 			    }
             }
        }
        int totalNum =  suc+fal;
        String feedback = "共导入"+totalNum+"条数据：";
        if(suc>0){
        	feedback += "导入成功"+suc+"条！";
        }
        if(fal>0){
        	feedback += "导入失败"+fal+"条！";
        }
 		result="{\"feedback\":\""+feedback+"\"}";
 		
 		return result;
	}

	@Override
	public String exportUsers(String filePath, List<User> list) throws IOException {
		String result="";
		String title = "人员导出测试";
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 生成一个表格
	    HSSFSheet sheet = workbook.createSheet(title);
	    // 设置表格默认列宽度为15个字节
	    sheet.setDefaultColumnWidth(15);
	    // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	    font.setColor(HSSFColor.VIOLET.index);
	    font.setFontHeightInPoints((short) 12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    // 把字体应用到当前的样式
	    style.setFont(font);
	    // 生成并设置另一个样式
	    HSSFCellStyle style2 = workbook.createCellStyle();
	    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    // 生成另一个字体
	    HSSFFont font2 = workbook.createFont();
	    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	    // 把字体应用到当前的样式
	    style2.setFont(font2);
	     
	    // 声明一个画图的顶级管理器
	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	    // 定义注释的大小和位置,详见文档
	    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	    // 设置注释内容
	    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	    comment.setAuthor("leno");
	 
	    //产生表格标题行
	    String[] headers = {"姓名","登录名","性别","在线时长"};
	    HSSFRow row = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	    	HSSFCell cell = row.createCell(i);
	    	cell.setCellStyle(style);
	    	HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	    	cell.setCellValue(text);
	    }
	 
	    //遍历集合数据，产生数据行
	    for(int i = 0;i<list.size();i++){
	    	row = sheet.createRow(i+1);
	    	User user = list.get(i);
	    	//填充列数据，若果数据有不同的格式请注意匹配转换
	        HSSFCell cell0 = row.createCell(0);	    
		    cell0.setCellValue(user.getCaption());
		    
		    HSSFCell cell1 = row.createCell(1);  
		    cell1.setCellValue(user.getName());
		    
		    HSSFCell cell2 = row.createCell(2);
		    String sex = "";
		    if(user.getSex()!=null){
		    	if(user.getSex().equals("0")){
			    	sex = "女";
			    }else if(user.getSex().equals("1")){
			    	sex = "男";
			    }else{
			    	sex = "未知";
			    }
		    }
		    cell2.setCellValue(sex);
		    
		    HSSFCell cell3 = row.createCell(3);
		    Double totalTime = 0.0;
		    if(user.getTotaltime()!=null){
		    	totalTime  = user.getTotaltime();
		    }	
		    cell3.setCellValue(totalTime);
	        
	    }
	     try {
	    	File file = new File(filePath);
	    	OutputStream out = new FileOutputStream(file) ;
	    	workbook.write(out);
	    	out.close();
	    	result = "文件导出成功："+filePath;
	     } catch (IOException e) {
	        e.printStackTrace();
	     } finally{
	    	 workbook.close();
	     }
		return result;
	}
	
	@Override
	public void exportUsersByResponse(HttpServletResponse response,String filename,List<User> list) throws IOException {
		String title = "人员导出测试";
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 生成一个表格
	    HSSFSheet sheet = workbook.createSheet(title);
	    // 设置表格默认列宽度为15个字节
	    sheet.setDefaultColumnWidth(15);
	    // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
	    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	    font.setColor(HSSFColor.VIOLET.index);
	    font.setFontHeightInPoints((short) 12);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    // 把字体应用到当前的样式
	    style.setFont(font);
	    // 生成并设置另一个样式
	    HSSFCellStyle style2 = workbook.createCellStyle();
	    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
	    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    // 生成另一个字体
	    HSSFFont font2 = workbook.createFont();
	    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	    // 把字体应用到当前的样式
	    style2.setFont(font2);
	     
	    // 声明一个画图的顶级管理器
	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	    // 定义注释的大小和位置,详见文档
	    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	    // 设置注释内容
	    comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	    comment.setAuthor("leno");
	 
	    //产生表格标题行
	    String[] headers = {"姓名","登录名","性别","在线时长"};
	    HSSFRow row = sheet.createRow(0);
	    for (int i = 0; i < headers.length; i++) {
	    	HSSFCell cell = row.createCell(i);
	    	cell.setCellStyle(style);
	    	HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	    	cell.setCellValue(text);
	    }
	 
	    //遍历集合数据，产生数据行
	    for(int i = 0;i<list.size();i++){
	    	row = sheet.createRow(i+1);
	    	User user = list.get(i);
	    	//填充列数据，若果数据有不同的格式请注意匹配转换
	        HSSFCell cell0 = row.createCell(0);	    
		    cell0.setCellValue(user.getCaption());
		    
		    HSSFCell cell1 = row.createCell(1);  
		    cell1.setCellValue(user.getName());
		    
		    HSSFCell cell2 = row.createCell(2);
		    String sex = "";
		    if(user.getSex()!=null){
		    	if(user.getSex().equals("0")){
			    	sex = "女";
			    }else if(user.getSex().equals("1")){
			    	sex = "男";
			    }else{
			    	sex = "未知";
			    }
		    }
		    cell2.setCellValue(sex);
		    
		    HSSFCell cell3 = row.createCell(3);
		    Double totalTime = 0.0;
		    if(user.getTotaltime()!=null){
		    	totalTime  = user.getTotaltime();
		    }	
		    cell3.setCellValue(totalTime);
	        
	    }
	     try {
	    	response.reset();
			response.setContentType("application/x-msdownload");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(filename, "UTF-8"));
			System.out.println(response.getHeader("Content-disposition"));
	    	OutputStream out=response.getOutputStream();
	    	workbook.write(out);
	    	out.flush();
	    	out.close();
	    	
	     } catch (IOException e) {
	        e.printStackTrace();
	        	System.out.println(e.toString());
	        
	     } finally{
	    	 workbook.close();
	     }
	}

	@Override
	public int updateJobByUserid(String jobstr, String userid, String unitid) {
		int res=0;
		UserJob userJob= new UserJob();
		userJob.setWhere(" where userid='"+userid+"' ");
		int delres=userJobDao.deleteByWhere(userJob);
		if(delres >= 0){
			String[] jobs=jobstr.split(",");
			for(int i=0;i<jobs.length;i++){
				if(!jobs[i].equals("")){
					UserJob userJob1= new UserJob();
					userJob1.setUserid(userid);
					userJob1.setJobid(jobs[i]);
					userJob1.setUnitid(unitid);
					res+=userJobDao.insert(userJob1);
				}
			}
		}
		return res;
	}

	@Override
	public boolean checkSerialNotOccupied(String id, String serial) {
		List<User> list = this.userDao.getListBySerial(serial);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(User user :list){
					if(!id.equals(user.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean checkCardidNotOccupied(String id, String cardid) {
		List<User> list = this.userDao.getListByCardid(cardid);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(User user :list){
					if(!id.equals(user.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
