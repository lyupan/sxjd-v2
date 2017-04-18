package edu.ssdut.mapper;

import java.util.List;
import java.util.Map;

import edu.ssdut.model.Teacher;

public interface TeacherMapper {

	/**
	 * Validate the login operation using given username and password in the parameter map
	 * @param map SHOULD contain two keys, username and password 
	 * @return returns the Teacher object specified by the username if validation passes, otherwise returns null
	 * @throws IllegalArgumentException parameter map doesnot contain enough keys
	 */
	Teacher login(Map<String, Object> map) throws IllegalArgumentException;
	
	/**
     * Using the all the fields (except id and username) in teacher to update a teacher record in the database. Do nothing if there is no
	 * such record in the database.
     * @param teacher the new Teacher Object
     */
	void update(Teacher teacher);
	
	void updatePwd(Map<String, Object> map);
	
	/**
	 * Delete the teacher record in the database according to the username. Do nothing if there is no
	 * such teacher record.
	 * @param username username of the teacher to delete
	 */
	void delete(String username);
	
	/**
	 * Add a new teacher record in the database. Do nothing if the record already exists.
	 * @param teacher the new teacher object
	 */
	void add(Teacher teacher);
	
	/**
	 * Returns the teacher object according to the record in the database. Returns null if there is no match.
	 * @param username username of the teacher
	 * @return the teacher object specified by the username field, null if no such element. 
	 */
	Teacher find(String username);
	
	/**
	 * Returns a list of all the teacher obejects in the database;
	 * @return
	 */
	List<Teacher> allTeachers();
}
