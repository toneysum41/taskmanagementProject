package com.tastmanager.test.Dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;

public interface TestcaseDao {
	/**
	 * read xml file and store the new testcases in Testcase_collection
	 */
	void createTestcases(String filename, String generalcategory, String username);
	
	
	/**
	 * Create new TestcaseInfo document in TestcaseInfo_collection by read the data from Testcase_collection
	 */
	void createTestcaseInfo();
	
	
	/**
	 * Update data in TestcaseInfo_collection
	 */
	void saveTestcaseInfo(TestcaseInfoEntity testcaseinfo);
	
	/**
	 * Delete data in TestcaseInfo_collection through certain condition
	 */
	void deleteTestcaseInfo(Query query, String collectionName);
	
	/**
	 * Delete all data in TestcaseInfo_collection
	 */
	void deleteAll(TestcaseInfoEntity testcaseinfo);
	
	/**
	 * 
	 * @param CollectionName
	 * @return
	 */
	List<TestcaseInfoEntity> findAllcaseInfoEntity(String CollectionName);
	
	/**
	 * 
	 * @param criteria
	 * @param skipnum
	 * @param limitnum
	 * @return
	 */
	List<TestcaseInfoEntity> findcaseInfoEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum);
	
	/**
	 * 
	 * @param criteria
	 * @param skipnum
	 * @param limitnum
	 * @return
	 */
	List<TestcaseEntity> findcaseEntityByCertainCondition(Criteria criteria, long skipnum, int limitnum);
	
}
