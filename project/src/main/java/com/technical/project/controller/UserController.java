package com.technical.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technical.project.servicesImpl.UserService;
import com.technical.project.entity.User;

/**
 * This class is the entry point to the back office treatment.
 * Here only the data is collected from and return to the front
 * web pages. 
 * @author Alexis
 *
 */
@RestController
@RequestMapping("/technical")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;	
	
	/**
	 * Constructor without arguments
	 */
	public UserController() {}
	
	
	/**
	 * Manages the POST request to save a new user.
	 * RETURN :
	 * - "Not Acceptable" 406, Series.CLIENT_ERROR, 
	 * - "OK" returns 200 Series.SUCCESSFUL,
     * - "SEE_OTHER" returns 303 Series.REDIRECTION,
     * - "NOT_CONTENT" returns 204, Series.SUCCESSFUL.
     * 
	 * @param newUser <pre>String</pre> (a new user to save in the table)
	 * @return ResponseEntity<User> (adding new user in the BDD)
	 */
	@PostMapping(path = "/new")
	public ResponseEntity<User> newUser(@RequestBody String newUser) {
		
		if (newUser != null) {
			logger.info("Strating controller with variable value: {}", newUser);
			System.out.println("see the JSON string ------------>" + newUser);
			try {
				JSONObject json = new JSONObject(newUser);
				User user = userService.createAll(json);
				if (user == null) {
					logger.info("The user is not saved in under method : {}", user);
					return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
				} else {
					logger.info("The user is saved : {}", user.toString());
					return  new ResponseEntity<User>(user, HttpStatus.OK);
				}				
			} catch (JSONException ex) {
				logger.debug("There is a JSON exception thrown : {}", ex);
				return new ResponseEntity<User>(HttpStatus.SEE_OTHER);
			}			
		} else {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}		
	}
	
	
	/**
     * Manages the GET request on the /user/{username} end point, 
     * return the user if it exists or NOT_FOUND error if not.
     * RETURN :
     * - "OK" returns 200 Series.SUCCESSFUL,
     * - "NOT_CONTENT" returns 204, Series.SUCCESSFUL.
     * 
     * @param username <pre>String</pre> (the userName of the user to find)
     * @return ResponseEntity<User> (the user if exists, or NOT_FOUND error if not)
     */
	@GetMapping(path = "/user")//@PathVariable("userName")   
    public ResponseEntity<User> getUserByName(@RequestParam(required=true, value="userName") String username) {
		
		logger.info("Strating controller with variable value: {}", username);    	
		User user = userService.findUserByName(username);
        if (user != null) {
        	logger.info("The user by its name is found in the controller : {}", user.toString());
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } else {
        	logger.info("The user by its name is  not found in the controller");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
	
	/**
     * Manages the GET request on the /userID/{userId} end point, 
     * return the user if it exists or NOT_FOUND error if not
     * RETURN :
     * - "OK" returns 200 Series.SUCCESSFUL,
     * - "NOT_CONTENT" returns 204, Series.SUCCESSFUL,
     * - "SEE_OTHER" returns 303 Series.REDIRECTION,
     * - "NOT_FOUND" returns 404 Series.CLIENT_ERROR.
     * 
     * @param userId <pre>String</pre> (the user's id to find)
     * @return ResponseEntity<User> (the user if exists, or NOT_FOUND error if not)
     */
	@GetMapping(path = "/userID/{userId}")    
    public ResponseEntity<Optional<User>> getUserById(@PathVariable String userId) {
		
		logger.info("Strating controller with variable value as String : {}", userId);
		Optional<User> user = null;
		if (userId != null) {
			try {
				logger.info("Try to convert String value to integer");
				Integer idUser = Integer.parseInt(userId);
				user = userService.findUserById(idUser);
				if (user != null) {//exists
					logger.info("The user by its id is found in the controller : {}", user.toString());
					return new ResponseEntity<Optional<User>>(user, HttpStatus.OK);
				} else {//doesn't exist
					logger.info("The user by its id is not found in the controller : ");
					return new ResponseEntity<Optional<User>>(HttpStatus.NO_CONTENT);
				}
				
			} catch (NumberFormatException ex) {
				logger.debug("The conversion of the id has thrown an exception: {}", ex);
				return new  ResponseEntity<Optional<User>>(HttpStatus.SEE_OTHER);
			}
		} else {
			logger.info("The id given is null in the controller");
			return new ResponseEntity<Optional<User>>(HttpStatus.NOT_FOUND);
		}       
    }
	
	/**
	 * The method returns all users corresponding to their presence
	 * in the table.
	 * COMMENT : The list can be empty
	 * RETURN :
	 * - "OK" returns 200 Series.SUCCESSFUL,
	 * - "NOT_CONTENT" returns 204, Series.SUCCESSFUL.
	 *  
	 * @return (List of users found)
	 */
	@GetMapping("/users")	
	public ResponseEntity<List<User>> getAllUsers() {
		
		logger.info("Strating controller to find all users");
		List<User> usersList = new ArrayList<User>();		
		usersList = userService.findAllUsers();
		logger.info("The number of users found in the list in the controller: {}", usersList.size());
		if (usersList.size() > 0) {
			return new ResponseEntity<List<User>>(usersList, HttpStatus.OK);
		} else {
			logger.info("The list is empty without data in the controller");
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}		
	}
	
}
