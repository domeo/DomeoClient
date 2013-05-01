package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Paolo Ciccarese
 */
public interface TermsProviderServiceAsync {
	void termsSearchServer(String uid, String username, String queryType, String term, 
			AsyncCallback<ArrayList<MLinkedResource>> callback);
	void termsSearchServer(String uid, String username, String queryType, String term, 
			boolean vocabularyProteinOntology, boolean vocabularyCehmicalEntities, boolean vocabularyGeneOntology,
			boolean vocabularyDiseaseOntology, boolean vocabularyCellOntology, boolean vocabularyPlantOntology,
			boolean vocabularyNifStdOntology, AsyncCallback<ArrayList<MLinkedResource>> callback);
}
