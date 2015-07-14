package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model;

import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MTrustedResource;
import org.mindinformatics.gwt.framework.src.Utils;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class _ResourcesFactory {

	/**
	 * Creates a Generic Resource.
	 * Throws a InvalidParameterException if the url is not valid.
	 * @param url	The URI of the resource
	 * @param label	The label for human consumption
	 * @return The newly created resource.
	 */
	public static MGenericResource createGenericResource(String url, String label) {
		if(Utils.isValidUrl(url, true)) return new MGenericResource(url, label);
		throw new RuntimeException(
			"GenericResource with not valid URL: " + url + " (label: "+label+")");
	}
	
	/**
	 * Create the Trusted Resource with already existing source.
	 * @param url		The URI of the resource
	 * @param label		The label for human consumption
	 * @param source	The source as the Generic Resource
	 * @return The Trusted Resource (a Generic Resource with specified source)
	 */
	public static MTrustedResource createTrustedResource(String url, String label, MGenericResource source) {
		if(Utils.isValidUrl(url, true)) return new MTrustedResource(url, label, source);
		throw new RuntimeException(
			"TrustedResource with not valid URL: " + url + " (label: "+label+")");
	}
	
	/**
	 * Create the Trusted Resource and the source Generic Resource
	 * @param url			The URI of the resource
	 * @param label			The label for human consumption
	 * @param sourceUrl		The URI of the source
	 * @param sourceLabel	The label of the source
	 * @returnThe Trusted Resource (a Generic Resource with specified source)
	 */
	public static MTrustedResource createTrustedResource(String url, String label, String sourceUrl, String sourceLabel) {
		if(Utils.isValidUrl(sourceUrl, true)) 
			return createTrustedResource(url, label, createGenericResource(sourceUrl, sourceLabel));
		throw new RuntimeException(
			"TrustedResource source with not valid URL: " + url + " (label: "+label+")");
	}
	
	/**
	 * Creates a Linked Data Resource.
	 * Throws a InvalidParameterException if the url is not valid.
	 * @param url
	 * @param label
	 * @param description
	 * @param category
	 * @param sourceId
	 * @param sourceLabel
	 * @return
	 */
	public static MTrustedResource createLinkedDataResource(
			String url, String label, String description, String category, 
			String sourceId, String sourceLabel) {
		MTrustedResource res = new MTrustedResource(url,label, new MGenericResource(sourceId, sourceLabel));
		res.setDescription(description);
		//res.setSource(new MGenericResource(sourceId, sourceLabel));
		return res;
	}
}
