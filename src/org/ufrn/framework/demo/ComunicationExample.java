package org.ufrn.framework.demo;

import java.util.ArrayList;
import java.util.List;

import org.ufrn.framework.annotation.CheckMethod;
import org.ufrn.framework.annotation.Comunication;
import org.ufrn.framework.annotation.DiscoveryMethod;
import org.ufrn.framework.comunication.interfaces.IComunication;
import org.ufrn.framework.database.access.Database;
import org.ufrn.framework.util.ManagerResultAction;
import org.ufrn.framework.virtualentity.VirtualEntity;

@Comunication(checkDelay = 5)
public class ComunicationExample implements IComunication {

	private List<VirtualEntity> temperatureSensors = new ArrayList<>();
	private VirtualEntity termostato;
	private static final String ACTION = "GetTemperature";
	private static final String OUTPUT = "temperature";

	@DiscoveryMethod
	public void discoveryDevices() {
		temperatureSensors = Database.discovery("Temp");
		termostato = Database.discovery("Termos").get(0);
		for (VirtualEntity ve : temperatureSensors) {
			termostato.listenExternalEntity(ve, "GetTemperature");
		}
	}

	@CheckMethod
	public void check() {
		if (termostato.getMappingValuesListen() != null) {
			String value = ManagerResultAction
					.getValue(termostato.getMappingValuesListen().get(ACTION)).get(OUTPUT);
			if (value != null) {
				if (Double.parseDouble(value) <= 0.0) {
					System.err.println("IT'S VERY COLD, MODIFY TERMOSTATE STATUS!!");
				}
			}
		}
	}
}
