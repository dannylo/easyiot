package org.ufrn.framework.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.virtualentity.VirtualDevice;

import br.ufrn.framework.virtualentity.resources.Resource;

public class ManagerFile {

	/* The list should to admit name entity - action pattern. */
	public static void createFileActionsDiscovery(List<String> mappingActions) throws IOException {
		File file = new File("actions_discovered");
		FileWriter fw = new FileWriter(file);
		PrintWriter printer = new PrintWriter(fw);
		printer.println("ID | DEVICE | ACTION");
		printer.println("");
		for (VirtualDevice device : DeviceManager.getRegistred()) {			
			for (Resource resource : device.getResources()) {
				printer.println("RESOURCE: " + resource.getDescription());
				printer.println("");
				for(String actionName: resource.getAction()) {

					printer.println(device.getIdentification().getId() 
							+ " | " + device.getIdentification().getDescriptionName() + 
							" | " + actionName);
				}
				printer.println("");
			}
		}
		printer.close();
		fw.close();
	}
}
