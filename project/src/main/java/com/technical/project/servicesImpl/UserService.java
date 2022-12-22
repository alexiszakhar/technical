/**
 * 
 */
package com.technical.project.servicesImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.technical.project.idao.IUserDao;
import com.technical.project.entity.User;
import com.technical.project.iservices.IUserService;
import com.technical.project.tools.UsersGender;

/**
 * The class which implements all of the method used
 * partly by the controller class and partly by the DAO class.
 * This class also treats all the work rules.
 * @author Alexis
 *
 */
@Service
@Transactional
public class UserService implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	//time between now and a date to have french majority (18 years old at the nearest second) 
	private static final Long delta = 567993600289L;
	
	@Autowired
	private IUserDao userDao;

	/**
	 * Empty constructor
	 */
	public UserService() {}
	
	
	/**
	 * The method saves a new user, but
	 * this new has to be french and adult to be
	 * save.
	 * COMMENT : Base on this principle, the method
	 * saves ONLY French adult users. Any other user
	 * will not be validated. 
	 * @param user <pre>User</pre> (new user)
	 * @return
	 */
	@Override
	public User saveUser(User user) {
		
		logger.info("Strating with variable value: {}", user);
		if ("french".equals(user.getResidenceCountry().toLowerCase())) {
			Long birthDay = user.getBirthDate().getTime();
			logger.info("Birthday date value: {}", birthDay);
			Long present = new Date().getTime();
			logger.info("asked at this time value: {}", present);
			Long result = present - delta;
			if (birthDay <= result) {
				try {
					return userDao.save(new User(user));
				}  catch (IllegalArgumentException ex) {
					logger.debug("Error during the saving : {}", ex);
				}
			} 	
		}
		logger.info("The user doesn't meet the conditions: {}", user);
		return null;		
	}
		
	/**
	 * The method returns an user found by its id.
	 * If the user is not in the BDD, the method returns
	 * a "null" value  
	 * @param id <pre>Integer</pre> (id of an user saved in BDD)
	 * @return (an user or null)
	 */
	@Override
	public Optional<User> findUserById(Integer id) {
		
		logger.info("Strating with variable value: {}", id);		
		Optional<User> user = userDao.findById(id);
		if (user.isPresent()) {
			logger.info("The user by its id is found: {}", user);
			return user;
		} else {
			logger.info("The user by its id is not found");
			return null;
		}		
	}
	
	/**
	 * Because a name can have namesakes and is 
	 * therefore not unique by default, searching a user 
	 * by its name may return a list. So the method takes 
	 * the first name in the returned list. If there is no
	 * name corresponding to th search, the method returns 
	 * a "null" value.
	 * @param userName <pre>String</pre> (an or more user by its name)
	 * @return	
	 */
	@Override
    public User findUserByName(String userName) {
        
		logger.info("Strating with variable value: {}", userName);
		List<User> userList = new ArrayList<User>();
        userList = userDao.findByUserName(userName);
        if (!userList.isEmpty()) {
        	logger.info("The user by its name is found: {}", userList.get(0));
            return userList.get(0);
        } else {
        	logger.info("The user by its name is found");
            return null;
        }
    }
	
	/**
	 * The method searches and return all of users
	 * saved in the BDD. If no users exists in BDD the method
	 * return an empty list.
	 * @return List (all users)
	 */
	@Override
	public List<User> findAllUsers() {
		
		logger.info("Strating to find all users");
		List<User> userList = new ArrayList<User>();
		userList = (List<User>) userDao.findAll();		
		return userList;
	}
	
	/**
	 * The method retrieves all elements to create :
	 * a new User with => userName, birthDate, residenceCountry, phoneNumber, gender.
	 * If the third arguments are "null" or empty = "", the method return null and
	 * of course doesn't save the new user. 
	 * 
	 * @param json <pre>JSON</pre> (the JSON string to create the user)
	 * @return (the new user or null)
	 * @throws JSONException
	 */	
	public User createAll(JSONObject json) throws JSONException {		
		
		logger.info("To get an instance of User values : {}", json);
		Timestamp birth;
		UsersGender gender;		
		String userName = json.getString("userName");
		if ("null".equals(userName) || "".equals(userName)) {
			logger.info("The userName is not valid : {}", userName);
			return null;
		}
		String birthDate = json.getString("birthDay");
		if ("null".equals(birthDate) || "".equals(birthDate)) {
			logger.info("The birthDate is not valid : {}", birthDate);
			return null;
		} else {
			try {
				birth = Timestamp.valueOf(birthDate);
			} catch (IllegalArgumentException ex) {
				logger.debug("The birthDate is not valid : {}", ex);
				return null;
			}
		}
		String residenceCountry = json.getString("residenceCountry");
		if ("null".equals(residenceCountry) || "".equals(residenceCountry)) {
			logger.info("The residenceCountry is not valid : {}", residenceCountry);
			return null;
		}
		String phoneNumber = json.getString("phoneNumber");		
		String genderType = json.getString("gender");
		if (UsersGender.Female.toString().equals(genderType)) {
			gender = UsersGender.Female;
		} else if (UsersGender.Male.toString().equals(genderType)) {
			gender = UsersGender.Male;
		} else {
			gender = null;
		}
		User aNewUser = new User(userName, birth, residenceCountry, phoneNumber, gender);
		User user = saveUser(aNewUser);
		if (user == null) {
			logger.info("The user can't be saved : {}", userName);
			return null;
		} else {
			return user;
		}		
	}

}
