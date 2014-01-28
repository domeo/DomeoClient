package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.info.SPLsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MPharmgx;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TSPLsTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TSPLsTile> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	private static int MAX_STATEMENT = 65;
	// By contract
	private MSPLsAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	// @UiField FlowPanel content;
	// @UiField HTML icon;
	@UiField
	HTML description;

	public TSPLsTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);

		initWidget(binder.createAndBindUi(this));

		tileResources.css().ensureInjected();
	}

	public MAnnotation getAnnotation() {
		return _annotation;
	}

	@Override
	public void initializeLens(MAnnotation annotation) {
		try {
			_annotation = (MSPLsAnnotation) annotation;
			refresh();
		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Widget getTile() {
		return this;
	}

	@Override
	public void refresh() {
		try {
			createProvenanceBar(SPLsPlugin.getInstance().getPluginName(),
					provenance, "SPLs Annotation", _annotation);

			StringBuffer sb2 = new StringBuffer();

			/*
			 * create html
			 */

			// Create the content to display
			MPharmgx pharmgx = _annotation.getPharmgxUsage().getPharmgx();

			String html1 = "<html><head></head> <body><table>";

			String html2 = "</table></body></html>";

			String labels = "<tr bgcolor='#EEE'><td>Biomarker</td><td>PK Impact</td><td>PD Impact</td><td>Drug Rec</td>"
					+ "<td>Dose Rec</td><td>Monitoring Rec</td><td>Test Rec</td><td>Statement</td><td>Varient</td>"
					+ "<td>Test</td><td>Alleles</td><td>Medical Condition</td></tr>";

			String biomarkerStr;
			if (pharmgx.getBiomarkers() != null) {
				biomarkerStr = pharmgx.getBiomarkers().getLabel();
			} else {
				biomarkerStr = "undefined";
			}

			String pkimpactStr;
			if (pharmgx.getPkImpactResource() != null) {
				pkimpactStr = pharmgx.getPkImpactResource().getLabel();
			} else {
				pkimpactStr = "undefined";
			}

			String pdimpackStr;
			if (pharmgx.getPdImpactResource() != null) {
				pdimpackStr = pharmgx.getPdImpactResource().getLabel();
			} else {
				pdimpackStr = "undefined";
			}

			String drugRecStr;
			if (pharmgx.getDrugRecResource() != null) {
				drugRecStr = pharmgx.getDrugRecResource().getLabel();
			} else {
				drugRecStr = "undefined";
			}

			String doseRecStr;
			if (pharmgx.getDoseRecResource() != null) {
				doseRecStr = pharmgx.getDoseRecResource().getLabel();
			} else {
				doseRecStr = "undefined";
			}

			String monRecStr;
			if (pharmgx.getMonitRecResource() != null) {
				monRecStr = pharmgx.getMonitRecResource().getLabel();
			} else {
				monRecStr = "undefined";
			}

			String testRecStr;
			if (pharmgx.getTestRecResource() != null) {
				testRecStr = pharmgx.getTestRecResource().getLabel();
			} else {
				testRecStr = "undefined";
			}

			String varientStr;
			if (pharmgx.getVarient() != null) {
				varientStr = pharmgx.getVarient().getLabel();
			} else {
				varientStr = "undefined";
			}

			String testStr;
			if (pharmgx.getTest() != null) {
				testStr = pharmgx.getTest().getLabel();
			} else {
				testStr = "undefined";
			}

			String allelesStr;
			if (pharmgx.getAlleles() != null) {
				allelesStr = pharmgx.getAlleles().getLabel();
			} else {
				allelesStr = "undefined";
			}

			String medicalStr;
			if (pharmgx.getMedicalCondition() != null) {
				medicalStr = pharmgx.getMedicalCondition().getLabel();
			} else {
				medicalStr = "undefined";
			}

			String statementsStr = "";
			if (_annotation.getPharmgxUsage().getStatements() != null) {
				int count = 2;
				for (MLinkedResource mr : _annotation.getPharmgxUsage()
						.getStatements()) {
					statementsStr += mr.getLabel().toString() + " ";
					if (count <= 0)
						break;
					count++;
				}
			} else {
				statementsStr = "undefined";
			}

			String values = "<tr bgcolor='#F0F8FF'><td>" + biomarkerStr
					+ "</td><td>" + pkimpactStr + "</td>" + "<td>"
					+ pdimpackStr + "</td><td>" + drugRecStr + "</td><td>"
					+ doseRecStr + "</td><td>" + monRecStr + "</td><td>"
					+ testRecStr + "</td><td>" + statementsStr
					+ "</td><td>" + varientStr + "</td><td>" + testStr
					+ "</td><td>" + allelesStr + "</td><td>" + medicalStr
					+ "</td></tr>";

			sb2.append(html1);

			sb2.append(labels);
			sb2.append(values);

			sb2.append(html2);
			System.out.println(sb2.toString());

			description.setHTML(sb2.toString());

		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

	// generate each statements for variables
	private static String addRecInHTML(String title, String label) {
		// System.out.println("****" + title + " | " + label);
		String str = "<tr bgcolor='#DEDEDE'><td>clinical statements from SPLs</td></tr>";
		str += "<tr><td>" + title;
		if (label.length() >= MAX_STATEMENT)
			label = label.substring(0, MAX_STATEMENT);
		str += label + "</td></tr>";
		return str;
	}
}
