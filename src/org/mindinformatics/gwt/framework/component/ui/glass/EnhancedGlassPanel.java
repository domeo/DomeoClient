package org.mindinformatics.gwt.framework.component.ui.glass;

import org.mindinformatics.gwt.framework.src.Application;
import org.mindinformatics.gwt.framework.src.ApplicationResources;
import org.mindinformatics.gwt.framework.src.IApplication;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class EnhancedGlassPanel implements IContainerPanel, ResizeHandler {
		
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
	protected Panel contentPanel;
	protected SimplePanel titlePanel;
	protected SimplePanel helpButtonPanel;
	
	protected Image reportButtonImage;
	protected SimplePanel reportButtonPanel;
	protected Image exportButtonImage;
	protected SimplePanel exportButtonPanel;
	protected Image printButtonImage;
	protected SimplePanel printButtonPanel;
	protected Image closeButtonImage;
	protected SimplePanel closeButtonPanel;
	
	protected IApplication _application;
	protected ApplicationResources _resources;
	protected Composite _panel;
	
	protected int _width;
	
	protected boolean _isVisible;
	protected boolean _isPrintingEnabled;
	protected boolean _isExportEnabled;
	protected boolean _isReportEnabled;
	
	/**
	 * Constructor for the glass panel where the size of the panel is defined 
	 * by the with parameter.
	 * @param application	The pointer to the application
	 * @param panel			The panel with the content to be displayed in the glass pane
	 * @param title			The title of the glass pane
	 * @param width			The width of the content area
	 * @param isPrintingEnabled	The flag that enables content printing
	 * @param isExportEnabled	The flag that enables content export
	 * @param isReportEnabled	The flag that enables reporting the content
	 */
	public EnhancedGlassPanel(final IApplication application, Composite panel,
			String title, int width, boolean isPrintingEnabled, 
			boolean isExportEnabled, boolean isReportEnabled) {
		this(application, panel, title, width, isPrintingEnabled, isExportEnabled, isReportEnabled, null);
	}
	
	/**
	 * Constructor for the glass panel where the size of the panel is defined 
	 * by the with parameter.
	 * @param application	The pointer to the application
	 * @param panel			The panel with the content to be displayed in the glass pane
	 * @param title			The title of the glass pane
	 * @param width			The width of the content area
	 * @param isPrintingEnabled	The flag that enables content printing
	 * @param isExportEnabled	The flag that enables content export
	 * @param isReportEnabled	The flag that enables reporting the content
	 */
	public EnhancedGlassPanel(final IApplication application, Composite panel,
			String title, int width, boolean isPrintingEnabled, 
			boolean isExportEnabled, boolean isReportEnabled, ClickHandler closingHandler) {	
	localResources.enhancedGlassCss().ensureInjected();
		
		_application = application;
		_resources = Application.applicationResources;
		
		_panel = panel;
		if(panel instanceof IResizable) application.addResizeListener((IResizable) panel);
		
		_width = width;
		_isVisible = true;
		_isPrintingEnabled = isPrintingEnabled;
		_isExportEnabled = isExportEnabled;
		_isReportEnabled = isReportEnabled;
		
		((IContentPanel)panel).setContainer(this);

		glassPanel = new GlassPanel();

		contentPanel = new SimplePanel();
		contentPanel.setStyleName(STYLE_PANEL);
		contentPanel.add(panel);
		
		titlePanel = new SimplePanel();
		titlePanel.setStyleName(STYLE_PANEL_TITLE);
		titlePanel.add(new HTML("&nbsp;<strong>" + title + "</strong>"));
		
		closeButtonImage = new Image(localResources.crossLittleIcon().getSafeUri().asString());
		closeButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
		closeButtonImage.setTitle("Close");
		closeButtonImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(_panel instanceof IResizable) 
					_application.removeResizeListener((IResizable) _panel);
				unload();
				hide();
			}
		});
		
		if(closingHandler !=null) {
			closeButtonImage.addClickHandler(closingHandler);
		}
		
		closeButtonPanel = new SimplePanel();
		closeButtonPanel.setStyleName(STYLE_PANEL_ICON);
		closeButtonPanel.add(closeButtonImage);
		
		if(_isPrintingEnabled) {
			printButtonImage = new Image(_resources.printLittleIcon().getSafeUri().asString());
			printButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			printButtonImage.setTitle("Print");
			printButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Printing not yet enabled");
				}
			});
			
			printButtonPanel = new SimplePanel();
			printButtonPanel.setStyleName(STYLE_PANEL_ICON);
			printButtonPanel.add(printButtonImage);
		}
		
		if(_isExportEnabled) {
			exportButtonImage = new Image(_resources.exportLittleIcon().getSafeUri().asString());
			exportButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			exportButtonImage.setTitle("Export");
			exportButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(_panel instanceof IExportable) 
						((IExportable)_panel).export();
					else
						Window.alert("Export not enabled");
				}
			});
			
			exportButtonPanel = new SimplePanel();
			exportButtonPanel.setStyleName(STYLE_PANEL_ICON);
			exportButtonPanel.add(exportButtonImage);
		}
		
		if(_isReportEnabled) {
			reportButtonImage = new Image(_resources.sendLittleIcon().getSafeUri().asString());
			reportButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			reportButtonImage.setTitle("Report");
			reportButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Report not yet enabled");
				}
			});
			
			reportButtonPanel = new SimplePanel();
			reportButtonPanel.setStyleName(STYLE_PANEL_ICON);
			reportButtonPanel.add(reportButtonImage);
		}
		
		Window.addResizeHandler(this);
		resize();
		
		RootPanel.get().add(glassPanel, 0, 0);
	}
	
	/**
	 * Constructor for the glass panel where the size of the panel is taking the all 
	 * window client area.
	 * @param application	The pointer to the application
	 * @param panel			The panel with the content to be displayed in the glass pane
	 * @param title			The title of the glass pane
	 * @param isPrintingEnabled	The flag that enables content printing
	 * @param isExportEnabled	The flag that enables content export
	 * @param isReportEnabled	The flag that enables reporting the content
	 */
	public EnhancedGlassPanel(final IApplication application, Composite panel, 
			String title, boolean isPrintingEnabled, boolean isExportEnabled,
			boolean isReportEnabled) {	
		this(application, panel, title, -1,
				isPrintingEnabled, isExportEnabled, isReportEnabled);
	}
	
	public EnhancedGlassPanel(final IApplication application, Composite panel, 
			String title, boolean isPrintingEnabled, boolean isExportEnabled,
			boolean isReportEnabled, ClickHandler closeHandler) {	
		this(application, panel, title, -1,
				isPrintingEnabled, isExportEnabled, isReportEnabled, closeHandler);
	}
	
    public void onResize(ResizeEvent event) {
        resize();
    }
	
    private void resize() {
    	if(!_isVisible) return;
    	int iconsCounter = 0;
    	if(_panel instanceof IResizable) ((IResizable)_panel).resized();
    	
    	if(_width != -1) {
        	
        	if(_isPrintingEnabled) {
        		RootPanel.get().add(printButtonPanel, ((Window.getClientWidth()-((Window.getClientWidth()-_width)/2)) - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        	if(_isExportEnabled) {
        		RootPanel.get().add(exportButtonPanel, ((Window.getClientWidth()-((Window.getClientWidth()-_width)/2)) - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        	if(_isReportEnabled) {
        		RootPanel.get().add(reportButtonPanel, ((Window.getClientWidth()-((Window.getClientWidth()-_width)/2)) - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        
        	RootPanel.get().add(titlePanel, ((Window.getClientWidth()-_width)/2) + 10, 34);
        	RootPanel.get().add(contentPanel, (Window.getClientWidth()-_width)/2, 50);
        	RootPanel.get().add(closeButtonPanel, (Window.getClientWidth()-((Window.getClientWidth()-_width)/2)) - 10, 34);
        	contentPanel.setSize(_width + "px", "auto");
    	} else {
        	if(_isPrintingEnabled) {
        		RootPanel.get().add(printButtonPanel, (Window.getClientWidth() - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        	if(_isExportEnabled) {
        		RootPanel.get().add(exportButtonPanel, (Window.getClientWidth() - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        	if(_isReportEnabled) {
        		RootPanel.get().add(reportButtonPanel, (Window.getClientWidth() - 140 - iconsCounter*26), 34);
        		iconsCounter++;
        	}
        	
        	RootPanel.get().add(titlePanel, 60, 34);
        	RootPanel.get().add(contentPanel, 50, 50);
        	RootPanel.get().add(closeButtonPanel, Window.getClientWidth() - 90, 34);
        	contentPanel.setSize(Window.getClientWidth()-130 + "px", "auto");
    	}
    }
    
    public void unload() {
    	_application.getComponentsManager().unregisterComponent(_panel);
    }
	
	public void hide() {
		_isVisible = false;
		glassPanel.removeFromParent();
		contentPanel.removeFromParent();
		titlePanel.removeFromParent();
		closeButtonPanel.removeFromParent();
		if(_isPrintingEnabled) printButtonPanel.removeFromParent();
		if(_isExportEnabled) exportButtonPanel.removeFromParent();
		if(_isReportEnabled) reportButtonPanel.removeFromParent();
	}
	
	
	/*
	public EnhancedGlassPanel(final IApplication application, Composite panel, 
			String title, boolean isPrintingEnabled, boolean isExportEnabled,
			boolean isReportEnabled) {	
		
		localResources.enhancedGlassCss().ensureInjected();
		
		_applicaiton = application;
		_resources = Application.applicationResources;
		
		_panel = panel;
		if(panel instanceof IResizable) application.addResizeListener((IResizable) panel);
		
		_isVisible = true;
		_isPrintingEnabled = isPrintingEnabled;
		_isExportEnabled = isExportEnabled;
		_isReportEnabled = isReportEnabled;
		
		((IContentPanel)panel).setContainer(this);

		glassPanel = new GlassPanel();

		contentPanel = new SimplePanel();
		contentPanel.setStyleName(STYLE_PANEL);
		contentPanel.add(panel);
		
		titlePanel = new SimplePanel();
		titlePanel.setStyleName(STYLE_PANEL_TITLE);
		titlePanel.add(new HTML("&nbsp;<strong>" + title + "</strong>"));
		
		closeButtonImage = new Image(localResources.crossLittleIcon().getURL());
		closeButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
		closeButtonImage.setTitle("Close");
		closeButtonImage.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//annotator.resetSelection();
				if(_panel instanceof IResizable) 
					_applicaiton.removeResizeListener((IResizable) _panel);
				hide();
			}
		});
		
		closeButtonPanel = new SimplePanel();
		closeButtonPanel.setStyleName(STYLE_PANEL_ICON);
		closeButtonPanel.add(closeButtonImage);
		
		int iconCounter = 0;
		if(_isPrintingEnabled) {
			printButtonImage = new Image(_resources.printLittleIcon().getURL());
			printButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			printButtonImage.setTitle("Print");
			printButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Printing not yet enabled");
				}
			});
			
			printButtonPanel = new SimplePanel();
			printButtonPanel.setStyleName(STYLE_PANEL_ICON);
			printButtonPanel.add(printButtonImage);
			
			RootPanel.get().add(printButtonPanel, 60 + (Window.getClientWidth() - (180 + iconCounter*26)), 34);
			iconCounter++;
		}
		
		if(_isExportEnabled) {
			exportButtonImage = new Image(_resources.exportLittleIcon().getURL());
			exportButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			exportButtonImage.setTitle("Export");
			exportButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Export not yet enabled");
				}
			});
			
			exportButtonPanel = new SimplePanel();
			exportButtonPanel.setStyleName(STYLE_PANEL_ICON);
			exportButtonPanel.add(exportButtonImage);
			
			RootPanel.get().add(exportButtonPanel, 60 + (Window.getClientWidth() - (180 + iconCounter*26)), 34);
			iconCounter++;
		}
		
		if(_isReportEnabled) {
			reportButtonImage = new Image(_resources.sendLittleIcon().getURL());
			reportButtonImage.setStyleName(STYLE_PANEL_ICON_IMAGE);
			reportButtonImage.setTitle("Report");
			reportButtonImage.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Window.alert("Report not yet enabled");
				}
			});
			
			reportButtonPanel = new SimplePanel();
			reportButtonPanel.setStyleName(STYLE_PANEL_ICON);
			reportButtonPanel.add(reportButtonImage);
			
			RootPanel.get().add(reportButtonPanel, 60 + (Window.getClientWidth() - (180 + iconCounter*26)), 34);
			iconCounter++;
		}
		
		Window.addResizeHandler(this);
			
		RootPanel.get().add(titlePanel, 60, 34);
		RootPanel.get().add(closeButtonPanel, 60 + (Window.getClientWidth() - 155), 34);
		RootPanel.get().add(contentPanel, 50, 50);
		RootPanel.get().add(glassPanel, 0, 0);
	}
	*/
}
