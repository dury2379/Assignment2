

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode; 

// singleton pattern for Admin

public class AdminSingleton extends Admin {

	protected static Admin instance;

	public static Admin getInstance()
	{
		if(instance == null)
		{
			instance = new Admin();
		}
		return instance;
	}

	
}