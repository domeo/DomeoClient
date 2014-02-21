package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.card;

import java.util.ArrayList;
import java.util.Iterator;

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
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLPharmgxUsage;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InsertPanel.ForIsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CSPLsCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CSPLsCard> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private int _index = -1;
	private MSPLsAnnotation _annotation;
	String statements_str = "", prevalence_str = "";
	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	@UiField
	FlowPanel content;
	// recommendation drug, recommendation dose, recommendation monitoring, test
	// section statements
	@UiField
	Label type, text, pkimpact, pdimpact, recdrug, recdose, recmonitoring,
			testrec, statement, biomarkers, allelesbody, medconditbody, test,
			variant, drugOfInterest, commentbody;

	@UiField
	Image wrongIcon, rightIcon;

	public CSPLsCard(IDomeo domeo) {
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

		try {
			if (_index > -1)
				createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
						_index, provenance, _annotation);
			else
				createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
						provenance, _annotation);

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
					System.out.println("event process.....");

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
					dialog.setText("The reason of disagree:");
					int left = wrongIcon.getAbsoluteLeft();
					int top = wrongIcon.getAbsoluteTop();
					dialog.setPopupPosition(left, top + 10);
					dialog.setHeight("150px");
					dialog.setWidth("200px");
					dialog.show();

				}
			});

			System.out.println("Images are loaded in UI");

			type.setText("SPLs Annotation:");

			text.setText("clinical statements from SPLs");

			/*
			 * can not skip some labels
			 */

			if (_annotation != null) {
				MSPLPharmgxUsage dataUsage = _annotation.getPharmgxUsage();

				if (dataUsage.getDrugOfInterest() != null) {
					String drugIStr = dataUsage.getDrugOfInterest().getLabel();
					drugOfInterest.setText(drugIStr);
				} else {
					drugOfInterest.setText("undefined");
				}

				if (dataUsage.getBiomarkers() != null) {
					String bioStr = dataUsage.getBiomarkers().getLabel();
					biomarkers.setText(bioStr);
				} else {
					biomarkers.setText("undefined");
				}

				if (dataUsage.getPkImpact() != null) {

					String pkimpactStr = dataUsage.getPkImpact().getLabel();
					pkimpact.setText(pkimpactStr);
				} else {
					pkimpact.setText("undefined");
				}

				if (dataUsage.getPdImpact() != null) {

					String pdimpactStr = dataUsage.getPdImpact().getLabel();
					pdimpact.setText(pdimpactStr);
				} else {
					pdimpact.setText("undefined");
				}

				if (dataUsage.getDrugRec() != null) {
					String drugRecStr = dataUsage.getDrugRec().getLabel();
					recdrug.setText(drugRecStr);
				} else {
					recdrug.setText("undefined");
				}

				if (dataUsage.getDoseRec() != null) {
					String doseRecStr = dataUsage.getDoseRec().getLabel();
					recdose.setText(doseRecStr);
				} else {
					recdose.setText("undefined");
				}

				if (dataUsage.getMonitRec() != null) {
					String monRecStr = dataUsage.getMonitRec().getLabel();
					recmonitoring.setText(monRecStr);
				} else {
					recmonitoring.setText("undefined");
				}

				if (dataUsage.getTestRec() != null) {
					String testRec = dataUsage.getTestRec().getLabel();
					testrec.setText(testRec);
				} else {
					testrec.setText("undefined");
				}

				if (dataUsage.getStatements() != null) {
					int count = 2;

					System.out.println(_annotation.getPharmgxUsage()
							.getStatements().size());
					for (MLinkedResource mr : _annotation.getPharmgxUsage()
							.getStatements()) {
						statements_str += mr.getLabel().toString() + " ";

						System.out.println(count + "|" + statements_str);

						if (count <= 0)
							break;
						count++;
					}
					statement.setText(statements_str);
				} else {
					statement.setText("undefined");
				}

				// Variant
				
				if (dataUsage.getVariant() != null&& !dataUsage.getVariant().getLabel().equals("")) {
					String variantStr = dataUsage.getVariant().getLabel();
					variant.setText(variantStr);
				} else {
					variant.setText("undefined");
				}
				

/*				if ((dataUsage.getVariantbody() != null && !dataUsage
						.getVariantbody().trim().equals(""))
						|| dataUsage.getVariant() != null) {

					if (dataUsage.getVariantbody() != null
							&& !dataUsage.getVariantbody().trim().equals("")) {
						variant.setText(dataUsage.getVariantbody());

					} else if (!dataUsage.getVariant().getLabel()
							.equals("unselected")) {
						variant.setText(dataUsage.getVariant().getLabel());
					}
				} else
					variant.setText("undefined");*/

				// test
				if (dataUsage.getTest() != null&& !dataUsage.getTest().getLabel().equals("")) {
					String testStr = dataUsage.getTest().getLabel();
					test.setText(testStr);
				} else {
					test.setText("undefined");
				}

/*				if ((dataUsage.getTestbody() != null && !dataUsage
						.getTestbody().trim().equals(""))
						|| dataUsage.getTest() != null) {

					if (dataUsage.getTestbody() != null
							&& !dataUsage.getTestbody().trim().equals("")) {
						test.setText(dataUsage.getTestbody());

					} else if (!dataUsage.getTest().getLabel()
							.equals("unselected")) {
						test.setText(dataUsage.getTest().getLabel());
					}
				} else
					test.setText("undefined");*/
				

				if (dataUsage.getAllelesbody() != null) {
					String allelesStr = dataUsage.getAllelesbody();
					allelesbody.setText(allelesStr);
				} else {
					allelesbody.setText("");
				}

				if (dataUsage.getMedconditbody() != null) {
					String medconditStr = dataUsage.getMedconditbody();
					medconditbody.setText(medconditStr);
				} else {
					medconditbody.setText("");
				}
				
				if (dataUsage.getComment() != null) {
					String commentStr = dataUsage.getComment();
					commentbody.setText(commentStr);
				} else {
					commentbody.setText("");
				}
			}

			System.out.println("load values of labels");

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
