package org.ufrn.framework.proxy.interfaces;

import java.util.Map;

import org.ufrn.framework.virtualentity.VirtualDevice;

public interface IProxy {

	void discoveryAll();
	
	boolean send(VirtualDevice virtualEntity, 
			Map<String, String> mappingArguments,
			Map<String, String> mappingValues) throws InterruptedException;
	
	Map<String, String> getData(VirtualDevice virtualEntity, 
			Map<String, String> mappingArguments);
	
	
}
