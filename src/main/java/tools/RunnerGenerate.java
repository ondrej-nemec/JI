package tools;

import java.io.BufferedWriter;
import java.io.IOException;

import common.Os;
import text.BufferedWriterFactory;
import text.plaintext.PlainTextCreator;

public class RunnerGenerate {

	public RunnerGenerate(final String jarName) {
		try(BufferedWriter br = BufferedWriterFactory.buffer("runApp.bat", false)) {
			new PlainTextCreator(br).write(
				"#!/bin/bash" +
				Os.WINDOWS_NEW_LINE + Os.LINUX_NEW_LINE +
				"java -version" +
				"# Linux" + Os.LINUX_NEW_LINE +
				"if [ $? -eq 0 ]; then" + Os.LINUX_NEW_LINE +
				"	java -Dfile.encoding=UTF8 -jar " + jarName +
				"else" + Os.LINUX_NEW_LINE +
				"	echo You must install java first" + Os.LINUX_NEW_LINE +
				"fi" +
				
				Os.WINDOWS_NEW_LINE + Os.LINUX_NEW_LINE +
				
				"# Windows" + Os.WINDOWS_NEW_LINE +
				"if errorlevel 0 start javaw -Dfile.encoding=UTF8 -jar " + jarName + " else echo You must install java first"
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RunnerGenerate(args[0]);
	}
}
