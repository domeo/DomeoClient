package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.card;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.info.AntibodyPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.info.SPLsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CSPLsCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CSPLsCard> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private int _index = -1;
	private MSPLsAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	@UiField
	FlowPanel content;

	@UiField
	Label type, text, pkimpact;

	@UiField SimplePanel acceptIcon, broadIcon, wrongIcon;

	public CSPLsCard(IDomeo domeo) {
		super(domeo);
		initWidget(binder.createAndBindUi(this));

		System.out.println("step2***");
		// Resources resource = Domeo.resources;
		// Image acceptImage = new Image(resource.acceptIcon());

		// rightIcon.add(acceptImage);

		tileResources.css().ensureInjected();
	}

	@Override
	public void initializeCard(CurationPopup curationPopup,
			MAnnotation annotation) {
		_annotation = (MSPLsAnnotation) annotation;
		_curationPopup = curationPopup;
		refresh();
	}

	@Override
	public void initializeCard(int index, CurationPopup curationPopup,
			MAnnotation annotation) {
		_index = index;
		initializeCard(curationPopup, annotation);
	}

	@Override
	public void setSpan(Element element) {
		_span = element;
	}

	@Override
	public Widget getCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {

		System.out.println("step1***");

		try {
			if (_index > -1)
				createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
						_index, provenance, _annotation);
			else
				createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
						provenance, _annotation);
			type.setText("SPLs:");

			System.out.println("pk in card: "
					+ _annotation.getPharmgxUsage().getPkImpact().getLabel());

			text.setText("clinical statements from SPLs");
			pkimpact.setText("Pk Impact: "
					+ _annotation.getPharmgxUsage().getPkImpact().getLabel());
			
			acceptIcon.add(new Image(Domeo.resources.acceptIcon()));
			broadIcon.add(new Image(Domeo.resources.acceptBroadIcon()));
			wrongIcon.add(new Image(Domeo.resources.crossIcon()));


			if (SelectorUtils.isOnMultipleTargets(_annotation.getSelectors())) {
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(new HTML("Targets:&nbsp;"));
				for (MSelector sel : _annotation.getSelectors()) {
					SimpleIconButtonPanel bu = new SimpleIconButtonPanel(
							_domeo, ActionShowAnnotation.getClickHandler(
									_domeo, _annotation.getLocalId() + ":"
											+ sel.getLocalId()),
							Domeo.resources.showLittleIcon().getSafeUri()
									.asString(), "Show target in context");
					hp.add(bu);
				}
				content.add(hp);
			}

			injectButtons(SPLsPlugin.getInstance().getPluginName(), content,
					_annotation);
		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

	@Override
	public MAnnotation getAnnotation() {
		// TODO Auto-generated method stub
		return _annotation;
	}

}
