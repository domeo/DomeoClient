package org.mindinformatics.gwt.framework.component.preferences.src;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 *
 * This class models a boolean preference item. 
 */
public class BooleanPreference extends GenericPreference {

	Boolean value;

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
	public String toString() {
		return value.toString();
	}
}
