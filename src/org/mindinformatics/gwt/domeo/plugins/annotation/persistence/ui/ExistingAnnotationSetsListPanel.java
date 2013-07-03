package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.service.AnnotationPersistenceServiceFacade;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetListHandler;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This panel displays the list of immediately available sets for the current
 * resource. Additional sets can be retrieved through an appropriate action.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ExistingAnnotationSetsListPanel extends Composite implements IInitializableComponent, 
		IRefreshableComponent, IAnnotationRefreshableComponent, IRetrieveExistingAnnotationSetListHandler{
	
	interface Binder extends UiBinder<Widget, ExistingAnnotationSetsListPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private String _allowed;
	private ExistingAnnotationViewerPanel _existingAnnotationViewerPanel;
	
	@UiField VerticalPanel body;
	@UiField SimplePanel toolbar;
	@UiField ScrollPanel setsListScroller;
	@UiField VerticalPanel setsList;
	@UiField VerticalPanel info;
	@UiField Button importButton;
	
	private JsoAnnotationSetSummary _setInfo;
	private List<JsAnnotationSetSummaryLens> lenses = new ArrayList<JsAnnotationSetSummaryLens>();
	
	public ExistingAnnotationSetsListPanel(IDomeo domeo, final ExistingAnnotationViewerPanel existingAnnotationViewerPanel, String allowed, List<JsoAnnotationSetSummary> responseOnSets) {
		_domeo = domeo;
		_allowed = allowed;
		_existingAnnotationViewerPanel = existingAnnotationViewerPanel;
		
		initWidget(binder.createAndBindUi(this));
		
		body.setCellVerticalAlignment(info, HasVerticalAlignment.ALIGN_TOP);
		body.setHeight("100%");
		
		if(_allowed.equals(AnnotationPersistenceServiceFacade.ALLOW_MINE)) {
			toolbar.add(new PrivateAnnotationTopbar(_domeo, this));
		} else if(_allowed.equals(AnnotationPersistenceServiceFacade.ALLOW_GROUPS)) {
			toolbar.add(new GroupsAnnotationTopbar(_domeo, this));
		}
		
		importButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				existingAnnotationViewerPanel.hide();
				List<String> uuids = getSelected();
				_domeo.getAnnotationPersistenceManager().retrieveExistingAnnotationSets(uuids, (IRetrieveExistingAnnotationSetHandler)_domeo);
			}
		});
		
		//_domeo.getPersistenceManager().retrieveExistingAnnotationSetList(this);
		refresh(responseOnSets);
		
		/*
		AnnotationPersistenceServiceFacade f = new AnnotationPersistenceServiceFacade();
		JsArray<JsAnnotationSetSummary> sets = f.retrieveAnnotationByDocumentUrl("", _allowed , "");
		
		List<JsAnnotationSetSummary> list = new ArrayList<JsAnnotationSetSummary> ();
		for(int i=0; i< sets.length(); i++) {
			list.add(sets.get(i));
		}
		refresh(list);
		*/
	}
	
	@Override
	public void init() {
		lenses = new ArrayList<JsAnnotationSetSummaryLens>();
		
		info.clear();
	}
	
	@Override
	public void refresh() {
		//refresh(_application.getAnnotationPersistenceManager().getAllUserSets());
	}
	
	/**
	 * Summary panel refresh with a given list of annotation sets
	 * @param sets	The list of sets to be displayed
	 */
	public void refresh(List<JsoAnnotationSetSummary> sets) {
		long start = System.currentTimeMillis();
		try {
			info.clear();
	
			// The following code, instead of rebuilding the panel every time,
			// is refreshing the already existing items reducing dramatically
			// the panel refresh time
			int counter=0;
			setsList.clear();
			for(JsoAnnotationSetSummary set: sets) {
				JsAnnotationSetSummaryLens newLens = new JsAnnotationSetSummaryLens((IDomeo) _domeo, this, set);
				
				setsList.add(newLens);
				lenses.add(newLens);
				/*
				AnnotationSetSummaryListLens lens = isLensAlreadyDisplayed(set);
				if(lens==null) {
					_application.getLogger().debug(this, "Creating lens for set js summary " + set.getUuid());
					AnnotationSetSummaryListLens newLens = new AnnotationSetSummaryListLens((IDomeo) _application, this, set);
					_application.getComponentsManager().registerObjectLens(set, newLens);
					setsList.add(newLens);
					lenses.add(newLens);
				} else {
					_application.getLogger().debug(this, "Refreshing lens of set js summary " + set.getUuid());
					setsList.add(lens);
					lens.refreshLens();
				}
				*/
				
				counter++;
			}
			body.setCellHeight(setsListScroller, (counter*22)+"px");
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Refresh of the annotation sets side panel failed " + e.getMessage());
		}
		_domeo.getLogger().debug(this, "Annotation sets summary panel refreshed in (ms):" + (System.currentTimeMillis()-start));
	}
	
	public void manageSelection(boolean selectAll) {
		for(JsAnnotationSetSummaryLens lens: lenses) {
			lens.setSelected(selectAll);
		}
	}
	
	public ArrayList<String> getSelected() {
		ArrayList<String> uuids = new ArrayList<String>();
		for(JsAnnotationSetSummaryLens lens: lenses) {
			if(lens.isSelected()) {
				// TODO take into consideration already loaded sets
				uuids.add(lens.getSet().getId());
			}
		}
		return uuids;
	}

	/**
	 * Displays the info lens for the specified annotation set
	 * @param set	The annotation set
	 */
	public void displayAannotationSetInfo(JsoAnnotationSetSummary set) {
		_setInfo = set;
		
		info.clear();
		info.add(new HTML(set.getId()+"-"+set.getLabel()));
		info.add(new HTML("Ajax call for the details"));
	}

	@Override
	public void setExistingAnnotationSetList(JsArray responseOnSets) {
		// TODO Improve the hiding as it flickers 
		if(responseOnSets==null || responseOnSets.length()==0) {
			_existingAnnotationViewerPanel.hide();
			return;
		}
		
	}
	
//	/**
//	 * Checks if the lens has been already cached.
//	 * @param set	The annotation set
//	 * @return	The lens for the set if one exists.
//	 */
//	private AnnotationSetSummaryListLens isLensAlreadyDisplayed(MAnnotationSet set) {
//		for(AnnotationSetSummaryListLens lens:lenses) {
//			if(lens.getSet().getLocalId()==set.getLocalId()) return lens;
//		}
//		return null;
//	}
}
