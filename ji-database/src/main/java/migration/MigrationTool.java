package migration;

public class MigrationTool {
	
	
	/*
	 * 1 - nahrat jmena souboru do mapy - klic je jmeno, hodnota zvoleny "migrator" -> sql/class
	 *   - !! java soubory nepridavat -> ? prvni kompilace
	 *   - !! sesortovat podle id
	 *   - otestovat, zda muze byt pouzito jako id
	 *   - viz loadResources
	 * 2 - kontrola existence z�znam� migrace (tabulka), p��padn� jej� vytvo�en�
	 *   - nejsp� select nad n�, a vr�cen� listu z�znamu, pokud se bude vytv��et, tak pr�zdn� seznam
	 * 3 - projiti seznamu souboru, pokud jmeno neni v seznamu zaznamu, zkontrolovat, ze neni dalsi zaznam a provest
	 *   - zapsat soubor do zaznamu migrace
	 *   - pro provadeni migrace a zapsani do zaznamu migraci pouzit transakci
	 * 
	 *  v sql souborech bude moznost dat oddelovac - pro foward a revert cast
	 * 
	 * 
	 */

}
