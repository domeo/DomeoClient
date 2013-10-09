package org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.existingsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.src.IRetrieveExistingAnnotationSetHandler;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.existingsets.AnnotationSetTreeViewModel.AnnotationSetInfo;
import org.mindinformatics.gwt.domeo.plugins.annotation.persistence.ui.existingsets.AnnotationSetTreeViewModel.Category;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.marshalling.JsoAnnotationSetSummary;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.TreeViewModel;


/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ExistingAnnotationViewerPanel2 extends Composite implements IContainerPanel, IContentPanel, IResizable {

	private static final String TITLE = "Import of Existing Annotation Sets";
	
	interface Binder extends UiBinder<HorizontalPanel, ExistingAnnotationViewerPanel2> { }	
	private static final Binder binder = GWT.create(Binder.class);
	
	//private Resources _resources;
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;

//	  /**
//	   * The constants used in this Content Widget.
//	   */
//	  public static interface CwConstants extends Constants {
//	    String cwCellTreeDescription();
//
//	    String cwCellTreeName();
//	  }
	
	// Layout
	@UiField HorizontalPanel main;
	@UiField ScrollPanel left;
	@UiField Button importButton;
	@UiField VerticalPanel preview;
	
//	 /**
//	   * The CellTree.
//	   */
//	  @UiField(provided = true)
//	  CellTree cellTree;
	  
	  /**
	   * The label that shows selected names.
	   */
	  @UiField
	  Label selectedLabel;
	
	public boolean tabToolsPanelDisabled;
	
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	
	public String getTitle() {
		return TITLE;
	}
	
	
	List<AnnotationSetInfo> privateList;
	List<AnnotationSetInfo> groupsList;
	List<AnnotationSetInfo> publicList;
	
	public List<AnnotationSetInfo> getPrivateList() {
		return privateList;
	}
	public List<AnnotationSetInfo> getGroupsList() {
		return groupsList;
	}
	public List<AnnotationSetInfo> getPublicList() {
		return groupsList;
	}
	public List<AnnotationSetInfo> queryContactsByCategory(Category category) {
	    List<AnnotationSetInfo> matches = new ArrayList<AnnotationSetInfo>();
	   // Window.alert("queryContactsByCategory: " + category.getDisplayName());
	   // Window.alert("queryContactsByCategory: " + dataProvider.getList().size());
	    for (AnnotationSetInfo contact : dataProvider.getList()) {
	      if (contact.getCategory().getDisplayName().equals(category.getDisplayName())) {
	    	 //if (contact.getCategory() == category) {
	        matches.add(contact);
	      }
	    }
	    return matches;
	  }
	
	private ListDataProvider<AnnotationSetInfo> dataProvider = new ListDataProvider<AnnotationSetInfo>();
	
	  public void addAnnotationSet(AnnotationSetInfo contact) {
	    List<AnnotationSetInfo> contacts = dataProvider.getList();
	    // Remove the contact first so we don't add a duplicate.
	    contacts.remove(contact);
	    contacts.add(contact);
	  }
	
	private  final Category[] categories;
	String[] catNames = {"Public", "Groups", "Private", "External"};
	ImageResource[] catIcons; 
	
	public Category[] getCategories() {
		return categories;
	}
	
	private static Images images;
	
	static interface Images extends ClientBundle {
		ImageResource publicAccess_24_bn();
		ImageResource privateAccess();
		ImageResource groupsAccess_24();
		ImageResource boxIcon_24();
	}
	
	// ------------------------------------------------------------------------
	//  CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public ExistingAnnotationViewerPanel2(IDomeo domeo, JsArray responseOnSets) {
		_domeo = domeo;
		//_resources = resources;
		//_listPanel = new LogListPanel(_application);

		if (images == null) {
			images = GWT.create(Images.class);
		}
		
		catIcons = new ImageResource[catNames.length];
		
		categories = new Category[catNames.length];
		categories[0] = new Category(catNames[0], images.publicAccess_24_bn());
		categories[1] = new Category(catNames[1], images.groupsAccess_24());
		categories[2] = new Category(catNames[2], images.privateAccess());
		categories[3] = new Category(catNames[3], images.boxIcon_24());
		
//		for (int i = 0; i < catNames.length; i++) {
//	      categories[i] = new Category(catNames[i]);
//	    }
		
		try {
			
			privateList = new ArrayList<AnnotationSetInfo> ();
			groupsList = new ArrayList<AnnotationSetInfo> ();
			publicList = new ArrayList<AnnotationSetInfo> ();
			for(int i=0; i< responseOnSets.length(); i++) {
				
				//Window.alert(((JsoAnnotationSetSummary)responseOnSets.get(i)).getPermissionsAccessType() + " ---- " + _domeo.getAgentManager().getUserPerson().getUri());
				
				if(((JsoAnnotationSetSummary)responseOnSets.get(i)).isPublic()) {
					publicList.add(new AnnotationSetInfo(categories[0], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					addAnnotationSet(new AnnotationSetInfo(categories[0], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					categories[0].incrementCounter();
				} else if(((JsoAnnotationSetSummary)responseOnSets.get(i)).isGroups()) {
					groupsList.add(new AnnotationSetInfo(categories[1], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					addAnnotationSet(new AnnotationSetInfo(categories[1], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					categories[1].incrementCounter();
				} else if(((JsoAnnotationSetSummary)responseOnSets.get(i)).getPermissionsAccessType().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					privateList.add(new AnnotationSetInfo(categories[2], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					addAnnotationSet(new AnnotationSetInfo(categories[2], (JsoAnnotationSetSummary)responseOnSets.get(i)));
					categories[2].incrementCounter();
				}
			}
			
			_domeo.getLogger().debug(this, "2");
		
		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		
		//this.setWidth((Window.getClientWidth() - 140) + "px");
		
		_domeo.getLogger().debug(this, "3");

		final ArrayList<String> idsToLoad = new ArrayList<String>();
		
		 final MultiSelectionModel<AnnotationSetInfo> selectionModel =
			      new MultiSelectionModel<AnnotationSetInfo>(AnnotationSetTreeViewModel.AnnotationSetInfo.KEY_PROVIDER);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							idsToLoad.clear();
							StringBuilder sb = new StringBuilder();
							boolean first = true;
							List<AnnotationSetInfo> selected = new ArrayList<AnnotationSetInfo>(
									selectionModel.getSelectedSet());
							Collections.sort(selected);
							for (AnnotationSetInfo value : selected) {
								idsToLoad.add(value.getId());
								if (first) {
									first = false;
								} else {
									sb.append(", ");
								}
								sb.append(value.getLabel());
							}
							//selectedLabel.setText(sb.toString());
							
							
							importButton.setText("Import " + selected.size() + (selected.size()==1?" Set":" Sets"));
							importButton.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									hide();
									List<String> uuids = idsToLoad;
									_domeo.getAnnotationPersistenceManager().retrieveExistingAnnotationSets(uuids, (IRetrieveExistingAnnotationSetHandler)_domeo);
								}							
							});
						}
					});

			    CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
			    CellTree cellTree = new CellTree(
			        new AnnotationSetTreeViewModel(selectionModel, this), null, res);
			    cellTree.setAnimationEnabled(true);
			    
			    
			   // TreeViewModel model = new CustomTreeModel();
			    
			   // CellTree.Resources res = GWT.create(CellTree.BasicResources.class);
			   
			    //CellTree cellTree = new CellTree(model, "Item 1");
			    //cellTree.setAnimationEnabled(true);
			
			    left.add(cellTree);
			    left.setHeight((Window.getClientHeight() - 320) + "px");
			
		} catch (Exception e) {
			_domeo.getLogger().debug(this, "ExistingAnnotationViewerPanel " + e.getMessage());
		}

	}
	
	public void showAnnotationSetPreview(String id) {
		preview.clear();
		//preview.add(new HTML("Preview of: " + id));
	}

	public void refreshPreferencesList() {
		//completePreferencesList.refresh();
	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 140) + "px");
		//tabToolsPanel.setWidth((Window.getClientWidth() - 130) + "px");
	}

	@Override
	public void hide() {
		_containerPanel.hide();
	}
	
	  // The model that defines the nodes in the tree.
    private static class CustomTreeModel implements TreeViewModel {

      // Get the NodeInfo that provides the children of the specified value.
      public <T> NodeInfo<?> getNodeInfo(T value) {

        // Create some data in a data provider. Use the parent value as a prefix for the next level.
        ListDataProvider<String> dataProvider = new ListDataProvider<String>();
        for (int i = 0; i < 2; i++) {
          dataProvider.getList().add(value + "." + String.valueOf(i));
        }

        // Return a node info that pairs the data with a cell.
        return new DefaultNodeInfo<String>(dataProvider, new TextCell());
      }

      // Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
      public boolean isLeaf(Object value) {
        // The maximum length of a value is ten characters.
        return value.toString().length() > 10;
      }
    }
    


}



