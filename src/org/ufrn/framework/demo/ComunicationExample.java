package org.ufrn.framework.demo;

import java.util.ArrayList;
import java.util.List;

import org.ufrn.framework.annotation.CheckMethod;
import org.ufrn.framework.annotation.Comunication;
import org.ufrn.framework.annotation.DiscoveryMethod;
import org.ufrn.framework.comunication.interfaces.IComunication;
import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.util.ManagerResultAction;
import org.ufrn.framework.virtualentity.VirtualDevice;

@Comunication(checkDelay = 5)
public class ComunicationExample implements IComunication {

	private List<VirtualDevice> temperatureSensors = new ArrayList<>();
	private VirtualDevice termostato;
	private static final String ACTION = "GetTemperature";
	private static final String OUTPUT = "temperature";

	@DiscoveryMethod
	public void discoveryDevices() {
		temperatureSensors = DeviceManager.discovery("Temp");
		termostato = DeviceManager.discovery("Termos").get(0);
		for (VirtualDevice ve : temperatureSensors) {
			termostato.listenExternalEntity(ve, "GetTemperature");
		}
	}

//	@CheckMethod
//	public void check() {
//		if (termostato.getMappingValuesListen() != null) {
//			if (termostato.getMappingValuesListen().get(ACTION) != null) {
//				String value = ManagerResultAction.getValue(termostato.getMappingValuesListen().get(ACTION))
//						.get(OUTPUT);
//				if (value != null) {
//					if (Double.parseDouble(value) <= 0.0) {
//						System.err.println("IT'S VERY COLD, MODIFY TERMOSTATE STATUS!!");
//					}
//				}
//			}
//		}
//	}
}
