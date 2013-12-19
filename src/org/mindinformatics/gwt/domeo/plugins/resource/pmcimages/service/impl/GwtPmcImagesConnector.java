package org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.impl;

import java.util.HashMap;

import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.model.JsPmcImage;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.IPmcImagesConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.pmcimages.service.IPmcImagesRequestCompleted;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class GwtPmcImagesConnector implements IPmcImagesConnector{

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
	
	@Override
	public void retrievePmcImagesData(
			IPmcImagesRequestCompleted completionCallback, String pmid,
			String pmcid, String doi) throws IllegalArgumentException {
		String response = 
			"[{\"uysie:hasCaption\": \"Representative self-terminating radical reactions.\"," + 
			"\"uysie:hasFullText\": \"Most organic radical reactions occur through a cascade of two or more individual steps [1,2]. Knowledge of the nature and rates of these steps Ì¢‰âÂ‰ÛÏ in other words, the mechanism of the reaction Ì¢‰âÂ‰ÛÏ is of fundamental interest and is also important in synthetic planning. In synthesis, both the generation of the initial radical of the cascade and the removal of the final radical are crucial events [3]. Many useful radical reactions occur through chains that provide a naturally coupled regulation of radical generation and removal. Among the non-chain methods, generation and removal of radicals by oxidation and reduction are important, as is the\"," +
			"\"uysie:hasFileName\": \"nihms28314f3\"," + 
			"\"uysie:hasTitle\": \"Do alpha-acyloxy and alpha-alkoxycarbonyloxy radicals fragment to form acyl and alkoxycarbonyl radicals?\"}]";
		
		@SuppressWarnings("unchecked")
		JsArray<JsPmcImage> responseOnSets = (JsArray<JsPmcImage>) parseJson(response);
		HashMap<String, JsPmcImage> images = new HashMap<String, JsPmcImage>();
		for(int i=0; i<responseOnSets.length(); i++) {
			images.put(responseOnSets.get(i).getName(), responseOnSets.get(i));
		}
		
		completionCallback.returnPmcImagesData(images);
	}
}
