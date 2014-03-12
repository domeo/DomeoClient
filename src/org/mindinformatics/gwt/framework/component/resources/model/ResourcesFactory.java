package org.mindinformatics.gwt.framework.component.resources.model;

import org.mindinformatics.gwt.framework.src.ApplicationUtils;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ResourcesFactory {

	/**
	 * Creates a Generic Resource. Throws a RuntimeException if the url is not
	 * valid.
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @return The newly created resource.
	 */
	public static MGenericResource createGenericResource(String url,
			String label) {
		if (ApplicationUtils.isValidUrl(url, false))
			return new MGenericResource(url, label);
		throw new RuntimeException("GenericResource with not valid URL: " + url
				+ " (label: " + label + ")");
	}

	/**
	 * Creates a Linked Resource.
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @return The Linked Resource
	 */
	public static MLinkedResource createLinkedResource(String url, String label) {
		if (ApplicationUtils.isValidUrl(url, true))
			return new MLinkedResource(url, label);
		throw new RuntimeException("LinkedResource with not valid URL: " + url
				+ " (label: " + label + ")");
	}

	/**
	 * Creates a Linked Resource.
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param description
	 *            The description for human consumption
	 * @return The Linked Resource
	 */
	public static MLinkedResource createLinkedResource(String url,
			String label, String description) {
		if (ApplicationUtils.isValidUrl(url, true))
			return new MLinkedResource(url, label, description);
		throw new RuntimeException("LinkedResource with not valid URL: " + url
				+ " (label: " + label + ")");
	}

	/**
	 * Create the Trusted Resource with already existing source.
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param source
	 *            The source as the Generic Resource
	 * @return The Trusted Resource (a Generic Resource with specified source)
	 */

	// temporary cancel theZZZ
	public static MTrustedResource createTrustedResource(String url,
			String label, MGenericResource source) {

		if (ApplicationUtils.isValidUrl(url, true))
			return new MTrustedResource(url, label, source);
		throw new RuntimeException("TrustedResource with not valid URL: " + url
				+ " (label: " + label + ")");
	}

	/**
	 * Create the Trusted Resource with already existing source.
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param description
	 *            The description for human consumption
	 * @param source
	 *            The source as the Generic Resource
	 * @return The Trusted Resource (a Generic Resource with specified source)
	 */
	public static MTrustedResource createTrustedResource(String url,
			String label, String description, MGenericResource source) {
		MTrustedResource trustedResource = createTrustedResource(url, label,
				source);
		trustedResource.setDescription(description);
		return trustedResource;
	}

	/**
	 * Create the Trusted Resource and the source Generic Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param sourceUrl
	 *            The URI of the source
	 * @param sourceLabel
	 *            The label of the source
	 * @returnThe Trusted Resource (a Generic Resource with specified source)
	 */
	public static MTrustedResource createTrustedResource(String url,
			String label, String sourceUrl, String sourceLabel) {
		if (ApplicationUtils.isValidUrl(sourceUrl, true))
			return createTrustedResource(url, label,
					createGenericResource(sourceUrl, sourceLabel));
		throw new RuntimeException(
				"TrustedResource source with not valid URL: " + url
						+ " (label: " + label + ")");
	}

	/**
	 * Create the Trusted Resource and the source Generic Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param description
	 *            The description for human consumption
	 * @param sourceUrl
	 *            The URI of the source
	 * @param sourceLabel
	 *            The label of the source
	 * @returnThe Trusted Resource (a Generic Resource with specified source)
	 */
	public static MTrustedResource createTrustedResource(String url,
			String label, String description, String sourceUrl,
			String sourceLabel) {
		if (ApplicationUtils.isValidUrl(sourceUrl, true)) {
			return createTrustedResource(url, label, description,
					createGenericResource(sourceUrl, sourceLabel));
		}
		throw new RuntimeException(
				"TrustedResource source with not valid URL: " + url
						+ " (label: " + label + ")");
	}

	/**
	 * Create the Typed Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @returnThe Typed Resource (a Linked Resource with types)
	 */
	public static MTypedResource createTypedResource(String url, String label) {
		if (ApplicationUtils.isValidUrl(url, true))
			return new MTypedResource(url, label);
		throw new RuntimeException("TypedResource with not valid URL: " + url
				+ " (label: " + label + ")");
	}

	/**
	 * Create the Trusted Typed Resource and the source Generic Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param source
	 *            The source as the Generic Resource
	 * @return Trusted Typed Resource (a Trusted Resource with types)
	 */
	public static MTrustedTypedResource createTrustedTypedResource(String url,
			String label, MGenericResource source) {
		if (ApplicationUtils.isValidUrl(url, true))
			return new MTrustedTypedResource(url, label, source);
		throw new RuntimeException("TrustedTypedResource with not valid URL: "
				+ url + " (label: " + label + ")");
	}

	/**
	 * Create the Trusted Typed Resource and the source Generic Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param source
	 *            The source as the Generic Resource
	 * @return Trusted Typed Resource (a Trusted Resource with types)
	 */
	public static MTrustedTypedResource createTrustedTypedResource(String url,
			String label, String description, String type,
			MGenericResource source) {
		MTrustedTypedResource r = createTrustedTypedResource(url, label, source);
		r.setDescription(description);
		r.addTypeUri(type);
		return r;
	}

	/**
	 * Create the Trusted Typed Resource and the source Generic Resource
	 * 
	 * @param url
	 *            The URI of the resource
	 * @param label
	 *            The label for human consumption
	 * @param source
	 *            The source as the Generic Resource
	 * @return Trusted Typed Resource (a Trusted Resource with types)
	 */
	public static MTrustedTypedResource createTrustedTypedResource(String url,
			String label, String description, String type, String sourceUrl,
			String sourceLabel) {
		MTrustedTypedResource r = createTrustedTypedResource(url, label,
				createGenericResource(sourceUrl, sourceLabel));
		r.setDescription(description);
		r.addTypeUri(type);
		return r;
	}
}
