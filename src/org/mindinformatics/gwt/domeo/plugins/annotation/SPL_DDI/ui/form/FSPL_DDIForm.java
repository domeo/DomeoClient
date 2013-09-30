package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.ui.form;

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
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.SPL_DDIFactory;
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
public class FSPL_DDIForm extends AFormComponent implements IResizable {

	private static Logger logger = Logger.getLogger("");

	public static final String LABEL = "SPL_DDI Annotation";
	public static final String LABEL_EDIT = "EDIT SPL_DDI ANNOTATION";

	public static final String LOG_CATEGORY_QUALIFIER_CREATE = "CREATING SPL_DDI ANNOTATION";
	public static final String LOG_CATEGORY_QUALIFIER_EDIT = "EDITING SPL_DDI ANNOTATION";

	public static final String SPL_DDI_POC_PREFIX = "http://purl.org/net/nlprepository/SPL_DDI-pharmgx-annotation-poc#";

	interface Binder extends UiBinder<VerticalPanel, FSPL_DDIForm> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private MSPL_DDIAnnotation _item;
	private MSPL_DDI currentSPL_DDI;
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

	
	// NEW annotation
	public FSPL_DDIForm(IDomeo domeo, final AFormsManager manager) {
		super(domeo);
		_manager = manager;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this, "SPL_DDI annotation widget bound to UI");

		refreshAnnotationSetFilter(annotationSet, null);

		_domeo.getLogger().debug(this, "SPL_DDI annotation filter set refreshed");

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
						"SPL_DDI annotation content validated");

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
							MSPL_DDIAnnotation annotation = SPL_DDIFactory
									.createSPL_DDIAnnotation(
											((AnnotationPersistenceManager) _domeo
													.getPersistenceManager())
													.getCurrentSet(), _domeo
													.getAgentManager()
													.getUserPerson(), _domeo
													.getAgentManager()
													.getSoftware(), _manager
													.getResource(), selector);

							_domeo.getLogger().debug(this,
									"SPL_DDI annotation factory initialized");

							MSPL_DDIUsage SPL_DDIUsage = SPL_DDIFactory
									.createSPL_DDIUsage();

							
							
							
							annotation.setSPL_DDIUsage(SPL_DDIUsage);

							

							annotation.setComment(commentBody.getText());
							_domeo.getLogger().debug(this, "SPL_DDI comment set");

							if (getSelectedSet(annotationSet) == null) {
								_domeo.getLogger()
										.debug(this,
												"empty annotation set, passing first SPL_DDI annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation, true);
							} else {
								_domeo.getLogger()
										.debug(this,
												"Annotation set is not empty, passing new SPL_DDI annotation to persistance manager");
								_domeo.getAnnotationPersistenceManager()
										.addAnnotation(annotation,
												getSelectedSet(annotationSet));
							}

							_domeo.getLogger()
									.debug(this,
											"Making the new SPL_DDI annotation visible in the content panel");
							_domeo.getContentPanel()
									.getAnnotationFrameWrapper()
									.performAnnotation(
											annotation,
											((TextAnnotationFormsPanel) _manager)
													.getHighlight());

							_domeo.getLogger()
									.debug(this,
											"SPL_DDI annotation data collected. widget should close now.");

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
	public FSPL_DDIForm(IDomeo domeo, final AFormsManager manager,
			final MSPL_DDIAnnotation annotation) {
		super(domeo);
		_manager = manager;
		_item = annotation;

		initWidget(binder.createAndBindUi(this));

		_domeo.getLogger().debug(this,
				"SPL_DDI annotation widget bound to UI (edit)");

		try {
			_domeo.getLogger()
					.debug(this,
							"Attempting to initialize UI with loaded annotation values");

			if (_item.getComment() != null && !_item.getComment().equals(""))
				commentBody.setText(_item.getComment());

			
			

		} catch (Exception e) {
			_domeo.getLogger().exception(
					this,
					"Failed to diSPL_DDIay current annotation "
							+ annotation.getLocalId());
			diSPL_DDIayDialog(
					"Failed to properly diSPL_DDIay existing annotation "
							+ e.getMessage(), true);
		}

		try {
			refreshAnnotationSetFilter(annotationSet, annotation);

			_domeo.getLogger().debug(this,
					"SPL_DDI annotation filter set refreshed");

			currentPharmgx = annotation.getPharmgxUsage().getPharmgx();

			_domeo.getLogger()
					.debug(this,
							"acquired the pharmgx usage from the annotation instance passed to this method");
		} catch (Exception e) {
			_domeo.getLogger().exception(
					AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
					this,
					"Failed to diSPL_DDIay current annotation "
							+ annotation.getLocalId());
			diSPL_DDIayDialog(
					"Failed to properly diSPL_DDIay existing annotation "
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
							"SPL_DDI annotation content validated (edit)");

					_item.setComment(commentBody.getText());

			

					_domeo.getLogger().debug(this,
							"SPL_DDI descriptions cleared and re-loaded (edit)");

					_item.getSPL_DDIUsage().setSPL_DDI(currentSPL_DDI);

					_domeo.getContentPanel()
							.getAnnotationFrameWrapper()
							.updateAnnotation(_item,
									getSelectedSet(annotationSet));

					_domeo.getLogger()
							.debug(this,
									"SPL_DDI annotation data collected. widget should close now.");
					_manager.hideContainer();
				} catch (Exception e) {
					_domeo.getLogger()
							.exception(
									AnnotationFrameWrapper.LOG_CATEGORY_EDIT_ANNOTATION,
									this,
									"Failed to apply modified anntoation "
											+ annotation.getLocalId());
					diSPL_DDIayDialog(
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
		// _manager.diSPL_DDIayMessage("Click received!");
		// if(currentPharmgx==null) {
		// _manager.diSPL_DDIayMessage("The body of the annotation cannot be empty!");
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
