package com.tastmanager.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/24/2018
 * @Modify 
 * 
*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.tastmanager.test.serviceInterface.dashboardInfo;
import com.tastmanager.test.serviceInterface.taskService;
import com.tastmanager.test.serviceInterface.testcaseService;

@Controller
public class HomeController {
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	private String dir = "F:\\Users\\toney\\eclipse-workspaceEE\\TaskManager\\src\\main\\resources\\static\\images\\faces\\";
	@Resource
	private MongoTemplate mongotemplate; 
	@Autowired
	private testcaseService testcaseservice;
	@Autowired
	private dashboardInfo dashboardinfo;
	
	public void testcaseAutoUpdate() {
		testcaseservice.updateTestcaseInfo();
	}
	@Autowired
	private taskService taskservice;
	public void taskAutoUpdate() {
		taskservice.updateTaskLife();
	}
	/*@Autowired
	private resultService resultservice;
	public void resultAutoUpdate() {
		resultservice.updateResultLife();
	}
	*/
    @RequestMapping(value="/index")
    public ModelAndView index() {
    	ModelAndView mv = new ModelAndView("index");
    	return mv; 
    }
    
    @RequestMapping(value = "/testcaseTable")
    public String testcaseTable(HttpServletRequest Request, Model model) {
    	return "testcaseTable"; 
    }
    
    @RequestMapping(value="/statisticTable")
    public ModelAndView statisticTable() {
    	ModelAndView mv = new ModelAndView("statisticTable");
    	return mv; 
    }
    
    @RequestMapping(value="/taskassignTable")
    public ModelAndView taskassignTable() {
    	ModelAndView mv = new ModelAndView("taskassignTable");
    	return mv; 
    }
    
    @ResponseBody
	@RequestMapping(value = "/index/images")
	public String getUserImage(@RequestParam("imagefile") MultipartFile file,@RequestParam("username") String username) {
		   if (!file.isEmpty()) {
			   String name = username+"_face"+".jpg";
	           File imagefile = new File(dir+name);
	           if (!imagefile.getParentFile().exists()) {
	        	   imagefile.getParentFile().mkdirs();
	           }
	           try {
	               BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imagefile));
	               out.write(file.getBytes());
	               out.flush();
	               out.close();
	               return imagefile.getName()+"upload success!";
	           } catch (FileNotFoundException e) {
	               e.printStackTrace();
	               return "Fail Upload," + e.getMessage();
	           } catch (IOException e) {
	               e.printStackTrace();
	               return "Fail Upload," + e.getMessage();
	           }
	       } else {
	           return "Fail upload, empty file.";
	       }
	}
    
    @ResponseBody
	@RequestMapping(value = "/index/getInfo")
	public String getInfo() {
    	Map<String,String> backendInfo = dashboardinfo.getInfoForCaseStatistic(); 	
    	return JSON.toJSONString(backendInfo);
	}
    
    @ResponseBody
	@RequestMapping(value = "/signout")
	public String UserSignout(@RequestParam("clear") String clear,HttpServletRequest request) {
    	HttpSession   session   =   request.getSession();
    	if(clear.equals("1")) {
        	session.invalidate();
        	return "1";
    	}else {
    		return "0";
    	}

    }
  
    
}
