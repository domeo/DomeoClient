package org.mindinformatics.gwt.framework.component.logging.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface LoggingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
}
