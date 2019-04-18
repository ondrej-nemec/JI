package utils.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RunnerGenerate {
	
	private String win = "\r\n";
	
	private String linux = "\n";

	public RunnerGenerate(final String jarName) {
		try(BufferedWriter br = new BufferedWriter(new FileWriter("run.bat"))) {
			br.write(
				"#!/bin/bash" + win + linux
				+ "# commented if run on linux" + win
				+ "GOTO Windows" + win + linux
				
				+ "# Linux" + linux
				+ "java -version" + linux
				+ "if [ $? -eq 0 ]; then" + linux
				+ "	java --Dfile.encoding=UTF8 jar ./" + jarName + linux
				+ "else" + linux
				+ "	echo You must install java first" + linux
				+ "fi" + linux
				+ "exit" + linux + win
				
				+ ":Windows" + win
				+ "java -version" + win
				+ "IF errorlevel 0 "
				+ "(start javaw -Dfile.encoding=UTF8 -jar ./" + jarName + ")"
				+ "ELSE"
				+ " (echo You must install java first)" + win + linux
				// + "exit"
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RunnerGenerate(args[0]);
	}
}
