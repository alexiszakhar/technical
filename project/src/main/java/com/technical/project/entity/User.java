package com.technical.project.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.technical.project.tools.UsersGender;

/**
 * 
 * @author Alexis
 * Tis class represents an entity, which is the jonction
 * between the application and a table of the database.
 * Here the table is an user defined by some arguments. 
 *
 */
@Entity
@Table (name = "users", schema = "public")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//iduser is automatcly incremented in the BDD table
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iduser", columnDefinition = "serial")
	private Integer idUser;
	
	//the name of an user remarks that the name can't be unique due to synonymous 
	@Column(name = "user_name", nullable = false, length=45)
    private String userName;
	
	//birthday date of an user
    @Column(name = "birthdate", nullable = false)
    private Timestamp birthDate;
    
    //the country where the user leaves
    @Column(name = "residence_country", nullable = false, length=45)
    private String residenceCountry;
    
    //it's phone number but not mandatory
    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;
    
    //the sex he or she is
    @Column(name = "gender", nullable = true)
	@Enumerated(EnumType.STRING)
	private UsersGender gender;
    
    /**
     * Constructor without argument
     */
	public User() {
		super();
	}
	
	/**
	 * The constructor with each argument
	 * @param name <pre>String</pre> (the name of the user)
	 * @param birthDay <pre>Timestamp</pre> (the birthday date)
	 * @param country <pre>String</pre> (the name of the country)
	 * @param phone <pre>String</pre> (the phone number)
	 * @param genderType <pre>Enum</pre> (his or her gender)
	 */
	public User(String name, Timestamp birthDay, String country, String phone, UsersGender genderType) {
		
		userName = name;
    	birthDate = birthDay;
    	residenceCountry = country;
    	phoneNumber = phone;
    	gender = genderType;
	}
	
	/**
	 * This constructor can be used when an user already
	 * exists and some of its arguments are changed.
	 * @param user <pre>User</pre> (an instance of an user)
	 */
	public User(User user) {
    	super ();		
    	
		userName = user.getUsername();
    	birthDate = user.getBirthDate();
    	residenceCountry = user.getResidenceCountry();
    	phoneNumber = user.getPhoneNumber();
    	gender = user.getGender();
    }
	
	/**
	 * Method to get the "idUser" argument
	 * @return <pre>Integer</pre> (the id of an instance)
	 */
	public Integer getIdUser() {
		return idUser;
	}
	
	/**
	 * Method to set the "idUser" argument
	 * @param idUser <pre>Integer</pre> (the id of the user)
	 */
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	
	/**
	 * Method to get the "userName" argument
	 * @return <pre>String</pre> (the  user name)
	 */
	public String getUsername() {
		return userName;
	}
	
	/**
	 * Method to set the "userName" argument
	 * @param username <pre>String</pre> (the name of the user)
	 */
	public void setUsername(String username) {
		this.userName = username;
	}
	
	/**
	 * Method to get the "birthDate" argument
	 * @return <pre>Timestamp</pre> (the date of the birthday)
	 */
	public Timestamp getBirthDate() {
		return birthDate;
	}
	
	/**
	 * Method to set the "birthDate" argument
	 * @param birthdate <pre>Timestamp</pre> (the birthday date)
	 */
	public void setBirthdate(Timestamp birthDay) {
		this.birthDate = birthDay;
	}
	
	/**
	 * Method to get the "residenceCountry" argument
	 * @return <pre>String</pre> (the name of the country or null)
	 */
	public String getResidenceCountry() {
		return residenceCountry;
	}
	
	/**
	 * Method to set the "residenceCountry" argument
	 * @param residenceCountry <pre>String</pre> (the name of the country)
	 */
	public void setResidenceCountry(String residenceCountry) {
		this.residenceCountry = residenceCountry;
	}
	
	/**
	 * Method to get the "phoneNumber" argument
	 * @return <pre>String</pre> (the phone number or null)
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Method to set the "phoneNumber" argument
	 * @param phoneNumber <pre>String</pre> (the phone number)
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Method to get the "gender" argument
	 * @return <pre>Enum</pre> (the gender)
	 */
	public UsersGender getGender() {
		return gender;
	}
	
	/**
	 * Method to set the "gender" argument
	 * @param gender <pre>Enum</pre> (his or her gender)
	 */
	public void setGender(UsersGender gender) {
		this.gender = gender;
	}

	@Override
	public int hashCode() {
		return Objects.hash(birthDate, gender, idUser, phoneNumber, residenceCountry, userName);
	}
	
	/**
	 * Method to compare the equality between to users
	 * @param obj <pre>Object</pre> (here an user instance)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(birthDate, other.birthDate) && gender == other.gender
				&& Objects.equals(idUser, other.idUser) && Objects.equals(phoneNumber, other.phoneNumber)
				&& Objects.equals(residenceCountry, other.residenceCountry) && Objects.equals(userName, other.userName);
	}
	
	/**
	 * Method to display information about an user.
	 */
	@Override
	public String toString() {
		return "User [idUser = " + idUser + ", user name = " + userName + ", birthdate = " + birthDate + ", residence country = "
				+ residenceCountry + ", phone number = " + phoneNumber + ", gender = " + gender + "]";
	}	

}
