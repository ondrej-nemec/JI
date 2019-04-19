package interfaces;

public interface AclRole {

	public String getId();
	
	public int getRank();
	
	public boolean equals(AclRole role);
}
