package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import org.mindinformatics.gwt.domeo.model.persistence.AnnotationPersistenceManager;
import org.mindinformatics.gwt.domeo.model.selectors.MTextQuoteSelector;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.SPLsFactory;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class FSPLsForm extends AFormComponent implements IResizable {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "SPL Annotation";
	public static final String LABEL_EDIT = "EDIT SPL ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING SPL ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING SPL ANNOTATION";

	public static final String SPL_POC_PREFIX = "http://purl.org/net/nlprepository/spl-pharmgx-annotation-poc#";

	interface Binder extends UiBinder<VerticalPanel, FSPLsForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private MSPLsAnnotation _item;
	private MPharmgx currentPharmgx;
	private ArrayList<Widget> tabs = new ArrayList<Widget>();

	@UiField
	VerticalPanel container;
	@UiField
	FlowPanel newQualifiers;
	@UiField
	HorizontalPanel buttonsPanel;
	@UiField
	ListBox annotationSet;
	@UiField
	VerticalPanel rightColumn;
	@UiField
	TabBar tabBar;

	// PK Impact

	@UiField
	RadioButton descriptpkdm;
	@UiField
	RadioButton descriptpkim;
	@UiField
	RadioButton descriptpkia;
	@UiField
	RadioButton descriptpkda;
	@UiField
	RadioButton descriptpkid;
	@UiField
	RadioButton descriptpkdd;
	@UiField
	RadioButton descriptpkie;
	@UiField
	RadioButton descriptpkde;
	@UiField
	RadioButton descriptpkni;

	// PD Impact

	@UiField
	RadioButton descriptpddt;
	@UiField
	RadioButton descriptpdit;
	@UiField
	RadioButton descriptpdir;
	@UiField
	RadioButton descriptpdni;
	@UiField
	RadioButton descriptpdie;
	@UiField
	RadioButton descriptpdde;

	// recommendation drug
	@UiField
	CheckBox descriptdsca;
	@UiField
	CheckBox descriptdsal;
	@UiField
	CheckBox descriptdsam;
	@UiField
	CheckBox descriptdsnr;
	@UiField
	CheckBox descriptdsnc;

	// recommendation dose
	@UiField
	CheckBox descriptdrdfb;
	@UiField
	CheckBox descriptdrifb;
	@UiField
	CheckBox descriptdrnc;
	@UiField
	CheckBox descriptdrus;
	@UiField
	CheckBox descriptdrcs;

	// recommendation monitoring
	@UiField
	CheckBox descriptmreq;
	@UiField
	CheckBox descriptmrec;
	@UiField
	CheckBox descriptmnc;
	@UiField
	CheckBox descriptmcms;

	// test section
	@UiField
	CheckBox descripttreq;
	@UiField
	CheckBox descripttrec;
	@UiField
	CheckBox descriptttna;
	@UiField
	CheckBox descripttnn;

	// statements
	@UiField
	CheckBox descriptsai;
	@UiField
	CheckBox descriptsmcc;
	@UiField
	CheckBox descriptsvt;
	@UiField
	CheckBox descriptsmc;
	@UiField
	CheckBox descriptsts;
	@UiField
	CheckBox descriptsbm;

	// @UiField RadioButton Drug, Dose, Monitoring, test_Re;

	@UiField
	TextArea commentBody;

	// Paolo this is taking care of the 'PK impact' for 'apply'
	// I Normally create a method for each group
	// RadioButton groups return MLinkedResource
	// CheckBoxes groups return Set<MLinkedResource>
	// See FAntibodyForm as an example

	public MLinkedResource getPkImpact() {
		if (descriptpkia.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "absorption-increase",
							"Absorption Increase",
							"The pharmacogenomic biomarker is associated with a increase in absorption of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkda.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "absorption-decrease",
							"Absorption Decrease",
							"The pharmacogenomic biomarker is associated with an decrease in absorption of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkid.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "distribution-increase",
							"Distribution Increase",
							"The pharmacogenomic biomarker is associated with a increase in distribution of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkdd.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "distribution-decrease",
							"Distribution Decrease",
							"The pharmacogenomic biomarker is associated with an decrease in distribution of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkim.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "metabolism-increase",
							"Metabolism Increase",
							"The pharmacogenomic biomarker is associated with an increase in metabolism of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkdm.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "metabolism-decrease",
							"Metabolism Decrease",
							"The pharmacogenomic biomarker is associated with a decrease in metabolism of the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkie.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "excretion-increase",
							"Excretion Increase",
							"*************************** this was missing ************************",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkde.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "excretion-decrease",
							"Excretion Decrease",
							"*************************** this was missing ************************",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpkni.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "not-important",
							"Not Important",
							"The pharmacogenomic biomarker is not associated any clinically relevant pharmacokinetic with respect to the drug.",
							SPL_POC_PREFIX + "PharmacokineticImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// Pharmacodynamic impact PD

	public MLinkedResource getPdImpact() {

		if (descriptpddt.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "drug-toxicity-risk-decreased",
							"Decreased Toxicity Risk",
							"The pharmacogenomic biomarker is associated with an decreased risk of toxicity.",
							SPL_POC_PREFIX + "PharmacodynamicImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpdit.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "drug-toxicity-risk-increased",
							"Increased Toxicity Risk",
							"The pharmacogenomic biomarker is associated with an increased risk of toxicity.",
							SPL_POC_PREFIX + "PharmacodynamicImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpdir.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(SPL_POC_PREFIX
					+ "influences-drug-response", "Influences Drug Response",
					"The pharmacogenomic biomarker influences drug response",
					SPL_POC_PREFIX + "PharmacodynamicImpact", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpdni.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "not-important",
							"Not Important",
							"The pharmacogenomic biomarker is not associated with clinically relevant pharmacodynamic effect",
							SPL_POC_PREFIX + "PharmacodynamicImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpdie.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "drug-efficacy-increased-from-baseline",
							"Increased Efficacy",
							"The pharmacogenomic biomarker is associated with an increase in the efficacy of the drug. ",
							SPL_POC_PREFIX + "PharmacodynamicImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptpdde.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "drug-efficacy-decreased-from-baseline",
							"Decreased Efficacy",
							"The pharmacogenomic biomarker is associated with an decrease in the efficacy of the drug.",
							SPL_POC_PREFIX + "PharmacodynamicImpact",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// Recommendation drug

	// recommendation drug

	public MLinkedResource getDrugRec() {

		if (descriptdsal.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "alternative-recommended",
							"Alternative Recommended",
							"The pharmacogenomic biomarker is related to a recommendation to use an alternative drug.",
							SPL_POC_PREFIX + "DrugSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdsnr.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "do-not-restart",
							"Do not restart",
							"The pharmacogenomic biomarker is related to a recommendation to not restart the drug",
							SPL_POC_PREFIX + "DrugSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdsnc.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "no-change-necessary",
							"Not change necessary",
							"The pharmacogenomic biomarker is not associated with any drug selection recommendation.",
							SPL_POC_PREFIX + "DrugSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdsam.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "addition-of-medication",
							"Addition of medication",
							"The pharmacogenomic biomarker is related to a recommendation to add a concomitant medication.",
							SPL_POC_PREFIX + "DrugSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdsca.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "change-in-route-of-admin",
							"Change in route of administration",
							"The pharmacogenomic biomarker is related to a recommendation to add change the route of administration for the drug.",
							SPL_POC_PREFIX + "DrugSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// Recommendation Dose

	public MLinkedResource getDoseRec() {

		if (descriptdrdfb.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "decrease-from-recommended-baseline",
							"Decrease from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to decrease the dose of the drug from the recommended baseline.",
							SPL_POC_PREFIX + "DoseSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdrifb.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "increase-from-recommended-baseline",
							"Increase from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to increase the dose of the drug from the recommended baseline.",
							SPL_POC_PREFIX + "DoseSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdrnc.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "not-change-from-recommended-baseline",
							"Not change from baseline",
							"The pharmacogenomic biomarker is related to a recommendation to not change the dose of the drug from the recommended baseline.",
							SPL_POC_PREFIX + "DoseSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdrus.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "use-specific",
							"Use specific",
							"The pharmacogenomic biomarker is related to a recommendation to use specific dose of the drug from the recommended baseline.",
							SPL_POC_PREFIX + "DoseSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptdrcs.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "change-schedule",
							"Change schedule",
							"The pharmacogenomic biomarker is related to a recommendation to change schedule of the dose of the drug from the recommended baseline.",
							SPL_POC_PREFIX + "DoseSelectionRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// Recommendation Monitoring

	public MLinkedResource getMonitRec() {

		if (descriptmreq.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "required",
							"Required",
							"A required monitoring recommendation is related to the pharmacogenomic biomarker.",
							SPL_POC_PREFIX + "MonitoringRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptmrec.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "recommended",
							"Recommended",
							"A recommended monitoring recommendation is related to the pharmacogenomic biomarker.",
							SPL_POC_PREFIX + "MonitoringRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptmnc.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "not-necessary",
							"Not necessary",
							"A not necessary monitoring recommendation is related to the pharmacogenomic biomarker.",
							SPL_POC_PREFIX + "MonitoringRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		} else if (descriptmcms.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "change-monitoring-strategy",
							"Change monitoring strategy",
							"A strategy changed monitoring recommendation is related to the pharmacogenomic biomarker.",
							SPL_POC_PREFIX + "MonitoringRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// test section

	public MLinkedResource getTest_Re() {
		if (descripttreq.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(SPL_POC_PREFIX
					+ "required", "Required",
					"A required test is related to the biomarker.",
					SPL_POC_PREFIX + "TestRecommendation", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation");
		} else if (descripttrec.getValue()) {
			return ResourcesFactory.createTrustedTypedResource(SPL_POC_PREFIX
					+ "recommend", "Recommend",
					"A recommended test is related to the biomarker.",
					SPL_POC_PREFIX + "TestRecommendation", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation");
		}

		else if (descriptttna.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX
									+ "take-note-that-tests-are-avaliable",
							"Take note that tests are avaliable",
							"Testing related to the pharmacogenomic biomarker is avaliable.",
							SPL_POC_PREFIX + "TestRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}

		else if (descripttnn.getValue()) {
			return ResourcesFactory
					.createTrustedTypedResource(
							SPL_POC_PREFIX + "not-necessary",
							"Not necessary",
							"Testing related to the pharmacogenomic biomarker is not necessary.",
							SPL_POC_PREFIX + "TestRecommendation",
							SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation");
		}
		return null;
	}

	// statements
	// the value of each variables is made up
	public Set<MLinkedResource> getStatements() {

		Set<MLinkedResource> statements = new HashSet<MLinkedResource>();

		if (descriptsai.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "ingredient-active", "Active ingredient",
					"the ingredient is active", SPL_POC_PREFIX + "Statements",
					SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation"));

		}

		if (descriptsmcc.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "concomitant-medication-concern",
					"Concomitant medication concern",
					"we should concern the concomitant medication.",
					SPL_POC_PREFIX + "Statements", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation"));
		}

		if (descriptsvt.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "variant", "Variant",
					"it is highly possible changes with time.", SPL_POC_PREFIX
							+ "Statements", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation"));
		}

		if (descriptsmc.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "medical-condition", "Medical Condition",
					"it is should be references to medical condition.",
					SPL_POC_PREFIX + "Statements", SPL_POC_PREFIX,
					"U of Pitt SPL Pharmgx Annotation"));

		}

		if (descriptsts.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "test", "Test",
					"based on the test result.", SPL_POC_PREFIX + "Statements",
					SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation"));
		}

		if (descriptsbm.getValue()) {
			statements.add(ResourcesFactory.createTrustedTypedResource(
					SPL_POC_PREFIX + "biomarker", "Biomarker",
					"Based on biomarker.", SPL_POC_PREFIX + "Statements",
					SPL_POC_PREFIX, "U of Pitt SPL Pharmgx Annotation"));

		}
		return statements;
	}

	// NEW annotation
	public FSPLsForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);
		_manager = manager;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this, "SPL annotation widget bound to UI");

		refreshAnnotationSetFilter(annotationSet, null);

		_domeo.getLogger().debug(this, "SPL annotation filter set refreshed");

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources
				.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isContentInvalid())
					return; // TODO: use this function to validate form elements
							// in the UI

				_domeo.getLogger().debug(this,
						"SPL annotation content validated");

				try {
					if (_item == null) {
						_domeo.getLogger().debug(this, "_item is null...");
						if (_manager instanceof TextAnnotationFormsPanel) {
							MTextQuoteSelector selector = AnnotationFactory
									.createPrefixSuffixTextSelector(
											_domeo.getAgentManager()
													.getUserPerson(),
											_domeo.getPersistenceManager()
													.getCurrentResource(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getExact(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getPrefix(),
											((TextAnnotationFormsPanel) _manager)
													.getHighlight().getSuffix());

							_domeo.getLogger().debug(this,
									"quote selector initialized");

							System.out.println("selector in new: "
									+ selector.getExact());

							// TODO Register coordinate of the selection.
							MSPLsAnnotation annotation = SPLsFactory
									.createSPLsAnnotation(
											((AnnotationPersistenceManager) _domeo
													.getPersistenceManager())
													.getCurrentSet(), _domeo
													.getAgentManager()
													.getUserPerson(), _domeo
													.getAgentManager()
													.getSoftware(), _manager
													.getResource(), selector);

							_domeo.getLogger().debug(this,
									"SPL annotation factory initialized");

							MSPLPharmgxUsage pharmgxUsage = SPLsFactory
									.createSPLPharmgxUsage();

							// take the form values and assign
							_domeo.getLogger().debug(this, "SPL annotation 1");
							pharmgxUsage.setPkImpact(getPkImpact());
							_domeo.getLogger().debug(this, "SPL annotation 2");
							pharmgxUsage.setPdImpact(getPdImpact());
							_domeo.getLogger().debug(this, "SPL annotation 3");
							pharmgxUsage.setDrugRec(getDrugRec());
							_domeo.getLogger().debug(this, "SPL annotation 4");
							pharmgxUsage.setDoseRec(getDoseRec());
							_domeo.getLogger().debug(this, "SPL annotation 5");
							pharmgxUsage.setMonitRec(getMonitRec());
							_domeo.getLogger().debug(this, "SPL annotation 6");
							pharmgxUsage.setStatements(getStatements());
							_domeo.getLogger().debug(this, "SPL annotation 7");
							pharmgxUsage.setTestRec(getTest_Re());

							annotation.setPharmgxUsage(pharmgxUsage);

							_domeo.getLogger()
									.debug(this,
											"SPL pharmgxUsage initialized and attached to the SPL annotation instance");

							// _domeo.getLogger().command(getLogCategoryCreate(),
							// this, " with term " + currentPharmgx.getLabel());

							annotation.setComment(commentBody.getText());
							_domeo.getLogger().debug(this, "SPL comment set");

							if (getSelectedSet(annotationSet) == null) {
								_domeo.getLogger()
										.debug(this,
												"empty annotation set, passing first SPL annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation, true);
							} else {
								_domeo.getLogger()
										.debug(this,
												"Annotation set is not empty, passing new SPL annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation,
												getSelectedSet(annotationSet));
							}

							_domeo.getLogger()
									.debug(this,
											"Making the new SPL annotation visible in the content panel");
							_domeo.getContentPanel()
									.getAnnotationFrameWrapper()
									.performAnnotation(
											annotation,
											((TextAnnotationFormsPanel) _manager)
													.getHighlight());

							_domeo.getLogger()
									.debug(this,
											"SPL annotation data collected. widget should close now.");

							_manager.hideContainer();

						}
					} else {
						// _item.setType(PostitType.findByName(postitTypes.getItemText(postitTypes.getSelectedIndex())));
						// _item.setText(getPostItBody());
						_domeo.getLogger().debug(this, "_item is NOT null...");

						_manager.hideContainer();
					}
				} catch (Exception e) {
					logger.log(Level.WARNING,
							"throwing exception " + e.getMessage());
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
	public FSPLsForm(IDomeo domeo, final AFormsManager manager,
			final MSPLsAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this,
				"SPL annotation widget bound to UI (edit)");

		try {
			_domeo.getLogger()
					.debug(this,
							"Attempting to initialize UI with loaded annotation values");

			if (_item.getComment() != null && !_item.getComment().equals(""))
				commentBody.setText(_item.getComment());

			if (_item.getPKImpact() != null) {
				if (_item.getPKImpact().getLabel()
						.equals("Metabolism Decrease"))
					descriptpkdm.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Metabolism Increase"))
					descriptpkim.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Absorption Increase"))
					descriptpkia.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Absorption Decrease"))
					descriptpkda.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Distribution Increase"))
					descriptpkid.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Distribution Decrease"))
					descriptpkdd.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Excretion Increase"))
					descriptpkie.setValue(true);
				else if (_item.getPKImpact().getLabel()
						.equals("Excretion Decrease"))
					descriptpkde.setValue(true);
				else if (_item.getPKImpact().getLabel().equals("Not Important"))
					descriptpkni.setValue(true);
			}

			if (_item.getPdImpact() != null) {
				if (_item.getPdImpact().getLabel()
						.equals("Decreased Toxicity Risk"))
					descriptpddt.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Increased Toxicity Risk"))
					descriptpdit.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Influences Drug Response"))
					descriptpdir.setValue(true);
				else if (_item.getPdImpact().getLabel().equals("Not Important"))
					descriptpdni.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Increased Efficacy"))
					descriptpdie.setValue(true);
				else if (_item.getPdImpact().getLabel()
						.equals("Decreased Efficacy"))
					descriptpdde.setValue(true);
			}

			if (_item.getDoseRec() != null) {
				if (_item.getDoseRec().getLabel()
						.equals("Decrease from baseline"))
					descriptdrdfb.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Increase from baseline"))
					descriptdrifb.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Not change from baseline"))
					descriptdrnc.setValue(true);
				else if (_item.getDoseRec().getLabel().equals("Use specific"))
					descriptdrus.setValue(true);
				else if (_item.getDoseRec().getLabel()
						.equals("Change schedule"))
					descriptdrcs.setValue(true);
			}

			if (_item.getMonitRec() != null) {
				if (_item.getMonitRec().getLabel().equals("Required"))
					descriptmreq.setValue(true);
				else if (_item.getMonitRec().getLabel().equals("Recommended"))
					descriptmrec.setValue(true);
				else if (_item.getMonitRec().getLabel().equals("Not necessary"))
					descriptmnc.setValue(true);
				else if (_item.getMonitRec().getLabel()
						.equals("Change monitoring strategy"))
					descriptmcms.setValue(true);
			}

			if (_item.getDrugRec() != null) {

				if (_item.getDrugRec().getLabel()
						.equals("Alternative Recommended"))
					descriptdsal.setValue(true);
				else if (_item.getDrugRec().getLabel()
						.equals("Change in route of administration"))
					descriptdsca.setValue(true);
				else if (_item.getDrugRec().getLabel().equals("Add medication"))
					descriptdsam.setValue(true);
				else if (_item.getDrugRec().getLabel().equals("Do not restart"))
					descriptdsnr.setValue(true);
				else if (_item.getDrugRec().getLabel()
						.equals("Not change necessary"))
					descriptdsnc.setValue(true);
			}

			if (_item.getTestRec() != null) {
				System.out
						.println("drug rec :" + _item.getTestRec().getLabel());
				if (_item.getTestRec().getLabel().equals("Required"))
					descripttreq.setValue(true);
				else if (_item.getTestRec().getLabel().equals("Recommend"))
					descripttrec.setValue(true);
				else if (_item.getTestRec().getLabel()
						.equals("Take note that tests are avaliable"))
					descriptttna.setValue(true);
				else if (_item.getTestRec().getLabel().equals("Not necessary"))
					descripttnn.setValue(true);

			}

			// statements, values are made up
			// checkbox

			if (_item.getStatements() != null) {
				for (MLinkedResource statemt : _item.getStatements()) {

					if (statemt.getLabel().equals("Active ingredient")) {
						descriptsai.setValue(true);
					}
					if (statemt.getLabel().equals(
							"Concomitant medication concern")) {
						descriptsmcc.setValue(true);
					}
					if (statemt.getLabel().equals("Variant")) {
						descriptsvt.setValue(true);
					}
					if (statemt.getLabel().equals("Medical condition")) {
						descriptsmc.setValue(true);
					}
					if (statemt.getLabel().equals("Test")) {
						descriptsts.setValue(true);
					}
					if (statemt.getLabel().equals("Biomarker")) {
						descriptsbm.setValue(true);
					}

				}

			}

		} catch (Exception e) {
			_domeo.getLogger().exception(
					this,
					"Failed to display current annotation "
							+ annotation.getLocalId());
			displayDialog(
					"Failed to properly display existing annotation "
							+ e.getMessage(), true);
		}

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);

			_domeo.getLogger().debug(this,
					"SPL annotation filter set refreshed");

			currentPharmgx = annotation.getPharmgxUsage().getPharmgx();

			_domeo.getLogger()
					.debug(this,
							"acquired the pharmgx usage from the annotation instance passed to this method");
		} catch (Exception e) {
			_domeo.getLogger().exception(
					AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
					this,
					"Failed to display current annotation "
							+ annotation.getLocalId());
			displayDialog(
					"Failed to properly display existing annotation "
							+ e.getMessage(), true);
		}

		ButtonWithIcon sameVersionButton = new ButtonWithIcon();
		sameVersionButton.setStyleName(Domeo.resources.generalCss()
				.applyButton());
		sameVersionButton.setWidth("78px");
		sameVersionButton.setHeight("22px");
		sameVersionButton.setResource(Domeo.resources.acceptLittleIcon());
		sameVersionButton.setText("Apply");
		sameVersionButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				System.out.println("onClick function triggered");
				try {
					if (isContentInvalid()) {
						return; // TODO: modify this function to validate our UI

					}

					MTextQuoteSelector selector = AnnotationFactory
							.createPrefixSuffixTextSelector(_domeo
									.getAgentManager().getUserPerson(), _domeo
									.getPersistenceManager()
									.getCurrentResource(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getExact(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getPrefix(),
									((TextAnnotationFormsPanel) _manager)
											.getHighlight().getSuffix());

					System.out.println("selector in edit: "
							+ selector.getExact());
					
					
					_item.setSelector(selector);

					_domeo.getLogger().debug(this,
							"SPL annotation content validated (edit)");

					_item.setComment(commentBody.getText());

					_item.setPKImpact(getPkImpact());
					_item.setPdImpact(getPdImpact());

					_item.setDrugRec(getDrugRec());
					_item.setDoseRec(getDoseRec());
					_item.setMonitRec(getMonitRec());
					_item.setStatements(getStatements());
					_item.setTestRec(getTest_Re());

					_domeo.getLogger().debug(this,
							"SPL descriptions cleared and re-loaded (edit)");

					_item.getPharmgxUsage().setPharmgx(currentPharmgx);

					_domeo.getContentPanel()
							.getAnnotationFrameWrapper()
							.updateAnnotation(_item,
									getSelectedSet(annotationSet));

					_domeo.getLogger()
							.debug(this,
									"SPL annotation data collected. widget should close now.");
					_manager.hideContainer();
				} catch (Exception e) {
					_domeo.getLogger()
							.exception(
									AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
									this,
									"Failed to apply modified anntoation "
											+ annotation.getLocalId());
					displayDialog(
							"Failed to apply modified annotation "
									+ e.getMessage(), true);
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
		// _manager.displayMessage("Click received!");
		// if(currentPharmgx==null) {
		// _manager.displayMessage("The body of the annotation cannot be empty!");
		// Timer timer = new Timer() {
		// public void run() {
		// _manager.clearMessage();
		// }
		// };
		// timer.schedule(2000);
		// return true;
		// }
		return false;
	}

	public boolean areMethodsChanged() {
		// if(_item.getSioDescriptions().size()!=getMethods().size()) return
		// true;
		// TODO check items
		return false;
	}

	@Override
	public void resized() {
		this.setWidth((Window.getClientWidth() - 340) + "px");
		tabBar.setWidth((Window.getClientWidth() - 615) + "px");
		for (Widget tab : tabs) {
			// if(tab instanceof IResizable) ((IResizable)tab).resized();
			tab.setWidth((Window.getClientWidth() - 615) + "px");
		}
	}

	@Override
	public boolean isContentChanged(MAnnotation annotation) {
		// TODO Auto-generated method stub
		return false;
	}

	// @Override
	// public void addPharmgx(MPharmgx pharmgx) {
	// addAssociatedAntibody(pharmgx);
	// }
}
