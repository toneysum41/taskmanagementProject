package com.tastmanager.test.Service;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tastmanager.test.serviceInterface.dataFormatService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

@Service
public class DataFormatBuilder implements dataFormatService{
	
	   @Override
       public List<JSONObject> reformatJsonData(String[] jsondata) {
		List<JSONObject> jsonstr= new ArrayList<JSONObject>();
		jsonstr.add(JSON.parseObject(JSON.toJSONString(StringUtils.substringAfter(jsondata[0], "json="))));
		jsonstr.add(JSON.parseObject(StringUtils.substringAfter(jsondata[1], "json=")));
		jsonstr.add(JSON.parseObject(StringUtils.substringAfter(jsondata[2], "jsonData=")));
		
		return jsonstr;  	   
       }
}
