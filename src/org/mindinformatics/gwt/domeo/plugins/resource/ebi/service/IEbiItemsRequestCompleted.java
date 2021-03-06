package org.mindinformatics.gwt.domeo.plugins.resource.ebi.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IEbiItemsRequestCompleted {
	public void returnTerms(ArrayList<MLinkedResource> terms);
	public void returnTerms(int totalPages, int pageSize, int pageNumber, ArrayList<MLinkedResource> terms);
	public void reportException();
	public void reportException(String message);
}
