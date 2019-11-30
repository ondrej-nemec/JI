package migration.resources;

import java.util.List;

import common.exceptions.NotImplementedYet;
import querybuilder.QueryBuilder;

public class MigrationPreparation {

	public List<String> getFilesToMigrate(List<String> loadedFiles, boolean isRevert, QueryBuilder builder) {
		throw new NotImplementedYet();
	}
	// nacte z db probehnute migrace, pripadne vytvory tabulku
	// pro revert vrati probehnute migrace ??
	/*
	try {
			return builder
				.select("id")
				.from(migrationTable)
				.fetchAll((row)->{
					return row.getValue("id");
				});
		} catch (Exception ignored) {
			builder
				.createTable(migrationTable)
				.addColumn("id", ColumnType.string(100), ColumnSetting.NOT_NULL, ColumnSetting.PRIMARY_KEY)
				.addColumn("Description", ColumnType.string(100), ColumnSetting.NOT_NULL)
				.addColumn("DateTime", ColumnType.string(100), ColumnSetting.NOT_NULL)
				.execute();
		}
		return new LinkedList<>();
	*/
	
	// projde ziskane migrace a porovna se soubory - kontrola "diry" v migracich
	// vrati seznam neprobehnutych migraci
	
}
