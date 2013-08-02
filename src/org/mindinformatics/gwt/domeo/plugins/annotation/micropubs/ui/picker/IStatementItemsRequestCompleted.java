package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IStatementItemsRequestCompleted {
	public void returnMicroPublicationObject(ArrayList<MMicroPublicationAnnotation> reference);
	public void microPublicationObjectNotFound();
	public void microPublicationObjectNotFound(String message);	
}
