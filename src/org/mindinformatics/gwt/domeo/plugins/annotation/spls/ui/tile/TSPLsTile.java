package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.tile;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.info.SPLsPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TSPLsTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TSPLsTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private MSPLsAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	//@UiField FlowPanel content;
	//@UiField HTML icon;
	@UiField HTML description;
	
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
		} catch(Exception e) {
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
			createProvenanceBar(SPLsPlugin.getInstance().getPluginName(), provenance, "SPLs", _annotation);
			
			StringBuffer sb2 = new StringBuffer();
			
			// Paolo Here all the content to display
			
			sb2.append("PkImpact: ");
			sb2.append(_annotation.getPharmgxUsage().getPharmgx().getPkImpact());
			
			description.setHTML(sb2.toString());

			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
}
