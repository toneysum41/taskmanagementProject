package com.tastmanager.test.Service;

import java.util.List;

public class pageInfo {
	   private int pageNum;
       private int pageSize;
       private int startDoc; //The serial number of the document for the first element in current page
       private int endDoc;//The serial number of the document for the last element in current page
       
       private int totalCaseNum;
       private int totalPages;
       private List<Object> caseInfo;
       
       private int firstPage;
       private int prePage;
       
       private boolean isFirstPage = false;
       private boolean isLastPage = false;
       private boolean hasPrePage = false;
       private boolean hasNextPage = false;
       
    public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStartDoc() {
		return startDoc;
	}
	public void setStartDoc(int startDoc) {
		this.startDoc = startDoc;
	}
	public int getEndDoc() {
		return endDoc;
	}
	public void setEndDoc(int endDoc) {
		this.endDoc = endDoc;
	}
	public int getTotalCaseNum() {
		return totalCaseNum;
	}
	public void setTotalCaseNum(int totalCaseNum) {
		this.totalCaseNum = totalCaseNum;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<Object> getCaseInfo() {
		return caseInfo;
	}
	public void setCaseInfo(List<Object> caseInfo) {
		this.caseInfo = caseInfo;
	}
	public int getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public boolean isFirstPage() {
		return isFirstPage;
	}
	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}
	public boolean isLastPage() {
		return isLastPage;
	}
	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	public boolean HasPrePage() {
		return hasPrePage;
	}
	public void setHasPrePage(boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}
	public boolean HasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
       
       
       
}
