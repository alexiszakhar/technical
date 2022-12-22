package com.technical.project.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.technical.project.entity.User;
import com.technical.project.servicesImpl.UserService;
import com.technical.project.tools.UsersGender;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class ServicesApplicationTest {
	
	//too young to be registered
	private static final String BIRTHDAY1 = "2006-10-21 08:14:23";
	//well to be registered in table
	private static final String BIRTHDAY2 = "1969-11-11 12:14:23";
	
	@Autowired
	private UserService userService;
	
	/**
	 * The method tests the save service method.
	 * To do this, the test starts by the creation of
	 * 3 users :
	 * - the first is French BUT too young, so it can't be
	 * saved in BDD
	 * - The second is French and its age is over 18 years old,
	 * So he is saved in BDD. Notice that his age is under 
	 * January 1, 1970, 00:00:00 GMT.
	 *  - The third has a good age BUT he isn't French, so it
	 *  can't be saved in BDD.
	 *  This method has to start first. 
	 */
	@Test
	@DisplayName("saveUser with conditions")
	@Order(1)
	public void saveUser() {
		
		boolean result = false;
		User test = null;		
		Timestamp birthDay1 = Timestamp.valueOf(BIRTHDAY1);
		User user1 = new User("Alfan", birthDay1, "French", null, UsersGender.Male);
		Timestamp birthDay2 = Timestamp.valueOf(BIRTHDAY2);
		User user2 = new User("Moulin", birthDay2, "French", "0492201023", UsersGender.Female);
		User user3 = new User("Smith", birthDay2, "English", "", UsersGender.Male);
		List<User> userList = new ArrayList<User>();
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		for (User user : userList) {
			if (user.equals(user1)) {
				test = userService.saveUser(user1);
				result = test != null ? true : false;
				assertFalse(result, "The user too young to be saved");
			}
			if (user.equals(user2)) {
				test = userService.saveUser(user2);
				result = test != null ? true : false;
				assertTrue(result, "The user is saved");				
			}
			if (user.equals(user3)) {
				test = userService.saveUser(user3);
				result = test != null ? true : false;
				assertFalse(result, "The user is adult but not french to be saved");
			}
		}
	}
	
	/**
	 * 	The second test in the hierarchy checks the presence of
	 * the user named "Moulin" and the absence of the user
	 * named "Smith" which is not French.
	 */
	@Test
	@DisplayName("findUserByName : for users named Moulin and Smith")
	@Order(2)
	public void findUserByName() {		
		
		boolean result = false;
		User user = userService.findUserByName("Moulin");
		assertNotNull(user);
		result = user != null ? true : false;		
		if (user != null) {
			assertTrue(result, "The user exist in BDD");
			System.out.println("result -------->" + user.toString());
		} else {
			assertFalse(result, "The user doesn't exist");
		}
		user = userService.findUserByName("Smith");
		assertNull(user);
	}
	
	/**
	 * The third test in the hierarchy find an user by its id.
	 * Depending on whether or not you have done the "DaoApplicationTests"
	 * before, the user's id can have the number 2 or 3. So the tests the
	 * two possibilities and if it finds one good, the method stops the
	 * search. 
	 */
	@Test
	@DisplayName("findUserById : two possibilities 2 or 3")
	@Order(3)
	public void findUserById() {
		
		boolean result = false;
		Optional<User> user = null;
		for (int i = 2; i < 4; i++) {
			user = userService.findUserById(i);
			if (user != null) {
				break;
			}
		}		
		assertNotNull(user);
		result = user != null ? true : false;
		assertTrue(result, "The user exists");
		if (user != null) {
			System.out.println("result -------->" + user.toString());
		}		
	}
	
	/**
	 * The last test searches and return all of the users
	 * who are in the BDD
	 */
	@Test
	@DisplayName("findAllUsers: all users in the database")
	@Order(4)
	public void findAllUsers() {
		
		List<User> users = (List<User>) userService.findAllUsers();
		assertNotNull(users);
		assertTrue(!users.isEmpty());
		for (User user: users) {
			System.out.println("<------------- in ----->");
			System.out.println("the user ----->" + user.toString());
		}
		
	}
	
	
		

}
