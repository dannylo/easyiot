package org.ufrn.framework.virtualentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.ufrn.framework.exceptions.ResourceException;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.resources.AbstractCoapResource;
import org.ufrn.framework.resources.DefaultCoapInputResource;
import org.ufrn.framework.resources.DefaultCoapOutputResource;

import com.google.gson.Gson;

import br.ufrn.framework.virtualentity.resources.Resource;

public class VirtualDevice extends ComunicableEntity {

	private final int DEFAULT_DELAY = 2;

	private Identification identification;
	private List<Resource> resources;
	private IProxy proxy;
	
	private CoapServer server;
	private CoapClient client;
	private Map<String, AbstractCoapResource> mappingResources;

	public VirtualDevice(int delayForVerifyState) {
		mappingResources = new HashMap<>();
		// this.listenMyStateAndNotifier(delayForVerifyState);
	}

	public VirtualDevice() {
		mappingResources = new HashMap<>();
		// this.listenMyStateAndNotifier(DEFAULT_DELAY);
	}

	public String getDataEvent(String pathEvent) throws ResourceException {
		DefaultCoapOutputResource resource = (DefaultCoapOutputResource) mappingResources.get(pathEvent);
		if (resource != null) {
			client = new CoapClient(resource.getUrlAcess());
			CoapResponse response = client.get();
			return response.getResponseText();
		} else {
			throw new ResourceException("The resource was not defined in the discovery process");
		}
	}

	public void sendEvent(String action, Map<String, String> newValues) {
		DefaultCoapInputResource coapResource = (DefaultCoapInputResource) mappingResources.get(action);

		Gson gson = new Gson();
		String json = gson.toJson(newValues);
		// this.client = new CoapClient(actionCoap.getUrlAcess()); //??? TODO:
		// Implementar esse método para os recursos de entrada do CoAP, e testar.
		this.client.put(json, MediaTypeRegistry.APPLICATION_JSON);
	}

	@Deprecated
	public void listenExternalEntity(VirtualDevice externalEntity, String actionDescription) {
		externalEntity.registerActionListener(this, actionDescription);
	}

	// This method notify all interested entitys about our actions.
	@Deprecated
	public void listenMyStateAndNotifier(int delay) {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				VirtualDevice.this.notifyStateOfActions(VirtualDevice.this);
			}
		};

		Timer timer = new Timer();
		timer.schedule(timerTask, 0, delay * 1000);
	}
	
	public static VirtualDevice createInstance() {
		VirtualDevice instance = new VirtualDevice();
		instance.setIdentification(new Identification());
		instance.setResources(new ArrayList<>());
		return instance;
	}


	public Identification getIdentification() {
		return identification;
	}

	public void setIdentification(Identification identification) {
		this.identification = identification;
	}

	public CoapServer getServer() {
		return server;
	}

	public void setServer(CoapServer server) {
		this.server = server;
	}

	public CoapClient getClient() {
		return client;
	}

	public void setClient(CoapClient client) {
		this.client = client;
	}

	public Map<String, AbstractCoapResource> getMappingResources() {
		return mappingResources;
	}

	public IProxy getProxy() {
		return proxy;
	}

	public void setProxy(IProxy proxy) {
		this.proxy = proxy;
	}
	
	public List<Resource> getResources() {
		return resources;
	}
	
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}


}
