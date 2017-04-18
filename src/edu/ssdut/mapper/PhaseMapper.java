package edu.ssdut.mapper;

import java.util.List;
import java.util.Map;

import edu.ssdut.model.ProjectPhase;

public interface PhaseMapper {

	void add(ProjectPhase phase);
	
	void update(ProjectPhase phase);
	
	void delete(Map<String, Object> map);
	
	List<ProjectPhase> allPhases(int projectId);
	
	int maxPhaseId(int projectId);
	
	ProjectPhase get(Map<String, Object> map);
}
