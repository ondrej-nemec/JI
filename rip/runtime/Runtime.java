package common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Runtime {
	public Runtime() {
		//first wax
		Runtime.getRuntime().exec(path-to-script);
		/********************************/
		// second way

		String cmd = "ls -al";
		Runtime run = Runtime.getRuntime();
		Process pr = run.exec(cmd);
		pr.waitFor();
		
		// this is in PlaintextReaderu
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		while ((line=buf.readLine())!=null) {
			System.out.println(line);
		}
		/********************************/
		//third way
		//how to run command from CLI and get resutl 
		
		/*Process p =*/ Runtime.getRuntime().exec(  //spusti skriptovy soubor, ne prikaz
				"run.bat"
				//C:/Users/ledvinka/workspace/install/instalation/
				);
		
		Runtime rt = Runtime.getRuntime();
		String[] commands = {"run.bat","-get t"};
		Process proc = rt.exec(commands);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		String s = null;
		while ((s = stdInput.readLine()) != null) {
		    System.out.println(s);
		}
		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
		    System.out.println(s);
		}
	}

}
