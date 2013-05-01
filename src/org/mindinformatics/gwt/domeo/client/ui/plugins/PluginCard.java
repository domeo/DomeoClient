package org.mindinformatics.gwt.domeo.client.ui.plugins;

// A simple data type that represents a contact.
public class PluginCard {
	public final String name;
	public final String type;
	public final String subType;
	public final String version;
	public Boolean mandatory;
	public Boolean selected;

	public PluginCard(String name, String type, String subType, String version, Boolean mandatory, Boolean selected) {
		this.name = name;
		this.type = type;
		this.subType = subType;
		this.version = version;
		this.mandatory = mandatory;
		this.selected = selected;
	}
	
	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}
}