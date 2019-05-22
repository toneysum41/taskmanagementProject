package com.tastmanager.test.Dao;

import java.util.Map;

import org.springframework.data.mongodb.core.query.Update;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 2/20/2019
 * @Modify 
 * 
*/

public interface StatisticDao {
	//Map<String,String> readUserInfo(String username); 
    void saveCurrentStatisticResult();
    
    void updateStatisticResult();

}
