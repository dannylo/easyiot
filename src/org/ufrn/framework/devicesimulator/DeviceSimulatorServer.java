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


public class DeviceSimulatorServer implements Runnable {
	
	public static void main(String[] args) {
		
		Thread serverThread = new Thread(new DeviceSimulatorServer());
        serverThread.setDaemon(false);
        serverThread.start();
		
	}

	@Override
	public void run() {
		DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier("Temperature Sensor"));
		 
		DeviceType type = new UDADeviceType("TemperatureSensor", 1);
		
		DeviceDetails details = new DeviceDetails("Demo Temperature Sensor",
				new ManufacturerDetails("Enterprise"),
                new ModelDetails(
                        "SenTempMod1",
                        "A sensor for measurable temperature.",
                        "v1"
                ));
	
		LocalService<VerifyTemperature> verifyService = new AnnotationLocalServiceBinder()
				.read(VerifyTemperature.class);
		verifyService.setManager(new DefaultServiceManager(verifyService, VerifyTemperature.class));
		
		try {
			LocalDevice temperatureSensor = new LocalDevice(identity, type, details, verifyService);
			final UpnpService upnpService = new UpnpServiceImpl();
			   Runtime.getRuntime().addShutdownHook(new Thread() {
	               @Override
	               public void run() {
	                   upnpService.shutdown();
	               }
	           });

	           upnpService.getRegistry().addDevice(
	                   temperatureSensor
	           );
	           	         
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	
		
		
		
	}}
