package org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.services.IWidget;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.INifDataRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.service.NifManager;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodiesSearchWidget extends Composite implements IWidget, IAntibodiesWidget, INifDataRequestCompleted {
	
	interface Binder extends UiBinder<VerticalPanel, AntibodiesSearchWidget> {}
	private static final Binder binder = GWT.create(Binder.class);
	
	public static final String WIDGET_TITLE = "Antibody Registry Search";

	// By contract 
	private IDomeo _annotator;
	private Resources _resources;
	private IAntibodiesSearchContainer _container;
	
	// Main panel: for this widget no other graphic element
	// has been defined in the xml
	@UiField VerticalPanel main;
	// Dynamically created ui elements
	private Panel resultsContainerPanel;
	private AntibodiesList publicationsResultsPanel;
	private AntibodiesSearch termsSearchPanel;
	
	private MAntibody associatedAntibody;
	private ArrayList<MAntibody> searchAntibodiesResults; 
	//private SelectedTerms selectedTerms;
	
	//AntibodyRegistryServiceAsync antibodyRegistryService;
	
	public AntibodiesSearchWidget(IDomeo domeo, IAntibodiesSearchContainer container, Resources resources, boolean showTitle) {
		_annotator = domeo;
		_resources = resources;
		_container = container;
		
		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite 
		//initMapOfAlreadyAssociatedBibReferences(container.getAntibodies());
		
		if(showTitle) main.add(new Label(WIDGET_TITLE));
		
		termsSearchPanel = new AntibodiesSearch(this, "");
		main.add(termsSearchPanel);
		
		resultsContainerPanel = new FlowPanel();
		resultsContainerPanel.setHeight("357px");
		resultsContainerPanel.add(new HTML("No results to display"));
		main.add(resultsContainerPanel);		
		
//		if (AnnotatorHelper.ifRealMode()) {
//			antibodyRegistryService = (AntibodyRegistryServiceAsync) GWT.create(AntibodyRegistryService.class);
//			ServiceDefTarget endpoint = (ServiceDefTarget) antibodyRegistryService;
//			String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc";
//			endpoint.setServiceEntryPoint(moduleRelativeURL);
//		} else {
//			GWT.log("No persistence service can be initialized in demo mode!");
//		}
	}
	
//	private void initMapOfAlreadyAssociatedAntibodies(ArrayList<MAntibody> antibodies) {
//		for(MAntibody item: antibodies) {
//			associatedAntibodies.put(item.getUrl(), item);
//		}
//	}
	
	public void performSearch(String typeQuery, String textQuery, String vendor) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new Image(_resources.littleProgressIcon()));
		
		// Ajax call in demo mode
		/*if(_annotator.isHostedMode()) {
			
			Window.alert("Live search not available in hosted mode: " + textQuery + " typeQuery:" + typeQuery + " vendor:" + vendor );
			NifManager nifManager = NifManager.getInstance();
			nifManager.selectConnector(_annotator);
			nifManager.searchData(this, textQuery, typeQuery, vendor, "nif-0000-07730-1", 0, 0);
//			final AsyncCallback callback = new AsyncCallback() {
//			    public void onFailure(Throwable caught) {
//			    	Window.alert("Failed to get response from server" + caught.getMessage());
//			    }
//	
//			    public void onSuccess(Object result) {
//			    	searchAntibodiesResult = (ArrayList<AntibodyDTO>) result;
//			    	displayResults(searchAntibodiesResult);
//			    }
//			};
//	
//			final AntibodyRegistryServiceAsync antibodyRegistryService = (AntibodyRegistryServiceAsync) 
//				GWT.create(AntibodyRegistryService.class);
//			((ServiceDefTarget) antibodyRegistryService).setServiceEntryPoint( 
//					GWT.getModuleBaseURL() + "antibodyRetrieval");
//			antibodyRegistryService.findAntibodies("", "", "", callback);
		} else {*/
			try {
				NifManager nifManager = NifManager.getInstance();
				nifManager.selectConnector(_annotator);
				nifManager.searchData(this, textQuery, typeQuery, vendor, "nif-0000-07730-1", 0, 0);
			} catch(Exception e) {
				_annotator.getLogger().exception(this, "Nif Manager terminated with exception: " + e.getMessage());
			}
		//}
	}
	
	public void filterBySource(String sourceId) {
		publicationsResultsPanel.update(sourceId);
		//updateSelectedTerms(0);
	}
	
	/*
	public void updateSelectedTerms(int numberOfTerms) {
		selectedTerms.updateSelectedTerms(numberOfTerms);
	}
	*/
	
	public String getFilterValue() {
		return termsSearchPanel.getSourceFilterValue();
	}
	
	// ------------------------------------------------------------------------
	//  TERMS RESULTS MANAGEMENT
	// ------------------------------------------------------------------------
	public ArrayList<MAntibody> getSearchTermsResult() {
		return searchAntibodiesResults;
	}
	
	public Set<String> getSearchTermsResultSources() {
		Set<String> map = new HashSet<String>();
		for(int i=0; i< searchAntibodiesResults.size(); i++) {
			if(!map.contains(searchAntibodiesResults.get(i).getVendor()))
				map.add(searchAntibodiesResults.get(i).getVendor());
		}
		return map;
	}
	
	private void displayResults(ArrayList<MAntibody> list) {
		HashMap<String, MAntibody> associatedAntibodies = new HashMap<String, MAntibody>();
		if(associatedAntibody!=null) 
		associatedAntibodies.put(associatedAntibody.getUrl(), associatedAntibody);
		publicationsResultsPanel = new AntibodiesList(_annotator, _resources, this, list, associatedAntibodies);
		
		resultsContainerPanel.clear();
		resultsContainerPanel.add(publicationsResultsPanel);
		
		termsSearchPanel.updateResultsStats();
		
	}
	
	public void updateResultsNumbers() {
		termsSearchPanel.updateResultsStats();
	}
	
	// ------------------------------------------------------------------------
	//  TERMS CHOICE MANAGEMENT
	// ------------------------------------------------------------------------
	public void addAntibody(MAntibody antibody) {
		associatedAntibody = antibody;
		_container.addAntibody(antibody);
	}

	public MAntibody getAntibody() {
		return associatedAntibody;
	}
	
	public ArrayList<MAntibody> getAntibodiesResults() {
		return searchAntibodiesResults;
	}
	
	public void removeAntibody(MAntibody antibody) {
		associatedAntibody = null;
	}
	
	@Override
	public String getWidgetTitle() {
		return WIDGET_TITLE;
	}

	@Override
	public void returnData(ArrayList<MGenericResource> data) {
		ArrayList<MAntibody> antibodies = new ArrayList<MAntibody>();
		for(MGenericResource datum: data) {
			if(datum instanceof MAntibody)
				antibodies.add((MAntibody)datum);
		}
		searchAntibodiesResults = antibodies;
		displayResults(antibodies);
	}

	@Override
	public void returnData(int totalPages, int pageSize, int pageNumber,
			ArrayList<MGenericResource> data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportException() {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while performing search. See logs for details."));
	}
	
	@Override
	public void reportException(String message) {
		resultsContainerPanel.clear();
		resultsContainerPanel.add(new HTML("Exception while performing search. " + message + " See logs for details."));
	}
}
