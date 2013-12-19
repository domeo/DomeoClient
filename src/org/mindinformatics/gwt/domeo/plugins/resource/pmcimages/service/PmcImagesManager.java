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
package org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.impl.GwtPmcImagesConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.impl.JsonPmcImagesConnector;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PmcImagesManager {

	private IPmcImagesConnector _connector;
	
	private PmcImagesManager() {}
	private static PmcImagesManager _instance;
	public static PmcImagesManager getInstance() {
		if(_instance == null) _instance = new  PmcImagesManager(); 
		return _instance;
	}
	
	public IPmcImagesConnector selectPubMedImagesConnector(IDomeo domeo) {
		if(_connector!=null) return _connector;
		if(domeo.isStandaloneMode()) {
			//_connector = new StandalonePubMedConnector();
		} else {
			if (domeo.isHostedMode()) {
				_connector = new GwtPmcImagesConnector();
			} else {
				_connector = new JsonPmcImagesConnector(domeo);
			}
		}
		
		domeo.getLogger().debug(this, "PubMed Connector selected: " + _connector.getClass().getName());
		
		return _connector;
	} 
	
	public void retrievePmcImagesData(IPmcImagesRequestCompleted callbackCompleted, String pmid, String pmcid, String doi) {
		_connector.retrievePmcImagesData(callbackCompleted, pmid, pmcid, doi);
	}
}
