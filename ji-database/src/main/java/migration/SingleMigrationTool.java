package migration;

import common.exceptions.NotImplementedYet;
import querybuilder.QueryBuilder;

public class SingleMigrationTool {
	
	public SingleMigrationTool() {
		// TODO Auto-generated constructor stub
	}

	public void transaction(String name, QueryBuilder builder, boolean isRevert) {
		// name je i s priponou - extract zde
		throw new NotImplementedYet();
	}
	// pro dany soubor zacne transakci, vybere spravny zpusob migrace a transakci spravne ukonci
	// opacne - transakce, zpusob odmigrace, smazani zaznamu, ukonceni transakce
	// foward / revert dulezite jen pro insert/delete
	/*
	try {
		con.setAutoCommit(false);
		String type = files.get(key.toString()).toString();
    	String name = key.toString();
    	switch (type) {
    		case "sql": 
    			migrate(path + "/" + name + ".sql", builder, revert, external);
    			break;
    		case "class":
    		case "java":
    			migrate(name, loader, external ? "" : path + ".", revert);
    			break;
    		default: break;
    	}
    	String[] names = parseName(name);
    	if (!names[0].contains(ALLWAYS_ID)) {
    		builder
    		.insert(migrationTable)
    		.addValue("id", names[0])
    		.addValue("Description", names[1])
    		.addValue("DateTime", DateTime.format("YYYY-mm-dd H:m:s"))
    		.execute();
    	}
		con.commit();
	} catch (Exception e) {
		con.rollback();
		throw e;
	}
	*/
	/*
	private String[] parseName(String name) {
		if (!name.contains(NAME_SEPARATOR)) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}		
		String[] names = name.split(NAME_SEPARATOR);
		if (names.length != 2) {
			throw new RuntimeException("File name is in incorrect format: " + name);
		}
		return names;
	}
	*/
}
