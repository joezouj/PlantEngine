/* Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sipai.snaker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.snaker.engine.SnakerEngine;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.entity.Order;
import org.snaker.engine.entity.Process;
import org.snaker.engine.entity.Surrogate;
import org.snaker.engine.entity.Task;
import org.snaker.engine.helper.StreamHelper;
import org.snaker.engine.helper.StringHelper;
import org.snaker.engine.model.TaskModel.TaskType;
import org.springframework.stereotype.Component;

import com.sipai.dao.work.WorkStationConfigDao;
import com.sipai.entity.work.WorkStationConfig;

/**
 * Snaker流程引擎本地化封装
 */
@Component
public class SnakerEngineFacets {
	@Resource
	private SnakerEngine engine;
	@Resource
	private WorkStationConfigDao workStationConfigDao;
	
	public void initFlows() {
		//路径为项目路径写法，不考虑window和Linux
		engine.process().deploy(StreamHelper.getStreamFromClasspath("com/sipai/snaker/flows/assemble.snaker"));
	}
	
	public SnakerEngine getEngine() {
		return engine;
	}
	
	public List<String> getAllProcessNames() {
		List<Process> list = engine.process().getProcesss(new QueryFilter());
		List<String> names = new ArrayList<String>();
		for(Process entity : list) {
			if(names.contains(entity.getName())) {
				continue;
			} else {
				names.add(entity.getName());
			}
		}
		return names;
	}
	/**
	 * 返回所有的工作流文件信息 
	 */
	public List<Process> getAllProcess() {
		List<Process> list = engine.process().getProcesss(new QueryFilter());
		return list;
	}
	/**
	 * 根据process id 开始流程实例，并且运行到第一个任务 
	 */
	public Order startInstanceById(String processId, String operator, Map<String, Object> args) {
		return engine.startInstanceById(processId, operator, args);
	}
	/**
	 * 根据process name 开始流程实例，并且运行到第一个任务 
	 */
	public Order startInstanceByName(String name, Integer version, String operator, Map<String, Object> args) {
		return engine.startInstanceByName(name, version, operator, args);
	}
	/**
	 * 根据process name,version 开始流程实例，运行并且执行第一个任务，直接运行到第二个任务 
	 */
	public Order startAndExecute(String name, Integer version, String operator, Map<String, Object> args) {
		Order order = engine.startInstanceByName(name, version, operator, args);
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		List<Task> newTasks = new ArrayList<Task>();
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}
		return order;
	}
	/**
	 * 根据process id 开始流程实例，运行并且执行第一个任务，直接运行到第二个任务 
	 */
	public Order startAndExecute(String processId, String operator, Map<String, Object> args) {
		Order order = engine.startInstanceById(processId, operator, args);
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(order.getId()));
		List<Task> newTasks = new ArrayList<Task>();
		if(tasks != null && tasks.size() > 0) {
			Task task = tasks.get(0);
			newTasks.addAll(engine.executeTask(task.getId(), operator, args));
		}
		return order;
	}
	/**
	 * 根据task id 执行任务 
	 */
	public List<Task> execute(String taskId, String operator, Map<String, Object> args) {
		return engine.executeTask(taskId, operator, args);
	}
	
	/**
	 * 根据task id 执行任务,然后更新下一个任务的参与者组信息
	 * @param operator 当前操作者
	 */
	public List<Task> executeAndUpdateActors(String taskId, String operator, Map<String, Object> args) {
		List<Task> tasks = new ArrayList<Task>();
		try {
			tasks = engine.executeTask(taskId, operator, args);
		} catch (Exception e) {
			//获取流程引擎抛出的异常
			System.out.println(e.getLocalizedMessage());
		} finally{
			for(int t=0;t<tasks.size();t++){
				Task task = tasks.get(t);
				if(task!=null&&task.getOrderId()!=null){
					String[] oldActors = task.getActorIds();
					//清空原有Actor(保留最高权限engine.ADMIN)
					if(oldActors!=null&&oldActors.length>0){
						String[] removerActors = new String[oldActors.length];
						int i= 0;
						boolean flag=false;
						for(String actor:oldActors){
							if(!actor.equals(engine.ADMIN)){
								removerActors[i] = actor;
								i++;
							}
							if(actor.equals(engine.ADMIN)){
								flag=true;
							}
						}
						if(!flag){
							//添加最高权限Actor engine.ADMIN为了避开SnakerBUG
							engine.task().addTaskActor(task.getId(), engine.ADMIN);
						}
						try {
							engine.task().removeTaskActor(task.getId(), removerActors);
						} catch (Exception e) {
							System.out.println(e.getLocalizedMessage());
						}
					}
					
					List<WorkStationConfig> configList = workStationConfigDao.selectListByOrderId(task.getOrderId());
					//获取不重复组别
					Set<String> actorSet = new HashSet<String>();
					for(WorkStationConfig sconf: configList){
						//获取对应任务节点下的最新Actor配置信息
						if(task.getTaskName().equals(sconf.getTaskname())){
							actorSet.add(sconf.getWorkstationid());
						}
					}
					if(actorSet!=null&&!actorSet.isEmpty()&&actorSet.size()>0){
						//定义当前参者String数组actors
						String[] actors = new String[actorSet.size()];
						int i = 0;
						for(String actor:actorSet){
							actors[i] = actor;
							i++;
						}
						try {
							//添加当前配置Actor
							engine.task().addTaskActor(task.getId(), actors);
						} catch (Exception e) {
							//获取流程引擎抛出的异常
							System.out.println(e.getLocalizedMessage());
						}
					}
				}
			}
		}
		return tasks;
	}
	
	/**
	 * 根据task id，nodeName(目标任务名称) 执行并跳转到目标任务 
	 */
	public List<Task> executeAndJump(String taskId, String operator, Map<String, Object> args, String nodeName) {
		return engine.executeAndJumpTask(taskId, operator, args, nodeName);
	}
	
	public List<Task> transfer(String taskId, String operator, String... actors) {
		List<Task> tasks = engine.task().createNewTask(taskId, TaskType.Major.ordinal(), actors);
		engine.task().complete(taskId, operator);
		return tasks;
	}
	
	public void addSurrogate(Surrogate entity) {
		if(entity.getState() == null) {
			entity.setState(1);
		}
		engine.manager().saveOrUpdate(entity);
	}
	
	public void deleteSurrogate(String id) {
		engine.manager().deleteSurrogate(id);
	}
	
	public Surrogate getSurrogate(String id) {
		return engine.manager().getSurrogate(id);
	}
	
	public List<Surrogate> searchSurrogate(Page<Surrogate> page, QueryFilter filter) {
		return engine.manager().getSurrogate(page, filter);
	}
	
	/**
	 * 根据orderId获取当前活动ID
	 * @param orderId
	 * @return
	 */
	public Task getActiveTask(String orderId) {
		List<Task> tasks = engine.query().getActiveTasks(new QueryFilter().setOrderId(orderId));
		Task task = new Task();
		if(tasks != null && tasks.size() > 0) {
			task = tasks.get(0);
		}
		return task;
	}
	
	/**
	 * 添加默认操作工位
	 */
	public void updateActors(String taskId){
		Task task = engine.query().getTask(taskId);
		
		String[] oldActors = task.getActorIds();
		//清空原有Actor(保留最高权限engine.ADMIN)
		if(oldActors!=null&&oldActors.length>0){
			String[] removerActors = new String[oldActors.length];
			int i= 0;
			boolean flag=false;
			for(String actor:oldActors){
				if(!actor.equals(engine.ADMIN)){
					removerActors[i] = actor;
					i++;
				}
				if(actor.equals(engine.ADMIN)){
					flag=true;
				}
			}
			if(!flag){
				//添加最高权限Actor engine.ADMIN为了避开SnakerBUG
				engine.task().addTaskActor(taskId, engine.ADMIN);
			}
			try {
				engine.task().removeTaskActor(taskId, removerActors);
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
				
		List<WorkStationConfig> configList = workStationConfigDao.selectListByOrderId(task.getOrderId());
		//获取不重复组别
		Set<String> actorSet = new HashSet<String>();
		for(WorkStationConfig sconf: configList){
			//获取对应任务节点下的最新Actor配置信息
			if(task.getTaskName().equals(sconf.getTaskname())){
				actorSet.add(sconf.getWorkstationid());
			}
		}
		if(actorSet!=null&&!actorSet.isEmpty()&&actorSet.size()>0){
			//定义当前参者String数组actors
			String[] actors = new String[actorSet.size()];
			int i = 0;
			for(String actor:actorSet){
				actors[i] = actor;
				i++;
			}
			//添加当前配置Actor
			try {
				engine.task().addTaskActor(taskId, actors);
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
	
	/**
	 * 根据orderId实时更新当前活动操作工位
	 */
	public void updateActorsByOrderId(String orderId){
		QueryFilter filter = new QueryFilter();
		filter.setOrderId(orderId);
		List<Task> taskList = engine.query().getActiveTasks(filter);
		if(taskList!=null&&taskList.size()>0){
			for(int t=0;t<taskList.size();t++){
				Task task = taskList.get(t);
				String[] oldActors = task.getActorIds();
				//清空原有Actor(保留最高权限engine.ADMIN)
				if(oldActors!=null&&oldActors.length>0){
					String[] removerActors = new String[oldActors.length];
					int i= 0;
					boolean flag=false;
					for(String actor:oldActors){
						if(!actor.equals(engine.ADMIN)){
							removerActors[i] = actor;
							i++;
						}
						if(actor.equals(engine.ADMIN)){
							flag=true;
						}
					}
					if(!flag){
						//添加最高权限Actor engine.ADMIN为了避开SnakerBUG
						engine.task().addTaskActor(task.getId(), engine.ADMIN);
					}
					try {
						engine.task().removeTaskActor(task.getId(), removerActors);
					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
					}
				}
				
				List<WorkStationConfig> configList = workStationConfigDao.selectListByOrderId(orderId);
				//获取不重复组别
				Set<String> actorSet = new HashSet<String>();
				for(WorkStationConfig sconf: configList){
					//获取对应任务节点下的最新Actor配置信息
					if(task.getTaskName().equals(sconf.getTaskname())){
						actorSet.add(sconf.getWorkstationid());
					}
				}
				if(actorSet!=null&&!actorSet.isEmpty()&&actorSet.size()>0){
					//定义当前参者String数组actors
					String[] actors = new String[actorSet.size()];
					int i = 0;
					for(String actor:actorSet){
						actors[i] = actor;
						i++;
					}
					//添加当前配置Actor
					try {
						engine.task().addTaskActor(task.getId(), actors);
					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
					}
				}
			}
		}
		
	}
}
