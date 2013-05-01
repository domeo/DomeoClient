package org.mindinformatics.gwt.domeo.plugins.resource.nif.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface INifDataRequestCompleted {
	public void returnData(ArrayList<MGenericResource> data);
	public void returnData(int totalPages, int pageSize, int pageNumber, ArrayList<MGenericResource> data);
	public void reportException();
	public void reportException(String message);
}
