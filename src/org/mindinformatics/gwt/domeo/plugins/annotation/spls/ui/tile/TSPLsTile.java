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
					provenance, "SPLs", _annotation);

			StringBuffer sb2 = new StringBuffer();

			/*
			 * create html
			 */

			// Create the content to display
			MPharmgx pharmgx = _annotation.getPharmgxUsage().getPharmgx();

			String html1 = "<html><head></head> <body><table>";

			String html2 = "</table></body></html>";

			String labels = "<tr bgcolor='#EEE'><td>PK Impact</td><td>PD Impact</td><td>Drug Rec</td>"
					+ "<td>Dose Rec</td><td>Monitoring Rec</td><td>Test</td><td>Statement</td></tr>";

			String statements_str = "";
			int count = 2;
			for (MLinkedResource mr : _annotation.getPharmgxUsage()
					.getStatements()) {
				statements_str += mr.getLabel().toString() + " ";
				if (count <= 0)
					break;
				count++;
			}

			String values = "<tr bgcolor='#F0F8FF'><td>"
					+ pharmgx.getPkImpactResource().getLabel() + "</td>"
					+ "<td>" + pharmgx.getPdImpactResource().getLabel()
					+ "</td><td>" + pharmgx.getDrugRecResource().getLabel()
					+ "</td><td>" + pharmgx.getDoseRecResource().getLabel()
					+ "</td><td>" + pharmgx.getMonitRecResource().getLabel()
					+ "</td><td>" + pharmgx.getTestRecResource().getLabel()
					+ "</td><td>" + statements_str + "</td></tr>";

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
