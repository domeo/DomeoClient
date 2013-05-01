package org.mindinformatics.gwt.framework.component.reporting.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.Widget;

/**
 * The client side stub for the RPC service.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
@RemoteServiceRelativePath("reporting")
public interface ReportingService extends RemoteService {
	String sendWidgetAsEmail(String sourceComponent, String title, Widget message, String url)  throws IllegalArgumentException;
}
