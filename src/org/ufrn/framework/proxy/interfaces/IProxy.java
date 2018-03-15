package org.ufrn.framework.proxy.interfaces;

import java.util.Map;

import org.ufrn.framework.virtualentity.VirtualEntity;

public interface IProxy {

	void discoveryAll();
	
	boolean send(VirtualEntity virtualEntity, String service, String actionConfiguration);
	
	Map<String, String> getData(VirtualEntity virtualEntity, String serviceDescription, String actionDescription);
	
	
}
