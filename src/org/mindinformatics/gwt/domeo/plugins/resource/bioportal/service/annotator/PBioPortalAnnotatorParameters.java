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
package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.annotator;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * This class manages all the parameters used for running the BioPortal Annotator service.
 * 
 * The class is a singleton and at the moment all the fields have been declared 
 * public and no getter and setters have been defined to keep the code compact. 
 * 
 * From documentation http://bioontology.stanford.edu/wiki/index.php?title=Annotator_User_Guide
 */ 
public class PBioPortalAnnotatorParameters {

	private PBioPortalAnnotatorParameters() {}
	
	public static PBioPortalAnnotatorParameters instance;
	public static PBioPortalAnnotatorParameters getInstance() {
		if(instance==null) 
			instance = new PBioPortalAnnotatorParameters();
		return instance;
	}
	
	public int minTermSize = 3; // Specifies the minimum length of the term to be included in the annotations (default: 3)
	public boolean longestOnly = false; // Specifies whether or not the entity recognition step (done with University of Michigan Mgrep tool) must match the longest word only if they are several terms that match to an expression.  (default: true)
	public boolean wholeWordOnly = true; // Specifies whether the term recognition step must match whole words only or not, if they are several terms that match to a given word from the input text.  (default: true)
	public boolean filterNumbers = true; // Specifies whether the entity recognition step to filter numbers or not. (default: true)
	public boolean withDefaultStopWords = false; // Specifies whether or not to use the default stop words. The default stop word list is available from this Web service call: http://rest.bioontology.org/obs/stopwords?apikey=YourAPIKey. If this parameter is set to true, this will override any stop words provided by the user in the parameter "stopWords". 
	public boolean isStopWordsCaseSenstive = false; // Specifies whether stop words are case-sensitive or not.
	public boolean scored = true; // Specifies whether or not the annotations are scored. A score is a number assigned to an annotation that reflects the accuracy of the annotation. The higher the score is the better the annotation is. The scoring algorithm gives a specific weight to an annotation according to the context of this annotation. For instance, an annotation done by matching a concept preferred name will be given a higher weight than an annotation done by matching a concept synonym or than an annotation done with a parent level 3 in the is_a hierarchy.
	public boolean withSynonyms = true; // Specifies whether or not the direct annotations are generated based on term synonyms. By default it includes all the synonyms and preferred name of a term. If 'false' is selected, the direct annotations are done with only the preferred name. 
}
