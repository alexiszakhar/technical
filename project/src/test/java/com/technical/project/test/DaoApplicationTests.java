package com.technical.project.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.technical.project.entity.User;
import com.technical.project.idao.IUserDao;
import com.technical.project.tools.UsersGender;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class DaoApplicationTests {
	
	//good to be registered in table
	private static final String BIRTHDAY = "1980-11-11 12:14:23";

	@Autowired
	private IUserDao userDao;
		
	/**
	 * The method tests the connection to the BDD
	 * by adding a new user in the table. Here the
	 * new user respects the two conditions :
	 * - Adult
	 * - French
	 * So recording must be true
	 * COMMENT : this method has to be the first played
	 * for the following.	
	 */
	@Test
	@DisplayName("saveUser: to save an user")
	@Order(1)
	public void saveUser() {
		
		Timestamp birthDay = Timestamp.valueOf(BIRTHDAY);				
		User test = userDao.save(new User("Porniche", birthDay, "French", null, UsersGender.Female));
		boolean result = test != null ? true : false;
		System.out.println("result -------->" + result);
		assertTrue(result, "The user is saved");
	}
	
	/**
	 * The second method to run searches and
	 * find the previous recording, so because
	 * the user exist, the must return true. 
	 */
	@Test
	@DisplayName("findUserByName : for the previous recording")
	@Order(2)
	public void findUserByName() {
		
		List<User> users = userDao.findByUserName("Porniche");
		assertNotNull(users);
		assertTrue(!users.isEmpty());
		for (User user: users) { 
			System.out.println(user.toString());
		}
	}
	
	/**
	 * The third test is the same that the previous
	 * except that here the search is about the new
	 * recorded user id.
	 */
	@Test
	@DisplayName("findUserById : for the previous recording")
	@Order(3)
	public void findUserById() {		
		
		Optional<User> user = null;
		boolean result = false; 
		try {
			user = userDao.findById(2);
			result = user != null ? true : false;
			assertTrue(result, "The result is :" + user);
			if (result) {
				System.out.println(user.toString());
			}			
		} catch (IllegalArgumentException e) {
			assertFalse(false, "The doesn't work" + e);
		}		
	}
	
	/**
	 * The last test searches and return all of the users
	 * who are in the BDD
	 */
	@Test
	@DisplayName("findAllUsers : all users in the database")
	@Order(4)
	public void findAllUsers() {		
		
		List<User> users = (List<User>) userDao.findAll();
		assertNotNull(users);
		assertTrue(!users.isEmpty());
		for (User user: users) {
			System.out.println("<------------- in ----->");
			System.out.println("the user ----->" + user.toString());
		}		
	}

}
