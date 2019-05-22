package com.tastmanager.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/24/2018
 * @Modify 
 * 
*/

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.tastmanager.test.Dao.GeneralDao;
import com.tastmanager.test.Dao.TestcaseDao;
import com.tastmanager.test.Dao.taskDao;
import com.tastmanager.test.Service.AsyncThreadServiceImpl;
import com.tastmanager.test.Service.DataCache;
import com.tastmanager.test.Service.caseName;
import com.tastmanager.test.Service.caseNameList;
import com.tastmanager.test.Service.caseUpdateInfo;
import com.tastmanager.test.Service.newFileInfo;
import com.tastmanager.test.Service.testcaseTableCond;
import com.tastmanager.test.Service.xmlFileFrame.imergeContent;
import com.tastmanager.test.Service.xmlFileFrame.xmlFrame;
import com.tastmanager.test.locker.testcaseStatusLock;
import com.tastmanager.test.serviceInterface.AsyncThreadService;
import com.tastmanager.test.serviceInterface.testcaseCondService;

import DBconnection.TaskEntity;
import DBconnection.TestcaseEntity;
import DBconnection.TestcaseInfoEntity;
import DBconnection.userEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author Terry
 * 
 */
@Controller
@RequestMapping("/testcaseTable")
public class testcaseController {
	private static final Logger logger = LoggerFactory.getLogger(testcaseController.class);
	@Resource
	private MongoTemplate mongotemplate; 
	
	private DataCache dataCache = new DataCache();
	private List<String> editlist = new ArrayList<String>();
	@Autowired
	private testcaseCondService testcasecondService;
	@Autowired
	private ThreadPoolTaskExecutor executorService;
	@Autowired
	private TestcaseDao testcasedao;
	@Autowired
	private AsyncThreadService AsyncthreadService;
	
	@Autowired
	private taskDao taskdao;
	private List<TestcaseInfoEntity> getTestcaseInfoList() {
		return testcasedao.findAllcaseInfoEntity("TestcaseInfo_collection");
	}
	
