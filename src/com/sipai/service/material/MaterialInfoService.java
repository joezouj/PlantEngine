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

import com.sipai.dao.material.MaterialInfoDao;
import com.sipai.entity.document.Data;
import com.sipai.entity.material.MaterialInfo;
import com.sipai.entity.material.MaterialType;
import com.sipai.entity.material.MaterialUnit;
import com.sipai.entity.user.User;
import com.sipai.service.document.DataService;
import com.sipai.tools.CommService;
import com.sipai.tools.CommUtil;
import com.sipai.tools.FileUtil;
@Service
public class MaterialInfoService implements CommService<MaterialInfo>{
	@Resource
	private MaterialInfoDao materialInfoDao;
	@Resource
	private MaterialUnitService materialUnitService;
	@Resource
	private MaterialTypeService materialTypeService;
	@Resource
	private MaterialBOMService materialBOMService;
	@Resource
	private DataService dataService;
	@Override
	public MaterialInfo selectById(String id) {
		return materialInfoDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialInfoDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialInfo materialInfo) {
		return materialInfoDao.insert(materialInfo);
	}

	@Override
	public int update(MaterialInfo materialInfo) {
		return materialInfoDao.updateByPrimaryKeySelective(materialInfo);
	}

	@Override
	public List<MaterialInfo> selectListByWhere(String wherestr) {
		MaterialInfo materialInfo = new MaterialInfo();
		materialInfo.setWhere(wherestr);
		return materialInfoDao.selectListByWhere(materialInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialInfo materialInfo = new MaterialInfo();
		materialInfo.setWhere(wherestr);
		return materialInfoDao.deleteByWhere(materialInfo);
	}
	
	public MaterialInfo getMaterialInfoByCode(String materialCode) {
		MaterialInfo materialInfo = new MaterialInfo();
		List<MaterialInfo> list = materialInfoDao.getListByMaterialCode(materialCode);
		if(list!=null && list.size()>0){
			materialInfo = list.get(0);
		}		
		return materialInfo;
	}
	
	/**
	 * materialCode是否已存在
	 * @param materialCode
	 * @return
	 */
	public boolean checkNotOccupied(String id,String materialCode) {
		List<MaterialInfo> list = this.materialInfoDao.getListByMaterialCode(materialCode);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(MaterialInfo materialInfo :list){
					if(!id.equals(materialInfo.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

	@SuppressWarnings("resource")
	public String doimport(String realPath, MultipartFile[] file,User operator) throws IOException {  
        String result = "";
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
 			        // 正文内容应该从第二行开始,第一行为表头的标题
 			        for (int i = 1; i < rowNum; i++) {
 			        	row = sheet.getRow(i);
 			        	
 			        	MaterialInfo mInfo = new MaterialInfo();
 			    		String UUID = CommUtil.getUUID();
 			        	mInfo.setId(UUID);
 			        	int j = 0;
 			            while (j < colNum) {
 			            	if(j==0){
 			            		mInfo.setMaterialcode(row.getCell(j)==null?null:row.getCell(j).toString());
 			            	}else if(j==1){
 			            		mInfo.setMaterialname(row.getCell(j)==null?null:row.getCell(j).toString());
 			            	}else if(j==2){
 			            		String figureNumber = row.getCell(j)==null?null:row.getCell(j).toString();
 			            		if(figureNumber!=null&&!figureNumber.isEmpty()){
 			            			//查找图纸中是否存在
 			            			String wherestr = " where 1=1 and doctype='B' and number = '"+figureNumber+"'";
 			            			List<Data> figureList = dataService.selectListByWhere(wherestr);
 			            			if(figureList!=null&&figureList.size()>0){
 			            				Data figure = figureList.get(0);
 			            				mInfo.setFigurenumberid(figure.getId());
 			            			}else{
 			            			//不存在则新增
 			            				Data figure = new Data();
 			            				figure.setId(CommUtil.getUUID());
 			            				figure.setPid("-1");
 			            				figure.setNumber(figureNumber);
 			            				figure.setDocname(row.getCell(3)==null?"":row.getCell(3).toString());
 			            				figure.setDoctype("B");
 			            				figure.setSt("1");
 			            				figure.setMemo("【物料导入自动生成】");
 			            				figure.setInsdt(CommUtil.nowDate());
 			            				figure.setInsuser(operator.getId());
 			            				
 			            				int res = dataService.save(figure);
 			            				if(res>0){
 			            					mInfo.setFigurenumberid(figure.getId());
 			            					figure = null;
 			            				}
 			            			}
 			            		}
 			            	}else if(j==4){
 			            		mInfo.setSpec(row.getCell(j)==null?null:row.getCell(j).toString());
 			            	}else if(j==5){ 	
 			            		if(row.getCell(j)!=null){
 			            			mInfo.setUnit(row.getCell(j).toString());
 	 			            		//数据库没有则新增一条
 	 			            		List<MaterialUnit> unitlist=materialUnitService.selectListByWhere(" where unit='"+row.getCell(j).toString()+"' order by insdt");
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
 			            		
 			            	}else if(j==6){
 			            		if(row.getCell(j)!=null){
 			            			//数据库没有则新增一条
 	 			            		List<MaterialType> mtypelist=materialTypeService.selectListByWhere(" where typename='"+row.getCell(j).toString()+"' order by insdt");
 	 			            		if(mtypelist.isEmpty()){
 	 			            			MaterialType materialType=new MaterialType();
 	 			            			String mtid=CommUtil.getUUID();
 	 			            			materialType.setId(mtid);
 	 			            			materialType.setTypecode(CommUtil.getUUID());
 	 			            			materialType.setTypename(row.getCell(j)==null?null:row.getCell(j).toString());
 	 			            			materialType.setStatus("1");
 	 			            			materialType.setRemark("【物料导入自动生成,请重新修改该物料‘类型代码’】");
 	 			            			materialType.setInsuser(operator.getId());
 	 			            			materialType.setInsdt(CommUtil.nowDate());
 	 			            			materialType.setModify(operator.getId());
 	 			            			materialType.setModifydt(CommUtil.nowDate());
 	 			            			int mRes = materialTypeService.save(materialType);
 	 			            			if(mRes==1){
 	 			            				mInfo.setTypeid(materialType.getId());
 	 			            			}
 	 			            		}else{
 	 			            			mInfo.setTypeid(mtypelist.get(0).getId());
 	 			            		}
 			            		}
 			            		
 			            	}else if(j==7){
 			            		mInfo.setRemark(row.getCell(j)==null?null:row.getCell(j).toString());
 			            	}else{}
 			
 			                j++;
 			            }
 			            mInfo.setInsdt(CommUtil.nowDate());
 			            mInfo.setInsuser(operator.getId());
 			            mInfo.setModifydt(CommUtil.nowDate());
 			            mInfo.setModify(operator.getId());
 			            mInfo.setStatus("0");
 			            int res = 0;
 			            if(this.checkNotOccupied(mInfo.getId(),mInfo.getMaterialcode())){
 			            	res = this.save(mInfo);
 			            }
 			        	mInfo = null;
 			        	if(res>0){
 			        		suc++;
 			        		list.add(mInfo);
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

	public void exportByResponse(HttpServletResponse response,
			String filename,List<MaterialInfo> list) throws IOException {
		String title = "物料基础信息表";
		// 声明一个工作薄
	    HSSFWorkbook workbook = new HSSFWorkbook();
	    // 生成一个表格
	    HSSFSheet sheet = workbook.createSheet(title);
	    // 设置表格默认列宽度为15个字节
	    sheet.setDefaultColumnWidth(15);
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
	     
	    // 声明一个画图的顶级管理器
	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	    // 定义注释的大小和位置,详见文档
	    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	    // 设置注释内容
	    comment.setString(new HSSFRichTextString("注释！"));
	    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	    comment.setAuthor("SIPAI");
	 
	    //产生表格标题行
	    String[] headers = {"物料代码","物料名称","图号","图名","规格参数","单位","类型","备注"};
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
	    	MaterialInfo mInfo = list.get(i);
	    	//填充列数据，若果数据有不同的格式请注意匹配转换
	        HSSFCell cell0 = row.createCell(0);	    
		    cell0.setCellValue(mInfo.getMaterialcode());
		    
		    HSSFCell cell1 = row.createCell(1);  
		    cell1.setCellValue(mInfo.getMaterialname());
		    
		    HSSFCell cell2 = row.createCell(2);
		    HSSFCell cell3 = row.createCell(3);
		    if(mInfo.getFigure()!=null){
		    	Data figure = mInfo.getFigure();
		    	cell2.setCellValue(figure.getNumber());
			    cell3.setCellValue(figure.getDocname());
		    }
		    
		    HSSFCell cell4 = row.createCell(4);
		    cell4.setCellValue(mInfo.getSpec());
		    
		    HSSFCell cell5 = row.createCell(5);
		    cell5.setCellValue(mInfo.getUnit());
		    
		    HSSFCell cell6 = row.createCell(6);
		    cell6.setCellValue(mInfo.getMaterialtype()==null?"":mInfo.getMaterialtype().getTypename());
		    
		    HSSFCell cell7 = row.createCell(7);
		    cell7.setCellValue(mInfo.getRemark());
	        
	    }
	     try {
	    	response.reset();
			response.setContentType("application/x-msdownload");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(filename, "UTF-8"));
			//System.out.println(response.getHeader("Content-disposition"));
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
	public List<MaterialInfo> getMaterialInfo(String wherestr) {
		MaterialInfo materialinfo=new MaterialInfo();
		materialinfo.setWhere(wherestr);		
		return this.materialInfoDao.getMaterialInfo( materialinfo);
	}
	public String getBomMaterialScope(String productcode) {
		Set<String> materialSet = new HashSet<String>();
		materialSet = materialBOMService.getUniqueBOMMaterials(productcode);
		String codeScope = "(";
		if(materialSet!=null&&materialSet.size()>0){
			for(String materialCode:materialSet){
				codeScope+="'"+materialCode+"',";
			}
			codeScope = codeScope.substring(0,codeScope.length()-1);
		}else{
			codeScope+="''";
		}
		codeScope+=") ";		
		return codeScope;
	}
}
