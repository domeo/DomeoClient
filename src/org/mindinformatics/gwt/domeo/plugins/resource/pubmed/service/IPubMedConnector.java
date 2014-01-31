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
package org.mindinformatics.gwt.domeo.plugins.resource.pubmed.service;

import java.util.List;

import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPubMedConnector {
	
	public void retrieveExistingBibliographySetByUrl(final IRetrieveExistingBibliographySetHandler handler, String url)
			throws IllegalArgumentException;
	
	public void retrieveExistingBibliographySet(IRetrieveExistingBibliographySetHandler completionCallback, int level) 
			throws IllegalArgumentException;
	
	public void getBibliographicObject(IPubMedItemsRequestCompleted completionCallback,
			String typeQuery, String textQuery) throws IllegalArgumentException;

	public void getBibliographicObjects(IPubMedItemsRequestCompleted completionCallback,
			String typeQuery, final List<String> textQuery, List<String> elements) throws IllegalArgumentException;
	
	public void searchBibliographicObjects(IPubMedPaginatedItemsRequestCompleted completionCallback,
			String typeQuery, final String textQuery, int maxResults, int offset) throws IllegalArgumentException;
}
