package com.tastmanager.test.Dao;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/24/2018
 * @Modify 
 * 
*/

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import DBconnection.ResultEntity;
import DBconnection.userEntity;

@Component
public class UserDaoImpl implements UserDao{
	
	@Resource
	private MongoTemplate mongotemplate;
	
	@Autowired
	private UserInfoDao userinfoDao;
	@Override
	public void createCollection(String collectionName) {
		mongotemplate.createCollection(collectionName);
	}
	
	@Override
	public void saveUser(userEntity userEntity) {
		mongotemplate.save(userEntity);
	}
	
	@Override
	public boolean createUser(List<String> userInfo) {
		if(!userInfo.isEmpty()) {
			 Query q = new Query(Criteria.where("_id").is(userInfo.get(0)).andOperator(Criteria.where("userName").is(userInfo.get(1))));
			 if(mongotemplate.find(q, userEntity.class).isEmpty()) {
					userEntity userentity = new  userEntity();
					userentity.setId(userInfo.get(0));
					userentity.setUserName(userInfo.get(1));
					userentity.setUserPassword(userInfo.get(2));
					userentity.setStatus(userInfo.get(3));
					userentity.setTitle(userInfo.get(4));
					userentity.setAuthorityLevel(userInfo.get(5));				
		        	String[] userId = {userInfo.get(0)};
		        	userentity.setUserInfo(userinfoDao.creatNewUserInfo(userId));
		        	saveUser(userentity);
					return true;
			 }else {
				 return false;
			 }
		}
		 return false;
	}
	
	@Override
	public void removeUser(String CollectionName, long id) {
		Query query = new Query(Criteria.where("_id").is(id));
		mongotemplate.remove(query,CollectionName);
	}
	
	@Override
	public void updateUser(userEntity userEntity, String CollectionName) {
		Query query = new Query(Criteria.where("_id").is(userEntity.getId()));
		
		Update update = new Update();
		
		update.set("userName", userEntity.getUserName());
		update.set("userPassword", userEntity.getUserPassword());
		update.set("userWorkload", userEntity.getEmail());
		update.set("status", userEntity.getStatus());
		update.set("Title", userEntity.getteamTitle());
		update.set("authorityLevel", userEntity.getAuthorityLevel());
		
		mongotemplate.updateFirst(query, update, CollectionName);
	}
	
	@Override
	public void updateCertainInfo(Criteria criteria, String CollectionName, String fieldName, String updateInfo) {
		Query query = new Query(criteria);
		Update update = new Update();
		update.set(fieldName, updateInfo);
		mongotemplate.updateFirst(query, update, CollectionName);
	}

	@Override
	public userEntity finduserEntityById(long id) {
		Query query = new Query(Criteria.where("_id").is(id));
		userEntity userEntity = mongotemplate.findOne(query, userEntity.class);
		return userEntity;
	}



}
