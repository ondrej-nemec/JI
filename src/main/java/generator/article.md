# Multiplatformní spuštìní java aplikací

V tomto tutoriálu vám pøedstavím zpùsob, jak vylepšit spuštìní java aplikací. Neprve vám ukážu jednoduchý skript, který pustí jar soubor na Windowsech i na Linuxu, popíšu, jak se dá rozšíøit, a nakonec je zaøazen generátor tohoto skriptu.

**Poznámka:** pùvodní verzi skript vymyslel mùj kolega a èlánek vznikl s jeho laskavým svolením. Originál naleznete zde: [github.com](https://github.com/petrknap/violetumleditor/blob/master/run.bat)

## Motivace

Možná si øíkáte, proè takový skript použít. No, pokud si píšete aplikace jen pro sebe, tak ho nevyužijete. V opaèném pøípadì: pokud nepoužijete skript a uživatel nemá nainstalovanou javu, tak se mu vaše aplikace mùže klidnì otevøít jako archiv, což není zrovna žádoucí stav. Se skriptem se vám to nestane. Také mùžete zajistit, že se uživatelùm pustí aplikace s pøepínaèi (napøíklad kodování), které potøebujete.

## Skript

Jak pravdìpodobnì víte, skripty pro windows a linux se znaènì liší. Jak tedy udìlat univerzální skript pro oba operaèní systémy? Skript rozdílného stringu pro nový øádek na windows (\r\n) a na linuxu (\n). V následující ukázce je nejjednodušší verze skriptu bez dalších rozšíøení. Prázdné øádky v ukázce jsou jen pro èitelnost. 

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

**Poznámka:** v pøedchozí ukázce jsem vypsal stringy pro nový øádek, v dalších ukázkách to již uvádìt nebudu. Prostì všechno, co bude mezi `# Linux ` a `exit`, respektive mezi `:Windows` a `exit`, bude konèit stejným stringem, jakým konèí pøedchozí øádek.

### Jak to zpracuje Windows

Terminál na windows pøijde na první øádek, ten neumí vykonat, tak pøejde na druhý. Tomu už rozumí a vykoná. Tím pøeskoèí èást pro linux a pustí vybraný jar. Následnì zavøe terminál.

### Jak to zpracuje Linux

Zde terminál umí zpracovat první øádek, ale u druhého vypíše chybu a pøejde na tøetí. Pak pustí daný jar a zavøe terminál.

## Rozšíøení

### Znaková sada a ostatní pøepínaèe

Následují rozšíøení skriptu oceníte zvláštì, pokud používáte v aplikaci èeštinu: `-Dfile.encoding`, nejèastìji `-Dfile.encoding=UTF8`. Použití ve skriptu

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

Takto si mùžete nastavit všechny další pøepínaèe. Jejich seznam najdete pomocí `java -help`.

### Nainstalovaná java

Jak jsem psal v úvodu, nikdy nevíte, na jakém PC bude vaše aplikace spuštìna a jestli bude nainstalovaná java. Neprve se tedy zavolá pøíkaz pro vypsání verze javy a pokud je návratový kod 0 (vše probìhlo bez problémù), je java nainstalovaná, pokud není na poèítaèi java nainstalová, vypíše se hláška "You must install java first".

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

Výše uvedený skript samozøejmì mùžete znaènì vylepšit, pokud místo vypsání hlášky pøi neexistenci javy, vyhodíte nìjaké modální okno nebo spustíte vlastní skript pro instalaci javy. Také mùžete výsledek `java -version` parsovat a kontrolovat, zda je nainstalovaná verze javy, kterou potøebujete.

## Generátor skriptu

Velkou nevýhodou tohoto skriptu je, že málo textových editorù na windows umí napsat nový øádek linuxu a opaènì. Naštìstí to java mùže udìlat za nás. Proto tu teï udìlám jednochý generátor tohoto skriptu. Jako parametr bude brát jméno `.jar` souboru.

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

Jak vydíte, tøídu lze pustit z pøíkazové øádky, kde pøidáte parametr jméno vaší aplikace. V pøíloze je k dispozici zdrojový kod tohoto skriptu, vyexportovaný generátor a ještì jeden jar - test.jar - velmi jednoduchá aplikace k testování funkènosti skriptu.

Doporuèuji vytvoøený `run.bat` soubor **needitovat** v bìžných editorech, protože pokud výsledný skript na jednom os vyeditujete, editor vám pravdìpodobnì upraví i stringy pro nové øádky a skript pravdìpodobnì (v závislosti na editoru) nepùjde použít na druhém os. 

## Slova závìrem

Pokud budou uživatelé pro spouštìní vašich java aplikací používat tento skript, snížíte riziko problémù se znakovou sadou nebo problémù s nenainstalovanou javou.

Pokud by nìkdo z vás mìl nápad na vylepšení funkcionality jako napøíklad rozlišování Windows, Linux a Mac, skripty pro parsování verze javy, skript pro vypsání hlášky o neexistenci javy nebo spuštìní její instalace, dejte do komenáøù odkaz nebo kód a já to zaèlením do tohoto èlánku.



---


## Obsah
Neprve se pokusím vám ukázat, proè používat pro spouštìní `.jar` souborù skript. Pak vám pøedvedu jednoduchý skript, který pustí jar soubor na Windowsech i na Linuxu, popíšu, jak se dá rozšíøit, a nakonec je zaøazen generátor tohoto skriptu.

**Poznámka:** pùvodní verzi skript vymyslel mùj kolega a èlánek vznikl s jeho laskavým svolením. Originál naleznete zde: [github.com](https://github.com/petrknap/violetumleditor/blob/master/run.bat)

## Motivace

Možná si øíkáte, proè takový skript použít. No, pokud si píšete aplikace jen pro sebe, tak ho nevyužijete. V opaèném pøípadì: pokud nepoužijete skript a uživatel nemá nainstalovanou javu, tak se mu vaše aplikace mùže klidnì otevøít jako archiv, což není zrovna žádoucí stav. Se skriptem se vám to nestane. Také mùžete zajistit, že se uživatelùm pustí aplikace s pøepínaèi (napøíklad kodování), které potøebujete.

## Skript

Jak pravdìpodobnì víte, skripty pro windows a linux se znaènì liší. Jak tedy udìlat univerzální skript pro oba operaèní systémy? Skript rozdílného stringu pro nový øádek na windows (\r\n) a na linuxu (\n). V následující ukázce je nejjednodušší verze skriptu bez dalších rozšíøení. Prázdné øádky v ukázce jsou jen pro èitelnost. 

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

**Poznámka:** v pøedchozí ukázce jsem vypsal stringy pro nový øádek, v dalších ukázkách to již uvádìt nebudu. Prostì všechno, co bude mezi `# Linux` a `exit`, respektive mezi `:Windows` a `exit`, bude konèit stejným stringem, jakým konèí pøedchozí øádek.

### Jak to zpracuje Windows

Terminál na windows pøijde na první øádek, ten neumí vykonat, tak pøejde na druhý. Tomu už rozumí a vykoná. Tím pøeskoèí èást pro linux a pustí vybraný jar. Následnì zavøe terminál.

### Jak to zpracuje Linux

Zde terminál umí zpracovat první øádek, ale u druhého vypíše chybu a pøejde na tøetí. Pak pustí daný jar a zavøe terminál.

## Rozšíøení

### Znaková sada a ostatní pøepínaèe

Následují rozšíøení skriptu oceníte zvláštì, pokud používáte v aplikaci èeštinu: `-Dfile.encoding`, nejèastìji `-Dfile.encoding=UTF8`. Použití ve skriptu

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

Takto si mùžete nastavit všechny další pøepínaèe. Jejich seznam najdete pomocí `java -help`.

### Nainstalovaná java

Jak jsem psal v úvodu, nikdy nevíte, na jakém PC bude vaše aplikace spuštìna a jestli bude nainstalovaná java. Neprve se tedy zavolá pøíkaz pro vypsání verze javy a pokud je návratový kod 0 (vše probìhlo bez problémù), je java nainstalovaná, pokud není na poèítaèi java nainstalová, vypíše se hláška "You must install java first".

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

Výše uvedený skript samozøejmì mùžete znaènì vylepšit, pokud místo vypsání hlášky pøi neexistenci javy, vyhodíte nìjaké modální okno nebo spustíte vlastní skript pro instalaci javy. Také mùžete výsledek `java -version` parsovat a kontrolovat, zda je nainstalovaná verze javy, kterou potøebujete.

## Generátor skriptu

Velkou nevýhodou tohoto skriptu je, že málo textových editorù na windows umí napsat nový øádek linuxu a opaènì. Naštìstí to java mùže udìlat za nás. Proto tu teï udìlám jednochý generátor tohoto skriptu. Jako parametr bude brát jméno `.jar` souboru.

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

Jak vydíte, tøídu lze pustit z pøíkazové øádky, kde pøidáte parametr jméno vaší aplikace. V pøíloze je k dispozici zdrojový kod tohoto skriptu, vyexportovaný generátor a ještì jeden jar - test.jar - velmi jednoduchá aplikace k testování funkènosti skriptu.

Použití vyexportovaného generátoru je - pøi použití jarù z pøílohy - následující:

Linux

/--code
java -jar ./run_generator.jar test.jar
\--

Windows

/--code
start javaw -jar ./run_generator.jar test.jar
\--

Doporuèuji vytvoøený `run.bat` soubor **needitovat** v bìžných editorech, protože pokud výsledný skript na jednom os vyeditujete, editor vám pravdìpodobnì upraví i stringy pro nové øádky a skript pravdìpodobnì (v závislosti na editoru) nepùjde použít na druhém os. 

## Slova závìrem

Pokud budou uživatelé pro spouštìní vašich java aplikací používat tento skript, snížíte riziko problémù se znakovou sadou nebo problémù s nenainstalovanou javou.

Pokud by nìkdo z vás mìl nápad na vylepšení funkcionality jako napøíklad rozlišování Windows, Linux a Mac, skripty pro parsování verze javy, skript pro vypsání hlášky o neexistenci javy nebo spuštìní její instalace, dejte do komenáøù odkaz nebo kód a já to zaèlením do tohoto èlánku.



