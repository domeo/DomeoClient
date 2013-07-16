package org.mindinformatics.gwt.domeo.component.bibliography.ui.listpicker;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.IExceptionReporter;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.model.references.IReferences;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.WidgetUtilsResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ReferencesListPickerWidget extends Composite implements IExceptionReporter, IResizable {
	
	interface Binder extends UiBinder<VerticalPanel, ReferencesListPickerWidget> {}
	private static final Binder binder = GWT.create(Binder.class);

	public static final WidgetUtilsResources widgetUtilsResources = 
			GWT.create(WidgetUtilsResources.class);
	
	interface LocalCss extends CssResource {
		String indexOdd();
		String indexEven();
		String imageWrap();
		String centerText();
	}
	
	@UiField LocalCss style;
	
	// By contract 
	protected IDomeo _domeo;
	protected Resources _resources;
	private IReferencesListPickerContainer _container;
	
	// Main panel: for this widget no other graphic element
	// has been defined in the xml
	@UiField VerticalPanel main;
	private ScrollPanel panel;
	// Dynamically created ui elements
	protected Panel resultsContainerPanel;
	
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	protected ArrayList<MLinkedResource> searchTermsResult; 
	//private SelectedTerms selectedTerms;
	
	public ReferencesListPickerWidget(IDomeo annotator, IReferencesListPickerContainer container, boolean showTitle) {
		_domeo = annotator;
		_resources = Domeo.resources;
		_container = container;
		

		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite 
		//initMapOfAlreadyAssociatedTerms(container.getItems());
		
		if(showTitle) main.add(new Label(getWidgetTitle()));	
		
        resultsContainerPanel = new FlowPanel();
       
        resultsContainerPanel.add(new HTML("No results to display"));
        
        main.clear();
        panel = new ScrollPanel();
        panel.setHeight((Window.getClientHeight() - 341) + "px");
        panel.setWidth((Window.getClientWidth() - 620) + "px");
       
        panel.add(resultsContainerPanel);
        main.add(panel);
        
        resized();
        displayReferences();
	}

	
//	public void setSearchBoxValue(String value, boolean search) {
//		termsSearchPanel.setSearchBoxValue(value);
//		if(search)  performSearch(value);
//	}
	
	private void initMapOfAlreadyAssociatedTerms(ArrayList<MLinkedResource> terms) {
		for(MLinkedResource term: terms) {
			associatedTerms.put(term.getUrl(), term);
		}
	}
	
	// ------------------------------------------------------------------------
	//  TERMS RESULTS MANAGEMENT
	// ------------------------------------------------------------------------
	public ArrayList<MLinkedResource> getTermsList() {
		return getSearchTermsResult();
	}
	
	public ArrayList<MLinkedResource> getSearchTermsResult() {
		return searchTermsResult;
	}
	
	public HashMap<String, String> getSearchTermsResultSources() {
		HashMap<String, String> map = new HashMap<String, String>();

		for(int i=0; i< searchTermsResult.size(); i++) {
			if(!map.containsKey(searchTermsResult.get(i).getSource().getUrl()))
				map.put(searchTermsResult.get(i).getSource().getUrl(), 
						searchTermsResult.get(i).getSource().getLabel());
		}

		return map;
	}
	
	protected void displayReferences() {
		resultsContainerPanel.clear();
		
		widgetUtilsResources.widgetCss().ensureInjected();
		
		int counter = 0;
		FlowPanel fp = new FlowPanel();
		for(int i=0; i<((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().size(); i++) {
			HorizontalPanel hp = new HorizontalPanel();
			if(i%2 == 1) hp.setStyleName(widgetUtilsResources.widgetCss().tableOddRow());
//			FlowPanel fp2 = new FlowPanel();
//			HTML leftHtml = new HTML(""+(((MAnnotationCitationReference)((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i)).getReferenceIndex().intValue()+1));
//			if(i%2 == 1)  leftHtml.setStyleName(style.indexOdd());
//			else leftHtml.setStyleName(style.indexEven());
//			fp2.add(leftHtml);
//			if(((MPublicationArticleReference)((MAnnotationCitationReference)((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i)).getReference()).getUnrecognized()!=null) {
//				final int index = i;
//				SimpleIconButtonPanel bu = new SimpleIconButtonPanel(_domeo, new ClickHandler() {
//							@Override
//							public void onClick(ClickEvent event) {
//								_domeo.getLogger().command(this, "Open panel for adding a citation for reference #" + index);
//								PubmedReferenceSearchPanel afp = new PubmedReferenceSearchPanel(_domeo, index, 800);
//								new EnhancedGlassPanel(_domeo, afp, afp.getTitle(), 800, false, false, false);
//							}},
//						Domeo.resources.littleBookAddIcon().getSafeUri().asString(), "Add reference metadata");
//				
//				fp2.add(bu);
//			} else {
//				/*
//				Image icon = new Image(Domeo.resources.editLittleIcon());
//				icon.setStyleName(ATileComponent.tileResources.css().button());
//				fp2.add(icon);
//				*/
//			}
//			hp.add(fp2);
//			hp.setCellWidth(fp2, "18px");
			
			final int ii = i;
			final CheckBox box = new CheckBox();
			box.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					box.setEnabled(false);
					_container.addBibliographicObject(((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(ii));
				}
			});			
			hp.add(box);
			
			hp.add(PubMedCitationPainter.getCitationAnnotationWithIds(((IReferences)_domeo.getPersistenceManager().getCurrentResource()).getReferences().get(i), _domeo));
			fp.add(hp);
		}
		resultsContainerPanel.add(fp);
	}

    public String getWidgetTitle() {
        return "Image picker";
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


	@Override
	public void resized() {
		resultsContainerPanel.setHeight((Window.getClientHeight() - 220) + "px");
		panel.setHeight((Window.getClientHeight() - 341) + "px");
	    panel.setWidth((Window.getClientWidth() - 620) + "px");
	}
}
