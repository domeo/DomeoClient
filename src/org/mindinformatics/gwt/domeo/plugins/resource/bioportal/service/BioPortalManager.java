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
package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextMiningConnector;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.annotopia.bioportal.src.AnnotopiaBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.annotator.FBioPortalAnnotatorParametrizationForm;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.annotator.PBioPortalAnnotatorParameters;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.GwtBioPortalServiceConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test.StandaloneBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.src.JsonBioPortalConnector;
import org.mindinformatics.gwt.framework.src.Utils;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class BioPortalManager implements ITextMiningConnector {

	private IDomeo _domeo;
	private IBioPortalConnector _connector;
	
	private BioPortalManager() {}
	private static BioPortalManager _instance;
	public static BioPortalManager getInstance() {
		if(_instance == null) _instance = new  BioPortalManager(); 
		return _instance;
	}
	
	public boolean selectConnector(IDomeo domeo) {
		if(_connector!=null) return true;
		_domeo = domeo;
		if(domeo.isStandaloneMode()) {
			_connector = new StandaloneBioPortalConnector();
		} else {
			if(domeo.isAnnotopiaEnabled()) {
				_connector = new AnnotopiaBioPortalConnector(domeo, Utils.getAnnotopiaLocation());
			} else if (domeo.isHostedMode()) {
				_connector = new GwtBioPortalServiceConnector();
			} else {
				// Real service
				_connector = new JsonBioPortalConnector(domeo);
			}
		}
		domeo.getLogger().debug(this, "BioPortal Connector selected: " + _connector.getClass().getName());
		return false;
	} 
	
	public void searchTerms(IBioPortalItemsRequestCompleted completionCallback, String textQuery) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.searchTerm(completionCallback, textQuery, "");
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
	}
	
	@Override
	public void annotate(ITextminingRequestCompleted completionCallback,
			String url, String textContent, String... params) throws IllegalArgumentException {
		if (_connector!=null) {
			 _connector.textmine(completionCallback, url, textContent, 
				PBioPortalAnnotatorParameters.getInstance().longestOnly,
				PBioPortalAnnotatorParameters.getInstance().wholeWordOnly,
				PBioPortalAnnotatorParameters.getInstance().filterNumbers,
				PBioPortalAnnotatorParameters.getInstance().withDefaultStopWords,
				PBioPortalAnnotatorParameters.getInstance().isStopWordsCaseSenstive,
				PBioPortalAnnotatorParameters.getInstance().scored,
				PBioPortalAnnotatorParameters.getInstance().withSynonyms
			);
		} else throw new IllegalArgumentException("No BioPortal Connector selected");
		
	}

	@Override
	public String getAnnotatorLabel() {
		return "NCBO Annotator Web Service";
	}

	@Override
	public Widget getAnnotatorPanel() {
		return new FBioPortalAnnotatorParametrizationForm(_domeo);
	}
}
