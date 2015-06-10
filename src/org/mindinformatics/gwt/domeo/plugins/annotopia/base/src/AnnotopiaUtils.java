package org.mindinformatics.gwt.domeo.plugins.annotopia.base.src;

import org.mindinformatics.gwt.framework.src.ApplicationUtils;

import com.google.gwt.query.client.Properties;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotopiaUtils {

	public static final String ANNOTOPIA_API_KEY = "annotopia-api-key";
	
	/**
	 * Get the properties for the HTTP headers
	 * @return The list of properties for the header
	 */
	public static Properties getAnnotopiaHeaders() {
		Properties props = ApplicationUtils.getAnnotopiaOAuthToken();
		if(!ApplicationUtils.getAnnotopiaOauthEnabled().equalsIgnoreCase("true")) props.set(ApplicationUtils.AUTHORIZATION, AnnotopiaUtils.ANNOTOPIA_API_KEY + ApplicationUtils.SPACE + ApplicationUtils.getAnnotopiaApiKey());
		return props;
	}
}
