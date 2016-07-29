package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.MaterialShortDao;
import com.sipai.dao.material.MaterialTypeDao;
import com.sipai.entity.material.MaterialShort;
import com.sipai.entity.material.MaterialShort;
import com.sipai.tools.CommService;
@Service
public class MaterialShortService implements CommService<MaterialShort>{
	@Resource
	private MaterialShortDao materialShortDao;
	@Override
	public MaterialShort selectById(String id) {
		return materialShortDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return materialShortDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(MaterialShort materialInfo) {
		return materialShortDao.insert(materialInfo);
	}

	@Override
	public int update(MaterialShort materialInfo) {
		return materialShortDao.updateByPrimaryKeySelective(materialInfo);
	}

	@Override
	public List<MaterialShort> selectListByWhere(String wherestr) {
		MaterialShort materialInfo = new MaterialShort();
		materialInfo.setWhere(wherestr);
		return materialShortDao.selectListByWhere(materialInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		MaterialShort materialType = new MaterialShort();
		materialType.setWhere(wherestr);
		return materialShortDao.deleteByWhere(materialType);
	}
	
	
}
