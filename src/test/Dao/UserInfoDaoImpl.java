package com.tastmanager.test.Dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import DBconnection.TaskEntity;
import DBconnection.userInfoEntity;

@Component
public class UserInfoDaoImpl implements UserInfoDao{
	@Resource
	private MongoTemplate mongotemplate;
	@Override
	public void saveUserInfo(userInfoEntity issueentity) {
		// TODO Auto-generated method stub
		mongotemplate.insert(issueentity);
	}

	@Override
	public userInfoEntity creatNewUserInfo(String[] userInfo) {
		// TODO Auto-generated method stub
		List<TaskEntity> l = new ArrayList<TaskEntity>() {
		private static final long serialVersionUID = 1L;};
		userInfoEntity userinfo = new userInfoEntity();
		userinfo.setUserId(userInfo[0]);
		userinfo.setUserAttendence("0");
		userinfo.setUserEfficency(100.0);
		userinfo.setUserWorkload(0);
		userinfo.setUserIssueFoundperWeek("0");
		userinfo.setUserTask(l);
		saveUserInfo(userinfo);
		return userinfo;
	}

	@Override
	public void updateUserInfo() {
		// TODO Auto-generated method stub
		
	}

}
