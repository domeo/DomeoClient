package org.mindinformatics.gwt.domeo.client.ui.sets;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.accesscontrol.AnnotationAccessManager;

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
public class DiscussionAccessPopup extends PopupPanel {

	//public static final CurationPopupResources localResources = 
	//	GWT.create(AnnotationAccessResources.class);
	
	private IDomeo _domeo;
	private DiscussionAccessPopup _this;
	private ILensRefresh _lens;
	private MAnnotationSet _set;
	
	private VerticalPanel vp = new VerticalPanel();
	
	public DiscussionAccessPopup(IDomeo domeo, ILensRefresh lens, MAnnotationSet set) {
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
		if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(AnnotationAccessManager.PUBLIC)) 
			publicRadio.setValue(true);
		publicRadio.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to public");
				_domeo.getAnnotationAccessManager().setAnnotationSetAccess(_set, AnnotationAccessManager.PUBLIC);
				_domeo.getComponentsManager().updateObjectLenses(_set);
				_lens.refresh();
				_this.hide();
			}
		});
		publicPanel.add(new Image(Domeo.resources.publicLittleIcon()));
		publicPanel.add(publicRadio);
		publicPanel.add(new Label("Public"));
		vp.add(publicPanel);
		
		if(_domeo.getUserManager().getUsersGroups().size()>0) {
			HorizontalPanel groupsPanel = new HorizontalPanel();
			RadioButton groupsRadio = new RadioButton("myRadioGroup", "");
			groupsRadio.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to " + _domeo.getUserManager().getUsersGroups().size() + " groups");
					_domeo.getAnnotationAccessManager().setAnnotationSetGroups(_set, _domeo.getUserManager().getUsersGroups());
					_domeo.getComponentsManager().updateObjectLenses(_set);
					_lens.refresh();
					_this.hide();
				}
			});
			groupsPanel.add(new Image(Domeo.resources.friendsLittleIcon()));
			groupsPanel.add(groupsRadio);
			groupsPanel.add(new Label("Groups"));
			vp.add(groupsPanel);
		
			/*
			HorizontalPanel customPanel = new HorizontalPanel();
			RadioButton customRadio = new RadioButton("myRadioGroup", "");
			customRadio.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					_lens.refresh();
					_this.hide();
				}
			});
			customPanel.add(new Image(Domeo.resources.customLittleIcon()));
			customPanel.add(customRadio);
			customPanel.add(new Label("Custom"));
			vp.add(customPanel);
			*/
		}
		
		/*
		HorizontalPanel privatePanel = new HorizontalPanel();
		RadioButton privateRadio = new RadioButton("myRadioGroup", "");
		if(_domeo.getAnnotationAccessManager().getAnnotationSetAccess(_set).equals(_domeo.getAgentManager().getUserPerson().getUri()))
			privateRadio.setValue(true);	
		privateRadio.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " access policy to private");
				_domeo.getAnnotationAccessManager().setAnnotationSetAccess(_set, _domeo.getAgentManager().getUserPerson().getUri());
				_domeo.getComponentsManager().updateObjectLenses(_set);
				_lens.refresh();
				_this.hide();
			}
		});
		privatePanel.add(new Image(Domeo.resources.privateLittleIcon()));
		privatePanel.add(privateRadio);
		privatePanel.add(new Label("Private"));
		vp.add(privatePanel);
		*/
	}
}
