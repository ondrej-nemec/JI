package ji.querybuilder.instances;

import java.util.List;
import java.util.function.Function;

import ji.common.functions.Implode;
import ji.common.structures.ObjectBuilder;
import ji.common.structures.Tuple2;
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
import ji.querybuilder.builder_impl.share.SelectImpl;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.Join;
import ji.querybuilder.enums.Where;
import ji.querybuilder.structures.Joining;
import ji.querybuilder.structures.SubSelect;

public class PostgreSqlQueryBuilder implements DbInstance {

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
	public String createSql(InsertBuilderImpl insert, boolean create) {
		StringBuilder sql = new StringBuilder();
		createWith(insert.getWiths(), sql, create);
		sql.append("INSERT INTO " + insert.getTable());
		if (insert.getValues().isEmpty()) {
			// insert from select
			sql.append("(");
			sql.append(Implode.implode(", ", insert.getColumns()));
			sql.append(")");
		} else {
			StringBuilder columns = new StringBuilder();
			StringBuilder values = new StringBuilder();
			insert.getValues().forEach((column, value)->{
				if (!columns.toString().isEmpty()) {
					columns.append(", ");
					values.append(", ");
				}
				columns.append(column);
				values.append(value);
			});
			sql.append(columns);
			sql.append(values);
		}
		return sql.toString();
	}

	@Override
	public String createSql(UpdateBuilderImpl updateBuilder, boolean create) {
		StringBuilder sql = new StringBuilder();
		createWith(updateBuilder.getWiths(), sql, create);
		sql.append("UPDATE ");
		sql.append(getWithAlias(updateBuilder.getTable(), updateBuilder.getAlias()));
		ObjectBuilder<Boolean> firstSet = new ObjectBuilder<>(true);
		updateBuilder.getSets().forEach(set->{
			if (firstSet.get()) {
				firstSet.set(false);
				sql.append(" SET ");
			} else {
				sql.append(", ");
			}
			sql.append(set);
		});
		ObjectBuilder<Boolean> firstJoin = new ObjectBuilder<>(true);
		updateBuilder.getJoins().forEach(join->{
			if (firstJoin.get()) {
				firstJoin.set(false);
				sql.append(" FROM ");
				sql.append(getWithAlias(
					create ? join.getBuilder().createSql() : join.getBuilder().getSql(),
					join.getAlias()
				));
			} else {
				createJoin(join, sql, create);
			}
		});
		createWhere(updateBuilder.getWheres(), sql, create);
		return sql.toString();
	}

	@Override
	public String createSql(DeleteBuilderImpl delete, boolean create) {
		StringBuilder sql = new StringBuilder();
		createWith(delete.getWiths(), sql, create);
		sql.append("DELETE FROM " + delete.getTable());
		
		StringBuilder joins = new StringBuilder();
		StringBuilder wheres = new StringBuilder();
		delete.getJoins().forEach(join->{
			if (joins.isEmpty()) {
				joins.append(" USING ");
				wheres.append(" WHERE");
			} else {
				joins.append(", ");
				wheres.append(" AND");
			}
			joins.append(getWithAlias(
				create ? join.getBuilder().createSql() : join.getBuilder().getSql(),
				join.getAlias()
			));
			wheres.append(" " + join.getOn());
		});
		delete.getWheres().forEach((where)->{
			if (wheres.isEmpty()) {
				wheres.append(" WHERE ");
			} else {
				wheres.append(" " + where._2().toString() + " ");
			}
			wheres.append("(" + where._1() + ")");
		});
		sql.append(joins.toString());
		sql.append(wheres.toString());
		return sql.toString();
	}

	@Override
	public String createSql(SelectBuilderImpl select, boolean create) {
		StringBuilder sql = new StringBuilder();
		createWith(select.getWiths(), sql, create);
		createPlainSelect(select, sql, create);
		return sql.toString();
	}

	@Override
	public String createSql(MultipleSelectBuilderImpl multipleSelect, boolean create) {
		StringBuilder sql = new StringBuilder();
		iterateList(
			sql, multipleSelect.getSelects(),
			i->"", i->" " + i._2().toString() + " ",  i->create ? i._1().createSql() : i._1().getSql()
		);
		iterateList(
			sql, multipleSelect.getOrderBy(),
			i->" ORDER BY  ", i->", ", i->i
		);
		return sql.toString();
	}

