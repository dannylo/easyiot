package org.ufrn.framework.virtualentity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.resources.AbstractCoapResource;
import org.ufrn.framework.resources.DefaultCoapInputResource;
import org.ufrn.framework.resources.DefaultCoapOutputResource;

import com.google.gson.Gson;

public class VirtualEntity extends ComunicableEntity  {	
	
	private final int DEFAULT_DELAY = 2;	
	
	private Identification identification;
	private CoapServer server;
	private CoapClient client;
	private Map<String, AbstractCoapResource> mappingResources;
	private IProxy proxy;
	private HashMap<String, String> mappingValuesListen;
		
	public VirtualEntity(int delayForVerifyState) {
		mappingResources = new HashMap<>();
		mappingValuesListen = new HashMap<>();
		identification = new Identification();
		this.listenMyStateAndNotifier(delayForVerifyState);		
	}
	
	public VirtualEntity() {
		mappingResources = new HashMap<>();
		mappingValuesListen = new HashMap<>();
		identification = new Identification();
		this.listenMyStateAndNotifier(DEFAULT_DELAY);		
	}
	
	public String launchEvent(String pathEvent) {
		client = new CoapClient(pathEvent);
		CoapResponse response = client.get();
		return response.getResponseText();		
	}
	
	public void sendEvent(String action, Map<String, String> newValues) {
		DefaultCoapInputResource coapResource = (DefaultCoapInputResource) mappingResources.get(action);
		
		Gson gson = new Gson();		
		String json = gson.toJson(newValues);
	//	this.client = new CoapClient(actionCoap.getUrlAcess()); //??? TODO: Implementar esse método para os recursos de entrada do CoAP, e testar.
		this.client.put(json, MediaTypeRegistry.APPLICATION_JSON);
	}
		
    public void listenExternalEntity(VirtualEntity externalEntity, String actionDescription) {
    	externalEntity.registerActionListener(this, actionDescription);
    }
    
	//This method notify all interested entitys about our actions.
	public void listenMyStateAndNotifier(int delay) {
		TimerTask timerTask = new TimerTask() {			
			@Override
			public void run() {
				VirtualEntity.this.notifyStateOfActions(VirtualEntity.this);
			}
		};		

		Timer timer = new Timer();
		timer.schedule(timerTask ,0,  delay*1000);
	}
		
	public void setMappingValuesListen(HashMap<String, String> mappingValuesListen) {
		this.mappingValuesListen = mappingValuesListen;
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
	
	public HashMap<String, String> getMappingValuesListen() {
		return mappingValuesListen;
	}
	
	

}
