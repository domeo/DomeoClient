package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.form;

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
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Iexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.expertstudy_pDDIFactory;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
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

	public static final String expertstudy_pDDI_POC_PREFIX = "http://purl.org/net/nlprepository/expertstudy_pDDI-annotation-poc#";

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
	ListBox annotationSet;
	@UiField
	VerticalPanel rightColumn;

	@UiField
	Label drugLabel1, drugLabel2;

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

	// role of drug 1 and drug 2

	public MLinkedResource getRoleOfDrug1() {

		if (roleob1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "object-drug-of-interaction",
							"Object drug of Interaction",
							"Referred to the role that each drug one plays within the interaction.");
		} else if (rolepp1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "precipitant-drug-of-interaction",
							"Precipitant drug of Interaction",
							"Referred to the role that each drug one plays within the interaction.");
		}

		return null;
	}

	public MLinkedResource getRoleOfDrug2() {

		if (roleob2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "object-drug-of-interaction",
							"Object drug of Interaction",
							"Referred to the role that each drug two plays within the interaction.");
		} else if (rolepp2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX
							+ "precipitant-drug-of-interaction",
							"Precipitant drug of Interaction",
							"Referred to the role that each drug two plays within the interaction.");
		}
		return null;
	}

	// type of drug 1 and drug 2

	public MLinkedResource getTypeOfDrug1() {

		if (typeai1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient",
							"Active ingredient",
							"Referred to the type of the mention within the sentence for drug one.");
		} else if (typemb1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "metabolite",
							"Metabolite",
							"Referred to the type of the mention within the sentence for drug one.");
		} else if (typedp1.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "drug-product",
							"Drug product",
							"Referred to the type of the mention within the sentence for drug one.");
		}

		return null;
	}

	public MLinkedResource getTypeOfDrug2() {

		if (typeai2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "active-ingredient",
							"Active ingredient",
							"Referred to the type of the mention within the sentence for drug two.");
		} else if (typemb2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "metabolite",
							"Metabolite",
							"Referred to the type of the mention within the sentence for drug two.");
		} else if (typedp2.getValue()) {
			return ResourcesFactory
					.createLinkedResource(DIKBD2R_PREFIX + "drug-product",
							"Drug product",
							"Referred to the type of the mention within the sentence for drug two.");
		}
		return null;
	}

	// statement

	public MLinkedResource getStatement() {

		if (statementqu.getValue()) {
			return ResourcesFactory.createLinkedResource(NCIT_PREFIX
					+ "quantitative", "Quantitative",
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
							"Positive",
							"Referred to extra sources of information that the annotator considers "
									+ "are helpful to provide evidence for or against the existence of the pDDI.");
		} else if (modalitynt.getValue()) {
			return ResourcesFactory
					.createLinkedResource(
							SIO_PREFIX + "negative",
							"Negative",
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

		ButtonWithIcon yesButton = new ButtonWithIcon(Domeo.resources
				.generalCss().applyButton());
		yesButton.setWidth("78px");
		yesButton.setHeight("22px");
		yesButton.setResource(Domeo.resources.acceptLittleIcon());
		yesButton.setText("Apply");
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
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

							System.out.println("selector in new: "
									+ selector.getExact());

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
							//expertstudy_pDDIUsage.setComment(comment.getText());
							
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
			_domeo.getLogger()
					.debug(this,
							"Attempting to initialize UI with loaded annotation values");

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
					"expertstudy_pDDI annotation filter set refreshed");

			currentMpDDI = annotation.getMpDDIUsage().getMpDDI();

			// check selections of drug role
			if (_item.getRole1() != null) {
				if (_item.getRole1().getLabel()
						.equals("Object drug of Interaction")) {
					roleob1.setValue(true);
				} else if (_item.getRole1().getLabel()
						.equals("Precipitant drug of Interaction")) {
					rolepp1.setValue(true);
				}
			}

			if (_item.getRole2() != null) {
				if (_item.getRole2().getLabel()
						.equals("Object drug of Interaction")) {
					roleob2.setValue(true);
				} else if (_item.getRole2().getLabel()
						.equals("Precipitant drug of Interaction")) {
					rolepp2.setValue(true);
				}
			}

			// check selections of drug type
			if (_item.getType1() != null) {
				if (_item.getType1().getLabel().equals("Active ingredient")) {
					typeai1.setValue(true);
				} else if (_item.getType1().getLabel().equals("Metabolite")) {
					typemb1.setValue(true);
				} else if (_item.getType1().getLabel().equals("Drug product")) {
					typedp1.setValue(true);
				}
			}

			if (_item.getType2() != null) {
				if (_item.getType2().getLabel().equals("Active ingredient")) {
					typeai2.setValue(true);
				} else if (_item.getType2().getLabel().equals("Metabolite")) {
					typemb2.setValue(true);
				} else if (_item.getType2().getLabel().equals("Drug product")) {
					typedp2.setValue(true);
				}
			}

			// check selections of statement
			if (_item.getStatement() != null) {
				if (_item.getStatement().getLabel().equals("Quantitative")) {
					statementqu.setValue(true);
				} else if (_item.getStatement().getLabel()
						.equals("Qualitative")) {
					statementql.setValue(true);
				}
			}

			// check selections of modality
			if (_item.getModality() != null) {
				if (_item.getModality().getLabel().equals("Positive")) {
					modalitypt.setValue(true);
				} else if (_item.getModality().getLabel().equals("Negative")) {
					modalitynt.setValue(true);
				}
			}

			if (_item.getComment() != null && !_item.getComment().equals(""))
				comment.setText(_item.getComment());
			
			
			_domeo.getLogger()
					.debug(this,
							"acquired the expertstudy_pDDI usage from the annotation instance passed to this method");
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
		// if(_item.getSioDescriptions().size()!=getMethods().size()) return
		// true;
		// TODO check items
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

	// @Override
	// public void addexpertstudy_pDDI(Mexpertstudy_pDDI expertstudy_pDDI) {
	// addAssociatedAntibody(expertstudy_pDDI);
	// }
}
