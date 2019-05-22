package com.tastmanager.test.Dao;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/28/2018
 * @Modify 
 * 
*/

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;

import DBconnection.ResultEntity;
import DBconnection.TaskEntity;
import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.userEntity;

public interface GeneralDao {
	void removeDocByCertainCondition(Criteria criteria, String CollectionName);
	
	Criteria setCriteriaByAndCondition(List<String[]> conditions);
	
	Criteria setCriteriaByOrCondition(List<String[]> conditions);
	
	void updateFeilds(String CollectionName, Map<String, String> updateInfo);
	
	Object readCertainInfo(String CollectionName, Criteria criteria, String fieldName);
	
	List<userEntity> finduserEntityByCertainCondition(Criteria criteria, String CollectionName);
	
	List<userEntity> finduserEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum);
	
	List<TaskEntity> findtaskEntityByCertainCondition(Criteria criteria, String CollectionName);
	
	List<TaskEntity> findtaskEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum);
	
	List<ResultEntity> findresultEntityByCertainCondition(Criteria criteria, String CollectionName);
	
	List<ResultEntity> findresultEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum);
}
