import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode; 
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UserView implements ActionListener {
	
	JFrame frame;
	Admin admin;
	User coreUser;
	JList<User> followers;
	JButton follow;
	JTextField userName;
	JTextField PostField;
	JButton postBtn;
	JList feed;
	JLabel lastUpdate;
	
	// initializing variables
	// users is passed from the top
	protected UserView(User user){
		admin = AdminSingleton.getInstance();
		coreUser = user;
		coreUser.setUserGUI(this);
		constructFrame();
	}
	
	/*
		Messy GUI. This gui is split in 4 panels.
		Panel one is follow text field/button.
		Panel two is list everyone this user
		follows.
		Pannel three is post text field/button.
		Pannel four is feed list
	*/
	private void constructFrame()
	{
		frame = new JFrame();
		frame.setTitle(coreUser.toString());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		
		Border b = BorderFactory.createLineBorder(new Color(100, 100, 200), 3);

		
		JPanel addfollowingPanel = new JPanel();
		addfollowingPanel.setBounds(3, 3, 470, 37);
		addfollowingPanel.setLayout(null);
		addfollowingPanel.setBorder(b);
		
		userName = new JTextField();
		userName.setBounds(6, 6, 105,25);
		addfollowingPanel.add(userName);
		
		follow = new JButton();
		follow.setBounds(115, 6, 110,25);
		follow.addActionListener(this);
		follow.setText("Follow");
		addfollowingPanel.add(follow);
		
		
		JPanel followingPanel = new JPanel();
		followingPanel.setBounds(3, 43, 470, 170);
		followingPanel.setLayout(null);
		followingPanel.setBorder(b);
		
		followers = new JList<>(coreUser.getFollowing());
		followers.setBounds(3, 3, 463, 163);
		followingPanel.add(followers);
		
		JPanel postPanel = new JPanel();
		postPanel.setBounds(3, 220, 470, 37);
		postPanel.setLayout(null);
		postPanel.setBorder(b);
		
		PostField = new JTextField();
		PostField.setBounds(6, 6, 275,25);
		postPanel.add(PostField);
		
		postBtn = new JButton();
		postBtn.setBounds(285, 6, 110,25);
		postBtn.addActionListener(this);
		postBtn.setText("Post");
		postPanel.add(postBtn);
		
		JPanel feedPanel = new JPanel();
		feedPanel.setBounds(3, 266, 470, 170);
		feedPanel.setLayout(null);
		feedPanel.setBorder(b);
		
		feed = new JList<>(coreUser.getFeed());
		feed.setBounds(3, 3, 463, 163);
		feedPanel.add(feed);
		
		JPanel datePannel = new JPanel();
		datePannel.setBounds(3, 440, 470, 33);
		datePannel.setLayout(null);
		datePannel.setBorder(b);
		
		JLabel timeCreated = new JLabel(("Creation Time: " + coreUser.getTimeCreated()) + "");
		timeCreated.setBounds(6, 6, 250,25);
		datePannel.add(timeCreated);
		
		lastUpdate = new JLabel(("Last Update: " + coreUser.getLastUpdateTime()) + "");
		lastUpdate.setBounds(245, 6, 250,25);
		datePannel.add(lastUpdate);
		
		frame.add(datePannel);
		frame.add(postPanel);
		frame.add(addfollowingPanel);
		frame.add(followingPanel);
		frame.add(feedPanel);
		
		
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
	// handels button presses
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == follow)
		{
			coreUser.follow(userName.getText());
			userName.setText("");
		}
		
		if(e.getSource() == postBtn)
		{
			coreUser.post(PostField.getText());
			PostField.setText("");
		}
	}
	
	public void updateLastUpdateTime()
	{
		lastUpdate.setText(("Last Update: " + coreUser.getLastUpdateTime()) + "");
	}
}