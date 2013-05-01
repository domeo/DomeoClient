package org.mindinformatics.gwt.framework.component.reporting.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * The async counterpart of <code>ProfilesService</code>.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ReportingServiceAsync {
	void sendWidgetAsEmail(String sourceComponent, String title, Widget message, String url, AsyncCallback<String> callback) 
			throws IllegalArgumentException;
}

