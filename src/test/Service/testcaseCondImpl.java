package com.tastmanager.test.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.tastmanager.test.Dao.GeneralDao;
import com.tastmanager.test.Dao.TestcaseDao;
import com.tastmanager.test.serviceInterface.testcaseCondService;

import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;

@Component
public class testcaseCondImpl implements testcaseCondService {
	@Resource
	private MongoTemplate mongotemplate; 
	@Autowired
	private TestcaseDao testcasedao;
	@Autowired
	private GeneralDao generaldao;
	private static final Logger LOGGER = LoggerFactory.getLogger(testcaseCondImpl.class);
	@Override
	public List<TestcaseInfoEntity>  getPartailTestcaseInfoList(testcaseTableCond testcaseCond) {
		
		Query query = new Query();
		List<Criteria> conditions = new ArrayList<Criteria>();
		if(!testcaseCond.getGeneralcategory().equals("All")) {
				conditions.add(Criteria.where("generalCategory").is(testcaseCond.getGeneralcategory()));	
		}
		if(testcaseCond.getCategory().length!=0) {
			if(testcaseCond.getCategory().length>1) {
				List<Criteria> temp = new ArrayList<Criteria>();
				for(String s : testcaseCond.getCategory()) {
					if(s.equals("All")) {
						temp.clear();
						break;
					}else {
						temp.add(Criteria.where("category").is(s));
					}
				}
		    	Criteria[] tempc = new Criteria[temp.size()];
		    	if(temp != null && temp.size()>0) {
		            for(int i=0; i<temp.size();i++) { 
		            	tempc[i]=temp.get(i);
		            }
					Criteria criteria =new Criteria().orOperator(tempc);
					conditions.add(criteria);
				}
			}else {
				if(!testcaseCond.getCategory()[0].equals("All")) {
					conditions.add(Criteria.where("category").is( testcaseCond.getCategory()[0]));			
				}
			}
		}
	    if(!testcaseCond.getTestcategory().equals("All") && !testcaseCond.getTestcategory().equals("None")) {
	    	conditions.add(Criteria.where("testcategory").is(testcaseCond.getTestcategory()));
	    }
	    if(!testcaseCond.getPeriod().equals("None")) {
	    	switch(testcaseCond.getPeriod()) {
	    	case "All":
	    		Criteria c = new Criteria();
	    		c.orOperator(Criteria.where("period").gt("7"),Criteria.where("period").is("0"));
	    		conditions.add(c);
	    		break;
	    	case "Last 1 week":
	    		conditions.add(Criteria.where("period").gt("7"));
	    		conditions.add(Criteria.where("period").lt("15"));
	    		break;
	    	case "Last 2 weeks":
	    		conditions.add(Criteria.where("period").gt("7"));
	    		conditions.add(Criteria.where("period").lt("22"));
	    		break;
	    	case "Last 1 Month":
	    		conditions.add(Criteria.where("period").gt("7"));
	    		conditions.add(Criteria.where("period").lt("29"));
	    		break;
	    	case "Last 3 Months":
	    		conditions.add(Criteria.where("period").gt("7"));
	    		conditions.add(Criteria.where("period").lt("91"));
	    		break;
	    	default:
	    		break;
	    	}
	    }
		if(testcaseCond.getSystembuild().length!=0) {
			if(testcaseCond.getSystembuild().length>1) {
				List<Criteria> temp = new ArrayList<Criteria>();
				for(String s : testcaseCond.getSystembuild()) {
					if(s.equals("All")) {
						temp.clear();
						break;
					}else {
						temp.add(Criteria.where("systemBuild").is(s));
					}
				}
		    	Criteria[] tempc = new Criteria[temp.size()];
		    	if(temp != null && temp.size()>0) {
		            for(int i=0; i<temp.size();i++) { 
		            	tempc[i]=temp.get(i);
		            }
					Criteria criteria =new Criteria().orOperator(tempc);
					conditions.add(criteria);
				}
			}else 
			{
				if(!testcaseCond.getSystembuild()[0].equals("All")) {
	                conditions.add(Criteria.where("systemBuild").is( testcaseCond.getSystembuild()[0]));			
				}
			}
		}
	    if(!testcaseCond.getPassRate().equals("All")) {
	    	switch(testcaseCond.getPassRate()) {
	    	case "less 50%":
	    		conditions.add(Criteria.where("casePassRate").lt("50%"));
	    		break;
	    	case "50% to 60%":
	    		conditions.add(Criteria.where("casePassRate").gt("50%"));
	    		conditions.add(Criteria.where("casePassRate").lt("60%"));
	    		break;
	    	case "60% to 70%":
	    		conditions.add(Criteria.where("casePassRate").gt("60%"));
	    		conditions.add(Criteria.where("casePassRate").lt("70%"));
	    		break;
	    	case "70% to 80%":
	    		conditions.add(Criteria.where("casePassRate").gt("70%"));
	    		conditions.add(Criteria.where("casePassRate").lt("80%"));
	    		break;
	    	case "80% to 90%":
	    		conditions.add(Criteria.where("casePassRate").gt("80%"));
	    		conditions.add(Criteria.where("casePassRate").lt("90%"));
	    		break;
	    	case "more 90%":
	    		conditions.add(Criteria.where("casePassRate").gt("90%"));
	    		break;	
	    	default:
	    		break;
	    	}
	    }
	    if(!testcaseCond.getLanguage().equals("All")) {
	    	conditions.add(Criteria.where("language").is( testcaseCond.getLanguage()));
	    }
	    if(!testcaseCond.getRegexcontent().isEmpty() && !testcaseCond.getRegexcontent().equals("None")) {
	    	conditions.add(Criteria.where("caseName").regex(testcaseCond.getRegexcontent(), "i"));
	    }
	    if(!testcaseCond.getStartpoint().isEmpty() && !testcaseCond.getStartpoint().equals("head")) {
	    	TestcaseInfoEntity testcase = mongotemplate.findOne(new Query(Criteria.where("caseName").is(testcaseCond.getStartpoint())), TestcaseInfoEntity.class);
	    	conditions.add(Criteria.where("_id").gt(testcase.getTestcaseId()));
	    }
	   
	    Criteria[] c = new Criteria[conditions.size()];
		if(conditions != null && conditions.size() == 1) {
			Criteria criteria =new Criteria().andOperator(conditions.get(0));
			query.addCriteria(criteria);
			query.with(new Sort(Sort.Direction.ASC, "_id"));
			query.limit(100);
		    return mongotemplate.find(query, TestcaseInfoEntity.class,"TestcaseInfo_collection");
		}else if(conditions != null && conditions.size() > 1) {
            for(int i=0; i<conditions.size();i++) { 
            	c[i]=conditions.get(i);
            }
			Criteria criteria =new Criteria().andOperator(c);
			query.addCriteria(criteria);
			query.with(new Sort(Sort.Direction.ASC, "_id"));
			query.limit(500);
		    return mongotemplate.find(query, TestcaseInfoEntity.class,"TestcaseInfo_collection");
		}
		else {
			Query query1 = new Query();
			query1.with(new Sort(Sort.Direction.ASC, "_id"));
			query1.limit(100);
			return mongotemplate.find(query1, TestcaseInfoEntity.class,"TestcaseInfo_collection");
		}
	}
	@Override
	public List<TestcaseEntity> getPartailTestcaseList(List<String> testcaseName) {
		List<TestcaseEntity> testcaseEntity = new ArrayList<TestcaseEntity>();
		if(testcaseName != null && testcaseName.size()>0) {
			for(String s : testcaseName) {
				TestcaseEntity temp = mongotemplate.findOne(new Query(Criteria.where("name").is(s)), TestcaseEntity.class);			
				if(temp!=null) {
					testcaseEntity.add(temp);
				}else {
					LOGGER.debug("No name found");
				}
			}
			return testcaseEntity;
		}else {
			LOGGER.debug("No case added");
		}
		return null;
	}
	@Override
	public synchronized void updateTestcaseList(List<String> nameList, Update update1, Update update2) {
	    if(!nameList.isEmpty() && nameList!=null) {
	    	for(String s : nameList) {
	    		mongotemplate.updateFirst(new Query(Criteria.where("caseName").is(s)),update1, TestcaseInfoEntity.class);
	    		mongotemplate.updateFirst(new Query(Criteria.where("name").is(s)),update2, TestcaseEntity.class);
	    	}
	    }	
	}
	@Override
	public synchronized String updateTestcaseInfo(String[] updateinfo) {
		// TODO Auto-generated method stub
		Query q1 =new Query(Criteria.where("caseName").is(updateinfo[0]));
		TestcaseInfoEntity t = mongotemplate.findOne(q1, TestcaseInfoEntity.class);
		if(t.getTimeBeforeAvalible().equals("0")) {
			Update update = new Update();   		
			update.set("timeBeforeAvalible", "5");
			update.set("theRecentTestDate", updateinfo[1]);
			mongotemplate.updateFirst(q1, update, "TestcaseInfo_collection");
			return "Success";
		}else {
			return t.getCaseName();
		}
	}


}
