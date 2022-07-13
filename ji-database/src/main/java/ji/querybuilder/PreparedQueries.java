package ji.querybuilder;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import ji.common.structures.DictionaryValue;
import ji.common.structures.ThrowingBiConsumer;
import ji.common.structures.ThrowingConsumer;
import ji.common.structures.ThrowingSupplier;
import ji.database.support.DatabaseRow;
import ji.querybuilder.builders.InsertBuilder;
import ji.querybuilder.builders.SelectBuilder;
import ji.querybuilder.builders.UpdateBuilder;

public interface PreparedQueries {
	
	default String[] _createSelect(String... select) {
		if (select == null) {
			return new String[] {"*"};
		}
		return select;
	}
	
	/******/
	
	default DatabaseRow get(QueryBuilder builder, String table, String... select) throws SQLException {
		return builder.select(_createSelect(select)).from(table).fetchRow();
	}
	
	/******/
	
	default List<DatabaseRow> getAll(QueryBuilder builder, String table, String... select) throws SQLException {
		return getAll(builder, table, r->r, select);
	}
	
	default <T> List<T> getAll(QueryBuilder builder, String table, Function<DatabaseRow, T> create, String... select) throws SQLException {
		return _getAll(builder, table, select).fetchAll(create);
	}
	
	default SelectBuilder _getAll(QueryBuilder builder, String table, String... select) {
		 return builder.select(_createSelect(select)).from(table);
	}
	
	/******/
	
	default List<DatabaseRow> getAllBy(QueryBuilder builder, String table, String idName, Object id, String... select) throws SQLException {
		return getAllBy(builder, table, idName, id, r->r, select);
	}
	
	default <T> List<T> getAllBy(QueryBuilder builder, String table, String idName, Object id, Function<DatabaseRow, T> create, String... select) throws SQLException {
		return builder.select(_createSelect(select)).from(table)
			.where(idName + " = :id").addParameter(":id", id)
			.fetchAll(create);
	}
	
	/******/
	
	default List<DatabaseRow> getAllIn(QueryBuilder builder, String table, String idName, Collection<Object> ids, String... select) throws SQLException {
		return getAllIn(builder, table, idName, ids, r->r, select);
	}
	
	default <T> List<T> getAllIn(QueryBuilder builder, String table, String idName, Collection<Object> ids, Function<DatabaseRow, T> create, String... select) throws SQLException {
		return builder.select(_createSelect(select)).from(table)
				.where(idName + " in (:id)").addParameter(":id", ids)
				.fetchAll(create);
	}
	
	/******/
	
	default int delete(QueryBuilder builder, String table, String idName, Object id) throws SQLException {
		return builder.delete(table).where(idName + " = :id").addParameter(":id", id).execute();
	}
	
	default int update(QueryBuilder builder, String table, String idName, Map<String, Object> data, Object id) throws SQLException {
		UpdateBuilder b = builder.update(table);
		data.forEach((name, value)->{
			b.set(String.format("%s = :%s", name, name)).addParameter(":" + name, value);
		});
		b.where(idName + " = :id").addParameter(":id", id);
		return b.execute();
	}
	
	default DictionaryValue insert(QueryBuilder builder, String table, Map<String, Object> data) throws SQLException {
		InsertBuilder b = builder.insert(table);
		data.forEach((name, value)->{
			b.addValue(name, value);
		});
		return b.execute();
	}
	
	default <T> void saveRelation(
			QueryBuilder builder, String relationTable,
			String primaryKeyName, String foreignKeyName,
			Object id, List<DatabaseRow> data) throws SQLException {
		saveRelation(
			data,
			row->row.getValue(primaryKeyName),
			()->getAllBy(builder, relationTable, foreignKeyName, id, primaryKeyName),
			row->delete(builder, relationTable, primaryKeyName, row.getValue(primaryKeyName)),
			(origin, newOne)->
				update(builder, relationTable, primaryKeyName, newOne.toMap(), origin.getValue(primaryKeyName)),
			row->{
				row.put(foreignKeyName, id);
				insert(builder, relationTable, row.toMap());
			}
		);
	}
	
	default <T> void saveRelation(List<T> data, Function<T, Object> createId,
			ThrowingSupplier<List<T>, SQLException> select,
			ThrowingConsumer<T, SQLException> delete,
			ThrowingBiConsumer<T, T, SQLException> update,
			ThrowingConsumer<T, SQLException> insert) throws SQLException {
		Map<Object, T> loaded = new HashMap<>();
		for (T t : data) {
			loaded.put(createId.apply(t), t);
		}
		List<T> database = select.get();
		for (T origin : database) {
			if (!loaded.containsKey(createId.apply(origin))) {
				delete.accept(origin);
			} else {
				T l = loaded.remove(createId.apply(origin));
				if (!l.equals(origin)) {
					update.accept(origin, l);
				}
			}
		}
		for (Entry<Object, T> toInsert : loaded.entrySet()) {
			insert.accept(toInsert.getValue());
		}
	}
	
}
