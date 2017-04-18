package edu.ssdut.service;

import java.util.List;

import edu.ssdut.model.Teacher;


public interface TeacherService {

	/**
     * Validate the login operation using given username and password. If the validation fails, 
     * returns null; otherwise returns the Teacher object specified by the username.
     * 
     * @param username username of the user
     * @param pwd MD5 of the original password
     * @return the Teacher object specified by the username, null if validation fails.
     */
	Teacher login(String username, String pwd);
	
	/**
     * Using the parameter teacher to update all the fields in teacher except id and username.Do nothing if there is no
	 * such teacher.  
     * @param teacher the new Teacher Object
     */
	boolean update(Teacher teacher);
	
	boolean updatePwd(String username, String pwd);
	
	/**
	 * Delete the teacher object according to the username. Do nothing if there is no
	 * such teacher.
	 * @param username username of the teacher to delete
	 */
	boolean delete(String username);
	
	/**
	 * Add a new teacher object. Do nothing if the same teacher (the method equals returns true) exists.
	 * @param teacher the new teacher object
	 */
	boolean add(Teacher teacher);
	
	/**
	 * Returns the teacher object according to the username field. Returns null if there is no match.
	 * @param username username of the teacher
	 * @return the teacher object specified by the username field, null if no such element. 
	 */
	Teacher find(String username);
	
	/**
	 *  Returns all the teacher objects. The size of the list is zero when there is no teacher object.
	 * @return A list of all teacher objects;
	 */
	List<Teacher> allTeachers();
}
