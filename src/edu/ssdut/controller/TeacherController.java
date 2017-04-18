package edu.ssdut.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.ssdut.bean.ResultBean;
import edu.ssdut.model.Teacher;
import edu.ssdut.service.TeacherService;


@Controller
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	//GET请求用于获取资源
	@RequestMapping(value="/teacher", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean<Teacher> getTeacher(HttpServletRequest request) {
		System.out.println("GET");

		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		
		String username = (String) o;
		Teacher t = teacherService.find(username);
		return ResultBean.success(t);
	}
	
	@RequestMapping(value="/t", method = RequestMethod.PUT)
	@ResponseBody
	public ResultBean<Teacher> updateTeacher(Teacher t, HttpServletRequest request) {
		System.out.println("PUT " + t);
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		String username = (String) o;
		
		//验证修改的对象是否是登录的对象
		if (!(username.equals(t.getUsername())))
			return ResultBean.failure("1004");
		
		teacherService.update(t);
		Teacher teacher = teacherService.find(username);
		return ResultBean.success(teacher);
	}
	
	//PATCH 更新资源，此处用于修改密码
	@RequestMapping(value="/teacher", method=RequestMethod.PATCH)
	@ResponseBody
	public ResultBean<Teacher> changePwd(String oriPwd, String newPwd, HttpServletRequest request) {
		System.out.println("PATCH\n" + oriPwd);
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		String username = (String) o;
		
		//验证参数是否正确
		if (oriPwd == null || newPwd == null)
			return ResultBean.failure("2003");
		oriPwd = oriPwd.trim();
		newPwd = newPwd.trim();
		
		if (newPwd.length() < 1)
			return ResultBean.failure("2003");
		
		//验证原密码是否正确
		oriPwd = Util.MD5(oriPwd);
		if (teacherService.login(username, oriPwd) == null)
			return ResultBean.failure("1004");
		
		newPwd = Util.MD5(newPwd);
		teacherService.updatePwd(username, newPwd);
		
		return ResultBean.success();
	}
	
	@RequestMapping(value="/teachers", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<List<Teacher>> getTeachers(HttpServletRequest request) {
		System.out.println("GET获取所有teacher");
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1005");
		if ( !(o instanceof String) )
			return ResultBean.failure("1005");
		
		List<Teacher> list = teacherService.allTeachers();
		return ResultBean.success(list);
	}
	
	@RequestMapping(value="/teachers", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<Teacher> addTeacher(Teacher t, HttpServletRequest request) {
		System.out.println("POST新建teacher");
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		
		//检测输入合法性
		if (t.getUsername() == null || t.getName() == null)
			return ResultBean.failure("2003");
		
		List<Teacher> teachers = teacherService.allTeachers();
		if (teachers.contains(t))
			return ResultBean.failure("3001");
		
		t.setPwd(Util.MD5(t.getUsername()));
		teacherService.add(t);
		return ResultBean.success(t);
	}
	
	@RequestMapping(value="/teachers", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultBean<Teacher> delTeacher(Teacher t, HttpServletRequest request) {
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		
		//if (t.getUsername() == null)
		//	return ResultBean.failure("2001");
		
		List<Teacher> teachers = teacherService.allTeachers();
		if (!teachers.contains(t))
			return ResultBean.failure("2001");
		
		Teacher teacher = teacherService.find(t.getUsername());
		teacherService.delete(t.getUsername());
		return ResultBean.success(teacher);
	}
}
