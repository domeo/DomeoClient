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
package org.mindinformatics.gwt.domeo.component.textmining.src;

import java.util.HashSet;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.textmining.ui.viewer.TextminingResultsViewer;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.JsonUnmarshallingManager;
import org.mindinformatics.gwt.framework.component.profiles.model.IProfile;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TextMiningManager implements ITextminingRequestCompleted {

	IDomeo _domeo;
	
	public TextMiningManager(IDomeo domeo) { _domeo = domeo; }
	
	@Override
	public void returnTextminingResults(JsAnnotationSet set, boolean all) {
		
		_domeo.getLogger().debug(this, "Textmining results received...");
		_domeo.getProgressPanelContainer().setProgressMessage("Textmining results received...");
		if(all || _domeo.getProfileManager().getUserCurrentProfile().isFeatureDisabled(IProfile.FEATURE_TEXTMINING_SUMMARY)) {		
			JsonUnmarshallingManager manager = _domeo.getUnmarshaller();	
			try {
				manager.unmarshallTextmining(set);
		
				_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection();		
				_domeo.getToolbarPanel().deselectAnalyze();
				_domeo.getProgressPanelContainer().setCompletionMessage("Textmining completed!");
				_domeo.getLogger().info(this, "Textmining completed!");
				_domeo.refreshAllComponents();
			} catch(Exception e) {
				textMiningNotCompleted();
			} 
		} else {
			_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection();		
			_domeo.getToolbarPanel().deselectAnalyze();
			_domeo.getProgressPanelContainer().setCompletionMessage("Textmining completed!");
			_domeo.getLogger().info(this, "Textmining completed!");
			
			TextminingResultsViewer afp = new TextminingResultsViewer(_domeo, this, set);
			new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), false, false, false);
		}
	}
	
	@Override
	public void returnTextminingResults(JsAnnotationSet set, HashSet<String> acceptedIds) {
		
		_domeo.getLogger().debug(this, "Textmining results received...");
		_domeo.getProgressPanelContainer().setProgressMessage("Textmining results received...");
		
		JsonUnmarshallingManager manager = _domeo.getUnmarshaller();	
		try {
			manager.unmarshallTextmining(set, acceptedIds);
	
			_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection();		
			_domeo.getToolbarPanel().deselectAnalyze();
			_domeo.getProgressPanelContainer().setCompletionMessage("Textmining completed!");
			_domeo.getLogger().info(this, "Textmining completed!");
			_domeo.refreshAllComponents();
		} catch(Exception e) {
			textMiningNotCompleted();
		} 

	}
	
	@Override
	public void textMiningNotCompleted(String message) {
		_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection();
		_domeo.getToolbarPanel().deselectAnalyze();
		_domeo.getProgressPanelContainer().setErrorMessage("Textmining not completed! " + message);
		_domeo.getLogger().info(this, "Textmining not completed!");
	}
	
	@Override
	public void textMiningNotCompleted() {
		_domeo.getContentPanel().getAnnotationFrameWrapper().clearSelection();
		_domeo.getToolbarPanel().deselectAnalyze();
		_domeo.getProgressPanelContainer().setErrorMessage("Textmining not completed!");
		_domeo.getLogger().info(this, "Textmining not completed!");
	}


}
