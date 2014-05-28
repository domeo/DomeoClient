package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.card;

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
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.info.expertstudy_pDDIPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIUsage;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Cexpertstudy_pDDICard extends ACardComponent {

	interface Binder extends UiBinder<Widget, Cexpertstudy_pDDICard> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private int _index = -1;
	private Mexpertstudy_pDDIAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	@UiField
	FlowPanel content;

	@UiField
	Label drug1, drug2, type, text, role1, type1, role2, type2, statement, modality;

	@UiField
	Image wrongIcon, rightIcon;

	public Cexpertstudy_pDDICard(IDomeo domeo) {
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
		_annotation = (Mexpertstudy_pDDIAnnotation) annotation;
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

		try {
			if (_index > -1)
				createProvenanceBar(expertstudy_pDDIPlugin.getInstance()
						.getPluginName(), _index, provenance, _annotation);
			else
				createProvenanceBar(expertstudy_pDDIPlugin.getInstance()
						.getPluginName(), provenance, _annotation);

			type.setText("expertstudy_pDDI:");

			rightIcon.setResource(Domeo.resources.acceptIcon());
			wrongIcon.setResource(Domeo.resources.crossIcon());

			final TextArea comment = new TextArea();
			final DialogBox dialog = new DialogBox();
			final VerticalPanel panel = new VerticalPanel();
			final HorizontalPanel hp1 = new HorizontalPanel();
			comment.setCharacterWidth(50);
			comment.setVisibleLines(10);
			panel.add(comment);

			wrongIcon.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					Button close = new Button("Close");
					close.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							dialog.hide();
						}
					});
					hp1.add(close);

					Button submit = new Button("submit");
					submit.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							System.out.println("comment: " + comment.getText());
							dialog.hide();

						}
					});
					hp1.add(submit);

					panel.add(hp1);

					dialog.setWidget(panel);
					dialog.setText("The reason of revise:");
					int left = wrongIcon.getAbsoluteLeft();
					int top = wrongIcon.getAbsoluteTop();
					dialog.setPopupPosition(left, top + 10);
					dialog.setHeight("150px");
					dialog.setWidth("200px");
					dialog.show();

				}
			});

			if (_annotation != null) {
				Mexpertstudy_pDDIUsage dataUsage = _annotation.getMpDDIUsage();
				
				if (dataUsage.getDrug1() != null) {
					String drug1Str = dataUsage.getDrug1().getLabel();
					drug1.setText(drug1Str);
				} else {
					drug1.setText("");
				}
				
				if (dataUsage.getDrug2() != null) {
					String drug2Str = dataUsage.getDrug2().getLabel();
					drug2.setText(drug2Str);
				} else {
					drug2.setText("");
				}

				if (dataUsage.getRole1() != null) {
					String role1Str = dataUsage.getRole1().getLabel();
					role1.setText(role1Str);
				} else {
					role1.setText("");
				}
				
				if (dataUsage.getRole2() != null) {
					String role2Str = dataUsage.getRole2().getLabel();
					role2.setText(role2Str);
				} else {
					role2.setText("");
				}
				
				if (dataUsage.getType1() != null) {
					String type1Str = dataUsage.getType1().getLabel();
					type1.setText(type1Str);
				} else {
					type1.setText("");
				}
				
				if (dataUsage.getType2() != null) {
					String type2Str = dataUsage.getType2().getLabel();
					type2.setText(type2Str);
				} else {
					type2.setText("");
				}
				
				if (dataUsage.getStatement() != null) {
					String statStr = dataUsage.getStatement().getLabel();
					statement.setText(statStr);
				} else {
					statement.setText("");
				}
				
				if (dataUsage.getModality() != null) {
					String modalityStr = dataUsage.getModality().getLabel();
					modality.setText(modalityStr);
				} else {
					modality.setText("");
				}



			}

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

			injectButtons(expertstudy_pDDIPlugin.getInstance().getPluginName(),
					content, _annotation);

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
