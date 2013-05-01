package org.mindinformatics.gwt.domeo.client.ui.annotation.cards;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ICardGenerator {

	public boolean isCardSupported(String annotationName);
	public ACardComponent getCard(String annotationName);
}
