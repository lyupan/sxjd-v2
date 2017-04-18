package edu.ssdut.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ssdut.mapper.StudentMapper;
import edu.ssdut.model.Student;
import edu.ssdut.service.StudentService;

@Service
@Transactional  //此处不再进行创建SqlSession和提交事务，都已交由spring去管理了。
public class StudentServiceImpl implements StudentService {
	
	@Resource
	private StudentMapper studentMapper;
	
	@Override
	public Student login(String username, String pwd) {
		if(username == null || pwd == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("pwd", pwd);
		Student s = null;
		try {
			s = studentMapper.login(map);
		} catch(IllegalArgumentException e) {
			System.out.println(e);
		}
		return s;
	}

	@Override
	public boolean update(Student student) {
		if (student == null || student.getUsername() == null)
			return false;
		if (find(student.getUsername()) == null)
			return false;
		studentMapper.update(student);
		return true;
	}

	@Override
	public boolean delete(String username) {
		if (username == null)
			return false;
		if (find(username) == null)
			return false;
		studentMapper.delete(username);
		return true;
	}

	@Override
	public boolean add(Student student) {
		if (student == null || student.getUsername() == null)
			return false;
		if (find(student.getUsername()) != null)
			return false;
		studentMapper.add(student);
		return true;
	}

	@Override
	public Student find(String username) {
		return studentMapper.find(username);
	}

	@Override
	public List<Student> allStudents() {
		List<Student> students = studentMapper.allStudents();
		if (students == null)
			students = new ArrayList<Student>();
		return students;
	}

	@Override
	public boolean updatePwd(String username, String pwd) {
		if (username == null || pwd == null)
			return false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("pwd", pwd);
		studentMapper.updatePwd(map);
		return true;
	}

}
