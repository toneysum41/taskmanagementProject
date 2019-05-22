package com.tastmanager.test.serviceInterface;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface dataFormatService {
	List<JSONObject> reformatJsonData(String[] jsondata);
}
