package br.ufrn.framework.virtualentity.resources;

import java.util.ArrayList;
import java.util.List;

public class Resource {

	private String description;
	private List<String> actions;
	
	public Resource() {
		actions = new ArrayList<>();
	}
	
	public List<String> getAction() {
		return actions;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
