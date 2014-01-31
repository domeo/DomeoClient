package org.mindinformatics.gwt.domeo.client.ui.east.resource.qualifiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.ITermsSelectionConsumer;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.ui.list.TermsSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms.SearchTermsWidget;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.NifResourcesSearch;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.NifResourcesSelectionList;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.resources.SearchNifResourcesWidget;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ISearchTermsContainer;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
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
public class QualifiersSearchPanel extends Composite implements IContentPanel, ISearchTermsContainer, ITermsSelectionConsumer {

	private static final String TITLE = "Qualifiers Search";
	
	// UI BInder
	interface Binder extends UiBinder<VerticalPanel, QualifiersSearchPanel> {}
	private static final Binder binder = GWT.create(Binder.class);	
	
	// By contract 
	private IDomeo _domeo;
	private IContainerPanel _glassPanel;
	
	SearchTermsWidget searchTermsWidget;
	ArrayList<MAnnotationSet>  setsBuffer;
	
	private ArrayList<Widget> tabs = new ArrayList<Widget>();
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	
	@UiField VerticalPanel container;
	@UiField FlowPanel newQualifiers;
	@UiField HorizontalPanel buttonsPanel;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	
	public QualifiersSearchPanel(IDomeo domeo) {
		this(domeo, 600);
	}
	
	public QualifiersSearchPanel(IDomeo domeo, int width) {
		_domeo = domeo;

		initWidget(binder.createAndBindUi(this)); // Necessary for initializing Composite
		
		refreshAnnotationSetFilter(annotationSet, null);
		
		//SearchTermsWidget widget = new SearchTermsWidget(_domeo, this, false);
		
		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MTargetSelector selector = AnnotationFactory.createTargetSelector(_domeo.getAgentManager().getUserPerson(), 
						_domeo.getPersistenceManager().getCurrentResource());
				_domeo.getLogger().debug(this, "00");
						
//						AnnotationFactory.createPrefixSuffixTextSelector(
//						_domeo.getAgentManager().getUserPerson(), 
//						_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
//						((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
				_domeo.getLogger().debug(this, "01");
				Collection<MLinkedResource> terms = associatedTerms.values();
				// TODO Register coordinate of highlight.
				MQualifierAnnotation annotation = AnnotationFactory.createQualifier(
						((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
						_domeo.getAgentManager().getUserPerson(), 
						_domeo.getAgentManager().getSoftware(),
						_domeo.getPersistenceManager().getCurrentResource(), selector);
				// TODO Register coordinate of highlight.
				_domeo.getLogger().debug(this, "02");
				Iterator<MLinkedResource> termsIterator = terms.iterator();
				while(termsIterator.hasNext()) {
					MLinkedResource term = termsIterator.next();
					MLinkedResource normalizedTerm = (MLinkedResource) _domeo.getResourcesManager().cacheResource(term);
					annotation.addTerm(normalizedTerm);
					_domeo.getLogger().command(this, " annotation with term " + term.getLabel());
				}
					
				if(getSelectedSet(annotationSet)==null) {
					_domeo.getLogger().debug(this, "03a");
					_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
				} else {
					_domeo.getLogger().debug(this, "03b");
					_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
					_domeo.getLogger().debug(this, "03c");
				}
				//_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
				//_manager.hideContainer();
				_glassPanel.hide();
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
	}
	
	protected void refreshAnnotationSetFilter(ListBox annotationSet, MAnnotation _item) {
		annotationSet.clear();
		setsBuffer = new ArrayList<MAnnotationSet>();
		int counter = 0;
		int selection = 0;
		
		// This line allows Domeo to create a new 'Default set' if no sets have been
		// created yet
		MAnnotationSet currentSet = _domeo.getAnnotationPersistenceManager().getCurrentSet();
		ArrayList<MAnnotationSet>  sets = _domeo.getAnnotationPersistenceManager().getAllUserSets();
			//_annotator.getAnnotationPersistenceManager()
			//.getUnlockedManuallyAnnotatedSets(true);

		for (MAnnotationSet set : sets) {
			//if (set.isLocked())
			//	continue;
			if(_item==null) {
				if(currentSet.getLocalId()==set.getLocalId()) {
					selection = counter;
				}
			} else {
				if(set.getLocalId().equals(_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(_item.getLocalId()).getLocalId())) {
					selection = counter;
				} 
			}
			if(set.getCreatedBy().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				annotationSet.addItem(set.getLabel() + " [by Me]");
			} else {
				annotationSet.addItem(set.getLabel());
			}
			setsBuffer.add(set);
			counter++; 
		}
		
		annotationSet.addItem("Create new set");
		setsBuffer.add(null);
		
		annotationSet.setSelectedIndex(selection);
		annotationSet.setWidth("200px");
	}
	
	@Override
	public void setContainer(IContainerPanel glassPanel) {
		_glassPanel = glassPanel;
	}

	@Override
	public IContainerPanel getContainer() {
		return _glassPanel;
	}
	
	public String getTitle() {
		return TITLE;
	}

	@Override
	public String getTextContent() {
		// TODO Auto-generated method stub
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
	public ArrayList<MLinkedResource> getTermsList() {
		return _domeo.getAnnotationPersistenceManager().getAllTerms();
	}

	@Override
	public String getFilterValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTerm(MLinkedResource term) {
		// TODO Auto-generated method stub
		
	}
	protected MAnnotationSet getSelectedSet(ListBox annotationSet) {
		return setsBuffer.get(annotationSet.getSelectedIndex());
	}
}
