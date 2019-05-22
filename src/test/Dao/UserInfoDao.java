package com.tastmanager.test.Dao;

import DBconnection.userInfoEntity;

public interface UserInfoDao {
	  void saveUserInfo(userInfoEntity issueentity);
	  
	  userInfoEntity creatNewUserInfo(String[] userInfo);
      
      void updateUserInfo();
}
