package edu.ssdut.model;

public class ProjectPhase {
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getPhaseId() {
		return phaseId;
	}
	public void setPhaseId(int phaseId) {
		this.phaseId = phaseId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ProjectPhase))
			return false;
		ProjectPhase that = (ProjectPhase)o;
		return this.projectId == that.projectId && this.phaseId == that.phaseId;
	}
	
	private int projectId;
	private int phaseId;
	private String name;
	private String fileName;
}
