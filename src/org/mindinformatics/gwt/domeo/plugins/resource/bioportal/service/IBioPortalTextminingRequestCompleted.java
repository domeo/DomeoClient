package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IBioPortalTextminingRequestCompleted {
	public void returnTextminingResults(JsAnnotationSet set);
	public void textMiningNotCompleted();
	public void textMiningNotCompleted(String message);
}
