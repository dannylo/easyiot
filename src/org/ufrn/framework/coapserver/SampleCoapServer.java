package org.ufrn.framework.coapserver;

import org.eclipse.californium.core.CoapServer;

public class SampleCoapServer {

	private static CoapServer coapServer;
	
	private SampleCoapServer() {		
	}
	
	public static synchronized CoapServer getInstance() {
		if(coapServer == null) {
			coapServer = new CoapServer();
		}
		return coapServer;
	}
}
