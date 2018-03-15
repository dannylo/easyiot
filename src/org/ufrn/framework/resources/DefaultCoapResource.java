package org.ufrn.framework.resources;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.virtualentity.VirtualEntity;

import com.google.gson.Gson;

/*The action to represent a action of specific service.*/
public class DefaultCoapResource extends AbstractCoapResource{
		
	public DefaultCoapResource(String name) {
		super(name);
	}
	
	public DefaultCoapResource(String name, 
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
		String resultQuery = new Gson().toJson(proxyAcess.getData(virtualEntity, 
				serviceDescription, 
				actionDescription));
		exchange.respond(resultQuery);
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {
		
		
	}
	
	public String getUrlAcess() {
		return urlAcess;
	}


}
