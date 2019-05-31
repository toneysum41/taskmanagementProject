package com.tastmanager.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class loginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		HttpSession session = request.getSession();
		Object loginUser = session.getAttribute("username");
		Object UserAuthority = session.getAttribute("userauthority");
		if(loginUser == null) {
			String a = request.getParameter("username");
			String b = request.getRequestURI();
			if(a==null && b.equals("/index")) {
				//response.sendRedirect("https://www.baidu.com");
				//request.getRequestDispatcher("/index").forward(request, response);
				return true;
			}else {
			    response.sendRedirect("http://192.168.1.88:8088/login");
				return false;
			}
		}else {
			String temp1 = request.getParameter("a");
			String temp2 = request.getParameter("username");
			if(UserAuthority.toString().equals(temp1) && loginUser.toString().equals(temp2) ) {
				return true;
			}else {
				response.sendRedirect("http://192.168.1.88:8088/error404");
				return false;
			}
			
		}
	}
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
    	
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{

    }
}


