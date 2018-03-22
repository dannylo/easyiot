package org.ufrn.framework.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.ufrn.framework.core.Core;
import org.ufrn.framework.proxy.implementations.UPnpProxy;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.virtualentity.VirtualDevice;

import com.google.gson.Gson;

public class DefaultCoapInputResource extends AbstractCoapResource {

	public DefaultCoapInputResource(String name) {
		super(name);
	}

	public DefaultCoapInputResource(String name, IProxy proxy, VirtualDevice virtualEntity, String serviceDescription,
			String actionDescription) {

		super(name);
		this.proxyAcess = proxy;
		this.virtualEntity = virtualEntity;
		this.serviceDescription = serviceDescription;
		this.actionDescription = actionDescription;
		this.urlAcess = PREFIX_URL.concat(this.actionDescription);

	}

	@Override
	public void handlePUT(CoapExchange exchange) {

		Map<String, String> arguments = new HashMap<>();
		arguments.put(UPnpProxy.SERVICE_KEY, serviceDescription);
		arguments.put(UPnpProxy.ACTION_KEY, actionDescription);
		byte[] payload = exchange.getRequestPayload();

		Map<String, String> mappingNewValues;
		try {
			String value = new String(payload, "UTF-8");
			Gson gson = new Gson();
			mappingNewValues = gson.fromJson(value, Map.class);
			boolean success = this.proxyAcess.send(virtualEntity, arguments, mappingNewValues);
			Core.getLogger().info("The result of put method for " + actionDescription + " is ");
			exchange.respond(ResponseCode.CHANGED, value);
		} catch (Exception e) {
			e.printStackTrace();
			exchange.respond(ResponseCode.BAD_REQUEST, "Invalid String");
		}
	}


}
