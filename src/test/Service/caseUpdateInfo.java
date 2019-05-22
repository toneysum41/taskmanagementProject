package com.tastmanager.test.Service;

public class caseUpdateInfo {
	 private String GeneralCategory;
      private String[] TestCategory;
      private String Category;
      private String RefreshCaseStatus;
      private String[] SystemBuild;
      private String caseupdatemode;
      
    public String getCaseupdatemode() {
		return caseupdatemode;
	}
	public void setCaseupdatemode(String caseupdatemode) {
		this.caseupdatemode = caseupdatemode;
	}
	public String getGeneralCategory() {
		return GeneralCategory;
	}
	public void setGeneralCategory(String generalCategory) {
		GeneralCategory = generalCategory;
	}
	public String[] getTestCategory() {
		return TestCategory;
	}
	public void setTestCategory(String[] testCategory) {
		TestCategory = testCategory;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	public String getRefreshCaseStatus() {
		return RefreshCaseStatus;
	}
	public void setRefreshCaseStatus(String refreshCaseStatus) {
		RefreshCaseStatus = refreshCaseStatus;
	}
	public String[] getSystemBuild() {
		return SystemBuild;
	}
	public void setSystemBuild(String[] systemBuild) {
		SystemBuild = systemBuild;
	}
}
