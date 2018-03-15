package org.ufrn.framework.demo;

import java.util.ArrayList;
import java.util.List;

import org.teleal.cling.model.resource.ServiceEventSubscriptionResource;
import org.ufrn.framework.annotation.CheckMethod;
import org.ufrn.framework.annotation.Comunication;
import org.ufrn.framework.annotation.DiscoveryMethod;
import org.ufrn.framework.comunication.interfaces.IComunication;
import org.ufrn.framework.database.access.Database;
import org.ufrn.framework.virtualentity.VirtualEntity;


@Comunication(checkDelay=5)
public class ComunicationExample implements IComunication{
	
	private List<VirtualEntity> temperatureSensors = new ArrayList<>();
	private VirtualEntity termostato;
	private static final String ACTION = "GetTemperature";
	
	@DiscoveryMethod
	public void discoveryDevices() {
		temperatureSensors = Database.discovery("Temp");
		termostato = Database.discovery("Termos").get(0);		
		for(VirtualEntity ve: temperatureSensors) {
			termostato.listenExternalEntity(ve, "GetTemperature");
		}
	}
	
	@CheckMethod
	public void check() {		
		System.out.println("DATA READING: " + termostato.getMappingValuesListen().get(ACTION));	
	}
}
