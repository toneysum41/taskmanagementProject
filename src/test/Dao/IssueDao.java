package com.tastmanager.test.Dao;

import DBconnection.IssueEntity;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 3/18/2019
 * @Modify 
 * 
*/
public interface IssueDao {
	  void saveIssue(IssueEntity issueentity);
	  
      void creatNewIssue(String[] issueInfo);
      
      void updateIssue();
}
