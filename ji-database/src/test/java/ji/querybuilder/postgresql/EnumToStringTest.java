package ji.querybuilder.postgresql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import ji.querybuilder.enums.ColumnSetting;
import ji.querybuilder.enums.ColumnType;
import ji.querybuilder.enums.Join;
import ji.querybuilder.enums.OnAction;
import ji.querybuilder.postgresql.EnumToPostgresqlString;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class EnumToStringTest {

	@Test
	@Parameters
	public void testJoinToString(Join join, String expected) {
		assertEquals(expected, EnumToPostgresqlString.joinToString(join));
	}
	
	public Object[] parametersForTestJoinToString() {
		return new Object[] {
			new Object[] {Join.INNER_JOIN, "JOIN"},
			new Object[] {Join.LEFT_OUTER_JOIN, "LEFT JOIN"},
			new Object[] {Join.RIGHT_OUTER_JOIN, "RIGHT JOIN"}
		};
	}
	
	@Test(expected=RuntimeException.class)
	public void testJoinToStringThrowsWhenFullOuterJoin() {
		EnumToPostgresqlString.joinToString(Join.FULL_OUTER_JOIN);
	}
	
	@Test
	@Parameters(method = "dataSettingToStringReturnsCorrectString")
	public void testSettingToStringReturnsCorrectString(String expected, ColumnSetting setting, String column, String appended) {
		StringBuilder append = new StringBuilder();
		assertEquals(expected, EnumToPostgresqlString.settingToString(setting, column, append));
		assertEquals(appended, append.toString());
	}
	
	public Object[] dataSettingToStringReturnsCorrectString() {
		return new Object[] {
			new Object[] {" SERIAL", ColumnSetting.AUTO_INCREMENT, "col", ""},
			new Object[] {" UNIQUE", ColumnSetting.UNIQUE, "col", ""},
			new Object[] {" NOT NULL", ColumnSetting.NOT_NULL, "col", ""},
			new Object[] {" NULL", ColumnSetting.NULL, "col", ""},
			new Object[] {"", ColumnSetting.PRIMARY_KEY, "col", ", PRIMARY KEY (col)"}
		};
	}

	@Test
	@Parameters(method = "dataTypeToStringReturnsCorrectString")
	public void testTypeToStringReturnsCorrectString(String expected, ColumnType type) {
		assertEquals(expected, EnumToPostgresqlString.typeToString(type));
	}
	
	public Object[] dataTypeToStringReturnsCorrectString() {
		return new Object[] {
			new Object[] {"BOOLEAN", ColumnType.bool()},
			new Object[] {"INT", ColumnType.integer()},
			new Object[] {"TIMESTAMP", ColumnType.datetime()},
			new Object[] {"CHAR(20)", ColumnType.charType(20)},
			new Object[] {"VARCHAR(10)", ColumnType.string(10)},
			new Object[] {"VARCHAR(255)", ColumnType.string(255)},
			new Object[] {"TEXT", ColumnType.text()}
		};
	}

	@Test
	@Parameters(method = "dataOnActionToStringReturnsCorrectString")
	public void testOnActionToStringReturnsCorrectString(String expected, OnAction action) {
		assertEquals(expected, EnumToPostgresqlString.onActionToString(action));
	}
	
	public Object[] dataOnActionToStringReturnsCorrectString() {
		return new Object[] {
			new Object[] {"RESTRICT", OnAction.RESTRICT},
			new Object[] {"CASCADE", OnAction.CASCADE},
			new Object[] {"SET NULL", OnAction.SET_NULL},
			new Object[] {"NO ACTION", OnAction.NO_ACTION},
			new Object[] {"SET DEFAULT", OnAction.SET_DEFAULT},
		};
	}
	
}
