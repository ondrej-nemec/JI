package interfaces;

import helper.Rules;

public interface RulesDao {
	
	Rules getRulesForUser(AclUser user, AclDestination domain);
	
}
