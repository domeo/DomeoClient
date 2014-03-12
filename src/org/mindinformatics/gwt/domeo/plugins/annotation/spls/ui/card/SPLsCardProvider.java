package org.mindinformatics.gwt.domeo.plugins.annotation.spls.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card.CPostItCard;
import org.mindinformatics.gwt.domeo.plugins.annotation.spls.model.MSPLsAnnotation;

public class SPLsCardProvider implements ICardGenerator{
	// By contract 
		private IDomeo _domeo;
		
		public SPLsCardProvider(IDomeo domeo) {
			_domeo = domeo;
		}
		
		@Override
		public boolean isCardSupported(String annotationName) {
			if(annotationName.equals(MSPLsAnnotation.class.getName())) return true;
			return false;
		}

		@Override
		public ACardComponent getCard(String annotationName) {
			if(isCardSupported(annotationName)) return new CSPLsCard(_domeo);
			return null;
		}
}
