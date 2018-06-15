package org.ufrn.framework.database.access;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.ufrn.framework.virtualentity.VirtualDevice;

/**
 * @author Dannylo Johnathan
 */
public class DeviceManager {

	private static long INDEX_CONTROL = 1L;

	private static Set<VirtualDevice> registred;

	// private static Logger logger = Logger.getLogger(DeviceManager.class);

	public static Set<VirtualDevice> getRegistred() {
		return registred;
	}

	public static void register(VirtualDevice virtualEntity) {
		if (registred == null) {
			registred = new HashSet<>();
		}

		// logger.info("Virtual entity discovered and registered: " +
		// virtualEntity.getIdentification().getDescriptionName());
		System.out.println(
				"Virtual entity discovered and registered: " + virtualEntity.getIdentification().getDescriptionName());
		virtualEntity.getIdentification().setId(INDEX_CONTROL);
		registred.add(virtualEntity);
		INDEX_CONTROL++;
	}
	
	public static Set<VirtualDevice> discoveryAll() {
		return registred;
	}

	public static List<VirtualDevice> discoveryByFeature(String feature) {
		// List<VirtualDevice> result = registred.stream()
		// .filter(entity ->
		// entity.getIdentification().getDescriptionName().contains(description))
		// .collect(Collectors.toList());

		List<VirtualDevice> result = new ArrayList<>();
		for (VirtualDevice virtualDevice : registred) {
			if (virtualDevice.getIdentification().getMainFeature().contains(feature)) {
				result.add(virtualDevice);
			}
		}

		return result;
	}
		

	public static List<VirtualDevice> discovery(String description) {
		// List<VirtualDevice> result = registred.stream()
		// .filter(entity ->
		// entity.getIdentification().getDescriptionName().contains(description))
		// .collect(Collectors.toList());

		List<VirtualDevice> result = new ArrayList<>();
		for (VirtualDevice virtualDevice : registred) {
			if (virtualDevice.getIdentification().getDescriptionName().contains(description)) {
				result.add(virtualDevice);
			}
		}

		return result;
	}

	public static VirtualDevice discovery(int id) {
		// List<VirtualDevice> result = registred.stream().filter(entity ->
		// entity.getIdentification().getId() == id)
		// .collect(Collectors.toList());

		List<VirtualDevice> result = new ArrayList<>();
		for (VirtualDevice virtualDevice : registred) {
			if (virtualDevice.getIdentification().getId() == id) {
				result.add(virtualDevice);
			}
		}

		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

}
