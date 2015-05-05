package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model;

public interface Iddi {

	public static final String LABEL = "ddi Annotation";
	//public static final String TYPE = "ao:expertstudyAnnotation";
	public static final String TYPE = "ao:ddiAnnotation";
	public static final String BODY_TYPE = "domeo:ddiUsage";
	
	public final String SIO_PREFIX = "http://semanticscience.org/resource/";
	public final String RXNORM_PREFIX = "http://purl.bioontology.org/ontology/RXNORM/";
	public final String DIKBD2R_PREFIX = "http://dbmi-icode-01.dbmi.pitt.edu:2020/vocab/resource/";
	public final String NCIT_PREFIX = "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#";
	public final String PDDI_POC_PREFIX = "http://purl.org/net/nlprepository/pddi#";
	public final String DAILYMED_PREFIX = "http://dbmi-icode-01.dbmi.pitt.edu/linkedSPLs/vocab/resource/";
	public final String MP_PREFIX = "http://purl.org/mp/";
	
	public final String EX_PREFIX = "http://example.org/";
}
