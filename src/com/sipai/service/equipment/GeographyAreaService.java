package com.sipai.service.equipment;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.equipment.GeographyAreaDao;
import com.sipai.entity.equipment.EquipmentClass;
import com.sipai.entity.equipment.GeographyArea;
import com.sipai.tools.CommService;


@Service
public class GeographyAreaService implements CommService<GeographyArea>{
	@Resource
	private GeographyAreaDao geographyareaDao;
	

	public List<GeographyArea> selectList() {
		// TODO Auto-generated method stub
		GeographyArea geographyarea = new GeographyArea();
		return this.geographyareaDao.selectList(geographyarea);
	}


	@Override
	public GeographyArea selectById(String id) {
		return this.geographyareaDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return this.geographyareaDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(GeographyArea entity) {
		return this.geographyareaDao.insert(entity);
	}

	@Override
	public int update(GeographyArea t) {
		return this.geographyareaDao.updateByPrimaryKeySelective(t);
	}

	@Override
	public List<GeographyArea> selectListByWhere(String wherestr) {
		GeographyArea geographyarea = new GeographyArea();
		geographyarea.setWhere(wherestr);
		return this.geographyareaDao.selectListByWhere(geographyarea);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		GeographyArea geographyarea = new GeographyArea();
		geographyarea.setWhere(wherestr);		
		return this.geographyareaDao.deleteByWhere(geographyarea);
	}
	/**
	 * 是否未被占用
	 * @param id
	 * @param name
	 * @return
	 */
	public boolean checkNotOccupied(String id, String name) {
		List<GeographyArea> list = this.selectListByWhere(" where name='"+name+"' order by insdt");
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(GeographyArea geographyarea :list){
					if(!id.equals(geographyarea.getId())){//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}

}
