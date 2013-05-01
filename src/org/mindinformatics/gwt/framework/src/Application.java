package org.mindinformatics.gwt.framework.src;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.framework.component.ComponentsManager;
import org.mindinformatics.gwt.framework.component.agents.src.IAgentManager;
import org.mindinformatics.gwt.framework.component.initializer.ui.InitializerPanel;
import org.mindinformatics.gwt.framework.component.logging.src.LogsManager;
import org.mindinformatics.gwt.framework.component.logging.ui.LogViewerPanel;
import org.mindinformatics.gwt.framework.component.preferences.src.PreferencesManager;
import org.mindinformatics.gwt.framework.component.profiles.src.IProfileManager;
import org.mindinformatics.gwt.framework.component.reporting.src.IReportsManager;
import org.mindinformatics.gwt.framework.component.resources.management.ResourcesManager;
import org.mindinformatics.gwt.framework.component.styles.src.StylesManager;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;
import org.mindinformatics.gwt.framework.component.ui.glass.DialogGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.component.ui.lenses.resources.ResourcePanelsManager;
import org.mindinformatics.gwt.framework.component.ui.progress.ProgressPanel;
import org.mindinformatics.gwt.framework.component.ui.progress.ProgressPanelContainer;
import org.mindinformatics.gwt.framework.component.users.src.HTMLUserUtils;
import org.mindinformatics.gwt.framework.component.users.src.IUserManager;
import org.mindinformatics.gwt.framework.src.pipelines.init.InitializationPipeline;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class Application implements IApplication {
	
	public static final String PREF_AUTOMATICALLY_REPORT_ISSUES = "Automatically report issues";
	
	/**
	 * The static images used throughout the Domeo application.
	 */
	public static final ApplicationResources applicationResources = 
		GWT.create(ApplicationResources.class);
	
	private Set<IResizable> resizeListeners = new HashSet<IResizable>();

	private ProgressPanelContainer progressPanelContainer;
	public  ProgressPanelContainer getProgressPanelContainer() {
		return progressPanelContainer;
	}
	
	public void initApplication() {
		
		this.logger.info(this.getClass().getName(), 
			"Creating Application Framework...");
		
		// Is storage enabled
		HtmlUtils htmlUtils = new HtmlUtils();
		this.logger.debug(this.getClass().getName(), 
			"Storage enabled: " + htmlUtils.isStorageEnabled());
			
		RootPanel.get("af-init").add(initializer);
		initializer.updateMessage("Initializing...");
		
		applicationResources.css().ensureInjected();
		
		initializer.updateMessage("Initializing shortcut keys...");
		keyInit();
		this.logger.debug(this.getClass().getName(), 
			"Definition of shortcut keys");
		
		initializer.updateMessage("Initialization pipeline...");
		InitializationPipeline ip = new InitializationPipeline(this);
		ip.start(new HashMap<String, String>());
		
		progressPanelContainer = new ProgressPanelContainer(this, new ProgressPanel());
		progressPanelContainer.hide();
	}
	
	public void updateMessage(String message) {
		initializer.updateMessage(message);
	}
	
	public void addResizeListener(IResizable widget) {
		resizeListeners.add(widget);
	}
	
	public void removeResizeListener(IResizable widget) {
		resizeListeners.remove(widget);
	}
	
	protected void notifyResizing() {
		for(IResizable item: resizeListeners) {
			item.resized();
		}
	}
	
	public void notifyEndInitialization() {
		
		this.getInitializer().updateMessage("Completing initialization pipeline");
		
		this.logger.info(this.getClass().getName(), 
			"Application [" + getAgentManager().getSoftware().getName() + "]");
		this.getInitializer().addMessage( 
				"Application [" + getAgentManager().getSoftware().getName() + "]");
		this.logger.info(this.getClass().getName(),
			"User [" + getUserManager().getUser().getUserName() + "]");
		this.getInitializer().addMessage( 
				"User [" + getUserManager().getUser().getUserName() + "]");
		this.logger.info(this.getClass().getName(),
			"Profile [" + getProfileManager().getUserCurrentProfile().getName() + "]");
		this.getInitializer().addMessage( 
				"Profile [" + getProfileManager().getUserCurrentProfile().getName() + "]");
		this.logger.info(this.getClass().getName(), 
			"Creation Application Framework completed");
		
		completeInitialization();
	}
	
	public abstract void completeInitialization();
	
	// ========================================================================
	// Application details
	// ========================================================================
	public abstract String getApplicationName();
	public abstract String getApplicationVersion();
	public abstract String getApplicationVersionLabel();
	
	// ========================================================================
	// Initializer
	// ========================================================================
	protected InitializerPanel initializer = new InitializerPanel(this);
	public InitializerPanel getInitializer() { return initializer; }	
	
	// ========================================================================
	// Logger
	// ========================================================================
	protected LogsManager logger = new LogsManager();
	public LogsManager getLogger() { return logger; }
	public void notifyException(String className, String message) {
		logger.exception(className, message);
	}
	
	// ========================================================================
	// Key Management
	// ------------------------------------------------------------------------
	// This allows hooking up actions to specific keys combinations. The 
	// interpretation of the keys is left to the instance of the application.
	// ========================================================================
	protected native void keyInit() /*-{ 		
		var _this = this;
		$doc.onkeypress = function(evt) { 
			_this.@org.mindinformatics.gwt.framework.src.Application::ifKeyPress(Lcom/google/gwt/user/client/Event;)(evt || $wnd.event); 
		} 
	}-*/; 
	
	// Override for more or different  options
	// The numeric key code is the position in the alphabet
	public void ifKeyPress(Event event) { 
		try {
			int keyCode = event.getCharCode();
			//Window.alert("blah>1 " + keyCode);
			boolean ctrl = DOM.eventGetCtrlKey(event);
			if((keyCode == 'l'|| keyCode == 12 || keyCode == 108) && ctrl) {
				LogViewerPanel lwp = new LogViewerPanel(this);
				new EnhancedGlassPanel(this, lwp, lwp.getTitle(), true, true, true);
			} 
			if((keyCode == 'w') && ctrl) {
				LogViewerPanel lwp = new LogViewerPanel(this);
				new EnhancedGlassPanel(this, lwp, lwp.getTitle(), true, true, true);
			}
			if((keyCode == 't'|| keyCode == 20) && ctrl) {
				Window.alert("Plugin manager not implemented yet");
			} 
		} catch(Exception e) {
			Window.alert(e.getMessage());
		}
	}

	// ========================================================================
	// User Management
	// ------------------------------------------------------------------------
	// Every application assumes the user is known
	// ========================================================================
	protected IUserManager _userManager;
	public void setUserManager(IUserManager userManager) { 
		_userManager = userManager; 
		getLogger().debug(this.getClass().getName(), 
				"Loaded User Manager " + _userManager.getClass().getName());
		getInitializer().addMessage(
				"Loaded User Manager " + _userManager.getClass().getName());
	}
	public IUserManager getUserManager() { return _userManager; }
	
	public abstract IUserManager selectUserManager(ICommandCompleted completionCallback);
	
	// ========================================================================
	// Agent Management
	// ------------------------------------------------------------------------
	// 
	// ========================================================================
	protected IAgentManager _agentManager;
	public void setAgentManager(IAgentManager agentManager) { 
		_agentManager = agentManager; 
		getLogger().debug(this.getClass().getName(), 
				"Loaded Agent Manager " + _agentManager.getClass().getName());
		getInitializer().addMessage(
				"Loaded Agent Manager " + _agentManager.getClass().getName());
	}
	public IAgentManager getAgentManager() { return _agentManager; }
	
	public abstract IAgentManager selectAgentManager(ICommandCompleted completionCallback);
	
	
	// ========================================================================
	// Profiles Management
	// ------------------------------------------------------------------------
	// 
	// ========================================================================
	protected IProfileManager _profileManager;
	public void setProfileManager(IProfileManager profileManager) { 
		_profileManager = profileManager; 
		getLogger().debug(this.getClass().getName(), 
				"Loaded Profile Manager " + _profileManager.getClass().getName());
		getInitializer().addMessage(
				"Loaded Profile Manager " + _profileManager.getClass().getName());
	}
	public IProfileManager getProfileManager() { return _profileManager; }
	
	public abstract IProfileManager selectProfileManager(ICommandCompleted completionCallback);
	
	// ========================================================================
	// Reports Management
	// ------------------------------------------------------------------------
	// 
	// ========================================================================
	protected IReportsManager _reportsManager;
	public void setReportManager(IReportsManager reportsManager) { 
		_reportsManager = reportsManager; 
		getLogger().debug(this.getClass().getName(), 
				"Loaded Reports Manager " + _reportsManager.getClass().getName());
		getInitializer().addMessage(
				"Loaded Reports Manager " + _reportsManager.getClass().getName());
	}
	public IReportsManager getReportManager() { return _reportsManager; }
	
	public abstract IReportsManager selectReportManager(ICommandCompleted completionCallback);
	
	// ========================================================================
	// Deployment mode
	// ========================================================================
	public boolean isHostedMode() { return !GWT.isScript();}
	public boolean isStandaloneMode() { 
		return new Boolean(HTMLUserUtils.getStandaloneFlag()).booleanValue(); } 
	public boolean isTestFilesOn() { 
		return new Boolean(HTMLUserUtils.getTestFilesFlag()).booleanValue(); } 
	public boolean isJsonFormat() {
		return new Boolean(HTMLUserUtils.getJsonFormatFlag()).booleanValue(); } 
	public boolean isLocalResources() {
        return new Boolean(HTMLUserUtils.getLocalResourcesFlag()).booleanValue(); } 

	public String getStartingMode() {
		if(!isHostedMode()) {
			if(isStandaloneMode()) return "in standalone testing mode";
			else return "in hosted mode";
		} else {
			return "in real mode";
		}
	}
	
	// ========================================================================
	// Preferences
	// ========================================================================
	protected PreferencesManager preferences = new PreferencesManager();
	public PreferencesManager getPreferences() { return preferences; }
	
	// ========================================================================
	// Stylesheet
	// ========================================================================
	protected StylesManager stylesheet = new StylesManager();
	public StylesManager getCssManager() { return stylesheet; }
	
	// ========================================================================
	// Components
	// ========================================================================
	protected ComponentsManager componentsManager = new ComponentsManager(this);
	public ComponentsManager getComponentsManager() {
		return componentsManager;
	}
	
	// ========================================================================
	// Resources
	// ========================================================================
	protected ResourcesManager resourcesManager = new ResourcesManager();
	public ResourcesManager getResourcesManager() {
		return resourcesManager;
	}	
	
	// ========================================================================
	// Resource Panels Manager
	// ========================================================================
	protected ResourcePanelsManager resourcePanelsManager = new ResourcePanelsManager();
	public ResourcePanelsManager getResourcePanelsManager() {
		return resourcePanelsManager;
	}

	// ========================================================================
	// Dialogs
	// ========================================================================
	protected DialogGlassPanel _dialogPanel;
	public void removeDialog() { _dialogPanel = null; }
	public void addDialogGlassPanel(Composite panel) {
		_dialogPanel = new DialogGlassPanel(this, panel);
	}
	public DialogGlassPanel getDialogPanel() {
		return _dialogPanel;
	}
	
	// ========================================================================
	// Side panels
	// ========================================================================
	protected SidePanelsFacade sidePanelsFacade;
	public SidePanelsFacade getSidePanelsFacade() {
		return sidePanelsFacade;
	}
}
