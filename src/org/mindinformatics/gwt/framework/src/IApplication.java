package org.mindinformatics.gwt.framework.src;

import org.mindinformatics.gwt.framework.component.ComponentsManager;
import org.mindinformatics.gwt.framework.component.initializer.ui.InitializerPanel;
import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;
import org.mindinformatics.gwt.framework.component.preferences.src.PreferencesManager;
import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.component.reporting.src.IReportsManager;
import org.mindinformatics.gwt.framework.component.resources.management.ResourcesManager;
import org.mindinformatics.gwt.framework.component.styles.src.StylesManager;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.ResourcePanelsManager;
import org.mindinformatics.gwt.framework.component.ui.progress.ProgressPanelContainer;
import org.mindinformatics.gwt.framework.component.users.src.IUserManager;
import org.mindinformatics.gwt.framework.src.commands.InitAgentManagerCommandCallback;
import org.mindinformatics.gwt.framework.src.commands.InitProfileManagerCommandCallback;
import org.mindinformatics.gwt.framework.src.commands.InitReportManagerCommandCallback;
import org.mindinformatics.gwt.framework.src.commands.InitUserManagerCommandCallback;
import org.mindinformatics.gwt.framework.src.pipelines.init.InitializationPipelineCallback;
import org.mindinformatics.gwt.framework.src.pipelines.init.ProcessingPipelineCallback;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface IApplication extends InitUserManagerCommandCallback, InitAgentManagerCommandCallback, 
		InitializationPipelineCallback, InitProfileManagerCommandCallback, InitReportManagerCommandCallback,
		ProcessingPipelineCallback {
	
	public boolean isHostedMode();
	public boolean isStandaloneMode();
	public boolean isTestFilesOn();
	public boolean isLocalResources();
	
	//public IPersistenceManager getAnnotationPersistenceManager();
	//
	public String getApplicationName();
	public String getApplicationVersion();
	public String getApplicationVersionLabel();
	// Resizing
	public void addResizeListener(IResizable widget);
	public void removeResizeListener(IResizable widget);
	// Resources panels
	public ResourcePanelsManager getResourcePanelsManager();
	// Logging
	public LogsManager getLogger();
	public void notifyException(String className, String message);
	// Initializer
	public InitializerPanel getInitializer();
	// Preferences
	public PreferencesManager getPreferences();
	// User Manager
	public IUserManager getUserManager();
	// User Manager
	public IProfileManager getProfileManager();
	public IReportsManager getReportManager();
	// Dialogs
	public void addDialogGlassPanel(Composite panel);
	public DialogGlassPanel getDialogPanel();
	public void removeDialog();
	
	public ResourcesManager getResourcesManager();
	public ComponentsManager getComponentsManager();
	
	public StylesManager getCssManager();
	
	// Side panels management
	public SidePanelsFacade getSidePanelsFacade();
	
	public  ProgressPanelContainer getProgressPanelContainer();
}
