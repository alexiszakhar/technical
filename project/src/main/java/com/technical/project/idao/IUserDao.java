package com.technical.project.idao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.technical.project.entity.User;

/**
 * @author Alexis
 * This interface is a Spring Boot interface to join
 * the application with the data in BDD. To do this,
 * the class extends the "CrudRepository" belonging to
 * JPA package.  
 *
 */
public interface IUserDao extends CrudRepository<User, Integer> {
	
	/**
	 *  The method returns an user or an empty value
	 * @param id <pre>Integer</pre> (an id) 
	 */
	public Optional<User> findById(Integer id);
	
	/**
	 * The name can be not unique, so the method can return a
	 * list of users with the same name.
	 * @param userName <pre>String</pre> (a name of an user)
	 * 
	 */
	public List<User> findByUserName(String userName);
	
	/**
	 * The method returns all users presents in the table
	 */
	public List<User> findAll();

}
