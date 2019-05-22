package com.tastmanager.test;

/***
 * coding = "utf-8"
 * @author Xiaoang_Tong
 * @Data 12/24/2018
 * @Modify 
 * 
*/

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@EnableAutoConfiguration
public class entryPageController {
    @RequestMapping(value="/login")
    public ModelAndView registration() {
    	ModelAndView mv = new ModelAndView("login");
    	return mv; 
    }
    
    @RequestMapping(value="/register")
    public ModelAndView register() {
    	ModelAndView mv = new ModelAndView("register");
    	return mv; 
    }
    
   @RequestMapping(value="/error404")
    public ModelAndView error() {
    	ModelAndView mv = new ModelAndView("error404");
    	return mv; 
    }
}