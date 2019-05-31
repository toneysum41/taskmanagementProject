package com.tastmanager.test.Dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.tastmanager.test.Dao.GeneralDao;
import com.tastmanager.test.Dao.UserDao;
import com.tastmanager.test.Service.pageInfo;

import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;
import contentRead.readTestcaseFile;
import net.sf.json.JSON;

@Component
@Service
public class TestcaseDaoImpl implements TestcaseDao{
	   @Resource
	   private MongoTemplate mongotemplate;   

	   Logger logger = LoggerFactory.getLogger(TestcaseDaoImpl.class);

    @Override   
   	public void createTestcases(String filename, String generalcategory, String username) {
		MongoCollection<Document> c = mongotemplate.getCollection("Testcase_collection");
		readTestcaseFile readTestcaseFile = new readTestcaseFile();
		List<List<List<String>>> testsuiteSet = readTestcaseFile.readTestcase(filename);
		int numC=0,numL=0,numN=0;
		for(List<List<String>> lang : testsuiteSet) {
			for(List<String> category : lang) {
				for(String j : category) {
					Query q = new Query(Criteria.where("name").is(readTestcaseFile.getCasesName().get(numN++)));
					if(mongotemplate.find(q, TestcaseEntity.class,"Testcase_collection").size()==0) {
						Document doc = Document.parse(j);
						doc.append("language", readTestcaseFile.getLanguarages().get(numL));
						doc.append("category", readTestcaseFile.getCategories().get(numC));
						doc.append("generalCategory", generalcategory);
						doc.append("systemBuild", readTestcaseFile.getSystemSpecVersion());
						doc.append("author", username);
						c.insertOne(doc);
					}
				}
				numC++;
			}
			numL++;
		}
   	}
    
    @Override
   	public void createTestcaseInfo() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MongoCollection<Document> c = mongotemplate.getCollection("Testcase_collection");
		//MongoCollection<Document> cI = mongotemplate.getCollection("TestcaseInfo_collection");
		FindIterable<Document> findIterable = c.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		mongoCursor.next();
		while(mongoCursor.hasNext()) {
			Document temp = mongoCursor.next();
			Query q = new Query(Criteria.where("caseName").is(String.valueOf(temp.get("name"))));
			if(mongotemplate.find(q, TestcaseInfoEntity.class).isEmpty()) {
				TestcaseInfoEntity tescaseinfo = new TestcaseInfoEntity();
				tescaseinfo.setCaseName(String.valueOf(temp.get("name")));
				tescaseinfo.setLanguage(String.valueOf(temp.get("language")));
				tescaseinfo.setSystemBuild(new ArrayList<String>());
				tescaseinfo.setTestedTimes("0");
				tescaseinfo.setCasePassRate("0%");
				tescaseinfo.setCasePriority("1");
				tescaseinfo.setGeneralCatagory(String.valueOf(temp.get("generalCategory")));
				tescaseinfo.setTestcategory(new ArrayList<String>());
				tescaseinfo.setCatagory(String.valueOf(temp.get("category")));
				tescaseinfo.setPeriod("0");
				tescaseinfo.setTimeBeforeAvalible("0");
				tescaseinfo.setTheRecentTestDate("None");
				tescaseinfo.setLatestIssue("None");
				tescaseinfo.setAuthor(String.valueOf(temp.get("author")));
				tescaseinfo.setCreateDate(df.format(new Date()));
				mongotemplate.save(tescaseinfo, "TestcaseInfo_collection");
			}
		}
   	}
   	
    @Override
   	public void saveTestcaseInfo(TestcaseInfoEntity testcaseinfo) {
   		mongotemplate.save(testcaseinfo);
   	}
   	
    @Override
   	public void deleteTestcaseInfo(Query query, String collectionName) {
   		mongotemplate.remove(query, collectionName);
   	}
   	
    @Override
   	public void deleteAll(TestcaseInfoEntity testcaseinfo) {
   		mongotemplate.remove(testcaseinfo);
   	}
    
	@Override
	public List<TestcaseInfoEntity> findAllcaseInfoEntity(String CollectionName) {
		Calendar cal=Calendar.getInstance();
		Query q = new Query();
		q.limit(100);
		List<TestcaseInfoEntity> TestcaseInfoEntity = mongotemplate.find(q,TestcaseInfoEntity.class, "TestcaseInfo_collection");
		for(TestcaseInfoEntity t : TestcaseInfoEntity) {
			if(!t.getTheRecentTestDate().equals("None") && t.getTheRecentTestDate().isEmpty()) {
				String temp=StringUtils.substringAfter(t.getTheRecentTestDate(),"-");	 
				String month=StringUtils.substringBefore(temp, "-");
				String date = StringUtils.substringAfter(temp,"-");
				if(cal.get(Calendar.MONTH)==Integer.parseInt(month)) {
					int a = cal.get(Calendar.DATE)-Integer.parseInt(date);
					if(a>=5) {
						t.setTimeBeforeAvalible("0");
					}else {
						t.setTimeBeforeAvalible(String.valueOf(5-a));
					}
				}				
			}
		}
		return TestcaseInfoEntity;
	}

	@Override
	public List<TestcaseInfoEntity> findcaseInfoEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum) {
		Query query = new Query(criteria);
		query.skip(skipnum).limit(limitnum);
		List<TestcaseInfoEntity> TestcaseInfoEntity = mongotemplate.find(query, TestcaseInfoEntity.class);
		return TestcaseInfoEntity;
	}

	@Override
	public List<TestcaseEntity> findcaseEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum) {
		Query query = new Query(criteria);
		query.skip(skipnum).limit(limitnum);
		List<TestcaseEntity> TestcaseEntity = mongotemplate.find(query, TestcaseEntity.class);
		return TestcaseEntity;
	}
	
}
