package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.tile;

import java.util.Set;

import org.mindinformatics.gwt.domeo.client.Domeo;
import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.ActionShowAnnotation;
import org.mindinformatics.gwt.domeo.client.ui.annotation.actions.IAnnotationEditListener;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ATileComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.tiles.ITileComponent;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.selectors.MSelector;
import org.mindinformatics.gwt.domeo.model.selectors.SelectorUtils;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;
import org.mindinformatics.gwt.framework.component.resources.model.MLinkedResource;
import org.mindinformatics.gwt.framework.component.ui.buttons.SimpleIconButtonPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard tile for the annotation type Antibody.
 * Tiles are the lenses display as popup on top of the annotation.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class TAntibodyTile extends ATileComponent implements ITileComponent {

	interface Binder extends UiBinder<Widget, TAntibodyTile> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private MAntibodyAnnotation _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	@UiField Label type;
	@UiField Label text;
	@UiField HTML method;
	@UiField HTML subject;
	@UiField HTML link;
	@UiField HTML note;
	
	public TAntibodyTile(IDomeo domeo, IAnnotationEditListener listener) {
		super(domeo, listener);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeLens(MAnnotation annotation) {
		_annotation = (MAntibodyAnnotation) annotation;
		refresh();
	}	
	@Override
	public Widget getTile() {
		return this;
	}
	@Override
	public void refresh() {
		try {
			createProvenanceBar(provenance, _annotation);

			type.setText("Antibody:");
			text.setText(_annotation.getAntibodyUsage().getAntibody().getLabel());
			
			int counter = 0;
			StringBuffer protocolsText = new StringBuffer();
			Set<MLinkedResource> protocols =  _annotation.getAntibodyUsage().getProtocols();
			for(MLinkedResource protocol: protocols) {
				protocolsText.append(protocol.getLabel());
				if(counter<protocols.size()-1) protocolsText.append(", ");
				counter++;
			}
			
			if(protocolsText.toString().trim().length()>0) 
				method.setHTML("<b>Method:</b> " + protocolsText.toString());
			if(_annotation.getAntibodyUsage().getSubject()!=null) 
				subject.setHTML("<b>Subject:</b> " + _annotation.getAntibodyUsage().getSubject().getLabel());
			if(_annotation.getAntibodyUsage().getComment().trim().length()>0) 
				note.setHTML("<b>Comment:</b>" + _annotation.getAntibodyUsage().getComment().trim());
			link.setHTML(" <a target='_blank' href='" + _annotation.getAntibodyUsage().getAntibody().getUrl() + "'>(@AntibodyRegistry)</a>" );
			text.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Window.alert("Edit or display?");
				}
			});
			
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
			
			injectButtons(content, _annotation);
			
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
}
