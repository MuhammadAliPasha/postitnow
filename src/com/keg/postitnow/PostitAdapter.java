package com.keg.postitnow;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The adapter of the list for the postits
 */
public class PostitAdapter extends BaseAdapter {
	/**
	 * ArrayList of Hashmap that represents the postits and replies
	 */
	private ArrayList<HashMap<String, String> > posts;
	
	/**
	 * Hashmap of avatar
	 */
	private HashMap<String, Drawable> avatar;
	
	/**
	 * The context
	 */
	private Context mContext;
	
	/**
	 * The layout inflater
	 */
	private LayoutInflater mInflater;

	/**
	 * Public constructor
	 * 
	 * @param 	context - The context
	 * 			posts - the postits we want to display
	 * 			avatar - the avatars we want to display
	 */
	public PostitAdapter(Context context, ArrayList<HashMap<String, String> > posts, HashMap<String, Drawable> avatar) {
		this.mContext = context;
		this.posts = posts;
		this.avatar = avatar;
		this.mInflater = LayoutInflater.from(mContext);
	}

	
	/**
	 * refresh the list
	 * 
	 * @param 	context - The context
	 * 			posts - the postits we want to display
	 * 			avatar - the avatars we want to display
	 */
	public void refresh(Context context, ArrayList<HashMap<String, String> > posts, HashMap<String, Drawable> avatar) {
		this.mContext = context;
		this.posts = posts;
		this.avatar = avatar;
		this.mInflater = LayoutInflater.from(mContext);
	}
	
	
	/**
	 * count how many postits we are going to display
	 */
	public int getCount() {
		return posts.size();
	}

	
	/**
	 * Get an item
	 * 
	 * @param 	position is an Integer representing the position of the postit we want to retrieve
	 */
	public Object getItem(int position) {
		return posts.get(position);
	}

	/**
	 * Get an item's id
	 * 
	 * @param 	position is an Integer representing the position of the postit's id we want to retrieve
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Return the view according to whether we have a postitt or a reply
	 * 
	 * @param 	position is an Integer representing the position of the postit's id we want to retrieve
	 * 			convertView is not used here
	 * 			parent used to built a linear layout
	 */	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * getView
		 * input: int position, View convertView, ViewGroup parent (read Android doc)
		 * output: View
		 * Set up the layour for reply or post
		 * 
		 */
		LinearLayout layoutItem;

		TextView idReply;
		if (posts.get(position).get("type") == "postit") {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.postit, parent, false);
		}
		else {
		    layoutItem = (LinearLayout) mInflater.inflate(R.layout.reply, parent, false);

		    idReply = (TextView) layoutItem.findViewById(R.id.idReply);
			idReply.setText(posts.get(position).get("idReply"));
		}
		
	    TextView idPostit = (TextView) layoutItem.findViewById(R.id.idPostit);
		TextView username = (TextView) layoutItem.findViewById(R.id.username);
		TextView date = (TextView) layoutItem.findViewById(R.id.date);
		TextView postit = (TextView) layoutItem.findViewById(R.id.postit);
		TextView type = (TextView) layoutItem.findViewById(R.id.type);
		TextView userId = (TextView) layoutItem.findViewById(R.id.userId);
		
		idPostit.setText(posts.get(position).get("idPostit"));
		username.setText(posts.get(position).get("username"));
		date.setText(posts.get(position).get("date"));
		postit.setText(posts.get(position).get("postit").replace("。。。"," 。。。"));
		type.setText(posts.get(position).get("type"));
		userId.setText(posts.get(position).get("userId"));
		
		
		ImageView imgView = (ImageView) layoutItem.findViewById(R.id.avatar);
		Drawable image  = avatar.get(posts.get(position).get("image"));
		imgView.setImageDrawable(image);

		return layoutItem;
	}
	
	

		
}
