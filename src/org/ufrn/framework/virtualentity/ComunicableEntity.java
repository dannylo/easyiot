package org.ufrn.framework.virtualentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapResource;
import org.ufrn.framework.resources.AbstractCoapResource;
import org.ufrn.framework.resources.DefaultCoapOutputResource;

public abstract class ComunicableEntity {

	private List<HashMap<VirtualDevice, String>> interestedEntitys = new ArrayList<>();
	
	private Logger logger = Logger.getLogger(ComunicableEntity.class);

	public void registerActionListener(VirtualDevice entity, String actionDescription) {
		
		if (interestedEntitys == null) {
			interestedEntitys = new ArrayList<>();
		}
		HashMap<VirtualDevice, String> register = new HashMap<>();
		register.put(entity, actionDescription);
		interestedEntitys.add(register);
		logger.info("A virtual entity "+ entity.getIdentification().getDescriptionName() + " was registered as a listener.");
	}

	@Deprecated
	public void notifyStateOfActions(VirtualDevice self) {
		HashMap<String, String> mappingResults = new HashMap<String, String>();
		for (HashMap<VirtualDevice, String> register : interestedEntitys) {
			VirtualDevice entity = register.keySet().stream().findFirst().get();
			AbstractCoapResource resource = self.getMappingResources().get(register.get(entity));	
			//String responseAction = self.getDataEvent(((DefaultCoapOutputResource) resource).getUrlAcess());
			//mappingResults.put(register.get(entity), responseAction);	
			//entity.setMappingValuesListen(mappingResults);
		}
	}
	
	public List<String> getMyActionsOnListen(){
		List<String> actions = new ArrayList<>();
		interestedEntitys.forEach((k)-> actions.addAll(k.values()));
		return actions;
	}

}
