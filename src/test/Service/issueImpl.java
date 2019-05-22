package com.tastmanager.test.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.tastmanager.test.Dao.IssueDao;
import com.tastmanager.test.serviceInterface.issueService;

import DBconnection.IssueEntity;
/**
 * Author: Terry
 * Data: 5.17.2019
 */
import DBconnection.ResultEntity;

public class issueImpl implements issueService {
	@Resource
	private MongoTemplate mongotemplate;
	
	@Autowired
	private IssueDao issueDao;
	
	private static final Logger logger = LoggerFactory.getLogger(resultImpl.class);
	private static final SimpleDateFormat dateFromat = new SimpleDateFormat("HH:mm:ss");
	@Override
	@Async
	@Scheduled(cron = "0 0 5 * * ?")
	public void updateIssueLife() {
		List<IssueEntity> Issues= mongotemplate.findAll(IssueEntity.class);
		for(IssueEntity i : Issues) {
			if(i.getLife().isEmpty()) {
				i.setLife("1");
				mongotemplate.save(i);
			}else {
				if(Integer.parseInt(i.getLife())>=180) {
					mongotemplate.remove(i);
				}else {
					i.setLife(String.valueOf(Integer.parseInt(i.getLife())+1));
					mongotemplate.save(i);
				}
			}
			
		}
		logger.info("Start to refresh issue result life at: "+ dateFromat.format(new Date()));
		
	}

	@Override
	public void analysisIssue(String bedgeId, String taskId) {
		// TODO Auto-generated method stub
		
	}

}
