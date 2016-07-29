package com.sipai.service.document;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.document.DataDao;
import com.sipai.entity.document.Data;
import com.sipai.tools.CommService;

@Service
public class DataService implements CommService<Data>{
	@Resource
	private DataDao dataDao;
	
	
	public String getNameById(String id) {
		String res="";
		Data data=this.selectById(id);
		if(data !=null){
			if(data.getDocname()==null){
				res = "空";
			}else{
				res = data.getDocname();
			}
		}
		return res;
	}
	
	
	public String getPidById(String id) {
		String res="";
		Data data=this.selectById(id);
		if(data !=null){
			if(data.getPid()==null){
				res = "";
			}else{
				res = data.getPid();
			}
		}
		return res;
	}
	
	public int getLevelById(String id) {
		int res=0;
		Data data=this.selectById(id);
		if(data !=null){
			if(data.getLevel()==null){
				
			}else{
				res = data.getLevel();
			}
		}
		return res;
	}
	public List<Data> selectList() {
		Data data= new Data();
		return this.dataDao.selectList(data);
	}
	
	@Override
	public Data selectById(String id) {
		return dataDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return dataDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Data entity) {
		return dataDao.insert(entity);
	}

	@Override
	public int update(Data t) {
		return dataDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<Data> selectListByWhere(String wherestr) {
		Data data = new Data();
		data.setWhere(wherestr);
		return dataDao.selectListByWhere(data);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		Data data = new Data();
		data.setWhere(wherestr);
		return dataDao.deleteByWhere(data);
	}


	public boolean checkNotOccupied(String id, String number, String doctype) {
		List<Data> list = this.dataDao.getListByNumberAndType(number,doctype);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Data data :list){
					if(!id.equals(data.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}


}
