package org.mindinformatics.gwt.domeo.plugins.annotation.ddi.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card.CPostItCard;
import org.mindinformatics.gwt.domeo.plugins.annotation.ddi.model.MddiAnnotation;

public class ddiCardProvider implements ICardGenerator{
	// By contract 
		private IDomeo _domeo;
		
		public ddiCardProvider(IDomeo domeo) {
			_domeo = domeo;
		}
		
		@Override
		public boolean isCardSupported(String annotationName) {
			if(annotationName.equals(MddiAnnotation.class.getName())) return true;
			return false;
		}

		@Override
		public ACardComponent getCard(String annotationName) {
			if(isCardSupported(annotationName)) return new CddiCard(_domeo);
			return null;
		}
}
