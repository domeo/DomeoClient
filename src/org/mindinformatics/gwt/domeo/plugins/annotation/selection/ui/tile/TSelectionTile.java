package org.mindinformatics.gwt.domeo.plugins.annotation.selection.ui.tile;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation2;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionEditAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.multipletargets.MultipleTargetsAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.selection.model.MSelectionAnnotation;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TSelectionTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TSelectionTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	//private IDomeo _domeo;
	private MSelectionAnnotation _annotation;
	private MultipleTargetsAnnotationFormsPanel _multipleTargetsAnnotationFormsPanel;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField Label type;
	@UiField Label text;
	
	public TSelectionTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	public void setContainer(MultipleTargetsAnnotationFormsPanel container) {
		_multipleTargetsAnnotationFormsPanel = container;
	}
	
	@Override
	public void initializeLens(MAnnotation annotation) {
		try {
			_annotation = (MSelectionAnnotation) annotation;
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	public void initializeLens(MAnnotation annotation, MultipleTargetsAnnotationFormsPanel container) {
		_multipleTargetsAnnotationFormsPanel = container;
		try {
			_annotation = (MSelectionAnnotation) annotation;
			refresh();
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public Widget getTile() {
		return this;
	}
	@Override
	public void refresh() {
		try {
			this.createProvenanceBar(provenance, _annotation);

			type.setText("Selection:");
			text.setText(SelectorUtils.getMatch(_annotation.getSelector()));
			text.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit or display?");
				}
			});

			injectButtons("", content, _annotation);
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
	
	public void createProvenanceBar(HorizontalPanel provenance, final MAnnotation annotation) {
		int step = 0;
		try {
			Resources resource = Domeo.resources;
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			editIcon.setStyleName(ATileComponent.tileResources.css().button());
			editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, _listener, getAnnotation()));
			step=1;
			
			Image showIcon = new Image(resource.showLittleIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(ActionShowAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=2;
	
			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			if(_multipleTargetsAnnotationFormsPanel!=null) 
				deleteIcon.addClickHandler(ActionDeleteAnnotation2.getClickHandler(_domeo, this, getAnnotation(), _multipleTargetsAnnotationFormsPanel));
			else
				deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=3;
			
			// TODO move to an abstract tile class
			if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue()) {
				if(annotation.getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
					if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue()) {
						provenance.clear();
						step=4;
						// TODO Externalize the icon management to the plugins
						if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
							Image ic = new Image(Domeo.resources.multipleLittleIcon());
							ic.setTitle("Annotation on multiple targets");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
							Image ic = new Image(Domeo.resources.littleImageIcon());
							ic.setTitle("Annotation on image");
							provenance.add(ic);
							provenance.setCellWidth(ic, "18px");
						} else {
							if(SelectorUtils.isOnAnnotation(annotation.getSelectors())) {
								Image ic = new Image(Domeo.resources.littleCommentIcon());
								ic.setTitle("Annotation on annotation");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							} else if(SelectorUtils.isOnResourceTarget(annotation.getSelectors())) {
								Image ic = new Image(Domeo.resources.littleCommentsIcon());
								ic.setTitle("Annotation on annotation");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							} else {
								Image ic = new Image(Domeo.resources.littleTextIcon());
								ic.setTitle("Annotation on text");
								provenance.add(ic);
								provenance.setCellWidth(ic, "18px");
							}
						}
						step=5;
						provenance.add(new Label("By Me on " + annotation.getFormattedCreationDate()));
						if(!(annotation.getSelector() instanceof MTargetSelector) && !(annotation.getSelector() instanceof MAnnotationSelector)) {
							provenance.add(showIcon);
							provenance.setCellWidth(showIcon, "22px");
							if(!(annotation instanceof MSelectionAnnotation)) {
								provenance.add(editIcon);
								provenance.setCellWidth(editIcon, "22px");
							}
						}
						provenance.add(deleteIcon);
						provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_LEFT);
						provenance.setCellWidth(deleteIcon, "22px");
					} else {
						provenance.setVisible(false);
					}
				} else {
					provenance.clear();
					step=8;
					if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
						Image ic = new Image(Domeo.resources.multipleLittleIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if(annotation.getSelector()!=null && annotation.getSelector().getTarget() instanceof MOnlineImage) {
						Image ic = new Image(Domeo.resources.littleImageIcon());
						ic.setTitle("Annotation on image");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else {
						Image ic = new Image(Domeo.resources.littleTextIcon());
						ic.setTitle("Annotation on text");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					}
					
					step=9;
					provenance.add(new Label("By " + annotation.getCreator().getName() + " on " + annotation.getFormattedCreationDate()));
					 
					provenance.add(showIcon);
					provenance.add(editIcon);
					provenance.add(deleteIcon);
				}
			} else {
				provenance.setVisible(false);
			}
		} catch (Exception e) {
			_domeo.getLogger().exception(this, "Provenance bar generation exception @" + step + " " + e.getMessage());
		}
	}
}
