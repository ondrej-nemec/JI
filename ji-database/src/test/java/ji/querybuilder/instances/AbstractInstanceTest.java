package ji.querybuilder.instances;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.querybuilder.Builder;
import ji.querybuilder.DbInstance;
import ji.querybuilder.Functions;
import ji.querybuilder.QueryBuilder;
import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.Join;
import ji.querybuilder.enums.OnAction;
import ji.querybuilder.enums.Where;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public abstract class AbstractInstanceTest {
	
	private final DbInstance instance;
	
	public AbstractInstanceTest(DbInstance instance) {
		this.instance = instance;
	}
	
	// TODO test enumns to string 
	
	@Test
	@Parameters(method="dataFunctions")
	public void testFunctions(Function<Functions, String> create, String expected) {
		assertEquals(expected, create.apply(instance));
	}
	
	public Object[] dataFunctions() {
		return new Object[] {
			new Object[] {
				ff(f->f.concat("a", "b", "c")), getFunctions_concat()
			},
			new Object[] {
				ff(f->f.groupConcat("a", ";")), getFunctions_groupConcat()
			},
			new Object[] {
				ff(f->f.cast("a", ColumnType.floatType())), getFunctions_cast()
			},
			new Object[] {
				ff(f->f.max("a")), getFunctions_max()
			},
			new Object[] {
				ff(f->f.min("a")), getFunctions_min()
			},
			new Object[] {
				ff(f->f.avg("a")), getFunctions_avg()
			},
			new Object[] {
				ff(f->f.sum("a")), getFunctions_sum()
			},
			new Object[] {
				ff(f->f.count("a")), getFunctions_count()
			},
			new Object[] {
				ff(f->f.lower("a")), getFunctions_lower()
			},
			new Object[] {
				ff(f->f.upper("a")), getFunctions_upper()
			}
		};
	}
	
	protected abstract String getFunctions_concat();

	protected abstract String getFunctions_groupConcat();

	protected abstract String getFunctions_cast();

	protected abstract String getFunctions_max();

	protected abstract String getFunctions_min();

	protected abstract String getFunctions_avg();

	protected abstract String getFunctions_sum();

	protected abstract String getFunctions_count();

	protected abstract String getFunctions_lower();

	protected abstract String getFunctions_upper();

	/********* TABLE **************/
	
	@Test
	public void testCreateTable() {
		test(
			b->b.createTable("SomeTable")
			.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL, ColumnSetting.PRIMARY_KEY, ColumnSetting.AUTO_INCREMENT)
			.addColumn("Column2", ColumnType.integer(), 42, ColumnSetting.NULL, ColumnSetting.UNIQUE)
			.addForeignKey("Column3", "AnotherTable", "AnotherColumn")
			.addForeignKey("Column4", "DifferentTable", "DifferentColumn", OnAction.CASCADE, OnAction.NO_ACTION)
			.addForeignKey("Column5", "DifferentTable2", "DifferentColumn2", OnAction.RESTRICT, OnAction.SET_DEFAULT)
			.addForeignKey("Column6", "DifferentTable3", "DifferentColumn3", OnAction.SET_NULL, null)
			.setPrimaryKey("Column7", "Column8"),
			getCreateTable()
		);
	}

	protected abstract String getCreateTable();
	
	@Test
	public void testAlterTable() {
		test(
			b->b.alterTable("SomeTable")
			.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL)
			.addColumn("Column2", ColumnType.integer(), 42, ColumnSetting.NULL, ColumnSetting.UNIQUE)
			.addForeignKey("Column3", "AnotherTable", "AnotherColumn")
			.addForeignKey("Column4", "DifferentTable", "DifferentColumn", OnAction.CASCADE, OnAction.NO_ACTION)
			.deleteColumn("Column7")
			.deleteForeingKey("Column8")
			.modifyColumnType("Column9", ColumnType.floatType())
			.renameColumn("Column10", "Column11", ColumnType.integer()),
			getAlterTable()
		);
	}
	
	protected abstract String getAlterTable();

	@Test
	public void testDeleteTable() {
		test(b->b.deleteTable("SomeTable"), getDeleteTable());
	}
	
	protected abstract String getDeleteTable();
	
	/********* VIEW **************/

	@Test
	@Parameters(method="dataCreateView")
	public void testCreateView(Function<QueryBuilder, Builder> create, String getSql, String createSql) {
		test(create, getSql, createSql);
	}
	
	public Object[] dataCreateView() {
		return new Object[] {
			new Object[] {
				f(b->b
					.createView("SomeView")
					.select("A")
					.from("SomeTable")
				),
				getCreateView_fromString(false),
				getCreateView_fromString(true)
			},
			new Object[] {
				f(b->b
					.createView("SomeView")
					.select("A")
					.from("SomeTable", "a")
				),
				getCreateView_fromStringAlias(false),
				getCreateView_fromStringAlias(true)
			},
			new Object[] {
				f(b->b
					.createView("SomeView")
					.select("A")
					.from(
						b.select("1 AS A"),
						"a"
					)
				),
				getCreateView_fromSelect(false),
				getCreateView_fromSelect(true)
			},
			new Object[] {
				f(b->b
					.createView("SomeView")
					.select("A")
					.from(
						b.multiSelect(b.select("1 AS A"))
						.union(b.select("2 AS A")),
						"a"
					)
				),
				getCreateView_fromMultiSelect(false),
				getCreateView_fromMultiSelect(true)
			},
			new Object[] {
				f(b->b
					.createView("SomeView")
					
					.select("A")
					.select(f->f.count("B") + " as B")
					
					.from("SomeTable")
					
					.join("AnotherTable", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable2", "at2", Join.INNER_JOIN, "1 = 1")
					.join(b.select("A as C"), "subSelect", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable3", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join("AnotherTable4", "at4", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join(b.select("A as C"), "subSelect2", Join.INNER_JOIN, f->f.max("1 = 1"))
					
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
					
					.groupBy("Column")
					.groupBy("NextColumn")
					
					.having(":a = :b")
					.having(f->f.sum(":x") + " = 5")
					
					.orderBy("Column1")
					.orderBy(f->f.avg("Column2"))
					.limit(10, 15)
					.addParameter(":x", "XXXX")
					.addParameter(":a", "AAAA")
					.addParameter(":b", "BBBBB")
				),
				getCreateView(false),
				getCreateView(true)
			}
		};
	}
	
	protected abstract String getCreateView_fromString(boolean create);
	protected abstract String getCreateView_fromStringAlias(boolean create);
	protected abstract String getCreateView_fromSelect(boolean create);
	protected abstract String getCreateView_fromMultiSelect(boolean create);
	protected abstract String getCreateView(boolean create);

	@Test
	@Parameters(method="dataAlterView")
	public void testAlterView(Function<QueryBuilder, Builder> alter, String getSql, String createSql) {
		test(alter, getSql, createSql);
	}
	
	public Object[] dataAlterView() {
		return new Object[] {
			new Object[] {
				f(b->b
					.alterView("SomeView")
					.select("A")
					.from("SomeTable")
				),
				getAlterView_fromString(false),
				getAlterView_fromString(true)
			},
			new Object[] {
				f(b->b
					.alterView("SomeView")
					.select("A")
					.from("SomeTable", "a")
				),
				getAlterView_fromStringAlias(false),
				getAlterView_fromStringAlias(true)
			},
			new Object[] {
				f(b->b
					.alterView("SomeView")
					.select("A")
					.from(
						b.select("1 AS A"),
						"a"
					)
				),
				getAlterView_fromSelect(false),
				getAlterView_fromSelect(true)
			},
			new Object[] {
				f(b->b
					.alterView("SomeView")
					.select("A")
					.from(
						b.multiSelect(b.select("1 AS A"))
						.union(b.select("2 AS A")),
						"a"
					)
				),
				getAlterView_fromMultiSelect(false),
				getAlterView_fromMultiSelect(true)
			},
			new Object[] {
				f(b->b
					.alterView("SomeView")
					
					.select("A")
					.select(f->f.count("B") + " as B")
					
					.from("SomeTable")
					
					.join("AnotherTable", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable2", "at2", Join.INNER_JOIN, "1 = 1")
					.join(b.select("A as C"), "subSelect", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable3", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join("AnotherTable4", "at4", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join(b.select("A as C"), "subSelect2", Join.INNER_JOIN, f->f.max("1 = 1"))
					
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
					
					.having(":a = :b")
					.having(f->f.sum(":x") + " = 5")
					
					.groupBy("Column")
					.groupBy("NextColumn")
					
					.orderBy("Column1")
					.orderBy(f->f.avg("Column2"))
					.limit(10, 15)
					.addParameter(":x", "XXXX")
					.addParameter(":a", "AAAA")
					.addParameter(":b", "BBBBB")
				),
				getAlterView(false),
				getAlterView(true)
			}
		};
	}
	
	protected abstract String getAlterView_fromString(boolean create);
	protected abstract String getAlterView_fromStringAlias(boolean create);
	protected abstract String getAlterView_fromSelect(boolean create);
	protected abstract String getAlterView_fromMultiSelect(boolean create);
	protected abstract String getAlterView(boolean create);

	@Test
	public void testDeleteView() {
		test(b->b.deleteView("SomeView"), getDeleteView());
	}
	
	protected abstract String getDeleteView();
	
	/********* INDEX **************/

	@Test
	public void testCreateIndex() {
		test(b->b.createIndex("IndexName", "SomeTable", "Column1", "Column2"), getCreateIndex());
	}
	
	protected abstract String getCreateIndex();

	@Test
	public void testDeleteIndex() {
		test(b->b.deleteIndex("IndexName", "SomeTable"), getDeleteIndex());
	}
	
	protected abstract String getDeleteIndex();
	
	/********* QUERING **************/

	@Test
	@Parameters(method="dataQueryInsert")
	public void testQueryInsert(Function<QueryBuilder, Builder> alter, String getSql) {
		test(alter, getSql);
	}
	
	public Object[] dataQueryInsert() {
		return new Object[] {
			new Object[] {
				f(
					b->b.insert("SomeTable")
					.addValue("Column1", "123")
					.addValue("Column2", 123)
				),
				getQueryInsert()
			},
			new Object[] {
				f(
					b->b
					.with("cte", b.select("'A' as A"))
					.insert("SomeTable")
					.fromSelect(Arrays.asList("Col1", "Col2"), b.select("'B'", "123"))
				),
				getQueryInsertFromSelect()
			}
		};
	}
	
	protected abstract String getQueryInsert();
	
	protected abstract String getQueryInsertFromSelect();

	@Test
	@Parameters(method="dataQueryUpdate")
	public void testQueryUpdate(Function<QueryBuilder, Builder> alter, String getSql, String createSql) {
		test(alter, getSql, createSql);
	}
	
	public Object[] dataQueryUpdate() {
		return new Object[] {
			new Object[] {
				f(
					b->b.update("SomeTable")
					.set("Column1 = :value").addParameter(":value", 123)
					.set(f->"Column2 = " + f.avg("Column3"))
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryUpdateBasic(false),
				getQueryUpdateBasic(true)
			},
			new Object[] {
				f(
					b->b.update("SomeTable", "a")
					.set("Column1 = :value").addParameter(":value", 123)
					.set(f->"Column2 = " + f.avg("Column3"))
					
					.join("AnotherTable", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable2", "at2", Join.INNER_JOIN, "1 = 1")
					.join(b.select("A as C"), "subSelect", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable3", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join("AnotherTable4", "at4", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join(b.select("A as C"), "subSelect2", Join.INNER_JOIN, f->f.max("1 = 1"))
					
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryUpdateJoins(false),
				getQueryUpdateJoins(true)
			},
			new Object[] {
				f(
					b->b
					.with("withName", b.select("1=1"))
					.update("SomeTable")
					.set("Column1 = :value").addParameter(":value", 123)
					.set(f->"Column2 = " + f.avg("Column3"))
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryUpdateWith(false),
				getQueryUpdateWith(true)
			}
		};
	}
	
	protected abstract String getQueryUpdateBasic(boolean create);

	protected abstract String getQueryUpdateJoins(boolean create);
	
	protected abstract String getQueryUpdateWith(boolean create);

	@Test
	@Parameters(method="dataQueryDelete")
	public void testQueryDelete(Function<QueryBuilder, Builder> alter, String getSql, String createSql) {
		test(alter, getSql, createSql);
	}
	
	public Object[] dataQueryDelete() {
		return new Object[] {
			new Object[] {
				f(
					b->b.delete("SomeTable")
					.where("1= :id").addParameter(":id", 123)
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryDeleteBasic(false),
				getQueryDeleteBasic(true)
			},
			new Object[] {
				f(
					b->b.delete("SomeTable")
					.join("AnotherTable", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable2", "at2", Join.INNER_JOIN, "2 = 2")
					.join(b.select("A as B"), "subSelect", Join.INNER_JOIN, "3 = 3")
					.join("AnotherTable3", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join("AnotherTable4", "at4", Join.INNER_JOIN, f->f.max("2 = 2"))
					.join(b.select("A as C"), "subSelect2", Join.INNER_JOIN, f->f.max("3 = 3"))
					
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryDeleteJoins(false),
				getQueryDeleteJoins(true)
			},
			new Object[] {
				f(
					b->b
					.with("withName", b.select("1=1"))
					.delete("SomeTable")
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
				),
				getQueryDeleteWith(false),
				getQueryDeleteWith(true)
			}
		};
	}

	protected abstract String getQueryDeleteBasic(boolean create);

	protected abstract String getQueryDeleteJoins(boolean create);

	protected abstract String getQueryDeleteWith(boolean create);

	@Test
	@Parameters(method="dataQuerySelect")
	public void testQuerySelect(Function<QueryBuilder, Builder> alter, String getSql, String createSql) {
		test(alter, getSql, createSql);
	}
	
	public Object[] dataQuerySelect() {
		return new Object[] {
			new Object[] {
				f(b->b
					.select("A")
					.from("SomeTable")
				),
				getQuerySelect_fromString(false),
				getQuerySelect_fromString(true)
			},
			new Object[] {
				f(b->b
					.select("A")
					.from("SomeTable", "a")
				),
				getQuerySelect_fromStringAlias(false),
				getQuerySelect_fromStringAlias(true)
			},
			new Object[] {
				f(b->b
					.select("A")
					.from(
						b.select("1 AS A"),
						"a"
					)
				),
				getQuerySelect_fromSelect(false),
				getQuerySelect_fromSelect(true)
			},
			new Object[] {
				f(b->b
					.select("A")
					.from(
						b.multiSelect(b.select("1 AS A"))
						.union(b.select("2 AS A")),
						"a"
					)
				),
				getQuerySelect_fromMultiSelect(false),
				getQuerySelect_fromMultiSelect(true)
			},
			new Object[] {
				f(b->b
					.with("b", b.select("2=2"))
					.select("A")
					.from("SomeTable")
				),
				getQuerySelect_with(false),
				getQuerySelect_with(true)
			},
			new Object[] {
				// this is recursive
				f(b->b
					.with(
						"b",
						b.multiSelect(b.select("1 AS A"))
						.union(b.select("2 AS A").from("b"))
					)
					.select("A")
					.from("SomeTable")
				),
				getQuerySelect_withRecursive(false),
				getQuerySelect_withRecursive(true)
			},
			new Object[] {
				f(b->b
					.select("A")
					.select(f->f.count("B") + " as B")
					
					.from("SomeTable")
					
					.join("AnotherTable", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable2", "at2", Join.INNER_JOIN, "1 = 1")
					.join(b.select("A as C"), "subSelect", Join.INNER_JOIN, "1 = 1")
					.join("AnotherTable3", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join("AnotherTable4", "at4", Join.INNER_JOIN, f->f.max("1 = 1"))
					.join(b.select("A as C"), "subSelect2", Join.INNER_JOIN, f->f.max("1 = 1"))
					
					.where("1=2")
					.where("2=3", Where.OR)
					.where(f->f.lower("x") + " = y")
					.where(f->f.upper("z") + " = u", Where.AND)
					
					.having(":a = :b")
					.having(f->f.sum(":x") + " = 5")
					
					.groupBy("Column")
					.groupBy("NextColumn")
					
					.orderBy("Column1")
					.orderBy(f->f.avg("Column2"))
					.limit(10, 15)
					.addParameter(":x", "XXXX")
					.addParameter(":a", "AAAA")
					.addParameter(":b", "BBBBB")
				),
				getQuerySelect(false),
				getQuerySelect(true)
			}
		};
	}

	protected abstract String getQuerySelect_fromString(boolean create);

	protected abstract String getQuerySelect_fromStringAlias(boolean create);

	protected abstract String getQuerySelect_fromSelect(boolean create);
	
	protected abstract String getQuerySelect_with(boolean create);
	
	protected abstract String getQuerySelect_withRecursive(boolean create);

	protected abstract String getQuerySelect_fromMultiSelect(boolean create);

	protected abstract String getQuerySelect(boolean create);

	@Test
	public void testQueryMultipleSelect() {
		test(f(
			b->b
			.multiSelect(b.select("1=1"))
			.union(b.select("1=1"))
			.intersect(b.select("1=1"))
			.unionAll(b.select("1=1"))
			.except(b.select("1=1"))
			.orderBy("ColA, :id")
			.orderBy(f->f.count("ColB"))
			.addParameter(":id", 321)
		), getQueryMultipleSelect(false), getQueryMultipleSelect(true));
	}
	
	protected abstract String getQueryMultipleSelect(boolean create);

	/********* OTHER **************/

	@Test
	public void testBatch() {
		test(f(
			b->b
			.batch()
			.addBatch(b.select("1=1"))
			.addBatch(b.select("a=:id"))
			.addParameter(":id", 123)
		), getBatch(false), getBatch(true));
	}
	
	protected abstract String getBatch(boolean create);

	/*********************/
	
	private void test(Function<QueryBuilder, Builder> create, String expected) {
		test(create, expected, expected);
	}
	
	private void test(Function<QueryBuilder, Builder> create, String expectedGet, String expectedCreate) {
		QueryBuilder queryBuilder = new QueryBuilder(instance, mock(Connection.class));
		Builder actual = create.apply(queryBuilder);
		assertEquals(expectedGet, actual.getSql());
		assertEquals(expectedCreate, actual.createSql());
	}

	private Function<QueryBuilder, Builder> f(Function<QueryBuilder, Builder> f) {
		return f;
	}

	private  Function<Functions, String> ff(Function<Functions, String> f) {
		return f;
	}
	
}
