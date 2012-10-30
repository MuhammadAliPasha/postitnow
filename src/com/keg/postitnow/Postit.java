package com.keg.postitnow;

import java.util.ArrayList;


/** 
 * Class that represents a post it
 */
public class Postit {
	/** 
	 * Postit's id
	 */
	private Long id;

	/** 
	 * Postit's message
	 */
	private String content;
	
	/** 
	 * Postit's mood
	 */	
	private String mood;
	
	
	/** 
	 * Postit's author's name
	 */
	private String name;
	
	/** 
	 * Postit's author's avatar
	 */
	private String image;
	
	/** 
	 * Postit's date
	 */
	private String date;
	
	/** 
	 * Postit's author's id
	 */
	private Long userId;
	
	/** 
	 * Postit's replies
	 */
	private ArrayList<Reply> replies;

	
	/** 
	 * Public constructor
	 * 
	 * @param 	id
	 * 			content
	 * 			mood
	 * 			name
	 * 			image
	 * 			date
	 * 			userId
	 */
	public Postit(Long id, String content, String mood, String name, String image, String date, Long userId){
		this.id = id;
		this.content = content;
		this.mood = mood;
		this.name = name;
		this.image = image;
		this.date = date;
		this.userId = userId;
		this.replies = new ArrayList<Reply>();
	}
	
	
	
	/** 
	 * Add a reply
	 * 
	 * @param 	reply the reply we want to add
	 */
	public void add(Reply reply) {
		this.replies.add(reply);
	}
	
	
	/** 
	 * Return the id of the postit
	 */
	public Long returnId() {
		return this.id;
	}

	/** 
	 * Return the id of the author's name
	 */
	public String returnUsername() {
		return this.name;
	}
	
	/** 
	 * Return the mood of the author when he posted
	 */
	public String returnMood() {
		return this.mood;
	}
	
	/** 
	 * Return the content of the postit
	 */
	public String returnContent() {
		return this.content;
	}
	
	/** 
	 * Return the url of the avatar of the author
	 */
	public String returnImage() {
		return this.image;
	}
	
	
	/** 
	 * Return the date when the post it was posted
	 */
	public String returnDate() {
		return this.date;
	}
	
	
	/** 
	 * Return the id of the author
	 */
	public Long returnUserId() {
		return this.userId;
	}
	
	
	/** 
	 * Return the list of the replies associated with the postit
	 */
	public ArrayList<Reply> returnReplies() {
		return this.replies;
	}

}
