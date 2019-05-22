package com.tastmanager.test.locker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tastmanager.test.Dao.taskDao;
import com.tastmanager.test.Service.DataCache;
import com.tastmanager.test.Service.xmlFileFrame.imergeContent;
import com.tastmanager.test.Service.xmlFileFrame.xmlFrame;
import com.tastmanager.test.config.ExecutorConfig;
import com.tastmanager.test.serviceInterface.testcaseCondService;

import DBconnection.TaskEntity;
import DBconnection.TestcaseEntity;
import DBconnection.userEntity;

public class testcaseStatusLock implements Callable<String> {
	private static Logger logger = LogManager.getLogger(testcaseStatusLock.class);
	
	private MongoTemplate mongotemplate; 
	
	private testcaseCondService testcasecondService;

	private taskDao taskdao;
	
    private Lock lock = new ReentrantLock();
    
    final private String dir1 = "C:\\Taskfile";

    private DataCache d;
    
    private String getCase;
    private String date;
    private String taskid;
    private String category;
    private String name;
    private String language;
    private String publisher;
    
    private boolean isFirstTime;
    
    private HttpSession httpSession;
    
    private List<String> testerInfoList;
   
    public testcaseStatusLock(DataCache d, List<String> testerInfoList,String getCase,String date, String taskid, String category, String name, String language, String publisher,HttpSession httpSession,MongoTemplate mongotemplate, testcaseCondService testcasecondService,taskDao taskdao) {
    	this.d = d;
    	this.getCase = getCase;
    	this.date = date;
    	this.taskid = taskid;
    	this.category = category;
    	this.name = name;
    	this.language = language;
    	this.publisher = publisher;
    	this.isFirstTime = true;
    	this.testerInfoList = testerInfoList;
    	this.httpSession = httpSession;
    	this.mongotemplate = mongotemplate;
    	this.testcasecondService = testcasecondService;
    	this.taskdao = taskdao;
    }

	@Override
	public String call() throws Exception {
        lock.lock(); // 获取锁
        Document caseDom = DocumentHelper.createDocument();
        List<String> repetition = new ArrayList<String>();
        try {
    		xmlFrame xmlframe = new xmlFrame();
    		imergeContent imergecontent = new imergeContent();
    		String[] configData = new String[17];   	    		
    		for(int i = 0;i < configData.length; i++) {
    			configData[i] = "-";
    		}  
		    if(getCase.equals("1") && d.getSelectedCaseList().size()>0) {
		    	userEntity user = mongotemplate.findOne(new Query(Criteria.where("userName").is(name)), userEntity.class);
		    	if(user!=null){
			    	List<TestcaseEntity> testcaseEntity = testcasecondService.getPartailTestcaseList(d.getSelectedCaseList());
		        	String[] taskInfo = new String[] {date,taskid,category,user.getId(),name+"_"+taskid,language,publisher,"ALL"};
		        	List<TaskEntity> newtasklist = user.getUserInfo().getUserTask();
		        	newtasklist.add(taskdao.recordTask(taskInfo));
		        	user.getUserInfo().setUserTask(newtasklist);
			    	for(TestcaseEntity tcEntity : testcaseEntity) {
			    		String[] updateInfo = new String[] {tcEntity.getName(),date};
			    		String updateStatus = testcasecondService.updateTestcaseInfo(updateInfo);
			    		if(updateStatus.equals("Success")) {
				    		if(isFirstTime) {
				    			caseDom = xmlframe.createTescoForm(testerInfoList.get(0), testerInfoList.get(1), testerInfoList.get(2), testerInfoList.get(3), configData, tcEntity);
				    			isFirstTime = false;
				    		}else {
				    			caseDom = imergecontent.combineWithLoadFile(tcEntity.getLanguage(), tcEntity.getCategory(), xmlframe.generateSDSCaseForm(tcEntity), caseDom);
				    		}
			    		}else {
			    			repetition.add(updateStatus+"\\n");
			    		}
			    	}
			    	/*if(repetition.isEmpty()) {
				    	user.getUserInfo().setUserAssignmentQuantity(String.valueOf(d.getSelectedCaseList().size()));
			    	}else {
			    		return repetition.toString();
			    	}*/
		    	}else {
		    		return "1";
		    	}
		    	File tescoFile;
		    	if(taskid!=null && name!=null) {
		    		  tescoFile=new File(dir1,name+"_"+taskid+".xml");
		    	}else {
		    	      tescoFile=new File(dir1,"unknown.xml");
		    	}
		    	
		    	OutputFormat format = OutputFormat.createPrettyPrint();
		    	format.setNewLineAfterDeclaration(false);
		    	format.setEncoding("UTF-8");
		    	XMLWriter xmlFrameWriter;
		    	try {
		    		FileOutputStream foutput = new FileOutputStream(tescoFile);
		    		xmlFrameWriter = new XMLWriter(foutput, format);
		    		try {
		    				xmlFrameWriter.write(caseDom);				
		    		} catch (IOException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    	} catch (UnsupportedEncodingException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	} catch (FileNotFoundException e) {
		    		// TODO Auto-generated catch block
		    		e.printStackTrace();
		    	}
		    	d.getSelectedCaseList().clear();
		    	httpSession.setAttribute("datacache", d);    	
		    	return "3";
		    }else {
		    	return "0";
		    }
        }finally {
            lock.unlock(); // 释放所
        }
	}

}

