package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Abstract unmarshaller.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public abstract class AUnmarshaller implements IUnmarshaller {

	DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss Z");
	
	/**
	 * This methods provides the scaffolding to fit the validation logic.
	 * @param json			The JSON object to validate
	 * @param validation	The type of validation to perform
	 */
	protected abstract void validation(JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors);
	
	/**
	 * Forces unmarshalling without validation	
	 * @param json		The JSON object to unmarshall
	 * @return	The unmarshalled object
	 */
	public Object unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		return unmarshall(manager, json, IUnmarshaller.OFF_VALIDATION, set, selectors);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling.IUnmarshaller#validateInput(com.google.gwt.core.client.JavaScriptObject, java.lang.String)
	 */
	public void validateInput(JsonUnmarshallingManager manager, JavaScriptObject json, String validation, MAnnotationSet set, 
			ArrayList<MSelector> selectors) {
		if(!validation.equals(IUnmarshaller.OFF_VALIDATION)) validation(json, validation, set, selectors);
	}
	
	/**
	 * Completion of the validation process. Generates a RuntimeException if a 
	 * non nullable field is null
	 * @param notNullableFieldsMap	Maps of non nullable fields and values.
	 */
	protected void completeValidation(Map<String, String> notNullableFieldsMap) {
		StringBuffer sb = new StringBuffer();
		Set<String> keys = notNullableFieldsMap.keySet();
		for(String key: keys) {
			if(notNullableFieldsMap.get(key)==null) {
				sb.append("Illegal null field: [" + key + "," + notNullableFieldsMap.get(key) + "], ");
			}
		}
		if(sb.length()>0) throw new RuntimeException(sb.toString());
	}
	
	/**
	 * This is used to verify the JavaScriptObject input type.
	 * @param json	The JavaScriptObject to check
	 * @param type	The allowed type
	 */
	protected void verifyType(JavaScriptObject json, String type) {
		if(!type.equals(getObjectType(json))) 
			throw new RuntimeException("Illegal JavaScriptObject type [" + getObjectType(json) + "] expected [" + type + "]");
	}
	
	/**
	 * Return the type of the JSON object
	 * @param json	The JavaScriptObject 
	 * @return The type if present
	 */
	protected final native String getObjectType(JavaScriptObject json) /*-{ 
		return json[@org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology::generalType]; 
	}-*/;
}
