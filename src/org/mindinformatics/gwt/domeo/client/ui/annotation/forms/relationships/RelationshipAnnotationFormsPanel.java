package org.mindinformatics.gwt.domeo.client.ui.annotation.forms.relationships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui.AnnotationSummaryRadioList;
import org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui.IPredicateSelection;
import org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui.ISubjectSelection;
import org.mindinformatics.gwt.domeo.plugins.annotation.relationship.ui.RelationshipSummaryRadioList;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.BioPortalManager;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalItemsRequestCompleted;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.MRelationshipIdentifiedByUri;
import org.mindinformatics.gwt.framework.src.IContainerPanel;
import org.mindinformatics.gwt.framework.src.IContentPanel;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.utils.src.HtmlUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class RelationshipAnnotationFormsPanel extends AFormsManager implements IContentPanel,
		IAnnotationEditListener, IRefreshableComponent, IAnnotationRefreshableComponent, IResizable,
		ISubjectSelection, IPredicateSelection, IBioPortalItemsRequestCompleted {

	private static final String TITLE = "Linking Annotation Creation";
	private static final String TITLE_EDIT = "Linking Annotation Editing";
	
	interface Binder extends UiBinder<HorizontalPanel, RelationshipAnnotationFormsPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private String _title;
	private Element _element;
	
	private MOnlineImage _imageResource;
	private Image imageBuffer;
	private List<AFormComponent> forms = new ArrayList<AFormComponent>();
	
	@UiField ScrollPanel annotationContainer;
	@UiField ScrollPanel relationshipsContainer;
	@UiField ScrollPanel objectsContainer;
	@UiField FlowPanel main;
	@UiField SpanElement footerSpan;
	
	public RelationshipAnnotationFormsPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
	}
	
	// ------------------------------------------------------------------------
	//  EDITING OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public RelationshipAnnotationFormsPanel(IDomeo domeo, ArrayList<MAnnotation> subjectAnnotations) {
		_domeo = domeo;
		_title = TITLE;

		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 300) + "px");
		
		refreshSubjectAnnotations(subjectAnnotations);
		
		//RelationshipFormProvider fp = new RelationshipFormProvider(_domeo);
		//fp.getForm(this, )
		
		/*
		_imageResource = ((MOnlineImage)annotation.getTarget());
		
		refreshImage((MOnlineImage)annotation.getTarget());
		refreshAnnotationForImage(annotation);
		
		//tabToolsPanel.setWidth((Window.getClientWidth() - 840) + "px");
		//tabToolsPanel.setHeight("590px");
		
		IFormGenerator formGenerator = _domeo.getAnnotationFormsManager().getAnnotationForm(annotation.getClass().getName());
		if(formGenerator!=null) {
			AFormComponent form = formGenerator.getForm(this, annotation);
			form.setWidth("600px");
			//tabToolsPanel.add(form, form.getTitle());
		}
		*/
	}
	
	/**
	 * Returns the image resource.
	 */
	public MGenericResource getResource() {
		return _imageResource;
	}
	
	// ------------------------------------------------------------------------
	//  CREATION OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public RelationshipAnnotationFormsPanel(IDomeo domeo,
			final String imageUrl, String imageTitle, String localId, final Element element, ArrayList<MAnnotation> existingAnnotationInTheTextSpan) {
		_domeo = domeo;
		_element = element;
		_title = TITLE;
		
		// Buffer the annotated resource
		_imageResource = new MOnlineImage();
		_imageResource.setLocalId(localId);
		_imageResource.setUrl(imageUrl);
		_imageResource.setLabel(imageTitle);
		_imageResource.setImage(element);
		_imageResource.setXPath(HtmlUtils.getElementXPath(element));
		
		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 340) + "px");
		
		//refreshImage(_imageResource);
		//refreshAnnotationForImage();
		
		//tabToolsPanel.setWidth((Window.getClientWidth() - 840) + "px");
		//tabToolsPanel.setHeight("590px");
		
		initializeForms();
	}
	
	public Element getSelectedElement() {
		return _element;
	}
	
	public int getImageY() {
		return _element.getAbsoluteTop();
	}
	
	@Override
	public void refresh() {
		initializeForms();
		//refreshAnnotationForImage();
	}
	
	public void initializeForms() {
		//tabToolsPanel.clear();
		
		Collection<IFormGenerator> formGenerators = _domeo.getAnnotationFormsManager().getAnnotationFormGenerators();
		Iterator<IFormGenerator> it = formGenerators.iterator();
		while(it.hasNext()) {
			AFormComponent form = it.next().getForm(this);
			form.setWidth("600px");
			if(form instanceof IResizable) forms.add(form);
			//tabToolsPanel.add(form, form.getTitle());
		}
	}
	
	public void refreshSubjectAnnotations(ArrayList<MAnnotation> subjectAnnotations) {
		annotationContainer.clear();
		AnnotationSummaryRadioList table = new AnnotationSummaryRadioList(_domeo, this, this);
		table.init();
		table.refreshPanel(subjectAnnotations);
		annotationContainer.add(table);
	}
	
	public String getTitle() {
		return _title;
	}

	@Override
	public void setContainer(IContainerPanel containerPanel) {
		_containerPanel = containerPanel;
	}
	@Override
	public IContainerPanel getContainer() {
		return _containerPanel;
	}
	public void hideContainer() {
		_domeo.getComponentsManager().removeComponent(this);
		_containerPanel.hide();
	}
	
	public void displayMessage(String message) {
		footerSpan.setInnerHTML(message);
	}
	
	public void clearMessage() {
		footerSpan.setInnerHTML("");
	}

