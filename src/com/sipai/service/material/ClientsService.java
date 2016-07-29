package com.sipai.service.material;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.material.ClientsDao;
import com.sipai.entity.material.Clients;
import com.sipai.tools.CommService;
@Service
public class ClientsService implements CommService<Clients>{
	@Resource
	private ClientsDao clientsDao;
	@Override
	public Clients selectById(String id) {
		return clientsDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return clientsDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Clients materialInfo) {
		return clientsDao.insert(materialInfo);
	}

	@Override
	public int update(Clients materialInfo) {
		return clientsDao.updateByPrimaryKeySelective(materialInfo);
	}

	@Override
	public List<Clients> selectListByWhere(String wherestr) {
		Clients materialInfo = new Clients();
		materialInfo.setWhere(wherestr);
		return clientsDao.selectListByWhere(materialInfo);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		Clients materialType = new Clients();
		materialType.setWhere(wherestr);
		return clientsDao.deleteByWhere(materialType);
	}
	
	
}
