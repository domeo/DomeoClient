package org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class PostItCardProvider implements ICardGenerator {

	// By contract 
	private IDomeo _domeo;
	
	public PostItCardProvider(IDomeo domeo) {
		_domeo = domeo;
	}
	
	@Override
	public boolean isCardSupported(String annotationName) {
		if(annotationName.equals(MPostItAnnotation.class.getName())) return true;
		return false;
	}

	@Override
	public ACardComponent getCard(String annotationName) {
		if(isCardSupported(annotationName)) return new CPostItCard(_domeo);
		return null;
	}
}
