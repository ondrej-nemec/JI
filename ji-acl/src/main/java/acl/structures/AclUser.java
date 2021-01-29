package acl.structures;

import java.util.List;

public interface AclUser {

	public Object getId();
	
	public int getRank();
	
	public List<AclRole> getRoles();
	
}
