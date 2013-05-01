package org.mindinformatics.gwt.domeo.plugins.resource.pubmedcentral.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.model.MAnnotationCitationReference;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class ReferenceCardProvider implements ICardGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public ReferenceCardProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isCardSupported(String annotationName) {
		if(annotationName.equals(MAnnotationCitationReference.class.getName())) return true;
		return false;
	}

	@Override
	public ACardComponent getCard(String annotationName) {
		if(isCardSupported(annotationName)) return new CReferenceCard(_domeo);
		return null;
	}
}
