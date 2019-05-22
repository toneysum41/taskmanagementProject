package com.tastmanager.test.Service;

import java.util.ArrayList;
import java.util.List;

public class testcaseTableGet {
       private List<String> casesName;
	   public testcaseTableGet() {
    	   this.casesName = new ArrayList<String>();
       }
       
       public void run(String[] casesname) {
    	   for(String s : casesname) {
    		   casesName.add(s);
    	   }
    	   setCasesName(casesName);
       }
       
       public List<String> getCasesName() {
		return casesName;
	   }

	    public void setCasesName(List<String> casesName) {
		this.casesName = casesName;
	   }
}
