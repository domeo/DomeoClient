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
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.model.MQualifierAnnotation;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.search.terms.SearchTermsWidget;
import org.mindinformatics.gwt.framework.component.qualifiers.ui.ISearchTermsContainer;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class _FQualifierForm extends AFormComponent implements IResizable, ISearchTermsContainer {

	public static final String LABEL = "Qualifier";
	public static final String LABEL_EDIT = "Edit Qualifier";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING QUALIFIER";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING QUALIFIER";
	
	interface Binder extends UiBinder<VerticalPanel, _FQualifierForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private MQualifierAnnotation _item;
	private MLinkedResource currentQualifier;
	
	@UiField HorizontalPanel buttonsPanel;
	@UiField ListBox annotationSet;
	@UiField FlowPanel newQualifiers;
	@UiField VerticalPanel rightColumn;
	
	private SearchTermsWidget searchTermsWidget;
	
	private HashMap<String, MLinkedResource> associatedTerms = new HashMap<String, MLinkedResource>();
	
	public _FQualifierForm(IDomeo domeo, final AFormsManager manager) {
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
						// condiser multiple?
						Collection<MLinkedResource> terms = associatedTerms.values();
						MQualifierAnnotation annotation = AnnotationFactory.createQualifier(
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								_manager.getResource(), selector);
						Iterator<MLinkedResource> termsIterator = terms.iterator();
						if(termsIterator.hasNext()) {
							MLinkedResource term = termsIterator.next();
							// TODO Register coordinate of highlight.
							
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + term.getLabel());
							annotation.addTerm(term);
						}
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
						_manager.hideContainer();
					} else if(_manager instanceof ImageAnnotationFormsPanel) {
						/*
						//TODO saving of image annotation!!!!!!!!!
						MAnnotation annotation = AnnotationFactory.createPostIt(
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_manager.getResource(),
								getType(), getPostItBody());
						annotation.setY((((ImageAnnotationFormsPanel)_manager).getImageY()));
						_domeo.getLogger().command(getLogCategoryCreate(), this, "of type " + getType().getName());
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						//TODO Display annotation for an image is present somehow
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, _manager.getResource(), ((ImageAnnotationFormsPanel) _manager).getSelectedElement());
						((ImageAnnotationFormsPanel) _manager).initializeForms();
						*/
					}
				} else {
					//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
					//_item.setText(getPostItBody());
				}
				
				
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");
		
		searchTermsWidget = new SearchTermsWidget(_domeo, this, true);
		rightColumn.add(searchTermsWidget);
		
		refreshAssociatedTerms();
		
		/*
		buttonsPanel.add(yesButton);
		typeNote.setValue(true);
		*/
	}
	
	// Edit
	public _FQualifierForm(IDomeo domeo, final AFormsManager manager, final MQualifierAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;
		
		initWidget(binder.createAndBindUi(this));
		
		/*
		postitTypes.addItem(PostitType.NOTE.getName());
		postitTypes.addItem(PostitType.ERRATUM.getName());
		postitTypes.addItem(PostitType.COMMENT.getName());
		postitTypes.addItem(PostitType.EXAMPLE.getName());
		*/
		
		try {
			/*
			annotationBody.setText(annotation.getText());
			refreshAnnotationSetFilter(annotationSet, annotation);
		
			if(annotation.getType()==PostitType.NOTE) {
				postitTypes.setSelectedIndex(0);
			} else if(annotation.getType()==PostitType.ERRATUM) {
				postitTypes.setSelectedIndex(1);
			} else if(annotation.getType()==PostitType.EXAMPLE) {
				postitTypes.setSelectedIndex(3);
			} else if(annotation.getType()==PostitType.COMMENT) {
				postitTypes.setSelectedIndex(2);
			} 
			*/
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
	}
	
	public String getTitle() {
		return LABEL;
	}
	
	/*
	public PostitType getType() {
		return PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex()));
	}
	
	public String getPostItBody() {
		return annotationBody.getValue().trim();
	}
	*/

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
		/*
		PostitType newType = getType();
		
		// check if type changed
		if(!((MPostItAnnotation)annotation).getType().getName().equals(newType.getName())) 
			return true;
		// check if body changed
		if(!getPostItBody().equals(((MPostItAnnotation)annotation).getText()))
			return true;
		// check if set changed 
		if(!_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId()).equals(getSelectedSet(annotationSet))) 
			return true;
		*/
		return false;
	}

	@Override
	public void resized() {
		Window.alert("here");
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
}
