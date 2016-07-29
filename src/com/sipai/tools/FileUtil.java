package com.sipai.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.sipai.entity.base.CommonFile;

public class FileUtil {

	public static final String IMAGE_FILE_EXT = "jpg;jpeg;png;gif;bmp;ico";
	public static final String ATTACHE_FILE_EXT = "doc;zip;rar;pdf";
	public static final String FORBID_FILE_EXT = "jsp;com;bat";
	public static final String EXE_FILE_EXT = "exe;com;bat";

	private static final FileUtil util = new FileUtil();

	public FileUtil() {
	}

	public static FileUtil getInstance() {
		return util;
	}
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	} 
	/**
	 * 判断文件的类型doc;zip;rar;pdf
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 返回boolean值
	 */
	public static boolean isAttacheFile(String fileName) {
		return checkExtFile("doc;zip;rar;pdf", fileName);
	}

	/**
	 * 判断文件的类型jsp;com;bat
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 返回boolean值
	 */
	public static boolean isForbidFile(String fileName) {
		return checkExtFile("jsp;com;bat", fileName);
	}

	/**
	 * 判断文件的类型为图象文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 返回boolean值
	 */
	public static boolean isImgageFile(String fileName) {
		return checkExtFile("jpg;jpeg;png;gif;bmp;ico", fileName);
	}

	/**
	 * 判断文件的类型为可执行文件
	 * 
	 * @param fileName
	 *            文件名称
	 * @return 返回boolean值
	 */
	public static boolean isExeFile(String fileName) {
		return checkExtFile("exe;com;bat", fileName);
	}

	/**
	 * 判断文件是否为该后缀名
	 * 
	 * @param ext
	 *            文件后缀名
	 * @param fileName
	 *            文件名
	 * @return 返回boolean值
	 */
	public static boolean checkExtFile(String ext, String fileName) {
		if (ext == null)
			return false;
		String exts[] = ext.split(";");
		String file = fileName.toLowerCase();
		for (int i = 0; i < exts.length; i++)
			if (file.endsWith("." + exts[i]))
				return true;

		return false;
	}

	/**
	 * 得到零时文件名称（绝对路径）
	 * 
	 * @param dir
	 *            文件路径
	 * @param fileExt
	 *            文件名
	 * @return 绝对路径零时文件名称字符串
	 */
	public static String getTempFile(String dir, String fileExt) {
		String tempFileName = CommUtil.getRandString(8) + fileExt;
		File file = new File(dir + "/" + tempFileName);
		if (file.exists())
			return getTempFile(dir, fileExt);
		else
			return tempFileName;
	}

	/**
	 * 上传文件并判断成功否
	 * 
	 * @param in
	 *            输入流
	 * @param fileName
	 *            文件名
	 * @return 返回boolean值
	 */
	public static boolean saveFile(InputStream in, String fileName) {
		if(FileUtil.isfilename(fileName.substring(0, fileName.lastIndexOf("\\")))){
			File outFile = new File(fileName);
			try {
				FileOutputStream out = new FileOutputStream(outFile);
				byte temp[] = new byte[1024];
				for (int length = -1; (length = in.read(temp)) > 0;){
					out.write(temp, 0, length);
				}
				out.flush();
				out.close();
				in.close();
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
			return true;
		}
		return false;
	}
	public static boolean saveFile(InputStream in, String fileName,CommonFile cf) {
		if(FileUtil.isfilename(fileName.substring(0, fileName.lastIndexOf("\\")))){
			File outFile = new File(fileName);
			try {
				FileOutputStream out = new FileOutputStream(outFile);
				byte temp[] = new byte[1024];
				for (int length = -1; (length = in.read(temp)) > 0;){
					out.write(temp, 0, length);
					cf.setSize(cf.getSize()+length);
				}
				out.flush();
				out.close();
				in.close();
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
			return true;
		}
		return false;
	}
	/**
	 * 删除文件
	 * 
	 * @param filepath
	 *            文件路径及名称
	 * @throws java.io.IOException
	 */
	public static void deleteFile(String filepath) throws java.io.IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						deleteFile(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
			deleteFile(filepath);// 递归调用
		}else{
			f.delete();
		}
	}

	/**
	 * 返回文件大小不以字节形势
	 * 
	 * @param filesize
	 *            文件大小值
	 * @return 返回文件大小以B，K，M，G
	 */
	public static String valuemork(int filesize) {
		String mess = "";
		int filesizeleng = 0;
		filesizeleng = filesize;
		if (filesizeleng < 1024) {
			mess = filesizeleng + " B";
		} else if (filesizeleng < (1024 * 1024)) {
			mess = (filesizeleng / 1024) + " K";
		} else if (filesizeleng < (1024 * 1024 * 1024)) {
			mess = (filesizeleng / (1024 * 1024)) + " M";
		} else {
			mess = (filesizeleng / (1024 * 1024 * 1024)) + " G";
		}
		return mess;
	}

	/**
	 * 判断文件夹存在，不存在则建立
	 * 
	 * @param val
	 *            文件夹名称
	 * @return 返回boolean值
	 */
	public static boolean isfilename(String val) {
		File dirFile;
		// File tempFile;
		boolean bFile;
		bFile = false;
		try {
			dirFile = new File(val);
			bFile = dirFile.exists();
			if (bFile == true) {
				// System.out.println("文件夹已存在，将用原来的文件夹");
			} else {
				bFile = dirFile.mkdirs(); // 创建文件夹
				if (bFile == true) {
				} else {
					// System.exit(1);
				}
			}

		} catch (Exception e) {
			// System.out.println("sendmaiaction.java 文件中不能创建新文件夹，错误发生在 ："
			// + e.toString());
		}
		return bFile;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	@SuppressWarnings({"resource", "unused" })
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[8192];
				while ((byteread = inStream.read(buffer, 0, 8192)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	/**
	 * 复制目录下的文件（不包含该目录和子目录，只复制目录下的文件）到指定目录。
	 * 
	 * @param targetDir
	 * @param path
	 */
	public static void copyFileOnly(String targetDir, String path) {
		File file = new File(path);
		File targetFile = new File(targetDir);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File subFile : files) {
				if (subFile.isFile()) {
					copyFile(targetFile, subFile);
				}
			}
		}
	}

	/**
	 * 复制目录到指定目录。targetDir是目标目录，path是源目录。
	 * 该方法会将path以及path下的文件和子目录全部复制到目标目录
	 * 
	 * @param targetDir
	 * @param path
	 */
	public static void copyDir(String targetDir, String path) {
		File targetFile = new File(targetDir);
		createFile(targetFile, false);
		File file = new File(path);
		if (targetFile.isDirectory() && file.isDirectory()) {
			copyFileToDir(targetFile.getAbsolutePath() + "/" + file.getName(),
					listFile(file));
		}
	}

	/**
	 * 复制文件到指定目录。targetDir是目标目录，file是源文件名，newName是重命名的名字。
	 * 
	 * @param targetFile
	 * @param file
	 * @param newName
	 */
	public static void copyFileToDir(String targetDir, File file, String newName) {
		String newFile = "";
		if (newName != null && !"".equals(newName)) {
			newFile = targetDir + "/" + newName;
		} else {
			newFile = targetDir + "/" + file.getName();
		}
		File tFile = new File(newFile);
		copyFile(tFile, file);
	}


	/**
	 * 复制一组文件到指定目录。targetDir是目标目录，filePath是需要复制的文件路径
	 * 
	 * @param targetDir
	 * @param filePath
	 */
	public static void copyFileToDir(String targetDir, String... filePath) {
		if (targetDir == null || "".equals(targetDir)) {
			System.out.println("参数错误，目标路径不能为空");
			return;
		}
		File targetFile = new File(targetDir);
		if (!targetFile.exists()) {
			targetFile.mkdir();
		} else {
			if (!targetFile.isDirectory()) {
				System.out.println("参数错误，目标路径指向的不是一个目录！");
				return;
			}
		}
		for (String path : filePath) {
			File file = new File(path);
			if (file.isDirectory()) {
				copyFileToDir(targetDir + "/" + file.getName(), listFile(file));
			} else {
				copyFileToDir(targetDir, file, "");
			}
		}
	}

	/**
	 * 复制文件。targetFile为目标文件，file为源文件
	 * 
	 * @param targetFile
	 * @param file
	 */
	public static void copyFile(File targetFile, File file) {
		if (targetFile.exists()) {
//			System.out.println("文件" + targetFile.getAbsolutePath()
//					+ "已经存在，跳过该文件！");
			return;
		} else {
			createFile(targetFile, true);
		}
//		System.out.println("复制文件" + file.getAbsolutePath() + "到"
//				+ targetFile.getAbsolutePath());
		try {
			InputStream is = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				fos.write(buffer);
			}
			is.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**浏览文件
	 * @param dir
	 * @return
	 */
	public static String[] listFile(File dir) {
		String absolutPath = dir.getAbsolutePath();
		String[] paths = dir.list();
		String[] files = new String[paths.length];
		for (int i = 0; i < paths.length; i++) {
			files[i] = absolutPath + "/" + paths[i];
		}
		return files;
	}

	/**新建文件，根据路径
	 * @param path
	 * @param isFile
	 */
	public static void createFile(String path, boolean isFile){
		createFile(new File(path), isFile);
	}

	/**新建文件，根据文件
	 * @param file
	 * @param isFile
	 */
	public static void createFile(File file, boolean isFile) {
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				createFile(file.getParentFile(), false);
			} else {
				if (isFile) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					file.mkdir();
				}
			}
		}
	}
	
	/**将批量文件压缩成zip的方式，并返回状态，不进行下载
	 * response为HttpServletResponse,zippath为压缩文件的绝对路径，zipname为压缩文件的名字，files为压缩文件
	 * @param zippath
	 * @param files
	 * @return
	 */
	public  int size=0;
	public  String copyfiletozipnotdown(String zippath,String zipname,File files[]){
		String res="";
		byte[] bt=new byte[8192]; 
		   try {
			    ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(zippath+zipname));
			    //循环下载每个文件并读取内容
			    for(int j=0;j<files.length;j++){
			    	if(files[j]!=null){
					     FileInputStream is=new FileInputStream(files[j]);
					     ZipEntry ze=new ZipEntry(files[j].getName());//设置文件编码格式
					     zout.putNextEntry(ze);
					     zout.setEncoding("gbk");
					     int len;
					     //读取下载文件内容
					     while((len=is.read(bt))>0){
					    	 size++;	 
					      zout.write(bt, 0, len);
					     }
					     zout.closeEntry();
					     is.close();	
			    	}
			    }
			    zout.close();
//			    toUpload(response,zippath+zipname);//调用下载方法 
			    res=null;
			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			    res="error";
			   }catch (IOException e) {
			    e.printStackTrace();
			    res="error";
			   } 
		
		return res;
	}
	
	/**将批量文件压缩成zip的方式，并返回状态
	 * response为HttpServletResponse,zippath为压缩文件的绝对路径，zipname为压缩文件的名字，files为压缩文件
	 * @param zippath
	 * @param files
	 * @return
	 */
	public static String copyfiletozip(HttpServletResponse response,String zippath,String zipname,File files[]){
		String res="";
		byte[] bt=new byte[8192]; 
		   try {
			    ZipOutputStream zout=new ZipOutputStream(new FileOutputStream(zippath+zipname));
			    //循环下载每个文件并读取内容
			    for(int j=0;j<files.length;j++){
			    	if(files[j]!=null){
					     FileInputStream is=new FileInputStream(files[j]);
					     ZipEntry ze=new ZipEntry(files[j].getName());//设置文件编码格式
					     zout.putNextEntry(ze);
					     zout.setEncoding("gbk");
					     int len;
					     //读取下载文件内容
					     while((len=is.read(bt))>0){
					      zout.write(bt, 0, len);
					     }
					     zout.closeEntry();
					     is.close();	
			    	}
			    }
			    zout.close();
			    toUpload(response,zippath+zipname);//调用下载方法 
			    res=null;
			   } catch (FileNotFoundException e) {
			    e.printStackTrace();
			    res="error";
			   }catch (IOException e) {
			    e.printStackTrace();
			    res="error";
			   } 
		
		return res;
	}

	//下载公用方法
	public static void toUpload(HttpServletResponse response,String str){
	   try {
	    String path=str;
	    if(!"".equals(path)){
	     File file=new File(path);
	     if(file.exists()){
	      InputStream ins=new FileInputStream(path);
	     BufferedInputStream bins=new BufferedInputStream(ins);//放到缓冲流里面
	           OutputStream outs=response.getOutputStream();//获取文件输出IO流
	           BufferedOutputStream bouts=new BufferedOutputStream(outs);
	           response.setContentType("application/x-download");//设置response内容的类型
	           response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(str, "UTF-8"));//设置头部信息
	           int bytesRead = 0;
	           byte[] buffer = new byte[8192];
	           //开始向网络传输文件流
	           while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
	               bouts.write(buffer, 0, bytesRead);
	           }
	           bouts.flush();//这里一定要调用flush()方法
	           ins.close();
	           bins.close();
	           outs.close();
	           bouts.close();
	     }
	    }
	   } catch (IOException e) {
	    e.printStackTrace();
	   }
	}
	
	/**将批量文件压缩成zip的方式，并返回状态
	 * @param response HttpServletResponse
	 * @param filename 要导出文件显示的名称
	 * @param abspath 文件的绝对路径和真实名称的组合Str
	 * @return
	 */
	public static void downloadFile(HttpServletResponse response,String filename,String abspath) {
		if(filename!=null&&!"".equals(filename)&&abspath!=null&&!"".equals(abspath)){
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				response.reset();
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-disposition", "attachment; filename="
						+ URLEncoder.encode(filename, "UTF-8"));
				bis = new BufferedInputStream(new FileInputStream(abspath));
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[1024];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
				bos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				bis = null;
				bos = null;
			}
		}
	}
	
}
