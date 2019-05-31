package com.tastmanager.test.Dao;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 1/23/2019
 * @Modify 
 * 
*/
import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import DBconnection.ResultEntity;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 2/25/2018
 * @Modify 
 * 
*/
@Component
public class resultDaoImpl implements resultDao{
	@Resource
	private MongoTemplate mongotemplate;
	@Override
	public void createCollection(String collectionName) {
		mongotemplate.createCollection(collectionName);
	}

	@Override
	public void saveResult(ResultEntity resultentity) {
	    mongotemplate.insert(resultentity);
	}

	@Override
	public void recordResult(List<String[]> results) {
		if(!results.isEmpty()) {
			 for(String[] result : results) {
				 Criteria criteria = new Criteria().andOperator(Criteria.where("caseName").is(result[0]), Criteria.where("language").is(result[1]));
				 Query q = new Query(criteria);
				 if(mongotemplate.find(q, ResultEntity.class).isEmpty()) {
			       	 ResultEntity resultEntity = new ResultEntity();
			       	 resultEntity.setCaseName(result[0]);
			       	 resultEntity.setLanguage(result[1]);
			       	 resultEntity.setSoftwareVersion(result[2]);
			       	 resultEntity.setDate(result[3]);
			       	 resultEntity.setResult(result[4]);
			       	 resultEntity.setDescription(result[5]);
			       	 resultEntity.setOccurence(result[6]);
			       	 resultEntity.setUserId(result[7]);
			       	 resultEntity.setLife(result[8]);
			       	 resultEntity.setIssueStatus(result[9]);
			       	 resultEntity.setTaskId(result[10]);
			       	 resultEntity.setSystemSpecVersion(result[11]);
			       	 saveResult(resultEntity);

				 }
			
			 }
		}
	}

	@Override
	public void updateResult() {
		Update update = new Update();
		List<ResultEntity> result = mongotemplate.findAll(ResultEntity.class);
		for(ResultEntity r : result) {
			if(Integer.parseInt(r.getLife())<60) {
				update.set("life", String.valueOf(Integer.parseInt(r.getLife())+1));
			}else {
				mongotemplate.remove(r);
			}
		}
	}
	
	@Override
	public List<ResultEntity> readResult(Criteria criteria) {
		Query q = new Query(criteria);
		List<ResultEntity> r = mongotemplate.find(q, ResultEntity.class);
		return r;
	}
}



