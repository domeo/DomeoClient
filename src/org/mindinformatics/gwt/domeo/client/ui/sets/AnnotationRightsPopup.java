package org.mindinformatics.gwt.domeo.client.ui.sets;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;

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
public class AnnotationRightsPopup extends PopupPanel {

	//public static final CurationPopupResources localResources = 
	//	GWT.create(AnnotationAccessResources.class);
	
	private IDomeo _domeo;
	private AnnotationRightsPopup _this;
	private ILensRefresh _lens;
	private MAnnotationSet _set;
	
	private VerticalPanel vp = new VerticalPanel();
	
	public AnnotationRightsPopup(IDomeo domeo, ILensRefresh lens, MAnnotationSet set) {
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
		HorizontalPanel writePanel = new HorizontalPanel();
		RadioButton publicRadio = new RadioButton("myRadioGroup", "");
		if(!_set.getIsLocked()) publicRadio.setValue(true);
		publicRadio.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " as Unlocked");
				_set.setIsLocked(false);
				_domeo.getComponentsManager().updateObjectLenses(_set);
				_lens.refresh();
				_this.hide();
			}
		});
		writePanel.add(new Image(Domeo.resources.readWriteLittleIcon()));
		writePanel.add(publicRadio);
		writePanel.add(new Label("Unlocked"));
		vp.add(writePanel);
		
		HorizontalPanel readOnlyPanel = new HorizontalPanel();
		RadioButton groupsRadio = new RadioButton("myRadioGroup", "");
		if(_set.getIsLocked()) groupsRadio.setValue(true);
		groupsRadio.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_domeo.getLogger().command(_this, "Set AnnotationSet " + _set.getLocalId() + " as Locked");
				_set.setIsLocked(true);
				_domeo.getComponentsManager().updateObjectLenses(_set);
				_lens.refresh();
				_this.hide();
			}
		});
		readOnlyPanel.add(new Image(Domeo.resources.readOnlyLittleIcon()));
		readOnlyPanel.add(groupsRadio);
		readOnlyPanel.add(new Label("Locked"));
		vp.add(readOnlyPanel);
	}
}
