package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import java.util.ArrayList;

import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Paolo Ciccarese
 */
public interface TermsRetrievalService extends RemoteService {
	public ArrayList<MLinkedResource> termsSearchServer(String uid, String username, String type, String term);
}
