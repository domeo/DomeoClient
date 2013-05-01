package org.mindinformatics.gwt.framework.component.preferences.src;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 *
 * This class models a text preference item. 
 */
public class TextPreference extends GenericPreference {

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
