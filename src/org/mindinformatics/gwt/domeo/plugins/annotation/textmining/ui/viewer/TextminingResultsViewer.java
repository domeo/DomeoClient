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
package org.mindinformatics.gwt.domeo.plugins.annotation.textmining.ui.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.linkeddata.model.JsoLinkedDataResource;
import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.component.resources.serialization.JsonGenericResource;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TextminingResultsViewer extends Composite implements IContentPanel, IResizable {

	private static final String TITLE = "Text Mining Results ";

	interface Binder extends UiBinder<FlowPanel, TextminingResultsViewer> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private JsAnnotationSet _jsonSet;
	private IContainerPanel _containerPanel;
	private ITextminingRequestCompleted _completionCallback;
	
	private ArrayList<TextminingResultCheckBox> boxes = new ArrayList<TextminingResultCheckBox>();
	private HashMap<String, MLinkedResource> terms = new HashMap<String, MLinkedResource>();
	private HashMap<MGenericResource, HashMap<MLinkedResource, Integer>> ontologies = new HashMap<MGenericResource, HashMap<MLinkedResource, Integer>>();
	
	@UiField HorizontalPanel header;
	@UiField HorizontalPanel summary;
	@UiField HorizontalPanel content;
	@UiField FlowPanel termsPanel;
	@UiField FlowPanel buttonsPanel;
	
	public TextminingResultsViewer(IDomeo domeo, final ITextminingRequestCompleted completionCallback, JsAnnotationSet jsonSet) {
		_domeo = domeo;
		_completionCallback = completionCallback;
		_jsonSet = jsonSet;
		
		initWidget(binder.createAndBindUi(this));
		
		header.add(new HTML("<span style='font-weight: bold;'>" + jsonSet.getLabel() + "</span>, " + jsonSet.getDescription()));
		
		JsArray<JavaScriptObject> jsonAnnotations = jsonSet.getAnnotation();
		for(int j=0; j<jsonAnnotations.length(); j++) {
			// Detect annotation type
			HashSet<String> typesSet = new HashSet<String>();
			if(hasMultipleTypes(jsonAnnotations.get(j))) {
				JsArrayString types = getObjectTypes(jsonAnnotations.get(j));
				for(int k=0; k<types.length(); k++) {
					typesSet.add(types.get(k));
				}
			} else {
				typesSet.add(getObjectType(jsonAnnotations.get(j)));
			}
			
			if(typesSet.contains(MQualifierAnnotation.TYPE)) {
				JsArray<JsoLinkedDataResource> tags = getSemanticTags(jsonAnnotations.get(j));
				for(int k=0; k<tags.length(); k++) {
					JsonGenericResource gr = tags.get(k).getSource();
					MGenericResource r = ResourcesFactory.createGenericResource(gr.getUrl(), gr.getLabel());		
					MLinkedResource ldr = ResourcesFactory.createTrustedResource(tags.get(k).getUrl(), tags.get(k).getLabel(), r);
					ldr.setUrl(tags.get(k).getUrl());
					ldr.setLabel(tags.get(k).getLabel());
					ldr.setDescription(tags.get(k).getDescription());

					if(!terms.containsKey(ldr.getUrl()))
						terms.put(ldr.getUrl(), ldr);
					
					if(!ontologies.containsKey(r)) {
						HashMap<MLinkedResource, Integer> hSet = new HashMap<MLinkedResource, Integer>();
						hSet.put(ldr, new Integer(1));
						ontologies.put(r, hSet);
					} else {
						if(ontologies.get(r).containsKey(ldr)) {
							Integer current = ontologies.get(r).get(ldr);
							ontologies.get(r).remove(ldr);
							ontologies.get(r).put(ldr, ++current);
						} else {
							ontologies.get(r).put(ldr, new Integer(1));
						}
					}
				}
			}
		}
		
		Set<MGenericResource> ts1 = ontologies.keySet();
		StringBuffer sb1 = new StringBuffer();	
		
		for(MGenericResource t: ts1) {
			termsPanel.add(new HTML("<a href='" + t.getUrl() + "' target='_blank'>" + t.getLabel() + " <span class='source'></a> (" +  ontologies.get(t).size() + " terms)<br/>"));
			//sb1.append("<a href='" + t.getUrl() + "' target='_blank'>" + t.getLabel() + " <span class='source'></a> (" +  ontologies.get(t).size() + ")<br/>");
			//sb1.append("<ul class='tags'>");
			FlowPanel p = new FlowPanel();
			for(MLinkedResource tt: ontologies.get(t).keySet()) {
				//sb1.append("<li><a href='" + tt.getUrl() + "' target='_blank'>" + tt.getLabel() + "</a></li>");
				TextminingResultCheckBox box = new TextminingResultCheckBox(_domeo, tt,  ontologies.get(t).get(tt));
				box.setStyleName("tagss");
				boxes.add(box);
				p.add(box);
			}
			termsPanel.add(p);
			
			//sb1.append("</ul>");
			//sb1.append("<br/><br/>");
			
			
		}
		termsPanel.add(new HTML(sb1.toString()));	
		
		/*
		Set<MGenericResource> ts1 = ontologies.keySet();
		StringBuffer sb1 = new StringBuffer();	
		for(MGenericResource t: ts1) {
			sb1.append("<a href='" + t.getUrl() + "' target='_blank'>" + t.getLabel() + " <span class='source'></a> (" +  ontologies.get(t).size() + ")<br/>");
			sb1.append("<ul class='tags'>");
			for(MLinkedResource tt: ontologies.get(t)) {
				sb1.append("<li><a href='" + tt.getUrl() + "' target='_blank'>" + tt.getLabel() + "</a></li>");
				termsPanel.add(new TextminingResultCheckBox(_domeo, tt));
			}
			sb1.append("</ul>");
			sb1.append("<br/><br/>");
			
			
		}
		termsPanel.add(new HTML(sb1.toString()));	
		*/
		
		summary.add(new HTML("<span>Total of " + terms.size() + " terms and " + jsonSet.getAnnotation().length() + " annotations</span>"));
		
		Button process = new Button("Process");
		process.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				HashSet<String> uris = new HashSet<String>();
				for(TextminingResultCheckBox box: boxes) {
					String sel = box.getSelection();
					if(sel!=null) uris.add(sel);
				}			
				completionCallback.returnTextminingResults(_jsonSet, uris);
				_containerPanel.hide();
			}
			
		});
		buttonsPanel.add(process);
		
		Button processAll = new Button("Process All");
		processAll.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {		
				completionCallback.returnTextminingResults(_jsonSet, true);
				_containerPanel.hide();
			}
		});
		buttonsPanel.add(processAll);
		
		/*
		Collection<MLinkedResource> ts = terms.values();
		StringBuffer sb2 = new StringBuffer();
		sb2.append(" <ul class='tags'>");
		for(MLinkedResource t: ts) {
			sb2.append("<li><a href='" + t.getUrl() + "' target='_blank'>" + 
				t.getLabel() + " <span class='source'>- " +  
				t.getSource().getLabel() + "</span></a></li>");
		}
		sb2.append("</ul>");
		termsPanel.add(new HTML(sb2.toString()));
		*/
	}
	
	private final native boolean hasMultipleTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType] instanceof Array; 
	}-*/;
	private final native String getObjectType(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	private final native JsArrayString getObjectTypes(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
	public final native JsArray<JsoLinkedDataResource> getSemanticTags(Object obj) /*-{ 
		return obj[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::semanticTag]; 
	}-*/;
		
	public String getTitle() {
		return TITLE + ": " + _jsonSet.getLabel();
	}
	
	@Override
	public void resized() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	@Override
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
}
