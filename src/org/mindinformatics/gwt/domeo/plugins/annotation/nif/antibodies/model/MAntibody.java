package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MAntibody extends MTrustedResource {

	String vendor, cloneId, organism, type, catalog;
	
	public MAntibody(String url, String label, MGenericResource source) {
		super(url, label, source);
	}
	
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getCloneId() {
		return cloneId;
	}

	public void setCloneId(String cloneId) {
		this.cloneId = cloneId;
	}
	
	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
}
