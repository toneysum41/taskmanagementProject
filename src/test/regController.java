package com.tastmanager.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.Criteria;
import com.tastmanager.test.Dao.UserDao;
import com.tastmanager.test.Dao.UserInfoDao;
import com.tastmanager.test.Service.regData;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 2/25/2019
 * @Modify 
 * 
*/
@RestController
@EnableAutoConfiguration
@RequestMapping("/register")
public class regController {
	@Resource
	private MongoTemplate mongotemplate; 
	@Autowired
	private UserDao userdao;
    
    @RequestMapping("userdata")
    @ResponseBody
    public String userRegister(@RequestBody regData regdata) {
        List<String> userdata = new ArrayList<String>();
    	byte[] bpw = java.util.Base64.getDecoder().decode(regdata.getUserPassword());
    	byte[] bId = java.util.Base64.getDecoder().decode(regdata.getId());
    	String password = new String (bpw);
    	String userId = new String (bId);
        userdata.add(userId);
        userdata.add(regdata.getUserName());
        userdata.add(password);
        if(userId.equals("C17345")) {
        	userdata.add("Developer");
        	userdata.add(regdata.getTeamTitle());
        	userdata.add("1");
        }
        else if(userId.equals("C17346")){
        	userdata.add("manager");
        	userdata.add(regdata.getTeamTitle());
        	userdata.add("2");
        }else {
        	userdata.add("Tester");
        	userdata.add(regdata.getTeamTitle());
        	userdata.add("3");
        }
        
         if(userdao.createUser(userdata)) {

        	 return JSON.toJSONString("{\"success\":\"1\"}");
         }
         else {
        	 return JSON.toJSONString("{\"success\":\"0\"}");
         }
    	
    }
}
