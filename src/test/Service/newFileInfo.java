package com.tastmanager.test.Service;

import java.io.File;

public class newFileInfo {
	private String userName;
       private String generalCategory;
       private File file;
       private String comment;
       
       public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGeneralCategory() {
		return generalCategory;
	}
	public void setGeneralCategory(String generalCategory) {
		this.generalCategory = generalCategory;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
       
}
