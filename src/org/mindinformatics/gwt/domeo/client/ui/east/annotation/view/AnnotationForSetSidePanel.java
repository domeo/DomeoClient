package org.mindinformatics.gwt.domeo.client.ui.east.annotation.view;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSummaryTable;
import org.mindinformatics.gwt.domeo.client.ui.sets.AnnotationSetSummaryLens;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
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
public class AnnotationForSetSidePanel extends ASidePanel implements IInitializableComponent, 
		IRefreshableComponent, IAnnotationRefreshableComponent {

	interface Binder extends UiBinder<Widget, AnnotationForSetSidePanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiField VerticalPanel topbar;
	@UiField VerticalPanel body;
	
	private AnnotationSummaryTable _content;
	private AnnotationSetSummaryLens _lens;
	
	// By contract 
	private IDomeo _domeo;
	private MAnnotationSet _set;
	
	public AnnotationForSetSidePanel(IDomeo domeo, SidePanelsFacade facade, ASideTab tab, AnnotationSummaryTable content, MAnnotationSet set) {
		super(domeo, facade, tab);
		
		_domeo = domeo;
		_content = content;
		_set = set;
		
		initWidget(binder.createAndBindUi(this));

		_lens = new AnnotationSetSummaryLens(_domeo, this, set);
		_domeo.getComponentsManager().registerObjectLens(set, _lens);
		
		topbar.add(new AnnotationSetViewerSidePanelTopbar(_domeo, this, "Annotation Set View"));
		topbar.add(_lens);
		
		body.add(content);
	}
	
	public void init() {
		
	}
	
	public void refresh() {
		_lens.refreshLens();
		List<MAnnotation> anns = new ArrayList<MAnnotation>();
		anns.addAll(_set.getAnnotations());
		_content.refreshPanel(anns);
	}

	@Override
	public boolean isSidePanelAlreadyOpen(Object obj) {
		if(obj instanceof MAnnotationSet) {
			if(((MAnnotationSet)obj).getLocalId()==_set.getLocalId()) {
				return true;
			}
		} 
		return false;
	}

	@Override
	public Object getComparisonContentObject() {
		return _set;
	}
}
