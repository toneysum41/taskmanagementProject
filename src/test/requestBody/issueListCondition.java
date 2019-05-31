package com.tastmanager.test.requestBody;

public class issueListCondition {
    private String[] status;
	private String[] testername;
    private String[] language;
	private String[] systemversion;
	private String[] softwareversion;

	private String startpoint;

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}
	
    public String[] getTestername() {
		return testername;
	}

	public void setTestername(String[] testername) {
		this.testername = testername;
	}

	public String[] getLanguage() {
		return language;
	}

	public void setLanguage(String[] language) {
		this.language = language;
	}
	
    public String getStartpoint() {
		return startpoint;
	}

	public void setStartpoint(String startpoint) {
		this.startpoint = startpoint;
	}
	
    public String[] getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String[] softwareversion) {
		this.softwareversion = softwareversion;
	}
	
    public String[] getSystemversion() {
		return systemversion;
	}

	public void setSystemversion(String[] systemversion) {
		this.systemversion = systemversion;
	}


}
