package org.mindinformatics.gwt.domeo.client.ui.annotation.tiles;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionDeleteAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionEditAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MOnlineImage;
import org.mindinformatics.gwt.domeo.model.selectors.MAnnotationSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTargetSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.qualifier.info.QualifierPlugin;
import org.mindinformatics.gwt.framework.component.preferences.src.BooleanPreference;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public abstract class ATileComponent extends Composite {

	// By contract 
	protected IDomeo _domeo;
	protected IAnnotationEditListener _listener;
	
	public static final TileResources tileResources = GWT.create(TileResources.class);
	
	public ATileComponent(IDomeo domeo, IAnnotationEditListener listener) {
		_domeo = domeo;
		_listener = listener;
	}
	
	public abstract MAnnotation getAnnotation();
	
	public void injectButtons(String plugin, FlowPanel content, final MAnnotation annotation) {
		if(!((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_PROVENANCE)).getValue() ||
				!(((BooleanPreference)_domeo.getPreferences().getPreferenceItem(Domeo.class.getName(), Domeo.PREF_DISPLAY_USER_PROVENANCE)).getValue())) {
			
			Resources resource = Domeo.resources;
			
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin)) {
				editIcon.setStyleName(ATileComponent.tileResources.css().button());
				editIcon.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Window.alert("Edit item not implmented yet! id:" + annotation.getLocalId());
					}
				});
			}
			
			Image showIcon = new Image(resource.showLittleIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(ActionShowAnnotation.getClickHandler(_domeo, this, getAnnotation()));

			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			
			content.add(showIcon);
			content.add(editIcon);
			content.add(deleteIcon);
		}
	}
	
	public void createProvenanceBar(String plugin, HorizontalPanel provenance, final MAnnotation annotation) {
		int step = 0;
		try {
			Resources resource = Domeo.resources;
			Image editIcon = new Image(resource.editLittleIcon());
			editIcon.setTitle("Edit Item");
			if(_domeo.getProfileManager().getUserCurrentProfile().isPluginEnabled(plugin)) {
				editIcon.setStyleName(ATileComponent.tileResources.css().button());
				editIcon.addClickHandler(ActionEditAnnotation.getClickHandler(_domeo, this, _listener, getAnnotation()));
			}
			step=1;
			
			Image showIcon = new Image(resource.showLittleIcon());
			showIcon.setTitle("Show Item in Context");
			showIcon.setStyleName(ATileComponent.tileResources.css().button());
			showIcon.addClickHandler(ActionShowAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=2;
	
			Image deleteIcon = new Image(resource.deleteLittleIcon());
			deleteIcon.setTitle("Delete Item");
			deleteIcon.setStyleName(ATileComponent.tileResources.css().button());
			deleteIcon.addClickHandler(ActionDeleteAnnotation.getClickHandler(_domeo, this, getAnnotation()));
			step=3;
			
			_domeo.getLogger().debug(this, ""+annotation.getCreator());
			_domeo.getLogger().debug(this, annotation.getCreator().getUri());
			
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
							provenance.add(editIcon);
							provenance.setCellWidth(editIcon, "22px");
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
