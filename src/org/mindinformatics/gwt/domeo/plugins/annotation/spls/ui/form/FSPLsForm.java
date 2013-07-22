package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IAllowsMultipleTargets;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.images.ImageAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.multipletargets.MultipleTargetsAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.component.cache.images.ui.ICachedImages;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLsType;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class FSPLsForm extends AFormComponent implements IResizable, IAllowsMultipleTargets {

	public static final String LABEL = "SPL";
	public static final String LABEL_EDIT = "Edit SPL Annotation";
	
	public static final String LOG_CATEGORY_SPLS_CREATE = "CREATING SPL ANNOT";
	public static final String LOG_CATEGORY_SPLS_EDIT = "EDITING SPL ANNOT";
	
	interface Binder extends UiBinder<VerticalPanel, FSPLsForm> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private MSPLsAnnotation _item;
	
	@UiField HorizontalPanel buttonsPanel;
	@UiField TextArea annotationBody;
	@UiField ListBox annotationSet;
	@UiField ListBox splsTypes;
	
	public FSPLsForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);
		_manager = manager;
		
		initWidget(binder.createAndBindUi(this));

		refreshAnnotationSetFilter(annotationSet, null);

		splsTypes.addItem(SPLsType.NOTE_TYPE.getName());
		splsTypes.addItem(SPLsType.TAG_TYPE.getName());
		splsTypes.addItem(SPLsType.ERRATUM_TYPE.getName());
		splsTypes.addItem(SPLsType.COMMENT_TYPE.getName());
		splsTypes.addItem(SPLsType.EXAMPLE_TYPE.getName());
		splsTypes.addItem(SPLsType.DESCRIPTION_TYPE.getName());
		
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
						
						// TODO Register coordinate of highlight.
						MAnnotation annotation = AnnotationFactory.createSPLs(
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								_manager.getResource(), selector,
								getType(), getSPLsBody());
						_domeo.getLogger().command(getLogCategoryCreate(), this, "of type " + getType().getName());
						
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}
						_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation, ((TextAnnotationFormsPanel)_manager).getHighlight());
						_manager.hideContainer();
					} else if(_manager instanceof ImageAnnotationFormsPanel) {
						//TODO saving of image annotation!!!!!!!!!
						MAnnotation annotation = AnnotationFactory.createSPLs(_domeo,
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								_manager.getResource(),
								getType(), getSPLsBody());
						annotation.setY((((ImageAnnotationFormsPanel)_manager).getImageY()));
						_domeo.getLogger().command(getLogCategoryCreate(), this, "of type " + getType().getName());
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
					} else if(_manager instanceof MultipleTargetsAnnotationFormsPanel) {
						MAnnotation annotation = AnnotationFactory.createSPLs(
								((AnnotationPersistenceManager)_domeo.getPersistenceManager()).getCurrentSet(), 
								_domeo.getAgentManager().getUserPerson(), 
								_domeo.getAgentManager().getSoftware(),
								getType(), getSPLsBody());
						/*
						for(MAnnotation target: _manager.getTargets()) {
							annotation.addSelector(target.getSelector());
						}
						*/
						_domeo.getLogger().command(getLogCategoryCreate(), this, "of type " + getType().getName());
						if(getSelectedSet(annotationSet)==null) {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
						} else {
							_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, getSelectedSet(annotationSet));
						}

						//TODO multiple targets annotation creation
						_domeo.getContentPanel().getAnnotationFrameWrapper().performMultipleTargetsAnnotation(annotation, _manager.getTargets());
						//_domeo.refreshAnnotationComponents();
						_manager.hideContainer();
					}
				} else {
					_item.setType(SPLsType.findByName(splsTypes.getItemText(splsTypes.getSelectedIndex())));
					_item.setText(getSPLsBody());
				}
				
			}
		});
		buttonsPanel.add(yesButton);
		
		this.setHeight("100px");
		
		/*
		buttonsPanel.add(yesButton);
		typeNote.setValue(true);
		*/
	}
	
	// Edit
	public FSPLsForm(IDomeo domeo, final AFormsManager manager, final MSPLsAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;
		
		initWidget(binder.createAndBindUi(this));
		
		splsTypes.addItem(SPLsType.NOTE_TYPE.getName());
		splsTypes.addItem(SPLsType.TAG_TYPE.getName());
		splsTypes.addItem(SPLsType.ERRATUM_TYPE.getName());
		splsTypes.addItem(SPLsType.COMMENT_TYPE.getName());
		splsTypes.addItem(SPLsType.EXAMPLE_TYPE.getName());
		
		try {
			annotationBody.setText(annotation.getText());
			refreshAnnotationSetFilter(annotationSet, annotation);
		
			if(annotation.getType()==SPLsType.NOTE_TYPE) {
				splsTypes.setSelectedIndex(0);
			} else if(annotation.getType()==SPLsType.TAG_TYPE) {
				splsTypes.setSelectedIndex(1);
			} else if(annotation.getType()==SPLsType.ERRATUM_TYPE) {
				splsTypes.setSelectedIndex(2);
			} else if(annotation.getType()==SPLsType.EXAMPLE_TYPE) {
				splsTypes.setSelectedIndex(4);
			} else if(annotation.getType()==SPLsType.COMMENT_TYPE) {
				splsTypes.setSelectedIndex(3);
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
					_item.setType(SPLsType.findByName(splsTypes.getItemText(splsTypes.getSelectedIndex())));
					_item.setText(getSPLsBody());
					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item, getSelectedSet(annotationSet));
					_manager.hideContainer();
				} catch(Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this, "Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);
	
		if(annotation.getLineageUri()!=null /*&& annotation.getHasChanged()*/) {
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
					_item.setType(SPLsType.findByName(splsTypes.getItemText(splsTypes.getSelectedIndex())));
					_item.setText(getSPLsBody());
					_item.setHasChanged(true);
					_item.setNewVersion(true);
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
	
	public SPLsType getType() {
		return SPLsType.findByName(splsTypes.getItemText(splsTypes.getSelectedIndex()));
	}
	
	public String getSPLsBody() {
		return annotationBody.getValue().trim();
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_SPLS_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_SPLS_EDIT;
	}

	@Override
	public boolean isContentInvalid() {
		if(getSPLsBody().trim().length()==0) {
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
		SPLsType newType = getType();
		
		// check if type changed
		if(!((MSPLsAnnotation)annotation).getType().getName().equals(newType.getName())) 
			return true;
		// check if body changed
		if(!getSPLsBody().equals(((MSPLsAnnotation)annotation).getText()))
			return true;
		// check if set changed 
		if(!_domeo.getAnnotationPersistenceManager().getSetByAnnotationId(annotation.getLocalId()).equals(getSelectedSet(annotationSet))) 
			return true;
		
		return false;
	}

	@Override
	public void resized() {
	}
}