	private boolean newfile = false;
	

/**
 * 	
 * @param model
 * @return
 */
@GetMapping
public ModelAndView readTestcaseList(Model model) {
	model.addAttribute("testcaseList",getTestcaseInfoList());
	return new ModelAndView("testcaseTable", "testcaseinfo", model);
}


/**
 * Ajax json post
 * @param conditions
 * @return
 * content: update test case table depend on the request condition
 */
@RequestMapping(value = "getMoreCase", method = RequestMethod.POST)
@ResponseBody
public String getMoreCase(@RequestBody testcaseTableCond conditions) {
	List<String> testcaseInfo = new ArrayList<String>();
	List<TestcaseInfoEntity> temp = testcasecondService.getPartailTestcaseInfoList(conditions);
	for(TestcaseInfoEntity t : temp) {
		if(t.getTimeBeforeAvalible().equals("0") || conditions.getSearchmode().equals("0")) {
			testcaseInfo.add(t.toString());
		}	
	}
	return JSON.toJSONString(testcaseInfo);
}

/**
 * Ajax json post
 * @param conditions
 * @return
 * content: update test case table depend on the request condition
 */
@RequestMapping(value = "casedata", method = RequestMethod.POST)
@ResponseBody
public String caseConditionAdd(@RequestBody testcaseTableCond conditions,HttpSession httpSession) {
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	List<String> testcaseInfo = new ArrayList<String>();
	List<TestcaseInfoEntity> temp = testcasecondService.getPartailTestcaseInfoList(conditions);
	for(TestcaseInfoEntity t : temp) {
		boolean exist=false;
		if(t.getTimeBeforeAvalible().equals("0") || conditions.getSearchmode().equals("0")) {
			if(d.getSelectedCaseList().size()>0) {
				for(String name : d.getSelectedCaseList()) {
					if(name.equals(t.getCaseName())) {
						exist=true;
						break;
					}
				}
				if(!exist) {
					testcaseInfo.add(t.toString());
				}
			}else {
				testcaseInfo.add(t.toString());
			}
		}	
	}
	return JSON.toJSONString(testcaseInfo);
}
/**
 * 
 * @param requestname
 * @return
 * content: read name list from data base
 */
@RequestMapping(value = "usernamelist", method = RequestMethod.GET)
@ResponseBody
public String getAllUsername(@RequestParam("requestname")String requestname) {
	List<String> usernames = new ArrayList<String>();
	List<userEntity> users = mongotemplate.findAll(userEntity.class);
		for(userEntity u : users) {
			usernames.add(u.getUserName());
		}	
	return JSON.toJSONString(usernames);
}

/**
 * 
 * @return
 * 
 */
@RequestMapping(value = "getSelectednum", method = RequestMethod.GET)
@ResponseBody
public String getSelectedNum(HttpSession httpSession) {
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	List<String> info = new ArrayList<String>();	
	info.add(String.valueOf(d.getSelectedCaseList().size()));
	long totalnum = mongotemplate.count(new Query(Criteria.where("casePriority").in("1","2","3")), TestcaseInfoEntity.class);
	info.add(String.valueOf(totalnum));
	return JSON.toJSONString(info);
}

/**
 * Ajax json post
 * @param selectedCaseList
 * @return
 * content: count all user selected case
 */
@RequestMapping(value = "caseSelected", method = RequestMethod.POST)
@ResponseBody
public String addSelectedCase(@RequestBody List<caseName> nameList,HttpSession httpSession) {
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	if(d.getSelectedCaseList().size()<=0) {
		List<String> temp = new ArrayList<String>();
		for(caseName c1 : nameList) {
			temp.add(c1.getName());
		}
		d.setSelectedCaseList(temp);
		httpSession.setAttribute("datacache", d);
	}else {
		if(!nameList.isEmpty()) {
			t1:for(caseName c1 : nameList) {
				for(String s : d.getSelectedCaseList()) {
					if(c1.getName().equals(s)) {
						break t1;
					}
				}
				d.getSelectedCaseList().add(c1.getName());
			}
		    httpSession.setAttribute("datacache", d);
		}
	}	
	return String.valueOf(d.getSelectedCaseList().size());
}

/**
 * Ajax json post
 * @param selectedCaseList
 * @return
 */
@RequestMapping(value = "caseDelete", method = RequestMethod.POST)
@ResponseBody
public String removeSelectedcase(@RequestBody List<caseName> nameList,HttpSession httpSession) {
	List<String> tempName = new ArrayList<String>();
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	if(d.getSelectedCaseList().size()<=0 && d.getSelectedCaseList() == null) {
		return "Nothing_Selected_Yet!";
	}else {
		if(!nameList.isEmpty()) {
			for(caseName c1 : nameList) {
				for(String s : d.getSelectedCaseList()) {
					if(c1.getName().equals(s)) {
						tempName.add(c1.getName());
					}
				}
			}
			for(String s : tempName) {
				d.getSelectedCaseList().remove(s);
			}	
			httpSession.setAttribute("datacache", d);
		}
		return String.valueOf(d.getSelectedCaseList().size());
	}	
	
}

/**
 * Ajax json post
 * @param selectedCaseList
 * @return
 */
@RequestMapping("caseClear")
@ResponseBody
public String clearCaseList(@RequestParam("status")String clear,HttpSession httpSession) {
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	if(clear.equals("1")) {
		d.getSelectedCaseList().clear();
		httpSession.setAttribute("datacache", d);
	}
	return "Cleared";
}

/**
 * Ajax json post
 * @param selectedCaseList
 * @return
 * @throws ExecutionException 
 * @throws InterruptedException 
 */
@RequestMapping(value = "getCase")
@ResponseBody
public String generateTaskFile(@RequestParam("Status")String getCase,@RequestParam("Date")String date,@RequestParam("Name")String name,@RequestParam("TaskID")String taskid
		                      ,@RequestParam("Language")String language,@RequestParam("Equipement")String category,@RequestParam("Publisher")String publisher,HttpSession httpSession) throws InterruptedException, ExecutionException {
	DataCache d = (DataCache)httpSession.getAttribute("datacache");
	if(mongotemplate.find(new Query(Criteria.where("_id").is(taskid)), TaskEntity.class).isEmpty()) {
		List<String> testerInfoList = new ArrayList<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				add("tester");
				add("-");
				add("-");
				add("-");
			}
		};
		testcaseStatusLock newtask = new testcaseStatusLock(d, testerInfoList, getCase, date, taskid, category, name, language, publisher, httpSession, mongotemplate,testcasecondService,taskdao);
		Future<String> future = executorService.submit(newtask);
		return future.get();
	}else {
		return "2";
	}


}


