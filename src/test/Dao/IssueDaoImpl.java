package com.tastmanager.test.Dao;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 3/18/2019
 * @Modify 
 * 
*/
import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import DBconnection.IssueEntity;

@Component
public class IssueDaoImpl implements IssueDao{
	@Resource
	private MongoTemplate mongotemplate;
	@Override
	public void saveIssue(IssueEntity issueentity) {
		// TODO Auto-generated method stub
		mongotemplate.insert(issueentity);
	}

	@Override
	public void creatNewIssue(String[] issueInfo) {
		// TODO Auto-generated method stub
		IssueEntity issue = new IssueEntity();
		issue.setCaseName(issueInfo[0]);
		issue.setLanguage(issueInfo[1]);
		issue.setSoftwareVersion(issueInfo[2]);
		issue.setDate(issueInfo[3]);
		issue.setStatus("None");
		issue.setIssueDescription(issueInfo[4]);
		issue.setUserId(issueInfo[5]);
		issue.setLife("1");
		saveIssue(issue);
	}

	@Override
	public void updateIssue() {
		// TODO Auto-generated method stub
		
	}

}
