package helper.implementations;

import interfaces.AclDestination;

public class TestDestination implements AclDestination {

	private String id;
	
	public TestDestination(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean equals(AclDestination destination) {
		return id.equals(destination.getId());
	}

	@Override
	public String toString() {
		return id;
	}
}