@RequestMapping(value = "editcase", method = RequestMethod.POST)
@ResponseBody
public String getEditCase(@RequestBody List<caseName> nameList) {
		List<String> temp = new ArrayList<String>();
		editlist.clear();
		for(caseName c1 : nameList) {
			temp.add(c1.getName());
		}
		editlist = temp;	
	return JSON.toJSONString(editlist);
}

/**
 * 
 * @param updateinfo
 * @return
 */
@RequestMapping(value = "updatetestcase", method = RequestMethod.POST)
@ResponseBody
public synchronized  String updateTestcase(@RequestBody caseUpdateInfo updateinfo) {
    Update infoupdate = new Update();
    Update caseupdate = new Update();
    if(!updateinfo.getGeneralCategory().isEmpty() && !updateinfo.getGeneralCategory().equals("NONE")) {
    	infoupdate.set("generalCategory", updateinfo.getGeneralCategory());
    	caseupdate.set("generalCategory", updateinfo.getGeneralCategory());
    }
    if(updateinfo.getTestCategory().length>0) {
    	if(updateinfo.getCaseupdatemode().equals("replace")) {
        	infoupdate.set("testcategory", updateinfo.getTestCategory());
    	}else {
    		for(String s : updateinfo.getTestCategory()) {
    			infoupdate.addToSet("testcategory", s);
    		}   	
    	}
    }
    if(!updateinfo.getCategory().isEmpty() && !updateinfo.getCategory().equals("NONE")) {
    	infoupdate.set("category", updateinfo.getCategory());
    	caseupdate.set("category", updateinfo.getCategory());
    }
    if(!updateinfo.getRefreshCaseStatus().isEmpty()) {
    	if(updateinfo.getRefreshCaseStatus().equals("YES")) {
    		infoupdate.set("timeBeforeAvalible", "0");
    	}	
    }
    if(updateinfo.getSystemBuild().length>0) {
    	infoupdate.set("systemBuild", updateinfo.getSystemBuild());
    }
    testcasecondService.updateTestcaseList(editlist, infoupdate, caseupdate);
	return "{\"success\":\"1\"}";
}


/**
 * single file
 *
 * @param file
 * @param request
 * @return
 * @throws ExecutionException 
 * @throws InterruptedException 
 */
@RequestMapping(value= "/upload", method = RequestMethod.POST)
@ResponseBody
public String casesUpload(MultipartFile testcaseFile,String username,String comment, String categoryselect) throws InterruptedException, ExecutionException {
	   if (!testcaseFile.isEmpty()) {
		   String saveFileName = testcaseFile.getOriginalFilename();
           List<Object> list = new ArrayList<>();
		   try {
			   byte[] bytes = IOUtils.toByteArray(testcaseFile.getInputStream());
			   list.add(bytes);
           } catch (Exception e) {
               logger.error("Failed to convert file stream to byte array:{}", e);
           }
		   Future<String> result = AsyncthreadService.executeUpload(list,saveFileName);
		   while(true) {
			   if(result.isDone()) {
				   break;
				   }
				   Thread.sleep(100);
		   }
           if(result.get().equals("1")) {
        	   executorService.execute(new Runnable() {
        		   public void run() {
        			   synchronized(this) {
                           testcasedao.createTestcases(saveFileName,categoryselect,username);
                           testcasedao.createTestcaseInfo();
        			   }
        		   }
        	   });
               newfile = true;
               return "1";
           }else {
        	   return result.get();
           }
       } else {
           return "0";
       }
}
    
}