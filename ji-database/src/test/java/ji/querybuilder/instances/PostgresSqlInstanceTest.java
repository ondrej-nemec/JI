package ji.querybuilder.instances;

import static org.junit.Assert.fail;

public class PostgresSqlInstanceTest extends AbstractInstanceTest {

	public PostgresSqlInstanceTest() {
		super(new PostgreSqlQueryBuilder());
	}

	@Override
	protected String getCreateTable() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterTable() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getDeleteTable() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateView_fromString(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateView_fromStringAlias(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateView_fromSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateView_fromMultiSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateView(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterView_fromString(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterView_fromStringAlias(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterView_fromSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterView_fromMultiSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getAlterView(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getDeleteView() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getCreateIndex() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getDeleteIndex() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryInsert() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryInsertFromSelect() {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryUpdateBasic(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryUpdateJoins(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryUpdateWith(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQueryDeleteBasic(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQueryDeleteJoins(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQueryDeleteWith(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect_fromString(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect_fromStringAlias(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect_fromSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect_with(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect_fromMultiSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected Object getQuerySelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getQueryMultipleSelect(boolean create) {
		fail("TODO");
		return null;
	}

	@Override
	protected String getBatch(boolean create) {
		fail("TODO");
		return null;
	}
	
}
