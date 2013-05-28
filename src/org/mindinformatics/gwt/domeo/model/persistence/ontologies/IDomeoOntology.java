package org.mindinformatics.gwt.domeo.model.persistence.ontologies;

/**
 * This interface provides all the terms necessary for Domeo
 * to marshall/unmarshall the persistence messages.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IDomeoOntology {
	
	public static final String generalId = "@id";
	public static final String generalType = "@type";
	
	public static final String uuid = "domeo:uuid";
	public static final String mongoUuid = "domeo:mongoUuid";

	public static final String annotationSet = "ao:AnnotationSet";
	public static final String discussionSet = "domeo:DiscussionSet";
	public static final String bibliographySet = "domeo:BibliographicSet";
	public static final String extractionLevel = "domeo:extractionLevel";
	
	public static final String agents = "domeo:agents";
	
	public static final String synonyms = "obo_annot:synonym"; 
	
	// TODO HIGH The properties have to be changed making use
	// of the ':' for separating the namespace
	//public static final String uuid = "domeo_uuid" ;

	public static final String annotations = "ao:item" ;
	public static final String annotationsCount = "ao:numberItems" ;

	public static final String content = "ao:body" ;
	public static final String title = "domeo_title" ;
	public static final String semanticTag = "ao:hasTopic" ;
	
	public static final String hasTarget = "ao:context";
	

	public static final String context = "domeo:inContext";
	public static final String xpath = "domeo:xPath";
	public static final String displaySource = "domeo:displaySource";
	
	// Annotation ontology
	public static final String annotates = "ao:annotatesResource";
	public static final String source = "ao:hasSource" ;
	
	// AO: Selectors
	public static final String specificResource = "ao:SpecificResource";
	public static final String selector = "ao:hasSelector";
	
	
	
	// TODO HIGH Change the names of the following properties 
	// swapping 'temp' with 'transient'
	public static final String transientLocalId = "domeo_temp_localId" ;
	public static final String transientHasChanged = "domeo_temp_hasChanged" ;
	public static final String transientNewVersion = "domeo_temp_saveAsNewVersion" ;
	
	
}
