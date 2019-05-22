package com.tastmanager.test.requestBody;


import org.springframework.web.multipart.MultipartFile;

public class resultFileInfo {

	  private String username;
      
      private String taskid;
      
      private String softwareversion;
      
      private MultipartFile testcaseFile;
      
      private String comment;
      
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getSoftwareversion() {
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion) {
		this.softwareversion = softwareversion;
	}

	public MultipartFile getTestcaseFile() {
		return testcaseFile;
	}

	public void setTestcaseFile(MultipartFile testcaseFile) {
		this.testcaseFile = testcaseFile;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
      
}
