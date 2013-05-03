package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;
import org.mindinformatics.gwt.domeo.plugins.resource.pubmed.lenses.PubMedCitationPainter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Provides the standard lens for the annotation type Highlight.
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class CReferenceCard extends ACardComponent {

	interface Binder extends UiBinder<Widget, CReferenceCard> { }
	private static final Binder binder = GWT.create(Binder.class);
	
	// By contract 
	private int _index = -1;
	private MAnnotationCitationReference _annotation;
	
	@UiField VerticalPanel body;
	@UiField HorizontalPanel provenance;
	@UiField FlowPanel content;
	
	public CReferenceCard(IDomeo domeo) {
		super(domeo);
		
		initWidget(binder.createAndBindUi(this));
		
		tileResources.css().ensureInjected();
	}
	
	public MAnnotation getAnnotation() {
		return _annotation;
	}
	
	@Override
	public void initializeCard(int index, CurationPopup curationPopup,
			MAnnotation annotation) {
		_index = index;
		initializeCard(curationPopup, annotation);
	}
	
	@Override
	public void initializeCard(CurationPopup curationPopup, MAnnotation annotation) {
		_annotation = (MAnnotationCitationReference) annotation;
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
			if (_index>-1) createProvenanceBar("", _index, provenance, _annotation);
			else createProvenanceBar("", provenance, _annotation);
			content.add(PubMedCitationPainter.getCitationAnnotationWithIds(_annotation, _domeo));
			injectButtons("", content, _annotation);
		} catch(Exception e) {
			_domeo.getLogger().exception(this, e.getMessage());
		}
	}
	
	@Override
    public void setSpan(Element element) {
        _span = element;
    }
}