//	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 340) + "px");
		//tabToolsPanel.setWidth((Window.getClientWidth() - 610) + "px");
		for(AFormComponent form:forms) {
			if(form instanceof IResizable) ((IResizable)form).resized();
		}
	}

	@Override
	public void editAnnotation(MAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectedSubject(MAnnotation annotation) {
		//Window.alert("Selected subject: " + annotation.getLocalId());
		
		relationshipsContainer.clear();
		
		FlowPanel fp = new FlowPanel();
		fp.add(new Label("Available relationships"));
		Image loadingIcon = new Image(Domeo.resources.spinningIcon());
		fp.add(loadingIcon);
		relationshipsContainer.add(fp);
		
		try {
			BioPortalManager bioPortalManager = BioPortalManager.getInstance();
			bioPortalManager.selectConnector(_domeo);
			bioPortalManager.searchTerms(this, "");
		} catch(Exception exc) {
			_domeo.getLogger().exception( 
				this, "Exception while searching BioPortal for " + "" + " - " + 
					exc.getMessage());
			return;
		}	
	}

	@Override
	public void returnTerms(ArrayList<MLinkedResource> terms) {
		ArrayList<MRelationshipIdentifiedByUri> relationships = new ArrayList<MRelationshipIdentifiedByUri>();
		for(MLinkedResource term: terms) {
			if(term instanceof MRelationshipIdentifiedByUri) relationships.add((MRelationshipIdentifiedByUri)term);
		}
	
		if(relationships.size()>0) {
			RelationshipSummaryRadioList rel = new RelationshipSummaryRadioList(_domeo, this, this);
			rel.refreshPanel(relationships);
			relationshipsContainer.clear();
			relationshipsContainer.add(rel);
		}
	}

	@Override
	public void selectedPredicate(MRelationshipIdentifiedByUri predicate) {
		Window.alert("Selected " + predicate.getLabel());
		objectsContainer.clear();
		AnnotationSummaryRadioList table = new AnnotationSummaryRadioList(_domeo, this, this);
		table.init();
		table.refreshPanel(_domeo.getAnnotationPersistenceManager().getAllAnnotations());
		objectsContainer.add(table);
	}

	@Override
	public ArrayList<MAnnotation> getTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void returnTerms(int totalPages, int pageSize, int pageNumber,
			ArrayList<MLinkedResource> terms) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void reportException() {
	}
	
	@Override
	public void reportException(String message) {
	}
}
