package tools;

import java.io.BufferedWriter;
import java.io.IOException;

import common.env.Os;
import text.plaintext.PlainTextCreator;

public class RunnerGenerate {

	public RunnerGenerate(PlainTextCreator creator, String jarName) {
		try(BufferedWriter br = creator.buffer("runApp.bat", false)) {
			creator.write(
				br,
				"#!/bin/bash" +
				Os.WINDOWS_NEW_LINE +
				"# Linux" +
				Os.LINUX_NEW_LINE +
				"java -Dfile.encoding=UTF8 -jar " + jarName +
				Os.WINDOWS_NEW_LINE + Os.LINUX_NEW_LINE +
				"# Windows" +
				Os.WINDOWS_NEW_LINE +
				"start javaw -Dfile.encoding=UTF8 -jar " + jarName
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new RunnerGenerate(new PlainTextCreator(), args[0]);
	}
}
