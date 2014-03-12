package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.images.ImageAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibody;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.NifAntibodyFactory;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies.AntibodiesSearchWidget;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies.IAntibodiesSearchContainer;
import org.mindinformatics.gwt.domeo.plugins.resource.nif.search.antibodies.IAntibodySelectionConsumer;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class FAntibodyForm extends AFormComponent implements IResizable, IAntibodiesSearchContainer, IAntibodySelectionConsumer {

	public static final String LABEL = "Antibody";
	public static final String LABEL_EDIT = "Edit Antibody";
	
	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING ANTIBODY";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING ANTIBODY";
	
	public static final String METHODS_PREFIX = 
		"http://www.unknown.com/method/";
	public static final String NIF_PREFIX = 
		"http://www.nif.org";
	public static final String ANTIBODY_PREFIX = 
		"http://antibodyregistry.org/Antibody12/antibodyform.html?gui_type=advanced&ab_id=";
	public static final String SPECIES_PREFIX = 
		"http://ontology.neuinfo.org/NIF/BiomaterialEntities/NIF-Organism.owl#";
	
	interface Binder extends UiBinder<VerticalPanel, FAntibodyForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private MAntibodyAnnotation _item;
	private MAntibody currentAntibody;
	
	private ArrayList<Widget> tabs = new ArrayList<Widget>();
	
	@UiField HorizontalPanel headerPanel;
	@UiField HorizontalPanel buttonsPanel;
	@UiField HorizontalPanel buttonsPanelSpacer;
	
	@UiField VerticalPanel container;
	@UiField FlowPanel newQualifiers;
	@UiField ListBox annotationSet;
	@UiField VerticalPanel rightColumn;
	@UiField TabBar tabBar;
	
	@UiField CheckBox methodib;
	@UiField CheckBox methodicc;
	@UiField CheckBox methodid;
	@UiField CheckBox methodiem;
	@UiField CheckBox methodif;
	@UiField CheckBox methodihc;
	@UiField CheckBox methodip;
	@UiField CheckBox methodria;
	@UiField CheckBox methodwb;
	@UiField CheckBox methodat;
	
	@UiField RadioButton none;
	@UiField RadioButton rat;
	@UiField RadioButton human;
	@UiField RadioButton mouse;
	@UiField RadioButton rabbit;
	
	@UiField TextArea commentBody;
	
	private AntibodiesSearchWidget antibodiesSearchWidget;
	
	public Set<MLinkedResource> getMethods() {
		Set<MLinkedResource> methods = new HashSet<MLinkedResource>();
		
		if(methodib.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(
					METHODS_PREFIX + "methodib", 
					"Immunoblotting", 
					"Immunoblotting", 
					METHODS_PREFIX + "method", 
					NIF_PREFIX, 
					"NIF STD"));
		if(methodif.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodif",*/ METHODS_PREFIX + "methodif", 
					"Immunofluorescence", "Immunofluorescence", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodwb.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodwb",*/ METHODS_PREFIX + "methodwb", 
					"Western Blot", "Western Blot", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodicc.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodicc",*/ METHODS_PREFIX + "methodicc", 
					"Immunocytochemistry", "Immunocytochemistry", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodid.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodid",*/ METHODS_PREFIX + "methodid", 
					"Immunodiffusion", "Immunodiffusion", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodiem.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodiem",*/ METHODS_PREFIX + "methodiem", 
					"Immuno-Electron Microscopy", "Immuno-Electron Microscopy", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodihc.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodihc",*/ METHODS_PREFIX + "methodihc", 
					"Immunohistochemistry", "Immunohistochemistry", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodip.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodip",*/ METHODS_PREFIX + "methodip", 
					"Immunoprecipitation", "Immunoprecipitation", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodria.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodria",*/ METHODS_PREFIX + "methodria", 
					"Radioimmunoassay", "Radioimmunoassay", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		if(methodat.getValue()) 
			methods.add(ResourcesFactory.createTrustedTypedResource(/*"methodat",*/ METHODS_PREFIX + "methodat", 
					"Antibody Therapy", "Antibody Therapy", METHODS_PREFIX + "method", NIF_PREFIX, "NIF STD"));
		return methods;
	}
	
	public MLinkedResource getSpecies() {
		if(rat.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(
				SPECIES_PREFIX + "birnlex_160", "Rat", "Rattus norvegicus", 
				SPECIES_PREFIX + "birnlex_2", NIF_PREFIX, "NIF STD");
		} else if(human.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(
				SPECIES_PREFIX + "birnlex_516", "Human", "Homo sapiens", 
				SPECIES_PREFIX + "birnlex_2", NIF_PREFIX, "NIF STD");
		} else if(mouse.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(
				SPECIES_PREFIX + "birnlex_167", "Mouse", "Mus musculus", 
				SPECIES_PREFIX + "birnlex_2", NIF_PREFIX, "NIF STD");
		} else if(rabbit.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(
				SPECIES_PREFIX + "birnlex_226", "Rabbit", "Oryctolagus cuniculus", 
				SPECIES_PREFIX + "birnlex_2", NIF_PREFIX, "NIF STD");
		}
		return null;
	}
	
	public FAntibodyForm(IDomeo domeo, final AFormsManager manager, boolean inFolders) {
		super(domeo);
		_manager = manager;
		
		initWidget(binder.createAndBindUi(this));
		
		refreshAnnotationSetFilter(annotationSet, null);

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(isContentInvalid()) return;
			
				try { 
					if(_item == null) {
						if(_manager instanceof TextAnnotationFormsPanel) {
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(), 
									_domeo.getPersistenceManager().getCurrentResource(), ((TextAnnotationFormsPanel)_manager).getHighlight().getExact(), 
									((TextAnnotationFormsPanel)_manager).getHighlight().getPrefix(), ((TextAnnotationFormsPanel)_manager).getHighlight().getSuffix());
							
							// TODO Register coordinate of the selection.
							MAntibodyAnnotation annotation = NifAntibodyFactory.createAntibody(
									((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
									_manager.getResource(), selector);
							// TODO Register coordinate of highlight.
							
							MAntibodyUsage antibodyUsage = NifAntibodyFactory.createAntibodyUsage();
							annotation.setAntibodyUsage(antibodyUsage);
								
							MAntibody normalizedAntibody = (MAntibody) _domeo.getResourcesManager().cacheResource(currentAntibody);
							annotation.getAntibodyUsage().setAntibody(normalizedAntibody);
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + currentAntibody.getLabel());
	
							annotation.setSubject(getSpecies());
							annotation.setComment(commentBody.getText());
							annotation.getProtocols().clear();
							annotation.addProtocols(getMethods());
								
							if(getSelectedSet(annotationSet)==null) {
								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
							} else {
								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
							}
							_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
							_manager.hideContainer();
							
						} else if(_manager instanceof ImageAnnotationFormsPanel) {
							MAntibodyAnnotation annotation = NifAntibodyFactory.createAntibody(_domeo,
									((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
									_manager.getResource());
	//						annotation.setY((((ImageAnnotationFormsPanel)_manager).getImageY()));
							
							MAntibodyUsage antibodyUsage = NifAntibodyFactory.createAntibodyUsage();
							annotation.setAntibodyUsage(antibodyUsage);
								
							MAntibody normalizedAntibody = (MAntibody) _domeo.getResourcesManager().cacheResource(currentAntibody);
							annotation.getAntibodyUsage().setAntibody(normalizedAntibody);
							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + currentAntibody.getLabel());
	
							annotation.setSubject(getSpecies());
							annotation.setComment(commentBody.getText());
							annotation.getProtocols().clear();
							annotation.addProtocols(getMethods());
							
	//						Iterator<MLinkedDataResource> termsIterator = terms.iterator();
	//						while(termsIterator.hasNext()) {
	//							MLinkedDataResource term = termsIterator.next();
	//							MLinkedDataResource normalizedTerm = (MLinkedDataResource) _domeo.getResourcesManager().cacheResource(term);
	//							annotation.addTerm(normalizedTerm);
	//							_domeo.getLogger().command(getLogCategoryCreate(), this, " with term " + term.getLabel());
	//						}
							
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
				} catch (Exception e) {
					_domeo.getLogger().exception(this, e.getMessage());
				}
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");
		antibodiesSearchWidget = new AntibodiesSearchWidget(_domeo, this, Domeo.resources, false);
		// TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
		//AntibodiesSelectionList termsList = new AntibodiesSelectionList(_domeo, this, getAntibodiesList(), new HashMap<String, MLinkedDataResource>(0));
		tabs.add(antibodiesSearchWidget);
		//tabs.add(termsList);		
		
		tabBar.addTab("Search for Antibodies");
		tabBar.addTab("Recently Used");
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));

		none.setValue(true);		
		refreshAssociatedAntibodies();
		resized();
	}
	
	// ------------------------
	//  EDITING OF ANNOTATION
	// ------------------------
	public FAntibodyForm(IDomeo domeo, final AFormsManager manager, final MAntibodyAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;
		
		initWidget(binder.createAndBindUi(this));
		
		try {
			if(_item.getComment()!=null) commentBody.setText(_item.getComment());
			refreshAnnotationSetFilter(annotationSet, annotation);
			
			if(_item.getSubject()!=null) { 
				if(_item.getSubject().getUrl().equals(SPECIES_PREFIX + "birnlex_167")) 
					mouse.setValue(true);
				else if(_item.getSubject().getUrl().equals(SPECIES_PREFIX + "birnlex_160")) 
					rat.setValue(true);
				else if(_item.getSubject().getUrl().equals(SPECIES_PREFIX + "birnlex_516")) 
					human.setValue(true);
				else if(_item.getSubject().getUrl().equals(SPECIES_PREFIX + "birnlex_226")) 
					rabbit.setValue(true);
			} else {
				none.setValue(true);
			}
			if(_item.getProtocols()!=null) {
				for(MLinkedResource protocol:_item.getProtocols()) {
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodib"))
						methodib.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodif"))
						methodif.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodwb"))
						methodwb.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodicc"))
						methodicc.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodid"))
						methodid.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodiem"))
						methodiem.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodihc"))
						methodihc.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodip"))
						methodip.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodria"))
						methodria.setValue(true);
					if(protocol.getUrl().equals(METHODS_PREFIX +"methodat"))
						methodat.setValue(true);
				}
			} 
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Failed to display current annotation " + annotation.getLocalId());
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}
		
		try {
			refreshAnnotationSetFilter(annotationSet, annotation);
			currentAntibody = annotation.getAntibodyUsage().getAntibody();
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
					_item.setComment(commentBody.getText());
					_item.setSubject(getSpecies());
					_item.getProtocols().clear();
					_item.addProtocols(getMethods());
					_item.getAntibodyUsage().setAntibody(currentAntibody);
					
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item, getSelectedSet(annotationSet));
					_manager.hideContainer();
				} catch(Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);
	
//		if(annotation.getUuid()!=null /*&& annotation.getHasChanged()*/) {
//			buttonsPanel.add(new HTML("&nbsp;"));
//			
//			ButtonWithIcon newVersionButton = new ButtonWithIcon();
//			newVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
//			newVersionButton.setWidth("170px");
//			newVersionButton.setHeight("22px");
//			newVersionButton.setResource(Domeo.resources.acceptLittleIcon());
//			newVersionButton.setText("Apply as new version");
//			newVersionButton.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) {
//					if(isContentInvalid()) return;
//					if(!isContentChanged(_item)) {
//						_manager.getContainer().hide();
//						return;
//					}
//					//_item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
//					//_item.setText(getPostItBody());
//					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotationAsNewVersion(_item, getSelectedSet(annotationSet));
//					_manager.hideContainer();
//				}
//			});
//			buttonsPanel.add(newVersionButton);
//		}

		this.setHeight("100px");
		antibodiesSearchWidget = new AntibodiesSearchWidget(_domeo, this, Domeo.resources, false);
		// TODO If border is needed, there is a need for a wrapper. SearchTermWidget is one
		//AntibodiesSelectionList termsList = new AntibodiesSelectionList(_domeo, this, getAntibodiesList(), new HashMap<String, MLinkedDataResource>(0));
		tabs.add(antibodiesSearchWidget);
		//tabs.add(termsList);		
		
		tabBar.addTab("Search for Antibodies");
		tabBar.addTab("Recently Used");
		tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				rightColumn.clear();
				rightColumn.add(tabs.get(event.getSelectedItem()));
			}
	    });
		
		rightColumn.add(tabs.get(0));
		refreshAssociatedAntibodies();
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
		if(currentAntibody==null) {
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
	
	public boolean isSubjectChanged() {
		if(_item!=null && _item.getSubject()==null && getSpecies()!=null) return true;
		if(_item!=null && _item.getSubject()!=null && getSpecies()==null) return true;
		if(_item!=null && _item.getSubject()!=null && getSpecies()!=null && !_item.getSubject().getUrl().equals(getSpecies().getUrl())) 
			return true;
		return false;
	}
	
	public boolean areMethodsChanged() {
		if(_item.getProtocols().size()!=getMethods().size()) return true;
		//TODO check items
		return false;
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO just checking the size is not right.
		if(_item!=null) {
			if(!_item.getAntibodyUsage().getAntibody().getUrl().equals(currentAntibody.getUrl())
					|| !commentBody.getText().equals(_item.getAntibodyUsage().getComment())) {
				return true;
			}
			if(isSubjectChanged()) return true;
		}	
		return false;
	}

//	@Override
//	public void resized() {
//		container.setWidth("100%");
//	}
	
	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 340) + "px");
		tabBar.setWidth((Window.getClientWidth() - 615) + "px");
		for(Widget tab:tabs) {
			//if(tab instanceof IResizable) ((IResizable)tab).resized();
			tab.setWidth((Window.getClientWidth() - 615) + "px");
		}
		
		buttonsPanelSpacer.setWidth(Math.max(0, (_manager.getContainerWidth()-300)) + "px");
		buttonsPanel.setWidth(Math.max(0, (_manager.getContainerWidth()-320)) + "px");
		headerPanel.setCellWidth(buttonsPanelSpacer, Math.max(0, (_manager.getContainerWidth()-300)) + "px");
	}

