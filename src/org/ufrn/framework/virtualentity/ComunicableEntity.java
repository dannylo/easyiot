package org.ufrn.framework.virtualentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.californium.core.CoapResource;
import org.ufrn.framework.resources.AbstractCoapResource;
import org.ufrn.framework.resources.DefaultCoapResource;

public abstract class ComunicableEntity {

	// cada registro guarda a entidade interessada e a ação que ela deverá escutar.
	private List<HashMap<VirtualEntity, String>> interestedEntitys = new ArrayList<>();
	
	private Logger logger = Logger.getLogger(ComunicableEntity.class);

	public void registerActionListener(VirtualEntity entity, String actionDescription) {
		
		if (interestedEntitys == null) {
			interestedEntitys = new ArrayList<>();
		}
		HashMap<VirtualEntity, String> register = new HashMap<>();
		register.put(entity, actionDescription);
		interestedEntitys.add(register);
		logger.info("A virtual entity "+ entity.getIdentification().getDescriptionName() + " was registered as a listener.");
	}

	public void notifyStateOfActions(VirtualEntity self) {
		// resultados mapeados: Ação ouvida, e resultado da ação.
		HashMap<String, String> mappingResults = new HashMap<String, String>();
		for (HashMap<VirtualEntity, String> register : interestedEntitys) {
			VirtualEntity entity = register.keySet().stream().findFirst().get();
			AbstractCoapResource resource = self.getMappingResources().get(register.get(entity));	
			String responseAction = self.launchEvent(((DefaultCoapResource) resource).getUrlAcess());
			mappingResults.put(register.get(entity), responseAction);	
			entity.setMappingValuesListen(mappingResults);
		}
	}
	
	public List<String> getMyActionsOnListen(){
		List<String> actions = new ArrayList<>();
		interestedEntitys.forEach((k)-> actions.addAll(k.values()));
		return actions;
	}

}
