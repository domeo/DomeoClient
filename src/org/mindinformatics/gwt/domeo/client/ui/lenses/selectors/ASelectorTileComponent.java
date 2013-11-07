package org.mindinformatics.gwt.domeo.client.ui.lenses.selectors;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteSelector;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.TileResources;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class ASelectorTileComponent extends Composite {

	// By contract 
	protected IDomeo _domeo;
	protected IAnnotationEditListener _listener;
	
	public static final TileResources tileResources = GWT.create(TileResources.class);
	
	public ASelectorTileComponent(IDomeo domeo, IAnnotationEditListener listener) {
		_domeo = domeo;
		_listener = listener;
	}
	
	public abstract MSelector getSelector();
	
	public void injectButtons(FlowPanel content, final MAnnotation annotation) {
		if(!((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue() ||
				!(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue())) {
			
			Resources resource = Domeo.resources;
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			editIcon.setStyleName(ATileComponent.tileResources.css().button());
			editIcon.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit item not implmented yet! id:" + annotation.getLocalId());
				}
			});
			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteSelector.getClickHandler(_domeo, this, annotation, getSelector()));
			
			//content.add(editIcon);
			content.add(deleteIcon);
		}
	}
	
	public void createProvenanceBar(HorizontalPanel provenance, final MAnnotation annotation, final boolean single) {
		Resources resource = Domeo.resources;
		Image editIcon = new Image(resource.editLittleIcon());
		editIcon.setTitle("Edit Item");
		editIcon.setStyleName(ATileComponent.tileResources.css().button());
		//editIcon.addClickHandler(ActionEditSelector.getClickHandler(_domeo, this, _listener, annotation, getSelector()));
		Image deleteIcon = new Image(resource.deleteLittleIcon());
		deleteIcon.setTitle("Delete Item");
		deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
		deleteIcon.addClickHandler(ActionDeleteSelector.getClickHandler(_domeo, this, annotation, getSelector()));
		
		// TODO move to an abstract tile class
		if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue()) {
			if(getSelector().getCreator().getUri().equals(_domeo.getAgentManager().getUserPerson().getUri())) {
				if(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue()) {
					provenance.clear();
					
					// TODO Externalize the icon management to the plugins
					if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
						Image ic = new Image(Domeo.resources.multipleLittleIcon());
						ic.setTitle("Annotation on multiple targets");
						provenance.add(ic);
						provenance.setCellWidth(ic, "18px");
					} else if(annotation.getSelector().getTarget() instanceof MOnlineImage) {
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
					
					provenance.add(new Label("By Me on " + getSelector().getFormattedCreationDate()));
					//provenance.add(editIcon);
					provenance.setCellWidth(editIcon, "22px");
					if(!single) {
						provenance.add(deleteIcon);
						provenance.setCellHorizontalAlignment(deleteIcon, HasHorizontalAlignment.ALIGN_LEFT);
						provenance.setCellWidth(deleteIcon, "22px");
					}			
				} else {
					provenance.setVisible(false);
				}
			} else {
				provenance.clear();
				
				if(SelectorUtils.isOnMultipleTargets(annotation.getSelectors())) { 
					Image ic = new Image(Domeo.resources.multipleLittleIcon());
					ic.setTitle("Annotation on multiple targets");
					provenance.add(ic);
					provenance.setCellWidth(ic, "18px");
				} else if(annotation.getSelector().getTarget() instanceof MOnlineImage) {
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
				
				provenance.add(new Label("By " + getSelector().getCreator().getName() + " on " + getSelector().getFormattedCreationDate()));
				//provenance.add(editIcon);
				provenance.add(deleteIcon);
			}
		} else {
			provenance.setVisible(false);
		}
	}
}
