package org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.nif.antibodies.model.MAntibodyAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AntibodyCardProvider implements ICardGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public AntibodyCardProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isCardSupported(String annotationName) {
		if(annotationName.equals(MAntibodyAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ACardComponent getCard(String annotationName) {
		if(isCardSupported(annotationName)) return new CAntibodyCard(_domeo);
		return null;
	}
}
