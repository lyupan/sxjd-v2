package edu.ssdut.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ssdut.mapper.TeacherMapper;
import edu.ssdut.model.Teacher;
import edu.ssdut.service.TeacherService;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Resource
	private TeacherMapper teacherMapper;
	
	@Override
	public Teacher login(String username, String pwd) {
		if(username == null || pwd == null)
			return null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("pwd", pwd);
		return teacherMapper.login(map);
	}

	@Override
	public boolean update(Teacher teacher) {
		if (teacher == null || teacher.getUsername() == null)
			return false;
		if (find(teacher.getUsername()) == null)
			return false;
		teacherMapper.update(teacher);
		return true;
	}

	@Override
	public boolean delete(String username) {
		if (username == null || find(username) == null)
			return false;
		teacherMapper.delete(username);
		return true;
	}

	@Override
	public boolean add(Teacher teacher) {
		if (teacher == null || teacher.getUsername() == null)
			return false;
		if (find(teacher.getUsername()) != null)
			return false;
		teacherMapper.add(teacher);
		return true;
	}

	@Override
	public Teacher find(String username) {
		return teacherMapper.find(username);
	}

	@Override
	public List<Teacher> allTeachers() {
		List<Teacher> list = teacherMapper.allTeachers();
		if (list == null)
			list = new ArrayList<Teacher>();
		return list;
	}

	@Override
	public boolean updatePwd(String username, String pwd) {
		if (username == null || pwd == null)
			return false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		map.put("pwd", pwd);
		teacherMapper.updatePwd(map);
		return true;
	}

}
