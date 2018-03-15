package org.ufrn.framework.devicesimulator;

import org.teleal.cling.binding.annotations.UpnpAction;
import org.teleal.cling.binding.annotations.UpnpInputArgument;
import org.teleal.cling.binding.annotations.UpnpService;
import org.teleal.cling.binding.annotations.UpnpServiceId;
import org.teleal.cling.binding.annotations.UpnpServiceType;
import org.teleal.cling.binding.annotations.UpnpStateVariable;

@UpnpService(serviceId = @UpnpServiceId("ConfigureTemperature"), 
serviceType = @UpnpServiceType(value = "Controller", version=1))
public class ConfigureTemperature {

	@UpnpStateVariable(defaultValue = "0", sendEvents = false)
	private int temperatureValue;
	
	@UpnpAction
	public void setTemperatureValue(@UpnpInputArgument(name = "TemperatureValue") int temperatureValue) {
		this.temperatureValue = temperatureValue;
		System.out.println("NEW TEMPERATURE CONFIG - " + this.temperatureValue);
	}
	
}
