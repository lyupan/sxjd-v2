package edu.ssdut.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.ssdut.bean.ResultBean;
import edu.ssdut.model.Student;
import edu.ssdut.service.StudentService;

/**
 * Control the operation on student object
 * @author LyuPan
 */

@Controller
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	//GET请求用于获取资源
	@RequestMapping(value="/student", method = RequestMethod.GET)
	@ResponseBody
	public ResultBean<Student> getStudent(HttpServletRequest request) {
		System.out.println("GET");

		//验证是否登录
		Object s = request.getAttribute("username");
		if (s == null)
			return ResultBean.failure("1004");
		if ( !(s instanceof String) )
			return ResultBean.failure("1004");
		
		String username = (String) s;
		Student student = studentService.find(username);
		return ResultBean.success(student);
	}
	
	//PUT请求用于更新资源
	@RequestMapping(value="/student", method = RequestMethod.PUT)
	@ResponseBody
	public ResultBean<Student> updateStudent(Student student, HttpServletRequest request) {
		System.out.println("PUT");
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		String username = (String) o;

		//验证修改的对象是否是登录的对象
		if ( !username.equals(student.getUsername()) )
			return ResultBean.failure("1004");
				
		studentService.update(student);
		student = studentService.find(username);
		//返回更新后的对象
		return ResultBean.success(student);
	}

	//PATCH 更新资源，此处用于修改密码
	@RequestMapping(value="/student", method=RequestMethod.PATCH)
	@ResponseBody
	public ResultBean<Student> updatePwd(String oriPwd, String newPwd, HttpServletRequest request) {
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
		if ( studentService.login(username, oriPwd) == null)
			return ResultBean.failure("1004");
		
		newPwd = Util.MD5(newPwd);
		studentService.updatePwd(username, newPwd);
		
		//返回更新后的对象
		Student student = studentService.find(username);
		return ResultBean.success(student);
	}
	
	//GET获取资源
	@RequestMapping(value="/students", method=RequestMethod.GET)
	@ResponseBody
	public ResultBean<List<Student>> allStudents(HttpServletRequest request) {
		System.out.println("GET获取所有student");
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1005");
		if ( !(o instanceof String) )
			return ResultBean.failure("1005");

		List<Student> list =  studentService.allStudents();
		return ResultBean.success(list);
	}
	
	//POST请求用于新建资源
	@RequestMapping(value="/students", method=RequestMethod.POST)
	@ResponseBody
	public ResultBean<Student> addStudent(Student s, HttpServletRequest request) {
		System.out.println("POST新建Student");
		
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		//String username = (String) o;
		
		//检测输入合法性
		if (s.getUsername() == null || s.getName() == null)
			return ResultBean.failure("2003");
		List<Student> students = studentService.allStudents();
		if (students.contains(s))
			return ResultBean.failure("3001");
		
		//默认密码与帐号相同
		s.setPwd(Util.MD5(s.getUsername()));
		studentService.add(s);
		return ResultBean.success(s);
	}
	
	//DELETE请求用于删除资源
	@RequestMapping(value="/students", method=RequestMethod.DELETE)
	@ResponseBody
	public ResultBean<Student> delStudent(Student s, HttpServletRequest request) {
		//验证是否登录
		Object o = request.getAttribute("username");
		if (o == null)
			return ResultBean.failure("1004");
		if ( !(o instanceof String) )
			return ResultBean.failure("1004");
		
		System.out.println(s);
		if (s.getUsername() == null)
			return ResultBean.failure("2001");
		List<Student> students = studentService.allStudents();
		if (!students.contains(s))
			return ResultBean.failure("2001");
		
		s = studentService.find(s.getUsername());
		studentService.delete(s.getUsername());
		return ResultBean.success(s);
	}
}
