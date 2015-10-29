package org.mindinformatics.gwt.domeo.model.persistence.ontologies;

/**
 * This interface lists all the terms belonging to the Resource
 * Description Schema (RDFS).
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IRdfsOntology {

	// TODO HIGH The properties have to be changed making use
	// of the ':' for separating the namespace
	public static final String id = "@id";
	public static final String type = "@type";
	public static final String value = "value";
	public static final String rdfValue = "rdf:" + value;
	public static final String label = "rdfs:label";
}
