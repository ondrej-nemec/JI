package ji.querybuilder.instances;

public class PostgresSqlInstanceTest extends AbstractInstanceTest {

	public PostgresSqlInstanceTest() {
		super(new PostgreSqlQueryBuilder());
	}

	@Override
	protected String getCreateTable() {
		return 
			"CREATE TABLE SomeTable ("
				+ " Column1 SERIAL NOT NULL,"
				+ " Column2 INT DEFAULT 42 UNIQUE NULL,"
				+ " CONSTRAINT FK_Column3 FOREIGN KEY (Column3) REFERENCES AnotherTable(AnotherColumn),"
				+ " CONSTRAINT FK_Column4 FOREIGN KEY (Column4) REFERENCES DifferentTable(DifferentColumn),"
				+ " CONSTRAINT FK_Column5 FOREIGN KEY (Column5) REFERENCES DifferentTable2(DifferentColumn2),"
				+ " CONSTRAINT FK_Column6 FOREIGN KEY (Column6) REFERENCES DifferentTable3(DifferentColumn3),"
				+ " PRIMARY KEY (Column7, Column8)"
			+ ")";
	}

	@Override
	protected String getAlterTable() {
		return "ALTER TABLE SomeTable"
			+ " ADD Column1 INT NOT NULL,"
			+ " ADD Column2 INT DEFAULT 42 NULL UNIQUE,"
			+ " ADD CONSTRAINT FK_Column3 FOREIGN KEY (Column3) REFERENCES AnotherTable(AnotherColumn),"
			+ " ADD CONSTRAINT FK_Column4 FOREIGN KEY (Column4) REFERENCES DifferentTable(DifferentColumn)"
				+ " ON DELETE CASCADE ON UPDATE NO ACTION,"
			+ " DROP COLUMN Column7,"
			+ " DROP CONSTRAINT FK_Column8,"
			+ " ALTER COLUMN Column9 TYPE FLOAT,"
			+ " RENAME COLUMN Column10 TO Column11";
	}

	@Override
	protected String getDeleteTable() {
		return "DROP TABLE SomeTable";
	}

	@Override
	protected String getCreateView_fromString(boolean create) {
		return "CREATE VIEW SomeView AS SELECT A FROM SomeTable";
	}

	@Override
	protected String getCreateView_fromStringAlias(boolean create) {
		return "CREATE VIEW SomeView AS SELECT A FROM SomeTable a";
	}

	@Override
	protected String getCreateView_fromSelect(boolean create) {
		return "CREATE VIEW SomeView AS SELECT A FROM (SELECT 1 AS A) a";
	}

	@Override
	protected String getCreateView_fromMultiSelect(boolean create) {
		return "CREATE VIEW SomeView AS"
			+ " SELECT A"
			+ " FROM ("
				+ " SELECT 1 AS A"
				+ " UNION"
				+ " SELECTZ 2 AS A"
			+ ") a";
	}

	@Override
	protected String getCreateView(boolean create) {
		return "CREATE VIEW SomeView AS"
			+ " SELECT A, COUNT(B) as B"
			+ " FROM SomeTable"
			+ " JOIN AnotherTable ON 1 = 1"
			+ " JOIN AnotherTabl2 AS at2 ON 1 = 1"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect ON 1 = 1"
			+ " JOIN AnotherTable3 ON MAX(1 = 1)"
			+ " JOIN AnotherTable4 ON MAX(1 = 1)"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect2 ON MAX(1 = 1)"
			+ " WHERE (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)"
			+ " GROUP BY Column, NextColumn"
			+ (
				create
					? " HAVING 'AAAA' = 'BBBBB', SUM('XXXX') = 5"
					: " HAVING :a = :b, SUM(:x) = 5"
			)
			+ " ORDER BY Column1, AVG(Column2)"
			+ " LIMIT 10 OFFSET 15";
	}

	@Override
	protected String getAlterView_fromString(boolean create) {
		return "DROP VIEW SomeView; CREATE VIEW SomeView AS SELECT A FROM SomeTable";
	}

	@Override
	protected String getAlterView_fromStringAlias(boolean create) {
		return "DROP VIEW SomeView; CREATE VIEW SomeView AS SELECT A FROM SomeTable a";
	}

	@Override
	protected String getAlterView_fromSelect(boolean create) {
		return "DROP VIEW SomeView; CREATE VIEW SomeView AS SELECT A FROM (SELECT 1 AS A) a";
	}

	@Override
	protected String getAlterView_fromMultiSelect(boolean create) {
		return "DROP VIEW SomeView; CREATE VIEW SomeView AS"
			+ " SELECT A"
			+ " FROM ("
				+ " SELECT 1 AS A"
				+ " UNION"
				+ " SELECTZ 2 AS A"
			+ ") a";
	}

