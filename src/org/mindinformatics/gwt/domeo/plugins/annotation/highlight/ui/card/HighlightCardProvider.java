package org.mindinformatics.gwt.domeo.plugins.annotation.highlight.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.highlight.model.MHighlightAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class HighlightCardProvider implements ICardGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public HighlightCardProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isCardSupported(String annotationName) {
		if(annotationName.equals(MHighlightAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ACardComponent getCard(String annotationName) {
		if(isCardSupported(annotationName)) return new CHighlightCard(_domeo);
		return null;
	}
}
