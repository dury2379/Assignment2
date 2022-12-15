
import java.util.function.Supplier;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode; 
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AdminView implements ActionListener {
	
	JFrame frame;				// initializing layout elements
	Admin admin;
	JTree tree;
	JButton addUser;
	JTextField userName;
	JButton addGroup;
	JTextField groupName;
	JButton userView;
	JButton userTotal;
	JButton groupTotal;
	JButton messageTotal;
	JButton positiveFraction;
	JButton checkIDs;
	JLabel checkResult;
	JButton lastUpdate;
	JLabel lastUpdatedUser;
	JTextField statOutput;
	
	Map<JButton, Supplier<Integer>> buttonProcesser;  // this is the HashSet map that will be used to map all buttons to actions
	
	protected AdminView(){							// just inintialize variables and create the GUI
		admin = AdminSingleton.getInstance();
		buttonProcesser = new HashMap<>();
		constructFrame();
	}
	
	private void constructFrame()
	{
	
	/*
		This sections is messy because it is alot of GUI stuff.
		This section boils doen to his:
		There is # panels: Tree panel, Add panel, and Stat panel.
		Tree panel is a panel designated for the JTree.
		Add panel designated for add user/group text field/button, 
		and open user view button.
		Stat panel contains 4 stat buttons (total users/groups/messages
		and precentage of happy messages) and a text field that displays 
		the number.
		Also, every button is bindede to HashMap, so later there is no
		if-statment hell.
	*/
		frame = new JFrame();
		frame.setTitle("Admin");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		
		Border b = BorderFactory.createLineBorder(new Color(100, 100, 200), 3);
		
		JPanel treePanel = new JPanel();
		treePanel.setLayout(new BoxLayout(treePanel, BoxLayout.Y_AXIS));
		treePanel.setBorder(b);
		treePanel.setBounds(3, 3, 244, 470);
		
		tree = admin.getTree();
		tree.setAlignmentX(Component.LEFT_ALIGNMENT);
		treePanel.add(tree);
		
		
		JPanel addPanel = new JPanel();
		addPanel.setBorder(b);
		addPanel.setBounds(253, 3, 230, 130);
		addPanel.setLayout(null);
		addUser = new JButton();
		addUser.setBounds(115, 6, 110,25);
		addUser.addActionListener(this);
		addUser.setText("Add User");
		buttonProcesser.put(addUser, this::processAddUser);
		addPanel.add(addUser);
		
		userName = new JTextField();
		userName.setBounds(6, 6, 105,25);
		addPanel.add(userName);
		
		addGroup = new JButton();
		addGroup.setBounds(115, 37, 110,25);
		addGroup.addActionListener(this);
		addGroup.setText("Add Group");
		buttonProcesser.put(addGroup, this::processAddGroup);
		addPanel.add(addGroup);
		
		userView = new JButton();
		userView.setBounds(6, 68, 220,25);
		userView.addActionListener(this);
		userView.setText("Open User View");
		buttonProcesser.put(userView, this::processOpenUserView);
		addPanel.add(userView);
		
		groupName = new JTextField();
		groupName.setBounds(6, 37, 105,25);
		addPanel.add(groupName);
		
		
		
		JPanel statPanel = new JPanel();
		statPanel.setBorder(b);
		//                   x    y    w    h
		statPanel.setBounds(253, 150, 230, 290);
		statPanel.setLayout(null);
		
		userTotal = new JButton();
		userTotal.setBounds(6, 6, 220,25);
		userTotal.addActionListener(this);
		userTotal.setText("Total Users");
		buttonProcesser.put(userTotal, this::processUserTotal);
		statPanel.add(userTotal);
		
		groupTotal = new JButton();
		groupTotal.setBounds(6, 37, 220,25);
		groupTotal.addActionListener(this);
		groupTotal.setText("Total Groups");
		buttonProcesser.put(groupTotal, this::processGroupTotal);
		statPanel.add(groupTotal);
		
		messageTotal = new JButton();
		messageTotal.setBounds(6, 68, 220,25);
		messageTotal.addActionListener(this);
		messageTotal.setText("Total Messages");
		buttonProcesser.put(messageTotal, this::processMessageTotal);
		statPanel.add(messageTotal);
		
		positiveFraction = new JButton();
		positiveFraction.setBounds(6, 99, 220,25);
		positiveFraction.addActionListener(this);
		positiveFraction.setText("Positive Precentage");
		buttonProcesser.put(positiveFraction, this::processPositivityFraction);
		statPanel.add(positiveFraction);
		
		statOutput = new JTextField();
		statOutput.setBounds(6, 130, 220,25);
		statOutput.setHorizontalAlignment(JTextField.CENTER);
		statOutput.setEditable(false);
		statPanel.add(statOutput);
		
		checkIDs = new JButton();
		checkIDs.setBounds(6, 161, 220,25);
		checkIDs.addActionListener(this);
		checkIDs.setText("Check IDs");
		buttonProcesser.put(checkIDs, this::processIDCheck);
		statPanel.add(checkIDs);
		
		checkResult = new JLabel("");
		checkResult.setBounds(6, 192, 220,25);
		statPanel.add(checkResult);
		
		lastUpdate = new JButton();
		lastUpdate.setBounds(6, 223, 220,25);
		lastUpdate.addActionListener(this);
		lastUpdate.setText("Last Updated");
		buttonProcesser.put(lastUpdate, this::processLastUpdatedUser);
		statPanel.add(lastUpdate);
		
		lastUpdatedUser = new JLabel("");
		lastUpdatedUser.setBounds(6, 254, 220,25);
		statPanel.add(lastUpdatedUser);
		
		
		frame.add(statPanel);
		frame.add(addPanel);
		frame.add(treePanel);
		
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
	// this is the click listener. it is called every time
	// LMB is pressed. If whatever was pressed on is a 
	// part of the HashMap, it will call according method
		if(buttonProcesser.containsKey(e.getSource()))
		{
			buttonProcesser.get(e.getSource()).get();
		}
	}
	
	
	// here is all the methods that are mentioned above
	
	private int processAddUser()
	{
		admin.addUser(userName.getText());
		userName.setText("");
		return 1;
	}
	
	private int processAddGroup()
	{
		admin.addGroup(groupName.getText());
		groupName.setText("");
		return 1;
	}
	
	private int processOpenUserView()
	{
		admin.openUserView();
		statOutput.setText("" + admin.getNumOfUsers());
		return 1;
	}
	
	private int processUserTotal()
	{
		admin.updateStats();
		statOutput.setText("" + admin.getNumOfUsers());
		return 1;
	}
	
	private int processGroupTotal()
	{
		admin.updateStats();
		statOutput.setText("" + admin.getNumOfGroups());
		return 1;
	}
	
	private int processMessageTotal()
	{
		admin.updateStats();
		statOutput.setText("" + admin.getNumOfMessages());
		return 1;
	}
	
	private int processIDCheck()
	{
		if(admin.getNameCheckResult())
		{
			checkResult.setText("Passed!");
		}
		else
		{
			checkResult.setText("Failed!");
		}
		return 1;
	}
	
	private int processLastUpdatedUser()
	{
		admin.updateStats();
		lastUpdatedUser.setText(admin.getLastUpdatedUser());
		return 1;
	}
	
	private int processPositivityFraction()
	{
		admin.updateStats();
		if(admin.getNumOfMessages() == 0)
		{
			statOutput.setText("0.0");
		}
		else
		{
			statOutput.setText("" + ((admin.getHappyTweets() * 100.0) / (admin.getNumOfMessages() * 1.0)) + "%");
		}
		return 1;
	}

}