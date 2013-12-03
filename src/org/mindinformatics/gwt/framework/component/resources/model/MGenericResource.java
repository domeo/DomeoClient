package org.mindinformatics.gwt.framework.component.resources.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A generic resource is just a URL or more generally a URI. Without a URI
 * the resource cannot be created. The URI could be also a URL or a URN.
 * The label allows to record a human readable short description of the URI.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MGenericResource implements Serializable, IsSerializable {

	public static final String PARAM_URI_READONLY = "URI Read Only";
	public static final String TITLE_URI_READONLY = "Title Read Only";
	
	protected String url;	// Mandatory
	protected String label; // Optional (recommended for human consumption)
	
	public MGenericResource() {}
	
	public MGenericResource(String url, String label) {
		this.url = url;
		this.label = label;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	 public int hashCode() {
		    return url.hashCode();
		  }
	
	public boolean equals(Object obj) {
	    //null instanceof Object will always return false
	    if (!(obj instanceof MGenericResource))
	      return false;
	    if (obj == this)
	      return true;
	    return  this.url.equals(((MGenericResource) obj).url);
	  }
}
