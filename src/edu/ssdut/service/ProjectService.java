package edu.ssdut.service;

import java.util.List;

import edu.ssdut.model.Project;
import edu.ssdut.model.ProjectPhase;
import edu.ssdut.model.Student;

public interface ProjectService {

	Project add(Project project);
	
	boolean update(Project project);
	
	Project delete(int id);
	
	Project searchById(int id);
	
	List<Project> allProjects();
	
	List<Project> searchByTeacher(String username);
	
	List<Project> fuzzySearch(String name);
	
	Project searchByName(String name);
	
	boolean addPhase(ProjectPhase pp);
	
	boolean updatePhase(ProjectPhase pp);
	
	boolean deletePhase(int projectId, int phaseId);
	
	List<ProjectPhase> allPhases(int projectId);
	
	ProjectPhase getPhase(int projectId, int phaseId);
	
	boolean addMember(int projectId, String username);
	
	List<Student> allMembers(int projectId);
	
	boolean delMember(int projectId, String username);
	
	Student getMember(int projectId, String username);
}
