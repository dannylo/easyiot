package org.ufrn.framework.proxy.interfaces;

import java.util.Map;

import org.ufrn.framework.virtualentity.VirtualEntity;

public interface IProxy {

	void discoveryAll();
	
	boolean send(VirtualEntity virtualEntity, 
			Map<String, String> mappingArguments,
			Map<String, String> mappingValues) throws InterruptedException;
	
	Map<String, String> getData(VirtualEntity virtualEntity, 
			Map<String, String> mappingArguments);
	
	
}
