package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class MSPL_DDI extends MTrustedResource {

	MLinkedResource  comment;
	
	public MLinkedResource getComment() {
		return comment;
	}

	public void setComment(MLinkedResource comment) {
		this.comment = comment;
	}

	Set<MLinkedResource> statementsResource;

	public MSPL_DDI(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
