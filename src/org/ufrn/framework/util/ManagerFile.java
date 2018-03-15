package org.ufrn.framework.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class ManagerFile {

	/* The list should to admit name entity - action pattern.*/
	public static void createFileActionsDiscovery(List<String> mappingActions) throws IOException {
		File file = new File("actions_discovered");
		FileWriter fw = new FileWriter(file);
		PrintWriter printer = new PrintWriter(fw);
		printer.println("Device -> Action");
		for(String actionMapping: mappingActions) {
			String[] breakString = actionMapping.split("-");
			printer.println(breakString[0] + " -> "+ breakString[1]);
		}
		printer.close();
		fw.close();
	}
}
