package org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.images.ImageAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.ATextFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.ITermsSelectionConsumer;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.TermsSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms.SearchTermsWidget;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.NifResourcesSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.NifResourcesSearch;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.SearchNifResourcesWidget;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ISearchTermsContainer;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class FQualifierForm2 extends AFormComponent implements IResizable, ISearchTermsContainer, ITermsSelectionConsumer {

	public static final String LABEL = "Qualifier";
	public static final String LABEL_EDIT = "Edit Qualifier";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING QUALIFIER";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING QUALIFIER";
	
	interface Binder extends UiBinder<VerticalPanel, FQualifierForm2> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private MQualifierAnnotation _item;
	private MLinkedResource currentQualifier;
	
	private ArrayList<Widget> tabs = new ArrayList<Widget>();
	
	@UiField VerticalPanel container;
	@UiField FlowPanel newQualifiers;
	@UiField HorizontalPanel buttonsPanel;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	
	private SearchTermsWidget searchTermsWidget;
	
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	
	public FQualifierForm2(IDomeo domeo, final AFormsManager manager, boolean inFolders) {
		super(domeo);
		_manager = manager;
		
		initWidget(binder.createAndBindUi(this));
				
		refreshAnnotationSetFilter(annotationSet, null);
		
		/*
		postitTypes.addItem(PostitType.NOTE.getName());
		postitTypes.addItem(PostitType.ERRATUM.getName());
		postitTypes.addItem(PostitType.COMMENT.getName());
		postitTypes.addItem(PostitType.EXAMPLE.getName());
		*/
		
		
		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(isContentInvalid()) return;
			
				if(_item == null) {
					if(_manager instanceof TextAnnotationFormsPanel) {
						MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
								((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
						
						Collection<MLinkedResource> terms = associatedTerms.values();
						// TODO Register coordinate of highlight.
						MQualifierAnnotation annotation = AnnotationFactory.createQualifier(
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								_manager.getResource(), selector);
						// TODO Register coordinate of highlight.
							
						Iterator<MLinkedResource> termsIterator = terms.iterator();
						while(termsIterator.hasNext()) {
							MLinkedResource term = termsIterator.next();
							MLinkedResource normalizedTerm = (MLinkedResource) _domeo.getResourcesManager().cacheResource(term);
							annotation.addTerm(normalizedTerm);
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + term.getLabel());
						}
							
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
						_manager.hideContainer();
						
					} else if(_manager instanceof ImageAnnotationFormsPanel) {
						Collection<MLinkedResource> terms = associatedTerms.values();
						//MLinkedDataResource term = termsIterator.next();
						MQualifierAnnotation annotation = AnnotationFactory.createQualifier(_domeo,
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								_manager.getResource());
						annotation.setY((((ImageAnnotationFormsPanel)_manager).getImageY()));
						
						Iterator<MLinkedResource> termsIterator = terms.iterator();
						while(termsIterator.hasNext()) {
							MLinkedResource term = termsIterator.next();
							MLinkedResource normalizedTerm = (MLinkedResource) _domeo.getResourcesManager().cacheResource(term);
							annotation.addTerm(normalizedTerm);
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + term.getLabel());
						}
						
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						//TODO Display annotation for an image is present somehow
						_domeo.getAnnotationPersistenceManager().cacheAnnotationOfImage(_manager.getResource().getUrl(), annotation);
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, _manager.getResource(), ((ImageAnnotationFormsPanel) _manager).getSelectedElement());
						((ImageAnnotationFormsPanel) _manager).initializeForms();
						
						// Display cached images
						Object w = _domeo.getResourcePanelsManager().getResourcePanel(
								_domeo.getPersistenceManager().getCurrentResource().getClass().getName());
						if(w instanceof ICachedImages) {
							((ICachedImages)w).createVisualization();
						}
					}
				} else {
					//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
					//_item.setText(getPostItBody());
				}
				
				
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");
		searchTermsWidget = new SearchTermsWidget(_domeo, this, false);
		
		NifResourcesSearch nifTermsSearch = new NifResourcesSearch();
		NifResourcesSelectionList nifResourcesList = new NifResourcesSelectionList(_domeo, this, getTermsList(), new HashMap<String, MLinkedResource>(0));
		SearchNifResourcesWidget searchNif = new SearchNifResourcesWidget(_domeo, this, nifTermsSearch, nifResourcesList, false);
		// TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
		
		tabs.add(searchTermsWidget);
		tabs.add(searchNif);
		
	      TermsSelectionList termsList = new TermsSelectionList(_domeo, this, getTermsList(), new HashMap<String, MLinkedResource>(0));
	        tabs.add(termsList); 
		//tabs.add(nifResourcesList);		
		
		tabBar.addTab(searchTermsWidget.getWidgetTitle());
		tabBar.addTab(searchNif.getWidgetTitle());
		tabBar.addTab("Recently Used");
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));

		
		refreshAssociatedTerms();
		
		resized();
		
		/*
		buttonsPanel.add(yesButton);
		typeNote.setValue(true);
		*/
	}
	
	// ------------------------
	//  EDITING OF ANNOTATION
	// ------------------------
	public FQualifierForm2(IDomeo domeo, final AFormsManager manager, final MQualifierAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;
		
		initWidget(binder.createAndBindUi(this));

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);
			
			for(int j=0; j<annotation.getTerms().size(); j++) {
				addAssociatedTerm(annotation.getTerms().get(j));
			}
		} catch(Exception e) {
			_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to display current annotation " + annotation.getLocalId());
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}
		
		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					if(isContentInvalid()) return;
					if(!isContentChanged(_item)) {
						_domeo.getLogger().debug(this, "No changes to save for annotation " + _item.getLocalId());
						_manager.getContainer().hide();
						return;
					}
					//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
					//_item.setText(getPostItBody());
					_item.getTerms().clear();
					_item.getTerms().addAll(associatedTerms.values());
					
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item, getSelectedSet(annotationSet));
					_manager.hideContainer();
				} catch(Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);
	
		if(annotation.getUuid()!=null /*&& annotation.getHasChanged()*/) {
			buttonsPanel.add(new HTML("&nbsp;"));
			
			ButtonWithIcon newVersionButton = new ButtonWithIcon();
			newVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
			newVersionButton.setWidth("170px");
			newVersionButton.setHeight("22px");
			newVersionButton.setResource(Domeo.resources.acceptLittleIcon());
			newVersionButton.setText("Apply as new version");
			newVersionButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(isContentInvalid()) return;
					if(!isContentChanged(_item)) {
						_manager.getContainer().hide();
						return;
					}
					//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
					//_item.setText(getPostItBody());
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotationAsNewVersion(_item, getSelectedSet(annotationSet));
					_manager.hideContainer();
				}
			});
			buttonsPanel.add(newVersionButton);
		}
		
		this.setHeight("100px");
		searchTermsWidget = new SearchTermsWidget(_domeo, this, false);
	
	    NifResourcesSearch nifTermsSearch = new NifResourcesSearch();
	    NifResourcesSelectionList nifResourcesList = new NifResourcesSelectionList(_domeo, this, getTermsList(), new HashMap<String, MLinkedResource>(0));
	    SearchNifResourcesWidget searchNif = new SearchNifResourcesWidget(_domeo, this, nifTermsSearch, nifResourcesList, false);
	    // TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
	    
	    tabs.add(searchTermsWidget);
	    tabs.add(searchNif);
	    
	    TermsSelectionList termsList = new TermsSelectionList(_domeo, this, getTermsList(), new HashMap<String, MLinkedResource>(0));
	    tabs.add(termsList); 
		
		tabBar.addTab(searchTermsWidget.getWidgetTitle());
		tabBar.addTab("Recently Used");
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));
		refreshAssociatedTerms();
		resized();
	}
	
	public String getTitle() {
		return LABEL;
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_QUALIFIER_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_QUALIFIER_EDIT;
	}

	@Override
	public boolean isContentInvalid() {
		if(associatedTerms.size()<=0) {
			_manager.displayMessage("The body of the annotation cannot be empty!");
			Timer timer = new Timer() {
				public void run() {
					_manager.clearMessage();
				}
			};
			timer.schedule(2000);
			return true;
		}
		return false;
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO just checking the size is not right.
		if(associatedTerms.size()!=((MQualifierAnnotation)annotation).getTerms().size())
			return true;
		return false;
	}

	@Override
	public void resized() {
		container.setWidth("100%");
	}

	@Override
	public String getTextContent() {
		// TODO Auto-generated method stub
		if(_manager instanceof ATextFormsManager)
			return ((ATextFormsManager)_manager).getHighlight().getExact();
		return "";
	}

	@Override
	public void addAssociatedTerm(MLinkedResource term) {
		associatedTerms.put(term.getUrl(), term);
		refreshAssociatedTerms();
	}
	
	public void refreshAssociatedTerms() {
		if(associatedTerms!=null && associatedTerms.size()>0) 
			displayAssociatedTerms(associatedTerms.values());
		else {
			newQualifiers.clear();
			HTML h = new HTML("<em>no qualifier selected</em>.");
			newQualifiers.add(h);
		}
		//refreshTabTitle();
	}
	
	private void displayAssociatedTerms(Collection<MLinkedResource> terms) {
		newQualifiers.clear();
		for(MLinkedResource term: terms) {
			final MLinkedResource _term = term;
			HorizontalPanel sp = new HorizontalPanel();
			sp.setStyleName("cs-acceptedQualifier");
			Label a = new Label(term.getLabel());
			a.setStyleName("cs-acceptedQualifierLabel");
			a.setTitle(term.getLabel() + ": " + term.getDescription() + " - SOURCE: " + term.getSource().getLabel());
			sp.add(a);
			Image i = new Image(Domeo.resources.deleteLittleIcon());
			i.setStyleName("cs-rejectQualifier");
			i.setTitle("Remove " + term.getLabel());
			i.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					associatedTerms.remove(_term.getUrl());
					searchTermsWidget.removeTerm(_term);
					refreshAssociatedTerms();
				}
			});
			sp.add(i);
			newQualifiers.add(sp);
		}
	}

	@Override
	public ArrayList<MLinkedResource> getItems() {
		// TODO Auto-generated method stub
		return new ArrayList<MLinkedResource>();
	}

	@Override
	public String getFilterValue() {
		// No filtering for this
		return "";
	}

	@Override
	public void addTerm(MLinkedResource term) {
		addAssociatedTerm(term);
	}

	@Override
	public ArrayList<MLinkedResource> getTermsList() {
		return _domeo.getAnnotationPersistenceManager().getAllTerms();
	}
}
