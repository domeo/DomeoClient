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
package org.mindinformatics.gwt.domeo.plugins.resource.nif.service.annotator;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 * 
 * This class manages all the parameters used for running the NIF Annotator service.
 * 
 * The class is a singleton and at the moment all the fields have been declared 
 * public and no getter and setters have been defined to keep the code compact. 
 * 
 * From documentation http://nif-services.neuinfo.org/servicesv1/resource_AnnotateService.html
 * List of categories http://beta.neuinfo.org/services/v1/vocabulary/categories
 * 
 * List of parameters
 * ------------------
 * name 			description 															type 	default
 * content 			The content to annotate 												query 	
 * includeCat 		A set of categories to include 											query 	
 * excludeCat 		A set of categories to exclude 											query 	
 * minLength 		The minimum length of annotated entities 								query 	4
 * longestOnly 		Should only the longest entity be returned for an overlapping group 	query 	false
 * includeAbbrev 	Should abbreviations be included 										query 	false
 * includeAcronym 	Should acronyms be included 											query 	false
 * includeNumbers 	Should numbers be included 												query 	false
 */
public class PNifAnnotatorParameters {
	
	private PNifAnnotatorParameters() {}
	
	final String categories[] = {"All", "biological_process", "anatomical entity","age", "Assay","brain","Platform","gene","assay","cell","Brain","organism (NCBI Taxonomy Slim) ",
			"Cellular Component","Data role","organism","molecular entity","multi-cellular organism","quality","Behavioral process","Population","Extracellular Structure",
			"reagent role","anatomical projection","PATO quality","Device","Molecule role","disease","Chemical role","anatomical_structure","Familial role","biological_region",
			"Phenotype","Regional Part Of Cell","molecule","polypeptide_region","institution","site","Resource","Disease","binding_site","cell line","Data object","Cell line",
			"Institution","Cell role","process","disease_state","biomaterial_region","Vendor","device","cellular_component","Reagent role"};
	
	public static PNifAnnotatorParameters instance;
	public static PNifAnnotatorParameters getInstance() {
		if(instance==null) 
			instance = new PNifAnnotatorParameters();
		return instance;
	}
	
	public int minLength = 4; // The minimum length of annotated entitie (default: 4)
	public String includeCat = ""; // A set of categories to include
	public String excludeCat = ""; // A set of categories to exclude
	public boolean longestOnly = true; // Should only the longest entity be returned for an overlapping group (default: false)
	public boolean includeAbbrev = false; // Should abbreviations be included (default: false)
	public boolean includeAcronym = false; // Should acronyms be included (default: false)
	public boolean includeNumbers = false; // Should numbers be included (default: false)
}
