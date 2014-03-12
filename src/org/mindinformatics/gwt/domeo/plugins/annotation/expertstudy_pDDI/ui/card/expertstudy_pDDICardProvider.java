package org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.ui.card;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ACardComponent;
import org.mindinformatics.gwt.domeo.client.ui.annotation.cards.ICardGenerator;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.model.MPostItAnnotation;
import org.mindinformatics.gwt.domeo.plugins.annotation.postit.ui.card.CPostItCard;
import org.mindinformatics.gwt.domeo.plugins.annotation.expertstudy_pDDI.model.Mexpertstudy_pDDIAnnotation;

public class expertstudy_pDDICardProvider implements ICardGenerator{
	// By contract 
		private IDomeo _domeo;
		
		public expertstudy_pDDICardProvider(IDomeo domeo) {
			_domeo = domeo;
		}
		
		@Override
		public boolean isCardSupported(String annotationName) {
			if(annotationName.equals(Mexpertstudy_pDDIAnnotation.class.getName())) return true;
			return false;
		}

		@Override
		public ACardComponent getCard(String annotationName) {
			if(isCardSupported(annotationName)) return new Cexpertstudy_pDDICard(_domeo);
			return null;
		}
}
