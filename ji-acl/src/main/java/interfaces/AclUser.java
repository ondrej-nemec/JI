package interfaces;

import java.util.List;

public interface AclUser<U, R> {

	public U getId();
	
	public int getRank();
	
	public List<AclRole<R>> getRoles();
	
}
