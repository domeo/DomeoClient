package org.mindinformatics.gwt.domeo.client.ui.annotation.forms.images;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.IFormGenerator;
import org.mindinformatics.gwt.domeo.client.ui.east.annotation.AnnotationSummaryTable;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.framework.component.IAnnotationRefreshableComponent;
import org.mindinformatics.gwt.framework.component.IRefreshableComponent;
import org.mindinformatics.gwt.framework.component.resources.model.MGenericResource;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ImageAnnotationFormsPanel extends AFormsManager implements IContentPanel,
		IAnnotationEditListener, IRefreshableComponent, IAnnotationRefreshableComponent, IResizable {

	private static final String TITLE = "Image Annotation Creation";
	private static final String TITLE_EDIT = "Image Annotation Editing";
	
	interface Binder extends UiBinder<HorizontalPanel, ImageAnnotationFormsPanel> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private IContainerPanel _containerPanel;
	private String _title;
	private Element _element;
	
	private MOnlineImage _imageResource;
	private Image imageBuffer;
	private List<AFormComponent> forms = new ArrayList<AFormComponent>();
	
	@UiField FlowPanel imageContainer;
	@UiField ScrollPanel annotationContainer;
	@UiField FlowPanel main;
	@UiField TabLayoutPanel tabToolsPanel;
	@UiField SpanElement footerSpan;
	
	public ImageAnnotationFormsPanel(IDomeo domeo) {
		_domeo = domeo;
		
		initWidget(binder.createAndBindUi(this));
	}
	
	// ------------------------------------------------------------------------
	//  EDITING OF ANNOTATIONS OF VARIOUS KIND
	// ------------------------------------------------------------------------
	public ImageAnnotationFormsPanel(IDomeo domeo,
			final MAnnotation annotation, ArrayList<MAnnotation> existingAnnotationInTheTextSpan) {
		_domeo = domeo;
		_title = TITLE_EDIT;

		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 340) + "px");

		_imageResource = ((MOnlineImage)annotation.getSelector().getTarget());
		
		refreshImage((MOnlineImage)annotation.getSelector().getTarget());
		refreshAnnotationForImage(annotation);

		tabToolsPanel.setHeight("590px");
		
		IFormGenerator formGenerator = _domeo.getAnnotationFormsManager().getAnnotationForm(annotation.getClass().getName());
		if(formGenerator!=null) {
			AFormComponent form = formGenerator.getForm(this, annotation);
			form.setWidth("100%");
			tabToolsPanel.add(form, form.getTitle());
		}
		//tabToolsPanel.setWidth((Window.getClientWidth() - 620) + "px");
		//resized();
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
	public ImageAnnotationFormsPanel(IDomeo domeo,
			final String imageUrl, String imageTitle, String localId, final Element element, ArrayList<MAnnotation> existingAnnotationInTheTextSpan) {
		_domeo = domeo;
		_element = element;
		_title = TITLE;
		
		// Buffer the annotated resource
		_imageResource = new MOnlineImage();
		_imageResource.setUrl(imageUrl);
		_imageResource.setLocalId(localId);
		_imageResource.setLabel(imageTitle);
		_imageResource.setImage(element);
		_imageResource.setXPath(HtmlUtils.getElementXPath(element));
		
		// Create layout
		initWidget(binder.createAndBindUi(this)); 
		this.setWidth((Window.getClientWidth() - 340) + "px");
		
		refreshImage(_imageResource);
		refreshAnnotationForImage();
		
		//tabToolsPanel.setWidth((Window.getClientWidth() - 840) + "px");
		tabToolsPanel.setHeight("590px");
		
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
		refreshAnnotationForImage();
	}
	
	public void initializeForms() {
		tabToolsPanel.clear();
		
		Collection<IFormGenerator> formGenerators = _domeo.getAnnotationFormsManager().getAnnotationFormGenerators();
		Iterator<IFormGenerator> it = formGenerators.iterator();
		while(it.hasNext()) {
			AFormComponent form = it.next().getForm(this);
			//form.setWidth("600px");
			if(form instanceof IResizable) forms.add(form);
			tabToolsPanel.add(form, form.getTitle());
		}
	}
	
	public void refreshAnnotationForImage() {
		annotationContainer.clear();
		AnnotationSummaryTable table = new AnnotationSummaryTable(_domeo, this);
		table.init();
		table.refreshPanel(_domeo.getAnnotationPersistenceManager().getAllAnnotationsForResource(_imageResource.getUrl()));
		annotationContainer.add(table);
	}
	
	public void refreshAnnotationForImage(MAnnotation annotation) {
		annotationContainer.clear();
		
		List<MAnnotation> annotations = new ArrayList<MAnnotation>();
		annotations.add(annotation);
		AnnotationSummaryTable table = new AnnotationSummaryTable(_domeo, this);
		table.init();
		table.refreshPanel(annotations);
		annotationContainer.add(table);
	}
	
	public void refreshImage(MOnlineImage image) {
		imageBuffer = new Image(image.getUrl());
		if(imageBuffer.getHeight()>=imageBuffer.getWidth()) {
			if(imageBuffer.getHeight()>200) imageBuffer.setHeight(200+"px");
		} else {
			if(imageBuffer.getWidth()>400) imageBuffer.setWidth(400+"px");
		}
		imageContainer.add(imageBuffer);
		if(!image.getLabel().equals("undefined") && !image.getLabel().equals("null")) 
			imageContainer.add(new HTML(image.getLabel()));
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
		tabToolsPanel.setWidth((Window.getClientWidth() - 615) + "px");
		for(AFormComponent form:forms) {
			if(form instanceof IResizable) ((IResizable)form).resized();
		}
	}

	@Override
	public void editAnnotation(MAnnotation annotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<MAnnotation> getTargets() {
		// TODO Auto-generated method stub
		return null;
	}
}
