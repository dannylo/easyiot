package org.ufrn.framework.demo;

import java.util.HashMap;

import org.ufrn.framework.core.Core;
import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.exceptions.ResourceException;
import org.ufrn.framework.virtualentity.VirtualDevice;

import br.ufrn.framework.virtualentity.resources.ResourceManager;

public class DemoMain {
	
	public static void main(String[] args) {
		Core.start();
		
		VirtualDevice sensor1 = DeviceManager.discovery("Temp").get(0);
		VirtualDevice termos = DeviceManager.discovery("Termo").get(0);
		
		HashMap<String, String> newValues = new HashMap<>();
		newValues.put(ResourceManager.ConfigureTemperature.ParamInSetTemperatureValue, "25");
		
		try {
			//show the temperature value of sensor1
			System.out.println(sensor1
					.getDataEvent(ResourceManager.VerifyTemperature.GetTemperature));
			
			//to configure temperature value in termostate to 25 degree.
			termos.sendEvent(ResourceManager.ConfigureTemperature.SetTemperatureValue, newValues);
			
		} catch (ResourceException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
