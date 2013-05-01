package org.mindinformatics.gwt.framework.component.agents.src.testing;

import org.mindinformatics.gwt.framework.component.agents.model.MAgentPerson;
import org.mindinformatics.gwt.framework.component.agents.model.MAgentSoftware;
import org.mindinformatics.gwt.framework.component.agents.service.AgentsService;
import org.mindinformatics.gwt.framework.component.agents.service.AgentsServiceAsync;
import org.mindinformatics.gwt.framework.component.agents.src.AAgentManager;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.ICommandCompleted;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GwtAgentManager extends AAgentManager {

	public GwtAgentManager(IApplication application, ICommandCompleted callbackCompleted) {
		super(application, callbackCompleted);
	}
	
	@Override
	public void retrieveSoftware(String name, String version) {

		final AsyncCallback<MAgentSoftware> userServiceCallback = new AsyncCallback<MAgentSoftware>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(MAgentSoftware softwareFound) {
				setSoftware(softwareFound);
				stageCompleted();
			}
		};

		final AgentsServiceAsync agentsService = GWT.create(AgentsService.class);
		((ServiceDefTarget) agentsService).setServiceEntryPoint(GWT.getModuleBaseURL() + "agents");
		agentsService.getSoftwareInfo(name, version, userServiceCallback);
	}
	
	@Override
	public void retrievePerson(String email) {

		final AsyncCallback<MAgentPerson> userServiceCallback = new AsyncCallback<MAgentPerson>() {
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get response from server" + caught.getMessage());
			}

			public void onSuccess(MAgentPerson personFound) {
				setUserPerson(personFound);
				stageCompleted();
			}
		};

		final AgentsServiceAsync agentsService = GWT.create(AgentsService.class);
		((ServiceDefTarget) agentsService).setServiceEntryPoint(GWT.getModuleBaseURL() + "agents");
		agentsService.getPersonInfo(email, userServiceCallback);
	}
}
