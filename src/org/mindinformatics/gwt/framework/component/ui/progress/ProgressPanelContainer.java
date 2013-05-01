package org.mindinformatics.gwt.framework.component.ui.progress;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationResources;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ProgressPanelContainer implements IContainerPanel /*, ResizeHandler*/ {
		
	// ------------------------------------------------
	// STYLESHEET
	// ------------------------------------------------
	public static final ProgressPanelContainerResources localResources = 
		GWT.create(ProgressPanelContainerResources.class);
	
    public static final String STYLE_PANEL = 
    	localResources.progressGlassPanelCss().progressPanel();
//    public static final String STYLE_PANEL_TITLE = 
//    	localResources.enhancedGlassCss().enhancedGlassPanelTitle();
//    public static final String STYLE_PANEL_CONTENT = 
//    	localResources.enhancedGlassCss().enhancedGlassPanelContent();
//    public static final String STYLE_PANEL_ICON = 
//    	localResources.enhancedGlassCss().enhancedGlassPanelIcon();
//    public static final String STYLE_PANEL_ICON_IMAGE = 
//    	localResources.enhancedGlassCss().enhancedGlassPanelIconImage();
	
	
	protected SimplePanel contentPanel;

	protected IApplication _application;
	protected ApplicationResources _resources;
	private ProgressPanel _panel;
	
	public ProgressPanelContainer(final IApplication application, ProgressPanel panel) {	
		
		localResources.progressGlassPanelCss().ensureInjected();
		
		_panel = panel;
		_application = application;
		_resources = Application.applicationResources;
		
		if(panel instanceof IContentPanel) 
			((IContentPanel)panel).setContainer(this);

		contentPanel = new SimplePanel();
		contentPanel.setStyleName(STYLE_PANEL);
		contentPanel.add(panel);
		
		//Window.addResizeHandler(this);
			
		RootPanel.get().add(contentPanel);
	}
	
	public void setProgressMessage(String message) {
		_panel.setProgressMessage(message);
		show();
	}
	
	public void setCompletionMessage(String message) {
		_panel.setCompletionMessage(message);
	    Timer t = new Timer() {
	    	public void run() {
	    		hide();
	    	}
	    };
	    // Schedule the timer to run once in 5 seconds.
	    t.schedule(5000);
	}
	
	public void setWarningMessage(String message) {
		_panel.setWarningMessage(message);
	    Timer t = new Timer() {
	    	public void run() {
	    		hide();
	    	}
	    };
	    // Schedule the timer to run once in 5 seconds.
	    t.schedule(5000);
	}
	
	public void setErrorMessage(String message) {
		_panel.setErrorMessage(message);
		Timer t = new Timer() {
			public void run() {
				hide();
			}
		};
		// Schedule the timer to run once in 5 seconds.
		t.schedule(5000);
	}
	
	public ProgressPanel getPanel() {
		return _panel;
	}
	
	/*
    public void onResize(ResizeEvent event) {
        resize();
    }
	
    private void resize() {
    	contentPanel.setSize(Window.getClientWidth()-120 + "px", "auto");
    }
    */
	
	public void hide() {
		_panel.clear();
		contentPanel.setVisible(false);
	}
	
	public void show() {
		contentPanel.setVisible(true);
	}
}
