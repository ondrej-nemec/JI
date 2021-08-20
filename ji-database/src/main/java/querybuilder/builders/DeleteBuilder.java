package querybuilder.builders;

import querybuilder.executors.SingleExecute;

public interface DeleteBuilder extends SingleExecute<DeleteBuilder> {
	
	DeleteBuilder where(String where);
	
	DeleteBuilder andWhere(String where);
	
	DeleteBuilder orWhere(String where);

}
