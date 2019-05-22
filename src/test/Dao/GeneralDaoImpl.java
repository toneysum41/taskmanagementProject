package com.tastmanager.test.Dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

import DBconnection.ResultEntity;
import DBconnection.TaskEntity;
import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.userEntity;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 2/24/2019
 * @Modify 
 * 
*/
@Component
public class GeneralDaoImpl implements GeneralDao {
	@Resource
	private MongoTemplate mongotemplate; 
	
	@Override
	public Criteria setCriteriaByAndCondition(List<String[]> conditions) {
		Criteria[] temp = new Criteria[conditions.size()];
		for(int i=0; i<conditions.size(); i++) {
			switch(conditions.get(i)[2]) {
			case "is":
				temp[i] = Criteria.where(conditions.get(i)[0]).is(conditions.get(i)[1]);
				break;
			case "in":
				temp[i] = Criteria.where(conditions.get(i)[0]).in(conditions.get(i)[1]);
				break;
			case "lt":
				temp[i] = Criteria.where(conditions.get(i)[0]).lt(conditions.get(i)[1]);	
				break;
			case "lte":
				temp[i] = Criteria.where(conditions.get(i)[0]).lte(conditions.get(i)[1]);	
				break;	
			case "gt":
				temp[i] = Criteria.where(conditions.get(i)[0]).gt(conditions.get(i)[1]);	
				break;
			case "gte":
				temp[i] = Criteria.where(conditions.get(i)[0]).gte(conditions.get(i)[1]);	
				break;	
			case "ne":
				temp[i] = Criteria.where(conditions.get(i)[0]).ne(conditions.get(i)[1]);	
				break;		
			case "regex":
				temp[i] = Criteria.where(conditions.get(i)[0]).regex(conditions.get(i)[1]);	
				break;	
			}
		}
		Criteria criteria = new Criteria();
		criteria = temp[0];
		for(int j=1; j<conditions.size();j++) {
			criteria = criteria.andOperator(criteria,temp[j]);
		}		
		return criteria;
	}

	@Override
	public Criteria setCriteriaByOrCondition(List<String[]> conditions) {
		Criteria[] temp = new Criteria[conditions.size()];
		for(int i=0; i<conditions.size(); i++) {
			switch(conditions.get(i)[2]) {
			case "is":
				temp[i] = Criteria.where(conditions.get(i)[0]).is(conditions.get(i)[1]);
				break;
			case "in":
				temp[i] = Criteria.where(conditions.get(i)[0]).in(conditions.get(i)[1]);
				break;
			case "lt":
				temp[i] = Criteria.where(conditions.get(i)[0]).lt(conditions.get(i)[1]);	
				break;
			case "lte":
				temp[i] = Criteria.where(conditions.get(i)[0]).lte(conditions.get(i)[1]);	
				break;	
			case "gt":
				temp[i] = Criteria.where(conditions.get(i)[0]).gt(conditions.get(i)[1]);	
				break;
			case "gte":
				temp[i] = Criteria.where(conditions.get(i)[0]).gte(conditions.get(i)[1]);	
				break;	
			case "ne":
				temp[i] = Criteria.where(conditions.get(i)[0]).ne(conditions.get(i)[1]);	
				break;		
			case "regex":
				temp[i] = Criteria.where(conditions.get(i)[0]).regex(conditions.get(i)[1]);	
				break;	
			}
		}
		Criteria criteria = new Criteria();
		criteria = temp[0];
		for(int j=1; j<conditions.size();j++) {
			criteria = criteria.orOperator(criteria,temp[j]);
		}
		return criteria;
	}
	
	@Override
	public void updateFeilds(String CollectionName, Map<String, String> updateInfo) {
		Update update = new Update();
		Set<String> keySet = updateInfo.keySet();
		for(String key : keySet) {
			if(!existsField(CollectionName, updateInfo.get(key))) {
				update.rename(key, updateInfo.get(key));
			}
		}
		if(!update.toString().equals("{}")) {
			mongotemplate.updateMulti(new Query(), update, CollectionName);
		}
	}
	
	private boolean existsField(String collectionName, String fieldName) {
		BasicDBObject dbObject = new BasicDBObject();
		BasicDBObject dbObjectOp = new BasicDBObject();
		dbObjectOp.put("$exists", true);
		dbObject.put(fieldName, dbObjectOp);
		MongoCursor<Document> count = mongotemplate.getCollection(collectionName).find(dbObject).iterator();
		if(count.hasNext()) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Object readCertainInfo(String CollectionName, Criteria criteria, String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<userEntity> finduserEntityByCertainCondition(Criteria criteria, String CollectionName) {
		Query query = new Query(criteria);
		List<userEntity> userEntity = mongotemplate.find(query, userEntity.class, CollectionName);
		return userEntity;
	}

	@Override
	public List<userEntity> finduserEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum) {
		Query query = new Query(criteria);
		query.skip(skipnum).limit(limitnum);
		List<userEntity> userEntity = mongotemplate.find(query, userEntity.class);
		return userEntity;
	}

	@Override
	public List<TaskEntity> findtaskEntityByCertainCondition(Criteria criteria, String CollectionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskEntity> findtaskEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResultEntity> findresultEntityByCertainCondition(Criteria criteria, String CollectionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResultEntity> findresultEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void removeDocByCertainCondition(Criteria criteria, String CollectionName) {
		Query query = new Query(criteria);
		mongotemplate.remove(query, CollectionName);
	}
}
