package org.mindinformatics.gwt.domeo.component.cache.images.ui.listpicker;

import java.util.ArrayList;
import java.util.HashMap;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.component.cache.images.model.ImageProxy;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.IExceptionReporter;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ImagesListPickerWidget extends Composite implements IResizable, IExceptionReporter {
	
	interface Binder extends UiBinder<VerticalPanel, ImagesListPickerWidget> {}
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
	private IImagesListPickerContainer _container;
	
	// Main panel: for this widget no other graphic element
	// has been defined in the xml
	@UiField VerticalPanel main;
	private ScrollPanel panel;
	// Dynamically created ui elements
	protected Panel resultsContainerPanel;
	
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	protected ArrayList<MLinkedResource> searchTermsResult; 
	//private SelectedTerms selectedTerms;
	
	@Override
	public void resized() {
		resultsContainerPanel.setHeight((Window.getClientHeight() - 220) + "px");
		panel.setHeight((Window.getClientHeight() - 341) + "px");
	    panel.setWidth((Window.getClientWidth() - 620) + "px");
	}
	
	public ImagesListPickerWidget(IDomeo annotator, IImagesListPickerContainer container, boolean showTitle) {
		_domeo = annotator;
		_resources = Domeo.resources;
		_container = container;
		
		widgetUtilsResources.widgetCss().ensureInjected();
		

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
        displayImages();
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
	
	protected void displayImages() {
		resultsContainerPanel.clear();
		
		widgetUtilsResources.widgetCss().ensureInjected();
		
		int counter = 0;
		for(String key:_domeo.getImagesCache().getKeys()) {
			ArrayList<ImageProxy> list = _domeo.getImagesCache().getValue(key);
			for(ImageProxy image: list) {
				HorizontalPanel hp = new HorizontalPanel();
				hp.setWidth("100%");
				
				VerticalPanel hp1 = new VerticalPanel();
				hp1.setWidth("100%");
				
				boolean small = false;
				boolean reduced = false;
				
				
				Image img = new Image(image.getDisplayUrl());
				if(img.getWidth()>440) {
					img.setWidth("430px");  
					reduced = true;
				} else if(img.getWidth()<220) {
					small = true;
					//img.setWidth("200px"); 
				}
				if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
					img.setTitle(image.getTitle());
				}
				
				if(!small) {
					SimplePanel imageWrap = new SimplePanel();
					imageWrap.setStyleName(style.imageWrap());
					imageWrap.add(img);
					imageWrap.setStyleName(style.centerText());
					hp1.add(imageWrap);
					hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
					if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
						HTML title = new HTML("<b>"+image.getTitle()+"</b>");
						hp1.add(title);
						hp1.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
					} 

					if(counter%2 == 1) {
						//hp1.addStyleName(style.indexOdd());
						hp1.addStyleName(style.indexOdd());
					} else {
						//hp1.addStyleName(style.indexEven());
						hp1.addStyleName(style.indexEven());
					}
					counter++;
				} else {
					HorizontalPanel main = new HorizontalPanel();
					
					SimplePanel imageWrap = new SimplePanel();
					imageWrap.setStyleName(style.imageWrap());
					imageWrap.add(img);
					main.add(imageWrap);
					//hp1.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
					
					VerticalPanel right = new VerticalPanel();
					right.setWidth("100%");
					
					if(image.getTitle()!=null && image.getTitle().trim().length()>0) {
						HTML title = new HTML("<b>"+image.getTitle()+"</b>");
						right.add(title);
						right.setCellHorizontalAlignment(title, HasHorizontalAlignment.ALIGN_LEFT);
					} 
					
					main.add(right);
					main.setCellWidth(right, "100%");
					hp1.add(main);	

					if(counter%2 == 1) {
						//hp1.addStyleName(style.indexOdd());
						hp1.addStyleName(style.indexOdd());
					} else {
						//hp1.addStyleName(style.indexEven());
						hp1.addStyleName(style.indexEven());
					}
					counter++;
				}		
				
				final CheckBox box = new CheckBox();
				final ImageProxy _image = image;
				box.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						box.setEnabled(false);
						_container.addImageAsData(_image);
					}
				});				
				
				hp.add(box);
				hp.setCellWidth(box, "20px");
				hp.add(hp1);
				resultsContainerPanel.add(hp);
			}
		}
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
}
