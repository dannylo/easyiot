package org.ufrn.framework.demo;

import java.util.HashMap;

import org.ufrn.framework.core.Core;
import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.exceptions.ResourceException;
import org.ufrn.framework.virtualentity.VirtualDevice;

import br.ufrn.framework.virtualentity.resources.ResourceManager;

public class DemoMain {
	
	private static final String ACTION = "GetTemperature";
	private static final String DEVICE = "Temp";
	
	public static void main(String[] args) {
		Core.start();
		
		VirtualDevice router = DeviceManager.discovery(DEVICE).get(0);
		HashMap<String, String> newValues = new HashMap<>();
		newValues.put("TemperatureValue", "25");
		
		try {
			System.out.println(router.getDataEvent(ACTION));
		} catch (ResourceException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
