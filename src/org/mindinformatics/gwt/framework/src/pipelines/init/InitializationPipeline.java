package org.mindinformatics.gwt.framework.src.pipelines.init;

import org.mindinformatics.gwt.framework.component.pipelines.src.APipeline;
import org.mindinformatics.gwt.framework.component.pipelines.src.IStage;
import org.mindinformatics.gwt.framework.component.pipelines.src.Stage;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.commands.InitAllAvailableProfilesCommand;
import org.mindinformatics.gwt.framework.src.commands.InitReportCommand;
import org.mindinformatics.gwt.framework.src.commands.InitSoftwareCommand;
import org.mindinformatics.gwt.framework.src.commands.InitUserAvailableProfilesCommand;
import org.mindinformatics.gwt.framework.src.commands.InitUserCurrentProfileCommand;
import org.mindinformatics.gwt.framework.src.commands.InitUserGroupsCommand;
import org.mindinformatics.gwt.framework.src.commands.InitUserInfoCommand;
import org.mindinformatics.gwt.framework.src.commands.InitUserPersonCommand;

import com.google.gwt.user.client.Window;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class InitializationPipeline extends APipeline {
	
	public InitializationPipeline(IApplication application) {
		super(application);
		try {
			IStage usersInfoStage = new Stage(new InitUserInfoCommand(application, application, this));
			_stages.add(usersInfoStage);
			
			IStage usersGroupsStage = new Stage(new InitUserGroupsCommand(application, application, this));
			_stages.add(usersGroupsStage);
			
			IStage personStage = new Stage(new InitUserPersonCommand(application, application, this));
			_stages.add(personStage);
			
			IStage softwareStage = new Stage(new InitSoftwareCommand(application, application, this));
			_stages.add(softwareStage);
			
			IStage reportStage = new Stage(new InitReportCommand(application, application, this));
			_stages.add(reportStage);
			
			IStage profileStage = new Stage(new InitUserCurrentProfileCommand(application, application, this));
			_stages.add(profileStage);
			
			IStage profilesStage = new Stage(new InitUserAvailableProfilesCommand(application, application, this));
			_stages.add(profilesStage);
			
			IStage allProfilesStage = new Stage(new InitAllAvailableProfilesCommand(application, application, this));
			_stages.add(allProfilesStage);
		} catch (Exception e) {
			Window.alert("InitializationPipeline " + e.getMessage());
		}
	}
	
	@Override
	public String getPipelineName() { return "Initialization"; }

	@Override
	public void notifyPipelineTermination(long startTime) {
		_application.getInitializer().addMessage( 
				"Initialization pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		_application.getLogger().debug(this.getClass().getName(), 
			"Initialization pipeline completed in " + (System.currentTimeMillis()-startTime) + "ms");
		_application.notifyEndInitialization();
	}
}
