package edu.ssdut.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ssdut.mapper.PhaseMapper;
import edu.ssdut.mapper.ProjectMapper;
import edu.ssdut.mapper.StudentMapper;
import edu.ssdut.model.Project;
import edu.ssdut.model.ProjectPhase;
import edu.ssdut.model.Student;
import edu.ssdut.service.ProjectService;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Resource
	private ProjectMapper projectMapper;
	@Resource
	private PhaseMapper phaseMapper;
	@Resource
	private StudentMapper studentMapper;
	
	@Override
	public Project add(Project project) {
		if (project == null || project.getName() == null)
			return null;
		
		String name = project.getName();
		if (searchByName(name) != null)
			return null;
		projectMapper.add(project);
		return projectMapper.searchByName(name);
	}

	@Override
	public boolean update(Project project) {
		if (project == null)
			return false; 
		if (searchById(project.getId()) == null)
			return false;
		projectMapper.update(project);
		return true;
	}

	@Override
	public Project delete(int id) {
		Project p = projectMapper.searchById(id);
		if (p == null)
			return null;
		projectMapper.delete(id);
		return p;
	}

	@Override
	public Project searchById(int id) {
		Project p =  projectMapper.searchById(id);
		if (p != null)
			p.setPhases(phaseMapper.allPhases(id));
		return p;
	}

	@Override
	public List<Project> allProjects() {
		List<Project> list = projectMapper.allProjects();
		if (list == null)
			list = new ArrayList<Project>();
		return list;
	}

	@Override
	public List<Project> searchByTeacher(String username) {
		List<Project> list = projectMapper.searchByTeacher(username);
		if (list == null)
			list = new ArrayList<Project>();
		return list;
	}

	@Override
	public Project searchByName(String name) {
		Project p = projectMapper.searchByName(name);
		if (p != null)
			p.setPhases(phaseMapper.allPhases(p.getId()));
		return p;
	}

	@Override
	public List<Project> fuzzySearch(String name) {
		List<Project> list = projectMapper.fuzzySearch(name);
		if (list == null)
			list = new ArrayList<Project>();
		return list;
	}

	@Override
	public boolean addPhase(ProjectPhase pp) {
		if (pp == null)
			return false;
		if (getPhase(pp.getProjectId(), pp.getPhaseId()) != null)
			return false;
		
//		int id = phaseMapper.maxPhaseId(pp.getProjectId());
//		if (pp.getPhaseId() != id + 1)
//			return false;
		
		phaseMapper.add(pp);
		return true;
	}

	@Override
	public boolean updatePhase(ProjectPhase pp) {
		if (pp == null)
			return false;
		if (projectMapper.searchById(pp.getProjectId()) == null)
			return false;
		phaseMapper.update(pp);
		return true;
	}

	@Override
	public boolean deletePhase(int projectId, int phaseId) {
		if (getPhase(projectId, phaseId) == null)
			return false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", projectId);
		map.put("phaseId", phaseId);
		
		phaseMapper.delete(map);
//		List<ProjectPhase> phases = allPhases(projectId);
//		for (ProjectPhase p : phases) {
//			int id = p.getPhaseId();
//			if (id < phaseId) {
//				p.setPhaseId(id - 1);
//				phaseMapper.update(p);
//			}
//		}
		return true;
	}

	@Override
	public List<ProjectPhase> allPhases(int projectId) {
		List<ProjectPhase> phases = phaseMapper.allPhases(projectId);
		if (phases == null)
			phases = new ArrayList<ProjectPhase>();
		return phases;
	}

	@Override
	public ProjectPhase getPhase(int projectId, int phaseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", projectId);
		map.put("phaseId", phaseId);
		return phaseMapper.get(map);
	}

	@Override
	public boolean addMember(int projectId, String username) {
		if (searchById(projectId) == null)
			return false;
		if (studentMapper.find(username) == null)
			return false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", projectId);
		map.put("username", username);
		projectMapper.addMember(map);
		return true;
	}

	@Override
	public List<Student> allMembers(int projectId) {
		return projectMapper.allMembers(projectId);
	}

	@Override
	public boolean delMember(int projectId, String username) {
		if (searchById(projectId) == null)
			return false;
		if (studentMapper.find(username) == null)
			return false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", projectId);
		map.put("username", username);
		projectMapper.delMember(map);
		return true;
	}

	@Override
	public Student getMember(int projectId, String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectId", projectId);
		map.put("username", username);
		return projectMapper.getMember(map);
	}

}
