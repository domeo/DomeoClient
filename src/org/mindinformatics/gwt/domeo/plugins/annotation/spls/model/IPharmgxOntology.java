package org.mindinformatics.gwt.domeo.plugins.annotation.spls.model;

public interface IPharmgxOntology {
	public  final String LABEL = "SPL Annotation";
	public  final String TYPE = "ao:SPLAnnotation";
	public  final String BODY_TYPE = "domeo:PharmgxUsage";
	
	public  final String SPL_URN_PREFIX = "urn:linkedspls:uuid:";
	public  final String SPL_POC_PREFIX = "http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#";
	public  final String SIO_PREFIX = "sio:";
	public  final String DAILYMED_PREFIX = "http://dbmi-icode-01.dbmi.pitt.edu/linkedSPLs/vocab/resource/";
	public  final String NCIT_PREFIX = "ncit:";
	
	//public String getUriOfPrefix(String qname);
	
}
