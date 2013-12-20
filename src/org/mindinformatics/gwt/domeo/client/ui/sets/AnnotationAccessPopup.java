package org.mindinformatics.gwt.domeo.client.ui.sets;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.component.groups.ui.GroupsAccessPicker;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.accesscontrol.AnnotationAccessManager;
import org.mindinformatics.gwt.framework.component.profiles.model.IProfile;
import org.mindinformatics.gwt.framework.component.ui.glass.EnhancedGlassPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationAccessPopup extends PopupPanel {

	//public static final CurationPopupResources localResources = 
	//	GWT.create(AnnotationAccessResources.class);
	
	private IDomeo _domeo;
	private AnnotationAccessPopup _this;
	private ILensRefresh _lens;
	private MAnnotationSet _set;
	
	private VerticalPanel vp = new VerticalPanel();
	
	public AnnotationAccessPopup(IDomeo domeo, ILensRefresh lens, MAnnotationSet set) {
		super(true, true);
		
		_domeo = domeo;
		_this = this;
		_lens = lens;
		_set = set;
		
		//localResources.popupCss().ensureInjected();
		//this.setStyleName(localResources.popupCss().popupPanel());
		//sp.setStyleName(localResources.popupCss().scrollPanel());

		
		
		this.setAnimationEnabled(true);
		this.setWidget(vp);
		
		
	}
	
	public void show(int x, int y) {
		buildPanel();
		this.setPopupPosition(x, y);
		this.show();
	}
	
	
	public void buildPanel() {
		
		HorizontalPanel publicPanel = new HorizontalPanel();
		RadioButton publicRadio = new RadioButton("myRadioGroup", "");
		
		if(!_domeo.getProfileManager().getUserCurrentProfile().isFeatureDisabled(IProfile.FEATURE_PUBLIC_ANNOTATION)) {
			if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(AnnotationAccessManager.PUBLIC)) 
				publicRadio.setValue(true);
			publicRadio.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to public");
					_domeo.getAnnotationAccessManager().setAnnotationSetAccess(_set, AnnotationAccessManager.PUBLIC);
					_domeo.getAnnotationAccessManager().clearAnnotaitonSetGroups(_set);
					_domeo.getComponentsManager().updateObjectLenses(_set);
					_lens.refresh();
					_this.hide();
				}
			});
			publicPanel.add(new Image(Domeo.resources.publicLittleIcon()));
			publicPanel.add(publicRadio);
			publicPanel.add(new Label("Public"));
			vp.add(publicPanel);
		}
		
		if(!_domeo.getProfileManager().getUserCurrentProfile().isFeatureDisabled(IProfile.FEATURE_GROUP_ANNOTATION)) {
			if(_domeo.getUserManager().getUsersGroups().size()>0) {
				HorizontalPanel groupsPanel = new HorizontalPanel();
				RadioButton groupsRadio = new RadioButton("myRadioGroup", "");
				if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(AnnotationAccessManager.GROUPS)) 
					groupsRadio.setValue(true);
				groupsRadio.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						GroupsAccessPicker gap = new GroupsAccessPicker(_domeo, _set, _lens);
						new EnhancedGlassPanel(_domeo, gap, gap.getTitle(), 800, false, false, false);
						
						_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to " + _domeo.getUserManager().getUsersGroups().size() + " groups");
						//_domeo.getAnnotationAccessManager().setAnnotationSetAccess(_set, AnnotationAccessManager.GROUPS);
						//_domeo.getAnnotationAccessManager().setAnnotationSetGroups(_set, _domeo.getUserManager().getUsersGroups());
						_domeo.getComponentsManager().updateObjectLenses(_set);
						//_lens.refresh();
						_this.hide();
					}
				});
				groupsPanel.add(new Image(Domeo.resources.friendsLittleIcon()));
				groupsPanel.add(groupsRadio);
				groupsPanel.add(new Label("Groups"));
				vp.add(groupsPanel);
			}
		}
		
		if(!_domeo.getProfileManager().getUserCurrentProfile().isFeatureDisabled(IProfile.FEATURE_PRIVATE_ANNOTATION)) {
			HorizontalPanel privatePanel = new HorizontalPanel();
			RadioButton privateRadio = new RadioButton("myRadioGroup", "");
			if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUri()))
				privateRadio.setValue(true);	
			privateRadio.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to private");
					_domeo.getAnnotationAccessManager().setAnnotationSetAccess(_set, _domeo.getAgentManager().getUserPerson().getUri());
					_domeo.getAnnotationAccessManager().clearAnnotaitonSetGroups(_set);
					_domeo.getComponentsManager().updateObjectLenses(_set);
					_lens.refresh();
					_this.hide();
				}
			});
			privatePanel.add(new Image(Domeo.resources.privateLittleIcon()));
			privatePanel.add(privateRadio);
			privatePanel.add(new Label("Private"));
			vp.add(privatePanel);
		}
	}
}
