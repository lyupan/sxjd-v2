package edu.ssdut.mapper;

import java.util.List;
import java.util.Map;

import edu.ssdut.model.Student;

public interface StudentMapper {
	

	/**
	 * Validate the login operation using given username and password in the parameter map
	 * @param map SHOULD contain two keys, username and password 
	 * @return returns the Student object specified by the username if validation passes, otherwise returns null
	 * @throws IllegalArgumentException parameter map doesnot contain enough keys
	 */
	Student login(Map<String, Object> map) throws IllegalArgumentException;
	
	/**
     * Using the all the fields (except id and username) in student to update a student record in the database. Do nothing if there is no
	 * such record in the database.
     * @param student the new Student Object
     */
	void update(Student student);
	
	
	void updatePwd(Map<String, Object> map);
	
	/**
	 * Delete the student record in the database according to the username. Do nothing if there is no
	 * such student record.
	 * @param username username of the student to delete
	 */
	void delete(String username);
	
	/**
	 * Add a new student record in the database. Do nothing if the record already exists.
	 * @param student the new student object
	 */
	void add(Student student);
	
	/**
	 * Returns the student object according to the record in the database. Returns null if there is no match.
	 * @param username username of the student
	 * @return the student object specified by the username field, null if no such element. 
	 */
	Student find(String username);
	
	/**
	 * Returns a list of all the student obejects in the database;
	 * @return
	 */
	List<Student> allStudents();
}
