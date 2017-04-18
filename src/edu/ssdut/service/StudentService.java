package edu.ssdut.service;

import java.util.List;

import edu.ssdut.model.Student;

public interface StudentService {

	/**
     * Validate the login operation using given username and password. If the validation fails, 
     * returns null; otherwise returns the Student object specified by the username.
     * 
     * @param username username of the user
     * @param pwd MD5 of the original password
     * @return the Student object specified by the username, null if validation fails.
     */
	Student login(String username, String pwd);
	
	/**
     * Using the parameter student to update all the fields in student except id and username.Return false if there is no
	 * such student.  
     * @param student the new Student Object
     */
	boolean update(Student student);
	
	
	boolean updatePwd(String username, String pwd);
	
	
	/**
	 * Delete the student object according to the username. Return false if there is no
	 * such student.
	 * @param username username of the student to delete
	 */
	boolean delete(String username);
	
	/**
	 * Add a new student object. Return false if the same student (the method equals returns true) exists.
	 * @param student the new student object
	 */
	boolean add(Student student);
	
	/**
	 * Returns the student object according to the username field. Returns null if there is no match.
	 * @param username username of the student
	 * @return the student object specified by the username field, null if no such element. 
	 */
	Student find(String username);
	
	/**
	 *  Returns all the student objects. The size of the list is zero when there is no student object.
	 * @return A list of all student objects;
	 */
	List<Student> allStudents();
	
}
