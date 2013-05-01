package org.mindinformatics.gwt.domeo.client.ui.annotation.helpers;

import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationHelpersManager {

	private HashMap<String, IAnnotationHelper> 
		annotationHelpers = new HashMap<String, IAnnotationHelper>();
	
	public boolean registerAnnotationHelper(String annotationName, IAnnotationHelper helper) {
		if(!annotationHelpers.containsKey(annotationName)) {
			annotationHelpers.put(annotationName, helper);
			return true;
		}
		return false;
	}
	
	public IAnnotationHelper getAnnotationHelper(String annotationName) {
		if(annotationHelpers.containsKey(annotationName)) {
			return annotationHelpers.get(annotationName);
		} else
			return null; //TODO define a default tail
	}
}
