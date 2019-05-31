package com.tastmanager.test.serviceInterface;

import java.util.List;

import com.tastmanager.test.requestBody.issueListCondition;

import DBconnection.ResultEntity;

public interface issueService {
	void updateIssueLife();
	
	void analysisIssue(String bedgeId, String taskId);
	
	//List<ResultEntity> getPartialResultList(issueListCondition issueconditions);
}