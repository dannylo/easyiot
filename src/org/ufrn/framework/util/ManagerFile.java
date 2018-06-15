package org.ufrn.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import org.ufrn.framework.coapserver.SampleCoapServer;
import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.virtualentity.VirtualDevice;

import br.ufrn.framework.virtualentity.resources.Resource;

public class ManagerFile {

	public static final String ANDROID_TYPE = "ANDROID_TYPE";

	/* The list should to admit name entity - action pattern. */
	public static void createFileActionsDiscovery(List<String> mappingActions) throws IOException {
		File file = new File("actions_discovered");
		FileWriter fw = new FileWriter(file);
		PrintWriter printer = new PrintWriter(fw);
		printer.println("COAP SERVER: " + SampleCoapServer.getInstance());
		printer.println("ID | DEVICE | ACTION(ENDPOINT COAP)");
		printer.println("");
		for (VirtualDevice device : DeviceManager.getRegistred()) {
			for (Resource resource : device.getResources()) {
				printer.println("RESOURCE: " + resource.getDescription());
				printer.println("");
				for (String actionName : resource.getAction()) {

					printer.println(device.getIdentification().getId() + " | "
							+ device.getIdentification().getDescriptionName() + " | " + actionName);
				}
				printer.println("");
			}
		}
		printer.close();
		fw.close();
	}

	public static void createFileActionsDiscovery(List<String> mappingActions, File root, String type)
			throws IOException {
		if (type.equals(ANDROID_TYPE)) {
			FileWriter fw = new FileWriter(root);
			PrintWriter printer = new PrintWriter(fw);
			printer.println("COAP SERVER: " + SampleCoapServer.getInstance());
			printer.println("ID | DEVICE | ACTION(ENDPOINT COAP)");
			printer.println("");
			for (VirtualDevice device : DeviceManager.getRegistred()) {
				for (Resource resource : device.getResources()) {
					printer.println("RESOURCE: " + resource.getDescription());
					printer.println("");
					for (String actionName : resource.getAction()) {

						printer.println(device.getIdentification().getId() + " | "
								+ device.getIdentification().getDescriptionName() + " | " + actionName);
					}
					printer.println("");
				}
			}
			printer.close();
			fw.close();
		}
	}

	public static void print(String file) {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
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
}
