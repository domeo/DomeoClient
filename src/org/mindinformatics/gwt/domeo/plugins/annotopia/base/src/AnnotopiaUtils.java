package org.mindinformatics.gwt.domeo.plugins.annotopia.base.src;

import org.mindinformatics.gwt.framework.src.Utils;

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
		Properties props = Utils.getAnnotopiaOAuthToken();
		if(!Utils.getAnnotopiaOauthEnabled().equalsIgnoreCase("true")) props.set(Utils.AUTHORIZATION, AnnotopiaUtils.ANNOTOPIA_API_KEY + Utils.SPACE + Utils.getAnnotopiaApiKey());
		return props;
	}
}
