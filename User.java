import java.util.ArrayList;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class User implements PostObserver, ItemElement{
	private String ID;
	private Feed toFollowers;
	private DefaultListModel feed;
	Admin admin;
	private DefaultListModel<User> following;
	private ArrayList<String> posts;
	private long TimeCreated;
	private long LastUpdate;
	private UserView gui;
	
	
	public User(String ID)
	{
	// basic initialization
		this.ID = ID;
		this.toFollowers = new Feed();
		feed = new DefaultListModel();
		admin = AdminSingleton.getInstance();
		following = new DefaultListModel<User>();
		posts = new ArrayList<String>();
		TimeCreated = System.currentTimeMillis();
		LastUpdate = System.currentTimeMillis();
	}
	
	public void follow(String ID)
	{
	
	/*
		when User is asled to follow some other user,
		first programm has to convert string into 
		User/observer object. I did it so that 
		user will ask admin to get the correct user
		(user will never access the entier tree)
		and if there is no such user, admin returns null
		if the user exsist, this user asks to become 
		a follower
	*/
		User toFollow = admin.getUser(ID); // ask admin to find the userer based on the string
		if(toFollow == null)
		{
			System.out.println("USER " + this.ID + ": User " + ID + " does not exsist.");
		}
		else
		{
			toFollow.addFollower(this);// ask to become a follower
			following.addElement(toFollow);// add user beeing followed to GUI 
		}
	}

	
	public void post(String tweet)
	{
		posts.add(tweet);// add tweet to the list of all tweets end by this user (used for stats)
		feed.addElement(this.toString() + ": " + tweet);
		LastUpdate = System.currentTimeMillis();
		gui.updateLastUpdateTime();
		toFollowers.notify(this, tweet);// using observer pattern to notify all followers
	}
	
	// someone whom this users is followinf has tweet
	// and this method is used to notify this user
	public void update(String post)
	{
		feed.addElement(post);
		LastUpdate = System.currentTimeMillis();
		gui.updateLastUpdateTime();
	}
	
	// getting list of people this user is following
	// used for GUI
	public DefaultListModel<User> getFollowing()
	{
		return following;
	}
	
	// getters for time stuff
	public long getTimeCreated()
	{
		return TimeCreated;
	}
	
	public long getLastUpdateTime()
	{
		return LastUpdate;
	}
	
	// when time has been updated, user have to
	// update the gui, so it needs an instance of gui
	public void setUserGUI(UserView GUI)
	{
		gui = GUI;
	}
	
	// some other user asked this user to 
	// become their follower. This method adds
	// otheruser to observer list.
	public void addFollower(User follower)
	{
		toFollowers.addObserver(follower);
	}
	
	// this method accepts Admin visit.
	// Used for stats
	public void accept(AdminVisitor vis)
	{
		vis.visitUser(ID, posts, LastUpdate);
	}
	
	// getting the feed of this user
	// used for GUI
	public DefaultListModel<String> getFeed()
	{
		return feed;
	}
	
	public String toString()
	{
		return ID;
	}
}