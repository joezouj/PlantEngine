package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.CutterInfoDao;
import com.sipai.entity.material.CutterInfo;
import com.sipai.tools.CommService;
@Service
public class CutterInfoService implements CommService<CutterInfo>{
	@Resource
	private CutterInfoDao cutterInfoDao;
	@Override
	public CutterInfo selectById(String id) {
		return cutterInfoDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return cutterInfoDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(CutterInfo cutterInfo) {
		return cutterInfoDao.insert(cutterInfo);
	}

	@Override
	public int update(CutterInfo cutterInfo) {
		return cutterInfoDao.updateByPrimaryKeySelective(cutterInfo);
	}

	@Override
	public List<CutterInfo> selectListByWhere(String wherestr) {
		CutterInfo cutterInfo = new CutterInfo();
		cutterInfo.setWhere(wherestr);
		return cutterInfoDao.selectListByWhere(cutterInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		CutterInfo cutterInfo = new CutterInfo();
		cutterInfo.setWhere(wherestr);
		return cutterInfoDao.deleteByWhere(cutterInfo);
	}
	
	public CutterInfo getCutterInfoByCode(String cutterCode) {
		CutterInfo cutterInfo = new CutterInfo();
		List<CutterInfo> list = cutterInfoDao.getListByCutterCode(cutterCode);
		if(list!=null && list.size()>0){
			cutterInfo = list.get(0);
		}		
		return cutterInfo;
	}
	
	/**
	 * cutterCode是否已存在
	 * @param cutterCode
	 * @return
	 */
	public boolean checkNotOccupied(String id,String cutterCode) {
		List<CutterInfo> list = this.cutterInfoDao.getListByCutterCode(cutterCode);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(CutterInfo materialInfo :list){
					if(!id.equals(materialInfo.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

	public List<CutterInfo> getCutterInfo(String wherestr) {
		CutterInfo cutterinfo = new CutterInfo();
		cutterinfo.setWhere(wherestr);		
		return this.cutterInfoDao.getCutterInfo(cutterinfo);
	}
}
