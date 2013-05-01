package org.mindinformatics.gwt.framework.component.ui.glass;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationResources;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class DialogGlassPanel implements IContainerPanel, ResizeHandler {
		
	// ------------------------------------------------
	// STYLESHEET
	// ------------------------------------------------
	public static final EnhancedGlassResources localResources = 
		GWT.create(EnhancedGlassResources.class);
	
    public static final String STYLE_PANEL = 
    	localResources.enhancedGlassCss().enhancedGlassPanel();
    public static final String STYLE_PANEL_TITLE = 
    	localResources.enhancedGlassCss().enhancedGlassPanelTitle();
    public static final String STYLE_PANEL_CONTENT = 
    	localResources.enhancedGlassCss().enhancedGlassPanelContent();
    public static final String STYLE_PANEL_ICON = 
    	localResources.enhancedGlassCss().enhancedGlassPanelIcon();
    public static final String STYLE_PANEL_ICON_IMAGE = 
    	localResources.enhancedGlassCss().enhancedGlassPanelIconImage();
	
	
	protected GlassPanel glassPanel;
	protected SimplePanel contentPanel;

	protected IApplication _application;
	protected ApplicationResources _resources;
	private Composite _panel;
	
	public DialogGlassPanel(final IApplication application, Composite panel) {	
		
		localResources.enhancedGlassCss().ensureInjected();
		
		_panel = panel;
		_application = application;
		_resources = Application.applicationResources;
		
		if(panel instanceof IContentPanel) 
			((IContentPanel)panel).setContainer(this);

		glassPanel = new GlassPanel();

		contentPanel = new SimplePanel();
		contentPanel.setStyleName(STYLE_PANEL);
		contentPanel.add(panel);
		
		Window.addResizeHandler(this);
			
		RootPanel.get().add(contentPanel, 50, 50);
		RootPanel.get().add(glassPanel, 0, 0);
	}
	
	public Composite getPanel() {
		return _panel;
	}
	
    public void onResize(ResizeEvent event) {
        resize();
    }
	
    private void resize() {
    	contentPanel.setSize(Window.getClientWidth()-120 + "px", "auto");
    }
    
    public void hideSoon() {
		Timer t = new Timer() {
			public void run() {
				_application.removeDialog();
				glassPanel.removeFromParent();
				contentPanel.removeFromParent();
			}
		};

		// Schedule the timer to run once in 5 seconds.
		t.schedule(2000);	
	}
    
    public void hideSoon(int mill) {
		Timer t = new Timer() {
			public void run() {
				_application.removeDialog();
				glassPanel.removeFromParent();
				contentPanel.removeFromParent();
			}
		};

		// Schedule the timer to run once in 5 seconds.
		t.schedule(mill);	
	}
	
	public void hide() {
		_application.removeDialog();
		glassPanel.removeFromParent();
		contentPanel.removeFromParent();
	}
}
