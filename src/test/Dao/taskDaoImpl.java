package com.tastmanager.test.Dao;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 1/24/2019
 * @Modify 
 * 
*/

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import DBconnection.TaskEntity;

@Component
public class taskDaoImpl implements taskDao{
	
	final public String WAITING = "waiting";
	@Resource
	private MongoTemplate mongotemplate;
	@Override
	public void saveResult(TaskEntity taskentity) {
		// TODO Auto-generated method stub
		mongotemplate.save(taskentity);
	}

	@Override
	public TaskEntity recordTask(String[] taskinfo) {
		TaskEntity taskentity = new TaskEntity();
		taskentity.setDate(taskinfo[0]);
		taskentity.setPeriod(1);
		taskentity.setTaskId(taskinfo[1]);
		taskentity.setEquipment(taskinfo[2]);
		taskentity.setUserId(taskinfo[3]);
		taskentity.setTaskName(taskinfo[4]);
		taskentity.setTaskLanguage(taskinfo[5]);
		taskentity.setTaskStatus(WAITING);
		taskentity.setPublisher(taskinfo[6]);
		taskentity.setTaskSpecSchedul(taskinfo[7]);
		saveResult(taskentity);
		return taskentity;
	}

}
