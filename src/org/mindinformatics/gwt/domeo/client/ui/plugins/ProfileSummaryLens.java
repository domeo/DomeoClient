package org.mindinformatics.gwt.domeo.client.ui.plugins;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ProfileSummaryLens extends Composite {

	interface Binder extends UiBinder<Widget, ProfileSummaryLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MProfile _profile;
	private ProfileSummaryLens _this;
	private AvailableProfilesPanel _parent;
	
	@UiField VerticalPanel body;
	@UiField FocusPanel wrapper;
	@UiField SimplePanel nameLabel;
	@UiField SimplePanel itemsNumberLabel;
	//@UiField SimplePanel descriptionField;
	@UiField SimplePanel provenanceLabel;
	@UiField RadioButton selectionButton;

	public ProfileSummaryLens(IDomeo domeo, final AvailableProfilesPanel parent, MProfile profile) {
		_domeo = domeo;
		_this = this;
		_profile = profile;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));
		
		wrapper.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//parent.displayAannotationSetInfo(_set);
				selectionButton.setValue(true);
				parent.setSelectedProfile(_profile);
			}
		});
		
		/*
		nameEditableField.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				//_set.setLabel(event.getValue());
				//_set.setHasChanged(true);
				_domeo.refreshAnnotationComponents();
			}
		});
		*/
		
		selectionButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.setSelectedProfile(_profile);	
				event.stopPropagation();
			}
		});
		
		refreshLens();
	}
	
	public void setSelected(boolean selection) {
		selectionButton.setValue(selection);
	}
	
	public boolean isSelected() {
		return selectionButton.getValue().booleanValue();
	}

	public void refresh() {
		//_parent.refresh();
	}

	public void refreshLens() {
		try {
			/*
			if(_parent.isAnnotationSetSelected(_set)) {
				selectionButton.setValue(true);
			}
			*/
			
			nameLabel.add(new HTML(_profile.getName()));
			//descriptionField.add(new HTML(_set.getDescription()));
			
			/*
			itemsNumberLabel.clear();
			if(_profile.>0) {
				Label itemsNumberText = new Label(_set.getNumberOfAnnotationItems() +(_set.getNumberOfAnnotationItems()==1?" item":" items"));
				itemsNumberText.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//_domeo.displayAnnotationOfSet(_set);
					}
				});
				itemsNumberLabel.add(itemsNumberText);
			} else {
				itemsNumberLabel.add(new HTML("No items"));
			}
			*/
			
			// Provenence
			// ----------
			provenanceLabel.clear();
			DateTimeFormat fmt = DateTimeFormat.getFormat("MM/dd/yy h:mma");

			//provenanceLabel.add(new HTML("By <a target='_blank' href='" + _profile.getLastSavedBy().getUrl() + "'>" + _profile.getLastSavedBy().getName() +"</a> on " + _profile.getFormattedLastSavedOn()));
			provenanceLabel.add(new HTML("By " + _profile.getLastSavedBy().getName() +" on " + fmt.format(_profile.getLastSavedOn())));
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, "Visualization of the set " + _profile.getName() + " failed");
		}
	}
	
	public MProfile getUserProfile() {
		return _profile;
	}

	private void addSimpleButton(SimplePanel panel, Image img, ClickHandler clickHandler) {
		panel.clear();
		img.addClickHandler(clickHandler);
		panel.add(img);
	}
}
