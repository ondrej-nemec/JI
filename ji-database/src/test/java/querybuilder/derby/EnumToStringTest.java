package querybuilder.derby;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import querybuilder.ColumnSetting;
import querybuilder.ColumnType;
import querybuilder.Join;
import querybuilder.OnAction;

@RunWith(JUnitParamsRunner.class)
public class EnumToStringTest {

	@Test
	@Parameters
	public void testJoinToString(Join join, String expected) {
		assertEquals(expected, EnumToDerbyString.joinToString(join));
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
		EnumToDerbyString.joinToString(Join.FULL_OUTER_JOIN);
	}
	
	@Test
	@Parameters(method = "dataSettingToStringReturnsCorrectString")
	public void testSettingToStringReturnsCorrectString(String expected, ColumnSetting setting, String column, String appended) {
		StringBuilder append = new StringBuilder();
		assertEquals(expected, EnumToDerbyString.settingToString(setting, column, append));
		assertEquals(appended, append.toString());
	}
	
	public Object[] dataSettingToStringReturnsCorrectString() {
		return new Object[] {
			new Object[] {" GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)", ColumnSetting.AUTO_INCREMENT, "col", ""},
			new Object[] {" UNIQUE", ColumnSetting.UNIQUE, "col", ""},
			new Object[] {" NOT NULL", ColumnSetting.NOT_NULL, "col", ""},
			new Object[] {"", ColumnSetting.NULL, "col", ""},
			new Object[] {"", ColumnSetting.PRIMARY_KEY, "col", ", PRIMARY KEY (col)"}
		};
	}

	@Test
	@Parameters(method = "dataTypeToStringReturnsCorrectString")
	public void testTypeToStringReturnsCorrectString(String expected, ColumnType type) {
		assertEquals(expected, EnumToDerbyString.typeToString(type));
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
		assertEquals(expected, EnumToDerbyString.onActionToString(action));
	}
	
	public Object[] dataOnActionToStringReturnsCorrectString() {
		return new Object[] {
			new Object[] {"RESTRICT", OnAction.RESTRICT},
		//	new Object[] {"CASCADE", OnAction.CASCADE},
			new Object[] {"SET NULL", OnAction.SET_NULL},
			new Object[] {"NO ACTION", OnAction.NO_ACTION},
			new Object[] {"SET DEFAULT", OnAction.SET_DEFAULT},
		};
	}

	@Test(expected = RuntimeException.class)
	public void testOnActionToStringThrowsWhenActionIsNotSupported() {
		EnumToDerbyString.onActionToString(OnAction.CASCADE);
	}
	
}