//	@Override
//	public String getTextContent() {
//		// TODO Auto-generated method stub
//		if(_manager instanceof ATextFormsManager)
//			return ((ATextFormsManager)_manager).getHighlight().getExact();
//		return "";
//	}

	public void addAssociatedAntibody(MAntibody antibody) {
		currentAntibody = antibody;
		refreshAssociatedAntibodies();
	}
	
	public void refreshAssociatedAntibodies() {
		if(currentAntibody!=null) 
			displayAssociatedAntibodies(currentAntibody);
		else {
			newQualifiers.clear();
			HTML h = new HTML("<em>no antibody selected</em>.");
			newQualifiers.add(h);
		}
		//refreshTabTitle();
	}
	
	private void displayAssociatedAntibodies(MAntibody antibody) {
		newQualifiers.clear();
		//for(MAntibody antibody: antibodies) {
			final MAntibody _antibody = antibody;
			HorizontalPanel sp = new HorizontalPanel();
			sp.setStyleName("cs-acceptedQualifier");
			HTML a = new HTML("<a target='_blank' href='" + antibody.getUrl() + "'>" + antibody.getLabel() + "</a> (" + antibody.getVendor() + ")");
			a.setStyleName("cs-acceptedQualifierLabel");
			a.setTitle(antibody.getLabel() + ": " + antibody.getDescription()); // + " - SOURCE: " + term.getSource().getLabel());
			sp.add(a);
			Image i = new Image(Domeo.resources.deleteLittleIcon());
			i.setStyleName("cs-rejectQualifier");
			i.setTitle("Remove " + antibody.getLabel());
			i.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					currentAntibody = null;
					antibodiesSearchWidget.removeAntibody(_antibody);
					refreshAssociatedAntibodies();
				}
			});
			sp.add(i);
			newQualifiers.add(sp);
		//}
	}

	@Override
	public ArrayList<MAntibody> getAntibodies() {
		// TODO Auto-generated method stub
		return new ArrayList<MAntibody>();
	}

	@Override
	public String getFilterValue() {
		// No filtering for this
		return "";
	}

	@Override
	public void addAntibody(MAntibody antibody) {
		addAssociatedAntibody(antibody);
	}

	@Override
	public ArrayList<MAntibody> getAntibodiesList() {
		//return _domeo.getAnnotationPersistenceManager().getAllTerms();
		return new ArrayList<MAntibody>();
	}
}
