package org.ufrn.framework.proxy.implementations;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.californium.core.CoapServer;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionInvocation;
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
import org.ufrn.framework.database.access.Database;
import org.ufrn.framework.proxy.interfaces.IProxy;
import org.ufrn.framework.resources.DefaultCoapResource;
import org.ufrn.framework.util.ManagerFile;
import org.ufrn.framework.virtualentity.Identification;
import org.ufrn.framework.virtualentity.VirtualEntity;

@ProxyTranslate(description = "UPnP")
public class UPnpProxy implements IProxy {

	private UpnpService service;
	private Map<String, Service> mapServices = new HashMap<>();

	public UPnpProxy() {
		this.service = new UpnpServiceImpl();
	}

	private void createListenerUpnp() {
		RegistryListener listenerNetwork = new DefaultRegistryListener() {

			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
				VirtualEntity entity = new VirtualEntity();
				entity.setIdentification(new Identification());
				entity.getIdentification().setDescriptionName(device.getDisplayString());
				entity.getIdentification().setIdProtocol(device.getType().getDisplayString());
				entity.setServer(new CoapServer());

				List<String> actionsRepport = new ArrayList<>();
				for (RemoteService serviceRemote : device.getServices()) {
					mapServices.put(serviceRemote.getServiceId().getId(), serviceRemote);
					for (Action action : serviceRemote.getActions()) {
						if (!action.hasInputArguments()) {	
							actionsRepport.add(entity.getIdentification().getDescriptionName() + "-" + action.getName());
							DefaultCoapResource coap = new DefaultCoapResource(action.getName(), UPnpProxy.this, entity,
									serviceRemote.getServiceId().getId(), action.getName());
							entity.getMappingResources().put(action.getName(), coap);
							SampleCoapServer.getInstance().add(coap);
						}
					}
				}
				Database.register(entity);
				try {
					ManagerFile.createFileActionsDiscovery(actionsRepport);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		service.getRegistry().addListener(listenerNetwork);
		service.getControlPoint().search(new STAllHeader());
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
	public boolean send(VirtualEntity virtualEntity) {
		return false;
	}

	@Override
	public Map<String, String> getData(VirtualEntity virtualEntity, String serviceDescription,
			String actionDescription) {

		Service serviceUse = mapServices.get(serviceDescription);
		Action action = serviceUse.getAction(actionDescription);
		ActionInvocation actionInvocation = new ActionInvocation(action);

		new ActionCallback.Default(actionInvocation, service.getControlPoint()).run();
		HashMap<String, String> results = new HashMap<>();
		for (ActionArgument actionArgument : action.getOutputArguments()) {
			results.put(actionArgument.getName(),  String.valueOf(actionInvocation.getOutput(actionArgument).getValue()));
		}

		return results;
	}

}
