package ji.querybuilder.builders;

import java.sql.SQLException;

import ji.querybuilder.Builder;
import ji.querybuilder.builders.share.PlainSelect;

public interface AlterViewBuilder extends Builder, PlainSelect<AlterViewBuilder> {
	
	int execute() throws SQLException;

}
