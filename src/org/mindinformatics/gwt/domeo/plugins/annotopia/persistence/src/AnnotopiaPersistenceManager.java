package org.mindinformatics.gwt.domeo.plugins.annotopia.persistence.src;

import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.APersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IPersistenceManager;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingBibliographySetHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.annotopia.serializers.AnnotopiaSerializerManager;
import org.mindinformatics.gwt.framework.src.ApplicationUtils;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaPersistenceManager extends APersistenceManager implements IPersistenceManager {

	public String URL = "http://127.0.0.1:8090/";
	
	/**
	 * @param domeo		Pointer to main application
	 * @param url		The URL of the Annotopia server
	 * @param callback  The pointer to the entity that will be triggered
	 */
	public AnnotopiaPersistenceManager(IDomeo domeo, String url, ICommandCompleted callback) {
		super(domeo, callback);
		if(url!=null) URL = url;
	}

	@Override
	public void saveAnnotation() {
		_application.getLogger().debug(this, "Saving annotation sets...");
		_application.getProgressPanelContainer().setProgressMessage("Saving annotation sets to Annotopia...");
		
		
		
	}
	
	/**
	 * Posts a new Annotation Set
	 */
	private void postAnnotationSet(MAnnotationSet set) {
		AnnotopiaSerializerManager manager = AnnotopiaSerializerManager.getInstance((IDomeo)_application);
		Properties v =  new JsUtils.JsUtilsImpl().parseJSON(
			"{\"apiKey\":\""+ ApplicationUtils.getAnnotopiaApiKey() +  
			"\",\"outCmd\":\"frame\",\"set\":" + 
			manager.serialize(set).toString() + "}");
	}
	
	/**
	 * Updates an existing Annotation Set
	 */
	private void putAnnotationSet(MAnnotationSet set) {
		
	}
	
	/**
	 * Deletes an existing Annotation Set
	 */
	private void deleteAnnotationSet(MAnnotationSet set) {
		
	}
	
	/**
	 * Posts a new annotation to an existing set
	 */
	private void postAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}
	
	/**
	 * Updates an existing annotation in an existing set
	 */
	private void putAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}
	
	/**
	 * Deletes an existing annotation to an existing set
	 */
	private void deleteAnnotationInSet(MAnnotation annotation, MAnnotationSet set) {
		
	}

	@Override
	public void retrieveExistingBibliographySet(
			IRetrieveExistingBibliographySetHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveExistingAnnotationSets(List<String> ids,
			IRetrieveExistingAnnotationSetHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveExistingAnnotationSetList(
			IRetrieveExistingAnnotationSetListHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveBibliography() {
		// TODO Auto-generated method stub		
	}
	
	private Properties getApiKey() {
		return Properties.create("ApiKey: " + ApplicationUtils.getAnnotopiaApiKey());
	}
	
	/** Return the user Annotopia OAuth token if it is enabled.
	 * @return The user Annotopia OAuth token if it is enabled. */
	private Properties getAnnotopiaOAuthToken() {
		return (ApplicationUtils.getAnnotopiaOauthEnabled().equalsIgnoreCase("true")? Properties.create("Authorization: Bearer " + ApplicationUtils.getAnnotopiaOauthToken()): Properties.create());
	}
	
	public static native  String stringify(JavaScriptObject obj) /*-{
		return JSON.stringify(obj);
	}-*/;
	
	public static native JavaScriptObject parseJson(String jsonStr) /*-{
		try {
			var jsonStr = jsonStr      
	    		.replace(/[\\]/g, '\\\\')
	    		.replace(/[\/]/g, '\\/')
	    		.replace(/[\b]/g, '\\b')
	    		.replace(/[\f]/g, '\\f')
	    		.replace(/[\n]/g, '\\n')
	    		.replace(/[\r]/g, '\\r')
	    		.replace(/[\t]/g, '\\t')
	    		.replace(/[\\][\"]/g, '\\\\\"')
	    		.replace(/\\'/g, "\\'");
	    	//alert(jsonStr);
		  	return JSON.parse(jsonStr);
		} catch (e) {
			alert("Error while parsing the JSON message: " + e);
		}
	}-*/;
}
