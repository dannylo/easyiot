package org.ufrn.framework.devicesimulator;

import org.teleal.cling.binding.annotations.UpnpAction;
import org.teleal.cling.binding.annotations.UpnpService;
import org.teleal.cling.binding.annotations.UpnpServiceId;
import org.teleal.cling.binding.annotations.UpnpServiceType;
import org.teleal.cling.binding.annotations.UpnpStateVariable;

import org.teleal.cling.binding.annotations.UpnpOutputArgument;

@UpnpService(serviceId = @UpnpServiceId("VerifyTemperature"), serviceType = @UpnpServiceType(value = "Temperature", version=1))
public class VerifyTemperature {	

	@UpnpStateVariable(defaultValue = "18", sendEvents = false)
	private double temperature;
	
	@UpnpAction(out = @UpnpOutputArgument(name = "temperature"))
	public double getTemperature() {
		return temperature;
	}
}
