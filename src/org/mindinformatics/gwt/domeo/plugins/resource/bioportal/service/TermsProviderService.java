package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Paolo Ciccarese
 */
public interface TermsProviderService extends RemoteService {
	public ArrayList<MLinkedResource> termsSearchServer(String uid, String username, String queryType, String term); 
	public ArrayList<MLinkedResource> termsSearchServer(String uid, String username, String queryType, String term,
			boolean vocabularyProteinOntology, boolean vocabularyCehmicalEntities, boolean vocabularyGeneOntology,
			boolean vocabularyDiseaseOntology, boolean vocabularyCellOntology, boolean vocabularyPlantOntology,
			boolean vocabularyNifStdOntology); 
}
