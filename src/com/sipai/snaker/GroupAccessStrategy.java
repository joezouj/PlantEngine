package com.sipai.snaker;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.snaker.engine.impl.GeneralAccessStrategy;

import com.sipai.entity.work.WorkScheduling;
import com.sipai.service.work.WorkSchedulingService;

public class GroupAccessStrategy extends GeneralAccessStrategy {
	@Resource
	private WorkSchedulingService workSchedulingService;
	/**
	 * 根据操作人id确定所有的组集合
	 * @param operator 操作人id
	 * @return List<String> 确定的组集合[如操作人属于多个部门、拥有多个角色]
	 */
	protected List<String> ensureGroup(String operator) {
		List<WorkScheduling> list = this.workSchedulingService.getWorkSation(operator);
		List<String> groupList = new ArrayList<String>();
		for(WorkScheduling w:list){
			groupList.add(w.getWorkstationid());
		}
		list = null;
		return groupList;
	}

}
