package ji.querybuilder.instances;

import ji.querybuilder.DbInstance;
import ji.querybuilder.builder_impl.AlterTableBuilderImpl;
import ji.querybuilder.builder_impl.AlterViewBuilderImpl;
import ji.querybuilder.builder_impl.CreateIndexBuilderImpl;
import ji.querybuilder.builder_impl.CreateTableBuilderImpl;
import ji.querybuilder.builder_impl.CreateViewBuilderImpl;
import ji.querybuilder.builder_impl.DeleteBuilderImpl;
import ji.querybuilder.builder_impl.DeleteIndexBuilderImpl;
import ji.querybuilder.builder_impl.DeleteTableBuilderImpl;
import ji.querybuilder.builder_impl.DeleteViewBuilderImpl;
import ji.querybuilder.builder_impl.InsertBuilderImpl;
import ji.querybuilder.builder_impl.MultipleSelectBuilderImpl;
import ji.querybuilder.builder_impl.SelectBuilderImpl;
import ji.querybuilder.builder_impl.UpdateBuilderImpl;
import ji.querybuilder.enums.ColumnType;

public class PostgreSqlQueryBuilder implements DbInstance {
	
	protected String toString(ColumnType type) {
		return type.getType().toString(); // TODO
	}

	@Override
	public String concat(String param, String... params) {
		StringBuilder builder = new StringBuilder("CONCAT(");
		builder.append(param);
		for (String p : params) {
			builder.append(", ");
			builder.append(p);
		}
		builder.append(")");
		return builder.toString();
	}

	@Override
	public String cast(String param, ColumnType type) {
		return String.format("CAST(%s AS %s)", param, toString(type));
	}

	@Override
	public String groupConcat(String param, String delimeter) {
		return String.format("STRING_AGG(%s, '%s')", param, delimeter);
	}
	
	@Override
	public String max(String param) {
		return String.format("MAX(%s)", param);
	}
	
	@Override
	public String min(String param) {
		return String.format("MIN(%s)", param);
	}
	
	@Override
	public String avg(String param) {
		return String.format("AVG(%s)", param);
	}
	
	@Override
	public String sum(String param) {
		return String.format("SUM(%s)", param);
	}
	
	@Override
	public String count(String param) {
		return String.format("COUNT(%s)", param);
	}
	
	@Override
	public String lower(String param) {
		return String.format("LOWER(%s)", param);
	}
	
	@Override
	public String upper(String param) {
		return String.format("UPPER(%s)", param);
	}
	
	/*************/

	@Override
	public String createSql(DeleteIndexBuilderImpl deleteIndex) {
		return "DROP INDEX " + deleteIndex.getIndexName();
	}

	@Override
	public String createSql(CreateIndexBuilderImpl createIndex) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE INDEX ");
		sql.append(createIndex.getIndexName());
		sql.append(" ON ");
		sql.append(createIndex.getTable());
		sql.append("(");
		String[] columns = createIndex.getColumns();
		for (int i = 0; i < columns.length; i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append(columns[i]);
		}
		sql.append(")");
		return sql.toString();
	}

	@Override
	public String createSql(DeleteTableBuilderImpl deleteTable) {
		return "DROP TABLE " + deleteTable.getTable();
	}

	@Override
	public String createSql(DeleteViewBuilderImpl deleteView) {
		return "DROP VIEW " + deleteView.getView();
	}

	@Override
	public String createSql(InsertBuilderImpl insert) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(UpdateBuilderImpl updateBuilder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(DeleteBuilderImpl delete) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(SelectBuilderImpl select) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(MultipleSelectBuilderImpl multipleSelect) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(CreateViewBuilderImpl createView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(AlterViewBuilderImpl alterView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(CreateTableBuilderImpl createTable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createSql(AlterTableBuilderImpl alterTable) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
