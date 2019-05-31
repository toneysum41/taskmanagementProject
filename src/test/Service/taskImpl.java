package com.tastmanager.test.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tastmanager.test.serviceInterface.taskService;

import DBconnection.TaskEntity;
import DBconnection.userEntity;

@Service
@EnableScheduling
public class taskImpl implements taskService{
	@Resource
	private MongoTemplate mongotemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(taskImpl.class);
	private static final SimpleDateFormat dateFromat = new SimpleDateFormat("HH:mm:ss");
	@Override
	@Async
	@Scheduled(cron = "0 0 6 * * ?")
	public void updateTaskLife() {
		List<TaskEntity> tasks= mongotemplate.findAll(TaskEntity.class);
		logger.info("Start to refresh task life at: "+ dateFromat.format(new Date()));
		for(TaskEntity t : tasks) {
			if(t.getPeriod()<=0) {
				t.setPeriod(1);
				mongotemplate.save(t);
			}else {
				if(t.getPeriod()>=7) {
					userEntity user = mongotemplate.findOne(new Query(Criteria.where("_id").is(t.getUserId())), userEntity.class);
					List<TaskEntity> usertasks = user.getUserInfo().getUserTask();
					for(TaskEntity task:usertasks) {
						if(task.getTaskId().equals(t.getTaskId())) {
							usertasks.remove(task);
							user.getUserInfo().setUserTask(usertasks);
							break;
						}
					}
					mongotemplate.remove(t);
				}else {
					t.setPeriod(t.getPeriod()+1);
					mongotemplate.save(t);
				}
			}
			
		}
	}
	@Override
	public Map<String,String> checkTaskStatus(String taskId) {
        Map<String,String> status = new LinkedHashMap<String,String>();
        taskId=taskId.replaceAll("\"", "");
        taskId=taskId.replace("[", "");
        taskId=taskId.replace("]", "");
        String[] taskIds = taskId.split(",");
		for(String s:taskIds) {
			Criteria c = new Criteria();
			c.andOperator(Criteria.where("_id").is(s),Criteria.where("period").lte(7));
			TaskEntity task = mongotemplate.findOne(new Query(c), TaskEntity.class);
			if(task!=null) {
				status.put(s, task.getTaskStatus()); 
			}		
		}
		return status;
	}
    
}
