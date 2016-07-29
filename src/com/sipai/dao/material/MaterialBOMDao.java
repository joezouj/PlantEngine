package com.sipai.dao.material;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.material.MaterialBOM;
@Repository
public class MaterialBOMDao extends CommDaoImpl<MaterialBOM> {
	public MaterialBOMDao() {
		super();
		this.setMappernamespace("material.MaterialBOMMapper");
	}

/*	public List<MaterialBOM> selectListByPid(MaterialBOM materialBOM) {
		List<MaterialBOM> list = getSqlSession().selectList("material.MaterialBOMMapper.selectListByPid", materialBOM);
		return list;
	}*/
	
	public List<MaterialBOM> selectTreeListByID(MaterialBOM materialBOM) {
		List<MaterialBOM> list = getSqlSession().selectList("material.MaterialBOMMapper.selectTreeListByID", materialBOM);
		return list;
	}
	
	public List<MaterialBOM> getRootByCodeAndVersion(String materialcode,Integer version) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setMaterialcode(materialcode);
		materialBOM.setVersion(version);
		List<MaterialBOM> list = getSqlSession().selectList("material.MaterialBOMMapper.getRootByCodeAndVersion", materialBOM);
		return list;
	}

	public List<MaterialBOM> getChildByCodeAndVersion(String pid,String materialCode,Integer version) {
		MaterialBOM materialBOM = new MaterialBOM();
		materialBOM.setPid(pid);
		materialBOM.setMaterialcode(materialCode);
		materialBOM.setVersion(version);
		List<MaterialBOM> list = getSqlSession().selectList("material.MaterialBOMMapper.getChildByCodeAndVersion", materialBOM);
		return list;
	}
	
	public List<MaterialBOM> getRootByMaterialcode(String materialcode) {
		List<MaterialBOM> list = this.getSqlSession().selectList("material.MaterialBOMMapper.getRootByMaterialcode", materialcode);
		return list;
	}
	
	public List<MaterialBOM> selectListByPid(String pid) {
		List<MaterialBOM> list = this.getSqlSession().selectList("material.MaterialBOMMapper.selectListByPid", pid);
		return list;
	}

	public List<MaterialBOM> getListByCodeAndVersion(String materialcode,
			Integer version) {
		MaterialBOM mbom = new MaterialBOM();
		mbom.setMaterialcode(materialcode);
		mbom.setVersion(version);
		List<MaterialBOM> list = this.getSqlSession().selectList("material.MaterialBOMMapper.getRootByCodeAndVersion",mbom);
		return list;
	}
}
