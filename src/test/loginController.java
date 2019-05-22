package com.tastmanager.test;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/24/2018
 * @Modify 
 * 
*/

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tastmanager.test.Dao.UserDao;
import com.tastmanager.test.Service.DataCache;
import com.tastmanager.test.Service.UserLoginInfo;
import com.tastmanager.test.Service.regData;

import DBconnection.userEntity;
import DBconnection.userInfoEntity;

@Controller
@RequestMapping("/login")
public class loginController {
	@Resource
	private MongoTemplate mongotemplate; 
    
    @RequestMapping("userVerify")
    @ResponseBody
    public String userVerification(@RequestBody UserLoginInfo logininfo, HttpServletRequest request, Model model) {
    	byte[] b = java.util.Base64.getDecoder().decode(logininfo.getPassword());
    	String password = new String (b);
    	Query q = new Query(Criteria.where("userName").is(logininfo.getName()).andOperator(Criteria.where("userPassword").is(password)));
    	List<userEntity> user = mongotemplate.find(q, userEntity.class);
    	if(!user.isEmpty()) {
    		//Query q1 = new Query(Criteria.where("_id").is(user.get(0).getId()));
    		//List<userInfoEntity> userInfo = mongotemplate.find(q1, userInfoEntity.class);
    		DataCache dataCache = new DataCache();
    	    HttpSession usersession = request.getSession();
        	usersession.setAttribute("username", logininfo.getName());
        	usersession.setAttribute("userauthority", user.get(0).getAuthorityLevel());
        	usersession.setAttribute("status", user.get(0).getStatus());
        	usersession.setAttribute("datacache", dataCache);
    		model.addAttribute("username", logininfo.getName());
        	String feedback = "{"+"\"loginStatus\":\"1\",\"authority\":"+"\""+user.get(0).getAuthorityLevel()+"\","+"\"username\":\""+user.get(0).getUserName()+"\",\"status\":\""+user.get(0).getStatus()+"\"}";
        	return feedback;
    	}else {
    		return "{"+"\"loginStatus\":\"0\"}";
    	}
    }
    
    @ResponseBody
	@RequestMapping(value = "/getUserSession")
	public String getUserSession(HttpServletRequest request, HttpSession httpSession) {
		JSONObject jsonObject = new JSONObject();
		if(httpSession!=null){
			jsonObject.put("username", httpSession.getAttribute("username"));
			jsonObject.put("password", httpSession.getAttribute("password"));
			jsonObject.put("status", httpSession.getAttribute("status"));
		}
		return JSON.toJSONString(jsonObject);
	}

}
