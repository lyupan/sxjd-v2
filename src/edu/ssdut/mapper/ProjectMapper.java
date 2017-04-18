package edu.ssdut.mapper;

import java.util.List;
import java.util.Map;

import edu.ssdut.model.Project;
import edu.ssdut.model.Student;

public interface ProjectMapper {

	void add(Project project);
	
	void update(Project project);
	
	void delete(int id);
	
	Project searchById(int id);
	
	Project searchByName(String name);
	
	List<Project> searchByTeacher(String username);
	
	List<Project> fuzzySearch(String name);
	
	List<Project> allProjects();
	
	void addMember(Map<String, Object> map);
	
	List<Student> allMembers(int projectId);
	
	void delMember(Map<String, Object> map);
	
	Student getMember(Map<String, Object> map);
}
