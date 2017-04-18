package edu.ssdut.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	public List<ProjectPhase> getPhases() {
		return phases;
	}
	public void setPhases(List<ProjectPhase> phases) {
		this.phases = phases;
	}

	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public boolean equals (Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Project))
			return false;
		Project that = (Project)o;
		return this.id == that.id || this.name == that.name;
	}
	
	private int id;
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", info=" + info + ", beginDate=" + beginDate + ", phases="
				+ phases + ", students=" + students + ", teacher=" + teacher + "]";
	}

	private String name;
	private String info;
	private Date beginDate;
	
	private List<ProjectPhase> phases = new ArrayList<ProjectPhase>();
	private List<Student> students = new ArrayList<Student>();
	private Teacher teacher;
}
