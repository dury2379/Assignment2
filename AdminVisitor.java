import java.util.ArrayList;
import java.util.List;

public interface AdminVisitor {
	public void visitUser(String name, ArrayList<String> tweets, long lastUpdatedTime);
	public void visitGroup();
}