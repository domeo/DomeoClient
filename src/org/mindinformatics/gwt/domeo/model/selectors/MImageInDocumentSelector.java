package org.mindinformatics.gwt.domeo.model.selectors;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MImageInDocumentSelector extends MSelector {
	
	public static final String TYPE = "domeo:ImageInDocumentSelector";
	
	private MGenericResource context; 
	
	public MGenericResource getContext() {
		return context;
	}

	public void setContext(MGenericResource context) {
		this.context = context;
	}

	public String getSelectorType() {
		return TYPE;
	}
}
