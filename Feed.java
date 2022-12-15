import java.util.ArrayList;
import java.util.List;

// Every User has their own set of followers
// so programm suppose to notify diffren users
// depending on who is posting. That means,
// every user hava to have their own 
// "to be notifyed" list. This is just a 
// wrapper for handeling all observers.

public class Feed
{
	private List<PostObserver> observers;
	
	public Feed()			
	{
	// initialise the list
		observers = new ArrayList<PostObserver>();
	}
	// add/remove observers follow/unfollow
	public void addObserver(PostObserver obs)
	{
		observers.add(obs);
	}
	
	public void removeObserver(PostObserver obs)
	{
		observers.remove(obs);
	}
	
	
	// This is used for showing following list in User View
	public List<PostObserver> getObservers()
	{
		return observers;
	}
	
	// notify all followers when this user tweets.
	public void notify(PostObserver poster, String Post)
	{
		for (PostObserver obs : observers) {
			obs.update(poster.toString() + ": " + Post);
		}
	}
}