package org.mindinformatics.gwt.domeo.plugins.annotation.curation.model;

import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MCurationToken extends MAnnotation {

	public static final String LABEL = "Curation";
	public static final String TYPE = "ao:Curation";
	
    public static final String INCORRECT = "Incorrect";
    public static final String CORRECT = "Correct";
    public static final String CORRECT_EXACT = "Exact Match";
    public static final String CORRECT_BROAD = "Broad Match";
    public static final String CORRECT_NARROW = "Narrow Match";
    public static final String UNCLEAR = "Unclear";
	
    String status;
    String description;
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getAnnotationType() {
		return TYPE;
	}
}
