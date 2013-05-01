package org.mindinformatics.gwt.utils.src;

import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;

import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

public class ResourcesUtils {

	public static final Image getImage(final LogsManager logger, final String url, final ImageResource defaultUrl) {
		final Image img = new Image();
		img.addErrorHandler(new ErrorHandler() {
			@Override
			public void onError(ErrorEvent event) {
				logger.warn(this, "Could not laod the image: " + url);
				logger.warn(this, "Default image loaded instead");
				img.setUrl(defaultUrl.getSafeUri());
			}
		});
		img.setUrl(url);
		return img;
	}
}
