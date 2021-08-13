package query.wrappers;

import query.executors.SingleExecute;

public interface UpdateBuilder extends SingleExecute<UpdateBuilder> {
	
	UpdateBuilder set(String update);
	
	UpdateBuilder where(String where);
	
	UpdateBuilder andWhere(String where);
	
	UpdateBuilder orWhere(String where);

}
