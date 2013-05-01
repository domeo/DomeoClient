package org.mindinformatics.gwt.domeo.model.selectors;

@SuppressWarnings("serial")
public class MTextSelector extends MSelector {

	public static final String TYPE = "ao:TextSelector";
	public static final String EXACT = "ao:exact";
	
	String exact;

	public String getExact() {
		return exact;
	}

	public void setExact(String exact) {
		this.exact = exact;
	}
	
    public String getSelectorType() {
    	return TYPE;
    }
}
