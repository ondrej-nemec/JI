package helper.implementations;

import java.util.List;

import interfaces.AclRole;
import interfaces.AclUser;

public class TestUser implements AclUser{

	private String id;
	
	private int rank;
	
	private List<AclRole> roles;
	
	public TestUser(String id, int rank, List<AclRole> roles) {
		this.id = id;
		this.rank = rank;
		this.roles = roles;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public List<AclRole> getRoles() {
		return roles;
	}

	@Override
	public boolean equals(AclUser user) {
		return id.equals(user.getId());
	}
	@Override
	public String toString() {
		return id;
	}
}
