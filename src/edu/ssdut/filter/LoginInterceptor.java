package edu.ssdut.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.ssdut.model.Student;
import edu.ssdut.model.Teacher;
import edu.ssdut.model.User;

public class LoginInterceptor  extends HandlerInterceptorAdapter{
	private static final String[] IGNORE_URI = {"/login","/index"};

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        System.out.println("login interceptor >>>: " + url);
        HttpSession session = request.getSession();
        
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
        	Student s = (Student) session.getAttribute("student");
        	Teacher t = (Teacher) session.getAttribute("teacher");
        	User u = (User) session.getAttribute("admin");
            if (s != null || t != null || u != null) 
            	flag = true;
            else
            	flag = false;
        }
        return flag;
	}
}
