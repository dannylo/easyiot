package org.ufrn.framework.resources;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.ufrn.framework.proxy.implementations.UPnpProxy;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.virtualentity.VirtualEntity;

import com.google.gson.Gson;

/*The action to represent a action of specific service.*/
public class DefaultCoapOutputResource extends AbstractCoapResource{
		
	public DefaultCoapOutputResource(String name) {
		super(name);
	}
	
	public DefaultCoapOutputResource(String name, 
			IProxy proxy, 
			VirtualEntity virtualEntity, 
			String serviceDescription, 
			String actionDescription) {
		
		super(name);
		this.proxyAcess = proxy;
		this.virtualEntity = virtualEntity;
		this.serviceDescription = serviceDescription;
		this.actionDescription = actionDescription;
		this.urlAcess = PREFIX_URL.concat(this.actionDescription);
		
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		Map<String, String> arguments = new HashMap<>();
		arguments.put(UPnpProxy.SERVICE_KEY, serviceDescription);
		arguments.put(UPnpProxy.ACTION_KEY, actionDescription);
		String resultQuery = new Gson().toJson(proxyAcess.getData(virtualEntity, 
				arguments));
		exchange.respond(resultQuery);
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
		
		
	}
	
	public String getUrlAcess() {
		return urlAcess;
	}


}
