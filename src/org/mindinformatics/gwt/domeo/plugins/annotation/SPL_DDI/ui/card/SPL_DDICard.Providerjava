package org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card.CPostItCard;
import org.mindinformatics.gwt.domeo.plugins.annotation.SPL_DDI.model.MSPL_DDIAnnotation;

public class SPL_DDICardProvider implements ICardGenerator{
	// By contract 
		private IDomeo _domeo;
		
		public SPL_DDICardProvider(IDomeo domeo) {
			_domeo = domeo;
		}
		
		@Override
		public boolean isCardSupported(String annotationName) {
			if(annotationName.equals(MSPL_DDIAnnotation.class.getName())) return true;
			return false;
		}

		@Override
		public ACardComponent getCard(String annotationName) {
			if(isCardSupported(annotationName)) return new CSPL_DDICard(_domeo);
			return null;
		}
}
