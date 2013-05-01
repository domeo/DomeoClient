package org.mindinformatics.gwt.domeo.client.ui.plugins;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.framework.component.profiles.model.MProfile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ProfileLens extends Composite {

	interface Binder extends UiBinder<Widget, ProfileLens> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	private IDomeo _domeo;
	private MProfile _profile;
	private ProfileLens _this;
	private AvailableProfilesPanel _parent;
	
	@UiField VerticalPanel body;
	@UiField FlowPanel wrapper;
	@UiField SimplePanel nameLabel;
	@UiField SimplePanel itemsNumberLabel;
	//@UiField SimplePanel descriptionField;
	@UiField SimplePanel provenanceLabel;
	@UiField SimplePanel pluginsListLabel;

	public ProfileLens(IDomeo domeo, final AvailableProfilesPanel parent, MProfile profile) {
		_domeo = domeo;
		_this = this;
		_profile = profile;
		_parent = parent;
		
		initWidget(binder.createAndBindUi(this));
		
		pluginsListLabel.add(new HTML("List of <b>enabled plugins</b> for the profile '" + profile.getName() + "'"));
		
		refreshLens();
	}

	public void refresh() {
		//_parent.refresh();
	}

	public void refreshLens() {
		try {
			nameLabel.add(new HTML("<b>" + _profile.getName() + "</b> - " + _profile.getDescription()));

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
