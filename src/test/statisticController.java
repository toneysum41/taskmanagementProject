package com.tastmanager.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tastmanager.test.serviceInterface.dataFormatService;

import DBconnection.TaskEntity;
import DBconnection.userEntity;

@Controller
@RequestMapping("/statisticTable")
public class statisticController {
	@Resource
	private MongoTemplate mongotemplate; 
	
	@Autowired
	private dataFormatService dataFormatbuilder;
	
	@RequestMapping(value = "userStatus")
	@ResponseBody
	public String getUserStatus(@RequestBody JSONObject jsonObject) {
		List<String> json = new ArrayList<String>();
		String username = jsonObject.get("name").toString();
		int weeks = Integer.parseInt(jsonObject.getString("weeks"));
		Query q = new Query(Criteria.where("userName").is(username));
		userEntity user = mongotemplate.findOne(q, userEntity.class);
		Query q1 = new Query(Criteria.where("userId").is(user.getId()));
		List<TaskEntity> usertask = mongotemplate.find(q1, TaskEntity.class);
		for(TaskEntity t : usertask) {
			if(t.getPeriod()>weeks*7 && t.getPeriod()<7+weeks*7) {
				String date = t.getDate();
				String a = "{\"taskDate\":\""+dateToweek(date)+"\",\"taskName\":\""+t.getTaskName()+"\",\"taskStatus\":\""+t.getTaskStatus()+"\",\"taskSpecSchedul\":\""+t.getTaskSpecSchedul()+"\",\"efficiency\":\""+"100%"+"\"}";
				json.add(a);
			}
		}
		
		return JSON.toJSONString(json);
	}
	
	@RequestMapping(value = "autotestResult")
	@ResponseBody
	public String getTestResult(@RequestBody JSONObject jsonObject) {
		String filename = jsonObject.get("filename").toString();
		List<JSONObject> reformJsonStr = dataFormatbuilder.reformatJsonData(new String[] {jsonObject.get("SemanticResult").toString(),
				jsonObject.get("JsonResult").toString(),jsonObject.get("GUIResult").toString()});	
		return "Success:" + reformJsonStr.get(1).get("_resultType").toString();
	}
	
	@RequestMapping(value = "getUserList")
	@ResponseBody
	public String getUserList() {
		List<String> username = new ArrayList<String>();
		List<userEntity> users=mongotemplate.findAll(userEntity.class);
		for(userEntity u : users) {
			username.add(u.getUserName());
		}
		return JSON.toJSONString(username);
	}


	public String dateToweek(String date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekdays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}; 
		Calendar cal = Calendar.getInstance();
		Date d = null;
		try {
			if(!date.equals("")) {
				d = f.parse(date);
				cal.setTime(d);
			}
		}catch(ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK)-1;
		if(w<0) {
			w=0;
		}
		return weekdays[w];
	}
		
}
