package interfaces;

import java.util.List;

public interface AclUser {

	public String getId();
	
	public int getRank();
	
	public List<AclRole> getRoles();
	
	public boolean equals(AclUser user);
	
}
