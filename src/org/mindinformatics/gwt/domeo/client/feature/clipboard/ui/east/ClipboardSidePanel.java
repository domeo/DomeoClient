package org.mindinformatics.gwt.domeo.client.feature.clipboard.ui.east;

import java.util.ArrayList;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.framework.component.IClipboardRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ClipboardSidePanel extends ASidePanel implements IInitializableComponent, IRefreshableComponent, IClipboardRefreshableComponent {

	interface Binder extends UiBinder<Widget, ClipboardSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	@UiField ScrollPanel container;
	@UiField VerticalPanel content;
	@UiField SimplePanel topbarPanel;
	
	private ClipboardSidePanelTopbar toolbar;
	
	// By contract 
	private IDomeo _domeo;
	
	public ClipboardSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		_domeo = (IDomeo) domeo;
		
		initWidget(binder.createAndBindUi(this));
		body.setHeight("100%");	
		container.setHeight("100%");
		
		toolbar = new ClipboardSidePanelTopbar(_domeo, this);
		topbarPanel.add(toolbar);
	}
	
	public void init() {
		_application.getLogger().debug(this, "Initializing...");
		toolbar.init();
	}
	
	public void refresh() {
		_application.getLogger().debug(this, "Refreshing...");
		toolbar.refresh();
		
		content.clear();
		
		ArrayList<MAnnotation>  annotations = _domeo.getClipboardManager().getBufferedAnnotation();
		for(MAnnotation annotation: annotations) {
			ITileComponent c = _domeo.getAnnotationTailsManager().getAnnotationTile(annotation.getClass().getName(), _domeo.getContentPanel().getAnnotationFrameWrapper());
			if(c==null) {
				VerticalPanel vp = new VerticalPanel();
				vp.add(new Label(annotation.getLocalId() + " - " + annotation.getClass().getName() + " - " + annotation.getY()));
				content.add(vp);
			} else {
				try {
					c.initializeLens(annotation);
					content.add(c.getTile());
				} catch(Exception e) {
					// If something goes wrong just display the default tile
					VerticalPanel vp = new VerticalPanel();
					vp.add(new Label(annotation.getLocalId() + " - " + annotation.getClass().getName() + " - " + annotation.getY()));
					content.add(vp);
				}
			}
		}
	}
}
