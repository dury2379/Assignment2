

class Group implements ItemElement{
	private String ID;
	
	// constructor only initializes ID
	public Group(String ID)
	{
		this.ID = ID;
	}
	
	// accept admin visit
	public void accept(AdminVisitor vis)
	{
		vis.visitGroup();
	}
	
	public String toString()
	{
		return ID;
	}
}