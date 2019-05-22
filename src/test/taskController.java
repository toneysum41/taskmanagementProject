package com.tastmanager.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.tastmanager.test.Dao.resultDao;
import com.tastmanager.test.Service.newFileInfo;
import com.tastmanager.test.requestBody.issueListCondition;
import com.tastmanager.test.requestBody.resultFileInfo;
import com.tastmanager.test.serviceInterface.issueCondService;
import com.tastmanager.test.serviceInterface.resultService;
import com.tastmanager.test.serviceInterface.taskService;

import DBconnection.IssueEntity;
import DBconnection.ResultEntity;
import DBconnection.TaskEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.userEntity;
import contentRead.readResultFile;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 2/9/2019
 * @Modify 
 * 
*/
@Controller
@RequestMapping("/taskassignTable")
public class taskController {
	final public String ONGOING = "Ongoing";
	final public String DONE = "Done";
	@Resource
	private MongoTemplate mongotemplate; 
	@Autowired
	private resultService resultservice;
	@Autowired
	private taskService taskservice;
	@Autowired
	private resultDao resultdao;
	@Autowired
	private issueCondService issuecondservice;
	@Autowired
	private ThreadPoolTaskExecutor executorService;
	
	private boolean newfile = false;
	/**
	 * single file
	 *
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	@ResponseBody
	public String resultUpload(String taskid, String username, String softwareversion, String systemversion, MultipartFile testcaseFile) {
		newfile=false;
		String pathDir = "C:\\Taskresultfile\\"; 
        Update update = new Update();
        update.set("TaskStatus", DONE);
		TaskEntity t = mongotemplate.findOne(new Query(Criteria.where("_id").is(taskid)), TaskEntity.class);
		if (!testcaseFile.isEmpty()) {
			   readResultFile readresultfile = new readResultFile();
	           String saveFileName = testcaseFile.getOriginalFilename();
	           File saveFile = new File(pathDir + saveFileName);
	           if (!saveFile.getParentFile().exists()) {
	               saveFile.getParentFile().mkdirs();
	           }
	           try {
	               BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
	               out.write(testcaseFile.getBytes());
	               out.flush();
	               out.close();
	               if(t!=null) {
	            	   if(t.getTaskStatus().equals("Ongoing")) {
		                   mongotemplate.updateFirst(new Query(Criteria.where("_id").is(taskid)), update, TaskEntity.class);
	            	   }
	            	   if(t.getUserId().equals(username)) {
			               resultdao.recordResult(readresultfile.readResult(pathDir + saveFileName, t.getTaskLanguage(), username,softwareversion,taskid,systemversion));
			               resultservice.analysisResult(username, taskid);
			               newfile=true;
			               t.setTaskStatus(DONE);
			               return "1";
	            	   }else {
	            		   return "This task is not your duty!";
	            	   }		              
	               }else {
	            	   return "Unknown TaskId!";
	               }

	           } catch (FileNotFoundException e) {
	               e.printStackTrace();
	               return "Upload Fail," + e.getMessage();
	           } catch (IOException e) {
	               e.printStackTrace();
	               return "Upload Fail," + e.getMessage();
	           }
	       } else {
	           return "Upload Fail, Cause empty file.";
	       }
	}
	

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping("/download")
    @ResponseBody
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String filename = request.getParameter("filename")+".xml";
        String name = request.getParameter("filename");
        String[] keyword =  name.split("_");
        TaskEntity t = mongotemplate.findOne(new Query(Criteria.where("_id").is(keyword[1])), TaskEntity.class);
    	if (filename != null) {
            //Set file path
            String realPath = "C:\\Taskfile\\";
            File file = new File(realPath , filename);
            if (file.exists()) {
                response.setContentType("application/force-download");
                response.setHeader("content-type", "application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    if(t.getTaskStatus().equals("waiting")) {
                        Update update = new Update();
                        update.set("TaskStatus", ONGOING);
                        mongotemplate.updateFirst(new Query(Criteria.where("_id").is(keyword[1])), update, TaskEntity.class);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @RequestMapping(value = "/getTask", method = RequestMethod.GET)
    @ResponseBody
    public String getTaskFileName(@RequestParam("Username")String username) {
    	List<String> taskInfo = new ArrayList<String>();
    	Query q = new Query(Criteria.where("userName").is(username));
    	Query q1 = new Query(Criteria.where("userId").is(mongotemplate.findOne(q, userEntity.class).getId()));
    	List<TaskEntity> tasks = mongotemplate.find(q1, TaskEntity.class);
    	for(TaskEntity t : tasks) {
    		taskInfo.add(t.toString());
    	}
         return JSON.toJSONString(taskInfo);
    }
    
    @RequestMapping(value = "/getUserStatus", method = RequestMethod.GET)
    @ResponseBody
    public String getUserStatus() {
    	List<Map<String,String>> resultInfo = new ArrayList<Map<String,String>>();
    	List<ResultEntity> result = mongotemplate.findAll(ResultEntity.class);
    	for(ResultEntity r : result) {
           if(Integer.parseInt(r.getLife())<5 && !r.getResult().equals("p") && !r.getResult().equals("")) { 
        	   if(r.getUserId()!=null) {
             		Map<String,String> userinfo = new HashMap<String,String>();
               		userinfo.put("caseName", r.getCaseName());
               		userinfo.put("userName", mongotemplate.findOne(new Query(Criteria.where("_id").is(r.getUserId())), userEntity.class).getUserName());
               		userinfo.put("softwareVersion", r.getSoftwareVersion());
               		userinfo.put("language", r.getLanguage());
               		userinfo.put("issue", r.getResult()+","+r.getOccurence()+", "+r.getDescription());
                    if(r.getIssueStatus().isEmpty()) {		
                  	   userinfo.put("Status", "0");  		
           	   }else if(r.getIssueStatus().equals("On hold")) {
           		   userinfo.put("Status", "1");
           	   }else if(r.getIssueStatus().equals("Fixed")) {
           		   userinfo.put("Status", "2");
           	   }else if(r.getIssueStatus().equals("NoTest")){
           		   userinfo.put("Status", "3");
           	   }else if(r.getIssueStatus().equals("Pending")){
           		   userinfo.put("Status", "4");
           	   } else {
           		 userinfo.put("Status", "5");
           	   }
                    resultInfo.add(userinfo);
        	   }
           }
    	}
    	return JSON.toJSONString(resultInfo);        
    }
    
    @RequestMapping("modifystatus")
    @ResponseBody
    public String modifyIssueStatus(@RequestParam("issueName")String issuenames,@RequestParam("issueStatus")String issuestatus) throws InterruptedException, ExecutionException {
    	Future<String> future = executorService.submit(new Callable<String>(){
            public synchronized String call() throws Exception {
            	String[] issuename = issuenames.split(",");
            	if(issuename.length>0) {
                	for(String s : issuename) {
                		Update update = new Update();
                		Query q = new Query(Criteria.where("caseName").is(s));
                		update.set("IssueStatus", issuestatus);
                    	mongotemplate.updateFirst(q, update, ResultEntity.class);
                	}
                	return "{\"success\":\"1\"}";
            	}else {
            		return "{\"success\":\"0\"}";
            	}        	
            }	
    	});  
   
    	return future.get();
    }
    
    @RequestMapping("gettaskstatus")
    @ResponseBody
    public String getTaskStatus(@RequestParam("username")String username,@RequestParam("taskIds")String taskids) {
    	Map<String,String> taskStatus = new LinkedHashMap<String,String>();
    	List<String> status = new ArrayList<String>();
    	taskStatus = taskservice.checkTaskStatus(taskids);
    	if(taskStatus!=null && taskStatus.size()>0) {
        	for(String key : taskStatus.keySet()) {
        		if(taskStatus.get(key).equals("Ongoing")) {
        			status.add("2");
        		}else if(taskStatus.get(key).equals("Done")) {
        			status.add("1");
        		}else {
        			status.add("0");
        		}		
        	}
    	}
    	
         return JSON.toJSONString(status);
    }
    
    @RequestMapping(value = "/issueList", method = RequestMethod.POST)
    @ResponseBody
    public String getIssueList(@RequestBody issueListCondition issueconditions ) {
    	List<Map<String,String>> issueInfo = new ArrayList<Map<String,String>>();
    	List<ResultEntity> issuelist = resultservice.getPartialResultList(issueconditions);
    	for(ResultEntity r : issuelist) {
            if(Integer.parseInt(r.getLife())<5 && !r.getResult().equals("p") && !r.getResult().equals("")) { 
         	   if(r.getUserId()!=null) {
              		Map<String,String> userinfo = new HashMap<String,String>();
                		userinfo.put("caseName", r.getCaseName());
                		userinfo.put("userName", mongotemplate.findOne(new Query(Criteria.where("_id").is(r.getUserId())), userEntity.class).getUserName());
                		userinfo.put("softwareVersion", r.getSoftwareVersion());
                		userinfo.put("language", r.getLanguage());
                		userinfo.put("date", r.getDate().split(" ")[0]);
                		userinfo.put("issue", r.getResult()+","+r.getOccurence()+", "+r.getDescription());
                        if(r.getIssueStatus().isEmpty()) {		
                       	   userinfo.put("Status", "0");  		
                	   }else if(r.getIssueStatus().equals("On hold")) {
                		   userinfo.put("Status", "1");
                	   }else if(r.getIssueStatus().equals("Fixed")) {
                		   userinfo.put("Status", "2");
                	   }else if(r.getIssueStatus().equals("NoTest")){
                		   userinfo.put("Status", "3");
                	   }else if(r.getIssueStatus().equals("Pending")){
                		   userinfo.put("Status", "4");
                	   } else {
                		 userinfo.put("Status", "5");
                	   }
                     issueInfo.add(userinfo);
         	   }
            }
     	}  
         return JSON.toJSONString(issueInfo);
    }
    
    @RequestMapping(value = "softwareVersionList", method = RequestMethod.GET)
    @ResponseBody
    public String getAllsoftwareversion() {
    	List<String> softwareversion = new ArrayList<String>();
    	List<ResultEntity> results = mongotemplate.findAll(ResultEntity.class);
        for(ResultEntity r : results) {
        	if(!softwareversion.contains(r.getSoftwareVersion())) {
        		softwareversion.add(r.getSoftwareVersion());
        	}
        }
    	return JSON.toJSONString(softwareversion);
    }
    
}
