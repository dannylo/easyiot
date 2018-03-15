package org.ufrn.framework.devicesimulator;

import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.teleal.cling.model.DefaultServiceManager;
import org.teleal.cling.model.meta.DeviceDetails;
import org.teleal.cling.model.meta.DeviceIdentity;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.LocalService;
import org.teleal.cling.model.meta.ManufacturerDetails;
import org.teleal.cling.model.meta.ModelDetails;
import org.teleal.cling.model.types.DeviceType;
import org.teleal.cling.model.types.UDADeviceType;
import org.teleal.cling.model.types.UDN;

public class TermostateSimulator implements Runnable{
	
	public static void main(String[] args) {
		
		Thread serverThread = new Thread(new TermostateSimulator());
        serverThread.setDaemon(false);
        serverThread.start();
			
	}

	@Override
	public void run() {
		DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier("Termostate"));
		 
		DeviceType type = new UDADeviceType("TermostateController", 1);
		
		DeviceDetails details = new DeviceDetails("Demo Temperature Sensor",
				new ManufacturerDetails("Enterprise"),
                new ModelDetails(
                        "TermostateContrMod1",
                        "The controlle envieroment temperature",
                        "v1"
                ));
	
		LocalService<ConfigureTemperature> configureService = new AnnotationLocalServiceBinder()
				.read(ConfigureTemperature.class);
		configureService.setManager(new DefaultServiceManager(configureService, ConfigureTemperature.class));
		
		try {
			LocalDevice termostate = new LocalDevice(identity, type, details, configureService);
			final UpnpService upnpService = new UpnpServiceImpl();
			   Runtime.getRuntime().addShutdownHook(new Thread() {
	               @Override
	               public void run() {
	                   upnpService.shutdown();
	               }
	           });

	           upnpService.getRegistry().addDevice(
	        		   termostate
	           );
	           	         
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
	}

}
