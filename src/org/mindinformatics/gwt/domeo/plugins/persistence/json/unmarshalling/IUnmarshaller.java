package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public interface IUnmarshaller {

	public static final String OFF_VALIDATION = "Off";
	/**
	 * Used for textmining/extraction results and those items that have
	 * been generated but not yet saved.
	 */
	public static final String EASY_VALIDATION = "Low";
	/**
	 * Used for textmining/extraction results and those items that have
	 * been generated but not yet saved.
	 */
	public static final String IMPORT_VALIDATION = "Import";
	/**
	 * Used for validating elements that have been already persisted.
	 */
	public static final String LOAD_VALIDATION = "Loading";
	
	/**
	 * Unmarshalling of the JSON object with the desired level of validation.
	 * @param manager		The unmarshalling manager
	 * @param obj			The JSON object to serialize
	 * @param validation	The desired validation to perform
	 * @return	The unmarshalled object
	 */
	public Object unmarshall(JsonUnmarshallingManager manager, JavaScriptObject obj, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors);
	
	/**
	 * Triggers the validation of the input
	 * @param manager		The unmarshalling manager
	 * @param json		The JSON object to unmarshall
	 * @param validation	The type of validation to perform
	 */
	public void validateInput(JsonUnmarshallingManager manager, JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors);
	
}
