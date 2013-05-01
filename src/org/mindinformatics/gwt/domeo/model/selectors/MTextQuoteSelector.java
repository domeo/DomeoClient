package org.mindinformatics.gwt.domeo.model.selectors;

/**
 *
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@SuppressWarnings("serial")
public class MTextQuoteSelector extends MTextSelector {

	public static final String TYPE = "ao:PrefixSuffixTextSelector";
	public static final String PREFIX = "ao:prefix";
	public static final String SUFFIX = "ao:suffix";
	
	String prefix, suffix;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
    public String getSelectorType() {
    	return TYPE;
    }
}
