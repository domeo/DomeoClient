package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Iexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.expertstudy_pDDIFactory;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.resources.model.ResourcesFactory;
import org.mindinformatics.gwt.framework.src.IResizable;
import org.mindinformatics.gwt.framework.widget.ButtonWithIcon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Richard Boyce <rdb20@pitt.edu>
 */
public class Fexpertstudy_pDDIForm extends AFormComponent implements
		IResizable, Iexpertstudy_pDDI {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "DDI";
	public static final String LABEL_EDIT = "EDIT expertstudy_pDDI ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING expertstudy_pDDI ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING expertstudy_pDDI ANNOTATION";

	// public static final String expertstudy_pDDI_POC_PREFIX =
	// "http://purl.org/net/nlprepository/expertstudy_pDDI-annotation-poc#";

	interface Binder extends UiBinder<VerticalPanel, Fexpertstudy_pDDIForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private Mexpertstudy_pDDIAnnotation _item;
	private Mexpertstudy_pDDI currentMpDDI;
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
	RadioButton typeai1, typemb1, typedp1;

	// type of drug2
	@UiField
	RadioButton typeai2, typemb2, typedp2;

	// role of drug1
	@UiField
	RadioButton roleob1, rolepp1;

	// role of drug1
	@UiField
	RadioButton roleob2, rolepp2;

	// statement
	@UiField
	RadioButton statementqu, statementql;

	// modality
	@UiField
	RadioButton modalitypt, modalitynt;
	@UiField
	TextArea comment;

	// drug 1 and drug 2

	public MLinkedResource getDrug1() {

		int indexdrug1 = drug1.getSelectedIndex();
		String drug1Uri = "";
		if (indexdrug1 != 0) {
			String drug1Str = drug1.getItemText(indexdrug1);

			if (Mexpertstudy_pDDI.getDrugEntities().containsKey(drug1Str)) {
				drug1Uri = "http://purl.bioontology.org/ontology/RXNORM/"
						+ Mexpertstudy_pDDI.getDrugEntities().get(drug1Str)
								.getRxcui();
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

			if (Mexpertstudy_pDDI.getDrugEntities().containsKey(drug2Str)) {
				drug2Uri = Mexpertstudy_pDDI.getDrugEntities().get(drug2Str)
						.getRxcui();
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
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "object-drug-of-interaction",
							"object-drug-of-interaction",
							"Referred to the role that each drug one plays within the interaction.");
		} else if (rolepp1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "precipitant-drug-of-interaction",
							"precipitant-drug-of-interaction",
							"Referred to the role that each drug one plays within the interaction.");
		}

		return null;
	}

	public MLinkedResource getRoleOfDrug2() {

		if (roleob2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "object-drug-of-interaction",
							"object-drug-of-interaction",
							"Referred to the role that each drug two plays within the interaction.");
		} else if (rolepp2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "precipitant-drug-of-interaction",
							"precipitant-drug-of-interaction",
							"Referred to the role that each drug two plays within the interaction.");
		}
		return null;
	}

	// type of drug 1 and drug 2

	public MLinkedResource getTypeOfDrug1() {

		if (typeai1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient",
							"active-ingredient",
							"Referred to the type of the mention within the sentence for drug.");
		} else if (typemb1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "metabolite",
							"metabolite",
							"Referred to the type of the mention within the sentence for drug.");
		} else if (typedp1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "drug-product",
							"drug-product",
							"Referred to the type of the mention within the sentence for drug.");
		}

		return null;
	}

	public MLinkedResource getTypeOfDrug2() {

		if (typeai2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient",
							"active-ingredient",
							"Referred to the type of the mention within the sentence for drug two.");
		} else if (typemb2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "metabolite",
							"metabolite",
							"Referred to the type of the mention within the sentence for drug two.");
		} else if (typedp2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "drug-product",
							"drug-product",
							"Referred to the type of the mention within the sentence for drug two.");
		}
		return null;
	}

	// statement

	public MLinkedResource getStatement() {

		if (statementqu.getValue()) {
			return ResourcesFactory.createLinkedResource(NCIT_PREFIX
					+ "quantitative", "quantitative",
					"Referred to data type that is described in the sentence.");
		} else if (statementql.getValue()) {
			return ResourcesFactory.createLinkedResource(NCIT_PREFIX
					+ "qualitative", "Qualitative",
					"Referred to data type that is described in the sentence.");
		}

		return null;
	}

	// modality

	public MLinkedResource getModality() {

		if (modalitypt.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SIO_PREFIX + "positive",
							"positive",
							"Referred to extra sources of information that the annotator considers "
									+ "are helpful to provide evidence for or against the existence of the pDDI.");
		} else if (modalitynt.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SIO_PREFIX + "negative",
							"negative",
							"Referred to extra sources of information that the annotator considers "
									+ "are helpful to provide evidence for or against the existence of the pDDI.");
		}

		return null;
	}

	// NEW annotation
	public Fexpertstudy_pDDIForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);

		_manager = manager;

		initWidget(binder.createAndBindUi(this));

		refreshAnnotationSetFilter(annotationSet, null);

		// get NER in selected text and add them into drugs (drop down list box)

		HashMap<Integer, String> drugs = getNERToDrugs();
		if (drugs != null && drugs.size() != 0) {
			for (int i = 0; i < drugs.size(); i++) {
				drug1.addItem(drugs.get(i));
				drug2.addItem(drugs.get(i));
			}

		}

		// automatically select another role when user chosen one of role
		autoSelectAnotherRole();

		// highlight drug
		highlightCurrentDrug();

		// preselect type

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources
				.generalCss().applyButton());
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

				_domeo.getLogger().debug(this,
						"expertstudy_pDDI annotation content validated");

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

							// TODO Register coordinate of the selection.
							Mexpertstudy_pDDIAnnotation annotation = expertstudy_pDDIFactory
									.createexpertstudy_pDDIAnnotation(
											((AnnotationPersistenceManager) _domeo
													.getPersistenceManager())
													.getCurrentSet(), _domeo
													.getAgentManager()
													.getUserPerson(), _domeo
													.getAgentManager()
													.getSoftware(), _manager
													.getResource(), selector);

							_domeo.getLogger()
									.debug(this,
											"expertstudy_pDDI annotation factory initialized");

							Mexpertstudy_pDDIUsage expertstudy_pDDIUsage = expertstudy_pDDIFactory
									.createexpertstudy_pDDIUsage();

							expertstudy_pDDIUsage.setRole1(getRoleOfDrug1());
							_domeo.getLogger().debug(this, "DDI annotation 1");
							expertstudy_pDDIUsage.setRole2(getRoleOfDrug2());
							_domeo.getLogger().debug(this, "DDI annotation 2");
							expertstudy_pDDIUsage.setType1(getTypeOfDrug1());
							_domeo.getLogger().debug(this, "DDI annotation 3");
							expertstudy_pDDIUsage.setType2(getTypeOfDrug2());
							_domeo.getLogger().debug(this, "DDI annotation 4");
							expertstudy_pDDIUsage.setStatement(getStatement());
							_domeo.getLogger().debug(this, "DDI annotation 5");
							expertstudy_pDDIUsage.setModality(getModality());
							_domeo.getLogger().debug(this, "DDI annotation 6");
							expertstudy_pDDIUsage.setDrug1(getDrug1());
							_domeo.getLogger().debug(this, "DDI annotation 7");
							expertstudy_pDDIUsage.setDrug2(getDrug2());
							_domeo.getLogger().debug(this, "DDI annotation 8");

							annotation.setMpDDIUsage(expertstudy_pDDIUsage);
							annotation.setComment(comment.getText());

							_domeo.getLogger().debug(this, "annotation loaded");

							if (getSelectedSet(annotationSet) == null) {
								_domeo.getLogger()
										.debug(this,
												"empty annotation set, passing first expertstudy_pDDI annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation, true);
							} else {
								_domeo.getLogger()
										.debug(this,
												"Annotation set is not empty, passing new expertstudy_pDDI annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation,
												getSelectedSet(annotationSet));
							}

							_domeo.getContentPanel()
									.getAnnotationFrameWrapper()
									.performAnnotation(
											annotation,
											((TextAnnotationFormsPanel) _manager)
													.getHighlight());

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
	public Fexpertstudy_pDDIForm(IDomeo domeo, final AFormsManager manager,
			final Mexpertstudy_pDDIAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this,
				"expertstudy_pDDI annotation widget bound to UI (edit)");

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);

			_domeo.getLogger().debug(this,
					"expertstudy_pDDI annotation filter set refreshed");

			currentMpDDI = annotation.getMpDDIUsage().getMpDDI();

			// check selections of drug role
			if (_item.getRole1() != null) {
				if (_item.getRole1().getLabel()
						.equals("object-drug-of-interaction")) {
					roleob1.setValue(true);
				} else if (_item.getRole1().getLabel()
						.equals("precipitant-drug-of-interaction")) {
					rolepp1.setValue(true);
				}
			}

			if (_item.getRole2() != null) {
				if (_item.getRole2().getLabel()
						.equals("object-drug-of-interaction")) {
					roleob2.setValue(true);
				} else if (_item.getRole2().getLabel()
						.equals("precipitant-drug-of-interaction")) {
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
				}
			}

			if (_item.getType2() != null) {
				if (_item.getType2().getLabel().equals("active-ingredient")) {
					typeai2.setValue(true);
				} else if (_item.getType2().getLabel().equals("metabolite")) {
					typemb2.setValue(true);
				} else if (_item.getType2().getLabel().equals("drug-product")) {
					typedp2.setValue(true);
				}
			}

			// check selections of statement
			if (_item.getStatement() != null) {
				if (_item.getStatement().getLabel().equals("quantitative")) {
					statementqu.setValue(true);
				} else if (_item.getStatement().getLabel()
						.equals("Qualitative")) {
					statementql.setValue(true);
				}
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

			HashMap<Integer, String> drugs = getNERToDrugs();
			if (drugs != null && drugs.size() != 0) {
				for (int i = 0; i < drugs.size(); i++) {

					System.out.println("add " + drugs.get(i));
					drug1.addItem(drugs.get(i));
					drug2.addItem(drugs.get(i));
				}
			}

			if (_item.getDrug1() != null) {

				String strdrug1 = _item.getDrug1().getLabel();

				// drug1.addItem(strdrug1);

				// System.out.println("drugname1:"+strdrug1);
				for (int i = 0; i < drugs.size(); i++) {

					// System.out.println("drug in list:"+drugs.get(i));
					if (drugs.get(i).equals(strdrug1))
						drug1.setSelectedIndex(i + 1);
				}
			}

			if (_item.getDrug2() != null) {

				String strdrug2 = _item.getDrug2().getLabel();

				// drug2.addItem(strdrug2);

				for (int i = 0; i < drugs.size(); i++) {
					if (drugs.get(i).equals(strdrug2))
						drug2.setSelectedIndex(i + 1);
				}
			}

			// comment

			if (_item.getComment() != null && !_item.getComment().equals(""))
				comment.setText(_item.getComment());

			// highlight drug
			highlightCurrentDrug();

			// automatically select another role when user chosen one of role
			autoSelectAnotherRole();

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

					// validates required fields
					if (!validatesRequiredFields())
						return;

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

					_item.setDrug1(getDrug1());
					_item.setDrug2(getDrug2());
					_item.setSelector(selector);
					_item.setRole1(getRoleOfDrug1());
					_item.setRole2(getRoleOfDrug2());
					_item.setType1(getTypeOfDrug1());
					_item.setType2(getTypeOfDrug2());
					_item.setStatement(getStatement());
					_item.setModality(getModality());
					_item.setComment(comment.getText());

					_item.getMpDDIUsage().setMpDDI(currentMpDDI);

					_domeo.getContentPanel()
							.getAnnotationFrameWrapper()
							.updateAnnotation(_item,
									getSelectedSet(annotationSet));

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
	 * TODO: add drug names from highlight into drop down list box
	 */
	public HashMap<Integer, String> getNERToDrugs() {

		ArrayList<MAnnotation> annotations = _domeo.getPersistenceManager()
				.getAllAnnotations();

		HashMap<Integer, String> ner_set = new HashMap<Integer, String>();
		HashMap<String, DrugInText> drugs = new HashMap<String, DrugInText>();

		// get expert study selected text
		HighlightedTextBuffer eselector = ((TextAnnotationFormsPanel) _manager)
				.getHighlight();
		String sentence = eselector.getExact();

		System.out.println("sentence:" + sentence);

		// filter ao:highlight
		if (annotations.size() != 0 && annotations != null) {
			int index = 0;
			for (MAnnotation ann : annotations) {

				if (ann.getAnnotationType().equals("ao:Highlight")) {

					MHighlightAnnotation highlight = (MHighlightAnnotation) ann;

					MTextQuoteSelector hselector = (MTextQuoteSelector) highlight
							.getSelector();

					String drug = hselector.getExact();

					if (sentence.contains(drug)) {

						if (!drugs.containsKey(drug)) {

							int num = countOcurrencesInStr(drug, sentence, 0);
							// System.out.println("numbers of " + drug + " is "
							// + num);
							// System.out.println("add drug: " + drug);
							ner_set.put(index++, drug);
							drugs.put(drug, new DrugInText(drug, num - 1));

						} else {

							int n = drugs.get(drug).getNum();
							if (n > 0) {
								// System.out.println("add drug: " + drug);
								ner_set.put(index++, drug);
								drugs.get(drug).setNum(n--);
							}
						}

					}

				}
			}

		}
		return ner_set;
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
			num = countOcurrencesInStr(s, str.substring(index + s.length()),
					num);
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
				rolepp2.setChecked(true);
			}
		});

		rolepp1.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				roleob2.setChecked(true);
			}
		});

		rolepp2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				roleob1.setChecked(true);
			}
		});

		roleob2.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				rolepp1.setChecked(true);
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

		String finalHtml = "";
		Element e = DOM.getElementById("exactmatch");
		String html = e.getInnerHTML();

		finalHtml = html.replaceAll(
				"<span style=\"background-color: #FFFF00\">", "").replaceAll(
				"</span>", "");
		// System.out.println("html: " + finalHtml);

		int selected1 = drugone.getSelectedIndex();
		String currentDrug1 = drugone.getItemText(selected1);

		if (finalHtml.contains(currentDrug1) && currentDrug1 != null
				&& !currentDrug1.trim().equals("")) {

			int currentMatch1 = getCurrentDrugMatch(finalHtml, currentDrug1,
					drugone);

			// System.out.println("match: " + currentDrug1 + " | at: " +
			// currentMatch1);

			finalHtml = finalHtml.substring(0, currentMatch1)
					+ "<span style=\"background-color: #FFFF00\">"
					+ currentDrug1
					+ "</span>"
					+ finalHtml
							.substring(currentMatch1 + currentDrug1.length());

			int selected2 = drugtwo.getSelectedIndex();
			String currentDrug2 = drugtwo.getItemText(selected2);

			// System.out.println("post html: " + finalHtml);

			if ((currentDrug2 != null && !currentDrug2.trim().equals(""))
					&& finalHtml.contains(currentDrug2)) {

				int currentMatch2 = getCurrentDrugMatch(finalHtml,
						currentDrug2, drugtwo);

				// System.out.println("match: " + currentDrug2 + " | at: " +
				// currentMatch2 +" | len: " + finalHtml.length());

				finalHtml = finalHtml.substring(0, currentMatch2)
						+ "<span style='background-color: #FFFF00'>"
						+ currentDrug2
						+ "</span>"
						+ finalHtml.substring(currentMatch2
								+ currentDrug2.length());
			}

			// System.out.println("final html: " + finalHtml);

			e.setInnerHTML(finalHtml);
		}
	}

	public int getCurrentDrugMatch(String html, String currentDrug, ListBox drug) {
		int occurences = 0;

		for (int i = 0; i <= drug.getSelectedIndex(); i++) {
			if (drug.getItemText(i).equals(currentDrug)) {
				occurences++;
			}
		}

		int currentMatch = html.indexOf(currentDrug);

		while (currentMatch >= 0 && occurences > 1) {

			// System.out.println(occurences + "|" + currentMatch);

			currentMatch = html.indexOf(currentDrug,
					currentMatch + currentDrug.length());
			occurences--;

		}

		return currentMatch;
	}

	/*
	 * preselect type when drug is defined
	 */

	public void preselectDrugType(ListBox drug, int whichDrug) {

		int selectedIndex = drug.getSelectedIndex();
		if (selectedIndex > 0 && selectedIndex < drug.getItemCount()) {

			String drugExact = drug.getItemText(selectedIndex);

			if (Mexpertstudy_pDDI.getDrugEntities().containsKey(drugExact)) {
				String drugType = Mexpertstudy_pDDI.getDrugEntities()
						.get(drugExact).getType();
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
			else
				typemb1.setValue(true);
		} else if (whichDrug == 2) {
			if (drugType.equals("Active Ingredient"))
				typeai2.setValue(true);
			else if (drugType.equals("Drug Product"))
				typedp2.setValue(true);
			else
				typemb2.setValue(true);

		} else
			return;

	}

	/*
	 * drug 1, drug 2 are required fields
	 */
	public boolean validatesRequiredFields() {
		ArrayList<String> requireds = new ArrayList<String>();

		if (drug1.getSelectedIndex() == 0)
			requireds.add("drug 1");
		if (drug2.getSelectedIndex() == 0)
			requireds.add("drug 2");
		if (!(typeai1.getValue() || typemb1.getValue() || typedp1.getValue())) {
			requireds.add("drug 1 type");
		}
		if (!(typeai2.getValue() || typemb2.getValue() || typedp2.getValue())) {
			requireds.add("drug 2 type");
		}
		if (!(rolepp1.getValue() || roleob1.getValue())) {
			requireds.add("drug 1 role");
		}
		if (!(rolepp2.getValue() || roleob2.getValue())) {
			requireds.add("drug 2 role");
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
}
