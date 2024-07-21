package ji.querybuilder.instances;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.sql.Connection;
import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.querybuilder.Builder;
import ji.querybuilder.DbInstance;
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
	
	/*@Test
	public void tesFunctions(Function<Functions, String> create, String expected) {
		System.out.println("Working " + getClass())// TODO
	}*/
	
	// TODO test enumns to string 
	
	
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
			.addColumn("Column1", ColumnType.integer(), ColumnSetting.NOT_NULL, ColumnSetting.PRIMARY_KEY, ColumnSetting.AUTO_INCREMENT)
			.addColumn("Column2", ColumnType.integer(), 42, ColumnSetting.NULL, ColumnSetting.UNIQUE)
			.addForeignKey("Column3", "AnotherTable", "AnotherColumn")
			.addForeignKey("Column4", "DifferentTable", "DifferentColumn", OnAction.CASCADE, OnAction.NO_ACTION)
			.addForeignKey("Column5", "DifferentTable2", "DifferentColumn2", OnAction.RESTRICT, OnAction.SET_DEFAULT)
			.addForeignKey("Column6", "DifferentTable3", "DifferentColumn3", OnAction.SET_NULL, null)
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
					.join("AnotherTable4", "at2", Join.INNER_JOIN, f->f.max("1 = 1"))
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
					.join("AnotherTable4", "at2", Join.INNER_JOIN, f->f.max("1 = 1"))
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
		test(b->b.createIndex("IndexName", "SomeTable", "Column1, Column2"), getCreateIndex());
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
					.addValue("Column1", 123)
				),
				getQueryInsert()
			},
			new Object[] {
				f(
					b->b.insert("SomeTable")
					.with("cte", b.select("'A' as A"))
					.fromSelect(Arrays.asList("Col1", "Col2"), b.select("'B'"))
				),
				getQueryInsertFromSelect()
			}
		};
	}
	
	protected abstract String getQueryInsert();
	
	protected abstract String getQueryInsertFromSelect();

	@Test
	public void testQueryUpdate() {
		fail("TODO");
	}

	@Test
	public void testQueryDelete() {
		fail("TODO");
	}

	@Test
	public void testQuerySelect() {
		fail("TODO");
	}

	@Test
	public void testQueryMultipleSelect() {
		fail("TODO");
	}
	
	/********* OTHER **************/

	@Test
	public void testBatch() {
		fail("TODO");
	}
	
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
	
}
