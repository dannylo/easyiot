package br.ufrn.framework.aspects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public aspect VirtualDeviceAspect {

	private static final String FILE = "actions_discovered";

	pointcut showResources() : execution(* org.ufrn.framework.proxy.interfaces.IProxy.discoveryAll(..));		

	after() returning() : showResources(){
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(FILE));
			String line = null;
			while ((line = buffer.readLine()) != null) {
				System.out.println(line);
			}

			buffer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String nameWithConstant(String line) {
		String constantName = "";
		constantName = line.trim().replace(" ", "_");
		constantName = constantName.toUpperCase();
		return constantName;
	}

}
