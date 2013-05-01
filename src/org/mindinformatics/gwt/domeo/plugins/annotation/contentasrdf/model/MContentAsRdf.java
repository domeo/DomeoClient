package org.mindinformatics.gwt.domeo.plugins.annotation.contentasrdf.model;

import com.google.gwt.i18n.client.DateTimeFormat;

public class MContentAsRdf {

	public static final String LABEL = "Content As RDF";
	public static final String TYPE = "cnt:ContentAsText";
	
	DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");
	
	// Transient fields: these are not going to be stored in the 
	// Annotation Set graph
	//private Long localId;				// sent to the server for matching saved items
	private String individualUri;	// Individual Uniform Resource Identifier
    //private Boolean hasChanged = true;	// flag for triggering the saving
	
    // Persistent immutable fields: these are going to be stored in the 
    // Annotation Set without being modified by the server
    //private Date createdOn;
    //private IAgent creator;
    //private ISoftware tool;
    
    String chars;
    String format;
        
	public String getIndividualUri() {
		return individualUri;
	}

	public void setIndividualUri(String individualUri) {
		this.individualUri = individualUri;
	}

	public String getChars() {
		return chars;
	}

	public void setChars(String chars) {
		this.chars = chars;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLabel() {
		return LABEL;
	}

	public String getAnnotationType() {
		return TYPE;
	}
}
