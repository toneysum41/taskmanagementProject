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

import org.springframework.data.mongodb.core.query.Criteria;

import com.tastmanager.test.requestBody.issueListCondition;

import DBconnection.ResultEntity;

public interface resultDao {
	void createCollection(String collectionName);
	
	void saveResult(ResultEntity userentity);
	
	void recordResult(List<String[]> results);
	
	void updateResult();
	
	List<ResultEntity> readResult(Criteria criteria);
}
