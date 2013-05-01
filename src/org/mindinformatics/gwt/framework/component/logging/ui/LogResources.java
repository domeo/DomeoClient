package org.mindinformatics.gwt.framework.component.logging.ui;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface LogResources extends ClientBundle {
		
	@Source("org/mindinformatics/gwt/framework/component/logging/ui/icons/exception.gif")
	ImageResource loggingException();
	
	@Source("org/mindinformatics/gwt/framework/component/logging/ui/icons/warning.gif")
	ImageResource loggingWarning();
	
	@Source("org/mindinformatics/gwt/framework/component/logging/ui/icons/command.gif")
	ImageResource loggingCommand();
	
	@Source("org/mindinformatics/gwt/framework/component/logging/ui/icons/info.gif")
	ImageResource loggingInfo();
	
	@Source("org/mindinformatics/gwt/framework/component/logging/ui/icons/debug.gif")
	ImageResource loggingDebug();
}