	@Override
	protected String getAlterView(boolean create) {
		return "DROP VIEW SomeView; CREATE VIEW SomeView AS"
			+ " SELECT A, COUNT(B) as B"
			+ " FROM SomeTable"
			+ " JOIN AnotherTable ON 1 = 1"
			+ " JOIN AnotherTabl2 AS at2 ON 1 = 1"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect ON 1 = 1"
			+ " JOIN AnotherTable3 ON MAX(1 = 1)"
			+ " JOIN AnotherTable4 ON MAX(1 = 1)"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect2 ON MAX(1 = 1)"
			+ " WHERE (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)"
			+ " GROUP BY Column, NextColumn"
			+ (
				create
					? " HAVING 'AAAA' = 'BBBBB', SUM('XXXX') = 5"
					: " HAVING :a = :b, SUM(:x) = 5"
			)
			+ " ORDER BY Column1, AVG(Column2)"
			+ " LIMIT 10 OFFSET 15";
	}

	@Override
	protected String getDeleteView() {
		return "DROP VIEW SomeView";
	}

	@Override
	protected String getCreateIndex() {
		return "CREATE INDEX IndexName ON SomeTable(Column1, Column2)";
	}

	@Override
	protected String getDeleteIndex() {
		return "DROP INDEX IndexName";
	}

	@Override
	protected String getQueryInsert() {
		return "INSERT INTO SomeTable (Column1, Column2) VALUES ('123', 123)";
	}

	@Override
	protected String getQueryInsertFromSelect() {
		return "WITH cte AS ("
				+ "SELECT 'A' as A"
			+ ")"
			+ "INSERT INTO SomeTable (Col1, Col2)"
			+ " SELECT 'B', 123";
	}

	@Override
	protected String getQueryUpdateBasic(boolean create) {
		return "UPDATE SomeTable"
			+ " SET Column1 = " + (create ? "123" : ":value") + ", Column2 = AVG(Column3)"
			+ " WHERE (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected String getQueryUpdateJoins(boolean create) {
		return "UPDATE SomeTable AS a"
			+ " SET Column1 = " + (create ? "123" : ":value") + ", Column2 = AVG(Column3)"
			+ " FROM AnotherTable"
			+ " JOIN AnotherTabl2 AS at2 ON 1 = 1"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect ON 1 = 1"
			+ " JOIN AnotherTable3 ON MAX(1 = 1)"
			+ " JOIN AnotherTable4 ON MAX(1 = 1)"
			+ " JOIN ("
				+ "SELECT A as C"
			+ ") subSelect2 ON MAX(1 = 1)"
			+ " WHERE (1 = 1) AND (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected String getQueryUpdateWith(boolean create) {
		return "WITH withName ("
				+ "SELECT 1=1"
			+ ")"
			+ "UPDATE SomeTable"
			+ " SET Column1 = " + (create ? "123" : ":value")
			+ ", Column2 = AVG(Column3)"
			+ " WHERE (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected Object getQueryDeleteBasic(boolean create) {
		return "DELETE FROM SomeTable"
			+ " WHERE (1= "
			+ (create ? ":id" : "123")
			+ ") OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected Object getQueryDeleteJoins(boolean create) {
		return "DELETE FROM SomeTable"
			+ " USING AnotherTable, AnotherTable2, ("
				+ " SELECT A as B"
			+ ") AS subSelect, AnotherTable3, AnotherTable4, ("
				+ "SELECT A as C"
			+ ") subSelect2"
			+ " WHERE 1 = 1 AND 2 = 2 AND 3 = 3 AND MAX(1 = 1) AND MAX(2 = 2) AND MAX(3 = 3)"
			+ " AND (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected Object getQueryDeleteWith(boolean create) {
		return "WITH withName AS ("
				+ "SELECT 1=1"
			+ ")"
			+ " DELETE FROM SomeTable"
			+ " WHERE (1=2) OR (2=3) AND (LOWER(x) = y) AND (LOWER(z) = u)";
	}

	@Override
	protected Object getQuerySelect_fromString(boolean create) {
		return "TODO";
	}

	@Override
	protected Object getQuerySelect_fromStringAlias(boolean create) {
		return "TODO";
	}

	@Override
	protected Object getQuerySelect_fromSelect(boolean create) {
		return "TODO";
	}

	@Override
	protected Object getQuerySelect_with(boolean create) {
		return "TODO";
	}

	@Override
	protected Object getQuerySelect_fromMultiSelect(boolean create) {
		return "TODO";
	}

	@Override
	protected Object getQuerySelect(boolean create) {
		return "TODO";
	}

	@Override
	protected String getQueryMultipleSelect(boolean create) {
		return "TODO";
	}

	@Override
	protected String getBatch(boolean create) {
		return "TODO";
	}
	
}
