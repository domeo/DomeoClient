package org.mindinformatics.gwt.domeo.model.persistence.ontologies;

/**
 * This interface lists all the terms belonging to the Provenance
 * Authoring and Versioning (PAV) ontology.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IPavOntology {

	public static final String createdBy = "pav:createdBy";
	public static final String createdOn = "pav:createdOn";
	public static final String createdWith = "pav:createdWith";
	public static final String lastSavedOn = "pav:lastSavedOn";
	public static final String lastSavedBy = "pav:lastSavedBy";
	public static final String versionNumber = "pav:versionNumber";
	public static final String previousVersion = "pav:previousVersion";
	
	public static final String importedFrom = "pav:importedFrom";
	public static final String importedBy = "pav:importedBy";
	public static final String importedOn = "pav:importedOn";
	public static final String providedBy = "pav:providedBy";
	
	public static final String lineageUri = "pav:lineageUri";
}
