package com.tastmanager.test.serviceInterface;

import java.util.List;

import com.tastmanager.test.requestBody.issueListCondition;

import DBconnection.ResultEntity;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 3/18/2019
 * @Modify 
 * 
*/
public interface resultService {
	void updateResultLife();
	
	void analysisResult(String bedgeId, String taskId);
	
	List<ResultEntity> getPartialResultList(issueListCondition issueconditions);
}
