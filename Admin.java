

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode; 
import javax.swing.tree.DefaultTreeModel;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Admin implements AdminVisitor{
	
	private JTree UserTree;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel UserTreeModel;
	private int numOfUsers;
	private int numOfGroups;
	private int numOfMessages;
	private int happyTweets;
	private Hashtable<String,Integer> NameMap;
	private long lastUpdateTime;
	private String lastUpdatedUser;
	
	protected Admin()
	{
	// Initializing variables.
	// creating Root node and setting stats variables
		root = new DefaultMutableTreeNode(new Group("Root"));
		UserTree = new JTree(root);
		UserTreeModel = (DefaultTreeModel)UserTree.getModel();
		numOfUsers = 0;
		numOfMessages = 0;
		numOfGroups = 1;
		happyTweets = 0;
	}
	
	// adding user. First programm get the selected 
	// node and checks if it is a folder or a user.
	// If it is a folder it adds a user.
	public void addUser(String ID)
	{
	// check if selected paths is valid
		if(UserTree.getSelectionPath() == null || !((DefaultMutableTreeNode)UserTree.getSelectionPath().getLastPathComponent()).getAllowsChildren())
		{
			System.out.println("ADMIN: Please select a folder");
		}
		else
		{
		// gets selected group
			DefaultMutableTreeNode group = (DefaultMutableTreeNode) UserTree.getSelectionPath().getLastPathComponent();
		// adds new user
			group.add(new DefaultMutableTreeNode(new User(ID), false));
			UserTreeModel.reload();
			expandAllNodes(UserTree, 0, UserTree.getRowCount());
			System.out.println("ADMIN: User added. ID: " + ID);
		}
	}
	
	
	// same as addUser, but with a Group
	public void addGroup(String ID)
	{
		if(UserTree.getSelectionPath() == null || !((DefaultMutableTreeNode)UserTree.getSelectionPath().getLastPathComponent()).getAllowsChildren())
		{
			System.out.println("ADMIN: Please select a folder");
		}
		else
		{
			DefaultMutableTreeNode group = (DefaultMutableTreeNode) UserTree.getSelectionPath().getLastPathComponent();
			group.add(new DefaultMutableTreeNode(new Group(ID)));
			UserTreeModel.reload();
			expandAllNodes(UserTree, 0, UserTree.getRowCount());
			System.out.println("ADMIN: Group added. ID: " + ID);
			
		}
	}
	
	// this method intializes tree traversal to find a user that
	// some other user wants to follow.
	public User getUser(String ID)
	{
		User user = searchTree(root, ID);
		return user;
	}
	
	// recursive tree traversal
	private User searchTree(DefaultMutableTreeNode start, String ID)
	{
		// get number of children
		int childCount = start.getChildCount();
		User result;
		
		// check if this node is the node that programm is looking for
		if(start.getUserObject() instanceof User && ((User)start.getUserObject()).toString().equals(ID))
		{
			return (User)start.getUserObject();
		}
		else		
		{
		// If not, traverse all children
			for(int i = 0; i < childCount; i++)
			{
				result = searchTree((DefaultMutableTreeNode)start.getChildAt(i), ID);
				if(result != null)
				{
					return result;
				}
			}
		}
		return null;

	}
	
	// before returning a stat data, programm updates
	// all stat.
	public void updateStats()
	{
		// set everithing to 0
		numOfUsers = 0;
		numOfMessages = 0;
		numOfGroups = 0;
		happyTweets = 0;
		lastUpdateTime = 0;
		// recuresive traversal to collect all data
		statTravers(root);
	}
	
	private void statTravers(DefaultMutableTreeNode start)
	{
		// visit and collect data
		((ItemElement)start.getUserObject()).accept(this);
		// visit all children
		int childCount = start.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			statTravers((DefaultMutableTreeNode)start.getChildAt(i));
		}
		
	}
	
	
	// initializes name check traverse.
	public boolean getNameCheckResult()
	{
		NameMap = new Hashtable<String,Integer>();
		return NameCheckTravers(root);
	}
	
	// traverses the tree to check all the names
	private boolean NameCheckTravers(DefaultMutableTreeNode start)
	{
		// Chech current node, if name check is not passed return false
		if(!checkName(((ItemElement)start.getUserObject()).toString()))
		{
			return false;
		}
		// Go through all children. If one of the children has invalid name return false
		int childCount = start.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			if(!NameCheckTravers((DefaultMutableTreeNode)start.getChildAt(i)))
			{
				return false;
			}
		}
		// if everything is clear return true
		return true;
	}
	
	private boolean checkName(String name)
	{
	    // return false if name has a space
		if(name.indexOf(" ") != -1)
		{
			return false;			
		}
		// return false if there is a duplicate
		if(NameMap.containsKey(name))
		{
			return false;
		}
		// add name to the list and return true
		NameMap.put(name, 0);
		return true;
		
	}
	
	// a User accepts Admins visit
	// here programm increments number
	// of users, and goes through all 
	// of the twiits to count all tweets
	// and find happy tweets.
	// also checks if this is the last uset that
	// was updated
	public void visitUser(String name, ArrayList<String> tweets, long lastUpdated)
	{
		if(lastUpdated > lastUpdateTime)
		{
			lastUpdateTime = lastUpdated;
			lastUpdatedUser = name;
		}
		numOfUsers++;
		countGood(tweets);
	}
	
	
	// only group related stat that programm
	// collects is a number of groups, so 
	// simply incrementing a variable is 
	// enough
	public void visitGroup()
	{
		numOfGroups++;
	}
	
	public void openUserView()
	{
		if(UserTree.getSelectionPath() == null || ((DefaultMutableTreeNode)UserTree.getSelectionPath().getLastPathComponent()).getAllowsChildren())
		{
			System.out.println("ADMIN: Please select a user");
		}
		else
		{
			User user = (User)(((DefaultMutableTreeNode)UserTree.getSelectionPath().getLastPathComponent()).getUserObject());
			UserView view = new UserView(user);
			
		}
	}
	
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
    	for(int i=startingIndex;i<rowCount;++i){
        	tree.expandRow(i);
    	}

    	if(tree.getRowCount()!=rowCount){
        	expandAllNodes(tree, rowCount, tree.getRowCount());
    	}
	}
	
	
	// this method counts habbpy tweets
	// for every tweet, this method tryes to find a 
	// match with a preset good words array
	// if it finds a match, number of happy tweets
	// goes up by one. It also counts all tweets.
	private void countGood(ArrayList<String> tweets)
	{
	
		// good words preset
		String[] goodWords = {"wonderful", "yes", "victory", "upbeat", "terrific", "smile", 
								"reward", "quality", "perfect", "okay", "nice", "motivating", "lovely", 
								"kind", "joy", "ideal", "happy", "good", "fantastic", "elegant", 
								"delightful", "charming", "brilliant", "awesome"};
		// search for all good words in all the tweets
		for(String t : tweets)
		{
			for(String gw : goodWords)
			{
				if(t.indexOf(gw) != -1)
				{
					happyTweets++;
					break;
				}
				
			}
			numOfMessages++;
		}
	}
	
	
	// getters for GUI
	public JTree getTree()
	{
		return UserTree;
	}
	
	public int getNumOfUsers()
	{
		return numOfUsers;
	}
	
	public int getNumOfMessages()
	{
		return numOfMessages;
	}
	
	public int getNumOfGroups()
	{
		return numOfGroups;
	}
	
	public int getHappyTweets()
	{
		return happyTweets;
	}
	
	public String getLastUpdatedUser()
	{
		return lastUpdatedUser;
	}
}