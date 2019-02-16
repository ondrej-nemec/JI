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