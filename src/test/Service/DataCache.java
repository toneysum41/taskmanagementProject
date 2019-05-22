package com.tastmanager.test.Service;

import java.util.ArrayList;
import java.util.List;

public class DataCache {
    private List<String> selectedCaseList;
    public DataCache() {
    	this.selectedCaseList = new ArrayList<String>();
    }

	public List<String> getSelectedCaseList() {
		return selectedCaseList;
	}

	public void setSelectedCaseList(List<String> selectedCaseList) {
		this.selectedCaseList = selectedCaseList;
	}
}
