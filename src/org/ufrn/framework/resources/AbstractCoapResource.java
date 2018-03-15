package org.ufrn.framework.resources;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.virtualentity.VirtualEntity;

public abstract class AbstractCoapResource extends CoapResource {

	protected static final String PREFIX_URL = "coap://localhost/";

	protected IProxy proxyAcess;
	protected VirtualEntity virtualEntity;
	protected String serviceDescription;
	protected String actionDescription;
	protected String urlAcess;

	public AbstractCoapResource(String name) {
		super(name);
	}
	
	protected String getUrlAcess() {
		return urlAcess;
	}

}
