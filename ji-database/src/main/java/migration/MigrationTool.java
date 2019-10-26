package migration;

public class MigrationTool {
	
	
	/*
	 * 1 - nahrat jmena souboru do mapy - klic je jmeno, hodnota zvoleny "migrator" -> sql/class
	 *   - !! java soubory nepridavat -> ? prvni kompilace
	 *   - !! sesortovat podle id
	 *   - otestovat, zda muze byt pouzito jako id
	 *   - viz loadResources
	 * 2 - kontrola existence záznamù migrace (tabulka), pøípadnì její vytvoøení
	 *   - nejspíš select nad ní, a vrácení listu záznamu, pokud se bude vytváøet, tak prázdný seznam
	 * 3 - projiti seznamu souboru, pokud jmeno neni v seznamu zaznamu, zkontrolovat, ze neni dalsi zaznam a provest
	 *   - zapsat soubor do zaznamu migrace
	 *   - pro provadeni migrace a zapsani do zaznamu migraci pouzit transakci
	 * 
	 *  v sql souborech bude moznost dat oddelovac - pro foward a revert cast
	 * 
	 * 
	 */

}
