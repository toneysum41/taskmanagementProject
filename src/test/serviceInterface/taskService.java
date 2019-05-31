package com.tastmanager.test.serviceInterface;

import java.util.List;
import java.util.Map;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 1/24/2019
 * @Modify 
 * 
*/
public interface taskService {
    void updateTaskLife();
    
    Map<String,String> checkTaskStatus(String taskId);
}
