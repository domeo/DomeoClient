package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.info.expertstudy_pDDIPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDI;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;
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
public class Texpertstudy_pDDITile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, Texpertstudy_pDDITile> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	private static int MAX_STATEMENT = 65;
	// By contract
	private Mexpertstudy_pDDIAnnotation _annotation;

	@UiField
	VerticalPanel body;
	@UiField
	HorizontalPanel provenance;
	// @UiField FlowPanel content;
	// @UiField HTML icon;
	@UiField
	HTML description;

	public Texpertstudy_pDDITile(IDomeo domeo, IAnnotationEditListener listener) {
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
			_annotation = (Mexpertstudy_pDDIAnnotation) annotation;
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
			createProvenanceBar(expertstudy_pDDIPlugin.getInstance().getPluginName(),
					provenance, "expertstudy_pDDI", _annotation);

			StringBuffer sb2 = new StringBuffer();

			/*
			 * create html
			 */

			// Create the content to display
			Mexpertstudy_pDDI expertstudy_pDDI = _annotation.getexpertstudy_pDDIUsage().getexpertstudy_pDDI();

			String html1 = "<html><head></head> <body><table>";

			String html2 = "</table></body></html>";

			sb2.append(html1);






			sb2.append(html2);
			System.out.println(sb2.toString());

			description.setHTML(sb2.toString());

		} catch (Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}

}
