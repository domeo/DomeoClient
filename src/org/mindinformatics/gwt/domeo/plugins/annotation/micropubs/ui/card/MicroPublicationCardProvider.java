package org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.micropubs.model.MMicroPublicationAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class MicroPublicationCardProvider implements ICardGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public MicroPublicationCardProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isCardSupported(String annotationName) {
		if(annotationName.equals(MMicroPublicationAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ACardComponent getCard(String annotationName) {
		if(isCardSupported(annotationName)) return new CMicroPublicationCard(_domeo);
		return null;
	}
}
