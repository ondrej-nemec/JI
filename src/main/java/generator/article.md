# Multiplatformn� spu�t�n� java aplikac�

V tomto tutori�lu v�m p�edstav�m zp�sob, jak vylep�it spu�t�n� java aplikac�. Neprve v�m uk�u jednoduch� skript, kter� pust� jar soubor na Windowsech i na Linuxu, pop�u, jak se d� roz���it, a nakonec je za�azen gener�tor tohoto skriptu.

**Pozn�mka:** p�vodn� verzi skript vymyslel m�j kolega a �l�nek vznikl s jeho laskav�m svolen�m. Origin�l naleznete zde: [github.com](https://github.com/petrknap/violetumleditor/blob/master/run.bat)

## Motivace

Mo�n� si ��k�te, pro� takov� skript pou��t. No, pokud si p�ete aplikace jen pro sebe, tak ho nevyu�ijete. V opa�n�m p��pad�: pokud nepou�ijete skript a u�ivatel nem� nainstalovanou javu, tak se mu va�e aplikace m��e klidn� otev��t jako archiv, co� nen� zrovna ��douc� stav. Se skriptem se v�m to nestane. Tak� m��ete zajistit, �e se u�ivatel�m pust� aplikace s p�ep�na�i (nap��klad kodov�n�), kter� pot�ebujete.

## Skript

Jak pravd�podobn� v�te, skripty pro windows a linux se zna�n� li��. Jak tedy ud�lat univerz�ln� skript pro oba opera�n� syst�my? Skript rozd�ln�ho stringu pro nov� ��dek na windows (\r\n) a na linuxu (\n). V n�sleduj�c� uk�zce je nejjednodu��� verze skriptu bez dal��ch roz���en�. Pr�zdn� ��dky v uk�zce jsou jen pro �itelnost. 

```
#!/bin/bash\n\r\n
GOTO Windows\r\n

# Linux\n
java -jar ./file-name.jar\n
exit\n\r\n

:Windows\r\n
start javaw -jar ./file-name.jar\r\n
exit\r\n
```

**Pozn�mka:** v p�edchoz� uk�zce jsem vypsal stringy pro nov� ��dek, v dal��ch uk�zk�ch to ji� uv�d�t nebudu. Prost� v�echno, co bude mezi `# Linux ` a `exit`, respektive mezi `:Windows` a `exit`, bude kon�it stejn�m stringem, jak�m kon�� p�edchoz� ��dek.

### Jak to zpracuje Windows

Termin�l na windows p�ijde na prvn� ��dek, ten neum� vykonat, tak p�ejde na druh�. Tomu u� rozum� a vykon�. T�m p�esko�� ��st pro linux a pust� vybran� jar. N�sledn� zav�e termin�l.

### Jak to zpracuje Linux

Zde termin�l um� zpracovat prvn� ��dek, ale u druh�ho vyp�e chybu a p�ejde na t�et�. Pak pust� dan� jar a zav�e termin�l.

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

```java
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

Jak vyd�te, t��du lze pustit z p��kazov� ��dky, kde p�id�te parametr jm�no va�� aplikace. V p��loze je k dispozici zdrojov� kod tohoto skriptu, vyexportovan� gener�tor a je�t� jeden jar - test.jar - velmi jednoduch� aplikace k testov�n� funk�nosti skriptu.

Doporu�uji vytvo�en� `run.bat` soubor **needitovat** v b�n�ch editorech, proto�e pokud v�sledn� skript na jednom os vyeditujete, editor v�m pravd�podobn� uprav� i stringy pro nov� ��dky a skript pravd�podobn� (v z�vislosti na editoru) nep�jde pou��t na druh�m os. 

## Slova z�v�rem

Pokud budou u�ivatel� pro spou�t�n� va�ich java aplikac� pou��vat tento skript, sn��te riziko probl�m� se znakovou sadou nebo probl�m� s nenainstalovanou javou.

Pokud by n�kdo z v�s m�l n�pad na vylep�en� funkcionality jako nap��klad rozli�ov�n� Windows, Linux a Mac, skripty pro parsov�n� verze javy, skript pro vyps�n� hl�ky o neexistenci javy nebo spu�t�n� jej� instalace, dejte do komen��� odkaz nebo k�d a j� to za�len�m do tohoto �l�nku.



---


## Obsah
Neprve se pokus�m v�m uk�zat, pro� pou��vat pro spou�t�n� `.jar` soubor� skript. Pak v�m p�edvedu jednoduch� skript, kter� pust� jar soubor na Windowsech i na Linuxu, pop�u, jak se d� roz���it, a nakonec je za�azen gener�tor tohoto skriptu.

**Pozn�mka:** p�vodn� verzi skript vymyslel m�j kolega a �l�nek vznikl s jeho laskav�m svolen�m. Origin�l naleznete zde: [github.com](https://github.com/petrknap/violetumleditor/blob/master/run.bat)

## Motivace

Mo�n� si ��k�te, pro� takov� skript pou��t. No, pokud si p�ete aplikace jen pro sebe, tak ho nevyu�ijete. V opa�n�m p��pad�: pokud nepou�ijete skript a u�ivatel nem� nainstalovanou javu, tak se mu va�e aplikace m��e klidn� otev��t jako archiv, co� nen� zrovna ��douc� stav. Se skriptem se v�m to nestane. Tak� m��ete zajistit, �e se u�ivatel�m pust� aplikace s p�ep�na�i (nap��klad kodov�n�), kter� pot�ebujete.

## Skript

Jak pravd�podobn� v�te, skripty pro windows a linux se zna�n� li��. Jak tedy ud�lat univerz�ln� skript pro oba opera�n� syst�my? Skript rozd�ln�ho stringu pro nov� ��dek na windows (\r\n) a na linuxu (\n). V n�sleduj�c� uk�zce je nejjednodu��� verze skriptu bez dal��ch roz���en�. Pr�zdn� ��dky v uk�zce jsou jen pro �itelnost. 

/--code 
#!/bin/bash\n\r\n
GOTO Windows\r\n

# Linux\n
java -jar ./file-name.jar\n
exit\n\r\n

:Windows\r\n
start javaw -jar ./file-name.jar\r\n
exit
\--

**Pozn�mka:** v p�edchoz� uk�zce jsem vypsal stringy pro nov� ��dek, v dal��ch uk�zk�ch to ji� uv�d�t nebudu. Prost� v�echno, co bude mezi `# Linux` a `exit`, respektive mezi `:Windows` a `exit`, bude kon�it stejn�m stringem, jak�m kon�� p�edchoz� ��dek.

### Jak to zpracuje Windows

Termin�l na windows p�ijde na prvn� ��dek, ten neum� vykonat, tak p�ejde na druh�. Tomu u� rozum� a vykon�. T�m p�esko�� ��st pro linux a pust� vybran� jar. N�sledn� zav�e termin�l.

### Jak to zpracuje Linux

Zde termin�l um� zpracovat prvn� ��dek, ale u druh�ho vyp�e chybu a p�ejde na t�et�. Pak pust� dan� jar a zav�e termin�l.

## Roz���en�

### Znakov� sada a ostatn� p�ep�na�e

N�sleduj� roz���en� skriptu ocen�te zvl�t�, pokud pou��v�te v aplikaci �e�tinu: `-Dfile.encoding`, nej�ast�ji `-Dfile.encoding=UTF8`. Pou�it� ve skriptu

/--code 
#!/bin/bash
GOTO Windows

# Linux
java -Dfile.encoding -jar ./file-name.jar
exit

:Windows
start javaw -Dfile.encoding -jar ./file-name.jar
exit
\-- 

Takto si m��ete nastavit v�echny dal�� p�ep�na�e. Jejich seznam najdete pomoc� `java -help`.

### Nainstalovan� java

Jak jsem psal v �vodu, nikdy nev�te, na jak�m PC bude va�e aplikace spu�t�na a jestli bude nainstalovan� java. Neprve se tedy zavol� p��kaz pro vyps�n� verze javy a pokud je n�vratov� kod 0 (v�e prob�hlo bez probl�m�), je java nainstalovan�, pokud nen� na po��ta�i java nainstalov�, vyp�e se hl�ka "You must install java first".

/--code 
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
\--

V��e uveden� skript samoz�ejm� m��ete zna�n� vylep�it, pokud m�sto vyps�n� hl�ky p�i neexistenci javy, vyhod�te n�jak� mod�ln� okno nebo spust�te vlastn� skript pro instalaci javy. Tak� m��ete v�sledek `java -version` parsovat a kontrolovat, zda je nainstalovan� verze javy, kterou pot�ebujete.

## Gener�tor skriptu

Velkou nev�hodou tohoto skriptu je, �e m�lo textov�ch editor� na windows um� napsat nov� ��dek linuxu a opa�n�. Na�t�st� to java m��e ud�lat za n�s. Proto tu te� ud�l�m jednoch� gener�tor tohoto skriptu. Jako parametr bude br�t jm�no `.jar` souboru.

/--code java
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
\--

Jak vyd�te, t��du lze pustit z p��kazov� ��dky, kde p�id�te parametr jm�no va�� aplikace. V p��loze je k dispozici zdrojov� kod tohoto skriptu, vyexportovan� gener�tor a je�t� jeden jar - test.jar - velmi jednoduch� aplikace k testov�n� funk�nosti skriptu.

Pou�it� vyexportovan�ho gener�toru je - p�i pou�it� jar� z p��lohy - n�sleduj�c�:

Linux

/--code
java -jar ./run_generator.jar test.jar
\--

Windows

/--code
start javaw -jar ./run_generator.jar test.jar
\--

Doporu�uji vytvo�en� `run.bat` soubor **needitovat** v b�n�ch editorech, proto�e pokud v�sledn� skript na jednom os vyeditujete, editor v�m pravd�podobn� uprav� i stringy pro nov� ��dky a skript pravd�podobn� (v z�vislosti na editoru) nep�jde pou��t na druh�m os. 

## Slova z�v�rem

Pokud budou u�ivatel� pro spou�t�n� va�ich java aplikac� pou��vat tento skript, sn��te riziko probl�m� se znakovou sadou nebo probl�m� s nenainstalovanou javou.

Pokud by n�kdo z v�s m�l n�pad na vylep�en� funkcionality jako nap��klad rozli�ov�n� Windows, Linux a Mac, skripty pro parsov�n� verze javy, skript pro vyps�n� hl�ky o neexistenci javy nebo spu�t�n� jej� instalace, dejte do komen��� odkaz nebo k�d a j� to za�len�m do tohoto �l�nku.



