/**
 * 
 */
package com.technical.project.iservices;

import java.util.List;
import java.util.Optional;

import com.technical.project.entity.User;



/**
 * @author Alexis
 * Interface which define the signature of methods
 * used in an implementation class service.
 */
public interface IUserService {
	
	//save user
	public User saveUser(User user);	
	
	//find user by its id
	public Optional<User> findUserById(Integer id);
	
	//find user by its name
	public User findUserByName(String username);
	
	//find all users in a table
	public List<User> findAllUsers();
	

}
