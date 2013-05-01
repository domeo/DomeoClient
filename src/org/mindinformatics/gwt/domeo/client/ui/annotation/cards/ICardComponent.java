package org.mindinformatics.gwt.domeo.client.ui.annotation.cards;

import org.mindinformatics.gwt.domeo.client.ui.popup.CurationPopup;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public interface ICardComponent {

	public void initializeCard(CurationPopup curationPopup, MAnnotation annotation);
	public void initializeCard(int index, CurationPopup curationPopup, MAnnotation annotation);
	public void setSpan(Element element);
	public Widget getCard();
	public void refresh();
}
