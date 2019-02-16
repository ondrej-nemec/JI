# Multiplatformn� spu�t�n� java aplikac�

V tomto tutori�lu v�m p�edstav�m zp�sob, jak vylep�it spu�t�n� java aplikac�. Neprve v�m uk�u jednoduch� skript, kter� pust� jar soubor na Windowsech i na Linuxu, pop�u, jak se d� roz���it, a nakonec je za�azen gener�tor tohoto skriptu.

**Pozn�mka:** skript p�vodn� vymyslel m�j kolega a �l�nek vznikl s jeho laskav�m svolen�m. Origin�l naleznete zde: [github.com](https://github.com/petrknap/violetumleditor/blob/master/run.bat)

## Motivace

Mo�n� si ��k�te, pro� takov� skript pou��t. No, pokud si p�ete aplikace jen pro sebe, tak ho nevyu�ijete. V opa�n�m p��pad�: pokud nepou�ijete skript a u�ivatel nem� nainstalovanou javu, tak se mu va�e aplikace m��e klidn� otev��t jako archiv, co� nen� zrovna ��douc� stav. Se skriptem se v�m to nestane. Tak� m��ete zajistit, �e se u�ivatel�m pust� aplikace s p�ep�na�i, kter� pot�ebujete.

## Skript

Jak pravd�podobn� v�te, skripty pro windows a linux se zna�n� li��. Jak tedy ud�lat univerz�ln� skript pro oba opera�n� syst�my?

**TODO**

```
#!/bin/bash
GOTO Windows

# Linux
java -jar ./file-name.jar
exit

:Windows
start javaw -jar ./file-name.jar
exit
```

**TODO** skript windows
**TODO** skript linux

## Roz���en�

### Znakov� sada a ostatn� p�ep�na�e

N�sleduj� roz���en� skriptu ocen�te zvl�t�, pokud pou��v�te v aplikaci �e�tinu: `-Dfile.encoding`, nej�ast�ji `-Dfile.encoding=UTF8`. Pou�it� ve skriptu

```
#!/bin/bash
GOTO Windows

# Linux
java -Dfile.encoding -jar ./file-name.jar
exit

:Windows
start javaw -Dfile.encoding -jar ./file-name.jar
exit
```

Takto si m��ete nastavit v�echny dal�� p�ep�na�e. Jejich seznam najdete pomoc� `java -help`.

### Nainstalovan� java

Jak jsem psal v �vodu, nikdy nev�te, na jak�m PC bude va�e aplikace spu�t�na a jestli bude nainstalovan� java. Neprve se tedy zavol� p��kaz pro vyps�n� verze javy a pokud je n�vratov� kod 0 (v�e prob�hlo bez probl�m�), je java nainstalovan�, pokud nen� na po��ta�i java nainstalov�, vyp�e se hl�ka "You must install java first".

```
#!/bin/bash
GOTO Windows

# Linux
java -version
if [ $? -eq 0 ]; then
	java -jar ./file-name.jar
else
	echo You must install java first
fi
exit

:Windows
java -version
IF %errorlevel% EQU 0 (
	start javaw -jar ./file-name.jar
) ELSE (
	echo You must install java first
)
exit
```

V��e uveden� skript samoz�ejm� m��ete zna�n� vylep�it, pokud m�sto vyps�n� hl�ky p�i neexistenci javy, vyhod�te n�jak� mod�ln� okno nebo spust�te vlastn� skript pro instalaci javy. Tak� m��ete v�sledek `java -version` parsovat a kontrolovat, zda je nainstalovan� verze javy, kterou pot�ebujete.

## Gener�tor skriptu

Velkou nev�hodou tohoto skriptu je, �e m�lo textov�ch editor� na windows um� napsat nov� ��dek linuxu a opa�n�. Na�t�st� to java m��e ud�lat za n�s. Proto tu te� ud�l�m jednoch� gener�tor tohoto skriptu. Jako parametr bude br�t jm�no `.jar` souboru.

```
package generator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RunnerGenerate {
	
	private String win = "\r\n";
	
	private String linux = "\n";

	public RunnerGenerate(final String jarFile) {
		try(BufferedWriter br = new BufferedWriter(new FileWriter("run.bat"))) {
			br.write(
					"#!/bin/bash" + linux + win
					+ "GOTO Windows" + win
					
					+ "# Linux" + linux
					+ "java -version" + linux
					+ "if [ $? -eq 0 ]; then" + linux
					+ "	java -Dfile.encoding=UTF8 -jar ./" + jarFile + linux
					+ "else" + linux
					+ "	echo You must install java first" + linux
					+ "fi" + linux
					+ "exit" + linux + win
					
					+ ":Windows" + win
					+ "java -version" + win
					+ "IF %errorlevel% EQU 0 (" + win
					+ "	start javaw -Dfile.encoding=UTF8 -jar ./" + jarFile + win
					+ ") ELSE (" + win
					+ "	echo You must install java first" + win
					+ ")" + win
					 + "exit"
			);
			System.out.println("completed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RunnerGenerate(args[0]);
	}
}
```


