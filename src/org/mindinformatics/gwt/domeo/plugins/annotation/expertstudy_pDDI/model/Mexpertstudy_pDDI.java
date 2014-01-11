package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model;

import java.util.Set;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
@SuppressWarnings("serial")
public class Mexpertstudy_pDDI extends MTrustedResource {

	MLinkedResource  comment;
	
	public MLinkedResource getComment() {
		return comment;
	}

	public void setComment(MLinkedResource comment) {
		this.comment = comment;
	}

	Set<MLinkedResource> statementsResource;

	public Mexpertstudy_pDDI(String url, String label, MGenericResource source) {
		super(url, label, source);
	}

}
