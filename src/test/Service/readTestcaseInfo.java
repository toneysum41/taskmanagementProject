package com.tastmanager.test.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tastmanager.test.serviceInterface.dashboardInfo;

import DBconnection.IssueEntity;
import DBconnection.ResultEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.statisticEntity;

@Service
public class readTestcaseInfo implements dashboardInfo {
    @Resource
    private MongoTemplate mongotemplate;
	@Override
	public Map<String,String> getInfoForCaseStatistic() {
    	Map<String,String> backendInfo = new HashMap<String,String>();
    	long totalnum = mongotemplate.count(new Query(Criteria.where("casePriority").in("1","2","3")), TestcaseInfoEntity.class);
    	statisticEntity resultstatistic = mongotemplate.findOne(new Query(Criteria.where("Title").is("weeklyTestresult")),statisticEntity.class);
    	backendInfo.put("totalnumber", String.valueOf(totalnum));
    	if(resultstatistic!=null) {     	
        	backendInfo.put("weeklyissuerate", resultstatistic.getIssueRate());
        	backendInfo.put("isserratechange", resultstatistic.getIssuerateChange());
        	backendInfo.put("weeklychange", resultstatistic.getWeeklytestedchange());
        	backendInfo.put("weeklytested", String.valueOf(resultstatistic.getWeeklytestedcase()));
    	}
		return backendInfo;
	}
	
	@Override
	@Async
	@Scheduled(cron = "0 0 6 * * MON")
	public void updateStatisticInfoForCase() {
		long issuerate = 0, issuerateChange=0;
    	Query q =new Query();
    	Criteria c = new Criteria();
    	c.andOperator(Criteria.where("result").ne(""),Criteria.where("life").lt("7"));
    	long weeklytestednum = mongotemplate.count(q.addCriteria(c), ResultEntity.class);
    	long weeklyissuenum = mongotemplate.count(new Query(Criteria.where("life").lt("7")), IssueEntity.class);
    	statisticEntity resultstatistic = mongotemplate.findOne(new Query(Criteria.where("Title").is("weeklyTestresult")),statisticEntity.class);

    	if(resultstatistic!=null){
    		
    		resultstatistic.setPreviousweeklyissuenum(resultstatistic.getWeeklyissuenum());
    		resultstatistic.setPreWeektestedcase(resultstatistic.getWeeklytestedcase());
    		resultstatistic.setWeeklyissuenum(weeklyissuenum);
    		resultstatistic.setWeeklytestedcase(weeklytestednum);
    		if(weeklytestednum>=resultstatistic.getPreWeektestedcase()) {
    			resultstatistic.setWeeklytestedchange("Increase "+String.valueOf((weeklytestednum-resultstatistic.getPreWeektestedcase())/resultstatistic.getPreWeektestedcase()));
    		}else {
    			resultstatistic.setWeeklytestedchange("Decrease "+String.valueOf((resultstatistic.getPreWeektestedcase()-weeklytestednum)/resultstatistic.getPreWeektestedcase()));
    		}
        	if(weeklytestednum!=0) {
        		issuerate = (weeklyissuenum/weeklytestednum)*100;
        		if(weeklyissuenum>resultstatistic.getPreviousweeklyissuenum()) {
            		issuerateChange = (weeklyissuenum/weeklytestednum)*100 - (resultstatistic.getPreviousweeklyissuenum()/weeklytestednum)*100;
            		resultstatistic.setIssuerateChange("Increase "+String.valueOf(issuerateChange));
        		}else {
        			issuerateChange = (resultstatistic.getPreviousweeklyissuenum()/weeklytestednum)*100 - (weeklyissuenum/weeklytestednum)*100;
        			resultstatistic.setIssuerateChange("Decrease "+String.valueOf(issuerateChange));
        		}
        	}else {
        		issuerate=0;
        		issuerateChange = 0;
        	} 
        	resultstatistic.setIssueRate(String.valueOf(issuerate));
    		mongotemplate.save(resultstatistic);		
    	}else {
    		statisticEntity resultstat = new statisticEntity();
    		resultstat.setTitle("weeklyTestresult");
    		resultstat.setPreviousweeklyissuenum(0);
    		resultstat.setPreWeektestedcase(0);
    		resultstat.setWeeklytestedchange("None");
    		resultstat.setWeeklyissuenum(weeklyissuenum);
    		resultstat.setWeeklytestedcase(weeklytestednum);
    		resultstat.setIssueRate("None");
    		resultstat.setIssuerateChange("0");
    		mongotemplate.save(resultstat);
    	}
	}
}
