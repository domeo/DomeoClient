/*
 * Copyright 2013 Massachusetts General Hospital
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.mindinformatics.gwt.domeo.plugins.resource.ebi.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextMiningConnector;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.model.MAnnotationReference;
import org.mindinformatics.gwt.domeo.plugins.annotopia.ebi.src.AnnotopiaEbiConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.ebi.service.annotator.FEbiAnnotatorParametrizationForm;
import org.mindinformatics.gwt.framework.model.references.MPublicationArticleReference;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class EbiManager implements ITextMiningConnector {

	private IDomeo _domeo;
	private IEbiConnector _connector;
	
	private EbiManager() {}
	private static EbiManager _instance;
	public static EbiManager getInstance() {
		if(_instance == null) _instance = new  EbiManager(); 
		return _instance;
	}
	
	public boolean selectConnector(IDomeo domeo) {
		if(_connector!=null) return true;
		_domeo = domeo;
		if(domeo.isStandaloneMode()) {
			//_connector = new StandaloneBioPortalConnector();
		} else {
			if(domeo.isAnnotopiaEnabled()) {
				_connector = new AnnotopiaEbiConnector(domeo, null);
			} else if (domeo.isHostedMode()) {
				//_connector = new GwtBioPortalServiceConnector();
			} else {
				// Real service
				//_connector = new JsonBioPortalConnector(domeo);
			}
		}
		domeo.getLogger().debug(this, "BioPortal Connector selected: " + _connector.getClass().getName());
		return false;
	} 
	
	@Override
	public void annotate(ITextminingRequestCompleted completionCallback,
			String url, String textContent, String... params) throws IllegalArgumentException {
		if (_connector!=null) {
			//_domeo.getAnnotationPersistenceManager().getBibliographicSet().getSelfReference()
			
			if(_domeo.getPersistenceManager().getBibliographicSet()!=null 
					&& _domeo.getPersistenceManager().getBibliographicSet().getSelfReference()!=null
					&& ((MAnnotationReference)_domeo.getPersistenceManager().getBibliographicSet().getSelfReference()).getReference()!=null) {
				if(((MPublicationArticleReference)((MAnnotationReference)_domeo.getPersistenceManager().getBibliographicSet().getSelfReference()).getReference()).getPubMedCentralId()!=null) {
					_connector.textmine(completionCallback, url, 
							((MPublicationArticleReference)((MAnnotationReference)_domeo.getPersistenceManager().getBibliographicSet().getSelfReference()).getReference()).getPubMedCentralId());
				}				
			} else {
				completionCallback.textMiningNotCompleted("No PMCID found");
			}
		} else throw new IllegalArgumentException("No BioPortal Connector selected");	
	}

	@Override
	public String getAnnotatorLabel() {
		return "EBI Annotator Web Service";
	}

	@Override
	public Widget getAnnotatorPanel() {
		return new FEbiAnnotatorParametrizationForm(_domeo);
	}
}
