package com.sipai.service.material;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

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
import org.springframework.web.multipart.MultipartFile;

import com.sipai.dao.material.MaterialBOMDao;
import com.sipai.entity.material.MaterialBOM;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.material.MaterialUnit;
import com.sipai.entity.user.User;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;
@Service
public class MaterialBOMService implements CommService<MaterialBOM>{
	@Resource
	private MaterialBOMDao materialBOMDao;
	@Resource
	private MaterialInfoService materialInfoService;
	@Resource
	private MaterialUnitService materialUnitService;
	@Override
	public MaterialBOM selectById(String id) {
		return materialBOMDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialBOMDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialBOM materialBOM) {
		return materialBOMDao.insert(materialBOM);
	}

	@Override
	public int update(MaterialBOM materialBOM) {
		return materialBOMDao.updateByPrimaryKeySelective(materialBOM);
	}

	@Override
	public List<MaterialBOM> selectListByWhere(String wherestr) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setWhere(wherestr);
		return materialBOMDao.selectListByWhere(materialBOM);
	}
	
	@Override
	public int deleteByWhere(String wherestr) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setWhere(wherestr);
		return materialBOMDao.deleteByWhere(materialBOM);
	}	
	
	public List<MaterialBOM> selectListByPid(String pid) {
		return materialBOMDao.selectListByPid(pid);
	}
	
	private List<MaterialBOM> selectRootByMaterialcode(String materialcode) {
		
		return materialBOMDao.getRootByMaterialcode(materialcode);
	}
