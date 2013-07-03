package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model;

import org.mindinformatics.gwt.domeo.client.Resources;

import com.google.gwt.resources.client.ImageResource;

public interface MicroPublicationsResources extends Resources {

	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/micropubs/ui/icons/document-green.gif")
	ImageResource supportedBy();
	
	@Source("org/mindinformatics/gwt/domeo/plugins/annotation/micropubs/ui/icons/document-red.gif")
	ImageResource challengedBy();
}
