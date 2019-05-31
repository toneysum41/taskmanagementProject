package com.tastmanager.test.Dao;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/23/2018
 * @Modify 
 * 
*/
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import DBconnection.userEntity;

public interface UserDao {
	void createCollection(String collectionName);
	
	void saveUser(userEntity userentity);
	
	void removeUser(String CollectionName, long id);
	
	boolean createUser(List<String> userInfo);
	
	void updateUser(userEntity userentity, String CollectionName);
	
	void updateCertainInfo(Criteria criteria, String CollectionName, String fieldName, String updateInfo);
	
	userEntity finduserEntityById(long id);
	
}
