package com.tastmanager.test.Dao;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 1/24/2019
 * @Modify 
 * 
*/

import java.util.List;
import java.util.Map;

import DBconnection.TaskEntity;


public interface taskDao {
	void saveResult(TaskEntity userentity);
	
	TaskEntity recordTask(String[] taskinfo);
	
}
