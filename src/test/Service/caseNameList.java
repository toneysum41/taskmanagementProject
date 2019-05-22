package com.tastmanager.test.Service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class caseNameList {
     private List<caseName> nameList;

	public List<caseName> getNameList() {
		return nameList;
	}

	public void setNameList(List<caseName> nameList) {
		this.nameList = nameList;
	}
}
