package org.mindinformatics.gwt.framework.component.resources.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A trusted resource is a resource that is provided with its source.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class __MLinkedResource extends MGenericResource implements Serializable, IsSerializable {
	
	protected String description;		// Optional (recommended for human consumption)
	protected MGenericResource source; 	// Mandatory
	
	public __MLinkedResource(){}
	
	/**
	 * Constructor should be used only by the factory.
	 * @param url			The url of the Trusted Resource
	 * @param label			The human readable label
	 * @param description	The human readable description
	 */
	public __MLinkedResource(String url, String label, String description) {
		super(url, label);
		this.description = description;
	}
	
	/**
	 * Constructor should be used only by the factory.
	 * @param url		The url of the Trusted Resource
	 * @param label		The human readable label
	 * @param source	The source as the Generic Resource
	 */
	public __MLinkedResource(String url, String label) {
		super(url, label);
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public MGenericResource getSource() {
		return source;
	}
	public void setSource(MGenericResource source) {
		this.source = source;
	}
}
