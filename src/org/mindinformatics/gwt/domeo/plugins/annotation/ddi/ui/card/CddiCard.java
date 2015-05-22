package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.ui.card;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.Resources;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AnnotationFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.info.ddiPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiUsage;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CddiCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CddiCard> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private int _index = -1;
	private MddiAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance, cardResults;
	@UiField
	FlowPanel content;

	
	@UiField
	Label drug1, drug1label, drug2, type, text, role1, type1, role2, type2,
			modality, evidenceType, assertType;

	public CddiCard(IDomeo domeo) {
		super(domeo);
		initWidget(binder.createAndBindUi(this));

		// Resources resource = Domeo.resources;
		// Image acceptImage = new Image(resource.acceptIcon());

		// rightIcon.add(acceptImage);

		tileResources.css().ensureInjected();
	}

	@Override
	public void initializeCard(CurationPopup curationPopup,
			MAnnotation annotation) {
		_annotation = (MddiAnnotation) annotation;
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
				createProvenanceBar(ddiPlugin.getInstance().getPluginName(),
						_index, provenance, _annotation);
			else
				createProvenanceBar(ddiPlugin.getInstance().getPluginName(),
						provenance, _annotation);

			MTextQuoteSelector hselector = (MTextQuoteSelector) _annotation
					.getSelector();

			String exact = hselector.getExact();

			if (exact.length() > 50)
				exact = exact.substring(0, 49) + "...";

			type.setText("Sentence: " + exact);

			final TextArea comment = new TextArea();
			// final DialogBox dialog = new DialogBox();
			final VerticalPanel panel = new VerticalPanel();
			// final HorizontalPanel hp1 = new HorizontalPanel();
			comment.setCharacterWidth(50);
			comment.setVisibleLines(10);
			panel.add(comment);
			
			if (_annotation != null) {
				MddiUsage dataUsage = _annotation.getMpDDIUsage();
				
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

//				if (dataUsage.getStatement() != null) {
//					String statStr = dataUsage.getStatement().getLabel();
//					statement.setText(statStr);
//				} else {
//					statement.setText("");
//				}

				if (dataUsage.getModality() != null) {
					String modalityStr = dataUsage.getModality().getLabel();
					modality.setText(modalityStr);
				} else {
					modality.setText("");
				}

				if (dataUsage.getAssertType() != null) {
										
					String assertText = dataUsage.getAssertType().getLabel();
					System.out.println("assertion type: " + assertText);

					assertType.setText(assertText);

					if (assertText.equals("increase-auc")) {
						addIncreaseAUC(dataUsage);
					} 

				} else {
					assertType.setText("PDDI study annotation");
				}

				if (dataUsage.getEvidenceType() != null) {
					evidenceType
							.setText(dataUsage.getEvidenceType().getLabel());
				} else {
					evidenceType.setText("");
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

			injectButtons(ddiPlugin.getInstance().getPluginName(), content,
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

	/*
	 * add increase AUC fields into card
	 */

	private void addIncreaseAUC(MddiUsage dataUsage) {

		if (dataUsage.getNumOfparcipitants() != null) {
			cardResults.add(createColumnInCard("Number of participant",
					dataUsage.getNumOfparcipitants().getLabel()));
		}
		
		if (dataUsage.getObjectDose() != null) {
			cardResults.add(createColumnInCard("Object dose",
					dataUsage.getObjectDose().getLabel()));
		}
		
		if (dataUsage.getPreciptDose() != null) {
			cardResults.add(createColumnInCard("Precipitant dose",
					dataUsage.getPreciptDose().getLabel()));
		}
		
		if (dataUsage.getIncreaseAuc() != null) {
			cardResults.add(createColumnInCard("AUC",
					dataUsage.getIncreaseAuc().getLabel()));
		}

	}

	/*
	 * take card label and value to create a vertical panel as new column
	 */

	private VerticalPanel createColumnInCard(String label, String value) {

		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel hPanel1 = new HorizontalPanel();
		HorizontalPanel hPanel2 = new HorizontalPanel();

		//System.out.println("drug1label style:" + drug1label.getStyleName());
		
		Label label1 = new Label(label);
		label1.setStyleName(drug1label.getStyleName());
		hPanel1.add(label1);
		hPanel1.setStyleName(cardResults.getStyleName());
		
		Label label2 = new Label(value);
		label2.setStyleName(drug1.getStyleName());
		hPanel2.add(label2);
		hPanel2.setStyleName(cardResults.getStyleName());
		
		vPanel.add(hPanel1);
		vPanel.add(hPanel2);

		return vPanel;
	}

}
