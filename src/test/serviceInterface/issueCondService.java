package com.tastmanager.test.serviceInterface;

import java.util.List;

import com.tastmanager.test.requestBody.issueListCondition;

import DBconnection.IssueEntity;

public interface issueCondService {
	/**
	 * 
	 * @param issueconditions
	 * @return
	 */
	List<IssueEntity> getPartialIssueList(issueListCondition issueconditions);
}
