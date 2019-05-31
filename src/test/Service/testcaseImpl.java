package com.tastmanager.test.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tastmanager.test.serviceInterface.testcaseService;

import DBconnection.TestcaseInfoEntity;

@Service
@EnableScheduling
public class testcaseImpl implements testcaseService {
	@Resource
	private MongoTemplate mongotemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(testcaseImpl.class);
	private static final SimpleDateFormat dateFromat = new SimpleDateFormat("HH:mm:ss");
	@Override
	@Async
	@Scheduled(cron = "0 0 6 * * MON-FRI")
	public void updateTestcaseInfo() {
		logger.info("Start to refresh task life at: "+ dateFromat.format(new Date()));
		Query q = new Query(Criteria.where("timeBeforeAvalible").gt("0"));
		List<TestcaseInfoEntity> TI = mongotemplate.find(q, TestcaseInfoEntity.class);
		for(TestcaseInfoEntity t:TI) {
			t.setTimeBeforeAvalible(String.valueOf(Integer.parseInt(t.getTimeBeforeAvalible())-1));
			mongotemplate.save(t);
		}
	}
}