/*	
	public List<MaterialBOM> selectListByPid(String pid) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setPid(pid);
		return materialBOMDao.selectListByPid(materialBOM);
	}*/
	
	public List<MaterialBOM> selectTreeListByID(String id) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setId(id);
		return materialBOMDao.selectTreeListByID(materialBOM);
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @param materialCode
	 * @return
	 */
	public boolean checkRootNotOccupied(String materialCode,Integer version) {
		List<MaterialBOM> list = this.materialBOMDao.getRootByCodeAndVersion(materialCode,version);
		if(list!=null && list.size()>0){
			return false;
		}
		return true;
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @param materialCode
	 * @return
	 */
	public boolean checkChildNotOccupied(String pid,String materialCode,Integer version) {
		List<MaterialBOM> list = this.materialBOMDao.getChildByCodeAndVersion(pid,materialCode,version);
		if(list!=null && list.size()>0){
			return false;
		}
		return true;
	}
	
	/**
	 * 根据物料编号和版本信息获取BOM节点信息
	 * @param materialCode
	 * @param materialCode
	 * @return
	 */
	public MaterialBOM getBomNode(String materialCode,Integer version) {
		List<MaterialBOM> list = this.materialBOMDao.getRootByCodeAndVersion(materialCode,version);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
/*
	public String doimport(String realPath, MultipartFile[] file,User operator) throws IOException {  
        String result = "";
        String feedback = "";
        int suc = 0;
	    int fal = 0;
	    //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        //服务器路径
        String serverPath = null;
        ArrayList<MaterialInfo> list = new ArrayList<MaterialInfo>();
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
 			        //获取BOM节点
 			        MaterialBOM bomroot = new MaterialBOM();
 			        int rootres = 0;
 			        if(rowNum>2){
 			        	row = sheet.getRow(1);
 			    		String UUID = CommUtil.getUUID();
 			    		bomroot.setId(UUID);
 			    		bomroot.setPid("-1");
 			        	int j = 1;
 			            while (j < colNum) {
 			            	switch(j){
	 			            	case 1:
	 			            		if(row.getCell(j)!=null){
	 			            			int ordernumber = (int)row.getCell(j).getNumericCellValue();
	 				            		bomroot.setOrdernumber(ordernumber);
	 			            		}else{
	 			            			bomroot.setOrdernumber(0);
	 			            		}
	 			            		break;
	 			            	case 2:
	 			            		if(row.getCell(j)!=null){
	 			            			String materialcode = row.getCell(j).toString();
		 			            		//检查物料数据库，没有则新增一条*
		 			            		MaterialInfo materialInfo = materialInfoService.getMaterialInfoByCode(materialcode);
		 			            		if(materialInfo.getMaterialcode()==null){
		 			            			MaterialInfo mInfo = new MaterialInfo();
		 			            			String mid = CommUtil.getUUID();
		 			            			mInfo.setId(mid);
		 			            			mInfo.setMaterialcode(materialcode);
		 			            			String materialname = row.getCell(3).getStringCellValue();
		 			            			mInfo.setMaterialname(materialname);
		 			            			mInfo.setInsdt(CommUtil.nowDate());
		 			            			mInfo.setInsuser(operator.getId());
		 			            			mInfo.setModify(operator.getId());
		 			            			mInfo.setModifydt(CommUtil.nowDate());
		 			            			int bomRes = materialInfoService.save(mInfo);
		 			            			if(bomRes==1){
		 			            				bomroot.setMaterialcode(mInfo.getMaterialcode());
		 			            			}
		 			            		}else{
		 			            			bomroot.setMaterialcode(materialInfo.getMaterialcode());
		 			            		}
	 			            		}
	 			            		break;
	 			            	case 3:
	 			            		bomroot.setMaterialname(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 4:
	 			            		if(row.getCell(j)!=null){
	 			            			Double num = row.getCell(j).getNumericCellValue();
		 			            		bomroot.setNum(num);
	 			            		}else{
	 			            			bomroot.setNum(1.0);
	 			            		}
	 			            		break;
	 			            	case 5:
	 			            		if(row.getCell(j)!=null){
	 			            			int version = (int) row.getCell(j).getNumericCellValue();
		 			            		bomroot.setVersion(version);
	 			            		}else{
	 			            			bomroot.setVersion(0);
	 			            		}
	 			            		break;
	 			            	case 6:
	 			            		bomroot.setRemark(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
 			            	}
 			                j++;
 			            }
 			            bomroot.setInsdt(CommUtil.nowDate());
 			            bomroot.setInsuser(operator.getId());
 			            bomroot.setModifydt(CommUtil.nowDate());
 			            bomroot.setModify(operator.getId());
 			            
 			            if(bomroot.getMaterialcode()!=null&&!bomroot.getMaterialcode().equals("")){
 			            	if(this.checkRootNotOccupied(bomroot.getMaterialcode(),bomroot.getVersion())){
 			            		rootres = this.save(bomroot);
 	 			            }
 			            }
 			        }
 			        if(rootres>0){
		        		feedback += bomroot.getMaterialname()+" BOM导入成功！";
		        		// 正文内容应该从第二行开始,第一行为表头的标题
	 			        for (int i = 2; i < rowNum; i++) {
	 			        	row = sheet.getRow(i);			        	
	 			        	MaterialBOM bom = new MaterialBOM();
	 			    		String UUID = CommUtil.getUUID();
	 			    		bom.setId(UUID);
	 			    		bom.setPid(bomroot.getId());
	 			        	int j = 1;
	 			            while (j < colNum) {
	 			            	switch(j){
	 			            	case 1:
	 			            		if(row.getCell(j)!=null){
	 			            			int ordernumber = (int)row.getCell(j).getNumericCellValue();
	 			            			bom.setOrdernumber(ordernumber);
	 			            		}else{
	 			            			bom.setOrdernumber(0);
	 			            		}
	 			            		break;
	 			            	case 2:
	 			            		if(row.getCell(j)!=null){
	 			            			String materialcode = row.getCell(j).toString();
		 			            		//检查物料数据库，没有则新增一条*
		 			            		MaterialInfo materialInfo = materialInfoService.getMaterialInfoByCode(materialcode);
		 			            		if(materialInfo.getMaterialcode()==null){
		 			            			MaterialInfo mInfo = new MaterialInfo();
		 			            			String mid = CommUtil.getUUID();
		 			            			mInfo.setId(mid);
		 			            			mInfo.setMaterialcode(materialcode);
		 			            			String materialname = row.getCell(3).getStringCellValue();
		 			            			mInfo.setMaterialname(materialname);
		 			            			mInfo.setInsdt(CommUtil.nowDate());
		 			            			mInfo.setInsuser(operator.getId());
		 			            			mInfo.setModify(operator.getId());
		 			            			mInfo.setModifydt(CommUtil.nowDate());
		 			            			int mRes = materialInfoService.save(mInfo);
		 			            			if(mRes==1){
		 			            				bom.setMaterialcode(mInfo.getMaterialcode());
		 			            			}
		 			            		}else{
		 			            			bom.setMaterialcode(materialInfo.getMaterialcode());
		 			            		}
	 			            		}
	 			            		break;
	 			            	case 3:
	 			            		bom.setMaterialname(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 4:
	 			            		if(row.getCell(j)!=null){
	 			            			Double num = row.getCell(j).getNumericCellValue();
	 			            			bom.setNum(num);
	 			            		}else{
	 			            			bom.setNum(1.0);
	 			            		}
	 			            		break;
	 			            	case 5:
	 			            		if(row.getCell(j)!=null){
	 			            			int version = (int) row.getCell(j).getNumericCellValue();
	 			            			bom.setVersion(version);
	 			            		}else{
	 			            			bom.setVersion(0);
	 			            		}
	 			            		break;
	 			            	case 6:
	 			            		bom.setRemark(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	}
	 			                j++;
	 			            }
	 			            bom.setInsdt(CommUtil.nowDate());
	 			            bom.setInsuser(operator.getId());
	 			            bom.setModifydt(CommUtil.nowDate());
	 			            bom.setModify(operator.getId());
	 			            int res = this.save(bom);
	 			        	bom = null;
	 			        	if(res>0){
	 			        		suc++;
	 			        	}else{
	 			        		fal++;
	 			        	}
	 			        }
		        	}else{
		        		feedback += bomroot.getMaterialname()+" BOM已存在！";
		        	}
 			        
 			        bomroot = null;
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
        feedback += "共导入"+totalNum+"条数据：";
        if(suc>0){
        	feedback += "导入成功"+suc+"条！";
        }
        if(fal>0){
        	feedback += "导入失败"+fal+"条！";
        }
 		result="{\"feedback\":\""+feedback+"\"}";
 		
 		return result;
	}
*/
	public String importByExcel(String realPath, MultipartFile[] file,User operator) throws IOException {  
        String result = "";
        String feedback = "";
        int suc = 0;
	    int fal = 0;
	    int reuse = 0;
	    //上传文件的原名(即上传前的文件名字)
        String originalFilename = null;
        //服务器路径
        String serverPath = null;
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
 			        	//Excel行数据
 			        	row = sheet.getRow(i);	
 			        	//BOM父物料信息
 	 			        MaterialBOM pbom = new MaterialBOM();
 	 			        //BOM子物料信息
 	 			        MaterialBOM cbom = new MaterialBOM();
 			        	int j = 0;
 			            while (j < colNum) {
 			            	switch(j){
	 			            	case 0:
	 			            		pbom.setMaterialcode(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 1:
	 			            		pbom.setMaterialname(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 2:
	 			            		if(row.getCell(j)!=null){
	 			            			int version = (int) row.getCell(j).getNumericCellValue();
	 			            			pbom.setVersion(version);
	 			            		}else{
	 			            			pbom.setVersion(0);
	 			            		}
	 			            		break;
	 			            	case 3:
	 			            		cbom.setMaterialcode(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 4:
	 			            		cbom.setMaterialname(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 5:
	 			            		if(row.getCell(j)!=null){
	 			            			int version = (int) row.getCell(j).getNumericCellValue();
	 			            			cbom.setVersion(version);
	 			            		}else{
	 			            			cbom.setVersion(0);
	 			            		}
	 			            		break;
	 			            	case 6:
	 			            		if(row.getCell(j)!=null){
	 			            			Double num = row.getCell(j).getNumericCellValue();
	 			            			cbom.setNum(num);
	 			            		}else{
	 			            			cbom.setNum(1.0);
	 			            		}
	 			            		break;
	 			            	case 7:
	 			            		cbom.setUnit(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
	 			            	case 8:
	 			            		cbom.setRemark(row.getCell(j)==null?null:row.getCell(j).toString());
	 			            		break;
 			            	}
 			                j++;
 			            }
 			            //检查单位数据库，没有则新增一条
 			            if(cbom.getUnit()!=null&&!cbom.getUnit().equals("")){
 			            	List<MaterialUnit> unitlist=materialUnitService.selectListByWhere(" where unit='"+row.getCell(7).toString()+"' order by insdt");
 		            		if(unitlist.isEmpty()){
 		            			MaterialUnit materialunit=new MaterialUnit(); 			            			
 		            			materialunit.setId(CommUtil.getUUID());
 		            			materialunit.setUnit(row.getCell(j).toString());
 		            			materialunit.setStatus("1");
 		            			materialunit.setRemark("【物料导入自动生成】");
 		            			materialunit.setInsdt(CommUtil.nowDate());
 		            			materialunit.setInsuser(operator.getId());
 		            			materialunit.setModify(operator.getId());
 		            			materialunit.setModifydt(CommUtil.nowDate());
 		            			materialUnitService.save(materialunit);
 		            		}
 			            }
 			            //检查物料数据库，没有则新增一条*
		            	if(cbom.getMaterialcode()!=null&&!cbom.getMaterialcode().equals("")){
	            		String materialcode = cbom.getMaterialcode();
		            		MaterialInfo materialInfo = materialInfoService.getMaterialInfoByCode(materialcode);
		            		if(materialInfo.getMaterialcode()==null){
		            			MaterialInfo mInfo = new MaterialInfo();
		            			String mid = CommUtil.getUUID();
		            			mInfo.setId(mid);
		            			mInfo.setMaterialcode(materialcode);
		            			String materialname = cbom.getMaterialname();
		            			mInfo.setMaterialname(materialname);
		            			mInfo.setInsdt(CommUtil.nowDate());
		            			mInfo.setInsuser(operator.getId());
		            			mInfo.setModify(operator.getId());
		            			mInfo.setModifydt(CommUtil.nowDate());
		            			materialInfoService.save(mInfo);
		            		}
		            	}
		            	//检查BOM数据库，没有则新增一条*
		            	if(pbom.getMaterialcode()!=null&&!pbom.getMaterialcode().equals("")){
		            		MaterialBOM mBom = this.getBomNode(pbom.getMaterialcode(),pbom.getVersion());
		            		if(mBom!=null){
		            			cbom.setPid(mBom.getId());
			            		mBom = null;
		            		}else{
					    		String UUID = CommUtil.getUUID();
					    		pbom.setId(UUID);
					    		pbom.setPid("-1");
					    		pbom.setOrdernumber(0);
					    		pbom.setNum(1.0);
					    		pbom.setInsdt(CommUtil.nowDate());
					    		pbom.setInsuser(operator.getId());
					    		pbom.setModifydt(CommUtil.nowDate());
					    		pbom.setModify(operator.getId());
					    		
					    		int pbomRes  =  this.save(pbom);
					    		if(pbomRes>0){
						    		cbom.setPid(UUID);
						    	}
		            		}
		            		//判断子物料信息是否已存在
		            		if(cbom.getPid()!=null&&!cbom.getPid().equals("")&&cbom.getMaterialcode()!=null&&!cbom.getMaterialcode().equals("")){
		            			if(this.checkChildNotOccupied(cbom.getPid(), cbom.getMaterialcode(), cbom.getVersion())){
		            				String UUID = CommUtil.getUUID();
						    		cbom.setId(UUID);
						    		cbom.setOrdernumber(0);
		            				cbom.setInsdt(CommUtil.nowDate());
			 			           	cbom.setInsuser(operator.getId());
			 			           	cbom.setModifydt(CommUtil.nowDate());
			 			           	cbom.setModify(operator.getId());
			 			           	//保存子物料进入BOM
			 			           	int res = this.save(cbom);
			 			        	cbom = null;
			 			        	if(res>0){
			 			        		suc++;
			 			        	}else{
			 			        		fal++;
			 			        	}
		            			}else{
		            				reuse++;
		            			}
		            		}
			            }
		            	pbom = null;
		            	cbom = null;
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
        feedback += "共导入"+totalNum+"条数据：";
        if(suc>0){
        	feedback += "导入成功"+suc+"条！";
        }
        if(reuse>0){
        	feedback += "重用数据"+reuse+"条！";
        }
        if(fal>0){
        	feedback += "导入失败"+fal+"条！";
        }
 		result="{\"feedback\":\""+feedback+"\"}";
 		
 		return result;
	}
	public void exportByResponse(HttpServletResponse response,
			String filename,String[] bomIds) throws IOException {
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 生成一个样式
	    HSSFCellStyle style = workbook.createCellStyle();
	    // 设置这些样式
	    style.setFillForegroundColor(HSSFColor.WHITE.index);
	    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    // 生成一个字体
	    HSSFFont font = workbook.createFont();
	    font.setColor(HSSFColor.BLACK.index);
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
	    int sheetIx=0;
	    for(String bomid:bomIds){
	    	MaterialBOM mBom = this.selectById(bomid);
	    	if(mBom!=null){
	    		// 生成一张表格
			    HSSFSheet sheet = workbook.createSheet();
			    workbook.setSheetName(sheetIx, mBom.getMaterialcode());
			    sheetIx++;
			    // 设置表格默认列宽度为15个字节
			    sheet.setDefaultColumnWidth(15);
			    // 声明一个画图的顶级管理器
			    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			    // 定义注释的大小和位置,详见文档
			    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
			    // 设置注释内容
			    comment.setString(new HSSFRichTextString("注释！"));
			    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
			    comment.setAuthor("SIPAI");	
		    	 //产生表格标题行
			    String[] headers = {"父物料代码","父物料名称","父版本","子物料代码","子物料名称","子版本","数量","单位","备注"};
			    HSSFRow row = sheet.createRow(0);
			    for (int i = 0; i < headers.length; i++) {
			    	HSSFCell cell = row.createCell(i);
			    	cell.setCellStyle(style);
			    	HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			    	cell.setCellValue(text);
			    }
			    List<MaterialBOM> mList = this.getBOMData(mBom.getId()); 
			    int rowCount = 1;
			    for(MaterialBOM bom:mList){
			    	MaterialBOM pbom = this.selectById(bom.getPid());
			    	row = sheet.createRow(rowCount);
			    	rowCount++;
			    	//填充列数据，若果数据有不同的格式请注意匹配转换
			        HSSFCell cell0 = row.createCell(0);	    
				    cell0.setCellValue(pbom==null?null:pbom.getMaterialcode());
				    
				    HSSFCell cell1 = row.createCell(1);  
				    cell1.setCellValue(pbom==null?null:pbom.getMaterialname());
				    
				    HSSFCell cell2 = row.createCell(2);  
				    cell2.setCellValue(pbom==null?null:pbom.getVersion());
				     
				    HSSFCell cell3 = row.createCell(3);
				    cell3.setCellValue(bom.getMaterialcode());
				    
				    HSSFCell cell4 = row.createCell(4);
				    cell4.setCellValue(bom.getMaterialname());
				    
				    HSSFCell cell5 = row.createCell(5);
				    cell5.setCellValue(bom.getVersion());
				    
				    HSSFCell cell6 = row.createCell(6);
				    cell6.setCellValue(bom.getNum());
				    
				    HSSFCell cell7 = row.createCell(7);
				    cell7.setCellValue(bom.getUnit());
				    
				    HSSFCell cell8 = row.createCell(8);
				    cell8.setCellValue(bom.getRemark());
			    	
			    }
	    	}
	    }
	   
	     try {
	    	response.reset();
			response.setContentType("application/x-msdownload");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(filename, "UTF-8"));
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
	
	/**
	 *获取Easyui树形结构数据
	 * @param id MaterialBOM id
	 * @return json 数据
	 */
	public String getBOMTreeGridData(String id) {
		StringBuffer treeGridDateString = new StringBuffer();
		treeGridDateString.append("{\"rows\":[");
		
		MaterialBOM materialBOM = this.selectById(id);		
		if(materialBOM!=null){
			JSONArray json = JSONArray.fromObject(materialBOM);
			String tempString = json.toString().substring(1, json.toString().length()-2)+",\"_num\":\""+materialBOM.getNum()+"\"},";
			treeGridDateString.append(tempString);
			getChildrenDates(materialBOM.getId(),materialBOM.getId(),materialBOM.getNum());
		}
		
		String dateStr = "";
		if(treeGridDates.length()>1){
			dateStr = treeGridDates.substring(0, treeGridDates.length()-1);
			treeGridDates.setLength(0);
			//_numStr = "";
			//totalNum = 1.0;
		}
		
		treeGridDateString.append(dateStr);
		treeGridDateString.append("]}");
		return treeGridDateString.toString();
	}
	
	/**
	 * getChildrenDates:获取对应父节点下子节点数据
	 * @Param parentId 树形的父节点 
	 * @param pid 真实的父节点
	 */
	private StringBuffer treeGridDates = new StringBuffer();
	//private String _numStr = "";
	//private Double totalNum = 1.0;
	public void getChildrenDates(String parentId,String pid,Double num) {
		//totalNum = num;		
		//_numStr += totalNum+"*";		
		getChildrenByPid(parentId,pid,num);
	}
	/**
	 *递归查询子节点
	 * 
	 */
	public void getChildrenByPid(String parentId,String pid,Double num) {
//		StringBuffer _num = new StringBuffer();
//		_num.append("("+num);

		String wherestr = "where pid='"+pid+"' order by materialcode ";
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setWhere(wherestr);
		List<MaterialBOM> list = materialBOMDao.selectListByWhere(materialBOM);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				//添加子元素｛obj｝
				String tempPid = findParent(list.get(i).getMaterialcode(),list.get(i).getVersion());
				Double tempNum = list.get(i).getNum();
//				tempNum *=num;
//				String _tempNumStr = tempNum+"";//tempNum+"("+tempNum+")";
				
				
				if(tempPid!=null&&!tempPid.equals("")){
					//_tempNumStr = tempNum+"("+_numStr.substring(0, _numStr.length()-1)+"*"+list.get(i).getNum()+")";
					String tempParentId = list.get(i).getId();
					getChildrenDates(tempParentId,tempPid,tempNum);
					
				}
				JSONArray json = JSONArray.fromObject(list.get(i));
				//,\"_num\":\""+_tempNumStr+"\"
				String tempString = json.toString().substring(1, json.toString().length()-2)+",\"_parentId\":\""+parentId+"\"},";
				treeGridDates.append(tempString);
			}
		}
	}

	private String findParent(String materialcode,Integer version) {
		MaterialBOM materialBOM = new MaterialBOM();
		List<MaterialBOM> list = materialBOMDao.getRootByCodeAndVersion(materialcode, version);
		if(list!=null&&list.size()>0){
			materialBOM = list.get(0);
			return materialBOM.getId().toString();
		}
		return null;
	}
	
	/**
	 *获取指定BOM下不重复的材料信息
	 * @param id MaterialBOM id
	 * @return materialcode 数据格式 'materialcode1','materialcode1','materialcode1'
	 */
	private Set<String> materialSet = new HashSet<String>();
	public Set<String> getUniqueBOMMaterials(String materialcode) {
		List<MaterialBOM> materialBOMList = this.selectRootByMaterialcode(materialcode);		
		if(materialBOMList!=null&&materialBOMList.size()>0){
			for(MaterialBOM mBOM:materialBOMList){
				getChildrenCodes(mBOM.getId(),mBOM.getId());
			}
		}
		return materialSet;
	}

	/**
	 * getChildrenCodes:获取对应父节点下子节点物料代码
	 * @Param parentId 树形的父节点 
	 * @param pid 真实的父节点
	 */
	public void getChildrenCodes(String parentId,String pid) {		
		getChildrenCodeByPid(parentId,pid);
	}
	/**
	 *递归查询子节点materialcode
	 * 
	 */
	public void getChildrenCodeByPid(String parentId,String pid) {
		String wherestr = "where pid='"+pid+"' order by materialcode ";
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setWhere(wherestr);
		List<MaterialBOM> list = materialBOMDao.selectListByWhere(materialBOM);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				//添加子元素｛obj｝
				String tempPid = findParent(list.get(i).getMaterialcode(),list.get(i).getVersion());
				if(tempPid!=null&&!tempPid.equals("")){
					String tempParentId = list.get(i).getId();
					getChildrenCodes(tempParentId,tempPid);
					
				}
				materialSet.add(list.get(i).getMaterialcode());
			}
		}
	}
	/**
	 *获取完整BOM数据
	 * @param id MaterialBOM id
	 * @return List<MaterialBOM> 数据（不包含BOM顶点）
	 */
	private List<MaterialBOM> bomList;
	public List<MaterialBOM> getBOMData(String id) {
		bomList = new ArrayList<MaterialBOM>();
		getChildrenBOMs(id);
		
		return bomList;
	}
	public void getChildrenBOMs(String pid) {
		List<MaterialBOM> list = materialBOMDao.selectListByPid(pid);
		int i = 0;
		for(MaterialBOM mBom:list){
			//添加子元素｛obj｝
			bomList.add(mBom);
			i++;
			String tempPid = findParent(mBom.getMaterialcode(),mBom.getVersion());
			if(tempPid!=null&&!tempPid.equals("")){
				getChildrenBOMs(tempPid);
			}
		}
	}

	public boolean checkNotOccupied(String id, String materialcode,
			Integer version) {
		List<MaterialBOM> list = this.materialBOMDao.getListByCodeAndVersion(materialcode,version);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialBOM mBom :list){
					if(!id.equals(mBom.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
}
