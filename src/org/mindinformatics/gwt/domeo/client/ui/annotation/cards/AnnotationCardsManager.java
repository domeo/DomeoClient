package org.mindinformatics.gwt.domeo.client.ui.annotation.cards;

import java.util.HashMap;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationCardsManager {

	private HashMap<String, ICardGenerator> 
		annotationCards = new HashMap<String, ICardGenerator>();
	
	public boolean registerAnnotationCard(String annotationName, ICardGenerator cardGenerator) {
		if(!annotationCards.containsKey(annotationName)) {
			annotationCards.put(annotationName, cardGenerator);
			return true;
		}
		return false;
	}
	
	public ACardComponent getAnnotationCard(String annotationName) {
		if(annotationCards.containsKey(annotationName)) {
			return annotationCards.get(annotationName).getCard(annotationName);
		} else
			return null; //TODO define a default card
	}
}
