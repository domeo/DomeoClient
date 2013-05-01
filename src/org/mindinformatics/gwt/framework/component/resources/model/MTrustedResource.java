package org.mindinformatics.gwt.framework.component.resources.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A trusted resource is a resource that is provided with its source.
 * Therefore the source is mandatory.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MTrustedResource extends MLinkedResource implements Serializable, IsSerializable {
	
	/**
	 * Constructor should be used only by the factory.
	 * @param url		The url of the Trusted Resource
	 * @param label		The human readable label
	 * @param source	The source as the Generic Resource
	 */
	public MTrustedResource(String url, String label, MGenericResource source) {
		super(url, label);
		this.source = source;
	}
}
