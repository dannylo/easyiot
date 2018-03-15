package org.ufrn.framework.database.access;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.ufrn.framework.virtualentity.VirtualEntity;

public class Database {

	private static long INDEX_CONTROL = 0L;

	private static List<VirtualEntity> registred;
	
	private static Logger logger = Logger.getLogger(Database.class);

	public static List<VirtualEntity> getRegistred() {
		return registred;
	}

	public static void register(VirtualEntity virtualEntity) {
		if (registred == null) {
			registred = new ArrayList<>();
		}
		
		logger.info("Virtual entity discovered and registered: " + virtualEntity.getIdentification().getDescriptionName());		
		virtualEntity.getIdentification().setId(INDEX_CONTROL);
		registred.add(virtualEntity);
	}

	public static List<VirtualEntity> discovery(String description) {
		List<VirtualEntity> result = registred.stream()
				.filter(entity -> entity.getIdentification().getDescriptionName().contains(description))
				.collect(Collectors.toList());
		return result;
	}

	public static VirtualEntity discovery(int id) {
		List<VirtualEntity> result = registred.stream().filter(entity -> entity.getIdentification().getId() == id)
				.collect(Collectors.toList());
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}
}
