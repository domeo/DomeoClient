package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.card;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.info.AntibodyPlugin;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CAntibodyCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CAntibodyCard> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private int _index = -1;
	private MAntibodyAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField Label type;
	@UiField Label text;
	
	public CAntibodyCard(IDomeo domeo) {
		super(domeo);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeCard(int index, CurationPopup curationPopup, MAnnotation annotation) {
		_index = index;
		initializeCard(curationPopup, annotation);
	}
	
	@Override
	public void initializeCard(CurationPopup curationPopup, MAnnotation annotation) {
		_annotation = (MAntibodyAnnotation) annotation;
		_curationPopup = curationPopup;
		refresh();
	}

	@Override
	public Widget getCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refresh() {
		try {
			if (_index>-1) createProvenanceBar(AntibodyPlugin.getInstance().getPluginName(), _index, provenance, _annotation);
			else createProvenanceBar(AntibodyPlugin.getInstance().getPluginName(), provenance, _annotation);
			type.setText("Antibody:");
			text.setText(_annotation.getAntibodyUsage().getAntibody().getLabel());
			
			if(SelectorUtils.isOnMultipleTargets(_annotation.getSelectors())) {
				HorizontalPanel hp = new HorizontalPanel();
				hp.add(new HTML("Targets:&nbsp;"));
				for(MSelector sel: _annotation.getSelectors()) {
					SimpleIconButtonPanel bu = new SimpleIconButtonPanel(_domeo, ActionShowAnnotation.getClickHandler(_domeo, 
							_annotation.getLocalId()+":"+sel.getLocalId()),
							Domeo.resources.showLittleIcon().getSafeUri().asString(), "Show target in context");
					hp.add(bu);		
				}
				content.add(hp);
			}
			
			injectButtons(AntibodyPlugin.getInstance().getPluginName(), content, _annotation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
	
	@Override
    public void setSpan(Element element) {
        _span = element;
    }
}
