package org.ufrn.framework.virtualentity;

public class Identification {
	
	private Long id;
	private String idProtocol;
	private String descriptionName;
	private String networkInfo;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescriptionName() {
		return descriptionName;
	}
	
	public void setDescriptionName(String descriptionName) {
		this.descriptionName = descriptionName;
	}
	
	public String getNetworkInfo() {
		return networkInfo;
	}
	
	public void setNetworkInfo(String networkInfo) {
		this.networkInfo = networkInfo;
	}

	public String getIdProtocol() {
		return idProtocol;
	}

	public void setIdProtocol(String idProtocol) {
		this.idProtocol = idProtocol;
	}
	 
	
	
	
}
