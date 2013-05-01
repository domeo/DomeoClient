package org.mindinformatics.gwt.domeo.client.feature.clipboard.ui.east;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.toolbar.ToolbarHorizontalPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ClipboardSidePanelTopbar  extends Composite 
	implements IInitializableComponent, IRefreshableComponent {

	interface Binder extends UiBinder<VerticalPanel, ClipboardSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	@SuppressWarnings("unused")
	private ClipboardSidePanel _clipboardSidePanel;
	
	@UiField SimplePanel clearButtonPanel;

	public ClipboardSidePanelTopbar(IDomeo domeo, final ClipboardSidePanel clipboardSidePanel) {
		_domeo = domeo;
		_clipboardSidePanel = clipboardSidePanel;
		
		initWidget(binder.createAndBindUi(this));
	
		Resources _resources = Domeo.resources;
		
		ToolbarHorizontalPanel clearButton = new ToolbarHorizontalPanel(
			_domeo, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getContentPanel().getAnnotationFrameWrapper().clearTemporaryAnnotations();
				}
			}, _resources.clearIcon().getSafeUri().asString(), "Clear", "Clear Clipboard");
		clearButtonPanel.add(clearButton);
		
		// Disable all when initializing
		disableElements();
	}
	
	@Override
	public void init() {

	}
	
	@Override
	public void refresh() {
		if(_domeo.getAnnotationPersistenceManager().getAllUserSets().size()>0) enableElements();
	}
	
	public void disableElements() {		
	}
	
	public void enableElements() {
	}
}
