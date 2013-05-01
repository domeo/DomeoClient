package org.mindinformatics.gwt.domeo.client.ui.east.sets;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.ExistingAnnotationViewerPanel;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationSetsSidePanelTopbar extends Composite implements IInitializableComponent, 
		IRefreshableComponent {

	interface Binder extends UiBinder<HorizontalPanel, AnnotationSetsSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private AnnotationSetsSidePanel _annotationSetSidePanel;
	
	@UiField HorizontalPanel addSetPanel;
	@UiField HorizontalPanel retrieveSetsPanel;
	@UiField ListBox accessFilter;
	//@UiField Button searchSets;
	
	private List<String> creatorsFilters = new ArrayList<String>();
	
	public AnnotationSetsSidePanelTopbar(IDomeo domeo, final AnnotationSetsSidePanel annotationSetSidePanel) {
		_domeo = domeo;
		_annotationSetSidePanel = annotationSetSidePanel;
		
		initWidget(binder.createAndBindUi(this));
		accessFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				search();
			}
		});	
		
		ClickHandler ch = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MAnnotationSet set = _domeo.getAnnotationPersistenceManager().createNewAnnotationSet();
				_domeo.getAnnotationPersistenceManager().setCurrentAnnotationSet(set);
				_domeo.refreshAnnotationComponents();
			}
		};
		
		Label l = new Label("Set");
		l.addClickHandler(ch);
		Image i = new Image(Domeo.resources.addLittleIcon());
		i.addClickHandler(ch);
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.add(i);
		hp1.add(l);
		addSetPanel.add(hp1);
		addSetPanel.setWidth("60px");
		
		Label l2 = new Label("Retreive Sets");
		l2.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.checkForExistingAnnotationSets();
				//ExistingAnnotationViewerPanel lwp = new ExistingAnnotationViewerPanel(_domeo);
				//new EnhancedGlassPanel(_domeo, lwp, lwp.getTitle(), false, false, false);
			}
		});
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add(new Image(Domeo.resources.browseLittleIcon()));
		hp2.add(l2);
		retrieveSetsPanel.add(hp2);
		retrieveSetsPanel.setWidth("105px");
		
		
		disableElements();
	}
	
	@Override
	public void init() {
		createAccessFilter();
	}
	
	@Override
	public void refresh() {
		if(_domeo.getAnnotationPersistenceManager().getAllUserSets().size()>0) enableElements();
		createAccessFilter();
	}
	
	private void createAccessFilter() {
		// Create the types list
		creatorsFilters.clear();
		if(_domeo.getUserManager().getUsersGroups()!=null) {
			creatorsFilters.add(ASearchComponent.ANYBODY);
			creatorsFilters.add(ASearchComponent.ONLY_MINE);
			creatorsFilters.add(ASearchComponent.ONLY_OTHERS);
			
			for(IUserGroup group: _domeo.getUserManager().getUsersGroups()) {
				creatorsFilters.add(group.getName()+" - "+group.getDescription());
			}
		}
		// Updating the list box
		accessFilter.clear();
		for(String creatorFilterItem: creatorsFilters) {
			accessFilter.addItem(creatorFilterItem);
		}
	}

	private void disableElements() {
		accessFilter.setEnabled(false);
	}
	
	private void enableElements() {
		accessFilter.setEnabled(true);
	}
	
	private void search() {
		try {
			long start = System.currentTimeMillis();
			_domeo.getLogger().command(this, "Displaying annotation sets with editor " + accessFilter.getItemText(accessFilter.getSelectedIndex()));
			List<MAnnotationSet> found = new ArrayList<MAnnotationSet>();
			for(MAnnotationSet set: _domeo.getAnnotationPersistenceManager().getAllUserSets()) {
				if(filterByEditor(set, accessFilter.getItemText(accessFilter.getSelectedIndex()))) {
					found.add(set);
				}
			}
			_domeo.getLogger().info(this, "Search completed in (ms): " + (System.currentTimeMillis()-start));
			_annotationSetSidePanel.refresh(found);
		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
	
	//TODO generalize with more precise permissions based on groups and custom
	public boolean filterByEditor(MAnnotationSet annotation, String creatorFilter) {
		if(creatorFilter.equals(ASearchComponent.ANYBODY)) {
			return true;
		} else if(creatorFilter.equals(ASearchComponent.ONLY_MINE) && annotation.getCreatedBy().getUri()
			.equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				return true;
		} else if(creatorFilter.equals(ASearchComponent.ONLY_OTHERS) && !annotation.getCreatedBy().getUri()
				.equals(_domeo.getAgentManager().getUserPerson().getUri())) {
			return true;
		}
		return false;
	}
}
