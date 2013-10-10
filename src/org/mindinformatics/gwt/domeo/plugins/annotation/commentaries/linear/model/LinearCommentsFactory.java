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
package org.mindinformatics.gwt.domeo.plugins.annotation.commentaries.linear.model;

import java.util.ArrayList;
import java.util.Date;

import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class LinearCommentsFactory extends AnnotationFactory {
	
	public static MLinearCommentAnnotation cloneLinearComment(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			String versionNumber, String previousVersion,
			IAgent creator, ISoftware tool, 
			ArrayList<MSelector> selectors,
			String title, String text) 
	{
		MLinearCommentAnnotation ann = 
			cloneLinearComment(individualUri, lineageUri,  
				createdOn, lastSavedOn, versionNumber, previousVersion, creator, tool, selectors, text);
		ann.setTitle(title);
		return ann;
	}
	
	public static MLinearCommentAnnotation cloneLinearComment(
			String individualUri, String lineageUri,
			Date createdOn, Date lastSavedOn, 
			String versionNumber, String previousVersion,
			IAgent creator,  ISoftware tool, 
			ArrayList<MSelector> selectors,
			String text) {
		MLinearCommentAnnotation ann = 
			initializeLinearComment(individualUri, lineageUri, versionNumber, previousVersion, creator, 
				createdOn, lastSavedOn, tool, selectors, text);
		ann.setUuid("");
		return ann;
	}
	
	public static MLinearCommentAnnotation createLinearComment(
			IAgent creator, ISoftware tool, MSelector selector, 
			String title, String text) {		
		MLinearCommentAnnotation ann = 
			createLinearComment(creator, tool, selector, text);
		ann.setTitle(title);
		return ann;
	}
	
	public static MLinearCommentAnnotation createLinearComment(
			IAgent creator, ISoftware tool, MSelector selector, 
			String text) {
		MLinearCommentAnnotation ann = 
			initializeLinearComment(creator, tool, selector, text);
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		ann.setCreatedOn(new Date());
		ann.setNewVersion(true);
		return ann;
	}
	
	private static MLinearCommentAnnotation initializeLinearComment(
			String individualUri, String lineageUri,
			String versionNumber, String previousVersion,
			IAgent creator, Date createdOn, Date lastSavedOn, 
			ISoftware tool, ArrayList<MSelector> selectors, 
			String text) {
		MLinearCommentAnnotation ann = new MLinearCommentAnnotation();
		ann.setLocalId(getLocalId());
		ann.setLineageUri(lineageUri);
		ann.setIndividualUri(individualUri);
		ann.setCreator(creator);
		ann.setCreatedOn(createdOn);
		ann.setLastSavedOn(lastSavedOn);
		ann.setTool(tool);
		ann.setVersionNumber(versionNumber);
		ann.setPreviousVersion(previousVersion);
		ann.getSelectors().addAll(selectors);
		ann.setText(text);
		ann.setHasChanged(false);
		return ann;
	}
	
	/**
	 * Internal method that creates a linear comment and initializes
	 * it with the common properties. 
	 * @param creator	The annotation creator
	 * @param tool		The tool that generated the annotation
	 * @param selector	The selector 
	 * @param text		The comment content
	 * @return
	 */
	private static MLinearCommentAnnotation initializeLinearComment(
			IAgent creator, ISoftware tool, MSelector selector, 
			String text) {
		MLinearCommentAnnotation ann = new MLinearCommentAnnotation();
		ann.setLocalId(getLocalId());
		ann.setCreator(creator);
		ann.setTool(tool);
		ann.setSelector(selector);
		ann.setText(text);
		return ann;
	}
}
