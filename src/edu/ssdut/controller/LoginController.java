package edu.ssdut.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.ssdut.bean.ResultBean;
import edu.ssdut.model.Student;
import edu.ssdut.model.Teacher;
import edu.ssdut.model.User;
import edu.ssdut.service.StudentService;
import edu.ssdut.service.TeacherService;
import edu.ssdut.util.MD5;
import edu.ssdut.util.Tokens;

@Controller
public class LoginController {
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService; 

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public ResultBean<String> login(String username, String password, String userType) {
		System.out.println(username + password + userType);
		
		//判断是否为空		
		if (username == null || password == null || userType == null)
			return ResultBean.failure("2003");
		
		//去掉前后的空格
		username = username.trim();
		password = password.trim();
		userType = userType.trim();
		if (username.length() == 0 || password.length() == 0)
			return ResultBean.failure("2003");
		
		//计算password的MD5值
		String pwd = MD5.createMD5(password);
		
		if ("student".equals(userType)) {
			Student s = studentService.login(username, pwd);
			if (s == null) 
				return  ResultBean.failure("1004");
			s = studentService.find(username);
			String token = Tokens.createJWT("student", username);
			return ResultBean.success(token);
		}
		
		if ("teacher".equals(userType)) {
			Teacher t = teacherService.login(username, pwd);
			if (t == null)
				return ResultBean.failure("1004");
			t = teacherService.find(username);
			String token = Tokens.createJWT("teacher", username);
			return ResultBean.success(token);
		}
		
		if ("admin".equals(userType)) {
			Properties pps = new Properties();
	        InputStream stream=Thread.currentThread().getContextClassLoader().getResourceAsStream("sxjd.properties");
	        try {
				pps.load(stream);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	        String admin1 = pps.getProperty("admin");
	        String password1 = pps.getProperty("password");
	        if (username.equals(admin1) && password.equals(password1)) {
	        	User u = new User();
	        	u.setUsername(username);
	        	String token = Tokens.createJWT("admin", username);
	        	return ResultBean.success(token);
	        }
	        return ResultBean.failure("1004");
		}
		return ResultBean.failure("2002");
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean logout() {
		
		return ResultBean.success();
	}
}
