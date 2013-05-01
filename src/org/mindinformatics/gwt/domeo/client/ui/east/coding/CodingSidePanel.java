package org.mindinformatics.gwt.domeo.client.ui.east.coding;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.ui.east.ASidePanel;
import org.mindinformatics.gwt.framework.component.ui.east.ASideTab;
import org.mindinformatics.gwt.framework.component.ui.east.SidePanelsFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CodingSidePanel extends ASidePanel  {

	interface Binder extends UiBinder<Widget, CodingSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	
	// By contract 
	@SuppressWarnings("unused")
	private IDomeo _domeo;
	
	public CodingSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		
		initWidget(binder.createAndBindUi(this));
		
		body.setHeight("100%");	
	}
	
	public void init() {
		
	}
	
	public void refresh() {
		
	}
}
