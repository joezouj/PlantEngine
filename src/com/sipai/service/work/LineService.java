package com.sipai.service.work;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.work.LineDao;
import com.sipai.entity.work.Line;
import com.sipai.tools.CommService;

@Service
public class LineService implements CommService<Line>{
	@Resource
	private LineDao lineDao;
	
	@Override
	public Line selectById(String id) {
		return lineDao.selectByPrimaryKey(id);
	}

	@Override
	public int deleteById(String id) {
		return lineDao.deleteByPrimaryKey(id);
	}

	@Override
	public int save(Line entity) {
		return lineDao.insert(entity);
	}

	@Override
	public int update(Line entity) {
		return lineDao.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<Line> selectListByWhere(String wherestr) {
		Line line = new Line();
		line.setWhere(wherestr);
		return lineDao.selectListByWhere(line);
	}

	@Override
	public int deleteByWhere(String wherestr) {
		Line line = new Line();
		line.setWhere(wherestr);
		return lineDao.deleteByWhere(line);
	}

	public boolean checkNotOccupied(String id, String serial) {
		List<Line> list = this.lineDao.getListBySerial(serial);
		if(id==null){//新增
			if(list!=null && list.size()>0){
				return false;
			}
		}else{//编辑
			if(list!=null && list.size()>0){
				for(Line line :list){
					if(!id.equals(line.getId())){
						//不是当前编辑的那条记录
						return false;
					}
				}
			}
		}
		return true;
	}
	
}
