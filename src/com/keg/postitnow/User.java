package com.keg.postitnow;

/**
 * User class represents the user (more precisely the client)
 */
public class User {
	/**
	 * The phone number of the user
	 */
	private String phoneNumber;
	
	/**
	 * The last session
	 */
	private String session;
	
	/**
	 * The user's id
	 */
	private Long userId;
	
	
	/**
	 * Public constructor
	 */
	public User(){
		this.phoneNumber = "";
		this.session = "";
	}
	
	/**
	 * Public constructor
	 * 
	 * @param	phoneNumber - the phone number of the user
	 */
	public User(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * Update the user
	 * 
	 * @param userId - The id of the user
	 * @param phoneNumber - The phone number of the user
	 * @param session - The session of the user
	 */
	public void update(Long userId, String phoneNumber, String session) {
		this.userId = userId;
		this.phoneNumber = phoneNumber;
		this.session = session;
	}
	
	/**
	 * update the phone number
	 * 
	 * @param phoneNumber - The new phone number
	 */
	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * update the session
	 * 
	 * @param session - The new session
	 */
	public void updateSession(String session) {
		this.session = session;
	}
	
	/**
	 * Update the user id
	 * 
	 * @param userId - The new user's id
	 */
	public void updateUserId(String userId) {
		this.userId = new Long(userId);
	}

	
	/**
	 * Return the phone number of the user
	 * @return the phone number of the user
	 */
	public String returnPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Return the session of the user
	 * @return the session of the user
	 */
	public String returnSession() {
		return session;
	}
	
	/**
	 * Return the id of the user
	 * @return the id of the user
	 */
	public Long returnUserId() {
		return userId;
	}
}