	@Override
	public String createSql(CreateViewBuilderImpl createView, boolean create) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE VIEW " + createView.getView() + " AS ");
		createPlainSelect(createView, sql, create);
		return sql.toString();
	}

	@Override
	public String createSql(AlterViewBuilderImpl alterView, boolean create) {
		StringBuilder sql = new StringBuilder();
		sql.append("DROP VIEW " + alterView.getView() + "; ");
		sql.append("CREATE VIEW " + alterView.getView() + " AS ");
		createPlainSelect(alterView, sql, create);
		return sql.toString();
	}

	@Override
	public String createSql(CreateTableBuilderImpl createTable) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE ");
		sql.append(createTable.getTable());
		sql.append(" (");
		// TODO Auto-generated method stub
		
		sql.append(")");
		return sql.toString();
	}

	@Override
	public String createSql(AlterTableBuilderImpl alterTable) {
		StringBuilder sql = new StringBuilder();
		// TODO Auto-generated method stub
		return sql.toString();
	}
	
	/****************************/

	// TODO test
	protected String toString(ColumnType type) {
		switch (type.getType()) {
			case STRING:
				return String.format("VARCHAR(%s)", type.getSize());
			case CHAR:
				return String.format("CHAR(%s)", type.getSize());
			case DATETIME: return "TIMESTAMP";
			default: return type.getType().toString();
		}
	}

	// TODO test
	protected String toString(Join join) {
		switch(join) {
			case FULL_OUTER_JOIN: throw new RuntimeException("Full Outer Join is not supported by mysql");
			case INNER_JOIN: return "JOIN";
			case LEFT_OUTER_JOIN: return "LEFT JOIN";
			case RIGHT_OUTER_JOIN: return "RIGHT JOIN";
			default: throw new RuntimeException("Not implemented join: " + join);
		}
	}

	/****************************/
	
	private <T> void iterateList(
			StringBuilder sql, List<T> list,
			Function<T, String> onFirst, Function<T, String> onOthers, Function<T, String> otherwise) {
		ObjectBuilder<Boolean> first = new ObjectBuilder<>(true);
		list.forEach(item->{
			if (first.get()) {
				first.set(false);
				sql.append(onFirst.apply(item));
			} else {
				sql.append(onOthers.apply(item));
			}
			sql.append(otherwise.apply(item));
		});
	}
	
	private String getWithAlias(String table, String alias) {
		StringBuilder result = new StringBuilder();
		result.append(table);
		if (alias != null) {
			result.append(" AS " + alias);
		}
		return result.toString();
	}
	
	private void createPlainSelect(SelectImpl<?> builder, StringBuilder sql, boolean create) {
		iterateList(
			sql, builder.getSelects(),
			i->"SELECT ", i->", ", i->i
		);
		if (builder.getFrom() != null) {
			sql.append(" FROM ");
			sql.append(getWithAlias(
				String.format(
					builder.getFrom()._1().wrap() ? "(%s)" : "%s",
					create ? builder.getFrom()._1().createSql() : builder.getFrom()._1().getSql()
				),
				builder.getFrom()._2()
			));
		}
		builder.getJoins().forEach(join->{
			createJoin(join, sql, create);
		});
		createWhere(builder.getWheres(), sql, create);
		iterateList(
			sql, builder.getGroupBy(),
			i->" GROUP BY  ", i->", ", i->i
		);
		iterateList(
			sql, builder.getHaving(),
			i->" HAVING  ", i->", ", i->i
		);
		iterateList(
			sql, builder.getOrderBy(),
			i->" ORDER BY  ", i->", ", i->i
		);
		if (builder.getLimit() != null) {
			sql.append(" LIMIT " + builder.getLimit());
		}
		if (builder.getOffset() != null) {
			sql.append(" OFFSET " + builder.getOffset());
		}
	}
	
	private void createWith(List<Tuple2<String, SubSelect>> withs, StringBuilder sql, boolean create) {
		withs.forEach((with)->{
			sql.append(String.format(
				"WITH %s AS (%s)",
				with._1(),
				create ? with._2().createSql() : with._2().getSql()
			));
		});
	}
	
	private void createWhere(List<Tuple2<String, Where>> wheres, StringBuilder sql, boolean create) {
		iterateList(
			sql, wheres,
			w->" WHERE ", w->" " + w._2().toString() + " ", w->"(" + w._1() + ")"
		);
	}
	
	private void createJoin(Joining join, StringBuilder sql, boolean create) {
		sql.append(" ");
		sql.append(toString(join.getJoin()));
		sql.append(" ");
		sql.append(getWithAlias(
			String.format(
				join.getBuilder().wrap() ? "(%s)" : "%s",
				create ? join.getBuilder().createSql() : join.getBuilder().getSql()
			),
			join.getAlias()
		));
		sql.append(" ON " + join.getOn());
	}
	
}
