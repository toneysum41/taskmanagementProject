package com.tastmanager.test.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 3/04/2019
 * @Modify 
 * 
*/
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tastmanager.test.Dao.IssueDao;
import com.tastmanager.test.requestBody.issueListCondition;
import com.tastmanager.test.serviceInterface.resultService;

import DBconnection.IssueEntity;
import DBconnection.ResultEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.userEntity;
import DBconnection.userInfoEntity;

@Component
@EnableScheduling
public class resultImpl implements resultService {
	@Resource
	private MongoTemplate mongotemplate;
	
	@Autowired
	private IssueDao issueDao;
	
	private static final Logger logger = LoggerFactory.getLogger(resultImpl.class);
	private static final SimpleDateFormat dateFromat = new SimpleDateFormat("HH:mm:ss");
	@Override
	@Async
	@Scheduled(cron = "0 30 6 * * ?")
	public void updateResultLife() {	
			List<ResultEntity> results= mongotemplate.findAll(ResultEntity.class);
			for(ResultEntity r : results) {
				if(r.getLife().equals("")) {
					r.setLife("1");
					mongotemplate.save(r);
				}else {
					if(r.getLife().equals("90")) {
						mongotemplate.remove(r);
					}else {
						r.setLife(String.valueOf(Integer.valueOf(r.getLife())+1));
						mongotemplate.save(r);
					}
				}
				
			}
			logger.info("Start to refresh result life at: "+ dateFromat.format(new Date()));
		
	}
	@Override
	public void analysisResult(String bedgeId, String taskId) {	
		int untested = 0;
		int issueNum = 0;
		List<String> digit = new ArrayList<String>();
		int passrateNum=0;
		Criteria c = new Criteria();
		c.andOperator(Criteria.where("userId").is(bedgeId),Criteria.where("taskId").is(taskId));
		List<ResultEntity> results = mongotemplate.find(new Query(c), ResultEntity.class);
		userInfoEntity userinfo = mongotemplate.findOne(new Query(Criteria.where("userId").is(bedgeId)), userInfoEntity.class);
		int totalNum = results.size();
		for(ResultEntity r :results) {
			TestcaseInfoEntity t =mongotemplate.findOne(new Query(Criteria.where("caseName").is(r.getCaseName())), TestcaseInfoEntity.class);		
			if(t!=null) {
				int testedtimes =Integer.valueOf(t.getTestedTimes())+1;
				t.setTestedTimes(String.valueOf(testedtimes));
				if(r.getResult().equals("")) {
					untested++;
				}else if(r.getResult().equals("f")) {
					issueNum++;
					String passrate= t.getCasePassRate();
					for(int x=0;x<passrate.length();x++) {
						if(passrate.charAt(x)!='%') {
							digit.add(String.valueOf(passrate.charAt(x)));
						}
					}
					if(digit.size()==2) {
						passrateNum=Integer.parseInt(digit.get(0))*10+Integer.parseInt(digit.get(1));
					}else {
						passrateNum=Integer.parseInt(digit.get(0));
					}
					Double newIssueRate = Math.floor((((testedtimes-(passrateNum/100)*testedtimes)+1)/testedtimes)*100);
					t.setCasePassRate(String.valueOf(100-newIssueRate)+"%");
					String[] issueInfo= {r.getCaseName(),r.getLanguage(),r.getSoftwareVersion(),r.getDate(),r.getDescription(),r.getUserId()};
					issueDao.creatNewIssue(issueInfo);
					try {
						int week = dayForWeek(r.getDate().split(" ")[0]);
						if(week>0 && week<6){
							userinfo.setUserIssueFoundperWeek(String.valueOf(Integer.parseInt(userinfo.getUserIssueFoundperWeek())+issueNum));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		int testedNum = totalNum-untested;
		if(userinfo!=null) {
			if( userinfo.getUserWorkload()>=0) {
				userinfo.setUserWorkload(userinfo.getUserWorkload()+totalNum);
				float efficiencyD = testedNum/totalNum*100;
				double efficientcyW = userinfo.getUserEfficency();
				efficientcyW = (efficientcyW+efficiencyD)/2;
				userinfo.setUserEfficency(efficientcyW);
			}else {
				userinfo.setUserWorkload(0);
			}
		}
		mongotemplate.save(userinfo);
	}

	
	@Override
	public List<ResultEntity> getPartialResultList(issueListCondition issueconditions){
		Query query = new Query();
		List<Criteria> conditions = new ArrayList<Criteria>();   
		int j=0;
	    if(issueconditions.getStatus().length>0) {
	    	if(issueconditions.getStatus().length>1) {
		    	Criteria[] tempc = new Criteria[issueconditions.getStatus().length];
    	    	for(String s: issueconditions.getStatus()) {
    	    		tempc[j]=Criteria.where("IssueStatus").is(s);
    	    		j++;
    	    	}
			Criteria criteria =new Criteria().orOperator(tempc);
			conditions.add(criteria);
	    	}else {
	    		conditions.add(Criteria.where("IssueStatus").is(issueconditions.getStatus()[0]));
	    	}
	    }
	    if(issueconditions.getLanguage().length>0) {
	    	if(issueconditions.getLanguage().length>1) {
		    	Criteria[] tempc = new Criteria[issueconditions.getLanguage().length];
    	    	for(String s: issueconditions.getLanguage()) {
    	    		tempc[j]=Criteria.where("language").is(s);
    	    		j++;
    	    	}
			Criteria criteria =new Criteria().orOperator(tempc);
			conditions.add(criteria);
	    	}else {
	    		conditions.add(Criteria.where("language").is(issueconditions.getLanguage()[0]));
	    	}
	    }
	    if(issueconditions.getSoftwareversion().length>0) {
	    	if(issueconditions.getSoftwareversion().length>1) {
		    	Criteria[] tempc = new Criteria[issueconditions.getSoftwareversion().length];
    	    	for(String s: issueconditions.getSoftwareversion()) {
    	    		tempc[j]=Criteria.where("softwareVersion").is(s);
    	    		j++;
    	    	}
			Criteria criteria =new Criteria().orOperator(tempc);
			conditions.add(criteria);
	    	}else {
	    		conditions.add(Criteria.where("softwareVersion").is(issueconditions.getSoftwareversion()[0]));
	    	}
	    }
	    if(issueconditions.getSystemversion().length>0) {
	    	if(issueconditions.getSystemversion().length>1) {
		    	Criteria[] tempc = new Criteria[issueconditions.getSystemversion().length];
    	    	for(String s: issueconditions.getSystemversion()) {
    	    		tempc[j]=Criteria.where("systemSpecVersion").is(s);
    	    		j++;
    	    	}
			Criteria criteria =new Criteria().orOperator(tempc);
			conditions.add(criteria);
	    	}else {
	    		conditions.add(Criteria.where("systemSpecVersion").is(issueconditions.getSystemversion()[0]));
	    	}
	    }
	    if(issueconditions.getTestername().length>0) {
	    	if(issueconditions.getTestername().length>1) {
		    	Criteria[] tempc = new Criteria[issueconditions.getTestername().length];
    	    	for(String s: issueconditions.getTestername()) {
    	    		String userid = mongotemplate.findOne(new Query(Criteria.where("userName").is(s)), userEntity.class).getId();
    	    		tempc[j]=Criteria.where("userId").is(userid);
    	    		j++;
    	    	}
			Criteria criteria =new Criteria().orOperator(tempc);
			conditions.add(criteria);
	    	}else {
	    		String userid = mongotemplate.findOne(new Query(Criteria.where("userName").is(issueconditions.getTestername()[0])), userEntity.class).getId();
	    		conditions.add(Criteria.where("userId").is(userid));
	    	}
	    }
	    if(issueconditions.getStartpoint().isEmpty() && !issueconditions.getStartpoint().equals("head")) {
	    	ResultEntity testcase = mongotemplate.findOne(new Query(Criteria.where("caseName").is(issueconditions.getStartpoint())), ResultEntity.class);
	    	conditions.add(Criteria.where("_id").gt(testcase.getCaseId()));
	    }
	    
	    Criteria[] c = new Criteria[conditions.size()];
		if(conditions != null && conditions.size()>0) {
			Criteria criteria = new Criteria();
			if(conditions.size()>1) {
		        for(int i=0; i<conditions.size();i++) { 
		        	c[i]=conditions.get(i);
		        }
				criteria =new Criteria().andOperator(c);
			}else {
				criteria = conditions.get(0);
			}
			query.addCriteria(criteria);
			query.with(new Sort(Sort.Direction.ASC, "_id"));
			query.limit(1000);
		    return mongotemplate.find(query, ResultEntity.class);
		}else {
			Query query1 = new Query();
			query1.with(new Sort(Sort.Direction.ASC, "_id"));
			query1.limit(1000);
			return mongotemplate.find(query1, ResultEntity.class);
		}
	}
	
	 public static int dayForWeek(String pTime) throws Exception {
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  c.setTime(format.parse(pTime));
		  int dayForWeek = 0;
		  if(c.get(Calendar.DAY_OF_WEEK) == 1){
		   dayForWeek = 7;
		  }else{
		   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		  }
		  return dayForWeek;
		 }
}
