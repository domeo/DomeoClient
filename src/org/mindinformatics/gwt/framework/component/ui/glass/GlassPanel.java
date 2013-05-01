package org.mindinformatics.gwt.framework.component.ui.glass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class GlassPanel extends Composite implements ResizeHandler {
	
	// ------------------------------------------------
	// STYLESHEET
	// ------------------------------------------------
	public static final GlassResources localResources = 
		GWT.create(GlassResources.class);
	
    public static final String STYLE_PANEL = 
    	localResources.glassCss().glassPanel();

    private SimplePanel panel = new SimplePanel();

    public GlassPanel() {
        initWidget(panel);
        localResources.glassCss().ensureInjected();
        
        panel.setStylePrimaryName(STYLE_PANEL);
        
        Window.addResizeHandler(this);
        resize();
    }

    public void onResize(ResizeEvent event) {
        resize();
    }

    public void show() {
        // Override the styles explicitly, because it's needed
        // every time the widget is detached
        Element elem = panel.getElement();
        DOM.setElementProperty(elem, "left", "0");
        DOM.setElementProperty(elem, "top", "0");
        DOM.setElementProperty(elem, "position", "absolute");
    }

    public void hide() {
        RootPanel.get().remove(this);
    }

    private void resize() {
        panel.setSize(Window.getClientWidth() + "px",
                Window.getClientHeight() + "px");
    }
}
