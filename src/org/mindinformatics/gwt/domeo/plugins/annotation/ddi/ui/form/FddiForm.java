package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.ui.form;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.AFormsManager;
import org.mindinformatics.gwt.domeo.client.ui.annotation.forms.text.TextAnnotationFormsPanel;
import org.mindinformatics.gwt.domeo.client.ui.content.AnnotationFrameWrapper;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.buffers.HighlightedTextBuffer;
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.Iddi;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.Mddi;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.ddiFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.thirdparty.guava.common.base.Throwables;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class FddiForm extends AFormComponent implements IResizable, Iddi {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "PK DDI";
	public static final String LABEL_EDIT = "EDIT ddi ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING ddi ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING ddi ANNOTATION";

	// public static final String ddi_POC_PREFIX =
	// "http://purl.org/net/nlprepository/ddi-annotation-poc#";

	interface Binder extends UiBinder<VerticalPanel, FddiForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private MddiAnnotation _item;
	private Mddi currentMpDDI;
	// private ArrayList<Widget> tabs = new ArrayList<Widget>();

	@UiField
	VerticalPanel container;
	@UiField
	FlowPanel newQualifiers;
	@UiField
	HorizontalPanel buttonsPanel;
	@UiField
	ListBox annotationSet, drug1, drug2;
	@UiField
	VerticalPanel rightColumn;

	// type of drug1
	@UiField
	RadioButton typeai1, typemb1, typedp1, typedg1;

	// type of drug2
	@UiField
	RadioButton typeai2, typemb2, typedp2, typedg2;

	// role of drug1
	@UiField
	RadioButton roleob1, rolepp1;

	// role of drug1
	@UiField
	RadioButton roleob2, rolepp2;

	// modality
	@UiField
	RadioButton modalitypt, modalitynt;

	@UiField
	Label commentlabel;

	// assertion type
	@UiField
	ListBox assertType;

	// evidence modality
	@UiField
	RadioButton evidenceSpt, evidenceAgt;

	// clinical trail fields
	@UiField
	TextArea numParticipt, objectDose, preciptDose, auc, cl, comment, cmax, t12, preciptDuration, objectDuration;

	@UiField
	ListBox preciptFormu, objectFormu, objectregimens, preciptregimens, aucDirection, clDirection, aucType, clType,
			cmaxDirection, cmaxType, t12Direction, t12Type;

	String[] regimensL = { "UNK", "SD", "QD", "BID", "TID", "QID", "Q12", "Q8", "Q6", "Daily" };
	String[] directionL = { "UNK", "Increase", "Decrease" };
	String[] typeL = { "UNK", "Percent", "Fold", "Hours" };
	String[] formulationL = { "UNK", "Oral", "IV", "transdermal" };

	// drug 1 and drug 2

	public MLinkedResource getDrug1() {

		int indexdrug1 = drug1.getSelectedIndex();
		String drug1Uri = "";
		if (indexdrug1 != 0) {
			String drug1Str = drug1.getItemText(indexdrug1);

			if (Mddi.getDrugEntities().containsKey(drug1Str.toLowerCase())) {
				drug1Uri = "http://purl.bioontology.org/ontology/RXNORM/"
						+ Mddi.getDrugEntities().get(drug1Str.toLowerCase()).getRxcui();
			} else
				drug1Uri = RXNORM_PREFIX;

			return ResourcesFactory.createLinkedResource(drug1Uri, drug1Str,
					"Referred to the drug in the interaction.");
		}

		return null;
	}

	public MLinkedResource getDrug2() {

		int indexdrug2 = drug2.getSelectedIndex();
		String drug2Uri = "";
		if (indexdrug2 != 0) {
			String drug2Str = drug2.getItemText(indexdrug2);

			if (Mddi.getDrugEntities().containsKey(drug2Str.toLowerCase())) {
				drug2Uri = "http://purl.bioontology.org/ontology/RXNORM/"
						+ Mddi.getDrugEntities().get(drug2Str.toLowerCase()).getRxcui();
			} else
				drug2Uri = RXNORM_PREFIX;

			return ResourcesFactory.createLinkedResource(drug2Uri, drug2Str,
					"Referred to the drug in the interaction.");
		}
		return null;
	}

	// role of drug 1 and drug 2

	public MLinkedResource getRoleOfDrug1() {

		if (roleob1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "object-drug-of-interaction",
					"object-drug-of-interaction",
					"Referred to the role that each drug one plays within the interaction.");
		} else if (rolepp1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "precipitant-drug-of-interaction",
					"precipitant-drug-of-interaction",
					"Referred to the role that each drug one plays within the interaction.");
		}

		return null;
	}

	public MLinkedResource getRoleOfDrug2() {

		if (roleob2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "object-drug-of-interaction",
					"object-drug-of-interaction",
					"Referred to the role that each drug two plays within the interaction.");
		} else if (rolepp2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "precipitant-drug-of-interaction",
					"precipitant-drug-of-interaction",
					"Referred to the role that each drug two plays within the interaction.");
		}
		return null;
	}

	// type of drug 1 and drug 2

	public MLinkedResource getTypeOfDrug1() {

		if (typeai1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient", "active-ingredient",
					"Referred to the type of the mention within the sentence for drug.");
		} else if (typemb1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "metabolite", "metabolite",
					"Referred to the type of the mention within the sentence for drug.");
		} else if (typedp1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "drug-product", "drug-product",
					"Referred to the type of the mention within the sentence for drug.");
		} else if (typedg1.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "drug-group", "drug-group",
					"Referred to the type of the mention within the sentence for drug.");
		}

		return null;
	}

	public MLinkedResource getTypeOfDrug2() {

		if (typeai2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient", "active-ingredient",
					"Referred to the type of the mention within the sentence for drug two.");
		} else if (typemb2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "metabolite", "metabolite",
					"Referred to the type of the mention within the sentence for drug two.");
		} else if (typedp2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "drug-product", "drug-product",
					"Referred to the type of the mention within the sentence for drug two.");
		} else if (typedg2.getValue()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "drug-group", "drug-group",
					"Referred to the type of the mention within the sentence for drug.");
		}
		return null;
	}

	// statement default selection (ddi:qualitative; auc: quantitative)

	public MLinkedResource getStatement() {
		if (assertType.getSelectedIndex() == 1) {
			return ResourcesFactory.createLinkedResource(NCIT_PREFIX + "quantitative", "quantitative",
					"Referred to data type that is described in the sentence.");
		} else if (assertType.getSelectedIndex() == 0) {
			return ResourcesFactory.createLinkedResource(NCIT_PREFIX + "qualitative", "Qualitative",
					"Referred to data type that is described in the sentence.");
		} else
			return null;
	}

	// modality

	public MLinkedResource getModality() {

		if (modalitypt.getValue()) {
			return ResourcesFactory.createLinkedResource(SIO_PREFIX + "positive", "positive",
					"Referred to extra sources of information that the annotator considers "
							+ "are helpful to provide evidence for or against the existence of the pDDI.");
		} else if (modalitynt.getValue()) {
			return ResourcesFactory.createLinkedResource(SIO_PREFIX + "negative", "negative",
					"Referred to extra sources of information that the annotator considers "
							+ "are helpful to provide evidence for or against the existence of the pDDI.");
		}

		return null;
	}

	// increase AUC fields
	// TextArea numParticipt, objectDose, preciptDose, auc, evidence type;

	public MLinkedResource getNumOfParticipants() {

		if (!numParticipt.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "numOfParticipants", numParticipt.getText(),
					"The number of participants involved in interaction");
		} else
			return null;
	}

	public MLinkedResource getObjectDose() {

		if (!objectDose.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "objectDose", objectDose.getText(),
					"The dose of object drug in interation");
		} else
			return null;
	}

	public MLinkedResource getObjectDoseDuration() {

		if (!objectDuration.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "objectDoseDuration",
					objectDuration.getText(), "The duration of object drug in interation");
		} else
			return null;
	}

	public MLinkedResource getPreciptDose() {

		if (!preciptDose.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "preciptDose", preciptDose.getText(),
					"The dose of preciptant drug in interation");
		} else
			return null;
	}

	public MLinkedResource getPreciptDoseDuration() {

		if (!preciptDuration.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "preciptDoseDuration",
					preciptDuration.getText(), "The duration of precipt drug in interation");
		} else
			return null;
	}

	public MLinkedResource getObjectFormulation() {

		int indexFormu = objectFormu.getSelectedIndex();

		if (indexFormu != 0) {
			String objectFormuStr = objectFormu.getItemText(indexFormu);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "objectFormulation", objectFormuStr,
					"Referred to the object drug formulation.");
		}
		return null;
	}

	public MLinkedResource getObjectRegimens() {

		int indexRegimen = objectregimens.getSelectedIndex();

		if (indexRegimen != 0) {
			String drugStr = objectregimens.getItemText(indexRegimen);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "objectRegimens", drugStr,
					"Referred to the drug in the interaction.");
		}
		return null;
	}

	public MLinkedResource getPreciptFormulation() {

		int indexFormu = preciptFormu.getSelectedIndex();

		if (indexFormu != 0) {
			String preciptFormuStr = preciptFormu.getItemText(indexFormu);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "preciptFormulation", preciptFormuStr,
					"Referred to the preciptant drug formulation.");
		}
		return null;
	}

	public MLinkedResource getPreciptRegimens() {

		int indexRegimen = preciptregimens.getSelectedIndex();

		if (indexRegimen != 0) {
			String drugStr = preciptregimens.getItemText(indexRegimen);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "preciptRegimens", drugStr,
					"Referred to the drug in the interaction.");
		}
		return null;
	}

	/*
	 * AUC
	 */

	public MLinkedResource getAuc() {

		if (!auc.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "auc", auc.getText(),
					"The maximum increasing Auc in interation");
		} else
			return null;
	}

	public MLinkedResource getAucDirection() {

		int indexDirection = aucDirection.getSelectedIndex();

		if (indexDirection != 0) {
			String directionStr = aucDirection.getItemText(indexDirection);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "aucDirection", directionStr,
					"Referred to the auc increase or decrease in the interaction.");
		}
		return null;
	}

	public MLinkedResource getAucType() {

		int indexType = aucType.getSelectedIndex();

		if (indexType != 0) {
			String typeStr = aucType.getItemText(indexType);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "aucType", typeStr,
					"Referred to the auc type in the interaction.");
		}
		return null;
	}

	/*
	 * CL
	 */

	public MLinkedResource getCl() {

		if (!cl.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "cl", cl.getText(),
					"The maximum increasing CL in interation");
		} else
			return null;
	}

	public MLinkedResource getclDirection() {

		int indexDirection = clDirection.getSelectedIndex();

		if (indexDirection != 0) {
			String directionStr = clDirection.getItemText(indexDirection);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "clDirection", directionStr,
					"Referred to the cl increase or decrease in the interaction.");
		}
		return null;
	}

	public MLinkedResource getclType() {

		int indexType = clType.getSelectedIndex();

		if (indexType != 0) {
			String typeStr = clType.getItemText(indexType);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "clType", typeStr,
					"Referred to the cl type in the interaction.");
		}
		return null;
	}

	/*
	 * cmax
	 */

	public MLinkedResource getCmax() {

		if (!cmax.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "cmax", cmax.getText(),
					"Cmax in clinical trail");
		} else
			return null;
	}

	public MLinkedResource getCmaxDirection() {

		int indexDirection = cmaxDirection.getSelectedIndex();

		if (indexDirection != 0) {
			String directionStr = cmaxDirection.getItemText(indexDirection);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "cmaxDirection", directionStr,
					"Referred to the cmax increase or decrease in the interaction.");
		}
		return null;
	}

	public MLinkedResource getCmaxType() {

		int indexType = cmaxType.getSelectedIndex();

		if (indexType != 0) {
			String typeStr = cmaxType.getItemText(indexType);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "cmaxType", typeStr,
					"Referred to the cmax type in the interaction.");
		}
		return null;
	}

	/*
	 * t12
	 */

	public MLinkedResource getT12() {

		if (!t12.getText().trim().isEmpty()) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "t12", t12.getText(),
					"T1/2 in clinical trail");
		} else
			return null;
	}

	public MLinkedResource getT12Direction() {

		int indexDirection = t12Direction.getSelectedIndex();

		if (indexDirection != 0) {
			String directionStr = t12Direction.getItemText(indexDirection);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "t12Direction", directionStr,
					"Referred to the t12 increase or decrease in the interaction.");
		}
		return null;
	}

	public MLinkedResource getT12Type() {

		int indexType = t12Type.getSelectedIndex();

		if (indexType != 0) {
			String typeStr = t12Type.getItemText(indexType);

			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "t12Type", typeStr,
					"Referred to the t12 type in the interaction.");
		}
		return null;
	}

	public MLinkedResource getEvidenceType() {

		if (evidenceSpt.getValue()) {
			return ResourcesFactory.createLinkedResource(MP_PREFIX + "supports", "evidence-supports",
					"evidence that supports drug-drug interaction assertion");
		} else if (evidenceAgt.getValue()) {
			return ResourcesFactory.createLinkedResource(MP_PREFIX + "challenges", "evidence-challenges",
					"evidence that challenges drug-drug interaction assertion");
		}
		return null;
	}

	public MLinkedResource getAssertType() {

		if (assertType.getSelectedIndex() == 0) {
			return ResourcesFactory.createLinkedResource(MP_PREFIX + "drug-drug-interaction", "drug-drug-interaction",
					"Drug drug interaction annotation");
		} else if (assertType.getSelectedIndex() == 1) {
			return ResourcesFactory.createLinkedResource(DIKBD2R_PREFIX + "EV_CT_DDI", "DDI-clinical-trial",
					"A study designed to quantify the pharmacokinetic and/or pharmacodynamic effects "
							+ "within study participants of a single drug in the presence of a purported precipitant.");
		} else
			return null;

	}

	// NEW annotation
	public FddiForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);

		// System.out.println("new annotation in form");

		_manager = manager;

		initWidget(binder.createAndBindUi(this));

		refreshAnnotationSetFilter(annotationSet, null);

		// get NER in selected text and add them into drugs (drop down list box)

		List<DrugInText> drugList = getNERToDrugs();

		if (drugList != null && drugList.size() != 0) {
			for (int i = 0; i < drugList.size(); i++) {

				int numberOfOcc = drugList.get(i).getNum();
				String drugName = drugList.get(i).getDrugname();

				// System.out.println("drugList- name:" + drugName + "| num:"
				// + numberOfOcc);

				for (int j = 0; j < numberOfOcc; j++) {
					drug1.addItem(drugName);
					drug2.addItem(drugName);
				}

			}
		}
		rightColumn.setVisible(false);

		// automatically select another role when user chosen one of role
		autoSelectAnotherRole();

		// highlight drug and preselect type
		highlightCurrentDrug();

		// switch assertion mode ddi/clinical trail
		switchAssertType();

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("OK");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// validates required fields
				if (!validatesRequiredFields())
					return;

				if (isContentInvalid())
					return;

				_domeo.getLogger().debug(this, "ddi annotation content validated");

				try {
					if (_item == null) {
						_domeo.getLogger().debug(this, "_item is null...");

						if (_manager instanceof TextAnnotationFormsPanel) {
							MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
									_domeo.getAgentManager().getUserPerson(),
									_domeo.getPersistenceManager().getCurrentResource(),
									((TextAnnotationFormsPanel) _manager).getHighlight().getExact(),
									((TextAnnotationFormsPanel) _manager).getHighlight().getPrefix(),
									((TextAnnotationFormsPanel) _manager).getHighlight().getSuffix());

							_domeo.getLogger().debug(this, "quote selector initialized");

							// TODO Register coordinate of the selection.
							MddiAnnotation annotation = ddiFactory.createddiAnnotation(
									((AnnotationPersistenceManager) _domeo.getPersistenceManager()).getCurrentSet(),
									_domeo.getAgentManager().getUserPerson(), _domeo.getAgentManager().getSoftware(),
									_manager.getResource(), selector);

							_domeo.getLogger().debug(this, "ddi annotation factory initialized");

							MddiUsage ddiUsage = ddiFactory.createddiUsage();

							ddiUsage.setRole1(getRoleOfDrug1());
							ddiUsage.setRole2(getRoleOfDrug2());
							ddiUsage.setType1(getTypeOfDrug1());
							ddiUsage.setType2(getTypeOfDrug2());
							ddiUsage.setStatement(getStatement());
							ddiUsage.setModality(getModality());
							ddiUsage.setDrug1(getDrug1());
							ddiUsage.setDrug2(getDrug2());
							ddiUsage.setNumOfparcipitants(getNumOfParticipants());
							ddiUsage.setPreciptDose(getPreciptDose());
							ddiUsage.setPreciptFormu(getPreciptFormulation());
							ddiUsage.setObjectDose(getObjectDose());
							ddiUsage.setObjectFormu(getObjectFormulation());
							ddiUsage.setIncreaseAuc(getAuc());
							ddiUsage.setEvidenceType(getEvidenceType());
							ddiUsage.setAssertType(getAssertType());
							ddiUsage.setCmax(getCmax());
							ddiUsage.setCmaxDirection(getCmaxDirection());
							ddiUsage.setCmaxType(getCmaxType());
							ddiUsage.setT12(getT12());
							ddiUsage.setT12Direction(getT12Direction());
							ddiUsage.setT12Type(getT12Type());
							ddiUsage.setObjectRegimen(getObjectRegimens());
							ddiUsage.setPreciptRegimen(getPreciptRegimens());
							ddiUsage.setObjectDuration(getObjectDoseDuration());
							ddiUsage.setPreciptDuration(getPreciptDoseDuration());
							ddiUsage.setAucDirection(getAucDirection());
							ddiUsage.setAucType(getAucType());
							ddiUsage.setCl(getCl());
							ddiUsage.setClDirection(getclDirection());
							ddiUsage.setClType(getclType());

							_domeo.getLogger().debug(this, "DDI annotation done");

							ddiUsage.setComment(comment.getText().trim());
							annotation.setMpDDIUsage(ddiUsage);

							_domeo.getLogger().debug(this, "annotation loaded");

							if (getSelectedSet(annotationSet) == null) {

								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation, true);
							} else {

								_domeo.getAnnotationPersistenceManager().addAnnotation(annotation,
										getSelectedSet(annotationSet));
							}

							_domeo.getContentPanel().getAnnotationFrameWrapper().performAnnotation(annotation,
									((TextAnnotationFormsPanel) _manager).getHighlight());

							_manager.hideContainer();

						}
					} else {
						_domeo.getLogger().debug(this, "_item is NOT null...");
						_manager.hideContainer();
					}
				} catch (Exception e) {
					logger.log(Level.WARNING, "throwing exception " + e.getMessage());
					_domeo.getLogger().exception(this, e.getMessage());
				}
			}
		});
		buttonsPanel.add(yesButton);

		_domeo.getLogger().debug(this, "'Apply' button added");

		this.setHeight("100px");

		// rightColumn.add(tabs.get(0));
		// resized();
	}

	// ------------------------
	// EDITING OF ANNOTATION
	// ------------------------
	public FddiForm(IDomeo domeo, final AFormsManager manager, final MddiAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;

		initWidget(binder.createAndBindUi(this));

		rightColumn.setVisible(false);

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);
			
			if (_item == null){
				displayDialog("current annotation is null", true);
				
			}

			// preselect assertion type

			if (_item.getAssertType() != null) {

				System.out.println("Assertion type:" + _item.getAssertType().getLabel());

				if (_item.getAssertType().getLabel().trim().equals("drug-drug-interaction")) {
					assertType.setSelectedIndex(0);
				} else if (_item.getAssertType().getLabel().trim().equals("DDI-clinical-trial")) {
					assertType.setSelectedIndex(1);
					rightColumn.setVisible(true);
				}

			} else
				assertType.setSelectedIndex(0);

			switchAssertType();
			
			currentMpDDI = annotation.getMpDDIUsage().getMpDDI();

			// check selections of drug role
			if (_item.getRole1() != null) {
				if (_item.getRole1().getLabel().equals("object-drug-of-interaction")) {
					roleob1.setValue(true);
				} else if (_item.getRole1().getLabel().equals("precipitant-drug-of-interaction")) {
					rolepp1.setValue(true);
				}
			}

			if (_item.getRole2() != null) {
				if (_item.getRole2().getLabel().equals("object-drug-of-interaction")) {
					roleob2.setValue(true);
				} else if (_item.getRole2().getLabel().equals("precipitant-drug-of-interaction")) {
					rolepp2.setValue(true);
				}
			}

			// check selections of drug type
			if (_item.getType1() != null) {
				if (_item.getType1().getLabel().equals("active-ingredient")) {
					typeai1.setValue(true);
				} else if (_item.getType1().getLabel().equals("metabolite")) {
					typemb1.setValue(true);
				} else if (_item.getType1().getLabel().equals("drug-product")) {
					typedp1.setValue(true);
				} else if (_item.getType1().getLabel().equals("drug-group")) {
					typedg1.setValue(true);
				}
			}

			if (_item.getType2() != null) {
				if (_item.getType2().getLabel().equals("active-ingredient")) {
					typeai2.setValue(true);
				} else if (_item.getType2().getLabel().equals("metabolite")) {
					typemb2.setValue(true);
				} else if (_item.getType2().getLabel().equals("drug-product")) {
					typedp2.setValue(true);
				} else if (_item.getType2().getLabel().equals("drug-group")) {
					typedg2.setValue(true);
				}
			}

			// check comment
			// System.out.println("comment in editing: " + _item.getComment());
			if (_item.getComment() != null && !_item.getComment().isEmpty()) {
				comment.setText(_item.getComment());

			}

			// check selections of modality
			if (_item.getModality() != null) {
				if (_item.getModality().getLabel().equals("positive")) {
					modalitypt.setValue(true);
				} else if (_item.getModality().getLabel().equals("negative")) {
					modalitynt.setValue(true);
				}
			}

			// drug 1 and drug 2

			List<DrugInText> drugList = getNERToDrugs();

			if (drugList != null && drugList.size() != 0) {
				for (int i = 0; i < drugList.size(); i++) {

					int numberOfOcc = drugList.get(i).getNum();
					String drugName = drugList.get(i).getDrugname();

					for (int j = 0; j < numberOfOcc; j++) {
						drug1.addItem(drugName);
						drug2.addItem(drugName);
					}

				}
			}

			if (_item.getDrug1() != null) {

				String strdrug1 = _item.getDrug1().getLabel();

				for (int i = 0; i < drug1.getItemCount(); i++) {
					if (drug1.getItemText(i).equals(strdrug1))
						drug1.setSelectedIndex(i);
				}

				preselectDrugType(drug1, 1);
				highlightCurrentDrugHelper(drug1, drug2);

			}

			if (_item.getDrug2() != null) {

				String strdrug2 = _item.getDrug2().getLabel();

				// System.out.println("drug2 in _item: " + strdrug2);

				for (int i = 0; i < drug2.getItemCount(); i++) {
					if (drug2.getItemText(i).equals(strdrug2))
						drug2.setSelectedIndex(i);
				}

				preselectDrugType(drug2, 2);
				highlightCurrentDrugHelper(drug2, drug1);

			}

			// increase Auc fields
			// TextArea numParticipt, objectDose, preciptDose, auc;

			if (_item.getNumOfparcipitants() != null) {
				numParticipt.setText(_item.getNumOfparcipitants().getLabel());
			}

			/*
			 * object dosage
			 */

			if (_item.getObjectDose() != null) {
				objectDose.setText(_item.getObjectDose().getLabel());
			}

			if (_item.getObjectFormu() != null) {

				String ObjectFormusStr = _item.getObjectFormu().getLabel();
				for (int i = 0; i < formulationL.length; i++) {
					if (formulationL[i].equals(ObjectFormusStr)) {
						objectFormu.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getObjectDuration() != null) {
				objectDuration.setText(_item.getObjectDuration().getLabel());
			}

			if (_item.getObjectRegimen() != null) {

				String objectregimensStr = _item.getObjectRegimen().getLabel();
				for (int i = 0; i < regimensL.length; i++) {
					if (regimensL[i].equals(objectregimensStr)) {
						objectregimens.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * precipt dosage
			 */

			if (_item.getPreciptDose() != null) {
				preciptDose.setText(_item.getPreciptDose().getLabel());
			}

			if (_item.getPreciptFormu() != null) {

				String preciptFormusStr = _item.getPreciptFormu().getLabel();
				for (int i = 0; i < formulationL.length; i++) {
					if (formulationL[i].equals(preciptFormusStr)) {
						preciptFormu.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getPreciptDuration() != null) {
				preciptDuration.setText(_item.getPreciptDuration().getLabel());
			}

			if (_item.getPreciptRegimen() != null) {

				String preciptregimensStr = _item.getPreciptRegimen().getLabel();
				for (int i = 0; i < regimensL.length; i++) {
					if (regimensL[i].equals(preciptregimensStr)) {
						preciptregimens.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * auc
			 */

			if (_item.getIncreaseAuc() != null) {
				auc.setText(_item.getIncreaseAuc().getLabel());
			}

			if (_item.getAucDirection() != null) {
				String aucDirectionStr = _item.getAucDirection().getLabel();
				for (int i = 0; i < directionL.length; i++) {
					if (directionL[i].equals(aucDirectionStr)) {
						aucDirection.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getAucType() != null) {
				String aucTypeStr = _item.getAucType().getLabel();
				for (int i = 0; i < typeL.length; i++) {
					if (typeL[i].equals(aucTypeStr)) {
						aucType.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * cl
			 */

			if (_item.getCl() != null) {
				cl.setText(_item.getCl().getLabel());
			}

			if (_item.getClDirection() != null) {
				String clDirectionStr = _item.getClDirection().getLabel();
				for (int i = 0; i < directionL.length; i++) {
					if (directionL[i].equals(clDirectionStr)) {
						clDirection.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getClType() != null) {
				String clTypeStr = _item.getClType().getLabel();
				for (int i = 0; i < typeL.length; i++) {
					if (typeL[i].equals(clTypeStr)) {
						clType.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * cmax
			 */

			if (_item.getCmax() != null) {
				cmax.setText(_item.getCmax().getLabel());
			}

			if (_item.getCmaxDirection() != null) {
				String CmaxDirectionStr = _item.getCmaxDirection().getLabel();
				for (int i = 0; i < directionL.length; i++) {
					if (directionL[i].equals(CmaxDirectionStr)) {
						cmaxDirection.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getCmaxType() != null) {
				String CmaxTypeStr = _item.getCmaxType().getLabel();
				for (int i = 0; i < typeL.length; i++) {
					if (typeL[i].equals(CmaxTypeStr)) {
						cmaxType.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * t12
			 */

			if (_item.getT12() != null) {
				t12.setText(_item.getT12().getLabel());
			}

			if (_item.getT12Direction() != null) {
				String T12DirectionStr = _item.getT12Direction().getLabel();
				for (int i = 0; i < directionL.length; i++) {
					if (directionL[i].equals(T12DirectionStr)) {
						t12Direction.setSelectedIndex(i + 1);
					}
				}
			}

			if (_item.getT12Type() != null) {
				String T12TypeStr = _item.getT12Type().getLabel();
				for (int i = 0; i < typeL.length; i++) {
					if (typeL[i].equals(T12TypeStr)) {
						t12Type.setSelectedIndex(i + 1);
					}
				}
			}

			/*
			 * evidence type
			 */

			if (_item.getEvidenceType() != null) {
				if (_item.getEvidenceType().getLabel().equals("evidence-supports")) {
					evidenceSpt.setValue(true);
				} else if (_item.getEvidenceType().getLabel().equals("evidence-challenges")) {
					evidenceAgt.setValue(true);
				}
			}

			// highlight drug
			highlightCurrentDrug();

			// automatically select another role when user chosen one of role
			autoSelectAnotherRole();

			// System.out.println("highlight drug mentions and select type in
			// EDIT..........");

		} catch (Exception e) {
			e.printStackTrace();

			String excepStr = "None";
			Object[] stackTrace = e.getStackTrace();
			if (stackTrace != null) {
				StringBuilder output = new StringBuilder();
				for (Object line : stackTrace) {
					output.append(line);
				}
				
				excepStr = excepStr + output.toString();
			}
			
			_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this,
					"Failed to display current annotation " + annotation.getLocalId());
			
			displayDialog("Failed to properly display existing annotation " + e.getMessage(), true);
		}

		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss().applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				try {

					// validates required fields
					if (!validatesRequiredFields())
						return;

					if (isContentInvalid()) {
						return; // TODO: modify this function to validate our UI

					}

					MTextQuoteSelector selector = AnnotationFactory.createPrefixSuffixTextSelector(
							_domeo.getAgentManager().getUserPerson(),
							_domeo.getPersistenceManager().getCurrentResource(),
							((TextAnnotationFormsPanel) _manager).getHighlight().getExact(),
							((TextAnnotationFormsPanel) _manager).getHighlight().getPrefix(),
							((TextAnnotationFormsPanel) _manager).getHighlight().getSuffix());

					_item.setAssertType(getAssertType());
					_item.setDrug1(getDrug1());
					_item.setDrug2(getDrug2());
					_item.setSelector(selector);
					_item.setRole1(getRoleOfDrug1());
					_item.setRole2(getRoleOfDrug2());
					_item.setType1(getTypeOfDrug1());
					_item.setType2(getTypeOfDrug2());
					_item.setStatement(getStatement());
					_item.setModality(getModality());
					_item.setNumOfparcipitants(getNumOfParticipants());
					_item.setObjectDose(getObjectDose());
					_item.setPreciptDose(getPreciptDose());
					_item.setObjectFormu(getObjectFormulation());
					_item.setPreciptFormu(getPreciptFormulation());
					_item.setObjectDuration(getObjectDoseDuration());
					_item.setPreciptDuration(getPreciptDoseDuration());
					_item.setIncreaseAuc(getAuc());
					_item.setAucDirection(getAucDirection());
					_item.setAucType(getAucType());
					_item.setCl(getCl());
					_item.setClDirection(getclDirection());
					_item.setClType(getclType());
					_item.setObjectRegimen(getObjectRegimens());
					_item.setPreciptRegimen(getPreciptRegimens());
					_item.setCmax(getCmax());
					_item.setCmaxType(getCmaxType());
					_item.setCmaxDirection(getCmaxDirection());
					_item.setT12(getT12());
					_item.setT12Type(getT12Type());
					_item.setT12Direction(getT12Direction());
					_item.setEvidenceType(getEvidenceType());
					_item.setComment(comment.getText());

					_item.getMpDDIUsage().setMpDDI(currentMpDDI);

					_domeo.getContentPanel().getAnnotationFrameWrapper().updateAnnotation(_item,
							getSelectedSet(annotationSet));

					_manager.hideContainer();
				} catch (Exception e) {
					_domeo.getLogger().exception(AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION, this,
							"Failed to apply modified anntoation " + annotation.getLocalId());
					displayDialog("Failed to apply modified annotation " + e.getMessage(), true);
				}
			}
		});
		buttonsPanel.add(sameVersionButton);

		this.setHeight("100px");

		// rightColumn.add(tabs.get(0));
		// resized();
	}

	public String getTitle() {
		return LABEL;
	}

	@Override
	public String getLogCategoryCreate() {
		return LOG_CATEGORY_QUALIFIER_CREATE;
	}

	@Override
	public String getLogCategoryEdit() {
		return LOG_CATEGORY_QUALIFIER_EDIT;
	}

	// TODO: edit this method to validate the relevant form elements
	@Override
	public boolean isContentInvalid() {

		return false;
	}

	public boolean areMethodsChanged() {

		return false;
	}

	@Override
	public void resized() {

	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * add drug names from highlight into drop down list box
	 */
	public List<DrugInText> getNERToDrugs() {

		ArrayList<MAnnotation> annotations = _domeo.getPersistenceManager().getAllAnnotations();

		Set<String> checked = new HashSet<String>();
		List<DrugInText> drugInListBox = new ArrayList<DrugInText>();

		// get expert study selected text
		HighlightedTextBuffer eselector = ((TextAnnotationFormsPanel) _manager).getHighlight();
		String sentence = eselector.getExact();

		// filter ao:highlight
		if (annotations.size() != 0 && annotations != null) {

			for (MAnnotation ann : annotations) {

				if (ann.getAnnotationType().equals("ao:Highlight")) {

					MHighlightAnnotation highlight = (MHighlightAnnotation) ann;

					MTextQuoteSelector hselector = (MTextQuoteSelector) highlight.getSelector();

					String drug = hselector.getExact().trim();

					if (sentence.contains(drug) && drug.length() > 1) {

						/*
						 * filter out incomplete drug highlight which is portion
						 * of word
						 * 
						 * such as one character highlight
						 */

						boolean isIncompleteHighlight = false;
						int index1 = sentence.indexOf(drug);
						if (index1 != 0 && (index1 + drug.length() < sentence.length())) {

							isIncompleteHighlight = Character.isLetter(sentence.charAt(index1 - 1))
									|| Character.isLetter(sentence.charAt(index1 + drug.length()));

						} else if (index1 == 0 && index1 + drug.length() < sentence.length()) {

							isIncompleteHighlight = !(sentence.charAt(index1 + drug.length()) == ' ');

						} else if (index1 + drug.length() == sentence.length()) {
							isIncompleteHighlight = !(sentence.charAt(index1 - 1) == ' ');
						}

						if (!checked.contains(drug) && !isIncompleteHighlight) {

							int num = countOcurrencesInStr(drug, sentence, 0);

							// System.out.println("numbers of " + drug + " is "
							// + num);

							drugInListBox.add(new DrugInText(drug, num));
							checked.add(drug);

						}

					}

				}
			}

		}
		return drugInListBox;
	}

	/*
	 * Embedded class that represents the occurrences of drug name in highlight
	 * text
	 */
	class DrugInText {

		private int num = 0;
		private String drugname;

		DrugInText(String name, int n) {
			this.num = n;
			this.drugname = name;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getDrugname() {
			return drugname;
		}

		public void setDrugname(String drugname) {
			this.drugname = drugname;
		}

	}

	/*
	 * count the number of occurrences of drug name in sentence
	 */

	public static int countOcurrencesInStr(String s, String str, int num) {

		int index = str.indexOf(s);

		if (index >= 0) {
			num++;
			num = countOcurrencesInStr(s, str.substring(index + s.length()), num);
		}

		return num;
	}

	/*
	 * automatically select another drug role
	 */

	public void autoSelectAnotherRole() {

		roleob1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				rolepp2.setValue(true);
			}
		});

		rolepp1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				roleob2.setValue(true);
			}
		});

		rolepp2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				roleob1.setValue(true);
			}
		});

		roleob2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				rolepp1.setValue(true);
			}
		});
	}

	/*
	 * highlight the current selection of drug in sentence span
	 * 
	 * preselect drug type
	 */

	public void highlightCurrentDrug() {

		// highlight drug1 and preselect type1

		drug1.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				preselectDrugType(drug1, 1);
				highlightCurrentDrugHelper(drug1, drug2);
			}
		});

		// highlight drug2 and preselect type2
		drug2.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				preselectDrugType(drug2, 2);
				highlightCurrentDrugHelper(drug2, drug1);
			}
		});

	}

	public void highlightCurrentDrugHelper(ListBox drugone, ListBox drugtwo) {

		// System.out.println(DOM.getInnerHTML(getElement()));

		String finalHtml = "";
		Element e = DOM.getElementById("exactmatch");

		// DivElement divID = (DivElement)
		// Document.get().getElementById("exactmatch");
		// System.out.println("***"+divID);

		if (e != null) {
			String html = e.getInnerHTML();

			finalHtml = html.replaceAll("<span style=\"background-color: #FFFF00\">", "").replaceAll("</span>", "");
			// System.out.println("html: " + finalHtml);

			int selected1 = drugone.getSelectedIndex();
			String currentDrug1 = drugone.getItemText(selected1);

			if (finalHtml.contains(currentDrug1) && currentDrug1 != null && !currentDrug1.trim().equals("")) {

				int currentMatch1 = getCurrentDrugMatch(finalHtml, currentDrug1, drugone);

				// System.out.println("match: " + currentDrug1 + " | at: "
				// + currentMatch1);

				finalHtml = finalHtml.substring(0, currentMatch1) + "<span style=\"background-color: #FFFF00\">"
						+ currentDrug1 + "</span>" + finalHtml.substring(currentMatch1 + currentDrug1.length());

				int selected2 = drugtwo.getSelectedIndex();
				String currentDrug2 = drugtwo.getItemText(selected2);

				// System.out.println("post html: " + finalHtml);

				if ((currentDrug2 != null && !currentDrug2.trim().equals("")) && finalHtml.contains(currentDrug2)) {

					int currentMatch2 = getCurrentDrugMatch(finalHtml, currentDrug2, drugtwo);

					// System.out.println("match: " + currentDrug2 + " | at: "
					// + currentMatch2 + " | len: " + finalHtml.length());

					finalHtml = finalHtml.substring(0, currentMatch2) + "<span style='background-color: #FFFF00'>"
							+ currentDrug2 + "</span>" + finalHtml.substring(currentMatch2 + currentDrug2.length());
				}

				// System.out.println("final html: " + finalHtml);

				e.setInnerHTML(finalHtml);
			}
		} else {

			// System.out.println("DOM.getElementById('exactmatch') is " + e);
		}
	}

	/*
	 * clean sentence span back removes style
	 */
	public void cleanStyleInSpan() {

		String finalHtml = "";
		Element e = DOM.getElementById("exactmatch");

		if (e != null) {
			String html = e.getInnerHTML();

			finalHtml = html.replaceAll("<span style=\"background-color: #FFFF00\">", "").replaceAll("</span>", "");
			// System.out.println("html: " + finalHtml);

			e.setInnerHTML(finalHtml);

		} else {

			// System.out.println("DOM.getElementById('exactmatch') is " + e);
		}
	}

	/*
	 * find which drug should be highlight in drop down list box return he index
	 * of occurrence of drug
	 */
	public int getCurrentDrugMatch(String html, String currentDrug, ListBox drug) {
		int occurrences = 0;

		// occurrences is which drug been selected in list box
		for (int i = 0; i < drug.getSelectedIndex(); i++) {
			if (drug.getItemText(i).equals(currentDrug)) {
				occurrences++;
			}
		}

		// System.out.println("the selected " + currentDrug + " is number: "
		// + occurrences);

		int currentMatch = html.indexOf(currentDrug);
		while (currentMatch >= 0 && occurrences >= 1) {
			// System.out.println(occurrences + "|" + currentMatch);
			currentMatch = html.indexOf(currentDrug, currentMatch + currentDrug.length());
			occurrences--;
		}

		return currentMatch;
	}

	/*
	 * preselect type when drug is defined
	 */

	public void preselectDrugType(ListBox drug, int whichDrug) {

		int selectedIndex = drug.getSelectedIndex();
		if (selectedIndex > 0 && selectedIndex < drug.getItemCount()) {

			String drugExact = drug.getItemText(selectedIndex).toLowerCase();

			if (Mddi.getDrugEntities().containsKey(drugExact)) {
				String drugType = Mddi.getDrugEntities().get(drugExact).getType();
				preselectDrugTypeHelper(drugType, whichDrug);
				// System.out.println("preselect type for drug "+whichDrug +
				// " as " + drugType);
			}
		}

	}

	public void preselectDrugTypeHelper(String drugType, int whichDrug) {

		if (whichDrug == 1) {
			if (drugType.equals("Active Ingredient"))
				typeai1.setValue(true);
			else if (drugType.equals("Drug Product"))
				typedp1.setValue(true);
			else if (drugType.equals("Metabolite"))
				typemb1.setValue(true);
		} else if (whichDrug == 2) {
			if (drugType.equals("Active Ingredient"))
				typeai2.setValue(true);
			else if (drugType.equals("Drug Product"))
				typedp2.setValue(true);
			else if (drugType.equals("Metabolite"))
				typemb2.setValue(true);

		} else
			return;

	}

	/*
	 * required fields
	 */
	public boolean validatesRequiredFields() {
		ArrayList<String> requireds = new ArrayList<String>();

		if (drug1.getSelectedIndex() == 0)
			requireds.add("Drug 1");
		if (drug2.getSelectedIndex() == 0)
			requireds.add("Drug 2");

		if (!(typeai1.getValue() || typemb1.getValue() || typedp1.getValue() || typedg1.getValue())) {
			requireds.add("Drug 1 type");
		}
		if (!(typeai2.getValue() || typemb2.getValue() || typedp2.getValue() || typedg2.getValue())) {
			requireds.add("Drug 2 type");
		}
		if (!(rolepp1.getValue() || roleob1.getValue())) {
			requireds.add("Drug 1 role");
		}
		if (!(rolepp2.getValue() || roleob2.getValue())) {
			requireds.add("Drug 2 role");
		}

		// if (!(statementqu.getValue() || statementql.getValue())) {
		// requireds.add("statement");
		// }

		if (!(modalitypt.getValue() || modalitynt.getValue())) {
			requireds.add("modality");
		}

		if (!(evidenceSpt.getValue() || evidenceAgt.getValue())) {
			requireds.add("Evidence modality");
		}

		// validates AUC fields:
		// numParticipt, objectDose, preciptDose, auc;
		if (assertType.getSelectedIndex() == 1) {
			if (numParticipt.getValue().trim().isEmpty()) {
				requireds.add("Number of participants");
			}
			if (objectDose.getValue().trim().isEmpty()) {
				requireds.add("Object dose");
			}
			if (preciptDose.getValue().trim().isEmpty()) {
				requireds.add("Precipitant dose");
			}

			if (objectDuration.getValue().trim().isEmpty()) {
				requireds.add("object duration");
			}

			if (preciptDuration.getValue().trim().isEmpty()) {
				requireds.add("precipt duration");
			}

			if (auc.getValue().trim().isEmpty()) {
				requireds.add("Auc");
			}

			if (aucDirection.getSelectedIndex() == 0) {
				requireds.add("auc direction");
			}

			if (aucType.getSelectedIndex() == 0) {
				requireds.add("auc type");
			}

			if (cl.getValue().trim().isEmpty()) {
				requireds.add("cl");
			}

			if (clDirection.getSelectedIndex() == 0) {
				requireds.add("cl direction");
			}

			if (clType.getSelectedIndex() == 0) {
				requireds.add("cl type");
			}

			if (objectregimens.getSelectedIndex() == 0) {
				requireds.add("object regimens");
			}
			if (preciptregimens.getSelectedIndex() == 0) {
				requireds.add("precipitant regimens");
			}

			if (cmax.getValue().trim().isEmpty()) {
				requireds.add("Cmax");
			}

			if (cmaxDirection.getSelectedIndex() == 0) {
				requireds.add("cmax direction");
			}

			if (cmaxType.getSelectedIndex() == 0) {
				requireds.add("cmax type");
			}

			if (t12.getValue().trim().isEmpty()) {
				requireds.add("T1/2");
			}

			if (t12Direction.getSelectedIndex() == 0) {
				requireds.add("T1/2 direction");
			}

			if (t12Type.getSelectedIndex() == 0) {
				requireds.add("T1/2 type");
			}
		}

		if (requireds.size() > 0) {

			String message = "";
			for (String str : requireds) {
				message += str + " ,";
			}
			message = message.substring(0, message.length() - 1);

			Window.alert(message + "are required");
			return false;
		}
		return true;
	}

	/*
	 * switch assertion type (drug-drug interaction / increaseAuc) type code:
	 * ddi: 0 , increaseAuc: 1
	 */

	public void switchAssertType() {

		assertType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int type = assertType.getSelectedIndex();
				if (type == 0) {

					System.out.println("create confirm dialog...");

					final DialogBox dialog = new DialogBox();
					final VerticalPanel panel = new VerticalPanel();
					final HorizontalPanel hp1 = new HorizontalPanel();
					final HorizontalPanel hp2 = new HorizontalPanel();

					Label label = new Label(
							"WARNING: Change to drug drug interaction will discard all your inputs in number of participants, "
									+ "object dose, precipitant dose and increase AUC ");
					hp1.add(label);

					dialog.setPopupPosition(Window.getClientWidth() / 2 - 150, Window.getClientHeight() / 2 - 70);
					dialog.setHeight("140px");
					dialog.setWidth("300px");
					dialog.show();

					Button submit = new Button("Confirm");
					submit.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {

							dialog.hide();
							// close clinical trail panel
							rightColumn.setVisible(false);

							// erase clinical trail fields
							numParticipt.setValue("");
							objectDose.setValue("");
							objectFormu.setSelectedIndex(0);
							objectDuration.setValue("");
							preciptDose.setValue("");
							preciptFormu.setSelectedIndex(0);
							preciptDuration.setValue("");
							objectregimens.setSelectedIndex(0);
							preciptregimens.setSelectedIndex(0);
							auc.setValue("");
							aucDirection.setSelectedIndex(0);
							aucType.setSelectedIndex(0);
							cl.setValue("");
							clDirection.setSelectedIndex(0);
							clType.setSelectedIndex(0);
							cmax.setValue("");
							cmaxDirection.setSelectedIndex(0);
							cmaxType.setSelectedIndex(0);
							t12.setValue("");
							t12Direction.setSelectedIndex(0);
							t12Type.setSelectedIndex(0);
						}
					});
					hp2.add(submit);

					Button close = new Button("Cancel");
					close.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							assertType.setSelectedIndex(1);
							dialog.hide();
						}
					});

					DOM.setElementAttribute(hp2.getElement(), "id", "dialog-button");

					hp2.add(close);

					panel.setStylePrimaryName("buttonsPanel");

					panel.add(hp1);
					panel.add(hp2);

					dialog.setWidget(panel);
					dialog.getElement().getStyle().setZIndex(100);
					dialog.show();
					/*
					 * pop up dialog: confirm to discard AUC fields
					 */

				} else {
					rightColumn.setVisible(true);
				}
			}
		});
	}
	


}
