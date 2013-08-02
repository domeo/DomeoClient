package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.picker;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IStatementsSearchObjectContainer {
	public void addMicroPublicationObject(MMicroPublicationAnnotation reference);
	public ArrayList<MMicroPublicationAnnotation> getMicroPublicationObjects();
	public ArrayList<MMicroPublicationAnnotation> getMicroPublicationAnnotations();
}
