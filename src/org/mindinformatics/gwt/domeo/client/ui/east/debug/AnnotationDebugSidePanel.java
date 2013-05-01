package org.mindinformatics.gwt.domeo.client.ui.east.debug;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
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
public class AnnotationDebugSidePanel extends ASidePanel implements IRefreshableComponent,
		IAnnotationRefreshableComponent {

	interface Binder extends UiBinder<Widget, AnnotationDebugSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel body;
	
	private AnnotationDebugSummaryTable table;
	
	public AnnotationDebugSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab) {
		super(domeo, facade, tab);
		
		initWidget(binder.createAndBindUi(this));
		
		AnnotationDebugSidePanelTopbar topbar = new AnnotationDebugSidePanelTopbar(domeo);
		body.add(topbar);
		body.setCellHeight(topbar, "20px");
		
		table = new AnnotationDebugSummaryTable(domeo);
		table.initializePanel();
		body.setHeight("100%");
		body.add(table);
	}
	
	public void refresh() {
		//DocumentAnnotationSummaryWrapper wrapper = _annotator.getDocumentAnnotationSummaryWrapper();
		//DocumentAnnotationDTO documentAnnotation = wrapper.getDocumentAnnotation();
		table.refreshPanel();
	}
	
	public void refreshAnnotationSetFilter() {
		//table.refreshAnnotationSetFilter();
	}
}
