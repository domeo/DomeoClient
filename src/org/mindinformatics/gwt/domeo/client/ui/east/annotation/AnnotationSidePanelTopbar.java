package org.mindinformatics.gwt.domeo.client.ui.east.annotation;

import java.util.ArrayList;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.search.ASearchComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.framework.component.IInitializableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.model.users.IUserGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationSidePanelTopbar  extends Composite 
	implements IInitializableComponent, IRefreshableComponent {

	interface Binder extends UiBinder<VerticalPanel, AnnotationSidePanelTopbar> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private IDomeo _domeo;
	private AnnotationSidePanel _annotationSidePanel;
	
	@UiField TextBox searchBox;
	@UiField ListBox accessFilter;
	@UiField ListBox setFilter;
	@UiField ListBox typeFilter;
	@UiField Button searchButton;
	
	private List<String> accessFilters = new ArrayList<String>();
	
	public AnnotationSidePanelTopbar(IDomeo domeo, final AnnotationSidePanel annotationSidePanel) {
		_domeo = domeo;
		_annotationSidePanel = annotationSidePanel;
		
		initWidget(binder.createAndBindUi(this));

		searchBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER ||
						event.getNativeEvent().getKeyCode() == 13) {
					search();
		        }
			}
		});
		searchButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//if(searchBox.getText().trim().length()>0) 
				search();
			}
		});
		
		ChangeHandler csh = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				search();
			}
		};
		accessFilter.addChangeHandler(csh);
		typeFilter.addChangeHandler(csh);
		setFilter.addChangeHandler(csh);
		
		// Disable all when initializing
		disableElements();
	}
	
	@Override
	public void init() {
		searchBox.setValue("");
		createAccessFilter();
		createSetsFilter();
		createTypesFilter();
	}
	
	@Override
	public void refresh() {
		if(_domeo.getAnnotationPersistenceManager().getAllUserSets().size()>0) enableElements();
		createSetsFilter();
		createTypesFilter();
		createAccessFilter();
	}
	
	private void createTypesFilter() {
		typeFilter.clear();
		typeFilter.addItem("All types", "All types");
		if(_domeo.getAnnotationPersistenceManager().getAllUserSets().size()>0) {
			for(String type:_domeo.getAnnotationPersistenceManager().getAllAnnotationTypes().keySet()) {			
				typeFilter.addItem(_domeo.getAnnotationPersistenceManager().getAllAnnotationTypes().get(type), type);
			}
		}
	}
	
	private void createSetsFilter() {
		setFilter.clear();
		setFilter.addItem("All sets", "-1");
		for(MAnnotationSet set:_domeo.getAnnotationPersistenceManager().getAllUserSets()) {		
			setFilter.addItem(set.getLabel()+" - "+set.getDescription(), set.getLocalId().toString());
		}
	}
	
	private void createAccessFilter() {
		// Create access list
		accessFilters.clear();
		if(_domeo.getUserManager().getUsersGroups()!=null) {
			accessFilters.add(ASearchComponent.ANYBODY);
			accessFilters.add(ASearchComponent.ONLY_MINE);
			accessFilters.add(ASearchComponent.ONLY_OTHERS);
			
			for(IUserGroup group: _domeo.getUserManager().getUsersGroups()) {
				accessFilters.add(group.getName()+" - "+group.getDescription());
			}
		}
		// Updating the list box
		accessFilter.clear();
		for(String creatorFilterItem: accessFilters) {
			accessFilter.addItem(creatorFilterItem);
		}
	}
	
	public void disableElements() {
		searchBox.setEnabled(false);
		searchButton.setEnabled(false);
		accessFilter.setEnabled(false);
		typeFilter.setEnabled(false);
		setFilter.setEnabled(false);
		
	}
	
	public void enableElements() {
		searchBox.setEnabled(true);
		searchButton.setEnabled(true);
		accessFilter.setEnabled(true);
		typeFilter.setEnabled(true);
		setFilter.setEnabled(true);
	}
	
	private void search() {
		try {
			long start = System.currentTimeMillis();
			logSearch();
			searchButton.setText("...");	
			List<MAnnotation> annotations = _domeo.getAnnotationSearchManager().search(_domeo, 
					accessFilter.getItemText(accessFilter.getSelectedIndex()), 
					new Long(setFilter.getValue(setFilter.getSelectedIndex())),
					typeFilter.getValue(typeFilter.getSelectedIndex()), 
					searchBox.getText().trim());
			searchButton.setEnabled(true);
			searchButton.setText("Go");
			_domeo.getLogger().info(this, "Search completed in (ms): " + (System.currentTimeMillis()-start));
			_annotationSidePanel.refresh(annotations);
		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			searchButton.setEnabled(true);
			searchButton.setText("!!!");
			Timer t = new Timer() {
			      public void run() {
			    	  searchButton.setText("Go");
			      }
			    };

		    // Schedule the timer to run once in 5 seconds.
		    t.schedule(3000);
			
		}
	}
	
	private void logSearch() {
		StringBuffer sb = new StringBuffer();
		if(searchBox.getText().trim().length()>0) sb.append("Searching for {" + searchBox.getText().trim() + "} and ");
		sb.append("displaying annotations with creator " + accessFilter.getItemText(accessFilter.getSelectedIndex()));
		sb.append(", type " + typeFilter.getItemText(typeFilter.getSelectedIndex()));
		sb.append(" and set " + setFilter.getItemText(setFilter.getSelectedIndex()));
		_domeo.getLogger().command(this, sb.toString());
	}
}
