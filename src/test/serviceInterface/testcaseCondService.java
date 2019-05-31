package com.tastmanager.test.serviceInterface;

import java.util.List;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.tastmanager.test.Service.testcaseTableCond;

import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;


public interface testcaseCondService {
	
	List<TestcaseInfoEntity> getPartailTestcaseInfoList(testcaseTableCond testcaseCond);
	
	List<TestcaseEntity> getPartailTestcaseList(List<String> testcaseName);
	
	void updateTestcaseList(List<String> nameList, Update update1, Update update2);
	
	String updateTestcaseInfo(String[] updateinfo);
}
