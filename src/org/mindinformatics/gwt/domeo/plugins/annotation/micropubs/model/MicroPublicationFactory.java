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
package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import java.util.Date;

import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.framework.component.agents.model.MAgent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.model.agents.IAgent;
import org.mindinformatics.gwt.framework.model.agents.ISoftware;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MicroPublicationFactory extends AnnotationFactory {

	public static MMicroPublicationAnnotation createMicroPublicationAnnotation(
			MAnnotationSet set, IAgent creator, ISoftware tool, 
			MGenericResource target, MSelector selector,
			MMicroPublication microPublication) 
	{
		MMicroPublicationAnnotation ann = new MMicroPublicationAnnotation();
		ann.setLocalId(getLocalId());
		ann.setUuid(getUuid());
		ann.setIndividualUri(getUrn(ann.getUuid()));
		ann.setCreator(creator);
		ann.setCreatedOn(new Date());
		ann.setSelector(selector);
		ann.setMicroPublication(microPublication);
		ann.setNewVersion(true);
		ann.setTool(tool);
		return ann;
	}
	
	public static MMicroPublication createMicroPublication(MTextQuoteSelector selector) {
		MMicroPublication mp = new MMicroPublication(selector);
		return mp;
	}	
	
	public static MMpRelationship createMicroPublicationRelationship(IAgent creator, MMpElement element, String relationship) {
		MMpRelationship rel = new MMpRelationship(element, relationship);
		rel.setId(getUuid());
		rel.setCreator(creator);
		rel.setCreationDate(new Date());
		return rel;
	}
}
