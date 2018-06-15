package org.ufrn.framework.proxy.implementations;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.CoapServer;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.android.AndroidUpnpServiceConfiguration;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.message.header.STAllHeader;
import org.teleal.cling.model.meta.Action;
import org.teleal.cling.model.meta.ActionArgument;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.model.meta.RemoteService;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.registry.DefaultRegistryListener;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;
import org.ufrn.framework.annotation.ProxyTranslate;
import org.ufrn.framework.coapserver.SampleCoapServer;
import org.ufrn.framework.database.access.DeviceManager;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.resources.DefaultCoapInputResource;
import org.ufrn.framework.resources.DefaultCoapOutputResource;
import org.ufrn.framework.util.ActionUpnpDefault;
import org.ufrn.framework.util.ManagerFile;
import org.ufrn.framework.virtualentity.Identification;
import org.ufrn.framework.virtualentity.VirtualDevice;

import br.ufrn.framework.virtualentity.resources.Resource;


@ProxyTranslate(description = "UPnP")
public class UPnpProxy implements IProxy {

	private UpnpService upnpService;
	private Map<String, Service> mapServices = new HashMap<>();
	private List<String> actionsRepport = new ArrayList<>();
	

	public static final String ACTION_KEY = "ACTION_KEY";
	public static final String SERVICE_KEY = "SERVICE_KEY";
	

	private boolean successOperation = false;

	public UPnpProxy() {
		this.upnpService = new UpnpServiceImpl();
	}

	private void createListenerUpnp() {
		RegistryListener listenerNetwork = new DefaultRegistryListener() {

			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
				VirtualDevice entity = VirtualDevice.createInstance();
				entity.getIdentification().setDescriptionName(device.getDisplayString());
				entity.getIdentification().setIdProtocol(device.getType().getDisplayString());
				entity.getIdentification().setMainFeature(device.getDetails().getModelDetails().getModelDescription());
				entity.setServer(new CoapServer());
				
				for (RemoteService serviceRemote : device.getServices()) {
					Resource resource = new Resource();
					resource.setDescription(serviceRemote.getServiceId().getId());
					mapServices.put(serviceRemote.getServiceId().getId(), serviceRemote);
					for (Action action : serviceRemote.getActions()) {
						if (!action.hasInputArguments()) {
							StringBuilder actionName = new StringBuilder(action.getName());
							actionName.append(":");
							Arrays.asList(action.getOutputArguments())
									.forEach(argument -> actionName.append(argument + ","));
							resource.getAction().add(actionName.toString());

							DefaultCoapOutputResource coap = new DefaultCoapOutputResource(action.getName(),
									UPnpProxy.this, entity, serviceRemote.getServiceId().getId(), action.getName());
							entity.getMappingResources().put(action.getName(), coap);
							SampleCoapServer.getInstance().add(coap);
						} else if(action.hasInputArguments()){
							StringBuilder actionName = new StringBuilder(action.getName());		
							actionName.append("(");
							Arrays.asList(action.getInputArguments())
									.forEach(argument -> actionName.append(argument + ","));
							actionName.append(")");
							resource.getAction().add(actionName.toString());
							
							DefaultCoapInputResource coap = new DefaultCoapInputResource(action.getName(),
									UPnpProxy.this, 
									entity, 
									serviceRemote.getServiceId().getId(), 
									action.getName());
							entity.getMappingResources().put(action.getName(), coap);
							SampleCoapServer.getInstance().add(coap);
						}
					}
					entity.getResources().add(resource);
				}
				DeviceManager.register(entity);
				try {
					ManagerFile.createFileActionsDiscovery(actionsRepport);
					ManagerFile.print("actions_discovered");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		upnpService.getRegistry().addListener(listenerNetwork);
		upnpService.getControlPoint().search(new STAllHeader());
	}

	@Override
	public void discoveryAll() {
		this.createListenerUpnp();
		try {
			Thread.sleep(5000);
			SampleCoapServer.getInstance().start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Map<String, String> getData(VirtualDevice virtualEntity, Map<String, String> mappingArguments) {
		Service serviceUse = mapServices.get(mappingArguments.get(UPnpProxy.SERVICE_KEY));
		Action action = serviceUse.getAction(mappingArguments.get(UPnpProxy.ACTION_KEY));
		ActionInvocation actionInvocation = new ActionInvocation(action);

		new ActionCallback.Default(actionInvocation, upnpService.getControlPoint()).run();
		HashMap<String, String> results = new HashMap<>();
		for (ActionArgument actionArgument : action.getOutputArguments()) {
			results.put(actionArgument.getName(),
					String.valueOf(actionInvocation.getOutput(actionArgument).getValue()));
		}

		return results;
	}

	@Override
	public boolean send(VirtualDevice virtualEntity, Map<String, String> mappingArguments,
			Map<String, String> mappingValues) throws InterruptedException {
		Service serviceUse = mapServices.get(mappingArguments.get(UPnpProxy.SERVICE_KEY));
		Action action = serviceUse.getAction(mappingArguments.get(UPnpProxy.ACTION_KEY));
		//mappingValues deve mapear todos os argumentos e novos valores que serão submetidos ao dispositivo para a mudança.
		for (String argument : mappingValues.keySet()) {
			ActionUpnpDefault actionInvocation = new ActionUpnpDefault(action, argument, mappingValues.get(argument));

			boolean success = false;

			this.upnpService.getControlPoint().execute(new ActionCallback(actionInvocation) {
				@Override
				public void success(ActionInvocation arg0) {
					successOperation = true;
				}

				@Override
				public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
					successOperation = false;
				}
			});

			Thread.sleep(1000);

		}

		return successOperation;
	}

}
