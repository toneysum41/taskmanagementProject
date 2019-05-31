package com.tastmanager.test.Service;

import org.springframework.stereotype.Service;

@Service
public class testcaseTableCond {

	   private String[] category;
       private String generalcategory;
	   private String testcategory;
       private String period;
       private String[] systembuild;
       private String passRate;
       private String language;
       private String searchmode;
       private String regexcontent;

	private String startpoint;
      

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}
	
	   public String getGeneralcategory() {
		return generalcategory;
	}

	public void setGeneralcategory(String generalcategory) {
		this.generalcategory = generalcategory;
	}

	public String getTestcategory() {
		return testcategory;
	}

	public void setTestcategory(String testcategory) {
		this.testcategory = testcategory;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String[] getSystembuild() {
		return systembuild;
	}

	public void setSystembuild(String[] systembuild) {
		this.systembuild = systembuild;
	}

	public String getPassRate() {
		return passRate;
	}

	public void setPassRate(String passRate) {
		this.passRate = passRate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
    public String getSearchmode() {
		return searchmode;
	}

	public void setSearchmode(String searchmode) {
		this.searchmode = searchmode;
	}
	
	public String getRegexcontent() {
		return regexcontent;
	}

	public void setRegexcontent(String regexcontent) {
		this.regexcontent = regexcontent;
	}
    public String getStartpoint() {
		return startpoint;
	}

	public void setStartpoint(String startpoint) {
		this.startpoint = startpoint;
	}
}
